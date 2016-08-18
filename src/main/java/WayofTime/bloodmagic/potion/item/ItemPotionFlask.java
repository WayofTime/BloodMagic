package WayofTime.bloodmagic.potion.item;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.client.IVariantProvider;

public class ItemPotionFlask extends Item implements IVariantProvider
{
    public ItemPotionFlask()
    {
        setUnlocalizedName(Constants.Mod.MODID + ".potionFlask");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setMaxStackSize(1);
        setMaxDamage(8);
        setNoRepair();
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving)
    {
        EntityPlayer player = entityLiving instanceof EntityPlayer ? (EntityPlayer) entityLiving : null;

        int remainingUses = stack.getMaxDamage() - stack.getItemDamage();
        if (remainingUses <= 0)
        {
            return stack;
        }

        if (player == null || !player.capabilities.isCreativeMode)
        {
            stack.setItemDamage(stack.getItemDamage() + 1);
        }

        if (!world.isRemote)
        {
            for (PotionEffect potioneffect : PotionUtils.getEffectsFromStack(stack))
            {
                entityLiving.addPotionEffect(new PotionEffect(potioneffect));
            }
        }

        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        int remainingUses = stack.getMaxDamage() - stack.getItemDamage();
        if (remainingUses <= 0)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
        }
        player.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
        tooltip.add("");
        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.potion.uses", stack.getMaxDamage() - stack.getItemDamage()));
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
//    {
//        for (PotionType potiontype : PotionType.REGISTRY)
//        {
//            subItems.add(PotionUtils.addPotionToItemStack(new ItemStack(itemIn), potiontype));
//        }
//    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=normal"));
        return ret;
    }
}