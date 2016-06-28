package WayofTime.bloodmagic.api.util.helper;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.registry.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlayerSacrificeHelper
{
    public static float scalingOfSacrifice = 1f;
    public static int soulFrayDuration = 400;
    public static Potion soulFrayId;

    public static double getPlayerIncense(EntityPlayer player)
    {
        return IncenseHelper.getCurrentIncense(player);
    }

    public static void setPlayerIncense(EntityPlayer player, double amount)
    {
        IncenseHelper.setCurrentIncense(player, amount);
    }

    public static boolean incrementIncense(EntityPlayer player, double min, double incenseAddition, double increment)
    {
        double amount = getPlayerIncense(player);
        if (amount < min || amount >= incenseAddition)
        {
            return false;
        }

        amount = amount + Math.min(increment, incenseAddition - amount);
        setPlayerIncense(player, amount);

        // System.out.println("Amount of incense: " + amount + ", Increment: " +
        // increment);

        return true;
    }

    /**
     * Sacrifices a player's health while the player is under the influence of
     * incense
     * 
     * @param player
     *        - The player sacrificing
     * 
     * @return Whether or not the health sacrificing succeeded
     */
    public static boolean sacrificePlayerHealth(EntityPlayer player)
    {
        if (player.isPotionActive(soulFrayId))
        {
            return false;
        }

        double amount = getPlayerIncense(player);

        if (amount >= 0)
        {
            float health = player.getHealth();
            float maxHealth = player.getMaxHealth();

            if (health > maxHealth / 10.0)
            {
                float sacrificedHealth = health - maxHealth / 10.0f;

                if (findAndFillAltar(player.getEntityWorld(), player, (int) (sacrificedHealth * ConfigHandler.sacrificialDaggerConversion * getModifier(amount)), false))
                {
                    player.setHealth(maxHealth / 10.0f);
                    setPlayerIncense(player, 0);
                    player.addPotionEffect(new PotionEffect(ModPotions.soulFray, soulFrayDuration));

                    return true;
                }
            }
        }

        return false;
    }

    public static double getModifier(double amount)
    {
        return 1 + amount * scalingOfSacrifice;
    }

    /**
     * Finds the nearest {@link IBloodAltar} and attempts to fill it
     * 
     * @param world
     *        - The world
     * @param sacrificingEntity
     *        - The entity having the sacrifice done on (can be
     *        {@link EntityPlayer} for self-sacrifice)
     * @param amount
     *        - The amount of which the altar should be filled
     * @param isSacrifice
     *        - Whether this is a Sacrifice or a Self-Sacrifice
     * 
     * @return Whether the altar is found and (attempted) filled
     */
    public static boolean findAndFillAltar(World world, EntityLivingBase sacrificingEntity, int amount, boolean isSacrifice)
    {
        IBloodAltar altarEntity = getAltar(world, sacrificingEntity.getPosition());

        if (altarEntity == null)
            return false;

        altarEntity.sacrificialDaggerCall(amount, isSacrifice);
        altarEntity.startCycle();

        return true;
    }

    /**
     * Gets the nearest {@link IBloodAltar}
     * 
     * @param world
     *        - The world
     * @param blockPos
     *        - The position of where the check should be in (in a 2 block
     *        radius from this)
     * 
     * @return The nearest altar, if no altar is found, then this will return
     *         null
     */
    public static IBloodAltar getAltar(World world, BlockPos blockPos)
    {
        TileEntity tileEntity;

        for (int i = -2; i <= 2; i++)
        {
            for (int j = -2; j <= 2; j++)
            {
                for (int k = -2; k <= 1; k++)
                {
                    tileEntity = world.getTileEntity(blockPos.add(i, j, k));

                    if (tileEntity instanceof IBloodAltar)
                    {
                        return (IBloodAltar) tileEntity;
                    }
                }
            }
        }

        return null;
    }
}