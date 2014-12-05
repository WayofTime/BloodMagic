package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.api.spell.SpellEnhancement;
import WayofTime.alchemicalWizardry.api.spell.SpellEnhancementCost;
import WayofTime.alchemicalWizardry.api.spell.SpellEnhancementPotency;
import WayofTime.alchemicalWizardry.api.spell.SpellEnhancementPower;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;

public class TESpellEnhancementBlock extends TESpellBlock
{
    @Override
    protected void applySpellChange(SpellParadigm parad)
    {
        int i = -1;

        switch (this.enhancementType())
        {
            case 0:
                i = parad.getBufferedEffectPower();
                break;
            case 1:
                i = parad.getBufferedEffectCost();
                break;
            case 2:
                i = parad.getBufferedEffectPotency();
                break;
        }

        if (i != -1 && i < this.getLimit())
        {
            parad.applyEnhancement(getSpellEnhancement());
        } else if (i < this.getLimit())
        {
            this.doBadStuff();
        }
    }

    public SpellEnhancement getSpellEnhancement()
    {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        switch (meta)
        {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                return new SpellEnhancementPower();
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return new SpellEnhancementCost();
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                return new SpellEnhancementPotency();
        }
        return new SpellEnhancementCost();
    }

    public int getLimit()
    {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        switch (meta)
        {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 1;
            case 6:
                return 2;
            case 7:
                return 3;
            case 8:
                return 4;
            case 9:
                return 5;
            case 10:
                return 1;
            case 11:
                return 2;
            case 12:
                return 3;
            case 13:
                return 4;
            case 14:
                return 5;
        }
        return 0;
    }

    public int enhancementType() //0 is power, 1 is cost, 2 is potency
    {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        switch (meta)
        {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                return 0;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return 1;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                return 2;
        }
        return 1;
    }

    public void doBadStuff()
    {

    }

    @Override
    public String getResourceLocationForMeta(int meta)
    {
        switch (meta)
        {
            case 0:
                return "alchemicalwizardry:textures/models/SpellEnhancementPower1.png";
            case 1:
                return "alchemicalwizardry:textures/models/SpellEnhancementPower2.png";
            case 2:
                return "alchemicalwizardry:textures/models/SpellEnhancementPower3.png";
            case 3:
            	return "alchemicalwizardry:textures/models/SpellEnhancementPower4.png";
            case 5:
                return "alchemicalwizardry:textures/models/SpellEnhancementCost1.png";
            case 6:
                return "alchemicalwizardry:textures/models/SpellEnhancementCost2.png";
            case 7:
            	return "alchemicalwizardry:textures/models/SpellEnhancementCost3.png";
            case 8:
                return "alchemicalwizardry:textures/models/SpellEnhancementCost4.png";
            case 10:
                return "alchemicalwizardry:textures/models/SpellEnhancementPotency1.png";
            case 11:
                return "alchemicalwizardry:textures/models/SpellEnhancementPotency2.png";
            case 12:
                return "alchemicalwizardry:textures/models/SpellEnhancementPotency3.png";
            case 13:
                return "alchemicalwizardry:textures/models/SpellEnhancementPotency4.png";

        }
        return "alchemicalwizardry:textures/models/SpellEnhancementPower1.png";
    }
}
