package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.simple.HomSpell;
import WayofTime.alchemicalWizardry.common.spell.simple.HomSpellRegistry;

public class TEHomHeart extends TileEntity
{
	public boolean canCastSpell(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		return true;
	}

	public int castSpell(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		HomSpell spell = getSpell();

		if (spell != null)
		{
			switch (getModifiedParadigm())
			{
			case 0:
				spell.onOffensiveRangedRightClick(par1ItemStack, par2World, par3EntityPlayer);
				break;

			case 1:
				spell.onOffensiveMeleeRightClick(par1ItemStack, par2World, par3EntityPlayer);
				break;

			case 2:
				spell.onDefensiveRightClick(par1ItemStack, par2World, par3EntityPlayer);
				break;

			case 3:
				spell.onEnvironmentalRightClick(par1ItemStack, par2World, par3EntityPlayer);
				break;
			}

			//spell.onOffensiveRangedRightClick(par1ItemStack, par2World, par3EntityPlayer);
		}

		return 0;
	}

	public HomSpell getSpell()
	{
		TileEntity tileEntity = worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord);

		if (tileEntity instanceof TEAltar)
		{
			ItemStack itemStack = ((TEAltar)tileEntity).getStackInSlot(0);

			if (itemStack != null)
			{
				HomSpell spell = HomSpellRegistry.getSpellForItemStack(itemStack);

				if (spell != null)
				{
					return spell;
				}
			}
		}

		tileEntity = worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord);

		if (tileEntity instanceof TEAltar)
		{
			ItemStack itemStack = ((TEAltar)tileEntity).getStackInSlot(0);

			if (itemStack != null)
			{
				HomSpell spell = HomSpellRegistry.getSpellForItemStack(itemStack);

				if (spell != null)
				{
					return spell;
				}
			}
		}

		tileEntity = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1);

		if (tileEntity instanceof TEAltar)
		{
			ItemStack itemStack = ((TEAltar)tileEntity).getStackInSlot(0);

			if (itemStack != null)
			{
				HomSpell spell = HomSpellRegistry.getSpellForItemStack(itemStack);

				if (spell != null)
				{
					return spell;
				}
			}
		}

		tileEntity = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1);

		if (tileEntity instanceof TEAltar)
		{
			ItemStack itemStack = ((TEAltar)tileEntity).getStackInSlot(0);

			if (itemStack != null)
			{
				HomSpell spell = HomSpellRegistry.getSpellForItemStack(itemStack);

				if (spell != null)
				{
					return spell;
				}
			}
		}

		return null;
	}

	public int getModifiedParadigm()
	{
		//TODO change so that it works with a Tile Entity for a custom head or whatnot
		int blockID = worldObj.getBlockId(xCoord, yCoord + 1, zCoord);

		if (blockID == Block.glowStone.blockID)
		{
			return 0;
		}
		else if (blockID == Block.blockRedstone.blockID)
		{
			return 1;
		}
		else if (blockID == Block.anvil.blockID)
		{
			return 2;
		}
		else if (blockID == Block.glass.blockID)
		{
			return 3;
		}

		TileEntity tileEntity = worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord);

		if (tileEntity instanceof TileEntitySkull)
		{
			int skullType = ((TileEntitySkull)tileEntity).getSkullType();

			switch (skullType)
			{
			case 0:
				return 0;

			case 1:
				return 1;

			case 2:
				return 2;

			case 4:
				return 3;
			}
		}

		return -1;
	}
}
