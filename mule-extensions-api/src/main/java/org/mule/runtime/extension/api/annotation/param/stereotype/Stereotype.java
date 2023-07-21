/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.param.stereotype;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.stereotype.StereotypeDefinition;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for {@link SourceModel Sources}, {@link OperationModel Operations}, {@link ConfigurationModel Configuration} and
 * {@link ConnectionProviderModel Connections} level to communicate and declare the {@link StereotypeDefinition}s that
 * characterize the annotated components
 *
 * @since 1.0
 * @see StereotypeDefinition
 */
@MinMuleVersion("4.1")
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Stereotype {

  Class<? extends StereotypeDefinition> value();
}
