package com.wayoftime.bloodmagic.core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * A simple utility for reading and writing JSON files. To handle custom (de)serialization, use
 * {@link com.google.gson.annotations.JsonAdapter} on your types.
 */
public class JsonUtil {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();

    /**
     * Reads a {@link T} back from the given file. If the file does not exist, a new file will be generated with the
     * provided default and the default will be returned.
     *
     * @param token The token type to use for deserialization.
     * @param file  The file to read the JSON from.
     * @param def   The default value to use if the file does not exist.
     * @param <T>   The object type to give back.
     * @return a {@link T} that was read from the given file or {@code def} if the file did not exist.
     */
    @Nonnull
    public static <T> T fromJson(@Nonnull TypeToken<T> token, @Nonnull File file, @Nonnull T def) {
        T ret = fromJson(token, file);
        if (ret == null) {
            toJson(def, token, file);
            ret = def;
        }

        return ret;
    }

    /**
     * Reads a {@link T} back from the given file. If the file does not exist, {@code null} is returned. If an exception
     * is thrown during deserialization, {@code null} is also returned.
     *
     * @param token The token type to use for deserialization.
     * @param file  - The file to read the JSON from.
     * @param <T>   The object type to give back.
     * @return a {@link T} that was read from the given file, {@code null} if the file does not exist, or {@code null} if
     * an exception was thrown.
     */
    @Nullable
    public static <T> T fromJson(@Nonnull TypeToken<T> token, @Nonnull File file) {
        if (!file.exists())
            return null;

        try (FileReader reader = new FileReader(file)) {
            return GSON.fromJson(reader, token.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T fromJson(@Nonnull TypeToken<T> token, @Nonnull String json) {
        return GSON.fromJson(json, token.getType());
    }

    /**
     * Converts a {@link T} to JSON and writes it to file. If the file does not exist, a new one is created. If the file
     * does exist, the contents are overwritten with the new value.
     *
     * @param type  The object to write to JSON.
     * @param token The token type to use for serialization.
     * @param file  The file to write the JSON to.
     * @param <T>   The object type to write.
     */
    public static <T> void toJson(@Nonnull T type, @Nonnull TypeToken<T> token, @Nonnull File file) {
        if (!file.exists()) {
            try {
                FileUtils.forceMkdirParent(file);
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(getJson(type, token));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> String getJson(@Nonnull T type, @Nonnull TypeToken<T> token) {
        return GSON.toJson(type, token.getType());
    }
}

