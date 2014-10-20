/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.marmotta.extension.services;

import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.marmotta.extension.api.MyService;

/**
 * Default Implementation of {@link MyService}
 */
@ApplicationScoped
public class MyServiceImpl implements MyService {

    @Inject
    private Logger log;

    @Override
    public void doThis(int i) {
        log.debug("Doing that for {} times...", i);
        for (int j = 0; j < i; j++) {
            doThat();
        }
        log.debug("Did this.");
    }

    @Override
    public void doThat() {
        log.debug("Doing THAT");
    }

    @Override
    public String helloWorld(String name) {
        log.debug("Greeting {}", name);
        return "Hello " + name;
    }

}
