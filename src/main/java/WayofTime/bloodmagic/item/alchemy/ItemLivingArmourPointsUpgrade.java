package WayofTime.bloodmagic.item.alchemy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
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
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.collect.Iterables;

public class ItemLivingArmourPointsUpgrade extends Item implements IVariantProvider
{
    @Getter
    private static ArrayList<String> names = new ArrayList<String>();

    public static final String DRAFT_ANGELUS = "draftAngelus";

    public ItemLivingArmourPointsUpgrade()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".livingPointUpgrade.");
        setHasSubtypes(true);
        setCreativeTab(BloodMagic.tabBloodMagic);

        buildItemList();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        if (!stack.hasTagCompound())
            return;

        tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.BloodMagic.livingArmourPointsUpgrade.desc", 200))));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        EntityPlayer player = entityLiving instanceof EntityPlayer ? (EntityPlayer) entityLiving : null;

        if (player == null || !player.capabilities.isCreativeMode)
        {
            --stack.stackSize;
        }

        if (!worldIn.isRemote)
        {
            player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 300, 5));
            player.addPotionEffect(new PotionEffect(MobEffects.POISON, 300, 5));
            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));

            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = Iterables.toArray(player.getArmorInventoryList(), ItemStack.class)[2];
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null)
                {
                    if (armour.maxUpgradePoints < 200)
                    {
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
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        playerIn.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }

    private void buildItemList()
    {
        names.add(0, DRAFT_ANGELUS);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names.get(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (int i = 0; i < names.size(); i++)
            list.add(new ItemStack(id, 1, i));
    }

    public static ItemStack getStack(String name)
    {
        return new ItemStack(ModItems.itemPointsUpgrade, 1, names.indexOf(name));
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (String name : names)
            ret.add(new ImmutablePair<Integer, String>(names.indexOf(name), "type=" + name));
        return ret;
    }

    public static ItemStack getStack(String key, int stackSize)
    {
        ItemStack stack = getStack(key);
        stack.stackSize = stackSize;

        return stack;
    }
}
