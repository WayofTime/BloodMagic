package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellEffect;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.*;

public class TESpellEffectBlock extends TESpellBlock
{
    @Override
    protected void applySpellChange(SpellParadigm parad)
    {
        parad.addBufferedEffect(this.getSpellEffect());
    }

    public SpellEffect getSpellEffect()
    {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        switch (meta)
        {
            case 0:
                return new SpellEffect(ComplexSpellType.FIRE);
            case 1:
                return new SpellEffect(ComplexSpellType.ICE);
            case 2:
                return new SpellEffect(ComplexSpellType.WIND);
            case 3:
                return new SpellEffect(ComplexSpellType.EARTH);
        }
        return new SpellEffect(ComplexSpellType.FIRE);
    }

    @Override
    public String getResourceLocationForMeta(int meta)
    {
        switch (meta)
        {
            case 0:
                return "alchemicalwizardry:textures/models/SpellEffectFire.png";
            case 1:
                return "alchemicalwizardry:textures/models/SpellEffectIce.png";
            case 2:
                return "alchemicalwizardry:textures/models/SpellEffectWind.png";
            case 3:
                return "alchemicalwizardry:textures/models/SpellEffectEarth.png";
        }
        return "";
    }
}
