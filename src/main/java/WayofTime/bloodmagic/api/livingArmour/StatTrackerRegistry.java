package WayofTime.bloodmagic.api.livingArmour;

import java.util.ArrayList;
import java.util.List;

public class StatTrackerRegistry {
	public static List<Class<? extends StatTracker>> trackers = new ArrayList();

	public static void registerStatTracker(Class<? extends StatTracker> tracker) {
		trackers.add(tracker);
	}
}
