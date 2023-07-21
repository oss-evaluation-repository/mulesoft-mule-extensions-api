/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.dsl.syntax.resources.spi;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.api.meta.model.ExtensionModel;

/**
 * Enables the generation of XML Schema Definitions for extensions.
 *
 * @since 1.0
 */
@NoImplement
public interface ExtensionSchemaGenerator {

  /**
   * Creates the XSD for an extension based on it's {@code extensionModel}.
   *
   * @param extensionModel the {@link ExtensionModel} used to generate the schema.
   * @return an String containing the XML Schema Definition for the given extension.
   */
  String generate(ExtensionModel extensionModel, DslResolvingContext context);
}
