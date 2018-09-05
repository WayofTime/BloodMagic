package com.wayoftime.bloodmagic.guide;

import com.google.common.collect.Lists;
import com.wayoftime.bloodmagic.guide.page.PageComponent;

import java.util.List;
import java.util.function.Consumer;

public class Entry {

    private final String id;
    private final List<PageComponent> components;

    public Entry(String id, Consumer<Entry> $) {
        this.id = id;
        this.components = Lists.newArrayList();

        $.accept(this);
    }

    public PageComponent appendComponent(PageComponent component) {
        components.add(component);
        return component;
    }

    public String getId() {
        return id;
    }
}
