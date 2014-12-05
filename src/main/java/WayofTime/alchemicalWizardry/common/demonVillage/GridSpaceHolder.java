package WayofTime.alchemicalWizardry.common.demonVillage;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;
import net.minecraft.block.Block;
import net.minecraft.world.World;
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
        for (int i = 0; i <= negZRadius + posZRadius; i++)
        {
            newGrid[0][i] = new GridSpace();
        }

        for (int i = 0; i <= negXRadius + posXRadius; i++)
        {
            for (int j = 0; j <= negZRadius + posZRadius; j++)
            {
                newGrid[i + 1][j] = area[i][j];
            }
        }

        area = newGrid;
        negXRadius += 1;
    }

    public void expandAreaInPosX()
    {
        GridSpace[][] newGrid = new GridSpace[negXRadius + posXRadius + 2][negZRadius + posZRadius + 1];

        for (int i = 0; i <= negZRadius + posZRadius; i++)
        {
            newGrid[negXRadius + posXRadius + 1][i] = new GridSpace();
        }

        for (int i = 0; i <= negXRadius + posXRadius; i++)
        {
            for (int j = 0; j <= negZRadius + posZRadius; j++)
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

        for (int i = 0; i <= negXRadius + posXRadius; i++)
        {
            newGrid[i][0] = new GridSpace();
        }

        for (int i = 0; i <= negXRadius + posXRadius; i++)
        {
            for (int j = 0; j <= negZRadius + posZRadius; j++)
            {
                newGrid[i][j + 1] = area[i][j];
            }
        }

        area = newGrid;
        negZRadius += 1;
    }

    public void expandAreaInPosZ()
    {
        GridSpace[][] newGrid = new GridSpace[negXRadius + posXRadius + 1][negZRadius + posZRadius + 2];

        for (int i = 0; i <= negXRadius + posXRadius; i++)
        {
            newGrid[i][negZRadius + posZRadius + 1] = new GridSpace();
        }

        for (int i = 0; i <= negXRadius + posXRadius; i++)
        {
            for (int j = 0; j <= negZRadius + posZRadius; j++)
            {
                newGrid[i][j] = area[i][j];
            }
        }

        area = newGrid;
        posZRadius += 1;
    }

    public GridSpace getGridSpace(int x, int z)
    {
        if (x > posXRadius || x < -negXRadius || z > posZRadius || z < -negZRadius)
        {
            return new GridSpace();
        } else
        {
            return (area[x + negXRadius][z + negZRadius]);
        }
    }

    public void setGridSpace(int x, int z, GridSpace space)
    {
        if (x > posXRadius)
        {
            this.expandAreaInPosX();
            this.setGridSpace(x, z, space);
            return;
        } else if (x < -negXRadius)
        {
            this.expandAreaInNegX();
            this.setGridSpace(x, z, space);
            return;
        } else if (z > posZRadius)
        {
            this.expandAreaInPosZ();
            this.setGridSpace(x, z, space);
            return;
        } else if (z < -negZRadius)
        {
            this.expandAreaInNegZ();
            this.setGridSpace(x, z, space);
            return;
        } else
        {
            area[x + negXRadius][z + negZRadius] = space;
        }
    }

    public boolean doesContainAll(GridSpaceHolder master, int xInit, int zInit, ForgeDirection dir)
    {
        if (master != null)
        {
        	if(TEDemonPortal.printDebug)
            AlchemicalWizardry.logger.info("negXRadius: " + negXRadius + " posXRadius: " + posXRadius + " negZRadius: " + negZRadius + " posZRadius: " + posZRadius);
            for (int i = -negXRadius; i <= posXRadius; i++)
            {
                for (int j = -negZRadius; j <= posZRadius; j++)
                {
                    GridSpace thisSpace = this.getGridSpace(i, j);
                    if (thisSpace.isEmpty())
                    {
                        continue;
                    }

                    if(TEDemonPortal.printDebug)
                    AlchemicalWizardry.logger.info("x: " + i + " z: " + j);

                    int xOff = 0;
                    int zOff = 0;

                    switch (dir)
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
                    if (!master.getGridSpace(xInit + xOff, zInit + zOff).isEmpty())
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
    	if(TEDemonPortal.printDebug)
        AlchemicalWizardry.logger.info("Grid space selected: (" + xInit + "," + zInit + ")");
        if (master != null)
        {
            for (int i = -negXRadius; i <= posXRadius; i++)
            {
                for (int j = -negZRadius; j <= posZRadius; j++)
                {
                    GridSpace thisSpace = this.getGridSpace(i, j);
                    if (thisSpace.isEmpty())
                    {
                        continue;
                    }

                    int xOff = 0;
                    int zOff = 0;

                    switch (dir)
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

                    if(TEDemonPortal.printDebug)
                    AlchemicalWizardry.logger.info("Grid space (" + (xInit + xOff) + "," + (zInit + zOff) + ")");

                    master.setGridSpace(xInit + xOff, zInit + zOff, new GridSpace(type, yLevel));
                }
            }
        }
    }

    public void destroyAllInGridSpaces(World world, int xCoord, int yCoord, int zCoord, ForgeDirection dir)
    {
        for (int i = -negXRadius; i <= posXRadius; i++)
        {
            for (int j = -negZRadius; j <= posZRadius; j++)
            {
                GridSpace thisSpace = this.getGridSpace(i, j);
                if (thisSpace.isEmpty())
                {
                    continue;
                }

                int xOff = 0;
                int zOff = 0;

                switch (dir)
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

                for (int l = -2; l <= 2; l++)
                {
                    for (int m = -2; m <= 2; m++)
                    {
                        Block block = world.getBlock(xCoord + xOff * 5 + l, yCoord, zCoord + zOff * 5 + m);
                        if (block == ModBlocks.blockDemonPortal)
                        {
                            continue;
                        }
                        world.setBlockToAir(xCoord + xOff * 5 + l, yCoord, zCoord + zOff * 5 + m);
                    }
                }
            }
        }
    }

    public int getNumberOfGridSpaces()
    {
        int num = 0;
        for (int i = -this.negXRadius; i <= this.posXRadius; i++)
        {
            for (int j = -this.negZRadius; j <= this.posZRadius; j++)
            {
                if (!this.getGridSpace(i, j).isEmpty())
                {
                    num++;
                }
            }
        }

        return num;
    }
}
