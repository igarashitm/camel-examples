/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.example.salesforce;

import java.io.InputStream;
import java.util.Properties;

import org.apache.camel.CamelContext;
import org.apache.camel.component.salesforce.AuthenticationType;
import org.apache.camel.component.salesforce.SalesforceComponent;
import org.apache.camel.component.salesforce.SalesforceEndpointConfig;
import org.apache.camel.component.salesforce.SalesforceLoginConfig;
import org.apache.camel.impl.DefaultCamelContext;

public final class Application {

    private Application() {
        // noop
    }

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        ClassLoader loader = Application.class.getClassLoader();
        try (InputStream resourceStream = loader.getResourceAsStream("application.properties")) {
            properties.load(resourceStream);
        }
        SalesforceComponent component = new SalesforceComponent();
        component.setPackages("org.apache.camel.example.salesforce");
        SalesforceEndpointConfig config = new SalesforceEndpointConfig();
        component.setConfig(config);
        SalesforceLoginConfig loginConfig = new SalesforceLoginConfig();
        loginConfig.setType(AuthenticationType.REFRESH_TOKEN);
        loginConfig.setClientId(properties.getProperty("clientId"));
        loginConfig.setClientSecret(properties.getProperty("clientSecret"));
        loginConfig.setRefreshToken(properties.getProperty("refreshToken"));
        component.setLoginConfig(loginConfig);
        CamelContext context = new DefaultCamelContext();
        context.addComponent("salesforce", component);
        context.start();

        context.addRoutes(new SalesforceRouteBuilder());
    }
}
