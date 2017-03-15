package com.inin.keymanagement.config.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used on Spring controllers. This will tell spring whether we should require authorization for the
 * endpoint.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRequired {
    boolean required() default true;
}