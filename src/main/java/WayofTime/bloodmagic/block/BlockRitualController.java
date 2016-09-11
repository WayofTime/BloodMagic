package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import amerifrance.guideapi.api.IGuideLinked;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.util.helper.RitualHelper;
import WayofTime.bloodmagic.block.base.BlockStringContainer;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.tile.TileImperfectRitualStone;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import WayofTime.bloodmagic.util.ChatUtil;

import javax.annotation.Nullable;

@Optional.Interface(modid = "guideapi", iface = "amerifrance.guideapi.api.IGuideLinked")
public class BlockRitualController extends BlockStringContainer implements IVariantProvider, IGuideLinked
{
    public static final String[] names = { "master", "imperfect" };

    public BlockRitualController()
    {
        super(Material.ROCK, names);

        setUnlocalizedName(Constants.Mod.MODID + ".stone.ritual.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setSoundType(SoundType.STONE);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (getMetaFromState(state) == 0 && tile instanceof TileMasterRitualStone)
        {
            if (heldItem != null && heldItem.getItem() == ModItems.ACTIVATION_CRYSTAL)
            {
                String key = RitualHelper.getValidRitual(world, pos);
                EnumFacing direction = RitualHelper.getDirectionOfRitual(world, pos, key);
                // TODO: Give a message stating that this ritual is not a valid
                // ritual.
                if (!key.isEmpty() && direction != null && RitualHelper.checkValidRitual(world, pos, key, direction))
                {
                    if (((TileMasterRitualStone) tile).activateRitual(heldItem, player, RitualRegistry.getRitualForId(key)))
                    {
                        ((TileMasterRitualStone) tile).setDirection(direction);
                    }
                } else
                {
                    ChatUtil.sendNoSpamUnloc(player, "chat.BloodMagic.ritual.notValid");
                }
            }
        } else if (getMetaFromState(state) == 1 && tile instanceof TileImperfectRitualStone)
        {

            IBlockState determinerState = world.getBlockState(pos.up());
            BlockStack determiner = new BlockStack(determinerState.getBlock(), determinerState.getBlock().getMetaFromState(determinerState));

            return ((TileImperfectRitualStone) tile).performRitual(world, pos, ImperfectRitualRegistry.getRitualForBlock(determiner), player);
        }

        return false;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (getMetaFromState(state) == 0 && tile instanceof TileMasterRitualStone)
            ((TileMasterRitualStone) tile).stopRitual(Ritual.BreakType.BREAK_MRS);
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, BlockPos pos, Explosion explosion)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileMasterRitualStone)
            ((TileMasterRitualStone) tile).stopRitual(Ritual.BreakType.EXPLOSION);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return meta == 0 ? (new TileMasterRitualStone()) : (new TileImperfectRitualStone());
    }

    // IVariantProvider

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 0; i < names.length; i++)
            ret.add(new ImmutablePair<Integer, String>(i, "type=" + names[i]));
        return ret;
    }

    // IGuideLinked

    @Override
    @Nullable
    public ResourceLocation getLinkedEntry(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getValue(getStringProp()).equals(names[0]))
        {
            TileMasterRitualStone mrs = (TileMasterRitualStone) world.getTileEntity(pos);
            if (mrs == null || mrs.getCurrentRitual() == null)
                return null;
            else
                return new ResourceLocation("bloodmagic", "ritual_" + mrs.getCurrentRitual().getName());
        } else if (state.getValue(getStringProp()).equals(names[1]))
        {
            ImperfectRitual imperfectRitual = ImperfectRitualRegistry.getRitualForBlock(BlockStack.getStackFromPos(world, pos.up()));
            if (imperfectRitual != null)
                return new ResourceLocation("bloodmagic", "ritual_" + imperfectRitual.getName());
        }
        return null;
    }
}
