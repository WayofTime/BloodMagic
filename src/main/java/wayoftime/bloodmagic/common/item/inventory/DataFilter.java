package wayoftime.bloodmagic.common.item.inventory;

import net.minecraft.world.inventory.ContainerData;

import java.util.List;

public abstract class DataFilter implements ContainerData {

    private final List<Integer> content;

    public DataFilter(List<Integer> buttonStates) {
        this.content = buttonStates;
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
