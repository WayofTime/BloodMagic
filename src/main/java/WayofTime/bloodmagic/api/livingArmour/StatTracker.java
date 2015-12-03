package WayofTime.bloodmagic.api.livingArmour;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.livingArmour.LivingArmour;

public abstract class StatTracker {

	private boolean isDirty = false;

	public abstract String getUniqueIdentifier();

	public abstract void resetTracker();

	public abstract void readFromNBT(NBTTagCompound tag);

	public abstract void writeToNBT(NBTTagCompound tag);

	public abstract void onTick(World world, EntityPlayer player, LivingArmour livingArmour);

	public final boolean isDirty() {
		return isDirty;
	}

	public final void markDirty() {
		this.isDirty = true;
	}

	public final void resetDirty() {
		this.isDirty = false;
	}
}
