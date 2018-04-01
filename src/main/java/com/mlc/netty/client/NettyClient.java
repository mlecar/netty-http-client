package com.mlc.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class NettyClient {

    private EventLoopGroup group;

    public NettyClient() {
        group = new NioEventLoopGroup();
    }

    public void send(HttpMethod method, String url, String body, HttpHeaders headers) throws Exception {
        RemoteAddressInfo remoteInfo = new RemoteAddressInfo(url);

        try {
            final SslContext sslCtx;
            if (remoteInfo.isHTTPS()) {
                sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            } else {
                sslCtx = null;
            }
            Bootstrap b = new Bootstrap();
            // b.group(group).channel(NioSocketChannel.class).handler(new
            // HttpRequestClientInitializer(sslCtx));

            b.group(group).channel(NioSocketChannel.class).handler(new HttpRequestClientInitializer(sslCtx, remoteInfo));

            // Make the connection attempt.
            Channel ch = b.connect(remoteInfo.host(), remoteInfo.port()).sync().channel();

            // Prepare the HTTP request.
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, remoteInfo.rawPath());
            request.headers().set(HttpHeaderNames.HOST, remoteInfo.host());
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
            if (headers != null) {
                request.headers().add(headers);
            }
            if (body != null) {
                request.content().writeBytes(body.getBytes());
            }

            // Set some example cookies.
            // request.headers().set(HttpHeaderNames.COOKIE,
            // ClientCookieEncoder.STRICT.encode(new DefaultCookie("my-cookie",
            // "foo"), new DefaultCookie("another-cookie", "bar")));

            // Send the HTTP request.
            ch.writeAndFlush(request);

            // Wait for the server to close the connection.
            ch.closeFuture().sync();
        } finally {
            // Shut down executor threads to exit.
            group.shutdownGracefully();
        }
    }
}
