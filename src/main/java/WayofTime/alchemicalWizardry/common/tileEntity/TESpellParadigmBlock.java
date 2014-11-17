package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;
import WayofTime.alchemicalWizardry.api.tile.ISpellParadigmTile;

public class TESpellParadigmBlock extends TESpellBlock implements ISpellParadigmTile
{
    public SpellParadigm getSpellParadigm()
    {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        switch (meta)
        {
            case 0:
                return new SpellParadigmProjectile();
            case 1:
                return new SpellParadigmSelf();
            case 2:
                return new SpellParadigmMelee();
            case 3:
                return new SpellParadigmTool();
        }
        return new SpellParadigmSelf();
    }

    @Override
    protected void applySpellChange(SpellParadigm parad)
    {
        return;
    }

    public boolean canInputRecieve()
    {
        return false;
    }

    @Override
    public void castSpell(World world, EntityPlayer entity, ItemStack spellCasterStack)
    {
        SpellParadigm parad = this.getSpellParadigm();
        this.modifySpellParadigm(parad);
        parad.applyAllSpellEffects();
        parad.castSpell(world, entity, spellCasterStack);
    }

    @Override
    public String getResourceLocationForMeta(int meta)
    {
        switch (meta)
        {
            case 0:
                return "alchemicalwizardry:textures/models/SpellParadigmProjectile.png";
            case 1:
                return "alchemicalwizardry:textures/models/SpellParadigmSelf.png";
            case 2:
                return "alchemicalwizardry:textures/models/SpellParadigmMelee.png";
            case 3:
                return "alchemicalwizardry:textures/models/SpellParadigmTool.png";
        }
        return "alchemicalwizardry:textures/models/SpellParadigmProjectile.png";
    }

    @Override
    public void setInputDirection(ForgeDirection direction)
    {

    }

    @Override
    public ForgeDirection getInputDirection()
    {
        return ForgeDirection.UNKNOWN;
    }
}
