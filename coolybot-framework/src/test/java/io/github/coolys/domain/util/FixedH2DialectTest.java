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

package io.github.coolys.domain.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Types;
import java.util.*;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.junit.Test;

import io.github.coolys.test.LogbackRecorder;

public class FixedH2DialectTest {

    @Test
    public void test() {
        List<LogbackRecorder> recorders = new LinkedList<>();
        recorders.add(LogbackRecorder.forName("org.jboss.logging").reset().capture("ALL"));
        recorders.add(LogbackRecorder.forClass(Dialect.class).reset().capture("ALL"));
        recorders.add(LogbackRecorder.forClass(H2Dialect.class).reset().capture("ALL"));

        Map<Integer, String> registered = new LinkedHashMap<>();

        new FixedH2Dialect() {

            @Override
            protected void registerColumnType(int code, String name) {
                registered.put(code, name);
                super.registerColumnType(code, name);
            }

        };

        assertThat(registered.get(Types.FLOAT)).isEqualTo("real");

        recorders.forEach(LogbackRecorder::release);
    }

}
