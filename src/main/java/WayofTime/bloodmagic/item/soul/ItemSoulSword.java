package WayofTime.bloodmagic.item.soul;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.ISoul;
import WayofTime.bloodmagic.api.soul.ISoulWeapon;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ItemSoulSword extends ItemSword implements ISoulWeapon
{
    public ItemSoulSword()
    {
        super(ModItems.soulToolMaterial);

        setUnlocalizedName(Constants.Mod.MODID + ".soul.sword");
        setHasSubtypes(true);
        setNoRepair();
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (player.isSneaking()) //TODO: change its state depending on soul consumption
            setActivated(stack, !getActivated(stack));

        if (getActivated(stack))
        {
            setDamageOfActivatedSword(stack, 7);
        }

        return stack;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BLOCK;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        NBTHelper.checkNBT(stack);

        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.soul.sword.desc"));

        if (getActivated(stack))
            tooltip.add(TextHelper.localize("tooltip.BloodMagic.activated"));
        else
            tooltip.add(TextHelper.localize("tooltip.BloodMagic.deactivated"));

    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        if (getActivated(stack))
        {
            return super.onLeftClickEntity(stack, player, entity);
        }

        return true;
    }

    private boolean getActivated(ItemStack stack)
    {
        return stack.getItemDamage() > 0;
    }

    private ItemStack setActivated(ItemStack stack, boolean activated)
    {
        stack.setItemDamage(activated ? 1 : 0);

        return stack;
    }

    @Override
    public List<ItemStack> getRandomSoulDrop(EntityLivingBase killedEntity, EntityLivingBase attackingEntity, ItemStack stack, int looting)
    {
        List<ItemStack> soulList = new ArrayList<ItemStack>();

        if (getActivated(stack))
        {
            ISoul soul = ((ISoul) ModItems.monsterSoul);
            ItemStack soulStack = soul.createSoul(0, looting * 0.5 + 1);
            soulList.add(soulStack);
        }

        return soulList;
    }

    public double getDamageOfActivatedSword(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getDouble(Constants.NBT.SOUL_SWORD_DAMAGE);
    }

    public void setDamageOfActivatedSword(ItemStack stack, double damage)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble(Constants.NBT.SOUL_SWORD_DAMAGE, damage);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", getActivated(stack) ? getDamageOfActivatedSword(stack) : 2, 0));
        return multimap;
    }
}
