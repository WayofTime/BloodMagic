package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.RitualHelper;
import WayofTime.bloodmagic.block.base.BlockStringContainer;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.tile.TileImperfectRitualStone;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockRitualController extends BlockStringContainer {

    public static final String[] names = {"master", "imperfect"};

    public BlockRitualController() {
        super(Material.rock, names);

        setUnlocalizedName(Constants.Mod.MODID + ".stone.ritual.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setStepSound(soundTypeStone);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);

        if (player.getHeldItem() == null && tile instanceof TileMasterRitualStone) {
            Ritual send = ((TileMasterRitualStone) tile).getCurrentRitual();
            if (send != null)
                player.addChatComponentMessage(new ChatComponentText(send.getName()));
            else {
                Ritual place = RitualRegistry.getRitualForId("ritualTest");
                for (RitualComponent ritualComponent : place.getComponents()) {
                    IBlockState toPlace = ModBlocks.ritualStone.getStateFromMeta(ritualComponent.getRuneType().ordinal());
                    world.setBlockState(pos.add(ritualComponent.getOffset()), toPlace);
                }
            }
        }

        if (getMetaFromState(state) == 0 && tile instanceof TileMasterRitualStone) {
            if (player.getHeldItem() != null && player.getHeldItem().getItem() == ModItems.activationCrystal) {
                if (RitualHelper.checkValidRitual(world, pos, "ritualTest", null))
                    ((TileMasterRitualStone) tile).activateRitual(player.getHeldItem(), player, RitualRegistry.getRitualForId("ritualTest"));
            }
        } else if (getMetaFromState(state) == 1 && tile instanceof TileImperfectRitualStone) {

            IBlockState determinerState = world.getBlockState(pos.up());
            BlockStack determiner = new BlockStack(determinerState.getBlock(), determinerState.getBlock().getMetaFromState(determinerState));

            return ((TileImperfectRitualStone) tile).performRitual(world, pos, ImperfectRitualRegistry.getRitualForBlock(determiner), player);
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
        IBlockState state = world.getBlockState(pos);

        if (getMetaFromState(state) == 0 && tile instanceof TileMasterRitualStone)
            ((TileMasterRitualStone) tile).stopRitual(Ritual.BreakType.EXPLOSION);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return meta == 0 ? new TileMasterRitualStone() : new TileImperfectRitualStone();
    }
}
