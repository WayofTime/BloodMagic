package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.EnumSubWillType;
import WayofTime.bloodmagic.client.IVariantProvider;

public class BlockDemonLight extends BlockEnum<EnumSubWillType> implements IVariantProvider
{
    public BlockDemonLight()
    {
        super(Material.ROCK, EnumSubWillType.class);

        setUnlocalizedName(Constants.Mod.MODID + ".demonlight.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
        setLightLevel(1);
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 0; i < this.getTypes().length; i++)
            ret.add(new ImmutablePair<Integer, String>(i, "type=" + this.getTypes()[i]));
        return ret;
    }
}
