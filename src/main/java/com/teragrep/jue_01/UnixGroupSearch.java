package com.teragrep.jue_01;

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

import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UnixGroupSearch {
    private static final Logger logger = LoggerFactory.getLogger(
            UnixGroupSearch.class);

    private final LoadingCache<String, HashSet<String>> loadingCache;

    public UnixGroupSearch() {
        CacheLoader<String, HashSet<String>> cacheLoader = new CacheLoader<String, HashSet<String>>() {
            @Override
            public HashSet<String> load(String user) throws Exception {
                return getGroupsInternal(user);
            }
        };

        loadingCache = CacheBuilder.newBuilder()
                .maximumSize(30000)
                .expireAfterWrite(3, TimeUnit.HOURS)
                .build(cacheLoader);
    }

    public HashSet<String> getGroups(String user) throws IOException {
        try {
            HashSet<String> groups = loadingCache.get(user);

            // force refresh for no groups
            if (groups.size() == 0) {
                loadingCache.refresh(user);
                groups = loadingCache.get(user);
            }

            return groups;

        } catch (ExecutionException e) {
            throw new IOException(e.getCause());
        }
    }

    public HashSet<String> getGroupsInternal(String user) throws IOException {
        String result = "";
        try {
            result = Shell.execCommand(Shell.getGroupsForUserCommand(user));
        } catch (Shell.ExitCodeException e) {
            logger.info("Unable to find groups for user ["+user+"]");
        }

        StringTokenizer tokenizer = new StringTokenizer(result);
        HashSet<String> groups = new HashSet<>();
        while (tokenizer.hasMoreTokens()) {
            groups.add(tokenizer.nextToken());
        }
        return groups;
    }
}
