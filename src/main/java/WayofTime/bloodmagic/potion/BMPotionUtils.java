package WayofTime.bloodmagic.potion;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.DamageSourceBloodMagic;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.recipe.alchemyTable.AlchemyTablePotionAugmentRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class BMPotionUtils {
    public static Random rand = new Random();

    public static double damageMobAndGrowSurroundingPlants(EntityLivingBase entity, int horizontalRadius, int verticalRadius, double damageRatio, int maxPlantsGrown) {
        World world = entity.getEntityWorld();
        if (world.isRemote) {
            return 0;
        }

        if (entity.isDead) {
            return 0;
        }

        double incurredDamage = 0;

        List<BlockPos> growList = new ArrayList<>();

        for (int i = 0; i < maxPlantsGrown; i++) {
            BlockPos blockPos = entity.getPosition().add(rand.nextInt(horizontalRadius * 2 + 1) - horizontalRadius, rand.nextInt(verticalRadius * 2 + 1) - verticalRadius, rand.nextInt(horizontalRadius * 2 + 1) - horizontalRadius);
            IBlockState state = world.getBlockState(blockPos);

            if (!BloodMagicAPI.INSTANCE.getBlacklist().getGreenGrove().contains(state)) {
                if (state.getBlock() instanceof IGrowable) {
                    growList.add(blockPos);
                }
            }
        }

        for (BlockPos blockPos : growList) {
            Block block = world.getBlockState(blockPos).getBlock();
//          if (world.rand.nextInt(50) == 0)
            {
                IBlockState preBlockState = world.getBlockState(blockPos);
                for (int n = 0; n < 10; n++)
                    block.updateTick(world, blockPos, world.getBlockState(blockPos), world.rand);

                IBlockState newState = world.getBlockState(blockPos);
                if (!newState.equals(preBlockState)) {
                    world.playEvent(2001, blockPos, Block.getIdFromBlock(newState.getBlock()) + (newState.getBlock().getMetaFromState(newState) << 12));
                    incurredDamage += damageRatio;
                }
            }
        }

        if (incurredDamage > 0) {
            entity.attackEntityFrom(DamageSourceBloodMagic.INSTANCE, (float) incurredDamage);
        }

        return incurredDamage;
    }

    public static double getLengthAugment(ItemStack flaskStack, Potion potion) {
        NBTHelper.checkNBT(flaskStack);
        NBTTagCompound tag = flaskStack.getTagCompound();

        return tag.getDouble(Constants.NBT.POTION_AUGMENT_LENGHT + potion.getName());
    }

    public static void setLengthAugment(ItemStack flaskStack, Potion potion, double value) {
        if (value < 0) {
            value = 0;
        }

        NBTHelper.checkNBT(flaskStack);
        NBTTagCompound tag = flaskStack.getTagCompound();

        tag.setDouble(Constants.NBT.POTION_AUGMENT_LENGHT + potion.getName(), value);
    }

    public static int getAugmentedLength(int originalLength, double lengthAugment, double powerAugment) {
        return Math.max((int) (originalLength * (Math.pow(8f / 3f, lengthAugment) * Math.pow(0.5, powerAugment))), 1);
    }

    /**
     * Copied from PotionUtils
     *
     * @param stack
     * @param effects
     * @return
     */
    public static ItemStack setEffects(ItemStack stack, Collection<PotionEffect> effects) {
        if (effects.isEmpty()) {
            return stack;
        } else {
            NBTTagCompound nbttagcompound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
            NBTTagList nbttaglist = new NBTTagList();

            for (PotionEffect potioneffect : effects) {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            nbttagcompound.setTag("CustomPotionEffects", nbttaglist);
            stack.setTagCompound(nbttagcompound);
            return stack;
        }
    }

    public static AlchemyTablePotionAugmentRecipe getLengthAugmentRecipe(int lpDrained, int ticksRequired, int tierRequired, List<ItemStack> inputItems, PotionEffect baseEffect, double lengthAugment) {
        return new AlchemyTablePotionAugmentRecipe(lpDrained, ticksRequired, tierRequired, inputItems, baseEffect, lengthAugment, 0);
    }

    public static AlchemyTablePotionAugmentRecipe getPowerAugmentRecipe(int lpDrained, int ticksRequired, int tierRequired, List<ItemStack> inputItems, PotionEffect baseEffect, int powerAugment) {
        return new AlchemyTablePotionAugmentRecipe(lpDrained, ticksRequired, tierRequired, inputItems, baseEffect, 0, powerAugment);
    }

    public static AlchemyTablePotionAugmentRecipe getLengthAugmentRecipe(int lpDrained, int ticksRequired, int tierRequired, ItemStack inputItem, PotionEffect baseEffect, double lengthAugment) {
        return new AlchemyTablePotionAugmentRecipe(lpDrained, ticksRequired, tierRequired, inputItem, baseEffect, lengthAugment, 0);
    }

    public static AlchemyTablePotionAugmentRecipe getPowerAugmentRecipe(int lpDrained, int ticksRequired, int tierRequired, ItemStack inputItem, PotionEffect baseEffect, int powerAugment) {
        return new AlchemyTablePotionAugmentRecipe(lpDrained, ticksRequired, tierRequired, inputItem, baseEffect, 0, powerAugment);
    }
}
