package wayoftime.bloodmagic.common.item.inventory;

import net.minecraft.world.inventory.ContainerData;

import java.util.ArrayList;
import java.util.List;

public abstract class DataFilter implements ContainerData {

    public final List<Integer> content;

    public DataFilter(List<Integer> buttonStates) {
        // copy so neither this nor the caller do weird stuff for whatever reason
        this.content = new ArrayList<>(buttonStates);
    }

    @Override
    public int get(int index) {
        return content.get(index);
    }

    @Override
    public void set(int index, int value) {
        this.content.set(index, value);
        save(index);
    }

    public abstract void save(int index);

    @Override
    public int getCount() {
        return content.size();
    }
}
