package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

import net.minecraftforge.common.util.FakePlayer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.PlayerSacrificeHelper;
import WayofTime.bloodmagic.api.util.helper.PurificationHelper;
import WayofTime.bloodmagic.client.IVariantProvider;

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
        if (attacker instanceof FakePlayer)
            return false;

        if (target == null || attacker == null || attacker.getEntityWorld().isRemote || (attacker instanceof EntityPlayer && !(attacker instanceof EntityPlayerMP)))
            return false;

        if (!target.isNonBoss())
            return false;

        if (target instanceof EntityPlayer)
            return false;

        if (target.isChild() && !(target instanceof IMob))
            return false;

        if (target.isDead || target.getHealth() < 0.5F)
            return false;

        String entityName = target.getClass().getSimpleName();
        int lifeEssenceRatio = 25;

        if (ConfigHandler.entitySacrificeValues.containsKey(entityName))
            lifeEssenceRatio = ConfigHandler.entitySacrificeValues.get(entityName);

        if (BloodMagicAPI.getEntitySacrificeValues().containsKey(entityName))
            lifeEssenceRatio = BloodMagicAPI.getEntitySacrificeValues().get(entityName);

        if (lifeEssenceRatio <= 0)
            return false;

        int lifeEssence = (int) (lifeEssenceRatio * target.getHealth());
        if (target instanceof EntityAnimal)
        {
            lifeEssence = (int) (lifeEssence * (1 + PurificationHelper.getCurrentPurity((EntityAnimal) target)));
        }

        if (target.isChild())
        {
            lifeEssence *= 0.5F;
        }

        if (PlayerSacrificeHelper.findAndFillAltar(attacker.getEntityWorld(), target, lifeEssence, true))
        {
            target.getEntityWorld().playSound(null, target.posX, target.posY, target.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (target.getEntityWorld().rand.nextFloat() - target.getEntityWorld().rand.nextFloat()) * 0.8F);
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
