package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.ItemSpellMultiTool;

public class DigAreaTunnel extends DigAreaEffect
{
    Random rand = new Random();

    public DigAreaTunnel(int power, int potency, int cost)
    {
        super(power, potency, cost);
    }

    @Override
    public int digSurroundingArea(ItemStack container, World world, EntityPlayer player, MovingObjectPosition blockPos, String usedToolClass, float blockHardness, int harvestLvl, ItemSpellMultiTool itemTool)
    {
        if (!blockPos.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK))
        {
            return 0;
        }

        List<Vec3> vectorLine = new LinkedList();

        double initialX = blockPos.func_178782_a().getX();
        double initialY = blockPos.func_178782_a().getY();
        double initialZ = blockPos.func_178782_a().getZ();
        EnumFacing sidehit = blockPos.field_178784_b;
        EnumFacing opposite = sidehit.getOpposite();

        double initialLength = this.getRandomVectorLength();

        Vec3 initialVector = new Vec3(opposite.getFrontOffsetX() * initialLength, opposite.getFrontOffsetY() * initialLength, opposite.getFrontOffsetZ() * initialLength);

        Vec3 lastVec = new Vec3(initialVector.xCoord, initialVector.yCoord, initialVector.zCoord);
        vectorLine.add(initialVector);

        double currentLength = lastVec.lengthVector();
        double totalLength = this.totalLength();

        while (currentLength < totalLength - 0.01)
        {
            Vec3 tempVec = lastVec.addVector(0, 0, 0);

            tempVec = tempVec.normalize();

            double varr = this.varyRate();

            tempVec = tempVec.addVector(varr * (rand.nextFloat() - rand.nextFloat()), varr * (rand.nextFloat() - rand.nextFloat()), varr * (rand.nextFloat() - rand.nextFloat()));

            tempVec = tempVec.normalize();

            double length = Math.min(this.getRandomVectorLength(), totalLength - currentLength);

            tempVec = new Vec3(tempVec.xCoord * length, tempVec.yCoord * length, tempVec.zCoord * length);

            vectorLine.add(tempVec);

            lastVec = tempVec;

            currentLength += tempVec.lengthVector();
        }

        for (Vec3 testVec : vectorLine)
        {
            this.travelVector(testVec, world, initialX, initialY, initialZ);

            initialX += testVec.xCoord;
            initialY += testVec.yCoord;
            initialZ += testVec.zCoord;
        }

        this.travelVector(lastVec, world, initialX, initialY, initialZ);

        return 0;
    }

    public double totalLength()
    {
        return 100;
    }

    public double getStepSize()
    {
        return 1;
    }

    public double varyRate()
    {
        return 0.5;
    }

    public double getRandomVectorLength()
    {
        return 10;
    }

    public double getRandomStepLength()
    {
        return 0.5;
    }

    public int getRandomRadius()
    {
        return 3;
    }

    public void destroySphereOfMundane(World world, double x, double y, double z, int radius)
    {
    	BlockPos pos = new BlockPos(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
        for (int i = -radius; i <= radius; i++)
        {
            for (int j = -radius; j <= radius; j++)
            {
                for (int k = -radius; k <= radius; k++)
                {
                    if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f))
                    {
                        continue;
                    }

                    BlockPos newPos = pos.add(i, j, k);

                    this.destroyMunadeAt(world, newPos);
                }
            }
        }
    }

    public void destroyMunadeAt(World world, BlockPos pos)
    {
        world.setBlockToAir(pos);
    }

    public void travelVector(Vec3 vector, World world, double x, double y, double z)
    {
        double vecLength = vector.lengthVector();
        AlchemicalWizardry.logger.info(vecLength);
        Vec3 normVec = new Vec3(vector.xCoord, vector.yCoord, vector.zCoord);
        normVec = normVec.normalize();

        Vec3 prevVec = new Vec3(0, 0, 0);
        double distanceTravelled = 0;

        while (distanceTravelled < vecLength)
        {
            AlchemicalWizardry.logger.info(distanceTravelled);
            double stepLength = this.getRandomStepLength();

            prevVec = prevVec.addVector(stepLength * normVec.xCoord, stepLength * normVec.yCoord, normVec.zCoord);

            this.destroySphereOfMundane(world, prevVec.xCoord + x, prevVec.yCoord + y, prevVec.zCoord + z, this.getRandomRadius());

            distanceTravelled = prevVec.lengthVector();
        }
    }
}
