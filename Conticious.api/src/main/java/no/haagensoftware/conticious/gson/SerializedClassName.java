package no.haagensoftware.conticious.gson;

import java.lang.annotation.ElementType;

@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ElementType.TYPE})
public @interface SerializedClassName {
    java.lang.String value();
}