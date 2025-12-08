package wayoftime.bloodmagic.common.capability;

import net.neoforged.neoforge.capabilities.ItemCapability;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.routing.IRoutingFilter;
import wayoftime.bloodmagic.common.routing.NodeContext;

public class BMCaps {
    // TODO does it matter that its IRF instead of IRF<?> here? like both permit any (afaik) but specifying it makes IC#create complain
    public static final ItemCapability<IRoutingFilter, NodeContext> ROUTING_FILTER_PROVIDER = ItemCapability.create(
            BloodMagic.rl("routing_filter_provider"),
            IRoutingFilter.class,
            NodeContext.class
    );
}
