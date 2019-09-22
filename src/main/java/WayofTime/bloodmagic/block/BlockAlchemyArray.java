package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectMovement;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectUpdraft;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockAlchemyArray extends Block {
    protected static final AxisAlignedBB ARRAY_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.1, 1);

    public BlockAlchemyArray() {
        super(Material.CLOTH);

        setTranslationKey(BloodMagic.MODID + ".alchemyArray");
        setHardness(0.1f);
    }

    @Override
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        // No-op
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileAlchemyArray) {
            ((TileAlchemyArray) tile).onEntityCollidedWithBlock(state, entity);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return ARRAY_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public boolean causesSuffocation(BlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
        TileAlchemyArray array = (TileAlchemyArray) world.getTileEntity(pos);

        if (array == null)
            return false;
        if (player.isSneaking() && array.rotateCooldown == 0) {
            array.setRotation(array.getRotation().rotateY());
            array.rotateCooldown = 5;
            world.notifyBlockUpdate(pos, state, state, 3);
            return false;
        }

        ItemStack playerItem = player.getHeldItem(hand);

        if (!playerItem.isEmpty()) {
            if (array.getStackInSlot(0).isEmpty()) {
                Utils.insertItemToTile(array, player, 0);
            } else if (!array.getStackInSlot(0).isEmpty()) {
                Utils.insertItemToTile(array, player, 1);
                array.attemptCraft();
            } else {
                return true;
            }
            if (array.arrayEffect instanceof AlchemyArrayEffectMovement && (playerItem.getItem() == Items.REDSTONE || playerItem.getItem() == Items.FEATHER)
                    || array.arrayEffect instanceof AlchemyArrayEffectUpdraft && (playerItem.getItem() == Items.FEATHER || playerItem.getItem() == Items.GLOWSTONE_DUST)) {
                for (int i = 0; i < array.getSizeInventory(); i++) {
                    ItemStack stack = array.getStackInSlot(i);
                    if (ItemStack.areItemsEqual(stack, playerItem)) {
                        if (stack.getCount() < 127) {
                            stack.setCount(stack.getCount() + 1);
                            playerItem.shrink(1);
                        }
                        break;
                    }
                }
                world.notifyBlockUpdate(pos, state, state, 3);
                return true;
            }
        }

        world.notifyBlockUpdate(pos, state, state, 3);
        return true;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(RegistrarBloodMagicItems.ARCANE_ASHES);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, BlockState blockState) {
        TileAlchemyArray alchemyArray = (TileAlchemyArray) world.getTileEntity(blockPos);
        if (alchemyArray != null)
            alchemyArray.dropItems();

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, BlockState state) {
        return new TileAlchemyArray();
    }
}
