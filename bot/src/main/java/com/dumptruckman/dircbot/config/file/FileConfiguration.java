package com.dumptruckman.dircbot.config.file;

import com.dumptruckman.dircbot.config.Configuration;
import com.dumptruckman.dircbot.config.ConfigurationSection;
import com.dumptruckman.dircbot.config.InvalidConfigurationException;
import com.dumptruckman.dircbot.config.MemoryConfiguration;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import pluginbase.config.ConfigSerializer;
import pluginbase.config.SerializationRegistrar;
import pluginbase.config.field.FieldMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a base class for all File based implementations of {@link
 * Configuration}
 */
public abstract class FileConfiguration extends MemoryConfiguration {
    /**
     * This value specified that the system default encoding should be
     * completely ignored, as it cannot handle the ASCII character set, or it
     * is a strict-subset of UTF8 already (plain ASCII).
     *
     * @deprecated temporary compatibility measure
     */
    @Deprecated
    public static final boolean UTF8_OVERRIDE;
    /**
     * This value specifies if the system default encoding is unicode, but
     * cannot parse standard ASCII.
     *
     * @deprecated temporary compatibility measure
     */
    @Deprecated
    public static final boolean UTF_BIG;
    /**
     * This value specifies if the system supports unicode.
     *
     * @deprecated temporary compatibility measure
     */
    @Deprecated
    public static final boolean SYSTEM_UTF;
    static {
        final byte[] testBytes = Base64Coder.decode("ICEiIyQlJicoKSorLC0uLzAxMjM0NTY3ODk6Ozw9Pj9AQUJDREVGR0hJSktMTU5PUFFSU1RVVldYWVpbXF1eX2BhYmNkZWZnaGlqa2xtbm9wcXJzdHV2d3h5ent8fX4NCg==");
        final String testString = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\r\n";
        final Charset defaultCharset = Charset.defaultCharset();
        final String resultString = new String(testBytes, defaultCharset);
        final boolean trueUTF = defaultCharset.name().contains("UTF");
        UTF8_OVERRIDE = !testString.equals(resultString) || defaultCharset.equals(Charset.forName("US-ASCII"));
        SYSTEM_UTF = trueUTF || UTF8_OVERRIDE;
        UTF_BIG = trueUTF && UTF8_OVERRIDE;
    }

    /**
     * Creates an empty {@link FileConfiguration} with no default values.
     */
    public FileConfiguration() {
        super();
    }

    /**
     * Creates an empty {@link FileConfiguration} using the specified {@link
     * Configuration} as a source for all default values.
     *
     * @param defaults Default value provider
     */
    public FileConfiguration(Configuration defaults) {
        super(defaults);
    }

