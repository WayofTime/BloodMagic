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
import WayofTime.bloodmagic.api.soul.IDemonWill;
import WayofTime.bloodmagic.api.soul.IDemonWillWeapon;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ItemSentientSword extends ItemSword implements IDemonWillWeapon
{
    public int[] soulBracket = new int[] { 16, 60, 200, 400 };
    public double[] damageAdded = new double[] { 1, 1.5, 2, 2.5 };
    public double[] soulDrainPerSwing = new double[] { 0.05, 0.1, 0.2, 0.4 };
    public double[] soulDrop = new double[] { 2, 4, 8, 12 };
    public double[] staticDrop = new double[] { 1, 1, 2, 3 };

    public ItemSentientSword()
    {
        super(ModItems.soulToolMaterial);

        setUnlocalizedName(Constants.Mod.MODID + ".sentientSword");
        setRegistryName(Constants.BloodMagicItem.SENTIENT_SWORD.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (player.isSneaking()) //TODO: change its state depending on soul consumption
            setActivated(stack, !getActivated(stack));

        if (getActivated(stack))
        {
            double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(player);
            int level = getLevel(stack, soulsRemaining);

            double drain = level >= 0 ? soulDrainPerSwing[level] : 0;
            double extraDamage = level >= 0 ? damageAdded[level] : 0;

            setDrainOfActivatedSword(stack, drain);
            setDamageOfActivatedSword(stack, 7 + extraDamage);
            setStaticDropOfActivatedSword(stack, level >= 0 ? staticDrop[level] : 1);
            setDropOfActivatedSword(stack, level >= 0 ? soulDrop[level] : 0);
        }

        return stack;
    }

    private int getLevel(ItemStack stack, double soulsRemaining)
    {
        int lvl = -1;
        for (int i = 0; i < soulBracket.length; i++)
        {
            if (soulsRemaining >= soulBracket[i])
            {
                lvl = i;
            }
        }

        return lvl;
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

        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.sentientSword.desc"));

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
            double drain = this.getDrainOfActivatedSword(stack);
            if (drain > 0)
            {
                double soulsRemaining = PlayerDemonWillHandler.getTotalDemonWill(player);

                if (drain > soulsRemaining)
                {
                    setActivated(stack, false);
                    return false;
                } else
                {
                    PlayerDemonWillHandler.consumeDemonWill(player, drain);
                }
            }

            return super.onLeftClickEntity(stack, player, entity);
        }

        return true;
    }

    public boolean getActivated(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        return tag.getBoolean("activated");
    }

    public ItemStack setActivated(ItemStack stack, boolean activated)
    {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        tag.setBoolean("activated", activated);

        return stack;
    }

    @Override
    public List<ItemStack> getRandomDemonWillDrop(EntityLivingBase killedEntity, EntityLivingBase attackingEntity, ItemStack stack, int looting)
    {
        List<ItemStack> soulList = new ArrayList<ItemStack>();

        if (getActivated(stack))
        {
            IDemonWill soul = ((IDemonWill) ModItems.monsterSoul);

            for (int i = 0; i <= looting; i++)
            {
                ItemStack soulStack = soul.createWill(0, this.getDropOfActivatedSword(stack) * attackingEntity.worldObj.rand.nextDouble() + this.getStaticDropOfActivatedSword(stack));
                soulList.add(soulStack);
            }
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

    public double getDrainOfActivatedSword(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getDouble(Constants.NBT.SOUL_SWORD_ACTIVE_DRAIN);
    }

    public void setDrainOfActivatedSword(ItemStack stack, double drain)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble(Constants.NBT.SOUL_SWORD_ACTIVE_DRAIN, drain);
    }

    public double getStaticDropOfActivatedSword(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getDouble(Constants.NBT.SOUL_SWORD_STATIC_DROP);
    }

    public void setStaticDropOfActivatedSword(ItemStack stack, double drop)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble(Constants.NBT.SOUL_SWORD_STATIC_DROP, drop);
    }

    public double getDropOfActivatedSword(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getDouble(Constants.NBT.SOUL_SWORD_DROP);
    }

    public void setDropOfActivatedSword(ItemStack stack, double drop)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble(Constants.NBT.SOUL_SWORD_DROP, drop);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", getActivated(stack) ? getDamageOfActivatedSword(stack) : 2, 0));
        return multimap;
    }
}
