package dev.lightdream.unchartedcore.managers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dev.lightdream.unchartedcore.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
public class FileManager {

    private final ObjectMapper objectMapper;
    private final PersistType persistType;
    private final Main plugin;

    public FileManager(Main plugin, PersistType persistType) {
        this.plugin = plugin;
        this.persistType = persistType;
        this.objectMapper = new ObjectMapper(persistType.getFactory());
    }

    private static String getName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    public static String getName(Object instance) {
        return getName(instance.getClass());
    }

    public static String getName(Type type) {
        return getName(type.getClass());
    }

    public File getFile(String name) {
        return new File(plugin.getDataFolder(), name + persistType.getExtension());
    }

    public File getFile(Class<?> clazz) {
        return getFile(getName(clazz));
    }

    public File getFile(Object instance) {
        return getFile(getName(instance));
    }

    public void save(Object instance) {
        save(instance, getFile(instance));
    }

    private void save(Object instance, File file) {
        StringBuilder newPath = new StringBuilder();
        List<String> subPaths = Arrays.asList(file.getPath().split("/"));
        for (int i = 0; i < subPaths.size() - 1; i++) {
            newPath.append(subPaths.get(i));
            newPath.append("/");
        }
        File newFile = new File(newPath.toString());
        newFile.mkdirs();
        try {
            objectMapper.writeValue(file, instance);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save " + file + ": " + e.getMessage());
        }
    }

    public String toString(Object instance) {
        try {
            return objectMapper.writeValueAsString(instance);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save " + instance.toString() + ": " + e.getMessage());
        }
        return "";
    }

    public <T> T load(Class<T> clazz) {
        return load(clazz, getFile(clazz));
    }

    public <T> T load(Class<T> clazz, File file) {
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, clazz);
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to parse " + file + ": " + e.getMessage());
            }
        }
        try {
            save(clazz.newInstance());
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T load(Class<T> clazz, String content) {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (IOException ignored) {
        }

        return null;
    }

    private void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[8192];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

    }

    private InputStream getFileFromResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    public void saveDefault(String subfolder, String fileName) throws IOException {
        new File(plugin.getDataFolder() + "/" + subfolder).mkdirs();
        copyInputStreamToFile(getFileFromResource(fileName), new File(plugin.getDataFolder() + "/" + subfolder + "/" + fileName));
    }

    public enum PersistType {

        YAML(".yml", new YAMLFactory()),
        JSON(".json", new JsonFactory());

        private final String extension;
        private final JsonFactory factory;

        PersistType(String extension, JsonFactory factory) {
            this.extension = extension;
            this.factory = factory;
        }

        public String getExtension() {
            return extension;
        }

        public JsonFactory getFactory() {
            return factory;
        }
    }

}
