package com.wayoftime.bloodmagic.item;

import com.wayoftime.bloodmagic.BloodMagic;
import net.minecraft.item.Item;

public class ItemMundane extends Item {

    public ItemMundane(String name) {
        setTranslationKey(BloodMagic.MODID + ":" + name);
        setCreativeTab(BloodMagic.TAB_BM);
        setRegistryName(name);
    }
}
