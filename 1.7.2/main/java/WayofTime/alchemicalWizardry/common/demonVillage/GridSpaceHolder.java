package WayofTime.alchemicalWizardry.common.demonVillage;

import net.minecraftforge.common.util.ForgeDirection;

public class GridSpaceHolder
{
	public GridSpace[][] area;
	public int negXRadius; //These variables indicate how much the grid has expanded from the 1x1
	public int posXRadius; //matrix in each direction
	public int negZRadius;
	public int posZRadius;
	
	public GridSpaceHolder()
	{
		area = new GridSpace[1][1];
		area[0][0] = new GridSpace();
	}
	
	public void expandAreaInNegX()
	{
		GridSpace[][] newGrid = new GridSpace[negXRadius + posXRadius + 2][negZRadius + posZRadius + 1];
		for(int i=0; i<=negZRadius + posZRadius; i++)
		{
			newGrid[0][i] = new GridSpace();
		}
		
		for(int i=0; i<=negXRadius + posXRadius; i++)
		{
			for(int j=0; j<=negZRadius + posZRadius; j++)
			{
				newGrid[i+1][j] = area[i][j];
			}
		}
		
		area = newGrid;
		negXRadius += 1;
	}
	
	public void expandAreaInPosX()
	{
		GridSpace[][] newGrid = new GridSpace[negXRadius + posXRadius + 2][negZRadius + posZRadius + 1];
		
		for(int i=0; i<=negZRadius + posZRadius; i++)
		{
			newGrid[negXRadius + posXRadius + 1][i] = new GridSpace();
		}
		
		for(int i=0; i<=negXRadius + posXRadius; i++)
		{
			for(int j=0; j<=negZRadius + posZRadius; j++)
			{
				newGrid[i][j] = area[i][j];
			}
		}
		
		area = newGrid;
		posXRadius += 1;
	}
	
	public void expandAreaInNegZ()
	{
		GridSpace[][] newGrid = new GridSpace[negXRadius + posXRadius + 1][negZRadius + posZRadius + 2];
		
		System.out.println("x " + newGrid.length + "z " + newGrid[0].length);
		
		for(int i=0; i<=negXRadius + posXRadius; i++)
		{
			newGrid[i][0] = new GridSpace();
		}
		
		for(int i=0; i<=negXRadius + posXRadius; i++)
		{
			for(int j=0; j<=negZRadius + posZRadius; j++)
			{
				newGrid[i][j+1] = area[i][j];
			}
		}
		
		area = newGrid;
		negZRadius += 1;
	}
	
	public void expandAreaInPosZ()
	{
		GridSpace[][] newGrid = new GridSpace[negXRadius + posXRadius + 1][negZRadius + posZRadius + 2];
		
		for(int i=0; i<=negXRadius + posXRadius; i++)
		{
			newGrid[i][negZRadius + posZRadius + 1] = new GridSpace();
		}
		
		for(int i=0; i<=negXRadius + posXRadius; i++)
		{
			for(int j=0; j<=negZRadius + posZRadius; j++)
			{
				newGrid[i][j] = area[i][j];
			}
		}
		
		area = newGrid;
		posZRadius += 1;
	}
	
	public GridSpace getGridSpace(int x, int z)
	{
		if(x > posXRadius|| x < -negXRadius || z > posZRadius || z < -negZRadius)
		{
			return new GridSpace();
		}else
		{
			return (area[x + negXRadius][z + negZRadius]);
		}
	}
	
	public void setGridSpace(int x, int z, GridSpace space)
	{
		if(x > posXRadius)
		{
			this.expandAreaInPosX();
			this.setGridSpace(x, z, space);
			return;
		}else if(x < -negXRadius)
		{
			this.expandAreaInNegX();
			this.setGridSpace(x, z, space);
			return;
		}else if(z > posZRadius)
		{
			this.expandAreaInPosZ();
			this.setGridSpace(x, z, space);
			return;
		}else if(z < -negZRadius)
		{
			this.expandAreaInNegZ();
			this.setGridSpace(x, z, space);
			return;
		}else
		{
			area[x + negXRadius][z + negZRadius] = space;
		}
	}
	
	public boolean doesContainAll(GridSpaceHolder master, int xInit, int zInit, ForgeDirection dir)
	{
		if(master != null)
		{
			for(int i=-negXRadius; i<=posXRadius; i++)
			{
				for(int j=-negZRadius; j<=posZRadius; j++)
				{
					GridSpace thisSpace = this.getGridSpace(i, j);
					if(thisSpace.isEmpty())
					{
						continue;
					}
					
					int xOff = 0;
					int zOff = 0;
					
					switch(dir)
					{
					case SOUTH:
						xOff = -i;
						zOff = -j;
						break;
					case WEST:
						xOff = j;
						zOff = -i;
						break;
					case EAST:
						xOff = -j;
						zOff = i;
						break;
					default:
						xOff = i;
						zOff = j;
						break;
					}
					if(!master.getGridSpace(xInit + xOff, zInit + zOff).isEmpty())
					{
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	public void setAllGridSpaces(int xInit, int zInit, int yLevel, ForgeDirection dir, int type, GridSpaceHolder master)
	{
		if(master != null)
		{
			for(int i=-negXRadius; i<=posXRadius; i++)
			{
				for(int j=-negZRadius; j<=posZRadius; j++)
				{
					GridSpace thisSpace = this.getGridSpace(i, j);
					if(thisSpace.isEmpty())
					{
						continue;
					}
					
					int xOff = 0;
					int zOff = 0;
					
					switch(dir)
					{
					case SOUTH:
						xOff = -i;
						zOff = -j;
						break;
					case WEST:
						xOff = j;
						zOff = -i;
						break;
					case EAST:
						xOff = -j;
						zOff = i;
						break;
					default:
						xOff = i;
						zOff = j;
						break;
					}
					
					master.setGridSpace(xInit + xOff, zInit + zOff, new GridSpace(type, yLevel));
				}
			}
		}
	}
}
