package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockString;
import net.minecraft.block.material.Material;

public class BlockBloodRune extends BlockString
{
    public static final String[] names = { "blank", "speed", "efficiency", "sacrifice", "selfSacrifice", "displacement", "capacity", "augCapacity", "orb", "acceleration" };

    public BlockBloodRune()
    {
        super(Material.rock, names);

        setUnlocalizedName(Constants.Mod.MODID + ".rune.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setStepSound(soundTypeStone);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    public int getRuneEffect(int meta)
    {
        return meta;
    }
}
