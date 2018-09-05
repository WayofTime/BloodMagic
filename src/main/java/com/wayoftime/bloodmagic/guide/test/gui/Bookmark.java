package com.wayoftime.bloodmagic.guide.test.gui;

import com.wayoftime.bloodmagic.guide.Category;
import com.wayoftime.bloodmagic.guide.Entry;

public class Bookmark {

    private Category category;
    private Entry entry;

    public Bookmark atPosition(Category category, Entry entry) {
        this.category = category;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Entry getEntry() {
        return entry;
    }
}
