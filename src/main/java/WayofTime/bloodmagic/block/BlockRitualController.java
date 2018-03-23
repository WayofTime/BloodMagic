package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.event.RitualEvent;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitualRegistry;
import WayofTime.bloodmagic.ritual.RitualRegistry;
import WayofTime.bloodmagic.ritual.Ritual;
import WayofTime.bloodmagic.ritual.imperfect.ImperfectRitual;
import WayofTime.bloodmagic.util.helper.RitualHelper;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.EnumRitualController;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.tile.TileImperfectRitualStone;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

public class BlockRitualController extends BlockEnum<EnumRitualController> implements IGuideLinked {

    public BlockRitualController() {
        super(Material.ROCK, EnumRitualController.class);

        setUnlocalizedName(BloodMagic.MODID + ".stone.ritual.");
        setCreativeTab(BloodMagic.TAB_BM);
        setSoundType(SoundType.STONE);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        TileEntity tile = world.getTileEntity(pos);

        if (state.getValue(getProperty()) != EnumRitualController.IMPERFECT && tile instanceof TileMasterRitualStone) {
            if (heldItem.getItem() == RegistrarBloodMagicItems.ACTIVATION_CRYSTAL) {
                if (((IBindable) heldItem.getItem()).getBinding(heldItem) == null)
                    return false;

                String key = RitualHelper.getValidRitual(world, pos);
                EnumFacing direction = RitualHelper.getDirectionOfRitual(world, pos, key);
                // TODO: Give a message stating that this ritual is not a valid ritual.
                if (!key.isEmpty() && direction != null && RitualHelper.checkValidRitual(world, pos, key, direction)) {
                    if (((TileMasterRitualStone) tile).activateRitual(heldItem, player, RitualRegistry.getRitualForId(key))) {
                        ((TileMasterRitualStone) tile).setDirection(direction);
                        if (state.getValue(getProperty()) == EnumRitualController.INVERTED)
                            ((TileMasterRitualStone) tile).setInverted(true);
                    }
                } else {
                    player.sendStatusMessage(new TextComponentTranslation("chat.bloodmagic.ritual.notValid"), true);
                }
            }
        } else if (state.getValue(getProperty()) == EnumRitualController.IMPERFECT && tile instanceof TileImperfectRitualStone) {
            IBlockState ritualBlock = world.getBlockState(pos.up());
            ImperfectRitual ritual = ImperfectRitualRegistry.getRitualForBlock(ritualBlock);
            if (ritual == null)
                return false;

            RitualEvent.ImperfectRitualActivatedEvent event = new RitualEvent.ImperfectRitualActivatedEvent((IImperfectRitualStone) tile, player, ritual);
            return !MinecraftForge.EVENT_BUS.post(event) && ((TileImperfectRitualStone) tile).performRitual(world, pos, ritual, player);
        }

        return false;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        TileEntity tile = world.getTileEntity(pos);

        if (getMetaFromState(state) == 0 && tile instanceof TileMasterRitualStone)
            ((TileMasterRitualStone) tile).stopRitual(Ritual.BreakType.BREAK_MRS);
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileMasterRitualStone)
            ((TileMasterRitualStone) tile).stopRitual(Ritual.BreakType.EXPLOSION);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return state.getValue(getProperty()) != EnumRitualController.IMPERFECT ? new TileMasterRitualStone() : new TileImperfectRitualStone();
    }

    // IGuideLinked

    @Override
    @Nullable
    public ResourceLocation getLinkedEntry(World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
        IBlockState state = world.getBlockState(pos);
        if (state.getValue(getProperty()).equals(EnumRitualController.MASTER)) {
            TileMasterRitualStone mrs = (TileMasterRitualStone) world.getTileEntity(pos);
            if (mrs == null || mrs.getCurrentRitual() == null)
                return null;
            else
                return new ResourceLocation("bloodmagic", "ritual_" + mrs.getCurrentRitual().getName());
        } else if (state.getValue(getProperty()).equals(EnumRitualController.IMPERFECT)) {
            ImperfectRitual imperfectRitual = ImperfectRitualRegistry.getRitualForBlock(world.getBlockState(pos.up()));
            if (imperfectRitual != null)
                return new ResourceLocation("bloodmagic", "ritual_" + imperfectRitual.getName());
        }
        return null;
    }
}
