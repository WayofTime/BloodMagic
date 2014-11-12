package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifierDefault;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifierDefensive;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifierEnvironmental;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellModifierOffensive;

public class TESpellModifierBlock extends TESpellBlock
{
    @Override
    protected void applySpellChange(SpellParadigm parad)
    {
        parad.modifyBufferedEffect(this.getSpellModifier());
    }

    public ComplexSpellModifier getSpellModifier()
    {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        switch (meta)
        {
            case 0:
                return ComplexSpellModifier.DEFAULT;
            case 1:
                return ComplexSpellModifier.OFFENSIVE;
            case 2:
                return ComplexSpellModifier.DEFENSIVE;
            case 3:
                return ComplexSpellModifier.ENVIRONMENTAL;
        }
        return ComplexSpellModifier.DEFAULT;
    }

    @Override
    public String getResourceLocationForMeta(int meta)
    {
        switch (meta)
        {
            case 0:
                return "alchemicalwizardry:textures/models/SpellModifierDefault.png";
            case 1:
                return "alchemicalwizardry:textures/models/SpellModifierOffensive.png";
            case 2:
                return "alchemicalwizardry:textures/models/SpellModifierDefensive.png";
            case 3:
                return "alchemicalwizardry:textures/models/SpellModifierEnvironmental.png";
        }
        return "alchemicalwizardry:textures/models/SpellModifierDefault.png";
    }
}
