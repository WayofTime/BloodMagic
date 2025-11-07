package wayoftime.bloodmagic.common.routing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IRoutingFilter<T> {

    void initializeFilter(List<IResourceMatcher<T>> matchList, BlockPos target, Direction interactionSide, boolean isOutput);

    ResourceLocation getTransferType();

    T transferThroughOutputFilter(T resource);

    int transferThroughInputFilter(IRoutingFilter<T> outputFilter, int maxTransfer);
}
