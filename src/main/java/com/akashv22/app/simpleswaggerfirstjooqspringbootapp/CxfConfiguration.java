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

import com.akashv22.app.simpleswaggerfirstjooqspringbootapp.all.endpoint.ApiEndpoint;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.openapi.OpenApiCustomizer;
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Configuration
public class CxfConfiguration {
    @Bean
    public Server rsServer(Bus bus, List<ApiEndpoint> endpoints, OpenApiFeature openApiFeature) {
        JAXRSServerFactoryBean serverFactory = new JAXRSServerFactoryBean();

        serverFactory.setBus(bus);
        serverFactory.setServiceBeans(List.copyOf(endpoints));
        serverFactory.setFeatures(List.of(openApiFeature));

        return serverFactory.create();
    }

    @Bean
    public ServletRegistrationBean<CXFServlet> cxfServlet() {
        var servletRegistrationBean = new ServletRegistrationBean<>(new CXFServlet(), "/api/*");

        servletRegistrationBean.setName("CXF");
        servletRegistrationBean.setLoadOnStartup(1);

        return servletRegistrationBean;
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    @Primary
    public Bus bus() {
        SpringBus bus = new SpringBus();
        bus.setId("cxf");
        bus.setFeatures(List.of(new LoggingFeature()));
        BusFactory.setDefaultBus(bus);
        return bus;
    }

    @Bean
    public OpenApiFeature openApiFeature() {
        var feature = new OpenApiFeature();

        var customizer = new OpenApiCustomizer();
        customizer.setDynamicBasePath(true);
        feature.setCustomizer(customizer);

        feature.setScanKnownConfigLocations(true);
        feature.setSupportSwaggerUi(true);
        feature.setScan(true);

        return feature;
    }

    @Path("/openapi.yaml")
    @Component
    public static class OpenApiEndpoint implements ApiEndpoint {
        @GET
        @Produces("text/yaml")
        public Response getYaml() throws URISyntaxException, IOException {
            return Response
                    .ok(Files.readString(Paths.get(CxfConfiguration.class.getResource("/openapi.yaml").toURI())))
                    .build();
        }
    }
}
