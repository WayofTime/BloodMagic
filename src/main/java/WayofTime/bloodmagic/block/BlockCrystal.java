package WayofTime.bloodmagic.block;

import net.minecraft.block.material.Material;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockString;

public class BlockCrystal extends BlockString
{
    public static final String[] names = { "large", "brick" };

    public BlockCrystal()
    {
        super(Material.rock, names);

        setUnlocalizedName(Constants.Mod.MODID + ".crystal.");
        setRegistryName(Constants.BloodMagicBlock.CRYSTAL.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        setStepSound(soundTypeStone);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }
}
