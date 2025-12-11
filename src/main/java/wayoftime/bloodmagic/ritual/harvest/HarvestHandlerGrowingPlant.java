package wayoftime.bloodmagic.ritual.harvest;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.util.helper.BlockProtectionHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class HarvestHandlerGrowingPlant implements IHarvestHandler {

    private static final ItemStack mockHoe = new ItemStack(Items.DIAMOND_HOE, 1);

    @Override
    public boolean harvest(Level world, BlockPos pos, BlockState state, List<ItemStack> drops, @Nullable UUID ownerUUID) {
        if (!BlockProtectionHelper.tryBreakBlockNoDrops(world, pos, ownerUUID)) {
            return false;
        }
        LootParams.Builder lootBuilder = new LootParams.Builder((ServerLevel) world);
        Vec3 blockCenter = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        List<ItemStack> blockDrops = state.getDrops(lootBuilder.withParameter(LootContextParams.ORIGIN, blockCenter).withParameter(LootContextParams.TOOL, mockHoe));
        drops.addAll(blockDrops);
        return true;
    }

    @Override
    public boolean test(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof GrowingPlantHeadBlock head) {
            return world.getBlockState(pos.relative(head.growthDirection.getOpposite())).getBlock() instanceof GrowingPlantBodyBlock;
        }

        return false;
    }
}
