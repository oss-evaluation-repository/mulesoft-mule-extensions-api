/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.test;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.config.ConfigurationModel;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.runtime.extension.api.introspection.source.SourceModel;
import org.mule.runtime.extension.xml.dsl.api.DslElementSyntax;
import org.mule.runtime.extension.xml.dsl.api.property.XmlModelProperty;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseXmlDeclarationTestCase
{

    static final String NAMESPACE = "mockns";
    static final String NAMESPACE_URI = "http://www.mulesoft.org/schema/mule/mockns";
    static final String SCHEMA_LOCATION = "http://www.mulesoft.org/schema/mule/mockns/current/mule-mockns.xsd";
    static final String PARAMETER_NAME = "myCamelCaseName";
    static final String EXTENSION_NAME = "extension";
    static final String OPERATION_NAME = "mockOperation";
    static final String SOURCE_NAME = "source";
    static final String CONFIGURATION_NAME = "configuration";
    static final String CONNECTION_PROVIDER_NAME = "connection";
    static final String IMPORT_NAMESPACE = "importns";
    static final String IMPORT_NAMESPACE_URI = "http://www.mulesoft.org/schema/mule/importns";
    static final String IMPORT_EXTENSION_NAME = "importExtension";
    static final BaseTypeBuilder<?> TYPE_BUILDER = BaseTypeBuilder.create(JAVA);

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

    @Mock
    protected SourceModel source;

    ClassTypeLoader TYPE_LOADER = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

    @Before
    public void before()
    {
        when(extension.getName()).thenReturn(EXTENSION_NAME);
        when(extension.getConfigurationModels()).thenReturn(asList(configuration));
        when(extension.getConfigurationModels()).thenReturn(asList(configuration));
        when(extension.getOperationModels()).thenReturn(asList(operation));
        when(extension.getSourceModels()).thenReturn(asList(source));
        when(extension.getConnectionProviders()).thenReturn(asList(connectionProvider));

        when(extension.getModelProperty(SubTypesModelProperty.class)).thenReturn(empty());
        when(extension.getModelProperty(ImportedTypesModelProperty.class)).thenReturn(empty());
        when(extension.getModelProperty(XmlModelProperty.class)).thenReturn(
                Optional.of(new XmlModelProperty(EMPTY, NAMESPACE, NAMESPACE_URI, EMPTY, SCHEMA_LOCATION)));

        when(configuration.getOperationModels()).thenReturn(asList(operation));
        when(configuration.getSourceModels()).thenReturn(asList(source));
        when(configuration.getConnectionProviders()).thenReturn(asList(connectionProvider));

        when(parameterModel.getName()).thenReturn(PARAMETER_NAME);
        when(parameterModel.getExpressionSupport()).thenReturn(ExpressionSupport.SUPPORTED);
        when(parameterModel.getModelProperty(any())).thenReturn(empty());

        when(source.getName()).thenReturn(SOURCE_NAME);
        when(operation.getName()).thenReturn(OPERATION_NAME);
        when(configuration.getName()).thenReturn(CONFIGURATION_NAME);
        when(connectionProvider.getName()).thenReturn(CONNECTION_PROVIDER_NAME);

        Stream.of(configuration, operation, connectionProvider, source).forEach(
                model -> when(model.getParameterModels()).thenReturn(asList(parameterModel)));
    }

    void assertChildElementDeclarationIs(boolean expected, DslElementSyntax result)
    {
        assertThat("Expected attribute only declaration", result.supportsChildDeclaration(), is(expected));
    }

    void assertIsWrappedElement(boolean expected, DslElementSyntax result)
    {
        assertThat("Expected no wrapping but element is wrapped", result.isWrapped(), is(expected));
    }

    void assertAttributeName(String expected, DslElementSyntax result)
    {
        assertThat(result.getAttributeName(), equalTo(expected));
    }

    void assertElementName(String expected, DslElementSyntax result)
    {
        assertThat(result.getElementName(), equalTo(expected));
    }

    void assertElementNamespace(String expected, DslElementSyntax result)
    {
        assertThat(result.getNamespace(), equalTo(expected));
    }

    void assertTopElementDeclarationIs(boolean expected, DslElementSyntax result)
    {
        assertThat("Expected the element to support Top Level definitions", result.supportsTopLevelDeclaration(), is(expected));
    }

}