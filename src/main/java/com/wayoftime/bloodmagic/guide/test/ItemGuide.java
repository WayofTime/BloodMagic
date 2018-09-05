package com.wayoftime.bloodmagic.guide.test;

import com.wayoftime.bloodmagic.guide.Guide;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemGuide extends Item {

    public ItemGuide(Guide guide) {
        setRegistryName(guide.getId().toString().replace(":", "_"));
        setTranslationKey(guide.getId().toString());
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.MISC);
    }
}
