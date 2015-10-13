/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import static org.mule.extension.api.introspection.ExpressionSupport.SUPPORTED;
import org.mule.extension.api.introspection.DataType;
import org.mule.extension.api.introspection.Described;
import org.mule.extension.api.introspection.ExpressionSupport;
import org.mule.extension.api.introspection.ParameterModel;

/**
 * A declaration object for a {@link ParameterModel}. It contains raw,
 * unvalidated data which is used to declare the structure of a
 * {@link ParameterModel}.
 * <p/>
 * By default, {@link #getExpressionSupport()} ()} returns
 * {@link ExpressionSupport#SUPPORTED} and {@link #getDescription()}
 * returns an empty {@link String}.
 *
 * @since 1.0
 */
public final class ParameterDeclaration extends BaseDeclaration<ParameterDeclaration> implements Described
{
    private String name;
    private String description = "";
    private boolean required;
    private ExpressionSupport expressionSupport = SUPPORTED;
    private DataType type;
    private Object defaultValue = null;

    public ParameterDeclaration()
    {
    }

    @Override
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public ExpressionSupport getExpressionSupport()
    {
        return expressionSupport;
    }

    public void setExpressionSupport(ExpressionSupport expressionSupport)
    {
        this.expressionSupport = expressionSupport;
    }

    public DataType getType()
    {
        return type;
    }

    public void setType(DataType type)
    {
        this.type = type;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Object getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue)
    {
        this.defaultValue = defaultValue;
    }
}
