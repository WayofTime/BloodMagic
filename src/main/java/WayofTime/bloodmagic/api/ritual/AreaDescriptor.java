package WayofTime.bloodmagic.api.ritual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public abstract class AreaDescriptor implements Iterator<BlockPos>
{
    public List<BlockPos> getContainedPositions(BlockPos pos)
    {
        return new ArrayList<BlockPos>();
    }

    public AxisAlignedBB getAABB(BlockPos pos)
    {
        return null;
    }

    public abstract void resetCache();

    public abstract boolean isWithinArea(BlockPos pos);

    public static class Rectangle extends AreaDescriptor
    {
        private BlockPos minimumOffset;
        private BlockPos maximumOffset; // Non-inclusive maximum offset.
        private BlockPos currentPosition;

        private ArrayList<BlockPos> blockPosCache;
        private BlockPos cachedPosition;

        private boolean cache = true;

        /**
         * This constructor takes in the minimum and maximum BlockPos. The
         * maximum offset is non-inclusive, meaning if you pass in (0,0,0) and
         * (1,1,1), calling getContainedPositions() will only give (0,0,0).
         * 
         * @param minimumOffset
         *        -
         * @param maximumOffset
         *        -
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
            if (!cache || !pos.equals(cachedPosition) || blockPosCache.isEmpty())
            {
                ArrayList<BlockPos> posList = new ArrayList<BlockPos>();

                for (int j = minimumOffset.getY(); j < maximumOffset.getY(); j++)
                {
                    for (int i = minimumOffset.getX(); i < maximumOffset.getX(); i++)
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

            return Collections.unmodifiableList(blockPosCache);
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
         *        -
         * @param offset2
         *        -
         */
        public void setOffsets(BlockPos offset1, BlockPos offset2)
        {
            this.minimumOffset = new BlockPos(Math.min(offset1.getX(), offset2.getX()), Math.min(offset1.getY(), offset2.getY()), Math.min(offset1.getZ(), offset2.getZ()));
            this.maximumOffset = new BlockPos(Math.max(offset1.getX(), offset2.getX()), Math.max(offset1.getY(), offset2.getY()), Math.max(offset1.getZ(), offset2.getZ()));
            blockPosCache = new ArrayList<BlockPos>();
        }

        @Override
        public void resetCache()
        {
            this.blockPosCache = new ArrayList<BlockPos>();
        }

        @Override
        public boolean isWithinArea(BlockPos pos)
        {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            return x >= minimumOffset.getX() && x < maximumOffset.getX() && y >= minimumOffset.getY() && y < maximumOffset.getY() && z >= minimumOffset.getZ() && z < maximumOffset.getZ();
        }

        @Override
        public void forEachRemaining(Consumer<? super BlockPos> action)
        {
            while (hasNext())
            {
                action.accept(next());
            }
        }

        @Override
        public boolean hasNext()
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public BlockPos next()
        {
            if (currentPosition != null)
            {
                int nextX = currentPosition.getX() + 1 >= maximumOffset.getX() ? minimumOffset.getX() : currentPosition.getX() + 1;
                int nextZ = nextX == minimumOffset.getX() ? currentPosition.getZ() : (currentPosition.getZ() + 1 >= maximumOffset.getZ() ? minimumOffset.getZ() : currentPosition.getZ() + 1);
                int nextY = nextZ == minimumOffset.getZ() ? currentPosition.getY() : currentPosition.getY() + 1;
                currentPosition = new BlockPos(nextX, nextY, nextZ);
            } else
            {
                currentPosition = minimumOffset;
            }

            return cachedPosition.add(currentPosition);
        }

        @Override
        public void remove()
        {

        }
    }

    public static class HemiSphere extends AreaDescriptor
    {
        private BlockPos minimumOffset;
        private int radius;

        private ArrayList<BlockPos> blockPosCache;
        private BlockPos cachedPosition;

        private boolean cache = true;

        public HemiSphere(BlockPos minimumOffset, int radius)
        {
            setRadius(minimumOffset, radius);
        }

        public void setRadius(BlockPos minimumOffset, int radius)
        {
            this.minimumOffset = new BlockPos(Math.min(minimumOffset.getX(), minimumOffset.getX()), Math.min(minimumOffset.getY(), minimumOffset.getY()), Math.min(minimumOffset.getZ(), minimumOffset.getZ()));
            this.radius = radius;
            blockPosCache = new ArrayList<BlockPos>();
        }

        @Override
        public List<BlockPos> getContainedPositions(BlockPos pos)
        {
            if (!cache || !pos.equals(cachedPosition) || blockPosCache.isEmpty())
            {
                ArrayList<BlockPos> posList = new ArrayList<BlockPos>();

                int i = -radius;
                int j = minimumOffset.getY();
                int k = -radius;

                //TODO For some reason the bottom of the hemisphere is not going up with the minOffset

                while (i <= radius)
                {
                    while (j <= radius)
                    {
                        while (k <= radius)
                        {
                            if (i * i + j * j + k * k >= (radius + 0.5F) * (radius + 0.5F))
                            {
                                k++;
                                continue;
                            }

                            posList.add(pos.add(i, j, k));
                            k++;
                        }

                        k = -radius;
                        j++;
                    }

                    j = minimumOffset.getY();
                    i++;
                }

                blockPosCache = posList;
                cachedPosition = pos;
            }

            return Collections.unmodifiableList(blockPosCache);
        }

        /**
         * Since you can't make a box using a sphere, this returns null
         */
        @Override
        public AxisAlignedBB getAABB(BlockPos pos)
        {
            return null;
        }

        @Override
        public void resetCache()
        {
            this.blockPosCache = new ArrayList<BlockPos>();
        }

        @Override
        public boolean isWithinArea(BlockPos pos)
        {
            return blockPosCache.contains(pos);
        }

        @Override
        public void forEachRemaining(Consumer<? super BlockPos> arg0)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean hasNext()
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public BlockPos next()
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void remove()
        {
            // TODO Auto-generated method stub

        }
    }

    public static class Cross extends AreaDescriptor
    {

        private ArrayList<BlockPos> blockPosCache;
        private BlockPos cachedPosition;

        private BlockPos centerPos;
        private int size;

        private boolean cache = true;

        public Cross(BlockPos center, int size)
        {
            this.centerPos = center;
            this.size = size;
            this.blockPosCache = new ArrayList<BlockPos>();
        }

        @Override
        public List<BlockPos> getContainedPositions(BlockPos pos)
        {
            if (!cache || !pos.equals(cachedPosition) || blockPosCache.isEmpty())
            {
                resetCache();

                blockPosCache.add(centerPos.add(pos));
                for (int i = 1; i <= size; i++)
                {
                    blockPosCache.add(centerPos.add(pos).add(i, 0, 0));
                    blockPosCache.add(centerPos.add(pos).add(0, 0, i));
                    blockPosCache.add(centerPos.add(pos).add(-i, 0, 0));
                    blockPosCache.add(centerPos.add(pos).add(0, 0, -i));
                }
            }

            cachedPosition = pos;

            return Collections.unmodifiableList(blockPosCache);
        }

        @Override
        public void resetCache()
        {
            blockPosCache = new ArrayList<BlockPos>();
        }

        @Override
        public boolean isWithinArea(BlockPos pos)
        {
            return blockPosCache.contains(pos);
        }

        @Override
        public boolean hasNext()
        {
            return false;
        }

        @Override
        public BlockPos next()
        {
            return null;
        }
    }
}
