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

public class BlockBloodStoneBrick extends BlockString implements IVariantProvider
{
    public static final String[] names = { "large", "brick" };

    public BlockBloodStoneBrick()
    {
        super(Material.rock, names);

        setUnlocalizedName(Constants.Mod.MODID + ".bloodstonebrick.");
        setRegistryName(Constants.BloodMagicBlock.BLOOD_STONE.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeStone);
        setHarvestLevel("pickaxe", 2);
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
