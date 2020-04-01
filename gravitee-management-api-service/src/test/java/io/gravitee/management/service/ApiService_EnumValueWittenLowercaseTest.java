/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.management.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.gravitee.management.model.Visibility;
import io.gravitee.management.model.api.ApiEntity;
import io.gravitee.management.service.jackson.ser.api.ApiCompositeSerializer;
import io.gravitee.management.service.jackson.ser.api.ApiSerializer;
import io.gravitee.management.service.spring.ServiceConfiguration;
import io.gravitee.repository.exceptions.TechnicalException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author Guillaume Gillon
 */
//@RunWith(MockitoJUnitRunner.class)
public class ApiService_EnumValueWittenLowercaseTest {

    private static final String API_ID = "id-api";

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws TechnicalException {
        //((ApiCompositeSerializer) objectMapper.getSerializerProvider()).afterPropertiesSet();
        ServiceConfiguration serviceConfiguration = new ServiceConfiguration();
        objectMapper = serviceConfiguration.objectMapper();

        ApiCompositeSerializer apiSerializer = (ApiCompositeSerializer)serviceConfiguration.apiSerializer();
        apiSerializer.afterPropertiesSet();

        SimpleModule module = new SimpleModule();
        module.addSerializer(ApiEntity.class, apiSerializer);
        objectMapper.registerModule(module);
    }

    @Test
    public void shouldConvertAsJsonForExportWithLowercaseEnum() throws TechnicalException, IOException {
        ApiEntity apiEntity = new ApiEntity();
        apiEntity.setId(API_ID);
        apiEntity.setName("test");
        apiEntity.setDescription("Gravitee.io");
        apiEntity.setVisibility(Visibility.PUBLIC);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ApiSerializer.METADATA_EXPORT_VERSION, ApiSerializer.Version.DEFAULT.getVersion());
        metadata.put(ApiSerializer.METADATA_FILTERED_FIELDS_LIST, Arrays.asList("groups","members","pages", "plans","metadata"));
        apiEntity.setMetadata(metadata);

        Object test = objectMapper.getRegisteredModuleIds();


        String result = objectMapper.writeValueAsString(apiEntity);
        System.out.println("result" + result);
        assertThat(result).isEqualTo("{\n" +
                "  \"name\" : \"test\",\n" +
                "  \"description\" : \"Gravitee.io\",\n" +
                "  \"visibility\" : \"public\",\n" +
                "  \"paths\" : { },\n" +
                "  \"resources\" : [ ],\n" +
                "  \"path_mappings\" : [ ]\n" +
                "}");
    }

}
