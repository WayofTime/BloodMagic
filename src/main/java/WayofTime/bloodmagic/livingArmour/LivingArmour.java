package WayofTime.bloodmagic.livingArmour;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;
import WayofTime.bloodmagic.api.livingArmour.StatTrackerRegistry;

public class LivingArmour {

	public HashMap<String, StatTracker> trackerMap = new HashMap();

	public void onTick(World world, EntityPlayer player) {
		for (Entry<String, StatTracker> entry : trackerMap.entrySet()) {
			StatTracker tracker = entry.getValue();

			if (tracker == null) {
				continue;
			}

			tracker.onTick(world, player, this);
		}
	}

	public void readFromNBT(NBTTagCompound tag) {
		for (Class<? extends StatTracker> clazz : StatTrackerRegistry.trackers) {
			try {
				Constructor<?> ctor = clazz.getConstructor();
				Object obj = ctor.newInstance();
				if (!(obj instanceof StatTracker)) {
					// ?????
				}
				StatTracker tracker = (StatTracker) obj;
				String key = tracker.getUniqueIdentifier();
				NBTTagCompound trackerTag = tag.getCompoundTag(key);
				if (!trackerTag.hasNoTags()) {
					tracker.readFromNBT(trackerTag);
				}
				trackerMap.put(key, tracker);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void writeToNBT(NBTTagCompound tag, boolean forceWrite) {
		for (Entry<String, StatTracker> entry : trackerMap.entrySet()) {
			StatTracker tracker = entry.getValue();

			if (tracker == null) {
				continue;
			}

			String key = tracker.getUniqueIdentifier();

			if (forceWrite || tracker.isDirty()) {
				NBTTagCompound trackerTag = new NBTTagCompound();
				tracker.writeToNBT(trackerTag);

				tag.setTag(key, trackerTag);

				tracker.resetDirty();
			}
		}
	}

	/**
	 * Writes the LivingArmour to the NBTTag. This will only write the trackers
	 * that are dirty.
	 * 
	 * @param tag
	 */
	public void writeDirtyToNBT(NBTTagCompound tag) {
		writeToNBT(tag, false);
	}

	public void writeToNBT(NBTTagCompound tag) {
		writeToNBT(tag, true);
	}
}
