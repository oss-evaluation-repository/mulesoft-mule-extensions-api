/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Optional.empty;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.metadata.internal.utils.MetadataTypeUtils.getTypeId;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.CONTENT;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.PRIMARY_CONTENT;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.isContent;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.ElementDslModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.SubTypesModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.dsl.model.ExtensibleType;
import org.mule.runtime.extension.api.dsl.model.SubType;
import org.mule.runtime.extension.api.dsl.model.SuperType;
import org.mule.runtime.extension.api.dsl.resolver.DslResolvingContext;
import org.mule.runtime.extension.api.dsl.resolver.DslSyntaxResolver;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseXmlDeclarationTestCase {

  static final String NAMESPACE = "mockns";
  static final String NAMESPACE_URI = "http://www.mulesoft.org/schema/mule/mockns";
  static final String SCHEMA_LOCATION = "http://www.mulesoft.org/schema/mule/mockns/current/mule-mockns.xsd";
  static final String PARAMETER_NAME = "myCamelCaseName";
  static final String COLLECTION_NAME = "myCamelCaseNames";
  static final String SINGULARIZABLE_PARAMETER_NAME = "singularizableNames";
  static final String EXTENSION_NAME = "extension";
  static final String OPERATION_NAME = "mockOperation";
  static final String SOURCE_NAME = "source";
  static final String CONFIGURATION_NAME = "configuration";
  static final String CONNECTION_PROVIDER_NAME = "connection";
  static final BaseTypeBuilder TYPE_BUILDER = BaseTypeBuilder.create(JAVA);
  static final String EXTENSIBLE_TYPE_LIST_NAME = "extensibleTypeList";

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        {BEHAVIOUR}, {CONTENT}, {PRIMARY_CONTENT}
    });
  }

  @Mock
  protected ExtensionModel extension;

  @Mock
  protected ConfigurationModel configuration;

  @Mock
  protected OperationModel operation;

  @Mock
  protected ConnectionProviderModel connectionProvider;

  @Mock
  protected ParameterModel parameterModel;

  @Mock(answer = RETURNS_DEEP_STUBS)
  protected SourceModel source;

  @Mock
  protected DslResolvingContext dslContext;


  protected final ParameterRole role;
  protected ClassTypeLoader TYPE_LOADER = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
  protected SubTypesModel subTypesModel =
      new SubTypesModel(TYPE_LOADER.load(SuperType.class), singleton(TYPE_LOADER.load(SubType.class)));

  public BaseXmlDeclarationTestCase(ParameterRole role) {
    this.role = role;
  }

  @Before
  public void before() {
    initMocks(this);
    when(extension.getName()).thenReturn(EXTENSION_NAME);
    when(extension.getConfigurationModels()).thenReturn(asList(configuration));
    when(extension.getConfigurationModels()).thenReturn(asList(configuration));
    when(extension.getOperationModels()).thenReturn(asList(operation));
    when(extension.getSourceModels()).thenReturn(asList(source));
    when(extension.getConnectionProviders()).thenReturn(asList(connectionProvider));

    when(extension.getSubTypes()).thenReturn(singleton(subTypesModel));
    when(extension.getImportedTypes()).thenReturn(emptySet());
    when(extension.getXmlDslModel()).thenReturn(XmlDslModel.builder()
        .setXsdFileName(EMPTY)
        .setNamespace(NAMESPACE)
        .setNamespaceUri(NAMESPACE_URI)
        .setSchemaLocation(SCHEMA_LOCATION)
        .setSchemaVersion(EMPTY)
        .build());

    when(configuration.getOperationModels()).thenReturn(asList(operation));
    when(configuration.getSourceModels()).thenReturn(asList(source));
    when(configuration.getConnectionProviders()).thenReturn(asList(connectionProvider));

    when(parameterModel.getName()).thenReturn(PARAMETER_NAME);
    when(parameterModel.getExpressionSupport()).thenReturn(ExpressionSupport.SUPPORTED);
    when(parameterModel.getModelProperty(any())).thenReturn(empty());
    when(parameterModel.getDslModel()).thenReturn(ElementDslModel.getDefaultInstance());
    when(parameterModel.getLayoutModel()).thenReturn(empty());
    when(parameterModel.getRole()).thenReturn(role);

    when(source.getName()).thenReturn(SOURCE_NAME);
    when(source.getParameterGroupModels()).thenReturn(emptyList());
    when(source.getSuccessCallback()).thenReturn(empty());
    when(source.getErrorCallback()).thenReturn(empty());
    when(operation.getName()).thenReturn(OPERATION_NAME);
    when(configuration.getName()).thenReturn(CONFIGURATION_NAME);
    when(connectionProvider.getName()).thenReturn(CONNECTION_PROVIDER_NAME);

    when(dslContext.getExtension(any())).thenReturn(empty());

    Stream.of(configuration, operation, connectionProvider, source).forEach(
                                                                            model -> when(model.getAllParameterModels())
                                                                                .thenReturn(asList(parameterModel)));
  }

  void assertParameterChildElementDeclaration(boolean expected, DslElementSyntax result) {
    if (isContent(parameterModel)) {
      expected = true;
    }

    assertChildElementDeclarationIs(expected, result);
  }

  void assertChildElementDeclarationIs(boolean expected, DslElementSyntax result) {
    assertThat("Expected attribute only declaration", result.supportsChildDeclaration(), is(expected));
  }

  void assertTopLevelDeclarationSupportIs(boolean expected, DslElementSyntax result) {
    assertThat("Wrong TopLevel declaration support", result.supportsTopLevelDeclaration(), is(expected));
  }

  void assertIsWrappedElement(boolean expected, DslElementSyntax result) {
    assertThat("Expected no wrapping but element is wrapped", result.isWrapped(), is(expected));
  }

  void assertAttributeDeclaration(boolean expected, DslElementSyntax result) {
    assertThat(result.supportsAttributeDeclaration(), is(expected));
  }

  void assertAttributeName(String expected, DslElementSyntax result) {
    assertThat(result.getAttributeName(), equalTo(expected));
  }

  void assertEmptyAttributeName(DslElementSyntax result) {
    assertThat(result.getAttributeName(), isEmptyString());
  }

  void assertElementName(String expected, DslElementSyntax result) {
    assertThat(result.getElementName(), equalTo(expected));
  }

  void assertElementNamespace(String expected, DslElementSyntax result) {
    assertThat(result.getNamespace(), equalTo(expected));
  }

  void assertTopElementDeclarationIs(boolean expected, DslElementSyntax result) {
    assertThat("Expected the element to support Top Level definitions", result.supportsTopLevelDeclaration(), is(expected));
  }

  DslSyntaxResolver getSyntaxResolver() {
    return new DslSyntaxResolver(extension, dslContext);
  }

  protected void assertComplexTypeDslFields(DslElementSyntax topDsl) {
    DslElementSyntax listDsl = getChildFieldDsl(EXTENSIBLE_TYPE_LIST_NAME, topDsl);
    assertAttributeName(EXTENSIBLE_TYPE_LIST_NAME, listDsl);
    assertElementName(hyphenize(EXTENSIBLE_TYPE_LIST_NAME), listDsl);
    assertElementNamespace(NAMESPACE, listDsl);
    assertChildElementDeclarationIs(true, listDsl);
    assertIsWrappedElement(false, listDsl);

    MetadataType listItemType = TYPE_LOADER.load(ExtensibleType.class);
    DslElementSyntax listItemDsl = getGenericTypeDsl(listItemType, listDsl);
    assertElementName(getTopLevelTypeName(listItemType), listItemDsl);
    assertElementNamespace(NAMESPACE, listItemDsl);
    assertChildElementDeclarationIs(true, listItemDsl);
    assertTopElementDeclarationIs(false, listItemDsl);
    assertIsWrappedElement(true, listItemDsl);

    String recursiveChildName = "recursiveChild";
    DslElementSyntax recursiveChildDsl = getChildFieldDsl(recursiveChildName, topDsl);
    assertAttributeName(recursiveChildName, recursiveChildDsl);
    assertElementName(hyphenize(recursiveChildName), recursiveChildDsl);
    assertElementNamespace(NAMESPACE, recursiveChildDsl);
    assertChildElementDeclarationIs(true, recursiveChildDsl);
    assertTopLevelDeclarationSupportIs(false, recursiveChildDsl);
    assertIsWrappedElement(false, recursiveChildDsl);

    String recursiveFromGroupName = "complexFieldsType";
    DslElementSyntax recursiveFromGroupDsl = getChildFieldDsl(recursiveFromGroupName, topDsl);
    assertAttributeName(recursiveFromGroupName, recursiveFromGroupDsl);
    assertElementName(hyphenize(recursiveFromGroupName), recursiveFromGroupDsl);
    assertElementNamespace(NAMESPACE, recursiveFromGroupDsl);
    assertChildElementDeclarationIs(true, recursiveFromGroupDsl);
    assertTopLevelDeclarationSupportIs(false, recursiveFromGroupDsl);
    assertIsWrappedElement(false, recursiveFromGroupDsl);

    String simplePojoName = "simplePojo";
    DslElementSyntax simplePojoDsl = getChildFieldDsl(simplePojoName, topDsl);
    assertAttributeName(simplePojoName, simplePojoDsl);
    assertElementName(hyphenize(simplePojoName), simplePojoDsl);
    assertElementNamespace(NAMESPACE, simplePojoDsl);
    assertChildElementDeclarationIs(true, simplePojoDsl);
    assertIsWrappedElement(false, simplePojoDsl);
    assertTopElementDeclarationIs(false, simplePojoDsl);

    String notGlobalName = "notGlobalType";
    DslElementSyntax notGlobalDsl = getChildFieldDsl(notGlobalName, topDsl);
    assertAttributeName(notGlobalName, notGlobalDsl);
    assertElementName(hyphenize(notGlobalName), notGlobalDsl);
    assertElementNamespace(NAMESPACE, notGlobalDsl);
    assertChildElementDeclarationIs(true, notGlobalDsl);
    assertIsWrappedElement(false, notGlobalDsl);

    String groupedField = "groupedField";
    DslElementSyntax groupedFieldDsl = getChildFieldDsl(groupedField, topDsl);
    assertAttributeName(groupedField, groupedFieldDsl);
    assertElementName("", groupedFieldDsl);
    assertElementNamespace("", groupedFieldDsl);
    assertChildElementDeclarationIs(false, groupedFieldDsl);
    assertIsWrappedElement(false, groupedFieldDsl);

    String anotherGroupedField = "anotherGroupedField";
    DslElementSyntax anotherGroupedFieldDsl = getChildFieldDsl(anotherGroupedField, topDsl);
    assertAttributeName(anotherGroupedField, anotherGroupedFieldDsl);
    assertElementName("", anotherGroupedFieldDsl);
    assertElementNamespace("", anotherGroupedFieldDsl);
    assertChildElementDeclarationIs(false, anotherGroupedFieldDsl);
    assertIsWrappedElement(false, anotherGroupedFieldDsl);

    String parameterGroupType = "parameterGroupType";
    assertThat(topDsl.getChild(parameterGroupType).isPresent(), is(false));

    String complexTypeFieldGroup = "complexTypeFieldGroup";
    assertThat(topDsl.getChild(complexTypeFieldGroup).isPresent(), is(false));
  }

  protected DslElementSyntax getGenericTypeDsl(MetadataType itemType, DslElementSyntax result) {
    Optional<DslElementSyntax> genericDsl = result.getGeneric(itemType);
    assertThat("No generic element found for type [" + getTypeId(itemType).orElse("") + "] for element ["
        + result.getElementName() + "]",
               genericDsl.isPresent(), is(true));

    return genericDsl.get();
  }

  protected DslElementSyntax getChildFieldDsl(String name, DslElementSyntax parent) {
    Optional<DslElementSyntax> childDsl = parent.getChild(name);
    assertThat("No child element found with name [" + name + "] for element [" + parent.getElementName() + "]",
               childDsl.isPresent(), is(true));

    return childDsl.get();
  }

}