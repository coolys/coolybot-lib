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

package io.github.coolys.web.filter;

import static io.github.coolys.web.filter.CachingHttpHeadersFilter.DEFAULT_DAYS_TO_LIVE;
import static io.github.coolys.web.filter.CachingHttpHeadersFilter.LAST_MODIFIED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;
import javax.servlet.FilterChain;

import org.junit.*;
import org.springframework.mock.web.*;

import io.github.coolys.config.CoolybotProperties;

public class CachingHttpHeadersFilterTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain chain;
    private CoolybotProperties properties;
    private CachingHttpHeadersFilter filter;

    @Before
    public void setup() {
        request = new MockHttpServletRequest();
        response = spy(new MockHttpServletResponse());
        chain = spy(new MockFilterChain());
        properties = new CoolybotProperties();
        filter = new CachingHttpHeadersFilter(properties);
    }

    @After
    public void teardown() {
        filter.destroy();
    }

    @Test
    public void testWithoutInit() {
        int daysToLive = DEFAULT_DAYS_TO_LIVE;
        long secsToLive = TimeUnit.DAYS.toMillis(daysToLive);

        long before = System.currentTimeMillis();
        before -= before % 1000L;

        Throwable caught = catchThrowable(() -> {
            filter.doFilter(request, response, chain);
            verify(chain).doFilter(request, response);
        });

        long after = System.currentTimeMillis();
        after += 1000L - (after % 1000L);

        verify(response).setHeader("Cache-Control", "max-age=" + secsToLive + ", public");
        verify(response).setHeader("Pragma", "cache");
        verify(response).setDateHeader(eq("Expires"), anyLong());
        assertThat(response.getDateHeader("Expires")).isBetween(before + secsToLive, after + secsToLive);
        verify(response).setDateHeader("Last-Modified", LAST_MODIFIED);
        assertThat(caught).isNull();
    }

    @Test
    public void testWithInit() {
        int daysToLive = DEFAULT_DAYS_TO_LIVE >>> 2;
        long secsToLive = TimeUnit.DAYS.toMillis(daysToLive);
        properties.getHttp().getCache().setTimeToLiveInDays(daysToLive);

        long before = System.currentTimeMillis();
        before -= before % 1000L;

        Throwable caught = catchThrowable(() -> {
            filter.init(null);
            filter.doFilter(request, response, chain);
            verify(chain).doFilter(request, response);
        });

        long after = System.currentTimeMillis();
        after += 1000L - (after % 1000L);

        verify(response).setHeader("Cache-Control", "max-age=" + secsToLive + ", public");
        verify(response).setHeader("Pragma", "cache");
        verify(response).setDateHeader(eq("Expires"), anyLong());
        assertThat(response.getDateHeader("Expires")).isBetween(before + secsToLive, after + secsToLive);
        verify(response).setDateHeader("Last-Modified", LAST_MODIFIED);
        assertThat(caught).isNull();
    }
}
