package com.dumptruckman.dircbot.config.file;

import com.dumptruckman.dircbot.Bot;
import com.dumptruckman.dircbot.config.ConfigurationSection;
import com.dumptruckman.dircbot.config.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * An implementation of {@link com.dumptruckman.dircbot.config.Configuration}
 * which saves all files in Yaml.
 * Note that this implementation is not synchronized.
 */
public class YamlConfiguration extends FileConfiguration {

    protected static final String COMMENT_PREFIX = "# ";
    protected static final String BLANK_CONFIG = "{}\n";
    private final DumperOptions yamlOptions = new DumperOptions();
    private final Representer yamlRepresenter = new YamlRepresenter();
    private final Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions);

    @Override
    public String saveToString() {
        yamlOptions.setIndent(options().indent());
        yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yamlOptions.setAllowUnicode(SYSTEM_UTF);
        yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        String header = buildHeader();
        String dump = yaml.dump(getValues(false));

        if (dump.equals(BLANK_CONFIG)) {
            dump = "";
        }

        return header + dump;
    }

    @Override
    public void loadFromString(@NotNull String contents) throws InvalidConfigurationException {
        Map<?, ?> input;
        try {
            input = (Map<?, ?>) yaml.load(contents);
        } catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
        } catch (ClassCastException e) {
            throw new InvalidConfigurationException("Top level is not a Map.");
        }

        String header = parseHeader(contents);
        if (header.length() > 0) {
            options().header(header);
        }

        if (input != null) {
            convertMapsToSections(input, this);
        }
    }

    protected void convertMapsToSections(Map<?, ?> input, ConfigurationSection section) {
        for (Map.Entry<?, ?> entry : input.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();

            if (value instanceof Map) {
                convertMapsToSections((Map<?, ?>) value, section.createSection(key));
            } else {
                section.set(key, value);
            }
        }
    }

    protected String parseHeader(String input) {
        String[] lines = input.split("\r?\n", -1);
        StringBuilder result = new StringBuilder();
        boolean readingHeader = true;
        boolean foundHeader = false;

        for (int i = 0; (i < lines.length) && (readingHeader); i++) {
            String line = lines[i];

            if (line.startsWith(COMMENT_PREFIX)) {
                if (i > 0) {
                    result.append("\n");
                }

                if (line.length() > COMMENT_PREFIX.length()) {
                    result.append(line.substring(COMMENT_PREFIX.length()));
                }

                foundHeader = true;
            } else if ((foundHeader) && (line.length() == 0)) {
                result.append("\n");
            } else if (foundHeader) {
                readingHeader = false;
            }
        }

        return result.toString();
    }

    @Override
    protected String buildHeader() {
        String header = options().header();

        if (header == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        String[] lines = header.split("\r?\n", -1);
        boolean startedHeader = false;

        for (int i = lines.length - 1; i >= 0; i--) {
            builder.insert(0, "\n");

            if ((startedHeader) || (lines[i].length() != 0)) {
                builder.insert(0, lines[i]);
                builder.insert(0, COMMENT_PREFIX);
                startedHeader = true;
            }
        }

        return builder.toString();
    }

    @Override
    public YamlConfigurationOptions options() {
        if (options == null) {
            options = new YamlConfigurationOptions(this);
        }

        return (YamlConfigurationOptions) options;
    }

    /**
     * Creates a new {@link YamlConfiguration}, loading from the given file.
     * <p>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be
     * returned.
     * <p>
     * The encoding used may follow the system dependent default.
     *
     * @param file Input file
     * @return Resulting configuration
     * @throws IllegalArgumentException Thrown if file is null
     */
    public static YamlConfiguration loadConfiguration(@NotNull File file) {
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            Bot.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        } catch (InvalidConfigurationException ex) {
            Bot.getLogger().log(Level.SEVERE, "Cannot load " + file , ex);
        }

        return config;
    }

    /**
     * Creates a new {@link YamlConfiguration}, loading from the given stream.
     * <p>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be
     * returned.
     *
     * @param stream Input stream
     * @return Resulting configuration
     * @throws IllegalArgumentException Thrown if stream is null
     * @deprecated does not properly consider encoding
     * @see #load(InputStream)
     * @see #loadConfiguration(Reader)
     */
    @Deprecated
    public static YamlConfiguration loadConfiguration(@NotNull InputStream stream) {
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(stream);
        } catch (IOException ex) {
            Bot.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        } catch (InvalidConfigurationException ex) {
            Bot.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        }

        return config;
    }


    /**
     * Creates a new {@link YamlConfiguration}, loading from the given reader.
     * <p>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be
     * returned.
     *
     * @param reader input
     * @return resulting configuration
     * @throws IllegalArgumentException Thrown if stream is null
     */
    public static YamlConfiguration loadConfiguration(@NotNull Reader reader) {
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(reader);
        } catch (IOException ex) {
            Bot.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        } catch (InvalidConfigurationException ex) {
            Bot.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        }

        return config;
    }

    public static class YamlConfigurationOptions extends FileConfigurationOptions {
        private int indent = 2;

        protected YamlConfigurationOptions(YamlConfiguration configuration) {
            super(configuration);
        }

        @Override
        public YamlConfiguration configuration() {
            return (YamlConfiguration) super.configuration();
        }

        @Override
        public YamlConfigurationOptions pathSeparator(char value) {
            super.pathSeparator(value);
            return this;
        }

        @Override
        public YamlConfigurationOptions header(String value) {
            super.header(value);
            return this;
        }

        /**
         * Gets how much spaces should be used to indent each line.
         * <p>
         * The minimum value this may be is 2, and the maximum is 9.
         *
         * @return How much to indent by
         */
        public int indent() {
            return indent;
        }

        /**
         * Sets how much spaces should be used to indent each line.
         * <p>
         * The minimum value this may be is 2, and the maximum is 9.
         *
         * @param value New indent
         * @return This object, for chaining
         */
        public YamlConfigurationOptions indent(int value) {
            if (value < 2) {
                throw new IllegalArgumentException("Indent must be at least 2 characters");
            } else if (value > 9) {
                throw new IllegalArgumentException("Indent cannot be greater than 9 characters");
            }

            this.indent = value;
            return this;
        }
    }

    public static class YamlConstructor extends SafeConstructor {
        public YamlConstructor() {
            this.yamlConstructors.put(Tag.MAP, new ConstructCustomObject());
        }

        private class ConstructCustomObject extends ConstructYamlMap {
            @Override
            public Object construct(Node node) {
                if (node.isTwoStepsConstruction()) {
                    throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
                }

                Map<?, ?> raw = (Map<?, ?>) super.construct(node);

                /*
                if (raw.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                    Map<String, Object> typed = new LinkedHashMap<String, Object>(raw.size());
                    for (Map.Entry<?, ?> entry : raw.entrySet()) {
                        typed.put(entry.getKey().toString(), entry.getValue());
                    }

                    try {
                        return ConfigurationSerialization.deserializeObject(typed);
                    } catch (IllegalArgumentException ex) {
                        throw new YAMLException("Could not deserialize object", ex);
                    }
                }
                */

                return raw;
            }

            @Override
            public void construct2ndStep(Node node, Object object) {
                throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
            }
        }
    }

    public static class YamlRepresenter extends Representer {
        public YamlRepresenter() {
            this.multiRepresenters.put(ConfigurationSection.class, new RepresentConfigurationSection());
            //this.multiRepresenters.put(ConfigurationSerializable.class, new RepresentConfigurationSerializable());
        }

        private class RepresentConfigurationSection extends RepresentMap {
            @Override
            public Node representData(Object data) {
                return super.representData(((ConfigurationSection) data).getValues(false));
            }
        }

        private class RepresentConfigurationSerializable extends RepresentMap {
            @Override
            public Node representData(Object data) {
                Map<String, Object> values = new LinkedHashMap<>();

                /*
                ConfigurationSerializable serializable = (ConfigurationSerializable) data;
                Map<String, Object> values = new LinkedHashMap<String, Object>();
                values.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(serializable.getClass()));
                values.putAll(serializable.serialize());
                */

                return super.representData(values);
            }
        }
    }
}
