package WayofTime.bloodmagic.util.helper;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.altar.IBloodAltar;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.event.SacrificeKnifeUsedEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class PlayerSacrificeHelper {
    public static float scalingOfSacrifice = 1f;
    public static int soulFrayDuration = 400;
    public static Potion soulFrayId;

    public static double getPlayerIncense(EntityPlayer player) {
        return IncenseHelper.getCurrentIncense(player);
    }

    public static void setPlayerIncense(EntityPlayer player, double amount) {
        IncenseHelper.setCurrentIncense(player, amount);
    }

    public static boolean incrementIncense(EntityPlayer player, double min, double incenseAddition, double increment) {
        double amount = getPlayerIncense(player);
        if (amount < min || amount >= incenseAddition) {
            return false;
        }

        amount = amount + Math.min(increment, incenseAddition - amount);
        setPlayerIncense(player, amount);

        if (amount == incenseAddition) {
            IncenseHelper.setMaxIncense(player, incenseAddition);
        }
        // System.out.println("Amount of incense: " + amount + ", Increment: " +
        // increment);

        return true;
    }

    /**
     * Sacrifices a player's health while the player is under the influence of
     * incense
     *
     * @param player - The player sacrificing
     * @return Whether or not the health sacrificing succeeded
     */
    public static boolean sacrificePlayerHealth(EntityPlayer player) {
        if (player.isPotionActive(soulFrayId)) {
            return false;
        }

        double amount = getPlayerIncense(player);

        if (amount >= 0) {
            float health = player.getHealth();
            float maxHealth = player.getMaxHealth();

            if (health > maxHealth / 10.0) {
                float sacrificedHealth = health - maxHealth / 10.0f;
                int lpAdded = (int) (sacrificedHealth * ConfigHandler.values.sacrificialDaggerConversion * getModifier(amount));

                IBloodAltar altar = getAltar(player.getEntityWorld(), player.getPosition());
                if (altar != null) {
                    SacrificeKnifeUsedEvent evt = new SacrificeKnifeUsedEvent(player, true, true, (int) sacrificedHealth, lpAdded);
                    if (MinecraftForge.EVENT_BUS.post(evt))
                        return false;

                    altar.sacrificialDaggerCall(evt.lpAdded, false);
                    altar.startCycle();

                    player.setHealth(maxHealth / 10.0f);
                    setPlayerIncense(player, 0);
                    player.addPotionEffect(new PotionEffect(RegistrarBloodMagic.SOUL_FRAY, soulFrayDuration));

                    return true;
                }
            }
        }

        return false;
    }

    public static double getModifier(double amount) {
        return 1 + amount * scalingOfSacrifice;
    }

    /**
     * Finds the nearest {@link IBloodAltar} and attempts to fill it
     *
     * @param world             - The world
     * @param sacrificingEntity - The entity having the sacrifice done on (can be
     *                          {@link EntityPlayer} for self-sacrifice)
     * @param amount            - The amount of which the altar should be filled
     * @param isSacrifice       - Whether this is a Sacrifice or a Self-Sacrifice
     * @return Whether the altar is found and (attempted) filled
     */
    public static boolean findAndFillAltar(World world, EntityLivingBase sacrificingEntity, int amount, boolean isSacrifice) {
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
     * @param world    - The world
     * @param blockPos - The position of where the check should be in (in a 2 block
     *                 radius from this)
     * @return The nearest altar, if no altar is found, then this will return
     * null
     */
    public static IBloodAltar getAltar(World world, BlockPos blockPos) {
        TileEntity tileEntity;

        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    tileEntity = world.getTileEntity(blockPos.add(x, y, z));

                    if (tileEntity instanceof IBloodAltar) {
                        return (IBloodAltar) tileEntity;
                    }
                }
            }
        }

        return null;
    }
}