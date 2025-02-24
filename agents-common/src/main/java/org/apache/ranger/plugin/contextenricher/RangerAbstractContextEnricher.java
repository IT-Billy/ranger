/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ranger.plugin.contextenricher;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.ranger.authorization.hadoop.config.RangerPluginConfig;
import org.apache.ranger.plugin.model.RangerServiceDef;
import org.apache.ranger.plugin.model.RangerServiceDef.RangerContextEnricherDef;
import org.apache.ranger.plugin.policyengine.RangerAccessRequest;
import org.apache.ranger.plugin.policyengine.RangerPluginContext;
import org.apache.ranger.plugin.policyengine.RangerPolicyEngineOptions;
import org.apache.ranger.plugin.service.RangerAuthContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

public abstract class RangerAbstractContextEnricher implements RangerContextEnricher {
    private static final Logger LOG = LoggerFactory.getLogger(RangerAbstractContextEnricher.class);

    protected RangerContextEnricherDef  enricherDef;
    protected String                    serviceName;
    protected String                    appId;
    protected RangerServiceDef          serviceDef;
    protected RangerPolicyEngineOptions options = new RangerPolicyEngineOptions();
    private   RangerPluginContext       pluginContext;

    public RangerContextEnricherDef getEnricherDef() {
        return enricherDef;
    }

    @Override
    public void setEnricherDef(RangerContextEnricherDef enricherDef) {
        this.enricherDef = enricherDef;
    }

    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public RangerServiceDef getServiceDef() {
        return serviceDef;
    }

    @Override
    public void setServiceDef(RangerServiceDef serviceDef) {
        this.serviceDef = serviceDef;
    }

    public String getAppId() {
        return appId;
    }

    @Override
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public void init() {
        LOG.debug("==> RangerAbstractContextEnricher.init({})", enricherDef);

        RangerAuthContext authContext = getAuthContext();

        if (authContext != null) {
            authContext.addOrReplaceRequestContextEnricher(this, null);
        } else {
            LOG.debug("authContext is null. This context-enricher is not added to authContext");
        }

        LOG.debug("<== RangerAbstractContextEnricher.init({})", enricherDef);
    }

    @Override
    public void enrich(RangerAccessRequest request, Object dataStore) {
        enrich(request);
    }

    @Override
    public boolean preCleanup() {
        LOG.debug("==> RangerAbstractContextEnricher.preCleanup({})", enricherDef);

        RangerAuthContext authContext = getAuthContext();

        if (authContext != null) {
            authContext.cleanupRequestContextEnricher(this);
        } else {
            LOG.debug("authContext is null. AuthContext need not be cleaned.");
        }

        LOG.debug("<== RangerAbstractContextEnricher.preCleanup({})", enricherDef);

        return true;
    }

    @Override
    public void cleanup() {
        preCleanup();
    }

    @Override
    public String getName() {
        return enricherDef == null ? null : enricherDef.getName();
    }

    public String getOption(String name) {
        String ret = null;

        Map<String, String> options = enricherDef != null ? enricherDef.getEnricherOptions() : null;

        if (options != null && name != null) {
            ret = options.get(name);
        }

        return ret;
    }

    public RangerAuthContext getAuthContext() {
        RangerPluginContext pluginContext = this.pluginContext;

        return pluginContext != null ? pluginContext.getAuthContext() : null;
    }

    public RangerPluginContext getPluginContext() {
        return this.pluginContext;
    }

    public final void setPluginContext(RangerPluginContext pluginContext) {
        this.pluginContext = pluginContext;
    }

    public RangerPluginConfig getPluginConfig() {
        RangerPluginContext pluginContext = this.pluginContext;

        return pluginContext != null ? pluginContext.getConfig() : null;
    }

    public RangerPolicyEngineOptions getPolicyEngineOptions() {
        return options;
    }

    public final void setPolicyEngineOptions(RangerPolicyEngineOptions options) {
        this.options = options;
    }

    public void notifyAuthContextChanged() {
        RangerPluginContext pluginContext = this.pluginContext;

        if (pluginContext != null) {
            pluginContext.notifyAuthContextChanged();
        }
    }

    public String getPropertyPrefix() {
        RangerPluginConfig pluginConfig = getPluginConfig();

        return pluginConfig != null ? pluginConfig.getPropertyPrefix() : "ranger.plugin." + serviceDef.getName();
    }

    public String getConfig(String configName, String defaultValue) {
        RangerPluginContext pluginContext = this.pluginContext;
        String              ret           = defaultValue;
        Configuration       config        = pluginContext != null ? pluginContext.getConfig() : null;

        if (config != null) {
            ret = config.get(configName, defaultValue);
        }

        return ret;
    }

    public int getIntConfig(String configName, int defaultValue) {
        RangerPluginContext pluginContext = this.pluginContext;
        int                 ret           = defaultValue;
        Configuration       config        = pluginContext != null ? pluginContext.getConfig() : null;

        if (config != null) {
            ret = config.getInt(configName, defaultValue);
        }

        return ret;
    }

    public boolean getBooleanConfig(String configName, boolean defaultValue) {
        RangerPluginContext pluginContext = this.pluginContext;
        boolean             ret           = defaultValue;
        Configuration       config        = pluginContext != null ? pluginContext.getConfig() : null;

        if (config != null) {
            ret = config.getBoolean(configName, defaultValue);
        }

        return ret;
    }

    public String getOption(String name, String defaultValue) {
        String ret = defaultValue;
        String val = getOption(name);

        if (val != null) {
            ret = val;
        }

        return ret;
    }

    public boolean getBooleanOption(String name, boolean defaultValue) {
        boolean ret = defaultValue;
        String  val = getOption(name);

        if (val != null) {
            ret = Boolean.parseBoolean(val);
        }

        return ret;
    }

    public char getCharOption(String name, char defaultValue) {
        String val = getOption(name);

        return !StringUtils.isEmpty(val) ? val.charAt(0) : defaultValue;
    }

    public long getLongOption(String name, long defaultValue) {
        String val = getOption(name);

        return val != null ? Long.parseLong(val) : defaultValue;
    }

    public Properties readProperties(String fileName) {
        Properties ret     = null;
        URL        fileURL = null;
        File       f       = new File(fileName);

        if (f.exists() && f.isFile() && f.canRead()) {
            try (InputStream inStr = new FileInputStream(f)) {
                fileURL = f.toURI().toURL();
            } catch (FileNotFoundException exception) {
                LOG.error("Error processing input file:{} or no privilege for reading file {}", fileName, fileName, exception);
            } catch (IOException malformedException) {
                LOG.error("Error processing input file:{} cannot be converted to URL {}", fileName, fileName, malformedException);
            }
        } else {
            fileURL = getClass().getResource(fileName);

            if (fileURL == null && !fileName.startsWith("/")) {
                fileURL = getClass().getResource("/" + fileName);
            }

            if (fileURL == null) {
                fileURL = ClassLoader.getSystemClassLoader().getResource(fileName);

                if (fileURL == null && !fileName.startsWith("/")) {
                    fileURL = ClassLoader.getSystemClassLoader().getResource("/" + fileName);
                }
            }
        }

        if (fileURL != null) {
            try (InputStream inStr = fileURL.openStream()) {
                Properties prop = new Properties();

                prop.load(inStr);

                ret = prop;
            } catch (Exception excp) {
                LOG.error("failed to load properties from file '{}'", fileName, excp);
            }
        }

        return ret;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            cleanup();
        } finally {
            super.finalize();
        }
    }
}
