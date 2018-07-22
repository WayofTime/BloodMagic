package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.event.TeleposeEvent;
import WayofTime.bloodmagic.teleport.TeleportQueue;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import WayofTime.bloodmagic.block.BlockTeleposer;
import WayofTime.bloodmagic.item.ItemTelepositionFocus;
import WayofTime.bloodmagic.ritual.portal.Teleports;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class TileTeleposer extends TileInventory implements ITickable {
    //TODO FUTURE: Make AreaDescriptor for Teleposer perhaps?
    public static final String TELEPOSER_RANGE = "teleposerRange";

    private int previousInput;

    public TileTeleposer() {
        super(1, "teleposer");
    }

    @Override
    public void deserialize(NBTTagCompound tagCompound) {
        super.deserialize(tagCompound);
        previousInput = tagCompound.getInteger(Constants.NBT.PREVIOUS_INPUT);
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tagCompound) {
        super.serialize(tagCompound);
        tagCompound.setInteger(Constants.NBT.PREVIOUS_INPUT, previousInput);
        return tagCompound;
    }

    @Override
    public void update() {
        if (!getWorld().isRemote) {
            int currentInput = getWorld().getStrongPower(pos);

            if (previousInput == 0 && currentInput != 0) {
                initiateTeleport();
            }

            previousInput = currentInput;
        }
    }

    public void initiateTeleport() {
        if (!getWorld().isRemote && canInitiateTeleport() && getBlockType() instanceof BlockTeleposer) {
            ItemStack focusStack = getStackInSlot(0);
            ItemTelepositionFocus focus = (ItemTelepositionFocus) focusStack.getItem();
            Binding binding = focus.getBinding(focusStack);
            if (binding == null)
                return;
            BlockPos focusPos = focus.getBlockPos(getStackInSlot(0));
            World focusWorld = focus.getWorld(getStackInSlot(0));
            if (focusWorld == null)
                return;

            TileEntity boundTile = focusWorld.getTileEntity(focusPos);
            if (boundTile instanceof TileTeleposer && boundTile != this) {
                final int focusLevel = (getStackInSlot(0).getItemDamage() + 1);
                final int lpToBeDrained = (int) (0.5F * Math.sqrt((pos.getX() - focusPos.getX()) * (pos.getX() - focusPos.getX()) + (pos.getY() - focusPos.getY() + 1) * (pos.getY() - focusPos.getY() + 1) + (pos.getZ() - focusPos.getZ()) * (pos.getZ() - focusPos.getZ())));

                if (NetworkHelper.getSoulNetwork(binding).syphonAndDamage(PlayerHelper.getPlayerFromUUID(binding.getOwnerId()), SoulTicket.block(world, pos, lpToBeDrained * (focusLevel * 2 - 1) * (focusLevel * 2 - 1) * (focusLevel * 2 - 1))).isSuccess()) {
                    int blocksTransported = 0;

                    for (int i = -(focusLevel - 1); i <= (focusLevel - 1); i++) {
                        for (int j = 0; j <= (focusLevel * 2 - 2); j++) {
                            for (int k = -(focusLevel - 1); k <= (focusLevel - 1); k++) {
                                TeleposeEvent event = new TeleposeEvent(getWorld(), pos.add(i, 1 + j, k), focusWorld, focusPos.add(i, 1 + j, k));
                                if (!MinecraftForge.EVENT_BUS.post(event) && Utils.swapLocations(event.initalWorld, event.initialBlockPos, event.finalWorld, event.finalBlockPos)) {
                                    blocksTransported++;
                                }
                            }
                        }
                    }

                    NetworkHelper.syphonFromContainer(focusStack, SoulTicket.item(focusStack, world, pos, lpToBeDrained * blocksTransported));

                    List<Entity> originalWorldEntities;
                    List<Entity> focusWorldEntities;
                    AxisAlignedBB originalArea = new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, Math.min(focusWorld.getHeight(), pos.getY() + 2 * focusLevel), pos.getZ() + 1).expand(focusLevel - 1, 0, focusLevel - 1);
                    originalWorldEntities = getWorld().getEntitiesWithinAABB(Entity.class, originalArea);
                    AxisAlignedBB focusArea = new AxisAlignedBB(focusPos.getX(), focusPos.getY() + 1, focusPos.getZ(), focusPos.getX() + 1, Math.min(focusWorld.getHeight(), focusPos.getY() + 2 * focusLevel), focusPos.getZ() + 1).expand(focusLevel - 1, 0, focusLevel - 1);
                    focusWorldEntities = focusWorld.getEntitiesWithinAABB(Entity.class, focusArea);

                    if (focusWorld.equals(getWorld())) {
                        if (!originalWorldEntities.isEmpty()) {
                            for (Entity entity : originalWorldEntities) {
                                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(new BlockPos(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ()), entity, binding.getOwnerId(), true));
                            }
                        }

                        if (!focusWorldEntities.isEmpty()) {
                            for (Entity entity : focusWorldEntities) {
                                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportSameDim(new BlockPos(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ()), entity, binding.getOwnerId(), true));
                            }
                        }
                    }
                    // FIXME - Fix cross-dimension teleports causing major desync
//                    } else {
//                        if (!originalWorldEntities.isEmpty()) {
//                            for (Entity entity : originalWorldEntities) {
//                                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportToDim(new BlockPos(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ()), entity, focusStack.getTagCompound().getString(Constants.NBT.OWNER_UUID), getWorld(), focusWorld.provider.getDimension(), true));
//                            }
//                        }
//
//                        if (!focusWorldEntities.isEmpty()) {
//                            for (Entity entity : focusWorldEntities) {
//                                TeleportQueue.getInstance().addITeleport(new Teleports.TeleportToDim(new BlockPos(entity.posX - pos.getX() + focusPos.getX(), entity.posY - pos.getY() + focusPos.getY(), entity.posZ - pos.getZ() + focusPos.getZ()), entity, focusStack.getTagCompound().getString(Constants.NBT.OWNER_UUID), focusWorld, getWorld().provider.getDimension(), true));
//                            }
//                        }
//                    }
                }
            }
        }
    }

    private boolean canInitiateTeleport() {
        ItemStack focusStack = getStackInSlot(0);
        return !focusStack.isEmpty() && focusStack.getItem() instanceof ItemTelepositionFocus && ((ItemTelepositionFocus) focusStack.getItem()).getBinding(focusStack) != null;
    }
}
