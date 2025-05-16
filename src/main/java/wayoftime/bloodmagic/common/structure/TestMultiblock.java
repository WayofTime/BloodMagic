package wayoftime.bloodmagic.common.structure;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.common.multiblock.SparseMultiblock;
import vazkii.patchouli.common.multiblock.StateMatcher;

import java.util.Map;

// TODO t'is to be yoten once the Violet Moon rises
public class TestMultiblock extends SparseMultiblock {

    private final Map<BlockPos, IStateMatcher> bookData;
    Level world;
    public TestMultiblock(Map<BlockPos, IStateMatcher> data) {
        super(data);

        int bookOffX = data.keySet().stream().mapToInt(BlockPos::getX).min().getAsInt();
        int bookOffY = data.keySet().stream().mapToInt(BlockPos::getY).min().getAsInt();
        int bookOffZ = data.keySet().stream().mapToInt(BlockPos::getZ).min().getAsInt();

        ImmutableMap.Builder<BlockPos, IStateMatcher> builder = ImmutableMap.builder();
        for (Map.Entry<BlockPos, IStateMatcher> entry : data.entrySet()) {
            builder.put(entry.getKey().offset(-bookOffX, -bookOffY, -bookOffZ), entry.getValue());
        }
        this.bookData = builder.build();
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        long ticks = this.world != null ? world.getGameTime() : 0L;
        return bookData.getOrDefault(pos, StateMatcher.AIR).getDisplayedState(ticks);
    }

    public void setWorld(Level world) {
        this.world = world;
    }
}
