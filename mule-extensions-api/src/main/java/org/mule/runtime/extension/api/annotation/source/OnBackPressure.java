/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.source;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.extension.api.runtime.source.BackPressureContext;
import org.mule.runtime.extension.api.runtime.source.Source;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to indicate that an annotated method in a {@link Source} should be executed when the runtime applies back pressure
 * on a message generated by the owning source.
 * <p>
 * The annotated method can either be argument-less or receive a {@link BackPressureContext}
 *
 * @since 1.1
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface OnBackPressure {

}