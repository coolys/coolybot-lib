/*
 * Copyright 2016-2019 the original author or authors from the Coolybot project.
 *
 * This file is part of the Coolybot project, see https://www.coolybot.tech/
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.coolys.config.apidoc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.data.domain.Pageable;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.schema.JacksonEnumTypeDeterminer;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.TypeNameProviderPlugin;
import springfox.documentation.spi.service.contexts.*;
import springfox.documentation.spring.web.WebMvcRequestHandler;
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver;

/**
 * Unit tests for io.github.coolys.config.apidoc.PageableParameterBuilderPlugin.
 *
 * @see PageableParameterBuilderPlugin
 */
public class PageableParameterBuilderPluginTest {

    private OperationBuilder builder;
    private OperationContext context;
    private TypeResolver resolver;
    private TypeNameExtractor extractor;
    private PageableParameterBuilderPlugin plugin;

    @Captor
    private ArgumentCaptor<List<Parameter>> captor;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        Method method = this.getClass().getMethod("test", new Class<?>[] { Pageable.class, Integer.class });
        resolver = new TypeResolver();
        RequestHandler handler = new WebMvcRequestHandler(new HandlerMethodResolver(resolver), null, new
            HandlerMethod(this, method));
        DocumentationContext docContext = mock(DocumentationContext.class);
        RequestMappingContext reqContext = new RequestMappingContext(docContext, handler);
        builder = spy(new OperationBuilder(null));
        context = new OperationContext(builder, RequestMethod.GET, reqContext, 0);
        List<TypeNameProviderPlugin> plugins = new LinkedList<>();
        extractor = new TypeNameExtractor(resolver, SimplePluginRegistry.create(plugins), new
            JacksonEnumTypeDeterminer());
        plugin = new PageableParameterBuilderPlugin(extractor, resolver);
    }

    @Test
    public void testConstructor() {
        assertThat(plugin.getResolver()).isEqualTo(resolver);
        assertThat(plugin.getNameExtractor()).isEqualTo(extractor);
    }

    @Test
    public void testSupports() {
        boolean test1 = plugin.supports(DocumentationType.SWAGGER_12);
        assertThat(test1).isEqualTo(false);
        boolean test2 = plugin.supports(DocumentationType.SWAGGER_2);
        assertThat(test2).isEqualTo(true);
    }

    @Test
    public void testApply() {
        plugin.apply(context);
        verify(builder).parameters(captor.capture());

        List<Parameter> parameters = captor.getValue();
        assertThat(parameters).hasSize(3);

        Parameter parameter0 = parameters.get(0);
        assertThat(parameter0.getParamType()).isEqualTo(PageableParameterBuilderPlugin.PAGE_TYPE);
        assertThat(parameter0.getName()).isEqualTo(PageableParameterBuilderPlugin.DEFAULT_PAGE_NAME);
        assertThat(parameter0.getDescription()).isEqualTo(PageableParameterBuilderPlugin.PAGE_DESCRIPTION);
        assertThat(parameter0.getModelRef().getType()).isEqualTo("int");
        assertThat(parameter0.isAllowMultiple()).isEqualTo(false);

        Parameter parameter1 = parameters.get(1);
        assertThat(parameter1.getParamType()).isEqualTo(PageableParameterBuilderPlugin.SIZE_TYPE);
        assertThat(parameter1.getName()).isEqualTo(PageableParameterBuilderPlugin.DEFAULT_SIZE_NAME);
        assertThat(parameter1.getDescription()).isEqualTo(PageableParameterBuilderPlugin.SIZE_DESCRIPTION);
        assertThat(parameter1.getModelRef().getType()).isEqualTo("int");
        assertThat(parameter1.isAllowMultiple()).isEqualTo(false);

        Parameter parameter2 = parameters.get(2);
        assertThat(parameter2.getParamType()).isEqualTo(PageableParameterBuilderPlugin.SORT_TYPE);
        assertThat(parameter2.getName()).isEqualTo(PageableParameterBuilderPlugin.DEFAULT_SORT_NAME);
        assertThat(parameter2.getDescription()).isEqualTo(PageableParameterBuilderPlugin.SORT_DESCRIPTION);
        assertThat(parameter2.getModelRef().getType()).isEqualTo("List");
        assertThat(parameter2.getModelRef().getItemType()).isEqualTo("string");
        assertThat(parameter2.isAllowMultiple()).isEqualTo(true);
    }

    public void test(Pageable yes, Integer no) {
        // noop
    }
}
