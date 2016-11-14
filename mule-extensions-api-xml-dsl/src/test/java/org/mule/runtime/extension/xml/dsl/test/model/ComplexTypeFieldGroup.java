/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.test.model;

import org.mule.runtime.extension.api.annotation.param.Parameter;

public class ComplexTypeFieldGroup {

  @Parameter
  private ComplexFieldsType complexFieldsType;

  public ComplexFieldsType getComplexFieldsType() {
    return complexFieldsType;
  }

}