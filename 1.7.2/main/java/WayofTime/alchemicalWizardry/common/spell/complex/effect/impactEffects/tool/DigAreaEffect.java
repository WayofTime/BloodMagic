package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.common.items.spell.ItemSpellMultiTool;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class DigAreaEffect implements IDigAreaEffect
{
	protected int powerUpgrades;
	protected int potencyUpgrades;
	protected int costUpgrades;
	
	public DigAreaEffect(int power, int potency, int cost)
	{
		this.powerUpgrades = power;
		this.potencyUpgrades = potency;
		this.costUpgrades = cost;
	}
	
	@Override
	public int digSurroundingArea(ItemStack container, World world, EntityPlayer player, MovingObjectPosition blockPos, String usedToolClass, float blockHardness, int harvestLvl, ItemSpellMultiTool itemTool) 
	{
		if(!blockPos.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK))
		{
			return 0;
		}
		
		int x = blockPos.blockX;
		int y = blockPos.blockY;
		int z = blockPos.blockZ;
		ForgeDirection sidehit = ForgeDirection.getOrientation(blockPos.sideHit);
		
		for(int xPos = x-1; xPos <= x+1; xPos++)
		{
			for(int yPos = y-1; yPos <= y+1; yPos++)
			{
				for(int zPos = z-1; zPos <= z+1; zPos++)
				{
					this.breakBlock(container, world, player, blockHardness, xPos, yPos, zPos, itemTool); 
				}
			}
		}
		
		return 0;
	}
	
	public void breakBlock(ItemStack container, World world, EntityPlayer player, float blockHardness, int x, int y, int z, ItemSpellMultiTool itemTool)
	{	
		int hlvl = -1;
		Block localBlock = world.getBlock(x, y, z);
        int localMeta = world.getBlockMetadata(x, y, z);
        String toolClass = localBlock.getHarvestTool(localMeta);
        if (toolClass != null && itemTool.getHarvestLevel(container, toolClass) != -1)
            hlvl = localBlock.getHarvestLevel(localMeta);
        int toolLevel = itemTool.getHarvestLevel(container, toolClass);
        
        float localHardness = localBlock == null ? Float.MAX_VALUE : localBlock.getBlockHardness(world, x, y, z);
        
        if (hlvl <= toolLevel && localHardness - this.getHardnessDifference() <= blockHardness)
        {
            boolean cancelHarvest = false;

            if (!cancelHarvest)
            {
                if (localBlock != null && !(localHardness < 0))
                {
                    boolean isEffective = false;

                    String localToolClass = itemTool.getToolClassForMaterial(localBlock.getMaterial());

                    if(localToolClass != null && itemTool.getHarvestLevel(container, toolClass) >= localBlock.getHarvestLevel(localMeta))
                    {
                    	isEffective = true;
                    }


                    if (localBlock.getMaterial().isToolNotRequired())
                    {
                        isEffective = true;
                    }


                    if (!player.capabilities.isCreativeMode)
                    {
                        if (isEffective)
                        {
                            if (localBlock.removedByPlayer(world, player, x, y, z))
                            {
                                localBlock.onBlockDestroyedByPlayer(world, x, y, z, localMeta);
                            }
                            //localBlock.harvestBlock(world, player, x, y, z, localMeta);
                            localBlock.onBlockHarvested(world, x, y, z, localMeta, player);
                            if (localHardness > 0f)
                            	itemTool.onBlockDestroyed(container, world, localBlock, x, y, z, player);
                            
                            List<ItemStack> items = SpellHelper.getItemsFromBlock(world, localBlock, x, y, z, localMeta, itemTool.getSilkTouch(container), itemTool.getFortuneLevel(container));
                            
                            SpellParadigmTool parad = itemTool.loadParadigmFromStack(container);
                            items = parad.handleItemList(container, items);
                            
                            if(!world.isRemote)
                            {
                            	SpellHelper.spawnItemListInWorld(items, world, x + 0.5f, y + 0.5f, z + 0.5f);
                            }
                            
                            world.func_147479_m(x, y, z);
                        }
                        else
                        {
//                            world.setBlockToAir(x, y, z);
//                            world.func_147479_m(x, y, z);
                        }


                    }
                    else
                    {
                        world.setBlockToAir(x, y, z);
                        world.func_147479_m(x, y, z);
                    }
                }
            }
        }
	}
	
	public float getHardnessDifference()
	{
		return 1.5f;
	}
}
