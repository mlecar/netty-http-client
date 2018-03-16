package com.mlc.netty.client;

import java.net.URISyntaxException;

import org.junit.Test;

public class NettyClientTest {

    @Test
    public void send() throws InterruptedException, URISyntaxException {
        NettyClient nettyClient = new NettyClient();

        String url = "http://localhost";
        Integer port = 8080;
        String json = "{\"a\":\"123\"}";
    }

}
