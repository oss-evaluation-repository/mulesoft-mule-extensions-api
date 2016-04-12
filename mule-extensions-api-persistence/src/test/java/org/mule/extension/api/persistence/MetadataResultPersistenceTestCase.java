/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import org.mule.api.metadata.DefaultMetadataKey;
import org.mule.api.metadata.MetadataKey;
import org.mule.api.metadata.MetadataKeyBuilder;
import org.mule.api.metadata.descriptor.ImmutableOperationMetadataDescriptor;
import org.mule.api.metadata.descriptor.ImmutableOutputMetadataDescriptor;
import org.mule.api.metadata.descriptor.ImmutableTypeMetadataDescriptor;
import org.mule.api.metadata.descriptor.OperationMetadataDescriptor;
import org.mule.api.metadata.descriptor.OutputMetadataDescriptor;
import org.mule.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.api.metadata.resolving.FailureCode;
import org.mule.api.metadata.resolving.ImmutableMetadataResult;
import org.mule.api.metadata.resolving.MetadataFailure;
import org.mule.api.metadata.resolving.MetadataResult;
import org.mule.extension.api.persistence.metadata.MetadataDescriptorResultJsonSerializer;
import org.mule.extension.api.persistence.metadata.MetadataKeysResultJsonSerializer;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.java.JavaTypeLoader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class MetadataResultPersistenceTestCase
{
    private static final String METADATA_DESCRIPTOR_RESULT_JSON = "metadata/success-result-descriptor.json";
    private static final String MEDATADA_KEYS_RESULT_JSON = "metadata/success-result-keys.json";
    private static final String METADATA_RESULT_FAILURE_JSON = "metadata/failure-result.json";

    private static final String FIRST_KEY_ID = "firstKey";
    private static final String SECOND_KEY_ID = "secondKey";
    private static final String METADATA_RESULT_ERROR_MESSAGE = "Metadata Failure Error";

    private OperationMetadataDescriptor operationMetadataDescriptor;
    private MetadataKeysResultJsonSerializer keysResultSerializer = new MetadataKeysResultJsonSerializer(true);
    private MetadataDescriptorResultJsonSerializer metadataDescriptorSerializer = new MetadataDescriptorResultJsonSerializer(true);
    private MetadataResult metadataResultFailure = MetadataResult.failure(null, METADATA_RESULT_ERROR_MESSAGE, FailureCode.CONNECTION_FAILURE, METADATA_RESULT_ERROR_MESSAGE);

    @Before
    public void setup()
    {
        operationMetadataDescriptor = buildTestOperationMetadataDescriptor();
    }

    @Test
    public void serializeSuccessMetadataDescriptorResult() throws IOException
    {
        String serialized = metadataDescriptorSerializer.serialize(MetadataResult.success(operationMetadataDescriptor));
        assertSerializedJson(serialized, METADATA_DESCRIPTOR_RESULT_JSON);
    }

    @Test
    public void serializeSuccessMetadataKeysResult() throws IOException
    {
        List<MetadataKey> keys = new ArrayList<>();
        keys.add(MetadataKeyBuilder.newKey(FIRST_KEY_ID).build());
        keys.add(MetadataKeyBuilder.newKey(SECOND_KEY_ID).build());

        String serialized = metadataDescriptorSerializer.serialize(MetadataResult.success(keys));
        assertSerializedJson(serialized, MEDATADA_KEYS_RESULT_JSON);
    }

    @Test
    public void serializeFailureMetadataResult() throws IOException
    {
        String serialized = metadataDescriptorSerializer.serialize(metadataResultFailure);
        assertSerializedJson(serialized, METADATA_RESULT_FAILURE_JSON);
    }

    @Test
    public void deserializeMetadataKeysResult() throws IOException
    {
        String resource = resourceAsString(MEDATADA_KEYS_RESULT_JSON);
        ImmutableMetadataResult<List<DefaultMetadataKey>> metadataResult = keysResultSerializer.deserialize(resource);

        assertThat(metadataResult.isSuccess(), is(true));
        assertThat(metadataResult.get().size(), is(2));
        assertThat(metadataResult.get().get(0).getDisplayName(), is(FIRST_KEY_ID));
        assertThat(metadataResult.get().get(1).getDisplayName(), is(SECOND_KEY_ID));
    }

    @Test
    public void deserializeOperationMetadataDescriptorResult() throws IOException
    {
        String resource = resourceAsString(METADATA_DESCRIPTOR_RESULT_JSON);
        ImmutableMetadataResult<ImmutableOperationMetadataDescriptor> metadataResult = metadataDescriptorSerializer.deserialize(resource);
        
        assertThat(metadataResult.isSuccess(), is(true));
        assertThat(metadataResult.get().getName(), is(operationMetadataDescriptor.getName()));
        assertThat(metadataResult.get().getParametersMetadata(), hasSize(3));
        assertOutputMedatada(metadataResult);
        assertContentMetadata(metadataResult);
    }

    @Test
    public void deserializeFailureResult() throws IOException
    {
        String resource = resourceAsString(METADATA_RESULT_FAILURE_JSON);
        ImmutableMetadataResult<List<DefaultMetadataKey>> metadataResult = keysResultSerializer.deserialize(resource);

        assertThat(metadataResult.isSuccess(), is(false));
        assertThat(metadataResult.getFailure().isPresent(), is(true));

        MetadataFailure metadataFailure = metadataResult.getFailure().get();
        assertThat(metadataFailure.getReason(), is(METADATA_RESULT_ERROR_MESSAGE));
        assertThat(metadataFailure.getMessage(), is(METADATA_RESULT_ERROR_MESSAGE));
        assertThat(metadataFailure.getFailureCode().getName(), is(FailureCode.CONNECTION_FAILURE.getName()));
    }

    private void assertSerializedJson(String serializedResult, String expectedFileName) throws IOException
    {
        String resource = resourceAsString(expectedFileName);
        final JsonParser jsonParser = new JsonParser();
        final JsonElement expected = jsonParser.parse(resource);
        final JsonElement result = jsonParser.parse(serializedResult);

        assertThat(result.equals(expected), is(true));
    }

    private String resourceAsString(String expectedFileName) throws IOException
    {
        final InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(expectedFileName);
        return IOUtils.toString(resourceAsStream);
    }

    private OperationMetadataDescriptor buildTestOperationMetadataDescriptor()
    {
        final JavaTypeLoader javaTypeLoader = new JavaTypeLoader(ExtensionModelPersistenceTestCase.class.getClassLoader());
        final MetadataType stringType = javaTypeLoader.load(String.class);
        final MetadataType intType = javaTypeLoader.load(Integer.class);

        List<TypeMetadataDescriptor> parameters = new ArrayList<>();
        parameters.add(new ImmutableTypeMetadataDescriptor("stringParam1", stringType));
        parameters.add(new ImmutableTypeMetadataDescriptor("stringParam2", stringType));
        parameters.add(new ImmutableTypeMetadataDescriptor("intParam1", intType));

        ImmutableOutputMetadataDescriptor outputMetadataDescriptor = new ImmutableOutputMetadataDescriptor(new ImmutableTypeMetadataDescriptor("output", stringType), new ImmutableTypeMetadataDescriptor("att", stringType));
        ImmutableTypeMetadataDescriptor content = new ImmutableTypeMetadataDescriptor("content", javaTypeLoader.load(Object.class));

        return new ImmutableOperationMetadataDescriptor("testOperationMetadataDescriptor", parameters, outputMetadataDescriptor, content);
    }

    private void assertOutputMedatada(MetadataResult<ImmutableOperationMetadataDescriptor> metadataResult)
    {
        OutputMetadataDescriptor outputMetadata = metadataResult.get().getOutputMetadata();
        OutputMetadataDescriptor expectedOutputMetadata = operationMetadataDescriptor.getOutputMetadata();
        assertThat(outputMetadata.getAttributesMetadata().getName(), is(expectedOutputMetadata.getAttributesMetadata().getName()));
        assertThat(outputMetadata.getAttributesMetadata().getType(), is(expectedOutputMetadata.getAttributesMetadata().getType()));
        assertThat(outputMetadata.getPayloadMetadata().getName(), is(expectedOutputMetadata.getPayloadMetadata().getName()));
        assertThat(outputMetadata.getPayloadMetadata().getType(), is(expectedOutputMetadata.getPayloadMetadata().getType()));
    }

    private void assertContentMetadata(MetadataResult<ImmutableOperationMetadataDescriptor> metadataResult)
    {
        TypeMetadataDescriptor contentMetadata = metadataResult.get().getContentMetadata().get();
        TypeMetadataDescriptor expectedContentMetadata = operationMetadataDescriptor.getContentMetadata().get();
        assertThat(contentMetadata.getName(), is(expectedContentMetadata.getName()));
        assertThat(contentMetadata.getType(), is(expectedContentMetadata.getType()));
    }
}