/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.api.resolver;

import static java.lang.String.format;
import static org.mule.metadata.utils.MetadataTypeUtils.getTypeId;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.FlattenedTypeAnnotation;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.TextTypeAnnotation;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.LayoutModelProperty;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.runtime.extension.api.util.SubTypesMappingContainer;
import org.mule.runtime.extension.xml.dsl.api.property.XmlModelProperty;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Utils class with helper methods for the {@link DslSyntaxResolver}
 *
 * @since 1.0
 */
class DslSyntaxUtils {

  static boolean isValidBean(ObjectType objectType) {
    return isInstantiable(objectType) && !objectType.getFields().isEmpty();
  }

  static boolean isFlattened(ObjectFieldType field, MetadataType fieldValue) {
    return fieldValue instanceof ObjectType && field.getAnnotation(FlattenedTypeAnnotation.class).isPresent();
  }

  static String getTypeKey(MetadataType type, String namespace, String namespaceUri) {
    return getTypeId(type)
        .orElse((type.getAnnotation(ClassInformationAnnotation.class).map(ClassInformationAnnotation::getName).orElse("")))
        + namespace + namespaceUri;
  }

  static boolean isText(ParameterModel parameter) {
    return parameter.getModelProperty(LayoutModelProperty.class).map(LayoutModelProperty::isText).orElse(false);
  }

  static boolean isText(MetadataType type) {
    return type.getAnnotation(TextTypeAnnotation.class).isPresent();
  }

  static boolean isInstantiable(MetadataType metadataType) {
    Optional<ClassInformationAnnotation> classInformation = metadataType.getAnnotation(ClassInformationAnnotation.class);
    return classInformation.map(ClassInformationAnnotation::isInstantiable).orElse(false);
  }

  static boolean isExtensible(MetadataType metadataType) {
    return metadataType.getAnnotation(ExtensibleTypeAnnotation.class).isPresent();
  }

  static Map<MetadataType, XmlModelProperty> loadImportedTypes(ExtensionModel extension, DslResolvingContext context) {
    final Map<MetadataType, XmlModelProperty> xmlByType = new HashMap<>();

    extension.getModelProperty(ImportedTypesModelProperty.class)
        .map(ImportedTypesModelProperty::getImportedTypes)
        .ifPresent(imports -> imports
            .forEach((type, ownerExtension) -> {
              ExtensionModel extensionModel = context.getExtension(ownerExtension)
                  .orElseThrow(
                               () -> new IllegalArgumentException(format("The Extension [%s] is not present in the current context",
                                                                         ownerExtension)));
              XmlModelProperty xml = extensionModel.getModelProperty(XmlModelProperty.class)
                  .orElseThrow(() -> new IllegalArgumentException(
                                                                  format("The Extension [%s] doesn't have the required model property [%s]",
                                                                         ownerExtension, XmlModelProperty.NAME)));
              xmlByType.put(type, xml);
            }));

    return xmlByType;
  }

  static SubTypesMappingContainer loadSubTypes(ExtensionModel extension) {
    return new SubTypesMappingContainer(extension.getModelProperty(SubTypesModelProperty.class)
        .map(SubTypesModelProperty::getSubTypesMapping)
        .orElse(ImmutableMap.of()));
  }

  static XmlModelProperty loadXmlProperties(ExtensionModel extension) {
    return extension.getModelProperty(XmlModelProperty.class)
        .orElseThrow(() -> new IllegalArgumentException(
                                                        format("The extension [%s] does not have the [%s], required for its Xml Dsl Resolution",
                                                               extension.getName(), XmlModelProperty.class.getSimpleName())));
  }

}