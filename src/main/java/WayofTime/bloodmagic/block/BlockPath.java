package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.incense.IIncensePath;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.EnumPath;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class BlockPath extends BlockEnum<EnumPath> implements IIncensePath, IVariantProvider
{

    public BlockPath()
    {
        super(Material.ROCK, EnumPath.class);

        setUnlocalizedName(Constants.Mod.MODID + ".path.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);

        setHarvestLevel("axe", 0, getStateFromMeta(0));
        setHarvestLevel("axe", 0, getStateFromMeta(1));
        setHarvestLevel("pickaxe", 0, getStateFromMeta(2));
        setHarvestLevel("pickaxe", 0, getStateFromMeta(3));
        setHarvestLevel("pickaxe", 0, getStateFromMeta(4));
        setHarvestLevel("pickaxe", 0, getStateFromMeta(5));
        setHarvestLevel("pickaxe", 3, getStateFromMeta(6));
        setHarvestLevel("pickaxe", 3, getStateFromMeta(7));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.decoration.safe"));
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public int getLevelOfPath(World world, BlockPos pos, IBlockState state)
    {
        switch (this.getMetaFromState(state))
        {
        case 0:
        case 1:
            return 2;
        case 2:
        case 3:
            return 4;
        case 4:
        case 5:
            return 6;
        case 6:
        case 7:
            return 8;
        default:
            return 0;
        }
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
