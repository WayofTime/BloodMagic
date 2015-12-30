package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockString;
import net.minecraft.block.material.Material;

public class BlockCrystal extends BlockString
{
    public static final String[] names = { "normal", "brick" };

    public BlockCrystal()
    {
        super(Material.rock, names);

        setUnlocalizedName(Constants.Mod.MODID + ".crystal.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setStepSound(soundTypeStone);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }
}
