package WayofTime.bloodmagic.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IRitualStone;
import WayofTime.bloodmagic.api.ritual.PropertyRuneType;

public class BlockRitualStone extends Block implements IRitualStone {

	public static final String[] names = { "blank", "water", "fire", "earth", "air", "dusk", "dawn" };
	public static final PropertyRuneType TYPE = PropertyRuneType.create("TYPE");

	public BlockRitualStone() {
		super(Material.iron);

		setUnlocalizedName(BloodMagic.MODID + ".ritualStone.");
		setCreativeTab(BloodMagic.tabBloodMagic);
		setStepSound(soundTypeStone);
		setHardness(2.0F);
		setResistance(5.0F);
		setHarvestLevel("pickaxe", 2);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TYPE, EnumRuneType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumRuneType) state.getValue(TYPE)).ordinal();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, TYPE);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, this.getMetaFromState(world
				.getBlockState(pos)));
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unchecked")
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
		for (int i = 0; i < names.length; i++)
			list.add(new ItemStack(this, 1, i));
	}

	@Override
	public boolean isRuneType(World world, BlockPos pos, int meta, EnumRuneType runeType) {
		return this.getRitualStone(world, pos, meta).equals(runeType);
	}

	private EnumRuneType getRitualStone(World world, BlockPos pos, int meta) {
		IBlockState state = this.getStateFromMeta(meta);
		return ((EnumRuneType) state.getValue(TYPE));
	}
}
