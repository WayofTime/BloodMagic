package WayofTime.bloodmagic.item.sigil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;

public class ItemSigilElementalAffinity extends ItemSigilToggleable
{
    public ItemSigilElementalAffinity()
    {
        super("elementalAffinity", 200);
        setRegistryName(Constants.BloodMagicItem.SIGIL_ELEMENTAL_AFFINITY.getRegName());
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        player.fallDistance = 0;
        player.extinguish();
        player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2, 1, true, false));
        player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 2, 0, true, false));
    }
}
