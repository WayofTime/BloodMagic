package WayofTime.alchemicalWizardry.common;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.ColourAndCoords;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles the packet wrangling for IronChest
 *
 * @author cpw
 */
public enum NewPacketHandler
{
    INSTANCE;

    /**
     * Our channel "pair" from {@link NetworkRegistry}
     */
    private EnumMap<Side, FMLEmbeddedChannel> channels;


    /**
     * Make our packet handler, and add an {@link IronChestCodec} always
     */
    private NewPacketHandler()
    {
        // request a channel pair for IronChest from the network registry
        // Add the IronChestCodec as a member of both channel pipelines
        this.channels = NetworkRegistry.INSTANCE.newChannel("BloodMagic", new TEAltarCodec());
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            addClientHandler();
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
    }


    /**
     * This class simply handles the {@link IronChestMessage} when it's received
     * at the client side It can contain client only code, because it's only run
     * on the client.
     *
     * @author cpw
     */
    private static class TEAltarMessageHandler extends SimpleChannelInboundHandler<TEAltarMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEAltarMessage msg) throws Exception
        {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
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
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
            if (te instanceof TEOrientable)
            {
                TEOrientable tile = (TEOrientable) te;

                ((TEOrientable) te).setInputDirection(ForgeDirection.getOrientation(msg.input));
                ((TEOrientable) te).setOutputDirection(ForgeDirection.getOrientation(msg.output));
            }
        }
    }

    private static class TEPedestalMessageHandler extends SimpleChannelInboundHandler<TEPedestalMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TEPedestalMessage msg) throws Exception
        {
            World world = AlchemicalWizardry.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
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
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
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
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
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
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
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
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
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

            world.spawnParticle(msg.particle, msg.xCoord, msg.yCoord, msg.zCoord, msg.xVel, msg.yVel, msg.zVel);
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
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
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
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
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

    public static class BMMessage
    {
        int index;
    }

    public static class TEAltarMessage extends BMMessage
    {
        int x;
        int y;
        int z;

        int[] items;
        int[] fluids;
        int capacity;
    }

    public static class TEOrientableMessage extends BMMessage
    {
        int x;
        int y;
        int z;

        int input;
        int output;
    }

    public static class TEPedestalMessage extends BMMessage
    {
        int x;
        int y;
        int z;

        int[] items;
    }

    public static class TEPlinthMessage extends BMMessage
    {
        int x;
        int y;
        int z;

        int[] items;
    }

    public static class TESocketMessage extends BMMessage
    {
        int x;
        int y;
        int z;

        int[] items;
    }

    public static class TETeleposerMessage extends BMMessage
    {
        int x;
        int y;
        int z;

        int[] items;
    }

    public static class TEWritingTableMessage extends BMMessage
    {
        int x;
        int y;
        int z;

        int[] items;
    }

    public static class ParticleMessage extends BMMessage
    {
        String particle;

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
        int x;
        int y;
        int z;

        String ritual;
        boolean isRunning;
    }

    public static class TEReagentConduitMessage extends BMMessage
    {
        int x;
        int y;
        int z;

        List<ColourAndCoords> destinationList;
    }
    
    public static class CurrentLPMessage extends BMMessage
    {
    	int currentLP;
    	int maxLP;
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
        }

        @Override
        public void encodeInto(ChannelHandlerContext ctx, BMMessage msg, ByteBuf target) throws Exception
        {
            target.writeInt(msg.index);

            switch (msg.index)
            {
                case 0:
                    target.writeInt(((TEAltarMessage) msg).x);
                    target.writeInt(((TEAltarMessage) msg).y);
                    target.writeInt(((TEAltarMessage) msg).z);

                    target.writeBoolean(((TEAltarMessage) msg).items != null);
                    if (((TEAltarMessage) msg).items != null)
                    {
                        int[] items = ((TEAltarMessage) msg).items;
                        for (int j = 0; j < items.length; j++)
                        {
                            int i = items[j];
                            target.writeInt(i);
                        }
                    }

                    target.writeBoolean(((TEAltarMessage) msg).fluids != null);
                    if (((TEAltarMessage) msg).fluids != null)
                    {
                        int[] fluids = ((TEAltarMessage) msg).fluids;
                        for (int j = 0; j < fluids.length; j++)
                        {
                            int i = fluids[j];
                            target.writeInt(i);
                        }
                    }

                    target.writeInt(((TEAltarMessage) msg).capacity);

                    break;

                case 1:
                    target.writeInt(((TEOrientableMessage) msg).x);
                    target.writeInt(((TEOrientableMessage) msg).y);
                    target.writeInt(((TEOrientableMessage) msg).z);

                    target.writeInt(((TEOrientableMessage) msg).input);
                    target.writeInt(((TEOrientableMessage) msg).output);

                    break;

                case 2:
                    target.writeInt(((TEPedestalMessage) msg).x);
                    target.writeInt(((TEPedestalMessage) msg).y);
                    target.writeInt(((TEPedestalMessage) msg).z);

                    target.writeBoolean(((TEPedestalMessage) msg).items != null);
                    if (((TEPedestalMessage) msg).items != null)
                    {
                        int[] items = ((TEPedestalMessage) msg).items;
                        for (int j = 0; j < items.length; j++)
                        {
                            int i = items[j];
                            target.writeInt(i);
                        }
                    }

                    break;

                case 3:
                    target.writeInt(((TEPlinthMessage) msg).x);
                    target.writeInt(((TEPlinthMessage) msg).y);
                    target.writeInt(((TEPlinthMessage) msg).z);

                    target.writeBoolean(((TEPlinthMessage) msg).items != null);
                    if (((TEPlinthMessage) msg).items != null)
                    {
                        int[] items = ((TEPlinthMessage) msg).items;
                        for (int j = 0; j < items.length; j++)
                        {
                            int i = items[j];
                            target.writeInt(i);
                        }
                    }

                    break;

                case 4:
                    target.writeInt(((TESocketMessage) msg).x);
                    target.writeInt(((TESocketMessage) msg).y);
                    target.writeInt(((TESocketMessage) msg).z);

                    target.writeBoolean(((TESocketMessage) msg).items != null);
                    if (((TESocketMessage) msg).items != null)
                    {
                        int[] items = ((TESocketMessage) msg).items;
                        for (int j = 0; j < items.length; j++)
                        {
                            int i = items[j];
                            target.writeInt(i);
                        }
                    }

                    break;

                case 5:
                    target.writeInt(((TETeleposerMessage) msg).x);
                    target.writeInt(((TETeleposerMessage) msg).y);
                    target.writeInt(((TETeleposerMessage) msg).z);

                    target.writeBoolean(((TETeleposerMessage) msg).items != null);
                    if (((TETeleposerMessage) msg).items != null)
                    {
                        int[] items = ((TETeleposerMessage) msg).items;
                        for (int j = 0; j < items.length; j++)
                        {
                            int i = items[j];
                            target.writeInt(i);
                        }
                    }

                    break;

                case 6:
                    target.writeInt(((TEWritingTableMessage) msg).x);
                    target.writeInt(((TEWritingTableMessage) msg).y);
                    target.writeInt(((TEWritingTableMessage) msg).z);

                    target.writeBoolean(((TEWritingTableMessage) msg).items != null);
                    if (((TEWritingTableMessage) msg).items != null)
                    {
                        int[] items = ((TEWritingTableMessage) msg).items;
                        for (int j = 0; j < items.length; j++)
                        {
                            int i = items[j];
                            target.writeInt(i);
                        }
                    }

                    break;

                case 7:
                    String str = ((ParticleMessage) msg).particle;
                    target.writeInt(str.length());
                    for (int i = 0; i < str.length(); i++)
                    {
                        target.writeChar(str.charAt(i));
                    }

                    target.writeDouble(((ParticleMessage) msg).xCoord);
                    target.writeDouble(((ParticleMessage) msg).yCoord);
                    target.writeDouble(((ParticleMessage) msg).zCoord);

                    target.writeDouble(((ParticleMessage) msg).xVel);
                    target.writeDouble(((ParticleMessage) msg).yVel);
                    target.writeDouble(((ParticleMessage) msg).zVel);

                    break;

                case 8:
                    target.writeDouble(((VelocityMessage) msg).xVel);
                    target.writeDouble(((VelocityMessage) msg).yVel);
                    target.writeDouble(((VelocityMessage) msg).zVel);

                    break;

                case 9:
                    target.writeInt(((TEMasterStoneMessage) msg).x);
                    target.writeInt(((TEMasterStoneMessage) msg).y);
                    target.writeInt(((TEMasterStoneMessage) msg).z);

                    String ritual = ((TEMasterStoneMessage) msg).ritual;
                    target.writeInt(ritual.length());
                    for (int i = 0; i < ritual.length(); i++)
                    {
                        target.writeChar(ritual.charAt(i));
                    }

                    target.writeBoolean(((TEMasterStoneMessage) msg).isRunning);

                    break;

                case 10:
                    target.writeInt(((TEReagentConduitMessage) msg).x);
                    target.writeInt(((TEReagentConduitMessage) msg).y);
                    target.writeInt(((TEReagentConduitMessage) msg).z);

                    List<ColourAndCoords> list = ((TEReagentConduitMessage) msg).destinationList;
                    target.writeInt(list.size());

                    for (ColourAndCoords colourSet : list)
                    {
                        target.writeInt(colourSet.colourRed);
                        target.writeInt(colourSet.colourGreen);
                        target.writeInt(colourSet.colourBlue);
                        target.writeInt(colourSet.colourIntensity);
                        target.writeInt(colourSet.xCoord);
                        target.writeInt(colourSet.yCoord);
                        target.writeInt(colourSet.zCoord);
                    }

                    break;
                    
                case 11:
                	target.writeInt(((CurrentLPMessage) msg).currentLP);
                	target.writeInt(((CurrentLPMessage) msg).maxLP);
                	
                	break;
            }
        }


        @Override
        public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, BMMessage msg)
        {
            int index = dat.readInt();

            switch (index)
            {
                case 0:
                    ((TEAltarMessage) msg).x = dat.readInt();
                    ((TEAltarMessage) msg).y = dat.readInt();
                    ((TEAltarMessage) msg).z = dat.readInt();
                    boolean hasStacks = dat.readBoolean();

                    ((TEAltarMessage) msg).items = new int[TEAltar.sizeInv * 3];
                    if (hasStacks)
                    {
                        ((TEAltarMessage) msg).items = new int[TEAltar.sizeInv * 3];
                        for (int i = 0; i < ((TEAltarMessage) msg).items.length; i++)
                        {
                            ((TEAltarMessage) msg).items[i] = dat.readInt();
                        }
                    }

                    boolean hasFluids = dat.readBoolean();
                    ((TEAltarMessage) msg).fluids = new int[6];
                    if (hasFluids)
                        for (int i = 0; i < ((TEAltarMessage) msg).fluids.length; i++)
                        {
                            ((TEAltarMessage) msg).fluids[i] = dat.readInt();
                        }

                    ((TEAltarMessage) msg).capacity = dat.readInt();

                    break;

                case 1:
                    ((TEOrientableMessage) msg).x = dat.readInt();
                    ((TEOrientableMessage) msg).y = dat.readInt();
                    ((TEOrientableMessage) msg).z = dat.readInt();

                    ((TEOrientableMessage) msg).input = dat.readInt();
                    ((TEOrientableMessage) msg).output = dat.readInt();

                    break;

                case 2:
                    ((TEPedestalMessage) msg).x = dat.readInt();
                    ((TEPedestalMessage) msg).y = dat.readInt();
                    ((TEPedestalMessage) msg).z = dat.readInt();

                    boolean hasStacks1 = dat.readBoolean();

                    ((TEPedestalMessage) msg).items = new int[TEPedestal.sizeInv * 3];
                    if (hasStacks1)
                    {
                        ((TEPedestalMessage) msg).items = new int[TEPedestal.sizeInv * 3];
                        for (int i = 0; i < ((TEPedestalMessage) msg).items.length; i++)
                        {
                            ((TEPedestalMessage) msg).items[i] = dat.readInt();
                        }
                    }

                    break;

                case 3:
                    ((TEPlinthMessage) msg).x = dat.readInt();
                    ((TEPlinthMessage) msg).y = dat.readInt();
                    ((TEPlinthMessage) msg).z = dat.readInt();

                    boolean hasStacks2 = dat.readBoolean();

                    ((TEPlinthMessage) msg).items = new int[TEPlinth.sizeInv * 3];
                    if (hasStacks2)
                    {
                        ((TEPlinthMessage) msg).items = new int[TEPlinth.sizeInv * 3];
                        for (int i = 0; i < ((TEPlinthMessage) msg).items.length; i++)
                        {
                            ((TEPlinthMessage) msg).items[i] = dat.readInt();
                        }
                    }

                    break;

                case 4:
                    ((TESocketMessage) msg).x = dat.readInt();
                    ((TESocketMessage) msg).y = dat.readInt();
                    ((TESocketMessage) msg).z = dat.readInt();

                    boolean hasStacks3 = dat.readBoolean();

                    ((TESocketMessage) msg).items = new int[TESocket.sizeInv * 3];
                    if (hasStacks3)
                    {
                        ((TESocketMessage) msg).items = new int[TESocket.sizeInv * 3];
                        for (int i = 0; i < ((TESocketMessage) msg).items.length; i++)
                        {
                            ((TESocketMessage) msg).items[i] = dat.readInt();
                        }
                    }

                    break;

                case 5:
                    ((TETeleposerMessage) msg).x = dat.readInt();
                    ((TETeleposerMessage) msg).y = dat.readInt();
                    ((TETeleposerMessage) msg).z = dat.readInt();

                    boolean hasStacks4 = dat.readBoolean();

                    ((TETeleposerMessage) msg).items = new int[TETeleposer.sizeInv * 3];
                    if (hasStacks4)
                    {
                        ((TETeleposerMessage) msg).items = new int[TETeleposer.sizeInv * 3];
                        for (int i = 0; i < ((TETeleposerMessage) msg).items.length; i++)
                        {
                            ((TETeleposerMessage) msg).items[i] = dat.readInt();
                        }
                    }

                    break;

                case 6:
                    ((TEWritingTableMessage) msg).x = dat.readInt();
                    ((TEWritingTableMessage) msg).y = dat.readInt();
                    ((TEWritingTableMessage) msg).z = dat.readInt();

                    boolean hasStacks5 = dat.readBoolean();

                    ((TEWritingTableMessage) msg).items = new int[TEWritingTable.sizeInv * 3];
                    if (hasStacks5)
                    {
                        ((TEWritingTableMessage) msg).items = new int[TEWritingTable.sizeInv * 3];
                        for (int i = 0; i < ((TEWritingTableMessage) msg).items.length; i++)
                        {
                            ((TEWritingTableMessage) msg).items[i] = dat.readInt();
                        }
                    }

                    break;

                case 7:
                    int size = dat.readInt();
                    String str = "";

                    for (int i = 0; i < size; i++)
                    {
                        str = str + dat.readChar();
                    }

                    ((ParticleMessage) msg).particle = str;

                    ((ParticleMessage) msg).xCoord = dat.readDouble();
                    ((ParticleMessage) msg).yCoord = dat.readDouble();
                    ((ParticleMessage) msg).zCoord = dat.readDouble();

                    ((ParticleMessage) msg).xVel = dat.readDouble();
                    ((ParticleMessage) msg).yVel = dat.readDouble();
                    ((ParticleMessage) msg).zVel = dat.readDouble();

                    break;

                case 8:
                    ((VelocityMessage) msg).xVel = dat.readDouble();
                    ((VelocityMessage) msg).yVel = dat.readDouble();
                    ((VelocityMessage) msg).zVel = dat.readDouble();

                    break;

                case 9:
                    ((TEMasterStoneMessage) msg).x = dat.readInt();
                    ((TEMasterStoneMessage) msg).y = dat.readInt();
                    ((TEMasterStoneMessage) msg).z = dat.readInt();

                    int ritualStrSize = dat.readInt();
                    String ritual = "";

                    for (int i = 0; i < ritualStrSize; i++)
                    {
                        ritual = ritual + dat.readChar();
                    }

                    ((TEMasterStoneMessage) msg).ritual = ritual;
                    ((TEMasterStoneMessage) msg).isRunning = dat.readBoolean();

                    break;

                case 10:
                    ((TEReagentConduitMessage) msg).x = dat.readInt();
                    ((TEReagentConduitMessage) msg).y = dat.readInt();
                    ((TEReagentConduitMessage) msg).z = dat.readInt();

                    int listSize = dat.readInt();

                    List<ColourAndCoords> list = new LinkedList();

                    for (int i = 0; i < listSize; i++)
                    {
                        list.add(new ColourAndCoords(dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt(), dat.readInt()));
                    }

                    ((TEReagentConduitMessage) msg).destinationList = list;

                    break;
                    
                case 11:
                	((CurrentLPMessage) msg).currentLP = dat.readInt();
                	((CurrentLPMessage) msg).maxLP = dat.readInt();

                	break;
            }
        }
    }

    //Packets to be obtained
    public static Packet getPacket(TEAltar tileAltar)
    {
        TEAltarMessage msg = new TEAltarMessage();
        msg.index = 0;
        msg.x = tileAltar.xCoord;
        msg.y = tileAltar.yCoord;
        msg.z = tileAltar.zCoord;
        msg.items = tileAltar.buildIntDataList();
        msg.fluids = tileAltar.buildFluidList();
        msg.capacity = tileAltar.getCapacity();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEOrientable tileOrientable)
    {
        TEOrientableMessage msg = new TEOrientableMessage();
        msg.index = 1;
        msg.x = tileOrientable.xCoord;
        msg.y = tileOrientable.yCoord;
        msg.z = tileOrientable.zCoord;
        msg.input = tileOrientable.getIntForForgeDirection(tileOrientable.getInputDirection());
        msg.output = tileOrientable.getIntForForgeDirection(tileOrientable.getOutputDirection());

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEPedestal tilePedestal)
    {
        TEPedestalMessage msg = new TEPedestalMessage();
        msg.index = 2;
        msg.x = tilePedestal.xCoord;
        msg.y = tilePedestal.yCoord;
        msg.z = tilePedestal.zCoord;
        msg.items = tilePedestal.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEPlinth tilePlinth)
    {
        TEPlinthMessage msg = new TEPlinthMessage();
        msg.index = 3;
        msg.x = tilePlinth.xCoord;
        msg.y = tilePlinth.yCoord;
        msg.z = tilePlinth.zCoord;
        msg.items = tilePlinth.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TESocket tileSocket)
    {
        TESocketMessage msg = new TESocketMessage();
        msg.index = 4;
        msg.x = tileSocket.xCoord;
        msg.y = tileSocket.yCoord;
        msg.z = tileSocket.zCoord;
        msg.items = tileSocket.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TETeleposer tileTeleposer)
    {
        TETeleposerMessage msg = new TETeleposerMessage();
        msg.index = 5;
        msg.x = tileTeleposer.xCoord;
        msg.y = tileTeleposer.yCoord;
        msg.z = tileTeleposer.zCoord;
        msg.items = tileTeleposer.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEWritingTable tileWritingTable)
    {
        TEWritingTableMessage msg = new TEWritingTableMessage();
        msg.index = 6;
        msg.x = tileWritingTable.xCoord;
        msg.y = tileWritingTable.yCoord;
        msg.z = tileWritingTable.zCoord;
        msg.items = tileWritingTable.buildIntDataList();

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getParticlePacket(String str, double xCoord, double yCoord, double zCoord, double xVel, double yVel, double zVel)
    {
        ParticleMessage msg = new ParticleMessage();
        msg.index = 7;
        msg.particle = str;
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
        msg.x = tile.xCoord;
        msg.y = tile.yCoord;
        msg.z = tile.zCoord;

        msg.ritual = tile.getCurrentRitual();
        msg.isRunning = tile.isRunning;

        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }

    public static Packet getPacket(TEReagentConduit tile)
    {
        TEReagentConduitMessage msg = new TEReagentConduitMessage();
        msg.index = 10;
        msg.x = tile.xCoord;
        msg.y = tile.yCoord;
        msg.z = tile.zCoord;

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
}
