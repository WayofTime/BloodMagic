package com.wayoftime.bloodmagic.guide;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.function.Consumer;

public class Category {

    private final Guide owner;
    private final String name;
    private final Map<String, Entry> entries;

    public Category(Guide owner, String name, Consumer<Category> $) {
        this.owner = owner;
        this.name = name;
        entries = Maps.newLinkedHashMap();

        $.accept(this);
    }

    @SideOnly(Side.CLIENT)
    public void draw(Minecraft minecraft, ScaledResolution resolution, FontRenderer font) {

    }

    public Entry addEntry(Entry entry) {
        entries.put(entry.getId(), entry);
        return entry;
    }
}
