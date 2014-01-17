package WayofTime.alchemicalWizardry.common.spell.simple;

import net.minecraft.item.ItemStack;

public class HomSpellComponent
{
    public HomSpell spell;
    public ItemStack item;
    public int blockID;

    public HomSpellComponent(ItemStack item, HomSpell spell)
    {
        this.item = item;
        this.spell = spell;
    }

    public HomSpell getSpell()
    {
        return spell;
    }

    public ItemStack getItemStack()
    {
        return this.item;
    }
}
