package com.dumptruckman.dircbot.plugin;

import com.dumptruckman.dircbot.util.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.annotation.Comment;
import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.annotation.ValidateWith;
import pluginbase.config.field.DependentField;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.config.field.Validator;
import pluginbase.config.field.Validators;
import pluginbase.config.serializers.Serializer;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.Message;
import pluginbase.messages.MessageProvider;

public class PluginConfig {

    @Comment("Settings related to language/locale.")
    @Nullable
    private Locale locale = null;

    @Comment("0 = off, 1-3 display debug info with increasing granularity.")
    @ValidateWith(DebugLevelValidator.class)
    private final VirtualDebug debugLevel = new VirtualDebug();

    @Comment("Will make the plugin perform tasks only done on a first run (if any.)")
    private boolean firstRun = true;

    public PluginConfig(@NotNull Plugin plugin) {
        this.debugLevel.setLogger(plugin.getLogger());
    }

    protected PluginConfig() { }

    void useLocalization() {
        if (locale == null) {
            locale = new Locale();
        }
    }

    @Nullable
    public Locale getLocaleSettings() {
        return locale;
    }

    public int getDebugLevel() {
        Integer level = debugLevel.get();
        return level != null ? level : 0;
    }

    public void setDebugLevel(int debugLevel) throws PropertyVetoException {
        Integer value = Validators.getValidator(DebugLevelValidator.class).validateChange(debugLevel, getDebugLevel());
        this.debugLevel.set(value != null ? value : 0);
    }

    public boolean isFirstRun() {
        return firstRun;
    }

    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }

    public static class Locale {

        @Comment("This is the locale you wish to use for messages.")
        @SerializeWith(LocaleSerializer.class)
        @NotNull
        private java.util.Locale locale = MessageProvider.DEFAULT_LOCALE;

        @Comment("The language file that contains localized messages.")
        @NotNull
        private String languageFile = MessageProvider.DEFAULT_LANGUAGE_FILE_NAME;

        protected Locale() {
        }

        @NotNull
        public java.util.Locale getLocale() {
            return locale;
        }

        public void setLocale(@NotNull java.util.Locale locale) {
            this.locale = locale;
        }

        @NotNull
        public String getLanguageFile() {
            return languageFile;
        }

        public void setLanguageFile(@NotNull String languageFile) {
            this.languageFile = languageFile;
        }

        public static class LocaleSerializer implements Serializer<java.util.Locale> {

            private LocaleSerializer() {
            }

            @Nullable
            @Override
            public Object serialize(@Nullable java.util.Locale locale) {
                return locale != null ? locale.toString() : MessageProvider.DEFAULT_LOCALE.toString();
            }

            @Nullable
            @Override
            public java.util.Locale deserialize(@Nullable Object object, @NotNull Class wantedType) throws IllegalArgumentException {
                if (object == null) {
                    return MessageProvider.DEFAULT_LOCALE;
                }
                String[] split = object.toString().split("_");
                switch (split.length) {
                    case 1:
                        return new java.util.Locale(split[0]);
                    case 2:
                        return new java.util.Locale(split[0], split[1]);
                    case 3:
                        return new java.util.Locale(split[0], split[1], split[2]);
                    default:
                        return new java.util.Locale(object.toString());
                }
            }
        }
    }

    private static class DebugLevelValidator implements Validator<Integer> {
        @Nullable
        @Override
        public Integer validateChange(@Nullable Integer newValue, @Nullable Integer oldValue) throws PropertyVetoException {
            if (newValue == null) {
                return oldValue == null ? 0 : oldValue;
            } else if (newValue >= 0 && newValue <= 3) {
                return newValue;
            } else {
                throw new PropertyVetoException(Message.bundleMessage(Language.INVALID_DEBUG_LEVEL));
            }
        }
    }

    private static class VirtualDebug extends DependentField<Integer, PluginLogger> {

        private PluginLogger logger = null;

        VirtualDebug() {
            super(0);
        }

        public void setLogger(PluginLogger logger) {
            this.logger = logger;
        }

        protected PluginLogger getDependency() {
            return logger;
        }

        @Override
        protected Integer getDependentValue() {
            return getDependency().getDebugLevel();
        }

        @Override
        protected void setDependentValue(@Nullable Integer value) {
            getDependency().setDebugLevel(value != null ? value : 0);
        }
    }
}
