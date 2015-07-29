package WayofTime.alchemicalWizardry.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.ColourAndCoords;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import WayofTime.alchemicalWizardry.common.tileEntity.TEOrientable;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPedestal;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPlinth;
import WayofTime.alchemicalWizardry.common.tileEntity.TEReagentConduit;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public enum NewPacketHandler
{
    INSTANCE;

    private EnumMap<Side, FMLEmbeddedChannel> channels;

    NewPacketHandler()
    {
        this.channels = NetworkRegistry.INSTANCE.newChannel("BloodMagic", new TEAltarCodec());
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            addClientHandler();
        }
        if(FMLCommonHandler.instance().getSide() == Side.SERVER)
        {
        	System.out.println("Server sided~");
        	addServerHandler();
        }
    }

    @SideOnly(Side.CLIENT)
    private void addClientHandler()
    {
        FMLEmbeddedChannel clientChannel = this.channels.get(Side.CLIENT);

        String tileAltarCodec = clientChannel.findChannelHandlerNameForType(TEAltarCodec.class);
        clientChannel.pipeline().addAfter(tileAltarCodec, "TEAltarHandler", new TEAltarMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TEOrientableHandler", new TEOrientableMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TEPedestalHandler", new TEPedestalMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TEPlinthHandler", new TEPlinthMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TESocketHandler", new TESocketMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TETeleposerHandler", new TETeleposerMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TEWritingTableHandler", new TEWritingTableMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "ParticleHandler", new ParticleMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "VelocityHandler", new VelocityMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TEMasterStoneHandler", new TEMasterStoneMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "TEReagentConduitHandler", new TEReagentConduitMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "CurrentLPMessageHandler", new CurrentLPMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "CurrentReagentBarMessageHandler", new CurrentReagentBarMessageHandler());
        clientChannel.pipeline().addAfter(tileAltarCodec, "CurrentAddedHPMessageHandler", new CurrentAddedHPMessageHandler());
    }
    
    @SideOnly(Side.SERVER)
	private void addServerHandler()
	{
        FMLEmbeddedChannel serverChannel = this.channels.get(Side.SERVER);

		String messageCodec = serverChannel.findChannelHandlerNameForType(TEAltarCodec.class);
		serverChannel.pipeline().addAfter(messageCodec, "KeyboardMessageHandler", new KeyboardMessageHandler());
	}

    private static class TEAltarMessageHandler extends SimpleChannelInboundHandler<TEAltarMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEAltarMessage msg) throws Exception
        {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.pos);
            if (te instanceof TEAltar)
            {
                TEAltar altar = (TEAltar) te;

                altar.handlePacketData(msg.items, msg.fluids, msg.capacity);
            }
        }
    }

    private static class TEOrientableMessageHandler extends SimpleChannelInboundHandler<TEOrientableMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEOrientableMessage msg) throws Exception
        {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.pos);
            if (te instanceof TEOrientable)
            {
                ((TEOrientable) te).setInputDirection(EnumFacing.getFront(msg.input));
                ((TEOrientable) te).setOutputDirection(EnumFacing.getFront(msg.output));
            }
        }
    }

    private static class TEPedestalMessageHandler extends SimpleChannelInboundHandler<TEPedestalMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEPedestalMessage msg) throws Exception
        {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.pos);
            if (te instanceof TEPedestal)
            {
                TEPedestal pedestal = (TEPedestal) te;

                pedestal.handlePacketData(msg.items);
            }
        }
    }

    private static class TEPlinthMessageHandler extends SimpleChannelInboundHandler<TEPlinthMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEPlinthMessage msg) throws Exception
        {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.pos);
            if (te instanceof TEPlinth)
            {
                TEPlinth Plinth = (TEPlinth) te;

                Plinth.handlePacketData(msg.items);
            }
        }
    }

    private static class TESocketMessageHandler extends SimpleChannelInboundHandler<TESocketMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TESocketMessage msg) throws Exception
        {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.pos);
            if (te instanceof TESocket)
            {
                TESocket Socket = (TESocket) te;

                Socket.handlePacketData(msg.items);
            }
        }
    }

    private static class TETeleposerMessageHandler extends SimpleChannelInboundHandler<TETeleposerMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TETeleposerMessage msg) throws Exception
        {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.pos);
            if (te instanceof TETeleposer)
            {
                TETeleposer Teleposer = (TETeleposer) te;

                Teleposer.handlePacketData(msg.items);
            }
        }
    }

    private static class TEWritingTableMessageHandler extends SimpleChannelInboundHandler<TEWritingTableMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEWritingTableMessage msg) throws Exception
        {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.pos);
            if (te instanceof TEWritingTable)
            {
                TEWritingTable WritingTable = (TEWritingTable) te;

                WritingTable.handlePacketData(msg.items);
            }
        }
    }

    private static class ParticleMessageHandler extends SimpleChannelInboundHandler<ParticleMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ParticleMessage msg) throws Exception
        {
            World world = AlchemicalWizardry.proxy.getClientWorld();

            world.spawnParticle(EnumParticleTypes.func_179342_a(msg.particle), msg.xCoord, msg.yCoord, msg.zCoord, msg.xVel, msg.yVel, msg.zVel);
        }
    }

    private static class VelocityMessageHandler extends SimpleChannelInboundHandler<VelocityMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, VelocityMessage msg) throws Exception
        {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;

            if (player != null)
            {
                player.motionX = msg.xVel;
                player.motionY = msg.yVel;
                player.motionZ = msg.zVel;
            }
        }
    }

    private static class TEMasterStoneMessageHandler extends SimpleChannelInboundHandler<TEMasterStoneMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEMasterStoneMessage msg) throws Exception
        {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.pos);
            if (te instanceof TEMasterStone)
            {
                TEMasterStone masterStone = (TEMasterStone) te;

                masterStone.setCurrentRitual(msg.ritual);
                masterStone.isRunning = msg.isRunning;
            }
        }
    }

    private static class TEReagentConduitMessageHandler extends SimpleChannelInboundHandler<TEReagentConduitMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEReagentConduitMessage msg) throws Exception
        {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.pos);
            if (te instanceof TEReagentConduit)
            {
                TEReagentConduit reagentConduit = (TEReagentConduit) te;

                reagentConduit.destinationList = msg.destinationList;
            }
        }
    }
    
    private static class CurrentLPMessageHandler extends SimpleChannelInboundHandler<CurrentLPMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, CurrentLPMessage msg) throws Exception
        {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            
            APISpellHelper.setPlayerLPTag(player, msg.currentLP);
            APISpellHelper.setPlayerMaxLPTag(player, msg.maxLP);
        }
    }
    
    private static class CurrentReagentBarMessageHandler extends SimpleChannelInboundHandler<CurrentReagentBarMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, CurrentReagentBarMessage msg) throws Exception
        {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            
            APISpellHelper.setPlayerReagentType(player, msg.reagent);
            APISpellHelper.setPlayerCurrentReagentAmount(player, msg.currentAR);
            APISpellHelper.setPlayerMaxReagentAmount(player, msg.maxAR);
        }
    }
    
    private static class CurrentAddedHPMessageHandler extends SimpleChannelInboundHandler<CurrentAddedHPMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, CurrentAddedHPMessage msg) throws Exception
        {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            
            APISpellHelper.setCurrentAdditionalHP(player, msg.currentHP);
            APISpellHelper.setCurrentAdditionalMaxHP(player, msg.maxHP);
        }
    }
    
    private static class KeyboardMessageHandler extends SimpleChannelInboundHandler<KeyboardMessage>
    {
    	public KeyboardMessageHandler()
    	{
    		System.out.println("I am being created");
    	}
    	@Override
        protected void channelRead0(ChannelHandlerContext ctx, KeyboardMessage msg) throws Exception
        {
    		System.out.println("Hmmm");
    		
        }
    }

    public static class BMMessage
    {
        int index;
    }

    public static class TEAltarMessage extends BMMessage
    {
        BlockPos pos;

        int[] items;
        int[] fluids;
        int capacity;
    }

    public static class TEOrientableMessage extends BMMessage
    {
    	BlockPos pos;

        int input;
        int output;
    }

    public static class TEPedestalMessage extends BMMessage
    {
    	BlockPos pos;

        int[] items;
    }

    public static class TEPlinthMessage extends BMMessage
    {
    	BlockPos pos;

        int[] items;
    }

    public static class TESocketMessage extends BMMessage
    {
    	BlockPos pos;

        int[] items;
    }

    public static class TETeleposerMessage extends BMMessage
    {
    	BlockPos pos;

        int[] items;
    }

    public static class TEWritingTableMessage extends BMMessage
    {
    	BlockPos pos;

        int[] items;
    }

    public static class ParticleMessage extends BMMessage
    {
        int particle;

        double xCoord;
        double yCoord;
        double zCoord;

        double xVel;
        double yVel;
        double zVel;
    }

    public static class VelocityMessage extends BMMessage
    {
        double xVel;
        double yVel;
        double zVel;
    }

    public static class TEMasterStoneMessage extends BMMessage
    {
    	BlockPos pos;

        String ritual;
        boolean isRunning;
    }

    public static class TEReagentConduitMessage extends BMMessage
    {
    	BlockPos pos;

        List<ColourAndCoords> destinationList;
    }
    
    public static class CurrentLPMessage extends BMMessage
    {
    	int currentLP;
    	int maxLP;
    }
    
    public static class CurrentReagentBarMessage extends BMMessage
    {
    	String reagent;
    	float currentAR;
    	float maxAR;
    }
    
    public static class CurrentAddedHPMessage extends BMMessage
    {
    	float currentHP;
    	float maxHP;
    }
    
    public static class KeyboardMessage extends BMMessage
    {
    	byte keyPressed;
    }
    
    private class ClientToServerCodec extends FMLIndexedMessageToMessageCodec<BMMessage>
    {
    	public ClientToServerCodec()
    	{
    	}
    	
		@Override
		public void encodeInto(ChannelHandlerContext ctx, BMMessage msg, ByteBuf target) throws Exception 
		{
			target.writeInt(msg.index);
			
			
//			switch(msg.index)
			{
			
			}
		}

		@Override
		public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, BMMessage msg) 
		{
			int index = source.readInt();
			
			System.out.println("Packet is recieved and being decoded");
			
//			switch(index)
			{
			
			}
		}
    }

    private class TEAltarCodec extends FMLIndexedMessageToMessageCodec<BMMessage>
    {
        public TEAltarCodec()
        {
            addDiscriminator(0, TEAltarMessage.class);
            addDiscriminator(1, TEOrientableMessage.class);
            addDiscriminator(2, TEPedestalMessage.class);
            addDiscriminator(3, TEPlinthMessage.class);
            addDiscriminator(4, TESocketMessage.class);
            addDiscriminator(5, TETeleposerMessage.class);
            addDiscriminator(6, TEWritingTableMessage.class);
            addDiscriminator(7, ParticleMessage.class);
            addDiscriminator(8, VelocityMessage.class);
            addDiscriminator(9, TEMasterStoneMessage.class);
            addDiscriminator(10, TEReagentConduitMessage.class);
            addDiscriminator(11, CurrentLPMessage.class);
            addDiscriminator(12, CurrentReagentBarMessage.class);
            addDiscriminator(13, CurrentAddedHPMessage.class);
            addDiscriminator(14, KeyboardMessage.class);
        }

        @Override
        public void encodeInto(ChannelHandlerContext ctx, BMMessage msg, ByteBuf target) throws Exception
        {
            PacketBuffer newBuffer = new PacketBuffer(target);
            newBuffer.writeInt(msg.index);

            switch (msg.index)
            {
                case 0:
                    newBuffer.writeBlockPos(((TEAltarMessage) msg).pos);

                    newBuffer.writeBoolean(((TEAltarMessage) msg).items != null);
                    if (((TEAltarMessage) msg).items != null)
                    {
                        int[] items = ((TEAltarMessage) msg).items;
                        for (int j = 0; j < items.length; j++)
                        {
                            int i = items[j];
                            newBuffer.writeInt(i);
                        }
                    }

                    newBuffer.writeBoolean(((TEAltarMessage) msg).fluids != null);
                    if (((TEAltarMessage) msg).fluids != null)
                    {
                        int[] fluids = ((TEAltarMessage) msg).fluids;
                        for (int j = 0; j < fluids.length; j++)
                        {
                            int i = fluids[j];
                            newBuffer.writeInt(i);
                        }
                    }

                    newBuffer.writeInt(((TEAltarMessage) msg).capacity);

                    break;

                case 1:
                    newBuffer.writeBlockPos(((TEOrientableMessage) msg).pos);

                    newBuffer.writeInt(((TEOrientableMessage) msg).input);
                    newBuffer.writeInt(((TEOrientableMessage) msg).output);

                    break;

                case 2:
                    newBuffer.writeBlockPos(((TEPedestalMessage) msg).pos);

                    newBuffer.writeBoolean(((TEPedestalMessage) msg).items != null);
                    if (((TEPedestalMessage) msg).items != null)
                    {
                        int[] items = ((TEPedestalMessage) msg).items;
                        for (int j = 0; j < items.length; j++)
                        {
                            int i = items[j];
                            newBuffer.writeInt(i);
                        }
                    }

                    break;

                case 3:
                    newBuffer.writeBlockPos(((TEPlinthMessage) msg).pos);

                    newBuffer.writeBoolean(((TEPlinthMessage) msg).items != null);
                    if (((TEPlinthMessage) msg).items != null)
                    {
                        int[] items = ((TEPlinthMessage) msg).items;
                        for (int j = 0; j < items.length; j++)
                        {
                            int i = items[j];
                            newBuffer.writeInt(i);
                        }
                    }

                    break;

                case 4:
                    newBuffer.writeBlockPos(((TESocketMessage) msg).pos);

                    newBuffer.writeBoolean(((TESocketMessage) msg).items != null);
                    if (((TESocketMessage) msg).items != null)
                    {
                        int[] items = ((TESocketMessage) msg).items;
                        for (int j = 0; j < items.length; j++)
                        {
                            int i = items[j];
                            newBuffer.writeInt(i);
                        }
                    }

                    break;

                case 5:
                    newBuffer.writeBlockPos(((TETeleposerMessage) msg).pos);

                    newBuffer.writeBoolean(((TETeleposerMessage) msg).items != null);
                    if (((TETeleposerMessage) msg).items != null)
                    {
                        int[] items = ((TETeleposerMessage) msg).items;
                        for (int j = 0; j < items.length; j++)
                        {
                            int i = items[j];
                            newBuffer.writeInt(i);
                        }
                    }

                    break;

                case 6:
                    newBuffer.writeBlockPos(((TEWritingTableMessage) msg).pos);

                    newBuffer.writeBoolean(((TEWritingTableMessage) msg).items != null);
                    if (((TEWritingTableMessage) msg).items != null)
                    {
                        int[] items = ((TEWritingTableMessage) msg).items;
                        for (int j = 0; j < items.length; j++)
                        {
                            int i = items[j];
                            newBuffer.writeInt(i);
                        }
                    }

                    break;

                case 7:
                    newBuffer.writeInt(((ParticleMessage) msg).particle);

                    newBuffer.writeDouble(((ParticleMessage) msg).xCoord);
                    newBuffer.writeDouble(((ParticleMessage) msg).yCoord);
                    newBuffer.writeDouble(((ParticleMessage) msg).zCoord);

                    newBuffer.writeDouble(((ParticleMessage) msg).xVel);
                    newBuffer.writeDouble(((ParticleMessage) msg).yVel);
                    newBuffer.writeDouble(((ParticleMessage) msg).zVel);

                    break;

                case 8:
                    newBuffer.writeDouble(((VelocityMessage) msg).xVel);
                    newBuffer.writeDouble(((VelocityMessage) msg).yVel);
                    newBuffer.writeDouble(((VelocityMessage) msg).zVel);

                    break;

                case 9:
                    newBuffer.writeBlockPos(((TEMasterStoneMessage) msg).pos);

                    String ritual = ((TEMasterStoneMessage) msg).ritual;
                    newBuffer.writeInt(ritual.length());
                    for (int i = 0; i < ritual.length(); i++)
                    {
                        newBuffer.writeChar(ritual.charAt(i));
                    }

                    newBuffer.writeBoolean(((TEMasterStoneMessage) msg).isRunning);

                    break;

                case 10:
                    newBuffer.writeBlockPos(((TEReagentConduitMessage) msg).pos);

                    List<ColourAndCoords> list = ((TEReagentConduitMessage) msg).destinationList;
                    newBuffer.writeInt(list.size());

                    for (ColourAndCoords colourSet : list)
                    {
                        newBuffer.writeInt(colourSet.colourRed);
                        newBuffer.writeInt(colourSet.colourGreen);
                        newBuffer.writeInt(colourSet.colourBlue);
                        newBuffer.writeInt(colourSet.colourIntensity);
                        newBuffer.writeInt(colourSet.xCoord);
                        newBuffer.writeInt(colourSet.yCoord);
                        newBuffer.writeInt(colourSet.zCoord);
                    }

                    break;
                    
                case 11:
                	newBuffer.writeInt(((CurrentLPMessage) msg).currentLP);
                	newBuffer.writeInt(((CurrentLPMessage) msg).maxLP);
                	
                	break;
                	
                case 12:
                	char[] charSet = ((CurrentReagentBarMessage)msg).reagent.toCharArray();
                	newBuffer.writeInt(charSet.length);
                	for(char cha : charSet)
                	{
                		newBuffer.writeChar(cha);
                	}
                	newBuffer.writeFloat(((CurrentReagentBarMessage)msg).currentAR);
                	newBuffer.writeFloat(((CurrentReagentBarMessage)msg).maxAR);
                	
                	break;
                	
                case 13:
                	newBuffer.writeFloat(((CurrentAddedHPMessage) msg).currentHP);
                	newBuffer.writeFloat(((CurrentAddedHPMessage) msg).maxHP);
                	
                	break;
                	
                case 14:
        			System.out.println("Packet is being encoded");

    				newBuffer.writeByte(((KeyboardMessage)msg).keyPressed);
    				break;
            }
        }


        @Override
        public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, BMMessage msg)
        {
            PacketBuffer newBuffer = new PacketBuffer(dat);
            int index = newBuffer.readInt();

            switch (index)
            {
                case 0:
                    ((TEAltarMessage) msg).pos = newBuffer.readBlockPos();
                    boolean hasStacks = newBuffer.readBoolean();

                    ((TEAltarMessage) msg).items = new int[TEAltar.sizeInv * 3];
                    if (hasStacks)
                    {
                        ((TEAltarMessage) msg).items = new int[TEAltar.sizeInv * 3];
                        for (int i = 0; i < ((TEAltarMessage) msg).items.length; i++)
                        {
                            ((TEAltarMessage) msg).items[i] = newBuffer.readInt();
                        }
                    }

                    boolean hasFluids = newBuffer.readBoolean();
                    ((TEAltarMessage) msg).fluids = new int[6];
                    if (hasFluids)
                        for (int i = 0; i < ((TEAltarMessage) msg).fluids.length; i++)
                        {
                            ((TEAltarMessage) msg).fluids[i] = newBuffer.readInt();
                        }

                    ((TEAltarMessage) msg).capacity = newBuffer.readInt();

                    break;

                case 1:
                    ((TEOrientableMessage) msg).pos = newBuffer.readBlockPos();

                    ((TEOrientableMessage) msg).input = newBuffer.readInt();
                    ((TEOrientableMessage) msg).output = newBuffer.readInt();

                    break;

                case 2:
                    ((TEPedestalMessage) msg).pos = newBuffer.readBlockPos();

                    boolean hasStacks1 = newBuffer.readBoolean();

                    ((TEPedestalMessage) msg).items = new int[TEPedestal.sizeInv * 3];
                    if (hasStacks1)
                    {
                        ((TEPedestalMessage) msg).items = new int[TEPedestal.sizeInv * 3];
                        for (int i = 0; i < ((TEPedestalMessage) msg).items.length; i++)
                        {
                            ((TEPedestalMessage) msg).items[i] = newBuffer.readInt();
                        }
                    }

                    break;

                case 3:
                    ((TEPlinthMessage) msg).pos = newBuffer.readBlockPos();

                    boolean hasStacks2 = newBuffer.readBoolean();

                    ((TEPlinthMessage) msg).items = new int[TEPlinth.sizeInv * 3];
                    if (hasStacks2)
                    {
                        ((TEPlinthMessage) msg).items = new int[TEPlinth.sizeInv * 3];
                        for (int i = 0; i < ((TEPlinthMessage) msg).items.length; i++)
                        {
                            ((TEPlinthMessage) msg).items[i] = newBuffer.readInt();
                        }
                    }

                    break;

                case 4:
                    ((TESocketMessage) msg).pos = newBuffer.readBlockPos();

                    boolean hasStacks3 = newBuffer.readBoolean();

                    ((TESocketMessage) msg).items = new int[TESocket.sizeInv * 3];
                    if (hasStacks3)
                    {
                        ((TESocketMessage) msg).items = new int[TESocket.sizeInv * 3];
                        for (int i = 0; i < ((TESocketMessage) msg).items.length; i++)
                        {
                            ((TESocketMessage) msg).items[i] = newBuffer.readInt();
                        }
                    }

                    break;

                case 5:
                    ((TETeleposerMessage) msg).pos = newBuffer.readBlockPos();

                    boolean hasStacks4 = newBuffer.readBoolean();

                    ((TETeleposerMessage) msg).items = new int[TETeleposer.sizeInv * 3];
                    if (hasStacks4)
                    {
                        ((TETeleposerMessage) msg).items = new int[TETeleposer.sizeInv * 3];
                        for (int i = 0; i < ((TETeleposerMessage) msg).items.length; i++)
                        {
                            ((TETeleposerMessage) msg).items[i] = newBuffer.readInt();
                        }
                    }

                    break;

                case 6:
                    ((TEWritingTableMessage) msg).pos = newBuffer.readBlockPos();

                    boolean hasStacks5 = newBuffer.readBoolean();

                    ((TEWritingTableMessage) msg).items = new int[TEWritingTable.sizeInv * 3];
                    if (hasStacks5)
                    {
                        ((TEWritingTableMessage) msg).items = new int[TEWritingTable.sizeInv * 3];
                        for (int i = 0; i < ((TEWritingTableMessage) msg).items.length; i++)
                        {
                            ((TEWritingTableMessage) msg).items[i] = newBuffer.readInt();
                        }
                    }

                    break;

                case 7:
                    ((ParticleMessage) msg).particle = newBuffer.readInt();

                    ((ParticleMessage) msg).xCoord = newBuffer.readDouble();
                    ((ParticleMessage) msg).yCoord = newBuffer.readDouble();
                    ((ParticleMessage) msg).zCoord = newBuffer.readDouble();

                    ((ParticleMessage) msg).xVel = newBuffer.readDouble();
                    ((ParticleMessage) msg).yVel = newBuffer.readDouble();
                    ((ParticleMessage) msg).zVel = newBuffer.readDouble();

                    break;

                case 8:
                    ((VelocityMessage) msg).xVel = newBuffer.readDouble();
                    ((VelocityMessage) msg).yVel = newBuffer.readDouble();
                    ((VelocityMessage) msg).zVel = newBuffer.readDouble();

                    break;

                case 9:
                    ((TEMasterStoneMessage) msg).pos = newBuffer.readBlockPos();

                    int ritualStrSize = newBuffer.readInt();
                    String ritual = "";

                    for (int i = 0; i < ritualStrSize; i++)
                    {
                        ritual = ritual + newBuffer.readChar();
                    }

                    ((TEMasterStoneMessage) msg).ritual = ritual;
                    ((TEMasterStoneMessage) msg).isRunning = newBuffer.readBoolean();

                    break;

                case 10:
                    ((TEReagentConduitMessage) msg).pos = newBuffer.readBlockPos();
                    
                    int listSize = newBuffer.readInt();

                    List<ColourAndCoords> list = new LinkedList();

                    for (int i = 0; i < listSize; i++)
                    {
                        list.add(new ColourAndCoords(newBuffer.readInt(), newBuffer.readInt(), newBuffer.readInt(), newBuffer.readInt(), newBuffer.readInt(), newBuffer.readInt(), newBuffer.readInt()));
                    }

                    ((TEReagentConduitMessage) msg).destinationList = list;

                    break;
                    
                case 11:
                	((CurrentLPMessage) msg).currentLP = newBuffer.readInt();
                	((CurrentLPMessage) msg).maxLP = newBuffer.readInt();

                	break;
                	
                case 12:
                	int size1 = newBuffer.readInt();
                	String str1 = "";
                	for(int i=0; i<size1; i++)
                	{
                		str1 = str1 + newBuffer.readChar();
                	}
                	
                	((CurrentReagentBarMessage) msg).reagent = str1;
                	((CurrentReagentBarMessage) msg).currentAR = newBuffer.readFloat();
                	((CurrentReagentBarMessage) msg).maxAR = newBuffer.readFloat();
                	
                	break;
                	
                case 13:
                	((CurrentAddedHPMessage) msg).currentHP = newBuffer.readFloat();
                	((CurrentAddedHPMessage) msg).maxHP = newBuffer.readFloat();

                	break;
                	
                case 14:
                	System.out.println("Packet recieved: being decoded");
    				((KeyboardMessage)msg).keyPressed = newBuffer.readByte();
    				break;
            }
        }
    }

    //Packets to be obtained
    public static Packet getPacket(TEAltar tileAltar)
    {
        TEAltarMessage msg = new TEAltarMessage();
        msg.index = 0;
        msg.pos = tileAltar.getPos();
        msg.items = tileAltar.buildIntDataList();
        msg.fluids = tileAltar.buildFluidList();
        msg.capacity = tileAltar.getCapacity();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEOrientable tileOrientable)
    {
        TEOrientableMessage msg = new TEOrientableMessage();
        msg.index = 1;
        msg.pos = tileOrientable.getPos();
        msg.input = tileOrientable.getIntForEnumFacing(tileOrientable.getInputDirection());
        msg.output = tileOrientable.getIntForEnumFacing(tileOrientable.getOutputDirection());

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEPedestal tilePedestal)
    {
        TEPedestalMessage msg = new TEPedestalMessage();
        msg.index = 2;
        msg.pos = tilePedestal.getPos();
        msg.items = tilePedestal.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEPlinth tilePlinth)
    {
        TEPlinthMessage msg = new TEPlinthMessage();
        msg.index = 3;
        msg.pos = tilePlinth.getPos();
        msg.items = tilePlinth.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TESocket tileSocket)
    {
        TESocketMessage msg = new TESocketMessage();
        msg.index = 4;
        msg.pos = tileSocket.getPos();
        msg.items = tileSocket.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TETeleposer tileTeleposer)
    {
        TETeleposerMessage msg = new TETeleposerMessage();
        msg.index = 5;
        msg.pos = tileTeleposer.getPos();
        msg.items = tileTeleposer.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEWritingTable tileWritingTable)
    {
        TEWritingTableMessage msg = new TEWritingTableMessage();
        msg.index = 6;
        msg.pos = tileWritingTable.getPos();
        msg.items = tileWritingTable.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getParticlePacket(EnumParticleTypes type, double xCoord, double yCoord, double zCoord, double xVel, double yVel, double zVel)
    {
        ParticleMessage msg = new ParticleMessage();
        msg.index = 7;
        msg.particle = type.ordinal();
        msg.xCoord = xCoord;
        msg.yCoord = yCoord;
        msg.zCoord = zCoord;
        msg.xVel = xVel;
        msg.yVel = yVel;
        msg.zVel = zVel;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getVelSettingPacket(double xVel, double yVel, double zVel)
    {
        VelocityMessage msg = new VelocityMessage();
        msg.index = 8;
        msg.xVel = xVel;
        msg.yVel = yVel;
        msg.zVel = zVel;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEMasterStone tile)
    {
        TEMasterStoneMessage msg = new TEMasterStoneMessage();
        msg.index = 9;
        msg.pos = tile.getPos();

        msg.ritual = tile.getCurrentRitual();
        msg.isRunning = tile.isRunning;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEReagentConduit tile)
    {
        TEReagentConduitMessage msg = new TEReagentConduitMessage();
        msg.index = 10;
        msg.pos = tile.getPos();

        msg.destinationList = tile.destinationList;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }
    
    public static Packet getLPPacket(int curLP, int maxLP)
    {
    	CurrentLPMessage msg = new CurrentLPMessage();
        msg.index = 11;
        msg.currentLP = curLP;
        msg.maxLP = maxLP;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }
    
    public static Packet getReagentBarPacket(Reagent reagent, float curAR, float maxAR)
    {
    	CurrentReagentBarMessage msg = new CurrentReagentBarMessage();
    	msg.index = 12;
    	msg.reagent = ReagentRegistry.getKeyForReagent(reagent);
    	msg.currentAR = curAR;
    	msg.maxAR = maxAR;
    	
    	return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }
    
    public static Packet getAddedHPPacket(float health, float maxHP)
    {
    	CurrentAddedHPMessage msg = new CurrentAddedHPMessage();
        msg.index = 13;
        msg.currentHP = health;
        msg.maxHP = maxHP;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }
    
    public static Packet getKeyboardPressPacket(byte bt)
    {
    	KeyboardMessage msg = new KeyboardMessage();
    	msg.index = 14;
    	msg.keyPressed = bt;
    	
    	System.out.println("Packet is being created");
    	
    	return INSTANCE.channels.get(Side.CLIENT).generatePacketFrom(msg);
    }

    public void sendTo(Packet message, EntityPlayerMP player)
    {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToAll(Packet message)
    {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToAllAround(Packet message, NetworkRegistry.TargetPoint point)
    {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }
    
    public void sendToServer(Packet message)
    {
        this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.channels.get(Side.CLIENT).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}
