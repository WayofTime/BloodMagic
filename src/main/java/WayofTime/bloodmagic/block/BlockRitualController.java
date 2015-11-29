package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.block.base.BlockStringContainer;
import WayofTime.bloodmagic.tile.TileImperfectRitualStone;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockRitualController extends BlockStringContainer {

    public static final String[] names = {"master", "imperfect"};

    public BlockRitualController() {
        super(Material.rock, names);

        setUnlocalizedName(Constants.Mod.MODID + ".stone.ritual.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setStepSound(soundTypeStone);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);

        if (getMetaFromState(state) == 1 && tile instanceof TileImperfectRitualStone) {

            IBlockState determinerState = world.getBlockState(pos.up());
            BlockStack determiner = new BlockStack(determinerState.getBlock(), determinerState.getBlock().getMetaFromState(determinerState));

            return ((TileImperfectRitualStone) tile).performRitual(world, pos, ImperfectRitualRegistry.getRitualForBlock(determiner), player);
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return meta == 0 ? new TileMasterRitualStone() : new TileImperfectRitualStone();
    }
}
