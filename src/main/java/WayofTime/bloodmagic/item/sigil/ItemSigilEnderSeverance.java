package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.registry.ModPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class ItemSigilEnderSeverance extends ItemSigilToggleable
{
    public ItemSigilEnderSeverance()
    {
        super("enderSeverance", 200);
    }

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(player.posX - 4.5, player.posY - 4.5, player.posZ - 4.5, player.posX + 4.5, player.posY + 4.5, player.posZ + 4.5));
        for (Entity entity : entityList)
        {
            if (entity instanceof EntityEnderman)
                ((EntityEnderman) entity).addPotionEffect(new PotionEffect(ModPotions.planarBinding.id, 40, 0));
        }
    }
}
