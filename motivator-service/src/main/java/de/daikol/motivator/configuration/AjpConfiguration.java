package de.daikol.motivator.configuration;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AjpConfiguration {

    @Value("${tomcat.ajp.port:8009}")
    int ajpPort;

    @Value("${tomcat.ajp.remoteauthentication:false}")
    String remoteAuthentication;

    @Value("${tomcat.ajp.enabled:true}")
    boolean tomcatAjpEnabled;

    public int getAjpPort() {
        return ajpPort;
    }

    public void setAjpPort(int ajpPort) {
        this.ajpPort = ajpPort;
    }

    public String getRemoteAuthentication() {
        return remoteAuthentication;
    }

    public void setRemoteAuthentication(String remoteAuthentication) {
        this.remoteAuthentication = remoteAuthentication;
    }

    public boolean isTomcatAjpEnabled() {
        return tomcatAjpEnabled;
    }

    public void setTomcatAjpEnabled(boolean tomcatAjpEnabled) {
        this.tomcatAjpEnabled = tomcatAjpEnabled;
    }

    @Bean
    public ServletWebServerFactory servletContainer() {

        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        if (tomcatAjpEnabled) {
            Connector ajpConnector = new Connector("AJP/1.3");
            ajpConnector.setPort(ajpPort);
            ajpConnector.setSecure(false);
            ajpConnector.setAllowTrace(false);
            ajpConnector.setScheme("http");
            tomcat.addAdditionalTomcatConnectors(ajpConnector);
        }

        return tomcat;
    }
}
