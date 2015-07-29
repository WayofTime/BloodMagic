package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.tile.ISpellTile;

public abstract class TESpellBlock extends TEOrientable implements ISpellTile
{
	@Override
    public void modifySpellParadigm(SpellParadigm parad)
    {
        this.applySpellChange(parad);
        TileEntity tile = this.getTileAtOutput();
        if (tile instanceof TESpellBlock)
        {
            TESpellBlock outputBlock = (TESpellBlock) tile;
            outputBlock.modifySpellParadigm(parad);
        }
    }

    protected abstract void applySpellChange(SpellParadigm parad);

    public TESpellBlock getTileAtOutput()
    {
        EnumFacing output = this.getOutputDirection();
        int xOffset = output.getFrontOffsetX();
        int yOffset = output.getFrontOffsetY();
        int zOffset = output.getFrontOffsetZ();

        TileEntity tile = worldObj.getTileEntity(pos.add(output.getDirectionVec()));

        if (tile instanceof TESpellBlock && ((TESpellBlock) tile).canInputRecieveOutput(output))
        {
            return (TESpellBlock) tile;
        }

        return null;
    }

    public boolean canInputRecieve()
    {
        return true;
    }

    @Override
    public boolean canInputRecieveOutput(EnumFacing output)
    {
        return this.canInputRecieve() && this.getInputDirection().getOpposite() == output;
    }
}
