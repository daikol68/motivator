package de.daikol.motivator.security;

import org.springframework.beans.factory.annotation.Value;

public class JwtConfig {

    public static final String CLAIM_AUTHORITIES = "authorities";

    @Value("${security.jwt.uri:/auth/login}")
    private String Uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:DAIKOL }")
    private String prefix;

    @Value("${security.jwt.expiration:#{24*60*60*1000}}")
    private int expiration;

    @Value("${security.jwt.secret:g7o78@8Y$jGbF7hi&uMqjq%J8h8f4du3hB#12tv^}")
    private String secret;

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
