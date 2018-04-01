package com.mlc.netty.client;

import org.junit.Before;
import org.junit.Test;

import io.netty.handler.codec.http.HttpMethod;

public class NettyClientTest {

    public NettyClient nettyClient;

    @Before
    public void setup() {
        nettyClient = new NettyClient();
    }

    @Test
    public void get() throws Exception {
        nettyClient.send(HttpMethod.GET, "http://www.google.com.br", "", null);
    }

    @Test
    public void post() throws Exception {
        nettyClient.send(HttpMethod.POST, "https://httpbin.org/post", "{\"teste\":\"123\"}", null);
    }

}
