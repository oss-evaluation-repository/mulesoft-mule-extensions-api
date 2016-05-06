/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.introspection.config.ConfigurationModel;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderFactory;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;

/**
 * A declaration object for a {@link ConnectionProviderModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link ConfigurationModel}
 *
 * @since 1.0
 */
public final class ConnectionProviderDeclaration extends ParameterizedInterceptableDeclaration<ConnectionProviderDeclaration> implements ParameterizedDeclaration
{
    private ConnectionProviderFactory factory;
    private Class<?> configurationType;
    private Class<?> connectionType;

    /**
     * {@inheritDoc}
     */
    ConnectionProviderDeclaration(String name)
    {
        super(name);
    }

    public ConnectionProviderFactory getFactory()
    {
        return factory;
    }

    void setFactory(ConnectionProviderFactory factory)
    {
        this.factory = factory;
    }

    public Class<?> getConfigurationType()
    {
        return configurationType;
    }

    void setConfigurationType(Class<?> configurationType)
    {
        this.configurationType = configurationType;
    }

    public Class<?> getConnectionType()
    {
        return connectionType;
    }

    void setConnectionType(Class<?> connectionType)
    {
        this.connectionType = connectionType;
    }
}