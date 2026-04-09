package wayoftime.bloodmagic.datagen.builder;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import wayoftime.bloodmagic.api.ritual.RitualStructure;

import java.util.ArrayList;
import java.util.List;

public class RitualStructureBuilder {
    private List<Pair<BlockPos, Block>> list = new ArrayList<>();

    public RitualStructure build() {
        return new RitualStructure(list);
    }

    public final void addRune(int x, int y, int z, Block rune)
    {
        list.add(new Pair<>(new BlockPos(x, y, z), rune));
    }

    public RitualStructureBuilder addOffsetRunes(int offset1, int offset2, int y, Block rune)
    {
        addRune(offset1, y, offset2, rune);
        addRune(offset1, y, -offset2, rune);
        addRune(offset2, y, offset1, rune);
        addRune(offset2, y, -offset1, rune);
        addRune(-offset1, y, offset2, rune);
        addRune(-offset1, y, -offset2, rune);
        addRune(-offset2, y, offset1, rune);
        addRune(-offset2, y, -offset1, rune);

        return this;
    }

    public RitualStructureBuilder addCornerRunes(int offset, int y, Block rune)
    {
        addRune(offset, y, offset, rune);
        addRune(offset, y, -offset, rune);
        addRune(-offset, y, -offset, rune);
        addRune(-offset, y, offset, rune);

        return this;
    }

    public RitualStructureBuilder addParallelRunes(int offset, int y, Block rune)
    {
        addRune(offset, y, 0, rune);
        addRune(-offset, y, 0, rune);
        addRune(0, y, -offset, rune);
        addRune(0, y, offset, rune);

        return this;
    }
}
