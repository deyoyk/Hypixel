package com.deyo.hypixel.type.generic.command;

import com.deyo.hypixel.type.generic.user.categories.Rank;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters {
    String description() default "";

    String labels() default "";

    String usage();

    Rank permission();

    boolean allowsConsole();
}
