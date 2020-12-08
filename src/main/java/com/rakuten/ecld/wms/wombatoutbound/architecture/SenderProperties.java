package com.rakuten.ecld.wms.wombatoutbound.architecture;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sender")
public class SenderProperties {
    private String hostName;
    private int port;
    private String uri;
    private String token;
    private int count;
    private long rate;

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public String getUri() {
        return uri;
    }

    public String getToken() {
        return token;
    }

    public int getCount() {
        return count;
    }
}
