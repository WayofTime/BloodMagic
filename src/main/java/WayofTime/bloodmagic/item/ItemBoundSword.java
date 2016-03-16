package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.client.mesh.CustomMeshDefinitionActivatable;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IActivatable;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ItemBoundSword extends ItemSword implements IBindable, IActivatable, IMeshProvider
{
    private float attackDamage;

    public ItemBoundSword()
    {
        super(ModItems.boundToolMaterial);

        setUnlocalizedName(Constants.Mod.MODID + ".bound.sword");
        setRegistryName(Constants.BloodMagicItem.BOUND_SWORD.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);

        this.attackDamage = 1.0F + ModItems.boundToolMaterial.getDamageVsEntity();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!player.isSneaking() && getActivated(stack))
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));

        if (player.isSneaking())
            setActivatedState(stack, !getActivated(stack));

        return stack;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
    {
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return oldStack.getItem() != newStack.getItem();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        NBTHelper.checkNBT(stack);

        if (StatCollector.canTranslate("tooltip.BloodMagic.bound.sword.desc"))
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.bound.sword.desc"));

        tooltip.add(TextHelper.localize("tooltip.BloodMagic." + (getActivated(stack) ? "activated" : "deactivated")));

        if (!Strings.isNullOrEmpty(stack.getTagCompound().getString(Constants.NBT.OWNER_UUID)))
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.currentOwner", PlayerHelper.getUsernameFromStack(stack)));
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers()
    {
        return HashMultimap.create();
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(stack);
        double damage = getActivated(stack) ? this.attackDamage : 1.0D;
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", damage, 0));
        return multimap;
    }

    public boolean getActivated(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);
        return stack.getTagCompound().getBoolean(Constants.NBT.ACTIVATED);
    }

    public ItemStack setActivatedState(ItemStack stack, boolean activated)
    {
        NBTHelper.checkNBT(stack);
        stack.getTagCompound().setBoolean(Constants.NBT.ACTIVATED, activated);

        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition()
    {
        return new CustomMeshDefinitionActivatable("ItemBoundSword");
    }

    @Override
    public List<String> getVariants()
    {
        List<String> ret = new ArrayList<String>();
        ret.add("active=true");
        ret.add("active=false");
        return ret;
    }

    // IBindable

    @Override
    public boolean onBind(EntityPlayer player, ItemStack stack)
    {
        return true;
    }

    @Override
    public String getOwnerName(ItemStack stack)
    {
        return stack != null ? NBTHelper.checkNBT(stack).getTagCompound().getString(Constants.NBT.OWNER_NAME) : null;
    }

    @Override
    public String getOwnerUUID(ItemStack stack)
    {
        return stack != null ? NBTHelper.checkNBT(stack).getTagCompound().getString(Constants.NBT.OWNER_UUID) : null;
    }
}
