package com.wayoftime.bloodmagic.core.util.register;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface IModelLocator {

    /**
     * Allows an item to point to a custom model path instead of using the registry name.
     *
     * @return the path to the model.
     */
    @Nonnull
    ResourceLocation getModelPath();
}
