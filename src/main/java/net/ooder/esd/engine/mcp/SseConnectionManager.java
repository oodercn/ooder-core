package net.ooder.esd.engine.mcp;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class SseConnectionManager {
    private static SseConnectionManager instance = new SseConnectionManager();
    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private SseConnectionManager() {}

    public static SseConnectionManager getInstance() {
        return instance;
    }

    public void addConnection(Channel channel) {
        channels.add(channel);
    }

    public void removeConnection(Channel channel) {
        channels.remove(channel);
    }

    public void broadcastEvent(String eventType, String data) {
        String sseEvent = String.format("event: %s\ndata: %s\n\n", eventType, data);
        channels.writeAndFlush(sseEvent);
    }
}