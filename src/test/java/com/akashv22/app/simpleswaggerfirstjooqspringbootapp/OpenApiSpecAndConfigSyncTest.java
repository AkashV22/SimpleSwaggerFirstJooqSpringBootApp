/*
 * Copyright (c) 2019 AkashV22
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.akashv22.app.simpleswaggerfirstjooqspringbootapp;

import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import java.io.IOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenApiSpecAndConfigSyncTest {
    @Test
    public void testSpecAndConfigAreInSync() throws IOException {
        SwaggerConfiguration swaggerConfiguration = readYaml("/openapi-configuration.yaml", SwaggerConfiguration.class);
        OpenAPI openAPI = readYaml("/openapi.yaml", OpenAPI.class);

        assertEquals(openAPI, swaggerConfiguration.getOpenAPI());
    }

    private <T> T readYaml(String resource, Class<T> clazz) throws IOException {
        return Yaml.mapper().readValue(getClass().getResourceAsStream(resource), clazz);
    }
}
