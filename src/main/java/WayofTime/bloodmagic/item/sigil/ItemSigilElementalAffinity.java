package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.iface.ISentientSwordEffectProvider;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemSigilElementalAffinity extends ItemSigilToggleableBase implements ISentientSwordEffectProvider {
    public ItemSigilElementalAffinity() {
        super("elemental_affinity", 200);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected) {
        if (PlayerHelper.isFakePlayer(player))
            return;

        player.fallDistance = 0;
        player.extinguish();
        player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 2, 1, true, false));
        player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 2, 0, true, false));
    }

    @Override
    public boolean applyOnHitEffect(EnumDemonWillType type, int willLevel, ItemStack swordStack, ItemStack providerStack, EntityLivingBase attacker, EntityLivingBase target)
    {
        boolean highWill = willLevel >= 3;
        int LPUsage = getLpUsed();
        if (highWill)
            LPUsage *= 2;
        
        if (!NetworkHelper.getSoulNetwork(getBinding(providerStack)).syphonAndDamage((EntityPlayer) attacker, SoulTicket.item(providerStack, attacker.getEntityWorld(), attacker, LPUsage)).isSuccess()) {
            return false;
        }
        
        // Disable their elemental affinity sigil, remove any fire resistance, water breathing
        if (target instanceof EntityPlayer) {
            Item current;
            
            // If attacker has a lot of will, attempt to disable all elemental affinity sigils in target's inventory
            // otherwise disable just 1
            boolean found = false;
            // disable any in their main inventory
            for (ItemStack stack: ((EntityPlayer) target).inventory.mainInventory) {
                current = stack.getItem();
                if (current instanceof ItemSigilElementalAffinity) {
                    if (((ItemSigilElementalAffinity) current).getActivated(stack)) {
                        ((ItemSigilElementalAffinity) current).setActivatedState(stack, false);
                        if (!highWill) {
                            found = true;
                            break;
                        }
                    }
                }
            }
            
            if (!found || highWill) {
                // check their off hand
                for (ItemStack stack: ((EntityPlayer) target).inventory.offHandInventory) {
                    current = stack.getItem();
                    if (current instanceof ItemSigilElementalAffinity) {
                        ((ItemSigilElementalAffinity) current).setActivatedState(stack, false);
                    }
                }
            }
            
        }
        target.removePotionEffect(MobEffects.FIRE_RESISTANCE);
        target.removePotionEffect(MobEffects.WATER_BREATHING);
        return true;
    }
}
