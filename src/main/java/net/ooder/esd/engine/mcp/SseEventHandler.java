package net.ooder.esd.engine.mcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

public class SseEventHandler extends SimpleChannelInboundHandler<HttpRequest> {
    private SseConnectionManager connectionManager = SseConnectionManager.getInstance();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception {
        if ("GET".equals(request.method().name()) && "/mcp/sse/events".equals(request.uri())) {
            // 设置SSE响应头
            DefaultHttpResponse response = new DefaultHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/event-stream");
            response.headers().set(HttpHeaderNames.CACHE_CONTROL, "no-cache");
            response.headers().set(HttpHeaderNames.CONNECTION, "keep-alive");
            ctx.writeAndFlush(response);

            // 注册连接
            connectionManager.addConnection(ctx.channel());
        } else {
            ctx.fireChannelRead(request);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        connectionManager.removeConnection(ctx.channel());
    }
}