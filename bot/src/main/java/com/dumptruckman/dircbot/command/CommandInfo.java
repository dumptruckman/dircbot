package com.dumptruckman.dircbot.command;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface CommandInfo {
    String[] aliases();
    CommandType type() default CommandType.BOTH;
    boolean isAdminRequired() default false;
}