    /**
     * Saves this {@link FileConfiguration} to the specified location.
     * <p>
     * If the file does not exist, it will be created. If already exists, it
     * will be overwritten. If it cannot be overwritten or created, an
     * exception will be thrown.
     * <p>
     * This method will save using the system default encoding, or possibly
     * using UTF8.
     *
     * @param file File to save to.
     * @throws IOException Thrown when the given file cannot be written to for
     *     any reason.
     * @throws IllegalArgumentException Thrown when file is null.
     */
    public void save(@NotNull File file) throws IOException {
        Files.createParentDirs(file);

        String data = saveToString();

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), UTF8_OVERRIDE && !UTF_BIG ? Charsets.UTF_8 : Charset.defaultCharset())) {
            writer.write(data);
        }
    }

    /**
     * Saves this {@link FileConfiguration} to the specified location.
     * <p>
     * If the file does not exist, it will be created. If already exists, it
     * will be overwritten. If it cannot be overwritten or created, an
     * exception will be thrown.
     * <p>
     * This method will save using the system default encoding, or possibly
     * using UTF8.
     *
     * @param file File to save to.
     * @throws IOException Thrown when the given file cannot be written to for
     *     any reason.
     * @throws IllegalArgumentException Thrown when file is null.
     */
    public void save(@NotNull String file) throws IOException {
        save(new File(file));
    }

    /**
     * Saves this {@link FileConfiguration} to a string, and returns it.
     *
     * @return String containing this configuration.
     */
    public abstract String saveToString();

    /**
     * Loads this {@link FileConfiguration} from the specified location.
     * <p>
     * All the values contained within this configuration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given file.
     * <p>
     * If the file cannot be loaded for any reason, an exception will be
     * thrown.
     * <p>
     * This will attempt to use the {@link Charset#defaultCharset()} for
     * files, unless {@link #UTF8_OVERRIDE} but not {@link #UTF_BIG} is
     * specified.
     *
     * @param file File to load from.
     * @throws FileNotFoundException Thrown when the given file cannot be
     *     opened.
     * @throws IOException Thrown when the given file cannot be read.
     * @throws InvalidConfigurationException Thrown when the given file is not
     *     a valid Configuration.
     * @throws IllegalArgumentException Thrown when file is null.
     */
    public void load(@NotNull File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        final FileInputStream stream = new FileInputStream(file);

        load(new InputStreamReader(stream, UTF8_OVERRIDE && !UTF_BIG ? Charsets.UTF_8 : Charset.defaultCharset()));
    }

    /**
     * Loads this {@link FileConfiguration} from the specified stream.
     * <p>
     * All the values contained within this configuration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given stream.
     * <p>
     * This will attempt to use the {@link Charset#defaultCharset()}, unless
     * {@link #UTF8_OVERRIDE} or {@link #UTF_BIG} is specified.
     *
     * @param stream Stream to load from
     * @throws IOException Thrown when the given file cannot be read.
     * @throws InvalidConfigurationException Thrown when the given file is not
     *     a valid Configuration.
     * @throws IllegalArgumentException Thrown when stream is null.
     * @deprecated This does not consider encoding
     * @see #load(Reader)
     */
    @Deprecated
    public void load(@NotNull InputStream stream) throws IOException, InvalidConfigurationException {
        load(new InputStreamReader(stream, UTF8_OVERRIDE ? Charsets.UTF_8 : Charset.defaultCharset()));
    }

    /**
     * Loads this {@link FileConfiguration} from the specified reader.
     * <p>
     * All the values contained within this configuration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given stream.
     *
     * @param reader the reader to load from
     * @throws IOException thrown when underlying reader throws an IOException
     * @throws InvalidConfigurationException thrown when the reader does not
     *      represent a valid Configuration
     * @throws IllegalArgumentException thrown when reader is null
     */
    public void load(@NotNull Reader reader) throws IOException, InvalidConfigurationException {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader input = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader)) {
            String line;

            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        }

        loadFromString(builder.toString());
    }

    /**
     * Loads this {@link FileConfiguration} from the specified location.
     * <p>
     * All the values contained within this configuration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given file.
     * <p>
     * If the file cannot be loaded for any reason, an exception will be
     * thrown.
     *
     * @param file File to load from.
     * @throws FileNotFoundException Thrown when the given file cannot be
     *     opened.
     * @throws IOException Thrown when the given file cannot be read.
     * @throws InvalidConfigurationException Thrown when the given file is not
     *     a valid Configuration.
     * @throws IllegalArgumentException Thrown when file is null.
     */
    public void load(@NotNull String file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        load(new File(file));
    }

    /**
     * Loads this {@link FileConfiguration} from the specified string, as
     * opposed to from file.
     * <p>
     * All the values contained within this configuration will be removed,
     * leaving only settings and defaults, and the new values will be loaded
     * from the given string.
     * <p>
     * If the string is invalid in any way, an exception will be thrown.
     *
     * @param contents Contents of a Configuration to load.
     * @throws InvalidConfigurationException Thrown if the specified string is
     *     invalid.
     * @throws IllegalArgumentException Thrown if contents is null.
     */
    public abstract void loadFromString(String contents) throws InvalidConfigurationException;

    /**
     * Compiles the header for this {@link FileConfiguration} and returns the
     * result.
     * <p>
     * This will use the header from {@link #options()} -> {@link
     * FileConfigurationOptions#header()}
     *
     * @return Compiled header
     */
    protected abstract String buildHeader();

    @Override
    public FileConfigurationOptions options() {
        if (options == null) {
            options = new FileConfigurationOptions(this);
        }

        return (FileConfigurationOptions) options;
    }

    /**
     * Retrieves the object data at the specified path and attempts to insert it into an object of the given type, if
     * possible.
     * <p/>
     * If the specified path contains an object of the given type, the object at the specified path will be returned.
     * <p/>
     * If the specified path contains valid data for filling an object of the given type, this will be done and the data
     * at the specified path will be replaced by the returned object.
     *
     * @param path The path to retrieve data from.
     * @param clazz The type of object expected from the given data.
     * @param <T> The object's type.
     * @return An object from the specified path or made from data at the specified path.  If there is nothing at the
     * specified path or the data cannot be parsed into the correct type of object, null will be returned.
     */

    @Nullable
    public <T> T getAs(@NotNull String path, @NotNull Class<T> clazz) {
        Object o = get(path);
        if (o != null && o.getClass().equals(clazz)) {
            return (T) o;
        }
        if (o instanceof ConfigurationSection) {
            o = convertSectionToMap((ConfigurationSection) o);
        }
        if (o instanceof List) {
            o = convertList((List) o);
        }
        if (o != null && SerializationRegistrar.isClassRegistered(clazz)) {
            try {
                T object = (T) ConfigSerializer.deserializeAs(o, clazz);
                set(path, object);
                return object;
            } catch (Exception e) {
                e.printStackTrace(); // TODO remove
                return null;
            }
        } else if (clazz.isInstance(o)) {
            return (T) o;
        } else {
            return null;
        }
    }

    @Nullable
    public <T> T getToObject(@NotNull String path, @NotNull T destination) {
        T source = getAs(path, (Class<T>) destination.getClass());
        if (destination.equals(source)) {
            return destination;
        }
        if (source != null) {
            destination = FieldMapper.mapFields(source, destination);
            set(path, destination);
            return destination;
        } else {
            return null;
        }
    }

    private Map convertSectionToMap(@NotNull ConfigurationSection section) {
        Map<String, Object> values = section.getValues(false);
        Map<String, Object> result = new LinkedHashMap<String, Object>(values.size());
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            int lastSeparatorIndex = key.lastIndexOf(options().pathSeparator());
            if (lastSeparatorIndex >= 0) {
                key = key.substring(lastSeparatorIndex + 1, key.length());
            }
            if (value instanceof ConfigurationSection) {
                value = convertSectionToMap((ConfigurationSection) value);
            }
            result.put(key, value);
        }
        return result;
    }

    private List convertList(@NotNull List list) {
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (o instanceof ConfigurationSection) {
                list.set(i, convertSectionToMap((ConfigurationSection) o));
            }
        }
        return list;
    }

    public static class FileConfigurationOptions extends MemoryConfigurationOptions {
        private String header = null;
        private boolean copyHeader = true;

        protected FileConfigurationOptions(MemoryConfiguration configuration) {
            super(configuration);
        }

        @Override
        public FileConfiguration configuration() {
            return (FileConfiguration) super.configuration();
        }

        @Override
        public FileConfigurationOptions pathSeparator(char value) {
            super.pathSeparator(value);
            return this;
        }

        /**
         * Gets the header that will be applied to the top of the saved output.
         * <p>
         * This header will be commented out and applied directly at the top of
         * the generated output of the {@link FileConfiguration}. It is not
         * required to include a newline at the end of the header as it will
         * automatically be applied, but you may include one if you wish for extra
         * spacing.
         * <p>
         * Null is a valid value which will indicate that no header is to be
         * applied. The default value is null.
         *
         * @return Header
         */
        public String header() {
            return header;
        }

        /**
         * Sets the header that will be applied to the top of the saved output.
         * <p>
         * This header will be commented out and applied directly at the top of
         * the generated output of the {@link FileConfiguration}. It is not
         * required to include a newline at the end of the header as it will
         * automatically be applied, but you may include one if you wish for extra
         * spacing.
         * <p>
         * Null is a valid value which will indicate that no header is to be
         * applied.
         *
         * @param value New header
         * @return This object, for chaining
         */
        public FileConfigurationOptions header(String value) {
            this.header = value;
            return this;
        }
    }
}
