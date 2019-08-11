package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.util.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class AreaDescriptor implements Iterator<BlockPos> {
    public List<BlockPos> getContainedPositions(BlockPos pos) {
        return new ArrayList<>();
    }

    public AxisAlignedBB getAABB(BlockPos pos) {
        return null;
    }

    public abstract void resetCache();

    public abstract boolean isWithinArea(BlockPos pos);

    public abstract void resetIterator();

    public void readFromNBT(NBTTagCompound tag) {

    }

    public void writeToNBT(NBTTagCompound tag) {

    }

    public abstract AreaDescriptor copy();

    public abstract int getVolumeForOffsets(BlockPos offset1, BlockPos offset2);

    public abstract boolean isWithinRange(BlockPos offset1, BlockPos offset2, int verticalLimit, int horizontalLimit);

    public abstract int getVolume();

    public abstract int getHeight();

    public abstract boolean isWithinRange(int verticalLimit, int horizontalLimit);

    /**
     * This method changes the area descriptor so that its range matches the two
     * blocks that are selected. When implementing this method, assume that
     * these positions are the blocks that are clicked by the player.
     *
     * @param pos1
     * @param pos2
     */
    public abstract void modifyAreaByBlockPositions(BlockPos pos1, BlockPos pos2);

    public abstract boolean intersects(AreaDescriptor descriptor);

    public abstract AreaDescriptor offset(BlockPos offset);

    public abstract AreaDescriptor rotateDescriptor(PlacementSettings settings);

    public static class Rectangle extends AreaDescriptor {
        protected BlockPos minimumOffset;
        protected BlockPos maximumOffset; // Non-inclusive maximum offset.
        private BlockPos currentPosition;

        private ArrayList<BlockPos> blockPosCache;
        private BlockPos cachedPosition;

        private boolean cache = true;

        /**
         * This constructor takes in the minimum and maximum BlockPos. The
         * maximum offset is non-inclusive, meaning if you pass in (0,0,0) and
         * (1,1,1), calling getContainedPositions() will only give (0,0,0).
         *
         * @param minimumOffset -
         * @param maximumOffset -
         */
        public Rectangle(BlockPos minimumOffset, BlockPos maximumOffset) {
            setOffsets(minimumOffset, maximumOffset);
        }

        public Rectangle(BlockPos minimumOffset, int sizeX, int sizeY, int sizeZ) {
            this(minimumOffset, minimumOffset.add(sizeX, sizeY, sizeZ));
        }

        public Rectangle(BlockPos minimumOffset, int size) {
            this(minimumOffset, size, size, size);
        }

        public Rectangle(AreaDescriptor.Rectangle rectangle) {
            this(rectangle.minimumOffset, rectangle.maximumOffset);
        }

        public AreaDescriptor.Rectangle copy() {
            return new AreaDescriptor.Rectangle(this);
        }

        @Override
        public List<BlockPos> getContainedPositions(BlockPos pos) {
            if (!cache || !pos.equals(cachedPosition) || blockPosCache.isEmpty()) {
                ArrayList<BlockPos> posList = new ArrayList<>();

                for (int j = minimumOffset.getY(); j < maximumOffset.getY(); j++) {
                    for (int i = minimumOffset.getX(); i < maximumOffset.getX(); i++) {
                        for (int k = minimumOffset.getZ(); k < maximumOffset.getZ(); k++) {
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
        public AxisAlignedBB getAABB(BlockPos pos) {
            AxisAlignedBB tempAABB = new AxisAlignedBB(minimumOffset, maximumOffset);
            return tempAABB.offset(pos.getX(), pos.getY(), pos.getZ());
        }

        @Override
        public int getHeight() {
            return this.maximumOffset.getY() - this.minimumOffset.getY();
        }

        public BlockPos getMinimumOffset() {
            return minimumOffset;
        }

        public BlockPos getMaximumOffset() {
            return maximumOffset;
        }

        /**
         * Sets the offsets of the AreaDescriptor in a safe way that will make
         * minimumOffset the lowest corner
         *
         * @param offset1 -
         * @param offset2 -
         */
        public void setOffsets(BlockPos offset1, BlockPos offset2) {
            this.minimumOffset = new BlockPos(Math.min(offset1.getX(), offset2.getX()), Math.min(offset1.getY(), offset2.getY()), Math.min(offset1.getZ(), offset2.getZ()));
            this.maximumOffset = new BlockPos(Math.max(offset1.getX(), offset2.getX()), Math.max(offset1.getY(), offset2.getY()), Math.max(offset1.getZ(), offset2.getZ()));
            blockPosCache = new ArrayList<>();
        }

        @Override
        public void resetCache() {
            this.blockPosCache = new ArrayList<>();
        }

        @Override
        public boolean isWithinArea(BlockPos pos) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            return x >= minimumOffset.getX() && x < maximumOffset.getX() && y >= minimumOffset.getY() && y < maximumOffset.getY() && z >= minimumOffset.getZ() && z < maximumOffset.getZ();
        }

        @Override
        public boolean hasNext() {
            return currentPosition == null || !(currentPosition.getX() + 1 == maximumOffset.getX() && currentPosition.getY() + 1 == maximumOffset.getY() && currentPosition.getZ() + 1 == maximumOffset.getZ());
        }

        @Override
        public BlockPos next() {
            if (currentPosition != null) {
                int nextX = currentPosition.getX() + 1 >= maximumOffset.getX() ? minimumOffset.getX() : currentPosition.getX() + 1;
                int nextZ = nextX != minimumOffset.getX() ? currentPosition.getZ() : (currentPosition.getZ() + 1 >= maximumOffset.getZ() ? minimumOffset.getZ() : currentPosition.getZ() + 1);
                int nextY = (nextZ != minimumOffset.getZ() || nextX != minimumOffset.getX()) ? currentPosition.getY() : (currentPosition.getY() + 1);
                currentPosition = new BlockPos(nextX, nextY, nextZ);
            } else {
                currentPosition = minimumOffset;
            }

            return currentPosition;
        }

        @Override
        public void remove() {

        }

        @Override
        public void resetIterator() {
            currentPosition = null;
        }

        @Override
        public void modifyAreaByBlockPositions(BlockPos pos1, BlockPos pos2) {
            setOffsets(pos1, pos2);
            maximumOffset = maximumOffset.add(1, 1, 1);
            resetIterator();
            resetCache();
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            minimumOffset = new BlockPos(tag.getInteger(Constants.NBT.X_COORD + "min"), tag.getInteger(Constants.NBT.Y_COORD + "min"), tag.getInteger(Constants.NBT.Z_COORD + "min"));
            maximumOffset = new BlockPos(tag.getInteger(Constants.NBT.X_COORD + "max"), tag.getInteger(Constants.NBT.Y_COORD + "max"), tag.getInteger(Constants.NBT.Z_COORD + "max"));
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            tag.setInteger(Constants.NBT.X_COORD + "min", minimumOffset.getX());
            tag.setInteger(Constants.NBT.Y_COORD + "min", minimumOffset.getY());
            tag.setInteger(Constants.NBT.Z_COORD + "min", minimumOffset.getZ());
            tag.setInteger(Constants.NBT.X_COORD + "max", maximumOffset.getX());
            tag.setInteger(Constants.NBT.Y_COORD + "max", maximumOffset.getY());
            tag.setInteger(Constants.NBT.Z_COORD + "max", maximumOffset.getZ());
        }

        @Override
        public int getVolumeForOffsets(BlockPos offset1, BlockPos offset2) {
            BlockPos minPos = new BlockPos(Math.min(offset1.getX(), offset2.getX()), Math.min(offset1.getY(), offset2.getY()), Math.min(offset1.getZ(), offset2.getZ()));
            BlockPos maxPos = new BlockPos(Math.max(offset1.getX(), offset2.getX()), Math.max(offset1.getY(), offset2.getY()), Math.max(offset1.getZ(), offset2.getZ()));

            maxPos = maxPos.add(1, 1, 1);

            return (maxPos.getX() - minPos.getX()) * (maxPos.getY() - minPos.getY()) * (maxPos.getZ() - minPos.getZ());
        }

        @Override
        public boolean isWithinRange(BlockPos offset1, BlockPos offset2, int verticalLimit, int horizontalLimit) {
            BlockPos minPos = new BlockPos(Math.min(offset1.getX(), offset2.getX()), Math.min(offset1.getY(), offset2.getY()), Math.min(offset1.getZ(), offset2.getZ()));
            BlockPos maxPos = new BlockPos(Math.max(offset1.getX(), offset2.getX()), Math.max(offset1.getY(), offset2.getY()), Math.max(offset1.getZ(), offset2.getZ()));

            return minPos.getY() >= -verticalLimit && maxPos.getY() <= verticalLimit && minPos.getX() >= -horizontalLimit && maxPos.getX() <= horizontalLimit && minPos.getZ() >= -horizontalLimit && maxPos.getZ() <= horizontalLimit;
        }

        @Override
        public int getVolume() {
            return (maximumOffset.getX() - minimumOffset.getX()) * (maximumOffset.getY() - minimumOffset.getY()) * (maximumOffset.getZ() - minimumOffset.getZ());
        }

        @Override
        public boolean isWithinRange(int verticalLimit, int horizontalLimit) {
            return minimumOffset.getY() >= -verticalLimit && maximumOffset.getY() <= verticalLimit + 1 && minimumOffset.getX() >= -horizontalLimit && maximumOffset.getX() <= horizontalLimit + 1 && minimumOffset.getZ() >= -horizontalLimit && maximumOffset.getZ() <= horizontalLimit + 1;
        }

        @Override
        public boolean intersects(AreaDescriptor descriptor) {
            if (descriptor instanceof AreaDescriptor.Rectangle) {
                AreaDescriptor.Rectangle rectangle = (AreaDescriptor.Rectangle) descriptor;

                return !(minimumOffset.getX() >= rectangle.maximumOffset.getX() || minimumOffset.getY() >= rectangle.maximumOffset.getY() || minimumOffset.getZ() >= rectangle.maximumOffset.getZ() || rectangle.minimumOffset.getX() >= maximumOffset.getX() || rectangle.minimumOffset.getY() >= maximumOffset.getY() || rectangle.minimumOffset.getZ() >= maximumOffset.getZ());
            }

            return false;
        }

        @Override
        public AreaDescriptor offset(BlockPos offset) {
            return new AreaDescriptor.Rectangle(this.minimumOffset.add(offset), this.maximumOffset.add(offset));
        }

        @Override
        public AreaDescriptor rotateDescriptor(PlacementSettings settings) {
            BlockPos rotatePos1 = Template.transformedBlockPos(settings, minimumOffset);
            BlockPos rotatePos2 = Template.transformedBlockPos(settings, maximumOffset.add(-1, -1, -1)); //It works, shut up!

            AreaDescriptor.Rectangle rectangle = new AreaDescriptor.Rectangle(this.minimumOffset, 1);
            rectangle.modifyAreaByBlockPositions(rotatePos1, rotatePos2);

            return rectangle;
        }
    }

    public static class HemiSphere extends AreaDescriptor {
        private BlockPos minimumOffset;
        private int radius;

        private ArrayList<BlockPos> blockPosCache;
        private BlockPos cachedPosition;

        private boolean cache = true;

        public HemiSphere(BlockPos minimumOffset, int radius) {
            setRadius(minimumOffset, radius);
        }

        public HemiSphere(AreaDescriptor.HemiSphere hemiSphere) {
            this(hemiSphere.minimumOffset, hemiSphere.radius);
        }

        public AreaDescriptor.HemiSphere copy() {
            return new AreaDescriptor.HemiSphere(this);
        }

        public void setRadius(BlockPos minimumOffset, int radius) {
            this.minimumOffset = new BlockPos(Math.min(minimumOffset.getX(), minimumOffset.getX()), Math.min(minimumOffset.getY(), minimumOffset.getY()), Math.min(minimumOffset.getZ(), minimumOffset.getZ()));
            this.radius = radius;
            blockPosCache = new ArrayList<>();
        }


        @Override
        public int getHeight() {
            return this.radius * 2;
        }


        @Override
        public List<BlockPos> getContainedPositions(BlockPos pos) {
            if (!cache || !pos.equals(cachedPosition) || blockPosCache.isEmpty()) {
                ArrayList<BlockPos> posList = new ArrayList<>();

                int i = -radius;
                int j = minimumOffset.getY();
                int k = -radius;

                //TODO For some reason the bottom of the hemisphere is not going up with the minOffset

                while (i <= radius) {
                    while (j <= radius) {
                        while (k <= radius) {
                            if (i * i + j * j + k * k >= (radius + 0.5F) * (radius + 0.5F)) {
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
        public AxisAlignedBB getAABB(BlockPos pos) {
            return null;
        }

        @Override
        public void resetCache() {
            this.blockPosCache = new ArrayList<>();
        }

        @Override
        public boolean isWithinArea(BlockPos pos) {
            return blockPosCache.contains(pos);
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public BlockPos next() {
            return null;
        }

        @Override
        public void remove() {

        }

        @Override
        public void resetIterator() {

        }

        @Override
        public void modifyAreaByBlockPositions(BlockPos pos1, BlockPos pos2) {

        }

        @Override
        public int getVolumeForOffsets(BlockPos pos1, BlockPos pos2) {
            return 0;
        }

        @Override
        public boolean isWithinRange(BlockPos offset1, BlockPos offset2, int verticalLimit, int horizontalLimit) {
            return false;
        }

        @Override
        public int getVolume() {
            return 0;
        }

        @Override
        public boolean isWithinRange(int verticalLimit, int horizontalLimit) {
            return false;
        }

        @Override
        public boolean intersects(AreaDescriptor descriptor) {
            return false;
        }

        @Override
        public AreaDescriptor offset(BlockPos offset) {
            return new AreaDescriptor.HemiSphere(minimumOffset.add(offset), radius);
        }

        @Override
        public AreaDescriptor rotateDescriptor(PlacementSettings settings) {
            return this;
        }
    }

    public static class Cross extends AreaDescriptor {

        private ArrayList<BlockPos> blockPosCache;
        private BlockPos cachedPosition;

        private BlockPos centerPos;
        private int size;

        private boolean cache = true;

        public Cross(BlockPos center, int size) {
            this.centerPos = center;
            this.size = size;
            this.blockPosCache = new ArrayList<>();
        }

        public Cross(AreaDescriptor.Cross cross) {
            this(cross.centerPos, cross.size);
        }

        public AreaDescriptor.Cross copy() {
            return new AreaDescriptor.Cross(this);
        }

        @Override
        public int getHeight() {
            return this.size * 2 + 1;
        }


        @Override
        public List<BlockPos> getContainedPositions(BlockPos pos) {
            if (!cache || !pos.equals(cachedPosition) || blockPosCache.isEmpty()) {
                resetCache();

                blockPosCache.add(centerPos.add(pos));
                for (int i = 1; i <= size; i++) {
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
        public void resetCache() {
            blockPosCache = new ArrayList<>();
        }

        @Override
        public boolean isWithinArea(BlockPos pos) {
            return blockPosCache.contains(pos);
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public BlockPos next() {
            return null;
        }

        @Override
        public void remove() {

        }

        @Override
        public void resetIterator() {

        }

        @Override
        public void modifyAreaByBlockPositions(BlockPos pos1, BlockPos pos2) {

        }

        @Override
        public int getVolumeForOffsets(BlockPos pos1, BlockPos pos2) {
            return 0;
        }

        @Override
        public boolean isWithinRange(BlockPos offset1, BlockPos offset2, int verticalLimit, int horizontalLimit) {
            return false;
        }

        @Override
        public int getVolume() {
            return 0;
        }

        @Override
        public boolean isWithinRange(int verticalLimit, int horizontalLimit) {
            return false;
        }

        @Override
        public boolean intersects(AreaDescriptor descriptor) {
            return false;
        }

        @Override
        public AreaDescriptor offset(BlockPos offset) {
            return new AreaDescriptor.Cross(centerPos.add(offset), size);
        }

        @Override
        public AreaDescriptor rotateDescriptor(PlacementSettings settings) {
            return this;
        }
    }
}
