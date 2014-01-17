/**
 * 
 * Copyright (c) Microsoft and contributors.  All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

// Warning: This code was generated by a tool.
// 
// Changes to this file may cause incorrect behavior and will be lost if the
// code is regenerated.

package com.microsoft.windowsazure.management.monitoring.alerts;

import com.microsoft.windowsazure.core.ServiceClient;
import com.microsoft.windowsazure.credentials.SubscriptionCloudCredentials;
import com.microsoft.windowsazure.management.ManagementConfiguration;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.http.impl.client.HttpClientBuilder;

public class AlertsClientImpl extends ServiceClient<AlertsClient> implements AlertsClient
{
    private URI baseUri;
    
    /**
    * Optional base uri parameter for Azure REST.
    */
    public URI getBaseUri() { return this.baseUri; }
    
    private SubscriptionCloudCredentials credentials;
    
    /**
    * When you create a Windows Azure subscription, it is uniquely identified
    * by a subscription ID. The subscription ID forms part of the URI for
    * every call that you make to the Service Management API.  The Windows
    * Azure Service ManagementAPI use mutual authentication of management
    * certificates over SSL to ensure that a request made to the service is
    * secure.  No anonymous requests are allowed.
    */
    public SubscriptionCloudCredentials getCredentials() { return this.credentials; }
    
    private IncidentOperations incidents;
    
    /**
    * Operations for managing the alert incidents.
    */
    public IncidentOperations getIncidentsOperations() { return this.incidents; }
    
    private RuleOperations rules;
    
    /**
    * Operations for managing the alert rules.
    */
    public RuleOperations getRulesOperations() { return this.rules; }
    
    /**
    * Initializes a new instance of the AlertsClientImpl class.
    *
    * @param httpBuilder The HTTP client builder.
    * @param executorService The executor service.
    */
    private AlertsClientImpl(HttpClientBuilder httpBuilder, ExecutorService executorService)
    {
        super(httpBuilder, executorService);
        this.incidents = new IncidentOperationsImpl(this);
        this.rules = new RuleOperationsImpl(this);
    }
    
    /**
    * Initializes a new instance of the AlertsClientImpl class.
    *
    * @param httpBuilder The HTTP client builder.
    * @param executorService The executor service.
    * @param credentials When you create a Windows Azure subscription, it is
    * uniquely identified by a subscription ID. The subscription ID forms part
    * of the URI for every call that you make to the Service Management API.
    * The Windows Azure Service ManagementAPI use mutual authentication of
    * management certificates over SSL to ensure that a request made to the
    * service is secure.  No anonymous requests are allowed.
    * @param baseUri Optional base uri parameter for Azure REST.
    */
    public AlertsClientImpl(HttpClientBuilder httpBuilder, ExecutorService executorService, SubscriptionCloudCredentials credentials, URI baseUri)
    {
        this(httpBuilder, executorService);
        if (credentials == null)
        {
            throw new NullPointerException("credentials");
        }
        if (baseUri == null)
        {
            throw new NullPointerException("baseUri");
        }
        this.credentials = credentials;
        this.baseUri = baseUri;
    }
    
    /**
    * Initializes a new instance of the AlertsClientImpl class.
    * Initializes a new instance of the AlertsClientImpl class.
    *
    * @param httpBuilder The HTTP client builder.
    * @param executorService The executor service.
    * @param credentials When you create a Windows Azure subscription, it is
    * uniquely identified by a subscription ID. The subscription ID forms part
    * of the URI for every call that you make to the Service Management API.
    * The Windows Azure Service ManagementAPI use mutual authentication of
    * management certificates over SSL to ensure that a request made to the
    * service is secure.  No anonymous requests are allowed.
    */
    @Inject
    public AlertsClientImpl(HttpClientBuilder httpBuilder, ExecutorService executorService, @Named(ManagementConfiguration.SUBSCRIPTION_CLOUD_CREDENTIALS) SubscriptionCloudCredentials credentials) throws java.net.URISyntaxException
    {
        this(httpBuilder, executorService);
        if (credentials == null)
        {
            throw new NullPointerException("credentials");
        }
        this.credentials = credentials;
        this.baseUri = new URI("https://management.core.windows.net");
    }
    
    /**
    *
    * @param httpBuilder The HTTP client builder.
    * @param executorService The executor service.
    */
    protected AlertsClientImpl newInstance(HttpClientBuilder httpBuilder, ExecutorService executorService)
    {
        return new AlertsClientImpl(httpBuilder, executorService, this.getCredentials(), this.getBaseUri());
    }
}