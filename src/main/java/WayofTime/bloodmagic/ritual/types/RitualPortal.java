package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.teleport.PortalLocation;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.ritual.portal.LocationsHandler;
import WayofTime.bloodmagic.tile.TileDimensionalPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.UUID;
import java.util.function.Consumer;

@RitualRegister("portal")
public class RitualPortal extends Ritual {

    public static final String PORTAL_NBT_TAG = "PortalRitualTag";
    public static final String PORTAL_ID_TAG = "PortalRitualID";
    private NBTTagCompound portalRitualTag;

    public RitualPortal() {
        super("ritualPortal", 0, 50000, "ritual." + BloodMagic.MODID + ".portalRitual");
        portalRitualTag = new NBTTagCompound();
    }

    @Override
    public boolean activateRitual(IMasterRitualStone masterRitualStone, EntityPlayer player, UUID owner) {
        World world = masterRitualStone.getWorldObj();
        int x = masterRitualStone.getBlockPos().getX();
        int y = masterRitualStone.getBlockPos().getY();
        int z = masterRitualStone.getBlockPos().getZ();
        EnumFacing direction = masterRitualStone.getDirection();

        String name = owner.toString();
        IBlockState blockState;

        if (!world.isRemote) {
            portalRitualTag.removeTag(PORTAL_ID_TAG);

            if (direction == EnumFacing.NORTH || direction == EnumFacing.SOUTH) {
                for (int i = x - 3; i <= x + 3; i++) {
                    for (int k = z - 2; k <= z + 2; k++) {
                        if (!world.isAirBlock(new BlockPos(i, y, k)) && !(getBlockState(world, i, y, k).getBlock() == RegistrarBloodMagicBlocks.RITUAL_STONE)) {
                            blockState = getBlockState(world, i, y, k);
                            name = addStringToEnd(name, ForgeRegistries.BLOCKS.getKey(blockState.getBlock()) + String.valueOf(blockState.getBlock().getMetaFromState(blockState)));
                        }
                    }
                }
                for (int j = y + 1; j <= y + 5; j++) {
                    if (!world.isAirBlock(new BlockPos(x - 3, j, z)) && !(getBlockState(world, x - 3, j, z).getBlock() == RegistrarBloodMagicBlocks.RITUAL_STONE)) {
                        blockState = getBlockState(world, x - 3, j, z);
                        name = addStringToEnd(name, ForgeRegistries.BLOCKS.getKey(blockState.getBlock()) + String.valueOf(blockState.getBlock().getMetaFromState(blockState)));
                    }
                }
                for (int j = y + 1; j <= y + 5; j++) {
                    if (!world.isAirBlock(new BlockPos(x + 3, j, z)) && !(getBlockState(world, x + 3, j, z) == RegistrarBloodMagicBlocks.RITUAL_STONE)) {
                        blockState = getBlockState(world, x + 3, j, z);
                        name = addStringToEnd(name, ForgeRegistries.BLOCKS.getKey(blockState.getBlock()) + String.valueOf(blockState.getBlock().getMetaFromState(blockState)));
                    }
                }
            } else if (direction == EnumFacing.EAST || direction == EnumFacing.WEST) {
                for (int k = z - 3; k <= z + 3; k++) {
                    for (int i = x - 2; i <= x + 2; i++) {
                        if (!world.isAirBlock(new BlockPos(i, y, k)) && !(getBlockState(world, i, y, k).getBlock() == RegistrarBloodMagicBlocks.RITUAL_STONE)) {
                            blockState = getBlockState(world, i, y, k);
                            name = addStringToEnd(name, ForgeRegistries.BLOCKS.getKey(blockState.getBlock()) + String.valueOf(blockState.getBlock().getMetaFromState(blockState)));
                        }
                    }
                }
                for (int j = y + 1; j <= y + 5; j++) {
                    if (!world.isAirBlock(new BlockPos(x, j, z - 3)) && !(getBlockState(world, x, j, z - 3).getBlock() == RegistrarBloodMagicBlocks.RITUAL_STONE)) {
                        blockState = getBlockState(world, x, j, z - 3);
                        name = addStringToEnd(name, ForgeRegistries.BLOCKS.getKey(blockState.getBlock()) + String.valueOf(blockState.getBlock().getMetaFromState(blockState)));
                    }
                }
                for (int j = y + 1; j <= y + 5; j++) {
                    if (!world.isAirBlock(new BlockPos(x, j, z + 3)) && !(getBlockState(world, x, j, z + 3).getBlock() == RegistrarBloodMagicBlocks.RITUAL_STONE)) {
                        blockState = getBlockState(world, x, j, z + 3);
                        name = addStringToEnd(name, ForgeRegistries.BLOCKS.getKey(blockState.getBlock()) + String.valueOf(blockState.getBlock().getMetaFromState(blockState)));
                    }
                }
            }
            LocationsHandler.addLocation(name, new PortalLocation(x, y + 1, z, world.provider.getDimension()));
            portalRitualTag.setString(PORTAL_ID_TAG, name);
            return true;
        }
        return false;
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        if (world.isRemote) {
            return;
        }

        int x = masterRitualStone.getBlockPos().getX();
        int y = masterRitualStone.getBlockPos().getY();
        int z = masterRitualStone.getBlockPos().getZ();
        EnumFacing direction = masterRitualStone.getDirection();

        if (direction == EnumFacing.NORTH || direction == EnumFacing.SOUTH) {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y + 1; j <= y + 3; j++) {
                    BlockPos tempPos = new BlockPos(i, j, z);

                    if (world.isAirBlock(tempPos)) {
                        IBlockState blockState = RegistrarBloodMagicBlocks.DIMENSIONAL_PORTAL.getStateFromMeta(0);
                        world.setBlockState(tempPos, blockState, 3);

                        if (world.getTileEntity(tempPos) != null && world.getTileEntity(tempPos) instanceof TileDimensionalPortal) {
                            TileDimensionalPortal tile = (TileDimensionalPortal) world.getTileEntity(tempPos);
                            tile.setMasterStonePos(masterRitualStone.getBlockPos());
                            tile.portalID = portalRitualTag.getString(PORTAL_ID_TAG);
                        }
                    }
                }
            }
        } else if (direction == EnumFacing.EAST || direction == EnumFacing.WEST) {
            for (int k = z - 1; k <= z + 1; k++) {
                for (int j = y + 1; j <= y + 3; j++) {
                    BlockPos tempPos = new BlockPos(x, j, k);
                    if (world.isAirBlock(tempPos)) {
                        IBlockState blockState = RegistrarBloodMagicBlocks.DIMENSIONAL_PORTAL.getStateFromMeta(1);
                        world.setBlockState(tempPos, blockState, 3);

                        if (world.getTileEntity(tempPos) != null && world.getTileEntity(tempPos) instanceof TileDimensionalPortal) {
                            TileDimensionalPortal tile = (TileDimensionalPortal) world.getTileEntity(tempPos);
                            tile.setMasterStonePos(masterRitualStone.getBlockPos());
                            tile.portalID = portalRitualTag.getString(PORTAL_ID_TAG);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void stopRitual(IMasterRitualStone masterRitualStone, BreakType breakType) {
        World world = masterRitualStone.getWorldObj();
        int x = masterRitualStone.getBlockPos().getX();
        int y = masterRitualStone.getBlockPos().getY();
        int z = masterRitualStone.getBlockPos().getZ();
        EnumFacing direction = masterRitualStone.getDirection();

        LocationsHandler.removeLocation(portalRitualTag.getString(PORTAL_ID_TAG), new PortalLocation(x, y + 1, z, world.provider.getDimension()));

        if (direction == EnumFacing.NORTH || direction == EnumFacing.SOUTH) {
            for (int i = x - 2; i <= x + 2; i++) {
                for (int j = y + 1; j <= y + 3; j++) {
                    if (getBlockState(world, i, j, z).getBlock() == RegistrarBloodMagicBlocks.DIMENSIONAL_PORTAL) {
                        world.setBlockToAir(new BlockPos(i, j, z));
                    }
                }
            }
        } else if (direction == EnumFacing.EAST || direction == EnumFacing.WEST) {
            for (int k = z - 2; k <= z + 2; k++) {
                for (int j = y + 1; j <= y + 3; j++) {
                    if (getBlockState(world, x, j, k).getBlock() == RegistrarBloodMagicBlocks.DIMENSIONAL_PORTAL) {
                        world.setBlockToAir(new BlockPos(x, j, k));
                    }
                }
            }
        }

        portalRitualTag.removeTag(portalRitualTag.getString(PORTAL_ID_TAG));
    }

    @Override
    public int getRefreshCost() {
        return 0;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addRune(components, 1, 0, 0, EnumRuneType.AIR);
        addRune(components, 2, 0, 0, EnumRuneType.WATER);
        addRune(components, -1, 0, 0, EnumRuneType.FIRE);
        addRune(components, -2, 0, 0, EnumRuneType.EARTH);
        addRune(components, 2, 1, 0, EnumRuneType.DUSK);

        addRune(components, 2, 2, 0, EnumRuneType.AIR);
        addRune(components, 2, 3, 0, EnumRuneType.WATER);
        addRune(components, 2, 4, 0, EnumRuneType.FIRE);
        addRune(components, 1, 4, 0, EnumRuneType.EARTH);
        addRune(components, 0, 4, 0, EnumRuneType.DUSK);

        addRune(components, -1, 4, 0, EnumRuneType.AIR);
        addRune(components, -2, 4, 0, EnumRuneType.WATER);
        addRune(components, -2, 3, 0, EnumRuneType.FIRE);
        addRune(components, -2, 2, 0, EnumRuneType.EARTH);
        addRune(components, -2, 1, 0, EnumRuneType.DUSK);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualPortal();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        portalRitualTag = tag.getCompoundTag(PORTAL_NBT_TAG);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setTag(PORTAL_NBT_TAG, portalRitualTag);
    }

    public IBlockState getBlockState(World world, int x, int y, int z) {
        return world.getBlockState(new BlockPos(x, y, z));
    }

    public String addStringToEnd(String input, String toAdd) {
        return input + toAdd;
    }
}
