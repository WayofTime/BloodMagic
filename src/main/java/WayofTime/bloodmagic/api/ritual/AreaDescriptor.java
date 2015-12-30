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
        private BlockPos maximumOffset;

        public Rectangle(BlockPos minimumOffset, BlockPos maximumOffset)
        {
            setOffsets(minimumOffset, maximumOffset);
        }

        @Override
        public List<BlockPos> getContainedPositions(BlockPos pos)
        {
            ArrayList<BlockPos> posList = new ArrayList<BlockPos>();
            
            for (int i = minimumOffset.getX(); i <= maximumOffset.getX(); i++)
            {
                for (int j = minimumOffset.getY(); j <= maximumOffset.getY(); j++)
                {
                    for (int k = minimumOffset.getZ(); k <= maximumOffset.getZ(); k++)
                    {
                        posList.add(pos.add(i, j, k));
                    }
                }
            }
            
            return posList;
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
        }
    }
}
