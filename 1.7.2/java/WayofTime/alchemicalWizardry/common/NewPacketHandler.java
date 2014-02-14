package WayofTime.alchemicalWizardry.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.EnumMap;

import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEOrientable;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPedestal;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPlinth;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
* Handles the packet wrangling for IronChest
* @author cpw
*
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
       this.channels = NetworkRegistry.INSTANCE.newChannel("BloodMagic", new TEAltarCodec(), new TEOrientableCodec(), new TEPedestalCodec(), new TEPlinthCodec(), new TESocketCodec());
       if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
       {
           addClientHandler();
       }
   }


   /**
    * This is only called on the client side - it adds an
    * {@link IronChestMessageHandler} to the client side pipeline, since the
    * only place we expect to <em>handle</em> messages is on the client.
    */
   @SideOnly(Side.CLIENT)
   private void addClientHandler() 
   {
       FMLEmbeddedChannel clientChannel = this.channels.get(Side.CLIENT);
       // These two lines find the existing codec (Ironchestcodec) and insert our message handler after it
       // in the pipeline
       String tileAltarCodec = clientChannel.findChannelHandlerNameForType(TEAltarCodec.class);
       clientChannel.pipeline().addAfter(tileAltarCodec, "TEAltarHandler", new TEAltarMessageHandler());
       
       String tileOrientableCodec = clientChannel.findChannelHandlerNameForType(TEOrientableCodec.class);
       clientChannel.pipeline().addAfter(tileOrientableCodec, "TEOrientableHandler", new TEOrientableMessageHandler());
       
       String tilePedestalCodec = clientChannel.findChannelHandlerNameForType(TEPedestalCodec.class);
       clientChannel.pipeline().addAfter(tilePedestalCodec, "TEPedestalHandler", new TEPedestalMessageHandler());
       
       String tilePlinthCodec = clientChannel.findChannelHandlerNameForType(TEPlinthCodec.class);
       clientChannel.pipeline().addAfter(tilePlinthCodec, "TEPlinthHandler", new TEPlinthMessageHandler());
       
       String tileSocketCodec = clientChannel.findChannelHandlerNameForType(TESocketCodec.class);
       clientChannel.pipeline().addAfter(tileSocketCodec, "TESocketHandler", new TESocketMessageHandler());
   }


   /**
    * This class simply handles the {@link IronChestMessage} when it's received
    * at the client side It can contain client only code, because it's only run
    * on the client.
    *
    * @author cpw
    *
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
        	   TEOrientable tile = (TEOrientable)te;

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

   public static class TEAltarMessage
   {
       int x;
       int y;
       int z;

       int[] items;
       int[] fluids;
       int capacity;
   }
   
   public static class TEOrientableMessage
   {
       int x;
       int y;
       int z;
       
       int input;
       int output;
   }
   
   public static class TEPedestalMessage
   {
	   int x;
       int y;
       int z;

       int[] items;
   }
   
   public static class TEPlinthMessage
   {
	   int x;
       int y;
       int z;

       int[] items;
   }
   
   public static class TESocketMessage
   {
	   int x;
       int y;
       int z;

       int[] items;
   }
   
   public static class TETeleposerMessage
   {
	   int x;
       int y;
       int z;

       int[] items;
   }
   
   public static class TEWritingTableMessage
   {
	   int x;
       int y;
       int z;

       int[] items;
   }

   private class TEAltarCodec extends FMLIndexedMessageToMessageCodec<TEAltarMessage>
   {
       public TEAltarCodec()
       {
           addDiscriminator(0, TEAltarMessage.class);
       }
       
       @Override
       public void encodeInto(ChannelHandlerContext ctx, TEAltarMessage msg, ByteBuf target) throws Exception
       {
           target.writeInt(msg.x);
           target.writeInt(msg.y);
           target.writeInt(msg.z);

           target.writeBoolean(msg.items != null);
           if (msg.items != null)
           {
               int[] items = msg.items;
               for (int j = 0; j < items.length; j++)
               {
                   int i = items[j];
                   target.writeInt(i);
               }
           }
           
           if(msg.fluids != null)
           {
        	   int[] fluids = msg.fluids;
        	   for (int j = 0; j < fluids.length; j++)
               {
                   int i = fluids[j];
                   target.writeInt(i);
               }
           }
           
           target.writeInt(msg.capacity);    
       }


       @Override
       public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, TEAltarMessage msg)
       {
           msg.x = dat.readInt();
           msg.y = dat.readInt();
           msg.z = dat.readInt();
           int typDat = dat.readByte();
           boolean hasStacks = dat.readBoolean();
           
           msg.items = new int[TEAltar.sizeInv*3];
           if (hasStacks)
           {
               msg.items = new int[TEAltar.sizeInv*3];
               for (int i = 0; i < msg.items.length; i++)
               {
                   msg.items[i] = dat.readInt();
               }
           }
           
           msg.fluids = new int[6];
           for (int i = 0; i < msg.fluids.length; i++)
           {
               msg.fluids[i] = dat.readInt();
           }
           
           msg.capacity = dat.readInt();
       }
   }
   
   private class TEOrientableCodec extends FMLIndexedMessageToMessageCodec<TEOrientableMessage>
   {
       public TEOrientableCodec()
       {
           addDiscriminator(0, TEOrientableMessage.class);
       }
       
       @Override
       public void encodeInto(ChannelHandlerContext ctx, TEOrientableMessage msg, ByteBuf target) throws Exception
       {
           target.writeInt(msg.x);
           target.writeInt(msg.y);
           target.writeInt(msg.z);

           target.writeInt(msg.input);
           target.writeInt(msg.output);
       }

       @Override
       public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, TEOrientableMessage msg)
       {
           msg.x = dat.readInt();
           msg.y = dat.readInt();
           msg.z = dat.readInt();
           
           msg.input = dat.readInt();
           msg.output = dat.readInt();
       }
   }
   
   private class TEPedestalCodec extends FMLIndexedMessageToMessageCodec<TEPedestalMessage>
   {
       public TEPedestalCodec()
       {
           addDiscriminator(0, TEPedestalMessage.class);
       }
       
       @Override
       public void encodeInto(ChannelHandlerContext ctx, TEPedestalMessage msg, ByteBuf target) throws Exception
       {
           target.writeInt(msg.x);
           target.writeInt(msg.y);
           target.writeInt(msg.z);

           target.writeBoolean(msg.items != null);
           if (msg.items != null)
           {
               int[] items = msg.items;
               for (int j = 0; j < items.length; j++)
               {
                   int i = items[j];
                   target.writeInt(i);
               }
           }
       }

       @Override
       public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, TEPedestalMessage msg)
       {
    	   msg.x = dat.readInt();
           msg.y = dat.readInt();
           msg.z = dat.readInt();
           int typDat = dat.readByte();
           boolean hasStacks = dat.readBoolean();
           
           msg.items = new int[TEPedestal.sizeInv*3];
           if (hasStacks)
           {
               msg.items = new int[TEPedestal.sizeInv*3];
               for (int i = 0; i < msg.items.length; i++)
               {
                   msg.items[i] = dat.readInt();
               }
           }
       }
   }
   
   private class TEPlinthCodec extends FMLIndexedMessageToMessageCodec<TEPlinthMessage>
   {
       public TEPlinthCodec()
       {
           addDiscriminator(0, TEPlinthMessage.class);
       }
       
       @Override
       public void encodeInto(ChannelHandlerContext ctx, TEPlinthMessage msg, ByteBuf target) throws Exception
       {
           target.writeInt(msg.x);
           target.writeInt(msg.y);
           target.writeInt(msg.z);

           target.writeBoolean(msg.items != null);
           if (msg.items != null)
           {
               int[] items = msg.items;
               for (int j = 0; j < items.length; j++)
               {
                   int i = items[j];
                   target.writeInt(i);
               }
           }
       }

       @Override
       public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, TEPlinthMessage msg)
       {
    	   msg.x = dat.readInt();
           msg.y = dat.readInt();
           msg.z = dat.readInt();
           int typDat = dat.readByte();
           boolean hasStacks = dat.readBoolean();
           
           msg.items = new int[TEPlinth.sizeInv*3];
           if (hasStacks)
           {
               msg.items = new int[TEPlinth.sizeInv*3];
               for (int i = 0; i < msg.items.length; i++)
               {
                   msg.items[i] = dat.readInt();
               }
           }
       }
   }
   
   private class TESocketCodec extends FMLIndexedMessageToMessageCodec<TESocketMessage>
   {
       public TESocketCodec()
       {
           addDiscriminator(0, TESocketMessage.class);
       }
       
       @Override
       public void encodeInto(ChannelHandlerContext ctx, TESocketMessage msg, ByteBuf target) throws Exception
       {
           target.writeInt(msg.x);
           target.writeInt(msg.y);
           target.writeInt(msg.z);

           target.writeBoolean(msg.items != null);
           if (msg.items != null)
           {
               int[] items = msg.items;
               for (int j = 0; j < items.length; j++)
               {
                   int i = items[j];
                   target.writeInt(i);
               }
           }
       }

       @Override
       public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, TESocketMessage msg)
       {
    	   msg.x = dat.readInt();
           msg.y = dat.readInt();
           msg.z = dat.readInt();
           int typDat = dat.readByte();
           boolean hasStacks = dat.readBoolean();
           
           msg.items = new int[TESocket.sizeInv*3];
           if (hasStacks)
           {
               msg.items = new int[TESocket.sizeInv*3];
               for (int i = 0; i < msg.items.length; i++)
               {
                   msg.items[i] = dat.readInt();
               }
           }
       }
   }
   
   private class TETeleposerCodec extends FMLIndexedMessageToMessageCodec<TETeleposerMessage>
   {
       public TETeleposerCodec()
       {
           addDiscriminator(0, TETeleposerMessage.class);
       }
       
       @Override
       public void encodeInto(ChannelHandlerContext ctx, TETeleposerMessage msg, ByteBuf target) throws Exception
       {
           target.writeInt(msg.x);
           target.writeInt(msg.y);
           target.writeInt(msg.z);

           target.writeBoolean(msg.items != null);
           if (msg.items != null)
           {
               int[] items = msg.items;
               for (int j = 0; j < items.length; j++)
               {
                   int i = items[j];
                   target.writeInt(i);
               }
           }
       }

       @Override
       public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, TETeleposerMessage msg)
       {
    	   msg.x = dat.readInt();
           msg.y = dat.readInt();
           msg.z = dat.readInt();
           int typDat = dat.readByte();
           boolean hasStacks = dat.readBoolean();
           
           msg.items = new int[TETeleposer.sizeInv*3];
           if (hasStacks)
           {
               msg.items = new int[TETeleposer.sizeInv*3];
               for (int i = 0; i < msg.items.length; i++)
               {
                   msg.items[i] = dat.readInt();
               }
           }
       }
   }
   
   private class TEWritingTableCodec extends FMLIndexedMessageToMessageCodec<TEWritingTableMessage>
   {
       public TEWritingTableCodec()
       {
           addDiscriminator(0, TEWritingTableMessage.class);
       }
       
       @Override
       public void encodeInto(ChannelHandlerContext ctx, TEWritingTableMessage msg, ByteBuf target) throws Exception
       {
           target.writeInt(msg.x);
           target.writeInt(msg.y);
           target.writeInt(msg.z);

           target.writeBoolean(msg.items != null);
           if (msg.items != null)
           {
               int[] items = msg.items;
               for (int j = 0; j < items.length; j++)
               {
                   int i = items[j];
                   target.writeInt(i);
               }
           }
       }

       @Override
       public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, TEWritingTableMessage msg)
       {
    	   msg.x = dat.readInt();
           msg.y = dat.readInt();
           msg.z = dat.readInt();
           int typDat = dat.readByte();
           boolean hasStacks = dat.readBoolean();
           
           msg.items = new int[TEWritingTable.sizeInv*3];
           if (hasStacks)
           {
               msg.items = new int[TEWritingTable.sizeInv*3];
               for (int i = 0; i < msg.items.length; i++)
               {
                   msg.items[i] = dat.readInt();
               }
           }
       }
   }
   
   //Packets to be obtained
   public static Packet getPacket(TEAltar tileAltar)
   {
       TEAltarMessage msg = new TEAltarMessage();
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
       msg.x = tilePedestal.xCoord;
       msg.y = tilePedestal.yCoord;
       msg.z = tilePedestal.zCoord;
       msg.items = tilePedestal.buildIntDataList();
       
       return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
   }
   
   public static Packet getPacket(TEPlinth tilePlinth)
   {
       TEPlinthMessage msg = new TEPlinthMessage();
       msg.x = tilePlinth.xCoord;
       msg.y = tilePlinth.yCoord;
       msg.z = tilePlinth.zCoord;
       msg.items = tilePlinth.buildIntDataList();
       
       return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
   }
   
   public static Packet getPacket(TESocket tileSocket)
   {
       TESocketMessage msg = new TESocketMessage();
       msg.x = tileSocket.xCoord;
       msg.y = tileSocket.yCoord;
       msg.z = tileSocket.zCoord;
       msg.items = tileSocket.buildIntDataList();
       
       return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
   }
   
   public static Packet getPacket(TETeleposer tileTeleposer)
   {
       TETeleposerMessage msg = new TETeleposerMessage();
       msg.x = tileTeleposer.xCoord;
       msg.y = tileTeleposer.yCoord;
       msg.z = tileTeleposer.zCoord;
       msg.items = tileTeleposer.buildIntDataList();
       
       return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
   }
   
   public static Packet getPacket(TEWritingTable tileWritingTable)
   {
       TEWritingTableMessage msg = new TEWritingTableMessage();
       msg.x = tileWritingTable.xCoord;
       msg.y = tileWritingTable.yCoord;
       msg.z = tileWritingTable.zCoord;
       msg.items = tileWritingTable.buildIntDataList();
       
       return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
   }
}
