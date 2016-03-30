package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.TeleposeEvent;
import WayofTime.bloodmagic.api.teleport.TeleportQueue;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.block.BlockTeleposer;
import WayofTime.bloodmagic.item.ItemTelepositionFocus;
import WayofTime.bloodmagic.ritual.portal.Teleports;
import WayofTime.bloodmagic.util.Utils;
import com.google.common.base.Strings;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class TileTeleposer extends TileInventory implements ITickable
{
    //TODO FUTURE: Make AreaDescriptor for Teleposer perhaps?
    public static final String TELEPOSER_RANGE = "teleposerRange";

    private int previousInput;

    public TileTeleposer()
    {
        super(1, "teleposer");
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        previousInput = tagCompound.getInteger(Constants.NBT.PREVIOUS_INPUT);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger(Constants.NBT.PREVIOUS_INPUT, previousInput);
    }

    @Override
    public void update()
    {
        if (!worldObj.isRemote)
        {
            int currentInput = worldObj.getStrongPower(pos);

            if (previousInput == 0 && currentInput != 0)
            {
                initiateTeleport();
            }

            previousInput = currentInput;
        }
    }

    public void initiateTeleport()
    {
        if (!worldObj.isRemote && worldObj.getTileEntity(pos) != null && worldObj.getTileEntity(pos) instanceof TileTeleposer && canInitiateTeleport((TileTeleposer) worldObj.getTileEntity(pos)) && worldObj.getBlockState(pos).getBlock() instanceof BlockTeleposer)
        {
            TileTeleposer teleposer = (TileTeleposer) worldObj.getTileEntity(pos);
            ItemStack focusStack = NBTHelper.checkNBT(teleposer.getStackInSlot(0));
            ItemTelepositionFocus focus = (ItemTelepositionFocus) focusStack.getItem();
            BlockPos focusPos = focus.getBlockPos(teleposer.getStackInSlot(0));
            World focusWorld = focus.getWorld(teleposer.getStackInSlot(0));

            if (focusWorld != null && focusWorld.getTileEntity(focusPos) instanceof TileTeleposer && !focusWorld.getTileEntity(focusPos).equals(this))
            {
                final int focusLevel = (teleposer.getStackInSlot(0).getItemDamage() + 1);
                final int lpToBeDrained = (int) (0.5F * Math.sqrt((pos.getX() - focusPos.getX()) * (pos.getX() - focusPos.getX()) + (pos.getY() - focusPos.getY() + 1) * (pos.getY() - focusPos.getY() + 1) + (pos.getZ() - focusPos.getZ()) * (pos.getZ() - focusPos.getZ())));

                if (NetworkHelper.getSoulNetwork(focus.getOwnerUUID(focusStack)).syphonAndDamage(PlayerHelper.getPlayerFromUUID(focus.getOwnerUUID(focusStack)), lpToBeDrained * (focusLevel * 2 - 1) * (focusLevel * 2 - 1) * (focusLevel * 2 - 1)))
                {
                    int blocksTransported = 0;

                    for (int i = -(focusLevel - 1); i <= (focusLevel - 1); i++)
                    {
                        for (int j = 0; j <= (focusLevel * 2 - 2); j++)
                        {
                            for (int k = -(focusLevel - 1); k <= (focusLevel - 1); k++)
                            {
                                TeleposeEvent event = new TeleposeEvent(worldObj, pos.add(i, 1 + j, k), focusWorld, focusPos.add(i, 1 + j, k));
                                if (Utils.swapLocations(event.initalWorld, event.initialBlockPos, event.finalWorld, event.finalBlockPos) && !MinecraftForge.EVENT_BUS.post(event))
                                {
                                    blocksTransported++;
                                }
                            }
                        }
                    }

                    NetworkHelper.syphonFromContainer(focusStack, lpToBeDrained * blocksTransported);

                    List<Entity> originalWorldEntities;
                    List<Entity> focusWorldEntities;
                    AxisAlignedBB originalArea = new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, Math.min(focusWorld.getHeight(), pos.getY() + 2 * focusLevel), pos.getZ() + 1).expand(focusLevel - 1, 0, focusLevel - 1);
                    originalWorldEntities = worldObj.getEntitiesWithinAABB(Entity.class, originalArea);
                    AxisAlignedBB focusArea = new AxisAlignedBB(focusPos.getX(), focusPos.getY() + 1, focusPos.getZ(), focusPos.getX() + 1, Math.min(focusWorld.getHeight(), focusPos.getY() + 2 * focusLevel), focusPos.getZ() + 1).expand(focusLevel - 1, 0, focusLevel - 1);
                    focusWorldEntities = focusWorld.getEntitiesWithinAABB(Entity.class, focusArea);

                    if (focusWorld.equals(worldObj))
                    {
                        if (originalWorldEntities != null && !originalWorldEntities.isEmpty())
                        {
                            for (Entity entity : originalWorldEntities)
                            {
                                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(new BlockPos(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ()), entity, focusStack.getTagCompound().getString(Constants.NBT.OWNER_UUID)));
                            }
                        }

                        if (focusWorldEntities != null && !focusWorldEntities.isEmpty())
                        {
                            for (Entity entity : focusWorldEntities)
                            {
                                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(new BlockPos(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ()), entity, focusStack.getTagCompound().getString(Constants.NBT.OWNER_UUID)));
                            }
                        }
                    } else
                    {
                        if (originalWorldEntities != null && !originalWorldEntities.isEmpty())
                        {
                            for (Entity entity : originalWorldEntities)
                            {
                                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportToDim(new BlockPos(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ()), entity, focusStack.getTagCompound().getString(Constants.NBT.OWNER_UUID), worldObj, focusWorld.provider.getDimension()));
                            }
                        }

                        if (focusWorldEntities != null && !focusWorldEntities.isEmpty())
                        {
                            for (Entity entity : focusWorldEntities)
                            {
                                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportToDim(new BlockPos(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ()), entity, focusStack.getTagCompound().getString(Constants.NBT.OWNER_UUID), focusWorld, worldObj.provider.getDimension()));
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean canInitiateTeleport(TileTeleposer teleposer)
    {
        return teleposer.getStackInSlot(0) != null && teleposer.getStackInSlot(0).getItem() instanceof ItemTelepositionFocus && !Strings.isNullOrEmpty(((ItemTelepositionFocus) teleposer.getStackInSlot(0).getItem()).getOwnerName(teleposer.getStackInSlot(0)));
    }
}
