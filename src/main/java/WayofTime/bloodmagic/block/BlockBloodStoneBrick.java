package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockString;
import net.minecraft.block.material.Material;

public class BlockBloodStoneBrick extends BlockString
{
    public static final String[] names = { "large", "normal" };

    public BlockBloodStoneBrick()
    {
        super(Material.rock, names);

        setUnlocalizedName(Constants.Mod.MODID + ".bloodstonebrick.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeStone);
        setHarvestLevel("pickaxe", 2);
    }
}
