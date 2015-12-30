package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockSoulForge extends Block
{

    public BlockSoulForge()
    {
        super(Material.iron);

        setUnlocalizedName(Constants.Mod.MODID + ".soulforge.");
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeMetal);
        setHarvestLevel("pickaxe", 2);
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
            return false;

        return false;
    }
}
