package com.wayoftime.bloodmagic.event;

import com.wayoftime.bloodmagic.core.living.LivingStats;
import com.wayoftime.bloodmagic.core.living.LivingUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LivingEquipmentEvent extends Event {

    private final EntityPlayer player;
    private final LivingStats stats;

    public LivingEquipmentEvent(EntityPlayer player, LivingStats stats) {
        this.player = player;
        this.stats = stats;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public LivingStats getStats() {
        return stats;
    }

    @Cancelable
    public static class GainExperience extends LivingEquipmentEvent {

        private final LivingUpgrade upgrade;
        private int experience;

        public GainExperience(EntityPlayer player, LivingStats stats, LivingUpgrade upgrade, int experience) {
            super(player, stats);
            this.upgrade = upgrade;
            this.experience = experience;
        }

        public LivingUpgrade getUpgrade() {
            return upgrade;
        }

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }
    }

    public static class LevelUp extends LivingEquipmentEvent {

        private final LivingUpgrade upgrade;

        public LevelUp(EntityPlayer player, LivingStats stats, LivingUpgrade upgrade) {
            super(player, stats);

            this.upgrade = upgrade;
        }

        public LivingUpgrade getUpgrade() {
            return upgrade;
        }
    }
}
