package com.dumptruckman.dircbot.config;

/**
 * Represents a source of configurable options and settings
 */
public interface Configuration extends ConfigurationSection {

    /**
     * Gets the {@link ConfigurationOptions} for this {@link Configuration}.
     * <p>
     * All setters through this method are chainable.
     *
     * @return Options for this configuration
     */
    ConfigurationOptions options();

    /**
     * Various settings for controlling the input and output of a {@link
     * Configuration}
     */
    class ConfigurationOptions {

        private char pathSeparator = '.';
        private final Configuration configuration;

        protected ConfigurationOptions(Configuration configuration) {
            this.configuration = configuration;
        }

        /**
         * Returns the {@link Configuration} that this object is responsible for.
         *
         * @return Parent configuration
         */
        public Configuration configuration() {
            return configuration;
        }

        /**
         * Gets the char that will be used to separate {@link
         * ConfigurationSection}s
         * <p>
         * This value does not affect how the {@link Configuration} is stored,
         * only in how you access the data. The default value is '.'.
         *
         * @return Path separator
         */
        public char pathSeparator() {
            return pathSeparator;
        }

        /**
         * Sets the char that will be used to separate {@link
         * ConfigurationSection}s
         * <p>
         * This value does not affect how the {@link Configuration} is stored,
         * only in how you access the data. The default value is '.'.
         *
         * @param value Path separator
         * @return This object, for chaining
         */
        public ConfigurationOptions pathSeparator(char value) {
            this.pathSeparator = value;
            return this;
        }
    }
}
