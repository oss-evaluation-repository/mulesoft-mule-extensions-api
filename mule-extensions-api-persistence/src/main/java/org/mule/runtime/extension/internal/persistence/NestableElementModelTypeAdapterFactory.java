/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.api.meta.model.nested.NestableElementModel;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * A {@link TypeAdapterFactory} which yields instances of {@link OperationModelTypeAdapter}
 *
 * @since 1.0
 */
public class NestableElementModelTypeAdapterFactory implements TypeAdapterFactory {

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (NestableElementModel.class.isAssignableFrom(type.getRawType())) {
      return (TypeAdapter<T>) new NestedElementModelTypeAdapter(this, gson);
    }

    return null;
  }
}
