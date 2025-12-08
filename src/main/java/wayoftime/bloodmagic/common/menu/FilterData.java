package wayoftime.bloodmagic.common.menu;

import net.minecraft.world.inventory.ContainerData;

import java.util.ArrayList;
import java.util.List;

public class FilterData implements ContainerData {

    private final List<Integer> contents;
    public FilterData(int count) {
        List<Integer> contents = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            contents.add(0);
        }
        this.contents = contents;
    }

    public FilterData(List<Integer> contents) {
        this.contents = new ArrayList<>(contents.size());
        // just so we dont end up with a non-mutable list
        for (int i = 0; i < contents.size(); i++) {
            this.contents.add(i, contents.get(i));
        }
    }

    @Override
    public int get(int index) {
        return contents.get(index);
    }

    @Override
    public void set(int index, int value) {
        contents.set(index, value);
    }

    @Override
    public int getCount() {
        return contents.size();
    }
}
