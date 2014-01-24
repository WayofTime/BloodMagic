package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellEffectBlock;


public class BlockSpellEffect extends BlockOrientable 
{
	public BlockSpellEffect(int id) 
	{
		super(id);
		setUnlocalizedName("blockSpellEffect");
	}
	
	@Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TESpellEffectBlock();
    }
}
