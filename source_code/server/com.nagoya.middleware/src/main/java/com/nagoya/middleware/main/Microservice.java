/**
 * (C) Copyright 2004 - 2018 CPB Software AG
 * 1020 Wien, Vorgartenstrasse 206c
 * All rights reserved.
 * 
 * This software is provided by the copyright holders and contributors "as is". 
 * In no event shall the copyright owner or contributors be liable for any direct,
 * indirect, incidental, special, exemplary, or consequential damages.
 * 
 * Created by : flba
 */

package com.nagoya.middleware.main;

import java.net.URL;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.UserStore;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.nagoya.middleware.rest.ShutdownResource;
import com.nagoya.middleware.rest.filter.CustomContainerResponseFilter;
import com.nagoya.middleware.rest.impl.PingResourceImpl;
import com.nagoya.middleware.rest.impl.ShutdownResourceImpl;

/**
 * 
 * @author Florin Balint
 */
public abstract class Microservice {

    private static final Logger   LOGGER                 = LogManager.getLogger(Microservice.class);

    private static final String   ADMIN_REALM            = "private";
    private static final String   SECURITY_HANDLER_REALM = "myrealm";
    private static final String   ROLE_USER              = "user";

    private ResourceConfig resourceConfig         = new ResourceConfig();

    /**
     * Returns the current resource configuration for the web application. Use this to extend and add your own web services.
     * 
     * @return
     */
    public ResourceConfig getResourceConfig() {
        return resourceConfig;
    }

    public void runServer(String[] args)
        throws Exception {

        // setup the server configuration and register the packages / classes
        resourceConfig.register(JacksonFeature.class);
        resourceConfig.register(CustomContainerResponseFilter.class);
        // register rest resources
        resourceConfig.register(PingResourceImpl.class);
        resourceConfig.register(ShutdownResourceImpl.class);

        ServletHolder jerseyServlet = new ServletHolder(new ServletContainer(resourceConfig));

        Server server = JettyProvider.getInstance().getServer();

        String contextPath = ServerPropertiesProvider.getString(ServerProperty.SERVER_CONTEXT_PATH);
        ServletContextHandler servletContextHandler = new ServletContextHandler(server, contextPath);
        servletContextHandler.addServlet(jerseyServlet, "/*");

        // add filter for the shutdown request
        String adminUsername = ServerPropertiesProvider.getString(ServerProperty.SERVER_ADMIN_USER);
        String adminPassword = ServerPropertiesProvider.getString(ServerProperty.SERVER_ADMIN_PASSWORD);
        servletContextHandler.setSecurityHandler(basicAuth(adminUsername, adminPassword, ADMIN_REALM));

        ResourceHandler webHandler = new ResourceHandler();
        webHandler.setWelcomeFiles(new String[] { "index.html" });
        webHandler.setResourceBase(".");

        // hide server version from response
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        HttpConnectionFactory httpFactory = new HttpConnectionFactory(httpConfig);
        ServerConnector httpConnector = new ServerConnector(server, httpFactory);
        httpConnector.setHost(ServerPropertiesProvider.getString(ServerProperty.SERVER_HOST));
        int port = ServerPropertiesProvider.getInteger(ServerProperty.SERVER_PORT);
        httpConnector.setPort(port);

        // Add the filter, and then use the provided FilterHolder to configure it
        boolean crossOriginEnabled = ServerPropertiesProvider.getBoolean(ServerProperty.SERVER_ENABLE_CROSS_ORIGIN);
        if (crossOriginEnabled) {
            FilterHolder holder = new FilterHolder(CrossOriginFilter.class);
            holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
            holder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
            holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, "Origin, Content-Type, Accept, Authorization");
            holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_EXPOSE_HEADERS_HEADER, "*");
            holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER, "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            holder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With, Content-Type, Accept, Origin, Authorization");
            holder.setName("cross-origin");

            servletContextHandler.addFilter(holder, "*", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));
        }

        // Server
        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(servletContextHandler);
        handlers.addHandler(webHandler);
        server.setHandler(handlers);

        // configure SSL
        ClassLoader classLoader = Microservice.class.getClassLoader();
        URL resource = classLoader.getResource(ServerPropertiesProvider.getString(ServerProperty.SERVER_KEYSTORE));

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(resource.toExternalForm());
        sslContextFactory.setKeyStorePassword(ServerPropertiesProvider.getString(ServerProperty.SERVER_KEYSTORE_PASSWORD));
        sslContextFactory.setKeyManagerPassword(ServerPropertiesProvider.getString(ServerProperty.SERVER_KEYSTORE_PASSWORD));
        sslContextFactory.setEndpointIdentificationAlgorithm("HTTPS");

        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());

        ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"),
            new HttpConnectionFactory(https));
        int httpsPort = ServerPropertiesProvider.getInteger(ServerProperty.SERVER_PORT_HTTPS);
        sslConnector.setPort(httpsPort);

        // set the connectors
        server.setConnectors(new Connector[] { httpConnector, sslConnector });
        // now start the server
        try {
            LOGGER.info("Starting server ...");
            server.start();
            LOGGER.info("Server started successfully on: http://localhost:" + port);
            LOGGER.info("Server started successfully on: https://localhost:" + httpsPort);
            server.join();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error(ex, ex.getCause());
        } finally {
            // server.destroy();
        }

    }

    private static final SecurityHandler basicAuth(String username, String password, String realm) {
        HashLoginService l = new HashLoginService();
        l.setName(realm);
        UserStore userStore = new UserStore();
        userStore.addUser(username, Credential.getCredential(password), new String[] { ROLE_USER });
        l.setUserStore(userStore);

        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[] { ROLE_USER });
        constraint.setAuthenticate(true);

        ConstraintMapping cm = new ConstraintMapping();
        cm.setConstraint(constraint);
        cm.setPathSpec("/" + ShutdownResource.SHUTDOWN_PATH);

        ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
        csh.setAuthenticator(new BasicAuthenticator());
        csh.setRealmName(SECURITY_HANDLER_REALM);
        csh.addConstraintMapping(cm);
        csh.setLoginService(l);

        return csh;
    }

}
