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
import com.gridnine.elsa.common.core.utils.TextUtils;
import com.gridnine.elsa.common.meta.remoting.RemotingMetaRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

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

    private FluxSink<ServerSentEvent<String>> sink;

    public BaseRemotingController(String restId) {
        this.remotingId = restId;
        serializationParameters = new SerializationParameters().setClassSerializationStrategy(SerializationParameters.ClassSerializationStrategy.NAME)
                .setEntityReferenceCaptionSerializationStrategy(SerializationParameters.EntityReferenceCaptionSerializationStrategy.ALL)
                .setEnumSerializationStrategy(SerializationParameters.EnumSerializationStrategy.NAME)
                .setEmptyListSerializationStrategy(SerializationParameters.EmptyListSerializationStrategy.INCLUDE);
    }

    @PostMapping("request")
    public void request(Principal principal, @RequestHeader String group, @RequestHeader String method, HttpServletRequest request, HttpServletResponse response) throws Exception{
        try {
            var remoting = registry.getRemotings().get(remotingId);
            var serverCall = remoting.getGroups().get(group).getServerCalls().get(method);
            var ctx = new RemotingServerCallContext();
            Object rq = null;
            if (serverCall.getRequestClassName() != null) {
                try (var is = request.getInputStream()) {
                    rq = unmarshaller.unmarshal(Class.forName(serverCall.getRequestClassName()), is, serializationParameters);
                }
            }
            var handler = bf.getBean(RemotingHandlersRegistry.class).getHandler("%s:%s:%s".formatted(remotingId, group, method));
            var rp = handler.service(rq, ctx);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            if (serverCall.getResponseClassName() != null) {
                try (var os = response.getOutputStream()) {
                    marshaller.marshal(rp, os, false, serializationParameters);
                    os.flush();
                }
            }
        } catch (Throwable t){
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
        if(e instanceof Xeption xe){
            return xe;
        }
        if(e.getCause() != null){
            return findXeption(e.getCause());
        }
        return null;
    }

    @GetMapping(value = "subscribe",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> subscribe() throws IOException {
        return Flux.create((emitter) ->{
            sink = emitter;
            emitter.next(ServerSentEvent.<String> builder().comment("ping").data("ping").build());
        });
    }

    @GetMapping(value = "check")
    public void check(){
    }
}
