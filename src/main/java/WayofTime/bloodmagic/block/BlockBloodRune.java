package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.client.IVariantProvider;
import net.minecraft.block.material.Material;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockString;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class BlockBloodRune extends BlockString implements IVariantProvider
{
    public static final String[] names = { "blank", "speed", "efficiency", "sacrifice", "selfSacrifice", "displacement", "capacity", "augCapacity", "orb", "acceleration", "charging" };

    public BlockBloodRune()
    {
        super(Material.rock, names);

        setUnlocalizedName(Constants.Mod.MODID + ".rune.");
        setRegistryName(Constants.BloodMagicBlock.BLOOD_RUNE.getRegName());
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

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 0; i < names.length; i++)
            ret.add(new ImmutablePair<Integer, String>(i, "type=" + names[i]));
        return ret;
    }
}
