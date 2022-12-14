/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.oap.log.analyzer.dsl.spec;

import groovy.lang.Closure;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.skywalking.apm.network.common.v3.KeyStringValuePair;
import org.apache.skywalking.oap.log.analyzer.dsl.Binding;
import org.apache.skywalking.oap.log.analyzer.provider.LogAnalyzerModuleConfig;
import org.apache.skywalking.oap.server.library.module.ModuleManager;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public abstract class AbstractSpec {
    private final ModuleManager moduleManager;

    private final LogAnalyzerModuleConfig moduleConfig;

    protected static final ThreadLocal<Binding> BINDING = ThreadLocal.withInitial(Binding::new);

    public void bind(final Binding b) {
        BINDING.set(b);
    }

    @SuppressWarnings("unused")
    public void abort(final Closure<Void> cl) {
        BINDING.get().abort();
    }

    @SuppressWarnings("unused")
    public Object propertyMissing(final String name) {
        return BINDING.get().getVariable(name);
    }

    @SuppressWarnings("unused")
    public String tag(String key) {
        return BINDING.get().log().getTags().getDataList()
                .stream()
                .filter(data -> key.equals(data.getKey()))
                .map(KeyStringValuePair::getValue)
                .findFirst()
                .orElse("");
    }
}
