/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting;

import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.model.remoting.RemotingMessage;
import com.gridnine.elsa.common.model.remoting.RemotingMessageType;
import com.gridnine.elsa.common.serialization.JsonMarshaller;
import com.gridnine.elsa.common.serialization.StandardSerializationParameters;
import com.gridnine.elsa.common.utils.ExceptionUtils;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.meta.remoting.RemotingDescription;
import com.gridnine.elsa.meta.remoting.RemotingGroupDescription;
import com.gridnine.elsa.meta.remoting.RemotingSubscriptionDescription;
import jakarta.servlet.AsyncContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RemotingChannels {

    private final Map<String, RemotingChannel> channels = new ConcurrentHashMap<>();

    private final Map<String, Object> serializationParameters = new HashMap<>();

    private final Logger log = LoggerFactory.getLogger(getClass());

    public RemotingChannels() {
        StandardSerializationParameters.setClassSerializationStrategy(StandardSerializationParameters.ClassSerializationStrategy.NAME, serializationParameters);
        StandardSerializationParameters.setEnumSerializationStrategy(StandardSerializationParameters.EnumSerializationStrategy.NAME, serializationParameters);
        StandardSerializationParameters.setEntityReferenceCaptionSerializationStrategy(StandardSerializationParameters.EntityReferenceCaptionSerializationStrategy.ALL, serializationParameters);
        StandardSerializationParameters.setPrettyPrint(false, serializationParameters);
    }

    Map<String, RemotingChannel> getChannels() {
        return channels;
    }

    public<T extends BaseIntrospectableObject> void sendSubscriptionEvent(String remotingId, String groupId, String subscriptionId, T event) {
        ExceptionUtils.wrapException(() -> {
            String eventContent = null;
            for (var channelData : channels.values()) {
                for (Map.Entry<String, SubscriptionData> entry : channelData.subscriptions.entrySet()) {
                    SubscriptionData data = entry.getValue();
                    if(data.remoting.getId().equals(remotingId) && data.group.getId().equals(groupId)
                            && data.subscription.getId().equals(subscriptionId)
                    &&( (RemotingSubscriptionHandler<Object,Object>) data.handler).isApplicable(event, data.param)){
                        if(eventContent == null ){
                            String content = null;
                            if(event != null){
                                var baos = new ByteArrayOutputStream();
                                JsonMarshaller.get().marshal(event, baos, false, serializationParameters);
                                content = baos.toString(StandardCharsets.UTF_8);
                            }
                            var message = new RemotingMessage();
                            message.setType(RemotingMessageType.SUBSCRIPTION);
                            message.setCallId(entry.getKey());
                            message.setRemotingId(remotingId);
                            message.setGroupId(groupId);
                            message.setMethodId(subscriptionId);
                            message.setData(content);
                            var baos = new ByteArrayOutputStream();
                            JsonMarshaller.get().marshal(message, baos, false, serializationParameters);
                            eventContent = baos.toString(StandardCharsets.UTF_8);
                        }
                        var writer = channelData.ctx.getResponse().getWriter();
                        writer.write("data: %s\r\n".formatted(eventContent));
                        writer.flush();
                    }
                }
            }
        });

    }

    public static RemotingChannels get(){
        return Environment.getPublished(RemotingChannels.class);
    }

    record RemotingChannel(String clientId, AsyncContext ctx, Map<String,SubscriptionData> subscriptions){
        public RemotingChannel(String clientId, AsyncContext ctx){
            this(clientId, ctx, new ConcurrentHashMap<>());
        }
    }

    record SubscriptionData(RemotingDescription remoting, RemotingGroupDescription group, RemotingSubscriptionDescription subscription, Object param, RemotingSubscriptionHandler<?,?> handler){}
}
