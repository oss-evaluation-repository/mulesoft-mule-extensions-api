/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.param;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation is meant to be applied on fields of classes which are serving as top level elements which can be referenced by
 * name, such as configs or global objects.
 * <p>
 * This annotation signals that the target field should be injected with the name that the element has received on the
 * application.
 * <p>
 * This implies the following restrictions:
 * <p>
 * <lu>
 * <li>The field should be of type {@link String}</li>
 * <li>No two fields in the same class should bear this annotation</li> </lu>
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(value = FIELD)
@Retention(RUNTIME)
@Documented
public @interface RefName {

}
