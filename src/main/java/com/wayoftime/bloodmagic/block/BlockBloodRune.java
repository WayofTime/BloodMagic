package com.wayoftime.bloodmagic.block;

import com.wayoftime.bloodmagic.core.type.BloodRunes;
import net.minecraft.block.material.Material;

public class BlockBloodRune extends BlockMundane {

    private final BloodRunes rune;

    public BlockBloodRune(BloodRunes rune) {
        super(Material.ROCK, "blood_rune_" + rune.getName(), true);

        this.rune = rune;
    }

    public BloodRunes getRune() {
        return rune;
    }
}
