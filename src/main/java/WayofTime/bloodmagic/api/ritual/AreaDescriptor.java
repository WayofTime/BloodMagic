package WayofTime.bloodmagic.api.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class AreaDescriptor
{
    public List<BlockPos> getContainedPositions(BlockPos pos)
    {
        return new ArrayList<BlockPos>();
    }

    public AxisAlignedBB getAABB(BlockPos pos)
    {
        return null;
    }

    public static class Rectangle extends AreaDescriptor
    {
        private BlockPos minimumOffset;
        private BlockPos maximumOffset; // Non-inclusive maximum offset.

        private ArrayList<BlockPos> blockPosCache = new ArrayList<BlockPos>();
        private BlockPos cachedPosition = new BlockPos(0, 0, 0);

        /**
         * This constructor takes in the minimum and maximum BlockPos. The
         * maximum offset is non-inclusive, meaning if you pass in (0,0,0) and
         * (1,1,1), calling getContainedPositions() will only give (0,0,0).
         * 
         * @param minimumOffset
         * @param maximumOffset
         */
        public Rectangle(BlockPos minimumOffset, BlockPos maximumOffset)
        {
            setOffsets(minimumOffset, maximumOffset);
        }
       
        public Rectangle(BlockPos minimumOffset, int sizeX, int sizeY, int sizeZ)
        {
            this(minimumOffset, minimumOffset.add(sizeX, sizeY, sizeZ));
        }
        
        public Rectangle(BlockPos minimumOffset, int size)
        {
            this(minimumOffset, size, size, size);
        }

        @Override
        public List<BlockPos> getContainedPositions(BlockPos pos)
        {
            if (!pos.equals(cachedPosition) || blockPosCache.isEmpty())
            {
                ArrayList<BlockPos> posList = new ArrayList<BlockPos>();

                for (int i = minimumOffset.getX(); i < maximumOffset.getX(); i++)
                {
                    for (int j = minimumOffset.getY(); j < maximumOffset.getY(); j++)
                    {
                        for (int k = minimumOffset.getZ(); k < maximumOffset.getZ(); k++)
                        {
                            posList.add(pos.add(i, j, k));
                        }
                    }
                }

                blockPosCache = posList;
                cachedPosition = pos;
            }

            return blockPosCache;
        }

        @Override
        public AxisAlignedBB getAABB(BlockPos pos)
        {
            AxisAlignedBB tempAABB = new AxisAlignedBB(minimumOffset, maximumOffset);
            return tempAABB.offset(pos.getX(), pos.getY(), pos.getZ());
        }

        /**
         * Sets the offsets of the AreaDescriptor in a safe way that will make
         * minimumOffset the lowest corner
         * 
         * @param offset1
         * @param offset2
         */
        public void setOffsets(BlockPos offset1, BlockPos offset2)
        {
            this.minimumOffset = new BlockPos(Math.min(offset1.getX(), offset2.getX()), Math.min(offset1.getY(), offset2.getY()), Math.min(offset1.getZ(), offset2.getZ()));
            this.maximumOffset = new BlockPos(Math.max(offset1.getX(), offset2.getX()), Math.max(offset1.getY(), offset2.getY()), Math.max(offset1.getZ(), offset2.getZ()));
            blockPosCache = new ArrayList<BlockPos>();
        }
    }
}
