package WayofTime.bloodmagic.item.soul;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;

public class ItemSentientBow extends ItemBow
{
    public ItemSentientBow()
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".sentientBow");
        setRegistryName(Constants.BloodMagicItem.SENTIENT_BOW.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft)
    {
        boolean flag = playerIn.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;

        if (flag || playerIn.inventory.hasItem(Items.arrow))
        {
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            net.minecraftforge.event.entity.player.ArrowLooseEvent event = new net.minecraftforge.event.entity.player.ArrowLooseEvent(playerIn, stack, i);
            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
                return;
            i = event.charge;
            float f = (float) i / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;

            if ((double) f < 0.1D)
            {
                return;
            }

            if (f > 1.0F)
            {
                f = 1.0F;
            }

            EntityArrow entityarrow = new EntitySentientArrow(worldIn, playerIn, f * 2.0F, 0);

            if (f == 1.0F)
            {
                entityarrow.setIsCritical(true);
            }

            int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);

            if (j > 0)
            {
                entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
            }

            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);

            if (k > 0)
            {
                entityarrow.setKnockbackStrength(k);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
            {
                entityarrow.setFire(100);
            }

            stack.damageItem(1, playerIn);
            worldIn.playSoundAtEntity(playerIn, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

            if (flag)
            {
                entityarrow.canBePickedUp = 2;
            } else
            {
                playerIn.inventory.consumeInventoryItem(Items.arrow);
            }

            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);

            if (!worldIn.isRemote)
            {
                worldIn.spawnEntityInWorld(entityarrow);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
    {
        if (player.getItemInUse() == null)
        {
            return null;
        }

        int i = stack.getMaxItemUseDuration() - player.getItemInUseCount();

        if (i >= 18)
        {
            return new ModelResourceLocation("bloodmagic:ItemSentientBow_pulling_2", "inventory");
        } else if (i > 13)
        {
            return new ModelResourceLocation("bloodmagic:ItemSentientBow_pulling_1", "inventory");
        } else if (i > 0)
        {
            return new ModelResourceLocation("bloodmagic:ItemSentientBow_pulling_0", "inventory");
        }

        return null;
    }
}
