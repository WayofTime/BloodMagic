package WayofTime.alchemicalWizardry.api;

import net.minecraft.util.EnumFacing;

public class RoutingFocusPosAndFacing 
{
	public Int3 location;
	public EnumFacing facing;
	
	public RoutingFocusPosAndFacing(Int3 location, EnumFacing facing)
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
