package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.items.ScribeTool;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RitualStone extends Block
{
	@SideOnly(Side.CLIENT)
	private static Icon blankIcon;
	@SideOnly(Side.CLIENT)
	private static Icon waterStoneIcon;
	@SideOnly(Side.CLIENT)
	private static Icon fireStoneIcon;
	@SideOnly(Side.CLIENT)
	private static Icon earthStoneIcon;
	@SideOnly(Side.CLIENT)
	private static Icon airStoneIcon;
	@SideOnly(Side.CLIENT)
	private static Icon duskStoneIcon;

	public RitualStone(int par1)
	{
		super(par1, Material.iron);
		setHardness(2.0F);
		setResistance(5.0F);
		setUnlocalizedName("ritualStone");
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		blankIcon = iconRegister.registerIcon("AlchemicalWizardry:RitualStone");
		waterStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:WaterRitualStone");
		fireStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:FireRitualStone");
		earthStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:EarthRitualStone");
		airStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:AirRitualStone");
		duskStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:DuskRitualStone");
	}

	@Override
	public int damageDropped(int metadata)
	{
		return 0;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
	{
		ItemStack playerItem = player.getCurrentEquippedItem();

		if (playerItem == null)
		{
			return false;
		}

		Item item = playerItem.getItem();

		if (!(item instanceof ScribeTool))
		{
			return false;
		}

		if (playerItem.getMaxDamage() <= playerItem.getItemDamage() && !(playerItem.getMaxDamage() == 0))
		{
			return false;
		}

		ScribeTool scribeTool = (ScribeTool)item;

		if (!player.capabilities.isCreativeMode)
		{
			playerItem.setItemDamage(playerItem.getItemDamage() + 1);
		}

		world.setBlockMetadataWithNotify(x, y, z, scribeTool.getType(), 3);
		world.markBlockForUpdate(x, y, z);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata)
	{
		switch (metadata)
		{
		case 0:
			return blankIcon;

		case 1:
			return waterStoneIcon;

		case 2:
			return fireStoneIcon;

		case 3:
			return earthStoneIcon;

		case 4:
			return airStoneIcon;

		case 5:
			return duskStoneIcon;

		default:
			return blankIcon;
		}
	}
}
