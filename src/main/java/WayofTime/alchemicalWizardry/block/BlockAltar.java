package WayofTime.alchemicalWizardry.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.tile.TileAltar;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAltar extends BlockContainer {

    public BlockAltar() {
        super(Material.rock);

        setUnlocalizedName(AlchemicalWizardry.MODID + ".altar");
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
//        return new TileAltar();
        return null;
    }
}
