package WayofTime.alchemicalWizardry.common.tileEntity;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.demonVillage.BuildingSchematic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TESchematicSaver extends TileEntity
{
    public Block targetBlock = ModBlocks.largeBloodStoneBrick;

    public void rightClickBlock(EntityPlayer player, int side)
    {
        BuildingSchematic schematic = new BuildingSchematic();

        int negX = this.getNegXLimit();
        int negY = this.getNegYLimit();
        int negZ = this.getNegZLimit();
        int posX = this.getPosXLimit();
        int posY = this.getPosYLimit();
        int posZ = this.getPosZLimit();

        for (int i = -negX + 1; i <= posX - 1; i++)
        {
            for (int j = -negY + 1; j <= posY - 1; j++)
            {
                for (int k = -negZ + 1; k <= posZ - 1; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                	IBlockState state = worldObj.getBlockState(newPos);
                    Block block = state.getBlock();
                    int meta = block.getMetaFromState(state);

                    if (!block.isAir(worldObj, newPos))
                    {
                        schematic.addBlockWithMeta(block, meta, i, j, k);
                    }
                }
            }

            AlchemicalWizardry.logger.info("" + i);
        }

        AlchemicalWizardry.logger.info("I got here!");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(schematic);
        AlchemicalWizardry.logger.info("Here, too!");
        Writer writer;
        try
        {
            writer = new FileWriter("config/BloodMagic/schematics/" + new Random().nextInt() + ".json");
            writer.write(json);
            writer.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public int getPosYLimit()
    {
        int i = 1;
        while (i < 100)
        {
            if (targetBlock == (worldObj.getBlockState(pos.add(0, i, 0)).getBlock()))
            {
                return i;
            }

            i++;
        }
        return 1;
    }

    public int getNegYLimit()
    {
        int i = 1;
        while (i < 100)
        {
            if (targetBlock == (worldObj.getBlockState(pos.add(0, -i, 0)).getBlock()))
            {
                return i;
            }

            i++;
        }
        return 1;
    }

    public int getPosXLimit()
    {
        int i = 1;
        while (i < 100)
        {
            if (targetBlock == (worldObj.getBlockState(pos.add(i, 0, 0)).getBlock()))
            {
                return i;
            }

            i++;
        }
        return 1;
    }

    public int getNegXLimit()
    {
        int i = 1;
        while (i < 100)
        {
            if (targetBlock == (worldObj.getBlockState(pos.add(-i, 0, 0)).getBlock()))
            {
                return i;
            }

            i++;
        }
        return 1;
    }

    public int getPosZLimit()
    {
        int i = 1;
        while (i < 100)
        {
            if (targetBlock == (worldObj.getBlockState(pos.add(0, 0, i)).getBlock()))
            {
                return i;
            }

            i++;
        }
        return 1;
    }

    public int getNegZLimit()
    {
        int i = 1;
        while (i < 100)
        {
            if (targetBlock == (worldObj.getBlockState(pos.add(0, 0, -i)).getBlock()))
            {
                return i;
            }

            i++;
        }
        return 1;
    }
}