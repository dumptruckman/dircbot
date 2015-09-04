package com.dumptruckman.dircbot.config;

import java.util.Map;

/**
 * This is a {@link Configuration} implementation that does not save or load
 * from any source, and stores all values in memory only.
 * This is useful for temporary Configurations for providing defaults.
 */
public class MemoryConfiguration extends MemorySection implements Configuration {
    protected Configuration defaults;
    protected MemoryConfigurationOptions options;

    /**
     * Creates an empty {@link MemoryConfiguration} with no default values.
     */
    public MemoryConfiguration() {}

    /**
     * Creates an empty {@link MemoryConfiguration} using the specified {@link
     * Configuration} as a source for all default values.
     *
     * @param defaults Default value provider
     * @throws IllegalArgumentException Thrown if defaults is null
     */
    public MemoryConfiguration(Configuration defaults) {
        this.defaults = defaults;
    }


    @Override
    public ConfigurationSection getParent() {
        return null;
    }

    public MemoryConfigurationOptions options() {
        if (options == null) {
            options = new MemoryConfigurationOptions(this);
        }

        return options;
    }

    /**
     * Various settings for controlling the input and output of a {@link
     * MemoryConfiguration}
     */
    public static class MemoryConfigurationOptions extends ConfigurationOptions {

        protected MemoryConfigurationOptions(MemoryConfiguration configuration) {
            super(configuration);
        }

        @Override
        public MemoryConfiguration configuration() {
            return (MemoryConfiguration) super.configuration();
        }

        @Override
        public MemoryConfigurationOptions pathSeparator(char value) {
            super.pathSeparator(value);
            return this;
        }
    }
}
