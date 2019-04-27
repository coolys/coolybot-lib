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

package io.github.coolys.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.lang.reflect.Method;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class CoolybotPropertiesTest {

    private CoolybotProperties properties;

    @Before
    public void setup() {
        properties = new CoolybotProperties();
    }

    @Test
    public void testComplete() throws Exception {
        // Slightly pedantic; this checks if there are tests for each of the properties.
        Set<String> set = new LinkedHashSet<>(64, 1F);
        reflect(properties, set, "test");
        for (String name : set) {
            this.getClass().getDeclaredMethod(name);
        }
    }

    private void reflect(Object obj, Set<String> dst, String prefix) throws Exception {
        Class<?> src = obj.getClass();
        for (Method method : src.getDeclaredMethods()) {
            String name = method.getName();
            if (name.startsWith("get")) {
                Object res = method.invoke(obj, (Object[]) null);
                if (res != null && src.equals(res.getClass().getDeclaringClass())) {
                    reflect(res, dst, prefix + name.substring(3));
                }
            } else if (name.startsWith("set")) {
                dst.add(prefix + name.substring(3));
            }
        }
    }

    @Test
    public void testAsyncCorePoolSize() {
        CoolybotProperties.Async obj = properties.getAsync();
        int val = CoolybotDefaults.Async.corePoolSize;
        assertThat(obj.getCorePoolSize()).isEqualTo(val);
        val++;
        obj.setCorePoolSize(val);
        assertThat(obj.getCorePoolSize()).isEqualTo(val);
    }

    @Test
    public void testAsyncMaxPoolSize() {
        CoolybotProperties.Async obj = properties.getAsync();
        int val = CoolybotDefaults.Async.maxPoolSize;
        assertThat(obj.getMaxPoolSize()).isEqualTo(val);
        val++;
        obj.setMaxPoolSize(val);
        assertThat(obj.getMaxPoolSize()).isEqualTo(val);
    }

    @Test
    public void testAsyncQueueCapacity() {
        CoolybotProperties.Async obj = properties.getAsync();
        int val = CoolybotDefaults.Async.queueCapacity;
        assertThat(obj.getQueueCapacity()).isEqualTo(val);
        val++;
        obj.setQueueCapacity(val);
        assertThat(obj.getQueueCapacity()).isEqualTo(val);
    }

    @Test
    public void testHttpVersion() {
        CoolybotProperties.Http.Version[] versions = CoolybotProperties.Http.Version.values();
        CoolybotProperties.Http obj = properties.getHttp();
        String str = CoolybotDefaults.Http.version.toString();
        CoolybotProperties.Http.Version val = CoolybotProperties.Http.Version.valueOf(str);
        assertThat(obj.getVersion()).isEqualTo(val);
        val = versions[(1 + val.ordinal()) % versions.length];
        obj.setVersion(val);
        assertThat(obj.getVersion()).isEqualTo(val);
    }

    @Test
    public void testHttpCacheTimeToLiveInDays() {
        CoolybotProperties.Http.Cache obj = properties.getHttp().getCache();
        int val = CoolybotDefaults.Http.Cache.timeToLiveInDays;
        assertThat(obj.getTimeToLiveInDays()).isEqualTo(val);
        val++;
        obj.setTimeToLiveInDays(val);
        assertThat(obj.getTimeToLiveInDays()).isEqualTo(val);
    }

    @Test
    public void testCacheHazelcastTimeToLiveSeconds() {
        CoolybotProperties.Cache.Hazelcast obj = properties.getCache().getHazelcast();
        int val = CoolybotDefaults.Cache.Hazelcast.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    public void testCacheHazelcastBackupCount() {
        CoolybotProperties.Cache.Hazelcast obj = properties.getCache().getHazelcast();
        int val = CoolybotDefaults.Cache.Hazelcast.backupCount;
        assertThat(obj.getBackupCount()).isEqualTo(val);
        val++;
        obj.setBackupCount(val);
        assertThat(obj.getBackupCount()).isEqualTo(val);
    }

    @Test
    public void testCacheHazelcastManagementCenterEnabled() {
        CoolybotProperties.Cache.Hazelcast.ManagementCenter obj =
            properties.getCache().getHazelcast().getManagementCenter();

        boolean val = CoolybotDefaults.Cache.Hazelcast.ManagementCenter.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = !val;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    public void testCacheHazelcastManagementCenterUpdateInterval() {
        CoolybotProperties.Cache.Hazelcast.ManagementCenter obj =
            properties.getCache().getHazelcast().getManagementCenter();

        int val = CoolybotDefaults.Cache.Hazelcast.ManagementCenter.updateInterval;
        assertThat(obj.getUpdateInterval()).isEqualTo(val);
        val++;
        obj.setUpdateInterval(val);
        assertThat(obj.getUpdateInterval()).isEqualTo(val);
    }

    @Test
    public void testCacheHazelcastManagementCenterUrl() {
        CoolybotProperties.Cache.Hazelcast.ManagementCenter obj =
            properties.getCache().getHazelcast().getManagementCenter();

        String val = CoolybotDefaults.Cache.Hazelcast.ManagementCenter.url;
        assertThat(obj.getUrl()).isEqualTo(val);
        val = "http://localhost:8080";
        obj.setUrl(val);
        assertThat(obj.getUrl()).isEqualTo(val);
    }

    @Test
    public void testCacheEhcacheTimeToLiveSeconds() {
        CoolybotProperties.Cache.Ehcache obj = properties.getCache().getEhcache();
        int val = CoolybotDefaults.Cache.Ehcache.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    public void testCacheEhcacheMaxEntries() {
        CoolybotProperties.Cache.Ehcache obj = properties.getCache().getEhcache();
        long val = CoolybotDefaults.Cache.Ehcache.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanConfigFile() {
        CoolybotProperties.Cache.Infinispan obj = properties.getCache().getInfinispan();
        String val = CoolybotDefaults.Cache.Infinispan.configFile;
        assertThat(obj.getConfigFile()).isEqualTo(val);
        val = "1" + val;
        obj.setConfigFile(val);
        assertThat(obj.getConfigFile()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanStatsEnabled() {
        CoolybotProperties.Cache.Infinispan obj = properties.getCache().getInfinispan();
        boolean val = CoolybotDefaults.Cache.Infinispan.statsEnabled;
        assertThat(obj.isStatsEnabled()).isEqualTo(val);
        val = !val;
        obj.setStatsEnabled(val);
        assertThat(obj.isStatsEnabled()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanLocalTimeToLiveSeconds() {
        CoolybotProperties.Cache.Infinispan.Local obj = properties.getCache().getInfinispan().getLocal();
        long val = CoolybotDefaults.Cache.Infinispan.Local.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanLocalMaxEntries() {
        CoolybotProperties.Cache.Infinispan.Local obj = properties.getCache().getInfinispan().getLocal();
        long val = CoolybotDefaults.Cache.Infinispan.Local.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanDistributedTimeToLiveSeconds() {
        CoolybotProperties.Cache.Infinispan.Distributed obj = properties.getCache().getInfinispan().getDistributed();
        long val = CoolybotDefaults.Cache.Infinispan.Distributed.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanDistributedMaxEntries() {
        CoolybotProperties.Cache.Infinispan.Distributed obj = properties.getCache().getInfinispan().getDistributed();
        long val = CoolybotDefaults.Cache.Infinispan.Distributed.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanDistributedInstanceCount() {
        CoolybotProperties.Cache.Infinispan.Distributed obj = properties.getCache().getInfinispan().getDistributed();
        int val = CoolybotDefaults.Cache.Infinispan.Distributed.instanceCount;
        assertThat(obj.getInstanceCount()).isEqualTo(val);
        val++;
        obj.setInstanceCount(val);
        assertThat(obj.getInstanceCount()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanReplicatedTimeToLiveSeconds() {
        CoolybotProperties.Cache.Infinispan.Replicated obj = properties.getCache().getInfinispan().getReplicated();
        long val = CoolybotDefaults.Cache.Infinispan.Replicated.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanReplicatedMaxEntries() {
        CoolybotProperties.Cache.Infinispan.Replicated obj = properties.getCache().getInfinispan().getReplicated();
        long val = CoolybotDefaults.Cache.Infinispan.Replicated.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    public void testCacheMemcachedEnabled() {
        CoolybotProperties.Cache.Memcached obj = properties.getCache().getMemcached();
        boolean val = CoolybotDefaults.Cache.Memcached.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = true;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    public void testCacheMemcachedServers() {
        CoolybotProperties.Cache.Memcached obj = properties.getCache().getMemcached();
        String val = CoolybotDefaults.Cache.Memcached.servers;
        assertThat(obj.getServers()).isEqualTo(val);
        val = "myserver:1337";
        obj.setServers(val);
        assertThat(obj.getServers()).isEqualTo(val);
    }

    @Test
    public void testCacheMemcachedExpiration() {
        CoolybotProperties.Cache.Memcached obj = properties.getCache().getMemcached();
        int val = CoolybotDefaults.Cache.Memcached.expiration;
        assertThat(obj.getExpiration()).isEqualTo(val);
        val++;
        obj.setExpiration(val);
        assertThat(obj.getExpiration()).isEqualTo(val);
    }

    @Test
    public void testCacheMemcachedUseBinaryProtocol() {
        CoolybotProperties.Cache.Memcached obj = properties.getCache().getMemcached();
        boolean val = CoolybotDefaults.Cache.Memcached.useBinaryProtocol;
        assertThat(obj.isUseBinaryProtocol()).isEqualTo(val);
        val = false;
        obj.setUseBinaryProtocol(val);
        assertThat(obj.isUseBinaryProtocol()).isEqualTo(val);
    }

    @Test
    public void testMailFrom() {
        CoolybotProperties.Mail obj = properties.getMail();
        String val = CoolybotDefaults.Mail.from;
        assertThat(obj.getFrom()).isEqualTo(val);
        val = "1" + val;
        obj.setFrom(val);
        assertThat(obj.getFrom()).isEqualTo(val);
    }

    @Test
    public void testMailBaseUrl() {
        CoolybotProperties.Mail obj = properties.getMail();
        String val = CoolybotDefaults.Mail.baseUrl;
        assertThat(obj.getBaseUrl()).isEqualTo(val);
        val = "1" + val;
        obj.setBaseUrl(val);
        assertThat(obj.getBaseUrl()).isEqualTo(val);
    }

    @Test
    public void testMailEnabled() {
        CoolybotProperties.Mail obj = properties.getMail();
        boolean val = CoolybotDefaults.Mail.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = !val;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    public void testSecurityClientAuthorizationAccessTokenUri() {
        CoolybotProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String val = CoolybotDefaults.Security.ClientAuthorization.accessTokenUri;
        assertThat(obj.getAccessTokenUri()).isEqualTo(val);
        val = "1" + val;
        obj.setAccessTokenUri(val);
        assertThat(obj.getAccessTokenUri()).isEqualTo(val);
    }

    @Test
    public void testSecurityClientAuthorizationTokenServiceId() {
        CoolybotProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String val = CoolybotDefaults.Security.ClientAuthorization.tokenServiceId;
        assertThat(obj.getTokenServiceId()).isEqualTo(val);
        val = "1" + val;
        obj.setTokenServiceId(val);
        assertThat(obj.getTokenServiceId()).isEqualTo(val);
    }

    @Test
    public void testSecurityClientAuthorizationClientId() {
        CoolybotProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String val = CoolybotDefaults.Security.ClientAuthorization.clientId;
        assertThat(obj.getClientId()).isEqualTo(val);
        val = "1" + val;
        obj.setClientId(val);
        assertThat(obj.getClientId()).isEqualTo(val);
    }

    @Test
    public void testSecurityClientAuthorizationClientSecret() {
        CoolybotProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String val = CoolybotDefaults.Security.ClientAuthorization.clientSecret;
        assertThat(obj.getClientSecret()).isEqualTo(val);
        val = "1" + val;
        obj.setClientSecret(val);
        assertThat(obj.getClientSecret()).isEqualTo(val);
    }

    @Test
    public void testSecurityAuthenticationJwtSecret() {
        CoolybotProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        String val = CoolybotDefaults.Security.Authentication.Jwt.secret;
        assertThat(obj.getSecret()).isEqualTo(val);
        val = "1" + val;
        obj.setSecret(val);
        assertThat(obj.getSecret()).isEqualTo(val);
    }

    @Test
    public void testSecurityAuthenticationJwtBase64Secret() {
        CoolybotProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        String val = CoolybotDefaults.Security.Authentication.Jwt.base64Secret;
        assertThat(obj.getSecret()).isEqualTo(val);
        val = "1" + val;
        obj.setBase64Secret(val);
        assertThat(obj.getBase64Secret()).isEqualTo(val);
    }

    @Test
    public void testSecurityAuthenticationJwtTokenValidityInSeconds() {
        CoolybotProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        long val = CoolybotDefaults.Security.Authentication.Jwt.tokenValidityInSeconds;
        assertThat(obj.getTokenValidityInSeconds()).isEqualTo(val);
        val++;
        obj.setTokenValidityInSeconds(val);
        assertThat(obj.getTokenValidityInSeconds()).isEqualTo(val);
    }

    @Test
    public void testSecurityAuthenticationJwtTokenValidityInSecondsForRememberMe() {
        CoolybotProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        long val = CoolybotDefaults.Security.Authentication.Jwt.tokenValidityInSecondsForRememberMe;
        assertThat(obj.getTokenValidityInSecondsForRememberMe()).isEqualTo(val);
        val++;
        obj.setTokenValidityInSecondsForRememberMe(val);
        assertThat(obj.getTokenValidityInSecondsForRememberMe()).isEqualTo(val);
    }

    @Test
    public void testSecurityRememberMeKey() {
        CoolybotProperties.Security.RememberMe obj = properties.getSecurity().getRememberMe();
        String val = CoolybotDefaults.Security.RememberMe.key;
        assertThat(obj.getKey()).isEqualTo(val);
        val = "1" + val;
        obj.setKey(val);
        assertThat(obj.getKey()).isEqualTo(val);
    }

    @Test
    public void testSwaggerTitle() {
        CoolybotProperties.Swagger obj = properties.getSwagger();
        String val = CoolybotDefaults.Swagger.title;
        assertThat(obj.getTitle()).isEqualTo(val);
        val = "1" + val;
        obj.setTitle(val);
        assertThat(obj.getTitle()).isEqualTo(val);
    }

    @Test
    public void testSwaggerDescription() {
        CoolybotProperties.Swagger obj = properties.getSwagger();
        String val = CoolybotDefaults.Swagger.description;
        assertThat(obj.getDescription()).isEqualTo(val);
        val = "1" + val;
        obj.setDescription(val);
        assertThat(obj.getDescription()).isEqualTo(val);
    }

    @Test
    public void testSwaggerVersion() {
        CoolybotProperties.Swagger obj = properties.getSwagger();
        String val = CoolybotDefaults.Swagger.version;
        assertThat(obj.getVersion()).isEqualTo(val);
        val = "1" + val;
        obj.setVersion(val);
        assertThat(obj.getVersion()).isEqualTo(val);
    }

    @Test
    public void testSwaggerTermsOfServiceUrl() {
        CoolybotProperties.Swagger obj = properties.getSwagger();
        String val = CoolybotDefaults.Swagger.termsOfServiceUrl;
        assertThat(obj.getTermsOfServiceUrl()).isEqualTo(val);
        val = "1" + val;
        obj.setTermsOfServiceUrl(val);
        assertThat(obj.getTermsOfServiceUrl()).isEqualTo(val);
    }

    @Test
    public void testSwaggerContactName() {
        CoolybotProperties.Swagger obj = properties.getSwagger();
        String val = CoolybotDefaults.Swagger.contactName;
        assertThat(obj.getContactName()).isEqualTo(val);
        val = "1" + val;
        obj.setContactName(val);
        assertThat(obj.getContactName()).isEqualTo(val);
    }

    @Test
    public void testSwaggerContactUrl() {
        CoolybotProperties.Swagger obj = properties.getSwagger();
        String val = CoolybotDefaults.Swagger.contactUrl;
        assertThat(obj.getContactUrl()).isEqualTo(val);
        val = "1" + val;
        obj.setContactUrl(val);
        assertThat(obj.getContactUrl()).isEqualTo(val);
    }

    @Test
    public void testSwaggerContactEmail() {
        CoolybotProperties.Swagger obj = properties.getSwagger();
        String val = CoolybotDefaults.Swagger.contactEmail;
        assertThat(obj.getContactEmail()).isEqualTo(val);
        val = "1" + val;
        obj.setContactEmail(val);
        assertThat(obj.getContactEmail()).isEqualTo(val);
    }

    @Test
    public void testSwaggerLicense() {
        CoolybotProperties.Swagger obj = properties.getSwagger();
        String val = CoolybotDefaults.Swagger.license;
        assertThat(obj.getLicense()).isEqualTo(val);
        val = "1" + val;
        obj.setLicense(val);
        assertThat(obj.getLicense()).isEqualTo(val);
    }

    @Test
    public void testSwaggerLicenseUrl() {
        CoolybotProperties.Swagger obj = properties.getSwagger();
        String val = CoolybotDefaults.Swagger.licenseUrl;
        assertThat(obj.getLicenseUrl()).isEqualTo(val);
        val = "1" + val;
        obj.setLicenseUrl(val);
        assertThat(obj.getLicenseUrl()).isEqualTo(val);
    }

    @Test
    public void testSwaggerDefaultIncludePattern() {
        CoolybotProperties.Swagger obj = properties.getSwagger();
        String val = CoolybotDefaults.Swagger.defaultIncludePattern;
        assertThat(obj.getDefaultIncludePattern()).isEqualTo(val);
        val = "1" + val;
        obj.setDefaultIncludePattern(val);
        assertThat(obj.getDefaultIncludePattern()).isEqualTo(val);
    }

    @Test
    public void testSwaggerHost() {
        CoolybotProperties.Swagger obj = properties.getSwagger();
        String val = CoolybotDefaults.Swagger.host;
        assertThat(obj.getHost()).isEqualTo(val);
        val = "1" + val;
        obj.setHost(val);
        assertThat(obj.getHost()).isEqualTo(val);
    }

    @Test
    public void testSwaggerProtocols() {
        CoolybotProperties.Swagger obj = properties.getSwagger();
        String[] def = CoolybotDefaults.Swagger.protocols;
        ArrayList<String> val;
        if (def != null) {
            val = newArrayList(def);
            assertThat(obj.getProtocols()).containsExactlyElementsOf(newArrayList(val));
        } else {
            assertThat(obj.getProtocols()).isNull();
            def = new String[1];
            val = new ArrayList<>(1);
        }
        val.add("1");
        obj.setProtocols(val.toArray(def));
        assertThat(obj.getProtocols()).containsExactlyElementsOf(newArrayList(val));
    }

    @Test
    public void testSwaggerUseDefaultResponseMessages() {
        CoolybotProperties.Swagger obj = properties.getSwagger();
        boolean val = CoolybotDefaults.Swagger.useDefaultResponseMessages;
        assertThat(obj.isUseDefaultResponseMessages()).isEqualTo(val);
        val = false;
        obj.setUseDefaultResponseMessages(val);
        assertThat(obj.isUseDefaultResponseMessages()).isEqualTo(val);
    }

    @Test
    public void testMetricsLogsEnabled() {
        CoolybotProperties.Metrics.Logs obj = properties.getMetrics().getLogs();
        boolean val = CoolybotDefaults.Metrics.Logs.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = !val;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    public void testMetricsLogsReportFrequency() {
        CoolybotProperties.Metrics.Logs obj = properties.getMetrics().getLogs();
        long val = CoolybotDefaults.Metrics.Logs.reportFrequency;
        assertThat(obj.getReportFrequency()).isEqualTo(val);
        val++;
        obj.setReportFrequency(val);
        assertThat(obj.getReportFrequency()).isEqualTo(val);
    }

    @Test
    public void testLoggingLogstashEnabled() {
        CoolybotProperties.Logging.Logstash obj = properties.getLogging().getLogstash();
        boolean val = CoolybotDefaults.Logging.Logstash.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = !val;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    public void testLoggingLogstashHost() {
        CoolybotProperties.Logging.Logstash obj = properties.getLogging().getLogstash();
        String val = CoolybotDefaults.Logging.Logstash.host;
        assertThat(obj.getHost()).isEqualTo(val);
        val = "1" + val;
        obj.setHost(val);
        assertThat(obj.getHost()).isEqualTo(val);
    }

    @Test
    public void testLoggingLogstashPort() {
        CoolybotProperties.Logging.Logstash obj = properties.getLogging().getLogstash();
        int val = CoolybotDefaults.Logging.Logstash.port;
        assertThat(obj.getPort()).isEqualTo(val);
        val++;
        obj.setPort(val);
        assertThat(obj.getPort()).isEqualTo(val);
    }

    @Test
    public void testLoggingLogstashQueueSize() {
        CoolybotProperties.Logging.Logstash obj = properties.getLogging().getLogstash();
        int val = CoolybotDefaults.Logging.Logstash.queueSize;
        assertThat(obj.getQueueSize()).isEqualTo(val);
        val++;
        obj.setQueueSize(val);
        assertThat(obj.getQueueSize()).isEqualTo(val);
    }

    @Test
    public void testSocialRedirectAfterSignIn() {
        CoolybotProperties.Social obj = properties.getSocial();
        String val = CoolybotDefaults.Social.redirectAfterSignIn;
        assertThat(obj.getRedirectAfterSignIn()).isEqualTo(val);
        val = "1" + val;
        obj.setRedirectAfterSignIn(val);
        assertThat(obj.getRedirectAfterSignIn()).isEqualTo(val);
    }

    @Test
    public void testGatewayAuthorizedMicroservicesEndpoints() {
        CoolybotProperties.Gateway obj = properties.getGateway();
        Map<String, List<String>> val = CoolybotDefaults.Gateway.authorizedMicroservicesEndpoints;
        assertThat(obj.getAuthorizedMicroservicesEndpoints()).isEqualTo(val);
        val.put("1", null);
        obj.setAuthorizedMicroservicesEndpoints(val);
        assertThat(obj.getAuthorizedMicroservicesEndpoints()).isEqualTo(val);
    }

    @Test
    public void testGatewayRateLimitingEnabled() {
        CoolybotProperties.Gateway.RateLimiting obj = properties.getGateway().getRateLimiting();
        boolean val = CoolybotDefaults.Gateway.RateLimiting.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = !val;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    public void testGatewayRateLimitingLimit() {
        CoolybotProperties.Gateway.RateLimiting obj = properties.getGateway().getRateLimiting();
        long val = CoolybotDefaults.Gateway.RateLimiting.limit;
        assertThat(obj.getLimit()).isEqualTo(val);
        val++;
        obj.setLimit(val);
        assertThat(obj.getLimit()).isEqualTo(val);
    }

    @Test
    public void testGatewayRateLimitingDurationInSeconds() {
        CoolybotProperties.Gateway.RateLimiting obj = properties.getGateway().getRateLimiting();
        int val = CoolybotDefaults.Gateway.RateLimiting.durationInSeconds;
        assertThat(obj.getDurationInSeconds()).isEqualTo(val);
        val++;
        obj.setDurationInSeconds(val);
        assertThat(obj.getDurationInSeconds()).isEqualTo(val);
    }

    @Test
    public void testRegistryPassword() {
        CoolybotProperties.Registry obj = properties.getRegistry();
        String val = CoolybotDefaults.Registry.password;
        assertThat(obj.getPassword()).isEqualTo(val);
        val = "1" + val;
        obj.setPassword(val);
        assertThat(obj.getPassword()).isEqualTo(val);
    }

    @Test
    public void testHttpUseUndertowUserCipherSuitesOrder(){
        CoolybotProperties.Http obj = properties.getHttp();
        boolean val = CoolybotDefaults.Http.useUndertowUserCipherSuitesOrder;
        assertThat(obj.isUseUndertowUserCipherSuitesOrder()).isEqualTo(val);
        val = !val;
        obj.setUseUndertowUserCipherSuitesOrder(val);
        assertThat(obj.isUseUndertowUserCipherSuitesOrder()).isEqualTo(val);
    }
}
