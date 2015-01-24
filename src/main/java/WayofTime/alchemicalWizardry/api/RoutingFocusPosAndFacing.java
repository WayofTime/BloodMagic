package WayofTime.alchemicalWizardry.api;

import net.minecraftforge.common.util.ForgeDirection;

public class RoutingFocusPosAndFacing 
{
	public Int3 location;
	public ForgeDirection facing;
	
	public RoutingFocusPosAndFacing(Int3 location, ForgeDirection facing)
	{
		this.location = location;
		this.facing = facing;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof RoutingFocusPosAndFacing ? facing.equals(((RoutingFocusPosAndFacing)obj).facing) && location.equals(((RoutingFocusPosAndFacing)obj).location) : false;
	}
}
