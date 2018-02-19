package WayofTime.bloodmagic.item.alchemy;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.ItemEnum;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.types.ISubItem;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.collect.Iterables;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ItemLivingArmourPointsUpgrade extends ItemEnum.Variant<ItemLivingArmourPointsUpgrade.UpgradeType> {

    public ItemLivingArmourPointsUpgrade() {
        super(UpgradeType.class, "living_point_upgrade");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.bloodmagic.livingArmourPointsUpgrade.desc", 200))));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        playerIn.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (!(entityLiving instanceof EntityPlayer))
            return super.onItemUseFinish(stack, worldIn, entityLiving);

        EntityPlayer player = (EntityPlayer) entityLiving;

        if (!player.capabilities.isCreativeMode)
            stack.shrink(1);

        if (!worldIn.isRemote) {
            player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 300, 5));
            player.addPotionEffect(new PotionEffect(MobEffects.POISON, 300, 5));
            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));

            if (LivingArmour.hasFullSet(player)) {
                ItemStack chestStack = Iterables.toArray(player.getArmorInventoryList(), ItemStack.class)[2];
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null) {
                    if (armour.maxUpgradePoints < 200) {
                        armour.maxUpgradePoints = 200;
                        ((ItemLivingArmour) chestStack.getItem()).setLivingArmour(chestStack, armour, true);
                        ItemLivingArmour.setLivingArmour(chestStack, armour);
                    }
                }
            }
        }

        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    public enum UpgradeType implements ISubItem {

        DRAFT_ANGELUS,
        ;

        @Nonnull
        @Override
        public String getInternalName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Nonnull
        @Override
        public ItemStack getStack(int count) {
            return new ItemStack(RegistrarBloodMagicItems.POINTS_UPGRADE, count, ordinal());
        }
    }
}
