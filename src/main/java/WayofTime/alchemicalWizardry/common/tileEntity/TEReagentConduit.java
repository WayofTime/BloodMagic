package WayofTime.alchemicalWizardry.common.tileEntity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.alchemicalWizardry.api.ColourAndCoords;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.alchemy.energy.IReagentHandler;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainer;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.api.alchemy.energy.TileSegmentedReagentHandler;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityParticleBeam;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class TEReagentConduit extends TileSegmentedReagentHandler implements IUpdatePlayerListBox
{
    public List<ColourAndCoords> destinationList; //These are offsets
    public Map<Reagent, List<Int3>> reagentTargetList;
    public Map<Reagent, Integer> reagentTankDesignationList;
    public int tickRate = 20; //Rate that the reagents are sent

    int hasChanged = 0;
    public boolean affectedByRedstone = true;

    public int maxConnextions = 5;

    public int renderCount = 0;

    public TEReagentConduit()
    {
        this(2, 2000);
    }

    public TEReagentConduit(int numberOfTanks, int size)
    {
        super(numberOfTanks, size);

        destinationList = new LinkedList();
        reagentTargetList = new HashMap();
        reagentTankDesignationList = new HashMap();
    }

    public Int3 getColour()
    {
        int[] redMap = new int[this.tanks.length];
        int[] greenMap = new int[this.tanks.length];
        int[] blueMap = new int[this.tanks.length];

        for (int i = 0; i < this.tanks.length; i++)
        {
            ReagentContainer container = this.tanks[i];
            if (container != null && container.getReagent() != null)
            {
                Reagent reagent = container.getReagent().reagent;

                redMap[i] = reagent.getColourRed();
                greenMap[i] = reagent.getColourGreen();
                blueMap[i] = reagent.getColourBlue();
            }
        }

        int red = 0;
        int green = 0;
        int blue = 0;

        for (int i = 0; i < this.tanks.length; i++)
        {
            red += redMap[i];
            green += greenMap[i];
            blue += blueMap[i];
        }

        red /= this.tanks.length;
        green /= this.tanks.length;
        blue /= this.tanks.length;

        return new Int3(red, green, blue);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setInteger("hasChanged", hasChanged);

        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < destinationList.size(); i++)
        {
            NBTTagCompound savedTag = new NBTTagCompound();
            tagList.appendTag(destinationList.get(i).writeToNBT(savedTag));
        }

        tag.setTag("destinationList", tagList);

        NBTTagList reagentTagList = new NBTTagList(); //TODO

        for (Entry<Reagent, List<Int3>> entry : reagentTargetList.entrySet())
        {
            NBTTagCompound savedTag = new NBTTagCompound();
            savedTag.setString("reagent", ReagentRegistry.getKeyForReagent(entry.getKey()));

            NBTTagList coordinateTagList = new NBTTagList();

            for (Int3 coord : entry.getValue())
            {
                NBTTagCompound coordinateTag = new NBTTagCompound();

                coord.writeToNBT(coordinateTag);

                coordinateTagList.appendTag(coordinateTag);
            }

            savedTag.setTag("coordinateList", coordinateTagList);

            reagentTagList.appendTag(savedTag);
        }

        tag.setTag("reagentTargetList", reagentTagList);

        NBTTagList tankDesignationList = new NBTTagList();

        for (Entry<Reagent, Integer> entry : this.reagentTankDesignationList.entrySet())
        {
            NBTTagCompound savedTag = new NBTTagCompound();

            savedTag.setString("reagent", ReagentRegistry.getKeyForReagent(entry.getKey()));
            savedTag.setInteger("integer", entry.getValue());

            tankDesignationList.appendTag(savedTag);
        }

        tag.setTag("tankDesignationList", tankDesignationList);

    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        hasChanged = tag.getInteger("hasChanged");

        NBTTagList tagList = tag.getTagList("destinationList", Constants.NBT.TAG_COMPOUND);

        destinationList = new LinkedList();

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound savedTag = tagList.getCompoundTagAt(i);

            destinationList.add(ColourAndCoords.readFromNBT(savedTag));
        }

        reagentTargetList = new HashMap();

        NBTTagList reagentTagList = tag.getTagList("reagentTargetList", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < reagentTagList.tagCount(); i++)
        {
            NBTTagCompound savedTag = reagentTagList.getCompoundTagAt(i);

            Reagent reagent = ReagentRegistry.getReagentForKey(savedTag.getString("reagent"));

            List<Int3> coordList = new LinkedList();

            NBTTagList coordinateList = savedTag.getTagList("coordinateList", Constants.NBT.TAG_COMPOUND);

            for (int j = 0; j < coordinateList.tagCount(); j++)
            {
                coordList.add(Int3.readFromNBT(coordinateList.getCompoundTagAt(j)));
            }

            reagentTargetList.put(reagent, coordList);
        }

        reagentTankDesignationList = new HashMap();

        NBTTagList tankDesignationList = tag.getTagList("tankDesignationList", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tankDesignationList.tagCount(); i++)
        {
            NBTTagCompound savedTag = tankDesignationList.getCompoundTagAt(i);

            this.reagentTankDesignationList.put(ReagentRegistry.getReagentForKey(savedTag.getString("reagent")), new Integer(savedTag.getInteger("integer")));
        }
    }

    public void readClientNBT(NBTTagCompound tag)
    {
        NBTTagList tagList = tag.getTagList("destinationList", Constants.NBT.TAG_COMPOUND);

        destinationList = new LinkedList();

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound savedTag = tagList.getCompoundTagAt(i);

            destinationList.add(ColourAndCoords.readFromNBT(savedTag));
        }

        NBTTagList reagentTagList = tag.getTagList("reagentTanks", Constants.NBT.TAG_COMPOUND);

        int size = reagentTagList.tagCount();
        this.tanks = new ReagentContainer[size];

        for (int i = 0; i < size; i++)
        {
            NBTTagCompound savedTag = reagentTagList.getCompoundTagAt(i);
            this.tanks[i] = ReagentContainer.readFromNBT(savedTag);
        }
    }

    public void writeClientNBT(NBTTagCompound tag)
    {
        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < destinationList.size(); i++)
        {
            NBTTagCompound savedTag = new NBTTagCompound();
            tagList.appendTag(destinationList.get(i).writeToNBT(savedTag));
        }

        tag.setTag("destinationList", tagList);

        NBTTagList reagentTagList = new NBTTagList();

        for (int i = 0; i < this.tanks.length; i++)
        {
            NBTTagCompound savedTag = new NBTTagCompound();
            if (this.tanks[i] != null)
            {
                this.tanks[i].writeToNBT(savedTag);
            }
            reagentTagList.appendTag(savedTag);
        }

        tag.setTag("reagentTanks", reagentTagList);
    }

    @Override
    public void update()
    {
        if (!worldObj.isRemote)
        {
            if (hasChanged > 1)
            {
                hasChanged = 1;
            } else if (hasChanged == 1)
            {
                hasChanged = 0;
            }

            if (worldObj.getWorldTime() % 100 == 99)
            {
                this.updateColourList();
            }

            if (affectedByRedstone && worldObj.isBlockPowered(pos)) //isBlockBeingIndirectlyPowered()
            {
                return;
            }

            int totalTransfered = 0;

            for (Entry<Reagent, List<Int3>> entry : this.reagentTargetList.entrySet())
            {
                for (Int3 coord : entry.getValue())
                {
                    if (totalTransfered >= this.tickRate)
                    {
                        break;
                    }

                    ReagentStack maxDrainAmount = this.drain(EnumFacing.UP, new ReagentStack(entry.getKey(), this.tickRate - totalTransfered), false);

                    if (maxDrainAmount == null)
                    {
                        continue;
                    }

                    int amountLeft = maxDrainAmount.amount;

                    if (amountLeft <= 0)
                    {
                        continue;
                    }

                    BlockPos newPos = pos.add(coord.xCoord, coord.yCoord, coord.zCoord);

                    TileEntity tile = worldObj.getTileEntity(newPos);
                    if (tile instanceof IReagentHandler)
                    {
                        int amount = Math.min(((IReagentHandler) tile).fill(EnumFacing.UP, maxDrainAmount, false), amountLeft);
                        if (amount > 0)
                        {
                            amountLeft -= amount;
                            totalTransfered += amount;

                            ReagentStack stack = this.drain(EnumFacing.UP, new ReagentStack(entry.getKey(), amount), true);
                            ((IReagentHandler) tile).fill(EnumFacing.UP, stack, true);
                        }
                    }
                }
            }
        } else
        {
            if (affectedByRedstone && worldObj.isBlockPowered(pos))
            {
                return;
            }

            renderCount++;

            if (worldObj.getWorldTime() % 100 != 0)
            {
                return;
            }

            this.sendPlayerStuffs();
        }
    }


    @SideOnly(Side.CLIENT)
    public void sendPlayerStuffs()
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        World world = mc.theWorld;
        if (SpellHelper.canPlayerSeeAlchemy(player))
        {
            for (ColourAndCoords colourSet : this.destinationList)
            {
            	BlockPos newPos = pos.add(colourSet.xCoord, colourSet.yCoord, colourSet.zCoord);
                if (!(worldObj.getTileEntity(newPos) instanceof IReagentHandler))
                {
                    continue;
                }
                EntityParticleBeam beam = new EntityParticleBeam(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                double velocity = Math.sqrt(Math.pow(colourSet.xCoord, 2) + Math.pow(colourSet.yCoord, 2) + Math.pow(colourSet.zCoord, 2));
                double wantedVel = 0.3d;
                beam.setVelocity(wantedVel * colourSet.xCoord / velocity, wantedVel * colourSet.yCoord / velocity, wantedVel * colourSet.zCoord / velocity);
                beam.setColour(colourSet.colourRed / 255f, colourSet.colourGreen / 255f, colourSet.colourBlue / 255f);
                beam.setDestination(pos.getX() + colourSet.xCoord, pos.getY() + colourSet.yCoord, pos.getZ() + colourSet.zCoord);
                worldObj.spawnEntityInWorld(beam);
            }
        }
    }

    public void updateColourList()
    {
        if (worldObj.isRemote)
        {
            return;
        }

        List<ColourAndCoords> newList = this.compileListForReagentTargets(this.reagentTargetList);

        if (newList != null && !newList.equals(destinationList))
        {
            this.destinationList = newList;
            worldObj.markBlockForUpdate(pos);
        }
    }

    public List<ColourAndCoords> compileListForReagentTargets(Map<Reagent, List<Int3>> map)
    {
        List<ColourAndCoords> list = new LinkedList();

        for (Entry<Reagent, List<Int3>> entry : map.entrySet())
        {
            if (entry.getValue() != null)
            {
                Reagent reagent = entry.getKey();
                if (reagent == null)
                {
                    continue;
                }
                List<Int3> coords = entry.getValue();
                for (Int3 coord : coords)
                {
                    if (coord == null)
                    {
                        continue;
                    }
                    list.add(new ColourAndCoords(reagent.getColourRed(), reagent.getColourGreen(), reagent.getColourBlue(), reagent.getColourIntensity(), coord.xCoord, coord.yCoord, coord.zCoord));
                }
            }
        }

        return list;
    }

    public boolean addDestinationViaOffset(int red, int green, int blue, int intensity, int xOffset, int yOffset, int zOffset)
    {
        if (xOffset == 0 && yOffset == 0 && zOffset == 0)
        {
            return false;
        }

        this.destinationList.add(new ColourAndCoords(red, green, blue, intensity, xOffset, yOffset, zOffset));

        return true;
    }

    public boolean addDestinationViaActual(int red, int green, int blue, int intensity, int x, int y, int z)
    {
        return this.addDestinationViaOffset(red, green, blue, intensity, x - pos.getX(), y - pos.getY(), z - pos.getZ());
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeClientNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(pos, 90210, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        super.onDataPacket(net, packet);
        readClientNBT(packet.getNbtCompound());
    }

    public boolean addReagentDestinationViaOffset(Reagent reagent, int xOffset, int yOffset, int zOffset)
    {
        int totalConnections = 0;

        for (Entry<Reagent, List<Int3>> entry : this.reagentTargetList.entrySet())
        {
            if (entry.getValue() != null)
            {
                totalConnections += entry.getValue().size();
            }
        }

        if (totalConnections >= this.maxConnextions)
        {
            //Send message that it cannot be done? Maybe add a Player instance
            return false;
        }

        if (xOffset == 0 && yOffset == 0 && zOffset == 0)
        {
            return false;
        }

        Int3 newCoord = new Int3(xOffset, yOffset, zOffset);

        if (this.reagentTargetList.containsKey(reagent))
        {
            List<Int3> coordList = this.reagentTargetList.get(reagent);
            if (coordList == null)
            {
                List<Int3> newCoordList = new LinkedList();
                newCoordList.add(newCoord);
                this.reagentTargetList.put(reagent, newCoordList);
            } else
            {
                coordList.add(newCoord);
            }

            return true;
        } else
        {
            List<Int3> newCoordList = new LinkedList();
            newCoordList.add(newCoord);
            this.reagentTargetList.put(reagent, newCoordList);

            return true;
        }
    }

    public boolean addReagentDestinationViaActual(Reagent reagent, int x, int y, int z)
    {
        return (this.addReagentDestinationViaOffset(reagent, x - pos.getX(), y - pos.getY(), z - pos.getZ()));
    }

    public boolean removeReagentDestinationViaOffset(Reagent reagent, int xOffset, int yOffset, int zOffset)
    {
        if (this.reagentTargetList.containsKey(reagent))
        {
            List<Int3> coords = this.reagentTargetList.get(reagent);
            if (coords != null)
            {
                Int3 reference = new Int3(xOffset, yOffset, zOffset);

                return coords.remove(reference);
            }
        }
        return false;
    }

    public boolean removeReagentDestinationViaActual(Reagent reagent, int x, int y, int z)
    {
        return this.removeReagentDestinationViaOffset(reagent, x - pos.getX(), y - pos.getY(), z - pos.getZ());
    }

    @Override
    public int fill(EnumFacing from, ReagentStack resource, boolean doFill)
    {
        if (doFill && !worldObj.isRemote)
        {
            worldObj.markBlockForUpdate(pos);
            hasChanged = 2;
        }

        return super.fill(from, resource, doFill);
    }

    @Override
    public ReagentStack drain(EnumFacing from, ReagentStack resource, boolean doDrain)
    {
        if (doDrain && !worldObj.isRemote)
        {
            worldObj.markBlockForUpdate(pos);
            hasChanged = 2;
        }

        return super.drain(from, resource, doDrain);
    }
}