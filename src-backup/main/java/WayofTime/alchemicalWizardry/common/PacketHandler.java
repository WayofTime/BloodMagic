package WayofTime.alchemicalWizardry.common;

import ibxm.Player;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.bloodAltarUpgrade.UpgradedAltars;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEOrientable;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPedestal;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPlinth;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.jcraft.jogg.Packet;

public class PacketHandler //implements IPacketHandler
{
//    @Override
//    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
//    {
//        if (packet.channel.equals("BloodAltar"))
//        {
//            ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
//            int x = dat.readInt();
//            int y = dat.readInt();
//            int z = dat.readInt();
//            boolean hasStacks = dat.readByte() != 0;
//            int[] items = new int[0];
//
//            if (hasStacks)
//            {
//                items = new int[1 * 3];
//
//                for (int i = 0; i < items.length; i++)
//                {
//                    items[i] = dat.readInt();
//                }
//            }
//
//            int fluidIDMain = dat.readInt();
//            int fluidAmountMain = dat.readInt();
//            int fluidIDOutput = dat.readInt();
//            int fluidAmountOutput = dat.readInt();
//            int fluidIDInput = dat.readInt();
//            int fluidAmountInput = dat.readInt();
//            int capacity = dat.readInt();
//            World world = AlchemicalWizardry.proxy.getClientWorld();
//            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
//
//            if (tileEntity instanceof TEAltar)
//            {
//                TEAltar tileEntityAltar = (TEAltar) tileEntity;
//                FluidStack flMain = new FluidStack(fluidIDMain, fluidAmountMain);
//                FluidStack flOutput = new FluidStack(fluidIDOutput, fluidAmountOutput);
//                FluidStack flInput = new FluidStack(fluidIDInput, fluidAmountInput);
//                tileEntityAltar.handlePacketData(items, flMain, flOutput, flInput, capacity);
//            }
//        } else if (packet.channel.equals("FallReset"))
//        {
//            if (player instanceof EntityPlayer)
//            {
//                ((EntityPlayer) player).fallDistance = 0;
//            }
//        } else if (packet.channel.equals("particle"))
//        {
//            ByteArrayInputStream bin = new ByteArrayInputStream(packet.data);
//            DataInputStream din = new DataInputStream(bin);
//            Random rand = new Random();
//
//            try
//            {
//                double x = din.readDouble();
//                double y = din.readDouble();
//                double z = din.readDouble();
//                short particleType = din.readShort();
//                World world = ((EntityPlayer) player).worldObj;
//
//                if (particleType == 1)
//                {
//                    world.spawnParticle("mobSpell", x + 0.5D + rand.nextGaussian() / 8, y + 1.1D, z + 0.5D + rand.nextGaussian() / 8, 0.5117D, 0.0117D, 0.0117D);
//                }
//
//                if (particleType == 2)
//                {
//                    world.spawnParticle("reddust", x + 0.5D + rand.nextGaussian() / 8, y + 1.1D, z + 0.5D + rand.nextGaussian() / 8, 0.82D, 0.941D, 0.91D);
//                }
//
//                if (particleType == 3)
//                {
//                    world.spawnParticle("mobSpell", x + 0.5D + rand.nextGaussian() / 8, y + 1.1D, z + 0.5D + rand.nextGaussian() / 8, 1.0D, 0.371D, 0.371D);
//                }
//
//                if (particleType == 4)
//                {
//                    float f = (float) 1.0F;
//                    float f1 = f * 0.6F + 0.4F;
//                    float f2 = f * f * 0.7F - 0.5F;
//                    float f3 = f * f * 0.6F - 0.7F;
//
//                    for (int l = 0; l < 8; ++l)
//                    {
//                        world.spawnParticle("reddust", x + Math.random() - Math.random(), y + Math.random() - Math.random(), z + Math.random() - Math.random(), f1, f2, f3);
//                    }
//                }
//            } catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        } else if (packet.channel.equals("CustomParticle"))
//        {
//            ByteArrayInputStream bin = new ByteArrayInputStream(packet.data);
//            DataInputStream din = new DataInputStream(bin);
//            Random rand = new Random();
//
//            try
//            {
//                World world = ((EntityPlayer) player).worldObj;
//                int size = din.readInt();
//                String str = "";
//
//                for (int i = 0; i < size; i++)
//                {
//                    str = str + din.readChar();
//                }
//
//                double x = din.readDouble();
//                double y = din.readDouble();
//                double z = din.readDouble();
//                double xVel = din.readDouble();
//                double yVel = din.readDouble();
//                double zVel = din.readDouble();
//                world.spawnParticle(str, x, y, z, xVel, yVel, zVel);
//            } catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        } else if (packet.channel.equals("SetLifeEssence")) //Sets the data for the character
//        {
//            ByteArrayInputStream bin = new ByteArrayInputStream(packet.data);
//            DataInputStream din = new DataInputStream(bin);
//
//            try
//            {
//                EntityPlayer user = (EntityPlayer) player;
//                int length = din.readInt();
//                String ownerName = "";
//
//                for (int i = 0; i < length; i++)
//                {
//                    ownerName = ownerName + din.readChar();
//                }
//
//                int addedEssence = din.readInt();
//                int maxEssence = din.readInt();
//                World world = MinecraftServer.getServer().worldServers[0];
//                LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);
//
//                if (data == null)
//                {
//                    data = new LifeEssenceNetwork(ownerName);
//                    world.setItemData(ownerName, data);
//                }
//
//                if (addedEssence > 0)
//                {
//                    if (data.currentEssence < maxEssence)
//                    {
//                        data.currentEssence = Math.min(maxEssence, data.currentEssence + addedEssence);
//                        data.markDirty();
//                    }
//
//                    if (!user.capabilities.isCreativeMode)
//                    {
//                        for (int i = 0; i < ((addedEssence + 99) / 100); i++)
//                        {
//                            //player.setEntityHealth((player.getHealth()-1));
//                            user.setHealth((user.getHealth() - 1));
//
//                            if (user.getHealth() <= 0.5f)
//                            {
//                                //user.inventory.dropAllItems();
//                                user.onDeath(DamageSource.generic);
//                                return;
//                            }
//                        }
//                    }
//                } else
//                {
//                    int removedEssence = -addedEssence;
//
//                    if ((data.currentEssence - removedEssence) >= 0)
//                    {
//                        data.currentEssence -= removedEssence;
//                        data.markDirty();
//                    } else
//                    {
//                        if (removedEssence >= 100)
//                        {
//                            for (int i = 0; i < ((removedEssence + 99) / 100); i++)
//                            {
//                                //player.setEntityHealth((player.getHealth()-1));
//                                user.setHealth((user.getHealth() - 1));
//
//                                if (user.getHealth() <= 0.5f)
//                                {
//                                    //user.inventory.dropAllItems();
//                                    user.onDeath(DamageSource.generic);
//                                    return;
//                                }
//                            }
//                        } else
//                        {
//                            if (user.worldObj.rand.nextInt(100) <= removedEssence)
//                            {
//                                user.setHealth((user.getHealth() - 1));
//
//                                if (user.getHealth() <= 0.5f)
//                                {
//                                    //user.inventory.dropAllItems();
//                                    user.onDeath(DamageSource.generic);
//                                    return;
//                                }
//                            }
//                        }
//                    }
//                }
//
//                //PacketDispatcher.sendPacketToPlayer(PacketHandler.getPacket(ownerName), (Player)user);
////                data.currentEssence = addedEssence;
////                data.markDirty();
//            } catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        } else if (packet.channel.equals("InfiniteLPPath"))
//        {
//            ByteArrayInputStream bin = new ByteArrayInputStream(packet.data);
//            DataInputStream din = new DataInputStream(bin);
//
//            try
//            {
//                EntityPlayer user = (EntityPlayer) player;
//                int length = din.readInt();
//                String ownerName = "";
//
//                for (int i = 0; i < length; i++)
//                {
//                    ownerName = ownerName + din.readChar();
//                }
//
//                boolean fill = din.readBoolean();
//                World world = MinecraftServer.getServer().worldServers[0];
//                LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);
//
//                if (data == null)
//                {
//                    data = new LifeEssenceNetwork(ownerName);
//                    world.setItemData(ownerName, data);
//                }
//
//                if (fill)
//                {
//                    data.currentEssence += 1000000;
//                    data.markDirty();
//                } else
//                {
//                    data.currentEssence = 0;
//                    data.markDirty();
//                }
//
//                //PacketDispatcher.sendPacketToPlayer(PacketHandler.getPacket(ownerName), (Player)user);
////                data.currentEssence = addedEssence;
////                data.markDirty();
//            } catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        } else if (packet.channel.equals("GetLifeEssence"))
//        {
//            ByteArrayInputStream bin = new ByteArrayInputStream(packet.data);
//            DataInputStream din = new DataInputStream(bin);
//
//            try
//            {
//                int length = din.readInt();
//                String ownerName = "";
//
//                for (int i = 0; i < length; i++)
//                {
//                    ownerName = ownerName + din.readChar();
//                }
//
//                World world = MinecraftServer.getServer().worldServers[0];
//                LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);
//
//                if (data == null)
//                {
//                    data = new LifeEssenceNetwork(ownerName);
//                    world.setItemData(ownerName, data);
//                }
//
//                if (player instanceof EntityPlayer)
//                {
//                    EntityPlayer owner = (EntityPlayer) player;
//                    ChatMessageComponent chatmessagecomponent = new ChatMessageComponent();
//                    //chatmessagecomponent.func_111072_b("Current Essence: " + data.currentEssence + "LP");
//                    chatmessagecomponent.addText("Current Essence: " + data.currentEssence + "LP");
//                    owner.sendChatToPlayer(chatmessagecomponent);
//                }
//            } catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        } else if (packet.channel.equals("GetAltarEssence"))
//        {
//            ByteArrayInputStream bin = new ByteArrayInputStream(packet.data);
//            DataInputStream din = new DataInputStream(bin);
//
//            try
//            {
//                int x = din.readInt();
//                int y = din.readInt();
//                int z = din.readInt();
//
//                if (player instanceof EntityPlayer)
//                {
//                    EntityPlayer owner = (EntityPlayer) player;
//                    World world = owner.worldObj;
//                    TEAltar tileEntity = (TEAltar) world.getBlockTileEntity(x, y, z);
//
//                    if (tileEntity != null)
//                    {
//                        int level = UpgradedAltars.isAltarValid(world, x, y, z);
//                        ChatMessageComponent chatmessagecomponent = new ChatMessageComponent();
//                        chatmessagecomponent.addText("Altar's Current Essence: " + tileEntity.getFluidAmount() + "LP" + "\n" + "Altar's Current Tier: " + level + "\nCapacity: " + tileEntity.getCapacity() + "LP");
//                        //chatmessagecomponent.addText();
//                        owner.sendChatToPlayer(chatmessagecomponent);
//                    }
//                }
//            } catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        } else if (packet.channel.equals("TESocket"))
//        {
//            ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
//            int x = dat.readInt();
//            int y = dat.readInt();
//            int z = dat.readInt();
//            boolean hasStacks = dat.readByte() != 0;
//            int[] items = new int[0];
//
//            if (hasStacks)
//            {
//                items = new int[1 * 3];
//
//                for (int i = 0; i < items.length; i++)
//                {
//                    items[i] = dat.readInt();
//                }
//            }
//
//            World world = AlchemicalWizardry.proxy.getClientWorld();
//            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
//
//            if (tileEntity instanceof TESocket)
//            {
//                TESocket tileEntityAltar = (TESocket) tileEntity;
//                tileEntityAltar.handlePacketData(items);
//            }
//        } else if (packet.channel.equals("TEWritingTable"))
//        {
//            ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
//            int x = dat.readInt();
//            int y = dat.readInt();
//            int z = dat.readInt();
//            boolean hasStacks = dat.readByte() != 0;
//            int[] items = new int[0];
//
//            if (hasStacks)
//            {
//                items = new int[7 * 3];
//
//                for (int i = 0; i < items.length; i++)
//                {
//                    items[i] = dat.readInt();
//                }
//            }
//
//            World world = AlchemicalWizardry.proxy.getClientWorld();
//            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
//
//            if (tileEntity instanceof TEWritingTable)
//            {
//                TEWritingTable tileEntityAltar = (TEWritingTable) tileEntity;
//                tileEntityAltar.handlePacketData(items);
//            }
//        } else if (packet.channel.equals("TEOrientor"))
//        {
//            ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
//            int x = dat.readInt();
//            int y = dat.readInt();
//            int z = dat.readInt();
//            World world = AlchemicalWizardry.proxy.getClientWorld();
//            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
//
//            if (tileEntity instanceof TEOrientable)
//            {
//                TEOrientable tileEntityOrientable = (TEOrientable) tileEntity;
//                tileEntityOrientable.setInputDirection(ForgeDirection.getOrientation(dat.readInt()));
//                tileEntityOrientable.setOutputDirection(ForgeDirection.getOrientation(dat.readInt()));
//                world.markBlockForRenderUpdate(x, y, z);
//            }
//        } else if (packet.channel.equals("TEPedestal"))
//        {
//            ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
//            int x = dat.readInt();
//            int y = dat.readInt();
//            int z = dat.readInt();
//            boolean hasStacks = dat.readByte() != 0;
//            int[] items = new int[0];
//
//            if (hasStacks)
//            {
//                items = new int[1 * 3];
//
//                for (int i = 0; i < items.length; i++)
//                {
//                    items[i] = dat.readInt();
//                }
//            }
//
//            World world = AlchemicalWizardry.proxy.getClientWorld();
//            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
//
//            if (tileEntity instanceof TEPedestal)
//            {
//                TEPedestal tileEntityAltar = (TEPedestal) tileEntity;
//                tileEntityAltar.handlePacketData(items);
//            }
//        } else if (packet.channel.equals("TEPlinth"))
//        {
//            ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
//            int x = dat.readInt();
//            int y = dat.readInt();
//            int z = dat.readInt();
//            boolean hasStacks = dat.readByte() != 0;
//            int[] items = new int[0];
//
//            if (hasStacks)
//            {
//                items = new int[1 * 3];
//
//                for (int i = 0; i < items.length; i++)
//                {
//                    items[i] = dat.readInt();
//                }
//            }
//
//            World world = AlchemicalWizardry.proxy.getClientWorld();
//            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
//
//            if (tileEntity instanceof TEPlinth)
//            {
//                TEPlinth tileEntityAltar = (TEPlinth) tileEntity;
//                tileEntityAltar.handlePacketData(items);
//            }
//        } else if (packet.channel.equals("TETeleposer"))
//        {
//            ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
//            int x = dat.readInt();
//            int y = dat.readInt();
//            int z = dat.readInt();
//            boolean hasStacks = dat.readByte() != 0;
//            int[] items = new int[0];
//
//            if (hasStacks)
//            {
//                items = new int[1 * 3];
//
//                for (int i = 0; i < items.length; i++)
//                {
//                    items[i] = dat.readInt();
//                }
//            }
//
//            World world = AlchemicalWizardry.proxy.getClientWorld();
//            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
//
//            if (tileEntity instanceof TETeleposer)
//            {
//                TETeleposer tileEntityAltar = (TETeleposer) tileEntity;
//                tileEntityAltar.handlePacketData(items);
//            }
//        } else if (packet.channel.equals("SetPlayerVel"))
//        {
//            ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
//            double xVel = dat.readDouble();
//            double yVel = dat.readDouble();
//            double zVel = dat.readDouble();
//            ((EntityPlayer) player).setVelocity(xVel, yVel, zVel);
//        } else if (packet.channel.equals("SetPlayerPos"))
//        {
//            ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
//            double xVel = dat.readDouble();
//            double yVel = dat.readDouble();
//            double zVel = dat.readDouble();
//            ((EntityPlayer) player).setPosition(xVel, yVel, zVel);
//        }
//    }
//
//    public static Packet getPacket(TEAltar tileEntity)
//    {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//        int[] items = tileEntity.buildIntDataList();
//        boolean hasStacks = (items != null);
//
//        try
//        {
//            dos.writeInt(tileEntity.xCoord);
//            dos.writeInt(tileEntity.yCoord);
//            dos.writeInt(tileEntity.zCoord);
//            dos.writeByte(hasStacks ? 1 : 0);
//
//            if (hasStacks)
//            {
//                for (int i = 0; i < 3; i++)
//                {
//                    dos.writeInt(items[i]);
//                }
//            }
//
//            FluidStack flMain = tileEntity.getFluid();
//
//            if (flMain == null)
//            {
//                dos.writeInt(AlchemicalWizardry.lifeEssenceFluid.getBlockID());
//                dos.writeInt(0);
//            } else
//            {
//                dos.writeInt(flMain.fluidID);
//                dos.writeInt(flMain.amount);
//            }
//
//            FluidStack flOut = tileEntity.getOutputFluid();
//
//            if (flOut == null)
//            {
//                dos.writeInt(AlchemicalWizardry.lifeEssenceFluid.getBlockID());
//                dos.writeInt(0);
//            } else
//            {
//                dos.writeInt(flOut.fluidID);
//                dos.writeInt(flOut.amount);
//            }
//
//            FluidStack flIn = tileEntity.getInputFluid();
//
//            if (flIn == null)
//            {
//                dos.writeInt(AlchemicalWizardry.lifeEssenceFluid.getBlockID());
//                dos.writeInt(0);
//            } else
//            {
//                dos.writeInt(flIn.fluidID);
//                dos.writeInt(flIn.amount);
//            }
//
//            dos.writeInt(tileEntity.capacity);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "BloodAltar";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        pkt.isChunkDataPacket = true;
//        return pkt;
//    }
//
//    public static Packet getPacket(TESocket tileEntity)
//    {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//        int[] items = tileEntity.buildIntDataList();
//        boolean hasStacks = (items != null);
//
//        try
//        {
//            dos.writeInt(tileEntity.xCoord);
//            dos.writeInt(tileEntity.yCoord);
//            dos.writeInt(tileEntity.zCoord);
//            dos.writeByte(hasStacks ? 1 : 0);
//
//            if (hasStacks)
//            {
//                for (int i = 0; i < 3; i++)
//                {
//                    dos.writeInt(items[i]);
//                }
//            }
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "TESocket";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        pkt.isChunkDataPacket = true;
//        return pkt;
//    }
//
//    public static Packet getPacket(String ownerName, int addedEssence, int maxEssence)
//    //Packet to be sent to server to change essence
//    {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//
//        try
//        {
//            dos.writeInt(ownerName.length());
//            dos.writeChars(ownerName);
//            dos.writeInt(addedEssence);
//            dos.writeInt(maxEssence); //Used for Blood Orbs, but does nothing for other items
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "SetLifeEssence";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        //pkt.isChunkDataPacket = true;
//        return pkt;
//    }
//
//    public static Packet getPacket(String ownerName) //stores the current essence in the player's NBT
//    {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//
//        try
//        {
//            dos.writeInt(ownerName.length());
//            dos.writeChars(ownerName);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "GetLifeEssence";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        //pkt.isChunkDataPacket = true;
//        return pkt;
//    }
//
//    public static Packet getAltarPacket(int x, int y, int z)
//    {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//
//        try
//        {
//            dos.writeInt(x);
//            dos.writeInt(y);
//            dos.writeInt(z);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "GetAltarEssence";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        //pkt.isChunkDataPacket = true;
//        return pkt;
//    }
//
//    public static Packet getPacket(TEWritingTable tileEntity)
//    {
//        // TODO Auto-generated method stub
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//        int[] items = tileEntity.buildIntDataList();
//        boolean hasStacks = (items != null);
//
//        try
//        {
//            dos.writeInt(tileEntity.xCoord);
//            dos.writeInt(tileEntity.yCoord);
//            dos.writeInt(tileEntity.zCoord);
//            dos.writeByte(hasStacks ? 1 : 0);
//
//            if (hasStacks)
//            {
//                for (int i = 0; i < 3 * 7; i++)
//                {
//                    dos.writeInt(items[i]);
//                }
//            }
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "TEWritingTable";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        pkt.isChunkDataPacket = true;
//        return pkt;
//    }
//
//    public static Packet getPacket(TEPedestal tileEntity)
//    {
//        // TODO Auto-generated method stub
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//        int[] items = tileEntity.buildIntDataList();
//        boolean hasStacks = (items != null);
//
//        try
//        {
//            dos.writeInt(tileEntity.xCoord);
//            dos.writeInt(tileEntity.yCoord);
//            dos.writeInt(tileEntity.zCoord);
//            dos.writeByte(hasStacks ? 1 : 0);
//
//            if (hasStacks)
//            {
//                for (int i = 0; i < 3 * 1; i++)
//                {
//                    dos.writeInt(items[i]);
//                }
//            }
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "TEPedestal";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        pkt.isChunkDataPacket = true;
//        return pkt;
//    }
//
//    public static Packet getPacket(TEPlinth tileEntity)
//    {
//        // TODO Auto-generated method stub
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//        int[] items = tileEntity.buildIntDataList();
//        boolean hasStacks = (items != null);
//
//        try
//        {
//            dos.writeInt(tileEntity.xCoord);
//            dos.writeInt(tileEntity.yCoord);
//            dos.writeInt(tileEntity.zCoord);
//            dos.writeByte(hasStacks ? 1 : 0);
//
//            if (hasStacks)
//            {
//                for (int i = 0; i < 3 * 1; i++)
//                {
//                    dos.writeInt(items[i]);
//                }
//            }
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "TEPlinth";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        pkt.isChunkDataPacket = true;
//        return pkt;
//    }
//
//    public static Packet getPacket(TETeleposer tileEntity)
//    {
//        // TODO Auto-generated method stub
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//        int[] items = tileEntity.buildIntDataList();
//        boolean hasStacks = (items != null);
//
//        try
//        {
//            dos.writeInt(tileEntity.xCoord);
//            dos.writeInt(tileEntity.yCoord);
//            dos.writeInt(tileEntity.zCoord);
//            dos.writeByte(hasStacks ? 1 : 0);
//
//            if (hasStacks)
//            {
//                for (int i = 0; i < 3 * 1; i++)
//                {
//                    dos.writeInt(items[i]);
//                }
//            }
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "TETeleposer";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        pkt.isChunkDataPacket = true;
//        return pkt;
//    }
//
//    public static Packet getCustomParticlePacket(String str, double x, double y, double z, double xVel, double yVel, double zVel)
//    {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//
//        try
//        {
//            dos.writeInt(str.length());
//            dos.writeChars(str);
//            dos.writeDouble(x);
//            dos.writeDouble(y);
//            dos.writeDouble(z);
//            dos.writeDouble(xVel);
//            dos.writeDouble(yVel);
//            dos.writeDouble(zVel);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "CustomParticle";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        pkt.isChunkDataPacket = false;
//        return pkt;
//    }
//
//    public static Packet getPlayerVelocitySettingPacket(double xVel, double yVel, double zVel)
//    {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//
//        try
//        {
//            dos.writeDouble(xVel);
//            dos.writeDouble(yVel);
//            dos.writeDouble(zVel);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "SetPlayerVel";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        pkt.isChunkDataPacket = false;
//        return pkt;
//    }
//
//    public static Packet getPlayerPositionSettingPacket(double xVel, double yVel, double zVel)
//    {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//
//        try
//        {
//            dos.writeDouble(xVel);
//            dos.writeDouble(yVel);
//            dos.writeDouble(zVel);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "SetPlayerPos";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        pkt.isChunkDataPacket = false;
//        return pkt;
//    }
//
//    public static Packet getCreativeCheatPacket(String ownerName, boolean isFill)
//    {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//
//        try
//        {
//            dos.writeInt(ownerName.length());
//            dos.writeChars(ownerName);
//            dos.writeBoolean(isFill);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "InfiniteLPPath";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        pkt.isChunkDataPacket = false;
//        return pkt;
//    }
//
//    public static Packet getBlockOrientationPacket(TEOrientable tileEntity)
//    {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
//        DataOutputStream dos = new DataOutputStream(bos);
//
//        try
//        {
//            dos.writeInt(tileEntity.xCoord);
//            dos.writeInt(tileEntity.yCoord);
//            dos.writeInt(tileEntity.zCoord);
//            dos.writeInt(tileEntity.getIntForForgeDirection(tileEntity.getInputDirection()));
//            dos.writeInt(tileEntity.getIntForForgeDirection(tileEntity.getOutputDirection()));
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        Packet250CustomPayload pkt = new Packet250CustomPayload();
//        pkt.channel = "TEOrientor";
//        pkt.data = bos.toByteArray();
//        pkt.length = bos.size();
//        pkt.isChunkDataPacket = true;
//        return pkt;
//    }
}
