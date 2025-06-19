package wayoftime.bloodmagic.common.routing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.List;

public interface IRoutingFilter<T> {

    void initializeFilter(List<IResourceMatcher<T>> matchList, BlockPos target, Direction interactionSide, boolean isOutput);

    T transferThroughOutputFilter(T resource);

    int transferThroughInputFilter(IRoutingFilter<T> outputFilter, int maxTransfer);

    boolean doesResourcePassFilter(T resource);

    boolean doesResourceMatch(IResourceMatcher<T> matcher, T resource);

    List<IResourceMatcher<T>> getMatchList();
}
