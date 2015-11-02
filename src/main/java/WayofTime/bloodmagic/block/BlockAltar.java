package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAltar extends BlockContainer {

    public BlockAltar() {
        super(Material.rock);

        setUnlocalizedName(BloodMagic.MODID + ".altar");
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
//        return new TileAltar();
        return null;
    }
}
