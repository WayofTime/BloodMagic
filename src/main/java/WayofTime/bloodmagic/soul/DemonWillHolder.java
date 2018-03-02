package WayofTime.bloodmagic.soul;

import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map.Entry;

public class DemonWillHolder {
    public HashMap<EnumDemonWillType, Double> willMap = new HashMap<>();

    public double addWill(EnumDemonWillType type, double amount, double max) {
        double current = 0;
        if (willMap.containsKey(type)) {
            current = willMap.get(type);
        }

        double added = Math.min(max - current, amount);
        addWill(type, amount);

        return added;
    }

    public void addWill(EnumDemonWillType type, double amount) {
        if (willMap.containsKey(type)) {
            willMap.put(type, amount + willMap.get(type));
        } else {
            willMap.put(type, amount);
        }
    }

    public double drainWill(EnumDemonWillType type, double amount) {
        if (willMap.containsKey(type)) {
            double current = willMap.get(type);
            double reduced = Math.min(current, amount);

            if (reduced >= current) {
                willMap.remove(type);
            } else {
                willMap.put(type, current - reduced);
            }

            return reduced;
        }

        return 0;
    }

    public double getWill(EnumDemonWillType type) {
        if (willMap.containsKey(type)) {
            return willMap.get(type);
        }

        return 0;
    }

    public void readFromNBT(NBTTagCompound tag, String key) {
        NBTTagCompound willTag = tag.getCompoundTag(key);

        willMap.clear();

        for (EnumDemonWillType type : EnumDemonWillType.values()) {
            double amount = willTag.getDouble("EnumWill" + type.getName());
            if (amount > 0) {
                willMap.put(type, amount);
            }
        }
    }

    public void writeToNBT(NBTTagCompound tag, String key) {
        NBTTagCompound willTag = new NBTTagCompound();
        for (Entry<EnumDemonWillType, Double> entry : willMap.entrySet()) {
            willTag.setDouble("EnumWill" + entry.getKey().getName(), entry.getValue());
        }

        tag.setTag(key, willTag);
    }

    public void clearWill() {
        willMap.clear();
    }
}
