package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.altar.ComponentType;
import WayofTime.bloodmagic.altar.IAltarComponent;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.EnumMimic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.tile.TileMimic;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockMimic extends BlockEnum<EnumMimic> implements IAltarComponent {
    public static final int sentientMimicMeta = 4;

    public BlockMimic() {
        super(Material.ROCK, EnumMimic.class);

        setUnlocalizedName(BloodMagic.MODID + ".mimic.");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 0);
        setLightOpacity(15);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        switch (this.getMetaFromState(state)) {
            case 1:
            case 2:
            case 3:
            case 4:
                TileMimic tileMimic = (TileMimic) world.getTileEntity(pos);
                if (tileMimic != null && !tileMimic.getStackInSlot(0).isEmpty()) {
                    Block mimicBlock = Block.getBlockFromItem(tileMimic.getStackInSlot(0).getItem());
                    if (mimicBlock == Blocks.AIR) {
                        return FULL_BLOCK_AABB;
                    }
                    IBlockState mimicState = mimicBlock.getStateFromMeta(tileMimic.metaOfReplacedBlock);
                    if (mimicBlock != this) {
                        return mimicState.getCollisionBoundingBox(world, pos);
                    }
                } else {
                    return FULL_BLOCK_AABB;
                }
            case 0:
            default:
                return NULL_AABB;
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
        TileMimic tileMimic = (TileMimic) world.getTileEntity(pos);
        if (tileMimic != null && !tileMimic.getStackInSlot(0).isEmpty()) {
            Block mimicBlock = Block.getBlockFromItem(tileMimic.getStackInSlot(0).getItem());
            if (mimicBlock == Blocks.AIR) {
                return FULL_BLOCK_AABB;
            }
            IBlockState mimicState = mimicBlock.getStateFromMeta(tileMimic.getStackInSlot(0).getItemDamage());
            if (mimicBlock != this) {
                return mimicState.getSelectedBoundingBox(world, pos);
            }
        }

        return FULL_BLOCK_AABB;
    }

    @Override
    public int getLightOpacity(IBlockState state) {
        switch (this.getMetaFromState(state)) {
            case 2:
            case 4:
                return 0;
            default:
                return this.lightOpacity;
        }
    }

    @Override
    public int getLightValue(IBlockState state) {
        switch (this.getMetaFromState(state)) {
            case 3:
                return 15;
            default:
                return this.lightValue;
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getBlock() == this) {
            return super.getMetaFromState(state);
        }

        return state.getBlock().getMetaFromState(state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileMimic mimic = (TileMimic) world.getTileEntity(pos);

        return mimic != null && mimic.onBlockActivated(world, pos, state, player, hand, player.getHeldItem(hand), side);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileMimic) {
            TileMimic mimic = (TileMimic) tile;
            ItemStack stack = mimic.getStackInSlot(0);
            if (stack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                IBlockState mimicState = block.getStateFromMeta(stack.getItemDamage());
                if (block != this) {
                    if (block.getRenderType(mimicState) == EnumBlockRenderType.ENTITYBLOCK_ANIMATED) {
                        return RegistrarBloodMagicBlocks.BLOOD_LIGHT.getDefaultState(); //Small and invisible-ish, basically this is returned in order to not render over the animated block (TESR)
                    }

                    return block.getActualState(mimicState, world, pos);
                }
            }
        }
        return state;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.CUTOUT;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof TileMimic) {
            TileMimic TileMimic = (TileMimic) world.getTileEntity(blockPos);
            if (TileMimic != null)
                TileMimic.dropItems();
        }

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileMimic();
    }

    // IAltarComponent

    @Nullable
    @Override
    public ComponentType getType(World world, IBlockState state, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileMimic) {
            TileMimic mimic = (TileMimic) tile;
            ItemStack stack = mimic.getStackInSlot(0);
            if (stack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (block instanceof IAltarComponent) {
                    return ((IAltarComponent) block).getType(world, block.getStateFromMeta(mimic.metaOfReplacedBlock), pos);
                } else {
                    for (ComponentType altarComponent : ComponentType.values())
                        if (block == Utils.getBlockForComponent(altarComponent))
                            return altarComponent;
                }
            }
        }
        return null;
    }
}
