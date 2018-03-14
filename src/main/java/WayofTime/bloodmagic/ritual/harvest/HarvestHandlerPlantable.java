package WayofTime.bloodmagic.ritual.harvest;

import WayofTime.bloodmagic.util.BMLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Harvest handler for standard plantable crops such as Wheat, Potatoes, and
 * Netherwart. <br>
 * Register a new crop for this handler with {@link HarvestRegistry#registerStandardCrop(Block, int)}
 */
public class HarvestHandlerPlantable implements IHarvestHandler {

    public HarvestHandlerPlantable() {
        HarvestRegistry.registerStandardCrop(Blocks.CARROTS, 7);
        HarvestRegistry.registerStandardCrop(Blocks.WHEAT, 7);
        HarvestRegistry.registerStandardCrop(Blocks.POTATOES, 7);
        HarvestRegistry.registerStandardCrop(Blocks.BEETROOTS, 3);
        HarvestRegistry.registerStandardCrop(Blocks.NETHER_WART, 3);

        addThirdPartyCrop("actuallyadditions", "blockFlax", 7);
        addThirdPartyCrop("actuallyadditions", "blockCanola", 7);
        addThirdPartyCrop("actuallyadditions", "blockRice", 7);

        addThirdPartyCrop("extrautils2", "redorchid", 6);
        addThirdPartyCrop("extrautils2", "enderlily", 7);

        addThirdPartyCrop("roots", "moonglow", 7);
        addThirdPartyCrop("roots", "terra_moss", 7);
        addThirdPartyCrop("roots", "pereskia", 7);
        addThirdPartyCrop("roots", "wildroot", 7);
        addThirdPartyCrop("roots", "aubergine", 7);
        addThirdPartyCrop("roots", "spirit_herb", 7);

        addPamCrops();
    }

    @Override
    public boolean harvest(World world, BlockPos pos, IBlockState state, List<ItemStack> drops) {
        NonNullList<ItemStack> blockDrops = NonNullList.create();
        state.getBlock().getDrops(blockDrops, world, pos, state, 0);
        boolean foundSeed = false;

        for (ItemStack stack : blockDrops) {
            if (stack.isEmpty())
                continue;

            if (stack.getItem() instanceof IPlantable) {
                stack.shrink(1);
                foundSeed = true;
                break;
            }
        }

        if (foundSeed) {
            world.setBlockState(pos, state.getBlock().getDefaultState());
            world.playEvent(2001, pos, Block.getStateId(state));
            for (ItemStack stack : blockDrops) {
                if (stack.isEmpty())
                    continue;

                drops.add(stack);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean test(World world, BlockPos pos, IBlockState state) {
        return HarvestRegistry.getStandardCrops().containsKey(state.getBlock()) && state.getBlock().getMetaFromState(state) == HarvestRegistry.getStandardCrops().get(state.getBlock());
    }

    private static void addThirdPartyCrop(String modid, String regName, int matureMeta) {
        if (!Loader.isModLoaded(modid))
            return;

        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(modid, regName));
        if (block != null && block != Blocks.AIR)
            HarvestRegistry.registerStandardCrop(block, matureMeta);
    }

    private static void addPamCrops() {
        if (!Loader.isModLoaded("harvestcraft"))
            return;

        try {
            Class<?> pamRegistry = Class.forName("com.pam.harvestcraft.blocks.CropRegistry");
            Field names = pamRegistry.getDeclaredField("cropNames");
            Method getCrop = pamRegistry.getMethod("getCrop", String.class);
            for (String name : (String[]) names.get(null)) {
                BlockCrops crop = (BlockCrops) getCrop.invoke(null, name);
                HarvestRegistry.registerStandardCrop(crop, crop.getMaxAge());
            }
        } catch (ClassNotFoundException e) {
            BMLog.DEFAULT.error("HarvestCraft integration cancelled; unable to find registry class");
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            BMLog.DEFAULT.error("HarvestCraft integration cancelled; unable to find crop name mapper");
        } catch (IllegalAccessException | InvocationTargetException e) {
            BMLog.DEFAULT.error("HarvestCraft integration cancelled; crop name lookup broke");
        }
    }
}
