package com.mlc.netty.client;

import java.net.URI;
import java.net.URISyntaxException;

import javax.net.ssl.SSLException;

public class RemoteAddressInfo {

    private String host;
    private String scheme;
    private int port;
    private URI uri;

    public RemoteAddressInfo(String url) throws URISyntaxException, SSLException {
        uri = new URI(url);
        scheme = uri.getScheme() == null ? "http" : uri.getScheme();
        host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        port = uri.getPort();
        if (port == -1) {
            if ("http".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("https".equalsIgnoreCase(scheme)) {
                port = 443;
            }
        }

        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            throw new IllegalArgumentException("Only HTTP(S) is supported.");
        }
    }

    public String host() {
        return host;
    }

    public int port() {
        return port;
    }

    public boolean isHTTPS() {
        if (uri.getScheme() == null) {
            return false;
        }
        return "https".equalsIgnoreCase(uri.getScheme());
    }

    public String rawPath() {
        return uri.getRawPath();
    }

}
