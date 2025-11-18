package wayoftime.bloodmagic.client.button;

import net.minecraft.client.gui.components.Button;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.item.routing.IItemFilterProvider;
import wayoftime.bloodmagic.common.item.routing.ItemRouterFilter;
import wayoftime.bloodmagic.network.BloodMagicPacketHandler;
import wayoftime.bloodmagic.network.FilterButtonPacket;

public class FilterButtonTogglePress implements Button.OnPress
{
	private final String buttonKey;
	private final ContainerFilter container;

	public FilterButtonTogglePress(String key, ContainerFilter container)
	{
		this.buttonKey = key;
		this.container = container;
	}

	@Override
	public void onPress(Button button)
	{
        // handled in container now
	}
}