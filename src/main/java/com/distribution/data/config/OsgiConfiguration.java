package com.distribution.data.config;

import com.distribution.data.config.osgi.AutoProcessor;
import com.distribution.data.config.osgi.EmbeddedOSGiServiceProvider;
import io.github.jhipster.config.JHipsterConstants;
import org.apache.felix.framework.util.Util;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Configuration
public class OsgiConfiguration implements BeanPostProcessor {
    public static Logger logger = LoggerFactory.getLogger(OsgiConfiguration.class);

    private Collection<EmbeddedOSGiServiceProvider> embeddedOSGiServiceProviders = new ArrayList<>();

    public static final String CONFIG_PROPERTIES_FILE_VALUE = "config.properties";

    public static final String CONFIG_PROPERTIES_FILE_VALUE_DEV = "config-dev.properties";

    public static final String CONFIG_PROPERTIES_FILE_VALUE_PROD = "config-prod.properties";

    public static final String CONFIG_DIRECTORY = "config";

    private static Framework m_fwk;

    private final Environment env;

    public OsgiConfiguration(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void initialize() {
        this.startFelix();
        this.initializeServiceProviders();
    }

    @PreDestroy
    public void destroy() {
        this.destroyServiceProviders();
        this.stopFelix();
    }

    protected void startFelix() {
        try {
            logger.info("Going to startup embedded OSGi osgi.");
            Map<String, String> configProps = loadConfigProperties();
            configProps.put("gosh.args", "--noshutdown --nointeractive");
            // Create an instance of the framework.
            FrameworkFactory factory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
            m_fwk = factory.newFramework(configProps);
            // Initialize the framework, but don't start it yet.
            m_fwk.init();
            // Use the system bundle context to process the auto-deploy
            // and auto-install/auto-start properties.
            AutoProcessor.process(configProps, m_fwk.getBundleContext());
            m_fwk.start();

            int hashCode = System.identityHashCode(m_fwk);
            logger.info("Embedded OSGi osgi has been started successfully: osgi-hashCode=" + hashCode);
        } catch (Exception e) {
            logger.error(
                "Error while starting embedded OSGi osgi: osgi-hashCode=" + System.identityHashCode(m_fwk), e);
        }
    }

    private void stopFelix() {
        try {
            logger.info("Going to shutdown embedded OSGi osgi: osgi-hashCode=" + System.identityHashCode(m_fwk));

            m_fwk.stop();
            m_fwk.waitForStop(0);

            logger.info(
                "Shutdown of an embedded OSGi osgi completed successfully: osgi-hashCode="
                    + System.identityHashCode(m_fwk));
        } catch (BundleException | InterruptedException e) {
            logger.error(
                "Error while shuting down embedded OSGi osgi: osgi-hashCode=" + System.identityHashCode(m_fwk), e);
        }
    }

    private void destroyServiceProviders() {
        logger.info("Going to destroy " + this.embeddedOSGiServiceProviders.size() + " service provider(s).");

        for (EmbeddedOSGiServiceProvider serviceProvider : this.embeddedOSGiServiceProviders) {
            serviceProvider.destroy();

            logger.info(
                "Destroyed service provider: " + serviceProvider.getClass().getName() + "; service-provider-hashCode="
                    + System.identityHashCode(serviceProvider));
        }
    }

    private void initializeServiceProviders() {
        logger.info("Going to initialize " + this.embeddedOSGiServiceProviders.size() + " service provider(s).");

        for (EmbeddedOSGiServiceProvider serviceProvider : this.embeddedOSGiServiceProviders) {
            serviceProvider.initialize(m_fwk.getBundleContext());

            logger.info(
                "Initialized service provider: " + serviceProvider.getClass().getName() + "; service-provider-hashCode="
                    + System.identityHashCode(serviceProvider));
        }
    }

    public Map<String, String> loadConfigProperties() {
        // Read the properties file.
        Properties props = new Properties();
        InputStream is = null;
        try {
            if (env.acceptsProfiles(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
                is = this.getClass().getClassLoader().getResourceAsStream(CONFIG_DIRECTORY + File.separator + CONFIG_PROPERTIES_FILE_VALUE_PROD);
            }else if (env.acceptsProfiles(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)) {
                is = this.getClass().getClassLoader().getResourceAsStream(CONFIG_DIRECTORY + File.separator + CONFIG_PROPERTIES_FILE_VALUE_DEV);
            }
            if(is == null) {
                is = this.getClass().getClassLoader().getResourceAsStream(CONFIG_DIRECTORY + File.separator + CONFIG_PROPERTIES_FILE_VALUE);
            }
            props.load(is);
            is.close();
        } catch (Exception ex) {
            // Try to close input stream if we have one.
            try {
                if (is != null) is.close();
            } catch (IOException ex2) {
                // Nothing we can do.
            }

            return null;
        }

        // Perform variable substitution for system properties and
        // convert to dictionary.
        Map<String, String> map = new HashMap<>();
        for (Enumeration e = props.propertyNames(); e.hasMoreElements(); ) {
            String name = (String) e.nextElement();
            map.put(name,
                Util.substVars(props.getProperty(name), name, null, props));
        }

        return map;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
        if(bean instanceof EmbeddedOSGiServiceProvider){
            EmbeddedOSGiServiceProvider p = (EmbeddedOSGiServiceProvider) bean;
            logger.info(p.getServiceName());
            p.initialize(m_fwk.getBundleContext());
            this.embeddedOSGiServiceProviders.add(p);
        }
        return bean;
    }
}
