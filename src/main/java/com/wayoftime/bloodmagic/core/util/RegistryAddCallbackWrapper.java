package com.wayoftime.bloodmagic.core.util;

import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public final class RegistryAddCallbackWrapper<T extends IForgeRegistryEntry<T>> {

    private static final Field CALLBACK_FIELD;

    static {
        try {
            CALLBACK_FIELD = ForgeRegistry.class.getDeclaredField("add");
            CALLBACK_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Could not find AddCallback field.");
        }
    }

    private final Class<T> registryType;
    private final BiConsumer<T, T> addCheck;
    private final Runnable postCallback;

    public RegistryAddCallbackWrapper(@Nonnull Class<T> registryType, @Nonnull BiConsumer<T, T> addCheck, @Nonnull Runnable postCallback) {
        this.registryType = registryType;
        this.addCheck = addCheck;
        this.postCallback = postCallback;
    }

    public void wrapParent() throws Exception {
        ForgeRegistry<T> registry = (ForgeRegistry<T>) GameRegistry.findRegistry(registryType);
        //noinspection unchecked
        IForgeRegistry.AddCallback<T> oldCallback = (IForgeRegistry.AddCallback<T>) CALLBACK_FIELD.get(registry);
        EnumHelper.setFailsafeFieldValue(CALLBACK_FIELD, registry, (IForgeRegistry.AddCallback<T>) (owner, stage, id, obj, oldObj) -> {
            oldCallback.onAdd(owner, stage, id, obj, oldObj);
            addCheck.accept(obj, oldObj);
        });
        BMLog.DEBUG.info("Wrapped add callback for {} registry.", registry.getRegistrySuperType().getSimpleName());
    }

    @Nonnull
    public Runnable getPostCallback() {
        return postCallback;
    }
}
