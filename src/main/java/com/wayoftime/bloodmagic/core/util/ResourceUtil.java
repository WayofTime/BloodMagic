package com.wayoftime.bloodmagic.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ResourceUtil {

    @Nonnull
    public static Set<Path> gatherResources(String home, String following, Predicate<Path> predicate) {
        FileSystem fileSystem = null;
        try {
            URL url = ResourceUtil.class.getResource(home);
            if (url != null) {
                URI uri = url.toURI();
                Path path;
                if (uri.getScheme().equals("file")) {
                    path = Paths.get(ResourceUtil.class.getResource(home + "/" + following).toURI());
                } else {
                    if (!uri.getScheme().equals("jar")) {
                        BMLog.DEFAULT.error("Unsupported URI scheme {}", uri.getScheme());
                        return Collections.emptySet();
                    }

                    fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    path = fileSystem.getPath(home + "/" + following);
                }

                return Files.walk(path).filter(predicate).collect(Collectors.toSet());
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fileSystem);
        }

        return Collections.emptySet();
    }

    @Nonnull
    public static Set<Path> gatherResources(String home, String following) {
        return gatherResources(home, following, p -> true);
    }

    @Nonnull
    public static ResourceLocation addContext(ResourceLocation rl, String context) {
        return new ResourceLocation(rl.getNamespace(), context + rl.getPath());
    }

    @SideOnly(Side.CLIENT)
    public static void injectDirectoryAsResource(File resourceDir) {
        if (!resourceDir.exists() || !resourceDir.isDirectory())
            return;

        FolderResourcePack resourcePack = new FolderResourcePack(resourceDir);
        Field _defaultResourcePacks = ReflectionHelper.findField(Minecraft.class, "field_110449_ao", "defaultResourcePacks");
        try {
            _defaultResourcePacks.setAccessible(true);
            // noinspection unchecked
            List<IResourcePack> defaultResourcePacks = (List<IResourcePack>) _defaultResourcePacks.get(Minecraft.getMinecraft());
            defaultResourcePacks.add(resourcePack);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
