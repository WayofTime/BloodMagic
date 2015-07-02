package WayofTime.alchemicalWizardry.api.alchemy.energy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IAlchemyGoggles
{
    boolean showIngameHUD(World world, ItemStack stack, EntityPlayer player);
}
