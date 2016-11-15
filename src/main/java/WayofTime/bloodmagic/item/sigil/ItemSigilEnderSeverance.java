package WayofTime.bloodmagic.item.sigil;

import java.util.List;

import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModPotions;

public class ItemSigilEnderSeverance extends ItemSigilToggleableBase
{
    public ItemSigilEnderSeverance()
    {
        super("enderSeverance", 200);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        if (PlayerHelper.isFakePlayer(player))
            return;

        List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, new net.minecraft.util.math.AxisAlignedBB(player.posX - 4.5, player.posY - 4.5, player.posZ - 4.5, player.posX + 4.5, player.posY + 4.5, player.posZ + 4.5));
        for (Entity entity : entityList)
        {
            if (entity instanceof EntityEnderman)
                ((EntityEnderman) entity).addPotionEffect(new PotionEffect(ModPotions.planarBinding, 40, 0));
        }
    }
}
