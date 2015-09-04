package com.dumptruckman.dircbot.config;

import java.util.Map;
import java.util.Set;

/**
 * Represents a section of a {@link Configuration}
 */
public interface ConfigurationSection {
    /**
     * Gets a set containing all keys in this section.
     * <p>
     * If deep is set to true, then this will contain all the keys within any
     * child {@link ConfigurationSection}s (and their children, etc). These
     * will be in a valid path notation for you to use.
     * <p>
     * If deep is set to false, then this will contain only the keys of any
     * direct children, and not their own children.
     *
     * @param deep Whether or not to get a deep list, as opposed to a shallow
     *     list.
     * @return Set of keys contained within this ConfigurationSection.
     */
    Set<String> getKeys(boolean deep);

    /**
     * Gets a Map containing all keys and their values for this section.
     * <p>
     * If deep is set to true, then this will contain all the keys and values
     * within any child {@link ConfigurationSection}s (and their children,
     * etc). These keys will be in a valid path notation for you to use.
     * <p>
     * If deep is set to false, then this will contain only the keys and
     * values of any direct children, and not their own children.
     *
     * @param deep Whether or not to get a deep list, as opposed to a shallow
     *     list.
     * @return Map of keys and values of this section.
     */
    Map<String, Object> getValues(boolean deep);

    /**
     * Checks if this {@link ConfigurationSection} contains the given path.
     * <p>
     * If the value for the requested path does not exist but a default value
     * has been specified, this will return true.
     *
     * @param path Path to check for existence.
     * @return True if this section contains the requested path, either via
     *     default or being set.
     * @throws IllegalArgumentException Thrown when path is null.
     */
    boolean contains(String path);

    /**
     * Checks if this {@link ConfigurationSection} has a value set for the
     * given path.
     * <p>
     * If the value for the requested path does not exist but a default value
     * has been specified, this will still return false.
     *
     * @param path Path to check for existence.
     * @return True if this section contains the requested path, regardless of
     *     having a default.
     * @throws IllegalArgumentException Thrown when path is null.
     */
    boolean isSet(String path);

    /**
     * Gets the path of this {@link ConfigurationSection} from its root {@link
     * Configuration}
     * <p>
     * For any {@link Configuration} themselves, this will return an empty
     * string.
     * <p>
     * If the section is no longer contained within its root for any reason,
     * such as being replaced with a different value, this may return null.
     * <p>
     * To retrieve the single name of this section, that is, the final part of
     * the path returned by this method, you may use {@link #getName()}.
     *
     * @return Path of this section relative to its root
     */
    String getCurrentPath();

    /**
     * Gets the name of this individual {@link ConfigurationSection}, in the
     * path.
     * <p>
     * This will always be the final part of {@link #getCurrentPath()}, unless
     * the section is orphaned.
     *
     * @return Name of this section
     */
    String getName();

    /**
     * Gets the root {@link Configuration} that contains this {@link
     * ConfigurationSection}
     * <p>
     * For any {@link Configuration} themselves, this will return its own
     * object.
     * <p>
     * If the section is no longer contained within its root for any reason,
     * such as being replaced with a different value, this may return null.
     *
     * @return Root configuration containing this section.
     */
    Configuration getRoot();

    /**
     * Gets the parent {@link ConfigurationSection} that directly contains
     * this {@link ConfigurationSection}.
     * <p>
     * For any {@link Configuration} themselves, this will return null.
     * <p>
     * If the section is no longer contained within its parent for any reason,
     * such as being replaced with a different value, this may return null.
     *
     * @return Parent section containing this section.
     */
    ConfigurationSection getParent();

    /**
     * Gets the requested Object by path.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Path of the Object to get.
     * @return Requested Object.
     */
    Object get(String path);

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path of the Object to get.
     * @param def The default value to return if the path is not found.
     * @return Requested Object.
     */
    Object get(String path, Object def);

    /**
     * Sets the specified path to the given value.
     * <p>
     * If value is null, the entry will be removed. Any existing entry will be
     * replaced, regardless of what the new value is.
     * <p>
     * Some implementations may have limitations on what you may store. See
     * their individual javadocs for details. No implementations should allow
     * you to store {@link Configuration}s or {@link ConfigurationSection}s,
     * please use {@link #createSection(java.lang.String)} for that.
     *
     * @param path Path of the object to set.
     * @param value New value to set the path to.
     */
    void set(String path, Object value);

    /**
     * Creates an empty {@link ConfigurationSection} at the specified path.
     * <p>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was itself a {@link ConfigurationSection}, it will
     * be orphaned.
     *
     * @param path Path to create the section at.
     * @return Newly created section
     */
    ConfigurationSection createSection(String path);

    /**
     * Creates a {@link ConfigurationSection} at the specified path, with
     * specified values.
     * <p>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was itself a {@link ConfigurationSection}, it will
     * be orphaned.
     *
     * @param path Path to create the section at.
     * @param map The values to used.
     * @return Newly created section
     */
    ConfigurationSection createSection(String path, Map<?, ?> map);

    /**
     * Gets the requested ConfigurationSection by path.
     * <p>
     * If the ConfigurationSection does not exist but a default value has been
     * specified, this will return the default value. If the
     * ConfigurationSection does not exist and no default value was specified,
     * this will return null.
     *
     * @param path Path of the ConfigurationSection to get.
     * @return Requested ConfigurationSection.
     */
    ConfigurationSection getConfigurationSection(String path);

    /**
     * Checks if the specified path is a ConfigurationSection.
     * <p>
     * If the path exists but is not a ConfigurationSection, this will return
     * false. If the path does not exist, this will return false. If the path
     * does not exist but a default value has been specified, this will check
     * if that default value is a ConfigurationSection and return
     * appropriately.
     *
     * @param path Path of the ConfigurationSection to check.
     * @return Whether or not the specified path is a ConfigurationSection.
     */
    boolean isConfigurationSection(String path);
}
