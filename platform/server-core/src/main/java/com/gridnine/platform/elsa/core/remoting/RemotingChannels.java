/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.platform.elsa.core.remoting;

import com.gridnine.platform.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.platform.elsa.common.core.serialization.JsonMarshaller;
import com.gridnine.platform.elsa.common.core.serialization.SerializationParameters;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.meta.remoting.RemotingDescription;
import com.gridnine.platform.elsa.common.meta.remoting.RemotingGroupDescription;
import com.gridnine.platform.elsa.common.meta.remoting.RemotingSubscriptionDescription;
import com.gridnine.platform.elsa.common.rest.core.RemotingMessage;
import com.gridnine.platform.elsa.common.rest.core.RemotingMessageType;
import jakarta.servlet.AsyncContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RemotingChannels {

    private final Map<String, RemotingChannel> channels = new ConcurrentHashMap<>();


    private final SerializationParameters serializationParameters;

    @Autowired
    private JsonMarshaller jsonMarshaller;

    public RemotingChannels() {
        serializationParameters = new SerializationParameters().setClassSerializationStrategy(SerializationParameters.ClassSerializationStrategy.NAME)
                .setEntityReferenceCaptionSerializationStrategy(SerializationParameters.EntityReferenceCaptionSerializationStrategy.ALL)
                .setEnumSerializationStrategy(SerializationParameters.EnumSerializationStrategy.NAME)
                .setEmptyListSerializationStrategy(SerializationParameters.EmptyListSerializationStrategy.INCLUDE).setPrettyPrint(false);
    }

    Map<String, RemotingChannel> getChannels() {
        return channels;
    }

    public <T extends BaseIntrospectableObject> void sendSubscriptionEvent(String remotingId, String groupId, String subscriptionId, T event) {
        ExceptionUtils.wrapException(() -> {
            String eventContent = null;
            for (var channelData : channels.values()) {
                for (Map.Entry<String, SubscriptionData> entry : channelData.subscriptions.entrySet()) {
                    SubscriptionData data = entry.getValue();
                    if (data.remoting.getId().equals(remotingId) && data.group.getId().equals(groupId)
                            && data.subscription.getId().equals(subscriptionId)
                            && ((SubscriptionHandler<Object, Object>) data.handler).isApplicable(event, data.param)) {
                        if (eventContent == null) {
                            String content = null;
                            if (event != null) {
                                var baos = new ByteArrayOutputStream();
                                jsonMarshaller.marshal(event, baos, false, serializationParameters);
                                content = baos.toString(StandardCharsets.UTF_8);
                            }
                            var message = new RemotingMessage();
                            message.setType(RemotingMessageType.SUBSCRIPTION);
                            message.setCallId(entry.getKey());
                            message.setRemotingId(remotingId);
                            message.setGroupId(groupId);
                            message.setSubscriptionId(subscriptionId);
                            message.setData(content);
                            var baos = new ByteArrayOutputStream();
                            jsonMarshaller.marshal(message, baos, false, serializationParameters);
                            eventContent = baos.toString(StandardCharsets.UTF_8);
                        }
                        var writer = channelData.ctx.getResponse().getWriter();
                        writer.write("data: %s\n\n".formatted(eventContent));
                        writer.flush();
                    }
                }
            }
        });

    }

    record RemotingChannel(String clientId, AsyncContext ctx, Map<String, SubscriptionData> subscriptions) {
        public RemotingChannel(String clientId, AsyncContext ctx) {
            this(clientId, ctx, new ConcurrentHashMap<>());
        }
    }

    record SubscriptionData(RemotingDescription remoting, RemotingGroupDescription group,
                            RemotingSubscriptionDescription subscription, Object param,
                            SubscriptionHandler<?, ?> handler) {
    }
}
