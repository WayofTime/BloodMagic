package wayoftime.bloodmagic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.tile.TileSoulForge;

public class BlockSoulForge extends Block// implements IBMBlock
{
	protected static final VoxelShape BODY = Block.makeCuboidShape(1, 0, 1, 15, 12, 15);

	public BlockSoulForge()
	{
		super(Properties.create(Material.IRON).hardnessAndResistance(2.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));

//		setTranslationKey(BloodMagic.MODID + ".soulForge");
//		setHardness(2.0F);
//		setResistance(5.0F);
//		setSoundType(SoundType.METAL);
//		setHarvestLevel("pickaxe", 1);
//		setCreativeTab(BloodMagic.TAB_BM);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return BODY;
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileSoulForge();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult)
	{
		if (world.isRemote)
			return ActionResultType.SUCCESS;

		TileEntity tile = world.getTileEntity(pos);
		if (!(tile instanceof TileSoulForge))
			return ActionResultType.FAIL;

		NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tile, pos);
//			player.openGui(BloodMagic.instance, Constants.Gui.SOUL_FORGE_GUI, world, pos.getX(), pos.getY(), pos.getZ());

		return ActionResultType.SUCCESS;
	}

//	@Override
//	public void onPlayerDestroy(IWorld world, BlockPos blockPos, BlockState blockState)
//	{
//		TileSoulForge tileSoulForge = (TileSoulForge) world.getTileEntity(blockPos);
//		if (tileSoulForge != null)
//			tileSoulForge.dropItems();
//
//		super.breakBlock(world, blockPos, blockState);
//	}

//
//	@Override
//	public BlockItem getItem()
//	{
//		return new BlockItem(this);
//	}
}
