package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.api.util.helper.PlayerSacrificeHelper;

public class ItemDaggerOfSacrifice extends Item implements IVariantProvider
{
    public ItemDaggerOfSacrifice()
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".daggerOfSacrifice");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setMaxStackSize(1);
        setFull3D();
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (target == null || attacker == null || attacker.worldObj.isRemote || (attacker instanceof EntityPlayer && !(attacker instanceof EntityPlayerMP)))
            return false;

        if (target.isChild() || target instanceof EntityPlayer)
            return false;

        if (target.isDead || target.getHealth() < 0.5F)
            return false;

        String entityName = target.getClass().getSimpleName();
        int lifeEssence = 500;

        if (ConfigHandler.entitySacrificeValues.containsKey(entityName))
            lifeEssence = ConfigHandler.entitySacrificeValues.get(entityName);

        if (BloodMagicAPI.getEntitySacrificeValues().containsKey(entityName))
            lifeEssence = BloodMagicAPI.getEntitySacrificeValues().get(entityName);

        if (PlayerSacrificeHelper.findAndFillAltar(attacker.worldObj, target, lifeEssence, true))
        {
            target.worldObj.playSound(null, target.posX, target.posY, target.posZ, SoundEvents.block_fire_extinguish, SoundCategory.BLOCKS, 0.5F, 2.6F + (target.worldObj.rand.nextFloat() - target.worldObj.rand.nextFloat()) * 0.8F);
            target.setHealth(-1);
            target.onDeath(BloodMagicAPI.getDamageSource());
        }

        return false;
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=normal"));
        return ret;
    }
}
