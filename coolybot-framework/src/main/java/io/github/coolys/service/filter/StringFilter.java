/*
 * Copyright 2016-2019 the original author or authors.
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

package io.github.coolys.service.filter;

import java.util.Objects;

/**
 * Class for filtering attributes with {@link String} type.
 * It can be added to a criteria class as a member, to support the following query parameters:
 * <code>
 * fieldName.equals='something'
 * fieldName.specified=true
 * fieldName.specified=false
 * fieldName.in='something','other'
 * fieldName.contains='thing'
 * </code>
 */
public class StringFilter extends Filter<String> {

    private static final long serialVersionUID = 1L;

    private String contains;

    public String getContains() {
        return contains;
    }

    public StringFilter setContains(String contains) {
        this.contains = contains;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final StringFilter that = (StringFilter) o;
        return Objects.equals(contains, that.contains);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contains);
    }

    @Override
    public String toString() {
        return getFilterName() + " ["
            + (getContains() != null ? "contains=" + getContains() + ", " : "")
            + (getEquals() != null ? "equals=" + getEquals() + ", " : "")
            + (getSpecified() != null ? "specified=" + getSpecified() : "")
            + "]";
    }

}
