package io.tackle.controls.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to define if a field's value must be evaluated when filtering the list of resources
 * the field belongs to.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Filterable {
    String filterName() default "";
}
