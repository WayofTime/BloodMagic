package com.wayoftime.bloodmagic.guide.page;

import net.minecraft.util.ResourceLocation;

import javax.vecmath.Point2i;

public class PageComponentImage extends PageComponent {

    private final Sprite sprite;

    public PageComponentImage(Sprite sprite) {
        super(sprite.size.y);

        this.sprite = sprite;
    }

    public static class Sprite {
        private final ResourceLocation location;
        private final Point2i startPosition;
        private final Point2i size;

        public Sprite(ResourceLocation location, Point2i startPosition, Point2i size) {
            this.location = location;
            this.startPosition = startPosition;
            this.size = size;
        }

        public void draw(int x, int y) {
            // TODO
        }
    }
}
