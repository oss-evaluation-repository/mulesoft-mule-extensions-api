/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api;

/**
 * Enum that stores a list of valid categories that a extension can be assigned to.
 * <p>
 * <ul>
 * <li>{@link Category#COMMUNITY}: Represents that the extension does not requires an Enterprise Mule Runtime to work.</li>
 * <li>{@link Category#SELECT}: Represents that the extension requires an Enterprise Mule Runtime to work.</li>
 * <li>{@link Category#PREMIUM}: Represents that the extension requires an Enterprise Mule Runtime to work and also
 * an extension entitlement to work</li>
 * <li>{@link Category#CERTIFIED}: Represent that the extension is MuleSoft certified and requires an Enterprise Mule
 * Runtime to work</li>
 * </ul>
 *
 * @since 1.0
 */
public enum Category
{
    COMMUNITY, SELECT, PREMIUM, CERTIFIED
}