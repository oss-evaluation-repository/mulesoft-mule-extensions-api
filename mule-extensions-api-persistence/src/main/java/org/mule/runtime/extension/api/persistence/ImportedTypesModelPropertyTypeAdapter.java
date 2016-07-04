/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.persistence.MetadataTypeGsonTypeAdapter;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link TypeAdapter} implementation which serializes {@link ImportedTypesModelProperty} instances
 *
 * @since 1.0
 */
class ImportedTypesModelPropertyTypeAdapter extends TypeAdapter<ImportedTypesModelProperty>
{

    private static final String EXTENSION = "extension";
    private static final String TYPE = "type";
    private final MetadataTypeGsonTypeAdapter typeAdapter = new MetadataTypeGsonTypeAdapter();

    @Override
    public void write(JsonWriter out, ImportedTypesModelProperty value) throws IOException
    {
        out.beginArray();
        for (Map.Entry<MetadataType, MetadataType> entry : value.getImportedTypes().entrySet())
        {
            out.beginObject();
            out.name(TYPE);
            typeAdapter.write(out, entry.getKey());
            out.name(EXTENSION);
            typeAdapter.write(out, entry.getValue());
            out.endObject();
        }
        out.endArray();
    }

    @Override
    public ImportedTypesModelProperty read(JsonReader in) throws IOException
    {
        final Map<MetadataType, MetadataType> importedTypesMap = new HashMap<>();
        final JsonArray importedTypesArray = new JsonParser().parse(in).getAsJsonArray();

        importedTypesArray.iterator().forEachRemaining(importedTypeElement -> {
            final JsonObject tuple = importedTypeElement.getAsJsonObject();
            importedTypesMap.put(typeAdapter.fromJsonTree(tuple.get(TYPE)), typeAdapter.fromJsonTree(tuple.get(EXTENSION)));
        });
        return new ImportedTypesModelProperty(importedTypesMap);
    }
}