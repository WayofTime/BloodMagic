package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.DamageSourceBloodMagic;
import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.client.IVariantProvider;

public class ItemDaggerOfSacrifice extends Item implements IVariantProvider
{
    public ItemDaggerOfSacrifice()
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".daggerOfSacrifice");
        setRegistryName(Constants.BloodMagicItem.DAGGER_OF_SACRIFICE.getRegName());
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

        if (findAndFillAltar(attacker.worldObj, target, lifeEssence))
        {
            target.worldObj.playSound(null, target.posX, target.posY, target.posZ, SoundEvents.block_fire_extinguish, SoundCategory.BLOCKS, 0.5F, 2.6F + (target.worldObj.rand.nextFloat() - target.worldObj.rand.nextFloat()) * 0.8F);
            target.setHealth(-1);
            target.onDeath(new DamageSourceBloodMagic());
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

    public boolean findAndFillAltar(World world, EntityLivingBase sacrifice, int amount)
    {
        IBloodAltar bloodAltar = findBloodAltar(world, sacrifice.getPosition());

        if (bloodAltar != null)
        {
            bloodAltar.sacrificialDaggerCall(amount, true);
            bloodAltar.startCycle();
            return true;
        }

        return false;
    }

    public IBloodAltar findBloodAltar(World world, BlockPos blockPos)
    {
        TileEntity tileEntity;

        for (int i = -2; i <= 2; i++)
        {
            for (int j = -2; j <= 2; j++)
            {
                for (int k = -2; k <= 1; k++)
                {
                    tileEntity = world.getTileEntity(blockPos.add(i, k, j));

                    if ((tileEntity instanceof IBloodAltar))
                        return (IBloodAltar) tileEntity;
                }
            }
        }

        return null;
    }
}
