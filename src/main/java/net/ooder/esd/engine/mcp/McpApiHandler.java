package net.ooder.esd.engine.mcp;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.annotation.UserSpace;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;

public class McpApiHandler extends SimpleChannelInboundHandler<HttpRequest> {
    private DSMFactory dsmFactory ;
    private SseConnectionManager sseManager = SseConnectionManager.getInstance();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception {
        String uri = request.uri();
        FullHttpResponse response = new DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
        response.headers().set(HttpHeaderNames.CACHE_CONTROL, "no-cache");
        dsmFactory = DSMFactory.getInstance();
        Map<String, Object> result = new HashMap<>();
        try {
            if (uri.startsWith("/mcp/project/compile")) {
                String projectVersionName = getParam(uri, "projectVersionName");
                dsmFactory.compileProject(projectVersionName);
                result.put("status", "success");
                result.put("message", "Project compilation started");
            } else if (uri.startsWith("/mcp/domain/get")) {
                String domainId = getParam(uri, "domainId");
                DomainInst domain = dsmFactory.getDomainInstById(domainId);
                result.put("status", "success");
                result.put("data", domain);
            } else if (uri.startsWith("/mcp/domain/create")) {
                String projectVersionName = getParam(uri, "projectVersionName");
                UserSpace userSpace = UserSpace.valueOf(getParam(uri, "userSpace"));
                DomainInst domain = dsmFactory.newDomain(projectVersionName, userSpace);
                result.put("status", "success");
                result.put("data", domain.getDomainId());
            } else if (uri.startsWith("/mcp/project/build")) {
                String projectVersionName = getParam(uri, "projectVersionName");
                RepositoryInst repo = dsmFactory.getRepositoryManager().getProjectRepository(projectVersionName);
                dsmFactory.buildProject(repo, dsmFactory.getCurrChromeDriver());
                result.put("status", "success");
                result.put("message", "Project build completed");
            } else {
                response.setStatus(HttpResponseStatus.NOT_FOUND);
                result.put("status", "error");
                result.put("message", "API not found");
            }
        } catch (Exception e) {
            response.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }

        response.content().writeBytes(JSON.toJSONString(result).getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private String getParam(String uri, String paramName) {
        String[] parts = uri.split("\\?");
        if (parts.length > 1) {
            String[] params = parts[1].split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && keyValue[0].equals(paramName)) {
                    return keyValue[1];
                }
            }
        }
        return "";
    }
}