package com.mlc.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.ssl.SslContext;

public class HttpRequestClientInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;
    private RemoteAddressInfo remoteAddressInfo;

    public HttpRequestClientInitializer(SslContext sslCtx, RemoteAddressInfo remoteAddressInfo) {
        this.sslCtx = sslCtx;
        this.remoteAddressInfo = remoteAddressInfo;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();

        // Enable HTTPS if necessary.
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc(), remoteAddressInfo.host(), remoteAddressInfo.port()));
        }

        p.addLast(new HttpClientCodec());

        // Remove the following line if you don't want automatic content
        // decompression.
        p.addLast(new HttpContentDecompressor());

        // Uncomment the following line if you don't want to handle
        // HttpContents.
        // p.addLast(new HttpObjectAggregator(1048576));

        p.addLast(new HttpHandler());
    }

}
