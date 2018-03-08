package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.function.Consumer;

public class ItemPotionFlask extends Item implements IMeshProvider {
    public ItemPotionFlask() {
        setUnlocalizedName(BloodMagic.MODID + ".potionFlask");
        setCreativeTab(BloodMagic.TAB_BM);
        setMaxStackSize(1);
        setMaxDamage(8);
        setNoRepair();
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        EntityPlayer player = entityLiving instanceof EntityPlayer ? (EntityPlayer) entityLiving : null;

        int remainingUses = stack.getMaxDamage() - stack.getItemDamage();
        if (remainingUses <= 0) {
            NBTHelper.checkNBT(stack);
            stack.getTagCompound().setBoolean("empty", true);
            return stack;
        }

        if (player == null || !player.capabilities.isCreativeMode) {
            stack.setItemDamage(stack.getItemDamage() + 1);
        }

        if (!world.isRemote) {
            for (PotionEffect potioneffect : PotionUtils.getEffectsFromStack(stack)) {
                entityLiving.addPotionEffect(new PotionEffect(potioneffect));
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

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        int remainingUses = stack.getMaxDamage() - stack.getItemDamage();
        if (remainingUses > 0 || !stack.hasTagCompound() || !stack.getTagCompound().hasKey("empty"))
            return EnumActionResult.PASS;

        RayTraceResult trace = rayTrace(world, player, true);

        if (trace.typeOfHit == RayTraceResult.Type.BLOCK && world.getBlockState(trace.getBlockPos()).getMaterial() == Material.WATER) {
            world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            player.setHeldItem(hand, new ItemStack(this));
            return EnumActionResult.SUCCESS;
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        int remainingUses = stack.getMaxDamage() - stack.getItemDamage();
        if (remainingUses <= 0) {
            NBTHelper.checkNBT(stack);
            stack.getTagCompound().setBoolean("empty", true);
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
        tooltip.add("");
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.potion.uses", stack.getMaxDamage() - stack.getItemDamage()));
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


    @SideOnly(Side.CLIENT)
    @Override
    public ItemMeshDefinition getMeshDefinition() {
        return stack -> {
            boolean full = true;
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("empty"))
                full = false;
            return new ModelResourceLocation(getRegistryName(), "full=" + full);
        };
    }

    @Override
    public void gatherVariants(Consumer<String> variants) {
        variants.accept("full=true");
        variants.accept("full=false");
    }
}