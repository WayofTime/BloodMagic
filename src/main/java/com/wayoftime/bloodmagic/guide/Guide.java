package com.wayoftime.bloodmagic.guide;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.function.Consumer;

public class Guide {

    private final ResourceLocation id;
    private final List<Category> categories;

    public Guide(ResourceLocation id, Consumer<Guide> $) {
        this.id = id;
        this.categories = Lists.newArrayList();

        $.accept(this);
    }

    public Category addCategory(String name, Consumer<Category> category) {
        Category cat = new Category(this, name, category);
        categories.add(cat);
        return cat;
    }

    public ResourceLocation getId() {
        return id;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
