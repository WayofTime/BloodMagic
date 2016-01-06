package WayofTime.bloodmagic.util;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import WayofTime.bloodmagic.api.altar.EnumAltarComponent;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.tile.TileInventory;

public class Utils
{
    public static boolean isInteger(String integer)
    {
        try
        {
            Integer.parseInt(integer);
        } catch (NumberFormatException e)
        {
            return false;
        } catch (NullPointerException e)
        {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    /**
     * @see #insertItemToTile(TileInventory, EntityPlayer, int)
     * 
     * @param tile
     *        - The {@link TileInventory} to input the item to
     * @param player
     *        - The player to take the item from.
     * 
     * @return {@code true} if the ItemStack is inserted, {@code false}
     *         otherwise
     */
    public static boolean insertItemToTile(TileInventory tile, EntityPlayer player)
    {
        return insertItemToTile(tile, player, 0);
    }

    /**
     * Used for inserting an ItemStack with a stacksize of 1 to a tile's
     * inventory at slot 0
     * 
     * EG: Block Altar
     * 
     * @param tile
     *        - The {@link TileInventory} to input the item to
     * @param player
     *        - The player to take the item from.
     * @param slot
     *        - The slot to attempt to insert to
     * 
     * @return {@code true} if the ItemStack is inserted, {@code false}
     *         otherwise
     */
    public static boolean insertItemToTile(TileInventory tile, EntityPlayer player, int slot)
    {
        if (tile.getStackInSlot(slot) == null && player.getHeldItem() != null)
        {
            ItemStack input = player.getHeldItem().copy();
            input.stackSize = 1;
            player.getHeldItem().stackSize--;
            tile.setInventorySlotContents(slot, input);
            return true;
        } else if (tile.getStackInSlot(slot) != null && player.getHeldItem() == null)
        {
            if (!tile.getWorld().isRemote)
            {
                EntityItem invItem = new EntityItem(tile.getWorld(), player.posX, player.posY + 0.25, player.posZ, tile.getStackInSlot(slot));
                tile.getWorld().spawnEntityInWorld(invItem);
            }
            tile.clear();
            return false;
        }

        return false;
    }

    /**
     * Gets a default block for each type of {@link EnumAltarComponent}
     * 
     * @param component
     *        - The Component to provide a block for.
     * 
     * @return The default Block for the EnumAltarComponent
     */
    public static Block getBlockForComponent(EnumAltarComponent component)
    {
        switch (component)
        {
        case GLOWSTONE:
            return Blocks.glowstone;
        case BLOODSTONE:
            return ModBlocks.bloodStoneBrick;
        case BEACON:
            return Blocks.beacon;
        case BLOODRUNE:
            return ModBlocks.bloodRune;
        case CRYSTAL:
            return ModBlocks.crystal;
        case NOTAIR:
            return Blocks.stonebrick;
        default:
            return Blocks.air;
        }
    }

    public static float getModifiedDamage(EntityLivingBase attackedEntity, DamageSource source, float amount)
    {
        if (!attackedEntity.isEntityInvulnerable(source))
        {
            if (amount <= 0)
                return 0;

            amount = net.minecraftforge.common.ISpecialArmor.ArmorProperties.applyArmor(attackedEntity, attackedEntity.getInventory(), source, amount);
            if (amount <= 0)
                return 0;
            amount = applyPotionDamageCalculations(attackedEntity, source, amount);

            return amount;
        }

        return 0;
    }

    public static float applyPotionDamageCalculations(EntityLivingBase attackedEntity, DamageSource source, float damage)
    {
        if (source.isDamageAbsolute())
        {
            return damage;
        } else
        {
            if (attackedEntity.isPotionActive(Potion.resistance) && source != DamageSource.outOfWorld)
            {
                int i = (attackedEntity.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = damage * (float) j;
                damage = f / 25.0F;
            }

            if (damage <= 0.0F)
            {
                return 0.0F;
            } else
            {
                int k = EnchantmentHelper.getEnchantmentModifierDamage(attackedEntity.getInventory(), source);

                if (k > 20)
                {
                    k = 20;
                }

                if (k > 0 && k <= 20)
                {
                    int l = 25 - k;
                    float f1 = damage * (float) l;
                    damage = f1 / 25.0F;
                }

                return damage;
            }
        }
    }
}
