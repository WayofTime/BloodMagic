package WayofTime.alchemicalWizardry.common.spell.simple;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISimpleSpell
{
    public abstract ItemStack onOffensiveRangedRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer);

    public abstract ItemStack onOffensiveMeleeRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer);

    public abstract ItemStack onDefensiveRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer);

    public abstract ItemStack onEnvironmentalRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer);
}
