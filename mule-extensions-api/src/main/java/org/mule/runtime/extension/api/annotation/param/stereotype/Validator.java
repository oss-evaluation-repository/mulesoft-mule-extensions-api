/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.param.stereotype;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.error.MuleErrors;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Stereotype annotation to mark that one or more operations are validators.
 * <p>
 * Any operation reached by this annotation must:
 * <ul>
 * <li>Have a void return type</li>
 * <li>Declare that it throws at least one Error which is {@link MuleErrors#VALIDATION} or has it as parent</li>
 * </ul>
 *
 * This annotation can either be used at the method level (signaling that the stereotype applies to that specific operation) or to
 * the class level (signaling that all operations in that class have that stereotype, unless overridden by a particular
 * operation).
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Validator {

}
