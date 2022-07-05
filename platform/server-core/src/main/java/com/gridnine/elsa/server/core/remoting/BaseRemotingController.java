/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.remoting;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.gridnine.elsa.common.core.l10n.Localizer;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.core.serialization.JsonMarshaller;
import com.gridnine.elsa.common.core.serialization.JsonUnmarshaller;
import com.gridnine.elsa.common.core.serialization.SerializationParameters;
import com.gridnine.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.elsa.common.core.utils.TextUtils;
import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.common.rest.core.ClientResponse;
import com.gridnine.elsa.common.rest.core.RemotingMessage;
import com.gridnine.elsa.common.rest.core.RemotingMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public abstract class BaseRemotingController {
    private final String remotingId;

    @Autowired
    private BeanFactory bf;

    @Autowired
    private RemotingMetaRegistry registry;

    @Autowired
    private JsonUnmarshaller unmarshaller;

    private SerializationParameters serializationParameters;

    private final JsonFactory jsonFactory = new JsonFactory();

    @Autowired
    private Localizer localizer;

    @Autowired
    private JsonMarshaller marshaller;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ConcurrentHashMap<String, ChannelData> channels = new ConcurrentHashMap<>();

    public BaseRemotingController(String restId) {
        this.remotingId = restId;
        serializationParameters = new SerializationParameters().setClassSerializationStrategy(SerializationParameters.ClassSerializationStrategy.NAME)
                .setEntityReferenceCaptionSerializationStrategy(SerializationParameters.EntityReferenceCaptionSerializationStrategy.ALL)
                .setEnumSerializationStrategy(SerializationParameters.EnumSerializationStrategy.NAME)
                .setEmptyListSerializationStrategy(SerializationParameters.EmptyListSerializationStrategy.INCLUDE).setPrettyPrint(false);
    }

    @PostMapping("subscribe")
    public String subscribe(@RequestHeader String groupId, @RequestHeader String subscriptionId, @RequestHeader String clientId, HttpServletRequest request) throws Exception {
        var channel = channels.get(clientId);
        if (channel == null) {
            throw Xeption.forDeveloper("channel does not exist");
        }
        var remoting = registry.getRemotings().get(remotingId);
        var subscriptionDescription = remoting.getGroups().get(groupId).getSubscriptions().get(subscriptionId);
        Object param;
        try (var is = request.getInputStream()) {
            param = unmarshaller.unmarshal(Class.forName(subscriptionDescription.getParameterClassName()), is, serializationParameters);
        }
        var uuid = TextUtils.generateUUID();
        var sd = new SubscriptionData(param);
        channel.subscriptions.put(uuid, sd);
        return uuid;
    }

    @PostMapping("confirmSubscriptionEvent")
    public void confirmSubscriptionEvent(@RequestHeader String clientId, @RequestHeader String subscriptionId, @RequestParam boolean received) throws Exception {
        var channel = channels.get(clientId);
        if (channel == null) {
            return;
        }
        synchronized (channel.lock) {
            var subscription = channel.subscriptions.get(subscriptionId);
            if (subscription == null) {
                return;
            }
            if (!received) {
                channel.subscriptions.remove(subscriptionId);
            }
            subscription.lastMessageSent = null;
        }
    }

    public void deleteStalledChannels() {
        var now = LocalDateTime.now();
        entry:
        for (var entry : new HashSet<>(channels.entrySet())) {
            synchronized (entry.getValue().lock) {
                for (SubscriptionData sv : entry.getValue().subscriptions.values()) {
                    if (sv.lastMessageSent != null && Duration.between(sv.lastMessageSent, now).toMillis() > 10000L) {
                        channels.remove(entry.getKey());
                        entry.getValue().sink.complete();
                        continue entry;
                    }
                }
                if (Duration.between(entry.getValue().lastUpdated, now).toMillis() > TimeUnit.MINUTES.toMillis(10)) {
                    channels.remove(entry.getKey());
                    entry.getValue().sink.complete();
                }
            }
        }
    }

    @PostMapping("unsubscribe")
    public void unsubscribe(@RequestHeader String clientId, @RequestHeader String subscriptionId) throws Exception {
        var channel = channels.get(clientId);
        if (channel == null) {
            return;
        }
        channel.subscriptions.remove(subscriptionId);
    }

    public void sendSubscriptionEvent(String groupId, String subscriptionId, Object event) {
        ExceptionUtils.wrapException(() -> {
            String content = null;
            var handler = bf.getBean(RemotingHandlersRegistry.class).getSubscriptionHandler("%s:%s:%s".formatted(remotingId, groupId, subscriptionId));
            for (ChannelData channelData : channels.values()) {
                for (Map.Entry<String, SubscriptionData> entry : channelData.subscriptions.entrySet()) {
                    String subId = entry.getKey();
                    if (handler.isApplicable(event, entry.getValue().parameter)) {
                        if (content == null) {
                            var remoting = registry.getRemotings().get(remotingId);
                            var subscriptionDescription = remoting.getGroups().get(groupId).getSubscriptions().get(subscriptionId);
                            var baos = new ByteArrayOutputStream();
                            marshaller.marshal(event, baos, false, serializationParameters);
                            content = baos.toString(StandardCharsets.UTF_8);
                        }
                        var message = new RemotingMessage();
                        message.setType(RemotingMessageType.SUBSCRIPTION);
                        message.setSubscriptionId(subId);
                        message.setData(content);
                        var baos = new ByteArrayOutputStream();
                        marshaller.marshal(message, baos, false, serializationParameters);
                        channelData.sink.next(ServerSentEvent.<String>builder().data(baos.toString(StandardCharsets.UTF_8)).build());
                        entry.getValue().lastMessageSent = LocalDateTime.now();
                    }
                }
            }
        });

    }

    public void checkStalledClientCalls() {
        channels.values().forEach(channelData -> {
                    synchronized (channelData.lock) {
                        channelData.lock.notifyAll();
                    }
                }
        );
    }

    public <RQ, RP> RP callClient(String clientId, String groupId, String methodId, RQ request) {
        return ExceptionUtils.wrapException(() -> {
            var channel = channels.get(clientId);
            if (channel == null) {
                throw Xeption.forDeveloper("channel is absent");
            }
            var requestId = TextUtils.generateUUID();
            channel.clientCalls.put(requestId, new ClientCallData(groupId, methodId, LocalDateTime.now()));
            var remotingMessage = new RemotingMessage();
            remotingMessage.setType(RemotingMessageType.CLIENT_CALL);
            remotingMessage.setCallId(requestId);
            remotingMessage.setGroupId(groupId);
            remotingMessage.setMethodId(methodId);
            var baos = new ByteArrayOutputStream();
            marshaller.marshal(request, baos, false, serializationParameters);
            remotingMessage.setData(baos.toString(StandardCharsets.UTF_8));
            baos = new ByteArrayOutputStream();
            marshaller.marshal(remotingMessage, baos, false, serializationParameters);
            channel.sink.next(ServerSentEvent.<String>builder().data(baos.toString(StandardCharsets.UTF_8)).build());
            while (true) {
                var reqData = channel.clientCalls.get(requestId);
                if (reqData == null) {
                    throw Xeption.forDeveloper("no request data");
                }
                if (reqData.errorMessage != null) {
                    channel.clientCalls.remove(requestId);
                    throw Xeption.forDeveloper("Client error occurred: %s".formatted(reqData.errorMessage));
                }
                if (reqData.completed) {
                    channel.clientCalls.remove(requestId);
                    //noinspection unchecked
                    return (RP) reqData.response;
                }
                if (Duration.between(reqData.sent, LocalDateTime.now()).toMillis() > 10000L) {
                    channel.clientCalls.remove(requestId);
                    throw Xeption.forDeveloper("Client error occurred: %s".formatted(reqData.errorMessage));
                }
                synchronized (channel.lock) {
                    channel.lock.wait();
                }
            }
        });
    }

    @PostMapping("registerClientResponse")
    public void registerClientResponse(@RequestHeader String clientId, @RequestHeader String requestId, HttpServletRequest request) throws Exception {
        var channel = channels.get(clientId);
        if (channel == null) {
            return;
        }
        var clientCallData = channel.clientCalls.get(requestId);
        try {
            if (clientCallData != null) {
                ClientResponse response = null;
                try (var is = request.getInputStream()) {
                    response = unmarshaller.unmarshal(ClientResponse.class, is, serializationParameters);
                }
                if (response.getErrorMessage() != null) {
                    clientCallData.errorMessage = response.getErrorMessage();
                    clientCallData.completed = true;
                    return;
                }
                var remoting = registry.getRemotings().get(remotingId);
                var responseStr = response.getResponseStr();
                var clientCall = remoting.getGroups().get(clientCallData.groupId).getClientCalls().get(clientCallData.methodId);
                Object rp = null;
                if (clientCall.getResponseClassName() != null) {
                    try (var is = new ByteArrayInputStream(response.getResponseStr().getBytes(StandardCharsets.UTF_8))) {
                        rp = unmarshaller.unmarshal(Class.forName(clientCall.getResponseClassName()), is, serializationParameters);
                    }
                }
                clientCallData.response = rp;
                clientCallData.completed = true;
            }
        } finally {
            synchronized (channel.lock) {
                channel.lock.notifyAll();
            }
        }

    }

    @PostMapping("request")
    public void request(Principal principal, @RequestHeader String groupId, @RequestHeader String clientId, @RequestHeader String methodId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            var channel = channels.get(clientId);
            synchronized (channel.lock) {
                channel.lock.notifyAll();
            }
            channel.lastUpdated = LocalDateTime.now();
            var remoting = registry.getRemotings().get(remotingId);
            var serverCall = remoting.getGroups().get(groupId).getServerCalls().get(methodId);
            var ctx = new RemotingServerCallContext();
            ctx.setClientId(clientId);
            Object rq = null;
            if (serverCall.getRequestClassName() != null) {
                try (var is = request.getInputStream()) {
                    rq = unmarshaller.unmarshal(Class.forName(serverCall.getRequestClassName()), is, serializationParameters);
                }
            }
            var handler = bf.getBean(RemotingHandlersRegistry.class).getServerCallHandler("%s:%s:%s".formatted(remotingId, groupId, methodId));
            var rp = handler.service(rq, ctx);
            if (serverCall.getResponseClassName() != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                try (var os = response.getOutputStream()) {
                    marshaller.marshal(rp, os, false, serializationParameters);
                    os.flush();
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } catch (Throwable t) {
            log.error("unable to process request", t);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try (var generator = jsonFactory.createGenerator(response.getOutputStream(), JsonEncoding.UTF8)) {
                generator.writeStartObject();
                var xeption = findXeption(t);
                if (xeption != null) {
                    generator.writeStringField("type", xeption.getType().name());
                    generator.writeStringField(
                            "message",
                            switch (xeption.getType()) {
                                case FOR_END_USER -> localizer.toString(xeption.getEndUserMessage());
                                case FOR_ADMIN -> localizer.toString(xeption.getAdminMessage());
                                case FOR_DEVELOPER -> xeption.getDeveloperMessage();
                            });
                } else {
                    generator.writeStringField("message", t.getCause() != null ? t.getCause().getMessage() : t.getMessage());
                }
                generator.writeStringField("stacktrace", TextUtils.getExceptionStackTrace(t.getCause() == null ? t : t.getCause()));
                generator.writeEndObject();
                generator.flush();
            }
        }
    }

    private static Xeption findXeption(Throwable e) {
        if (e instanceof Xeption xe) {
            return xe;
        }
        if (e.getCause() != null) {
            return findXeption(e.getCause());
        }
        return null;
    }

    @GetMapping(value = "createChannel", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> createChannel(@RequestParam String clientId) throws IOException {
        var data = new ChannelData();
        var existing = channels.putIfAbsent(clientId, data);
        var fdata = existing == null ? data : existing;
        synchronized (fdata.lock) {
            if (fdata.sink != null) {
                fdata.sink.complete();
            }
            fdata.subscriptions.clear();
            return Flux.create((emitter) -> {
                fdata.lastUpdated = LocalDateTime.now();
                fdata.sink = emitter;
                var message = new RemotingMessage();
                message.setType(RemotingMessageType.PING);
                message.setData("connection is established");
                var baos = new ByteArrayOutputStream();
                marshaller.marshal(message, baos, false, serializationParameters);
                emitter.next(ServerSentEvent.<String>builder().data(baos.toString(StandardCharsets.UTF_8)).build());
            });
        }
    }

    @GetMapping(value = "check")
    public void check() {
    }

    static class SubscriptionData {
        final Object parameter;
        volatile LocalDateTime lastMessageSent;

        SubscriptionData(Object parameter) {
            this.parameter = parameter;
        }
    }

    static class ClientCallData {
        volatile String errorMessage;
        volatile boolean completed;
        volatile Object response;
        final String methodId;
        final String groupId;
        final LocalDateTime sent;

        public ClientCallData(String groupId, String methodId, LocalDateTime sent) {
            this.methodId = methodId;
            this.groupId = groupId;
            this.sent = sent;
        }
    }

    static class ChannelData {
        volatile LocalDateTime lastUpdated = LocalDateTime.now();
        volatile FluxSink<ServerSentEvent<String>> sink;
        final Map<String, SubscriptionData> subscriptions = new ConcurrentHashMap<>();
        final Map<String, ClientCallData> clientCalls = new ConcurrentHashMap<>();
        final Object lock = new Object();
    }
}
