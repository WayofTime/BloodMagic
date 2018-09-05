package com.wayoftime.bloodmagic.core.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.wayoftime.bloodmagic.core.network.BloodOrb;

import java.util.List;

public class OrbUtil {

    public static final List<BloodOrb> ORBS = Lists.newArrayList();
    public static final ArrayListMultimap<Integer, BloodOrb> TIERED = ArrayListMultimap.create();

    public static void addOrb(BloodOrb orb) {
        ORBS.add(orb);

        TIERED.put(orb.getTier(), orb);
    }

    // TODO :LUL: This needs to be implemented before orb-related recipes can be added.
    public static List<BloodOrb> getOrbsMatching(int tier) {
        return Lists.newArrayList();
    }
}
