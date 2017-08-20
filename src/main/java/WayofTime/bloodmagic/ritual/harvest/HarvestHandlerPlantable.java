package WayofTime.bloodmagic.ritual.harvest;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.iface.IHarvestHandler;
import WayofTime.bloodmagic.api.registry.HarvestRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Harvest handler for standard plantable crops such as Wheat, Potatoes, and
 * Netherwart. <br>
 * Register a new crop for this handler with
 * {@link HarvestRegistry#registerStandardCrop(Block, int)}
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
    public boolean harvestAndPlant(World world, BlockPos pos, BlockStack blockStack) {
        if (!HarvestRegistry.getStandardCrops().containsKey(blockStack.getBlock()))
            return false;

        int matureMeta = HarvestRegistry.getStandardCrops().get(blockStack.getBlock());

        if (blockStack.getMeta() < matureMeta)
            return false;

        List<ItemStack> drops = blockStack.getBlock().getDrops(world, pos, blockStack.getState(), 0);
        boolean foundSeed = false;

        for (ItemStack stack : drops) {
            if (stack.isEmpty())
                continue;

            if (stack.getItem() instanceof IPlantable) {
                stack.shrink(1);
                foundSeed = true;
                break;
            }
        }

        if (foundSeed) {
            world.setBlockState(pos, blockStack.getBlock().getDefaultState());
            world.playEvent(2001, pos, Block.getStateId(blockStack.getState()));
            for (ItemStack stack : drops) {
                if (stack.isEmpty())
                    continue;

                if (!world.isRemote) {
                    EntityItem toDrop = new EntityItem(world, pos.getX(), pos.getY() + 0.5, pos.getZ(), stack);
                    world.spawnEntity(toDrop);
                }
            }

            return true;
        }

        return false;
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
            ClassLoader loader = HarvestHandlerPlantable.class.getClassLoader();
            String className = "com.pam.harvestcraft.blocks.CropRegistry";
            Class<?> registry = ReflectionHelper.getClass(loader, className);
            Field names = ReflectionHelper.findField(registry, "cropNames");
            Method getCrop = registry.getMethod("getCrop", String.class);
            for (String name : (String[]) names.get(null)) {
                BlockCrops crop = (BlockCrops) getCrop.invoke(null, name);
                HarvestRegistry.registerStandardCrop(crop, crop.getMaxAge());
            }
        } catch (NoSuchMethodException e) {
            BloodMagic.LOGGER.error("HarvestCraft integration cancelled; unable to find crop name mapper");
        } catch (IllegalAccessException e) {
            BloodMagic.LOGGER.error("HarvestCraft integration cancelled; crop name lookup broke");
        } catch (InvocationTargetException e) {
            BloodMagic.LOGGER.error("HarvestCraft integration cancelled; crop name lookup broke");
        }
    }
}
