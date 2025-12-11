package wayoftime.bloodmagic.ritual.harvest;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import wayoftime.bloodmagic.util.helper.BlockProtectionHelper;

/**
 * Harvest handler for crops that grow vertically such as Sugar Cane and Cactus.
 * <br>
 * Register a new crop for this handler with
 * {@link HarvestRegistry#registerTallCrop(BlockState)}
 */
public class HarvestHandlerTall implements IHarvestHandler
{
	private static final ItemStack mockHoe = new ItemStack(Items.DIAMOND_HOE, 1);

	public HarvestHandlerTall()
	{
		// bottom block is set to age 0 when one grows above
		HarvestRegistry.registerTallCrop(Blocks.SUGAR_CANE.defaultBlockState().setValue(SugarCaneBlock.AGE, 0));
		HarvestRegistry.registerTallCrop(Blocks.CACTUS.defaultBlockState().setValue(CactusBlock.AGE, 0));

		HarvestRegistry.registerTallCrop(Blocks.BAMBOO.defaultBlockState().setValue(BambooStalkBlock.STAGE, 0).setValue(BambooStalkBlock.AGE, 1).setValue(BambooStalkBlock.LEAVES, BambooLeaves.NONE));
	}

	@Override
	public boolean harvest(Level world, BlockPos pos, BlockState state, List<ItemStack> drops, @Nullable UUID ownerUUID)
	{
		BlockState up = world.getBlockState(pos.above());
		if (up.getBlock() == state.getBlock())
		{
			if (!BlockProtectionHelper.tryBreakBlockNoDrops(world, pos.above(), ownerUUID))
			{
				return false;
			}
			LootParams.Builder lootBuilder = new LootParams.Builder((ServerLevel) world);
			Vec3 blockCenter = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			List<ItemStack> blockDrops = state.getDrops(lootBuilder.withParameter(LootContextParams.ORIGIN, blockCenter).withParameter(LootContextParams.TOOL, mockHoe));
			drops.addAll(blockDrops);
			return true;
		}

		return false;
	}

	@Override
	public boolean test(Level world, BlockPos pos, BlockState state)
	{
		return HarvestRegistry.getTallCrops().contains(state);
	}
}