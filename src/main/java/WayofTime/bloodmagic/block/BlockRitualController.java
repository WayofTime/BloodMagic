package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import WayofTime.bloodmagic.api.iface.IBindable;
import com.google.common.base.Strings;
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
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.api.util.helper.RitualHelper;
import WayofTime.bloodmagic.block.base.BlockEnumContainer;
import WayofTime.bloodmagic.block.enums.EnumRitualController;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.tile.TileImperfectRitualStone;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import WayofTime.bloodmagic.util.ChatUtil;
import amerifrance.guideapi.api.IGuideLinked;

@Optional.Interface(modid = "guideapi", iface = "amerifrance.guideapi.api.IGuideLinked")
public class BlockRitualController extends BlockEnumContainer<EnumRitualController> implements IVariantProvider, IGuideLinked
{
    public BlockRitualController()
    {
        super(Material.ROCK, EnumRitualController.class);

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

        if (state.getValue(getProperty()) != EnumRitualController.IMPERFECT && tile instanceof TileMasterRitualStone)
        {
            if (heldItem != null && heldItem.getItem() == ModItems.ACTIVATION_CRYSTAL)
            {
                IBindable bindable = (IBindable) heldItem.getItem();
                if (Strings.isNullOrEmpty(bindable.getOwnerName(heldItem)))
                    return false;

                String key = RitualHelper.getValidRitual(world, pos);
                EnumFacing direction = RitualHelper.getDirectionOfRitual(world, pos, key);
                // TODO: Give a message stating that this ritual is not a valid ritual.
                if (!key.isEmpty() && direction != null && RitualHelper.checkValidRitual(world, pos, key, direction))
                {
                    if (((TileMasterRitualStone) tile).activateRitual(heldItem, player, RitualRegistry.getRitualForId(key)))
                    {
                        ((TileMasterRitualStone) tile).setDirection(direction);
                        if (state.getValue(getProperty()) == EnumRitualController.INVERTED)
                            ((TileMasterRitualStone) tile).setInverted(true);
                    }
                } else
                {
                    ChatUtil.sendNoSpamUnloc(player, "chat.BloodMagic.ritual.notValid");
                }
            }
        } else if (state.getValue(getProperty()) == EnumRitualController.IMPERFECT && tile instanceof TileImperfectRitualStone)
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
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return state.getValue(getProperty()) != EnumRitualController.IMPERFECT ? new TileMasterRitualStone() : new TileImperfectRitualStone();
    }

    // IVariantProvider

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 0; i < this.getTypes().length; i++)
            ret.add(new ImmutablePair<Integer, String>(i, "type=" + this.getTypes()[i]));
        return ret;
    }

    // IGuideLinked

    @Override
    @Nullable
    public ResourceLocation getLinkedEntry(World world, BlockPos pos, EntityPlayer player, ItemStack stack)
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getValue(getProperty()).equals(EnumRitualController.MASTER))
        {
            TileMasterRitualStone mrs = (TileMasterRitualStone) world.getTileEntity(pos);
            if (mrs == null || mrs.getCurrentRitual() == null)
                return null;
            else
                return new ResourceLocation("bloodmagic", "ritual_" + mrs.getCurrentRitual().getName());
        } else if (state.getValue(getProperty()).equals(EnumRitualController.IMPERFECT))
        {
            ImperfectRitual imperfectRitual = ImperfectRitualRegistry.getRitualForBlock(BlockStack.getStackFromPos(world, pos.up()));
            if (imperfectRitual != null)
                return new ResourceLocation("bloodmagic", "ritual_" + imperfectRitual.getName());
        }
        return null;
    }
}
