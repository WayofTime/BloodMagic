package wayoftime.bloodmagic.client.button;

import net.minecraft.client.gui.components.Button;
import wayoftime.bloodmagic.common.container.item.ContainerFilter;
import wayoftime.bloodmagic.common.item.routing.IRoutingFilterProvider;
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
		if (button.active)
		{
			int currentGhostSlot = container.lastGhostSlotClicked;
			if (container.filterStack.getItem() instanceof IRoutingFilterProvider)
			{
				int currentButtonState = ((IRoutingFilterProvider) container.filterStack.getItem()).getCurrentButtonState(container.filterStack, buttonKey, currentGhostSlot);

				BloodMagicPacketHandler.INSTANCE.sendToServer(new FilterButtonPacket(container.player.getInventory().selected, currentGhostSlot, buttonKey, currentButtonState));

				((IRoutingFilterProvider) container.filterStack.getItem()).receiveButtonPress(container.filterStack, buttonKey, currentGhostSlot, currentButtonState);
			}
		}
	}
}