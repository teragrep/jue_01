package com.teragrep.jue_01.tests;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
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

import com.teragrep.jue_01.UnixGroupSearch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class UnixGroupSearchTest {

    @Test
    public void testGroupSearch() throws IOException {
        UnixGroupSearch unixGroupSearch = new UnixGroupSearch();
        // we don't know which groups are there, but at least one is present
        Assertions.assertNotEquals(0, unixGroupSearch.getGroups(System.getProperty("user.name")).size());
    }
}
