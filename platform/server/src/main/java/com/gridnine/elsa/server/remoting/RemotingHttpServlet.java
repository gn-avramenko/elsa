/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.remoting;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.gridnine.elsa.common.l10n.Localizer;
import com.gridnine.elsa.common.model.common.BaseIntrospectableObject;
import com.gridnine.elsa.common.model.common.Pair;
import com.gridnine.elsa.common.model.common.Xeption;
import com.gridnine.elsa.common.reflection.ReflectionFactory;
import com.gridnine.elsa.common.serialization.JsonMarshaller;
import com.gridnine.elsa.common.serialization.JsonUnmarshaller;
import com.gridnine.elsa.common.serialization.StandardSerializationParameters;
import com.gridnine.elsa.common.utils.ExceptionUtils;
import com.gridnine.elsa.common.utils.TextUtils;
import com.gridnine.elsa.meta.remoting.RemotingDescription;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.remoting.RemotingServerCallDescription;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RemotingHttpServlet extends HttpServlet {

    private final Map<String, Pair<RemotingServerCallHandler<?,?>, Pair<RemotingDescription, RemotingServerCallDescription>>> scHandlersCache = new ConcurrentHashMap<>();

    private final Map<String, Object> serializationParameters = new HashMap<>();

    private final Logger log = LoggerFactory.getLogger(getClass());

    public RemotingHttpServlet(){
        StandardSerializationParameters.setClassSerializationStrategy(StandardSerializationParameters.ClassSerializationStrategy.NAME, serializationParameters);
        StandardSerializationParameters.setEnumSerializationStrategy(StandardSerializationParameters.EnumSerializationStrategy.NAME, serializationParameters);
        StandardSerializationParameters.setEntityReferenceCaptionSerializationStrategy(StandardSerializationParameters.EntityReferenceCaptionSerializationStrategy.ALL, serializationParameters);
        StandardSerializationParameters.setPrettyPrint(false, serializationParameters);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LoggerFactory.getLogger(getClass()).debug("hello world");
        resp.getWriter().write("Hello world");
        resp.getWriter().flush();
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        var pathInfo = req.getPathInfo();
        var sc = scHandlersCache.get(pathInfo);
        if(sc != null){
            processServerCall(req,resp, sc);
            return;
        }
        var parts = pathInfo.split("/");
        var remoting = RemotingMetaRegistry.get().getRemotings().get(parts[1]);
        var group = remoting.getGroups().get(parts[2]);
        var serverCall = group.getServerCalls().get(parts[3]);
        if(serverCall != null){
            var pair = new Pair(ReflectionFactory.get().newInstance( serverCall.getAttributes().get("handler-class-name")), new Pair<>(remoting, serverCall));
            scHandlersCache.put(pathInfo, pair);
            processServerCall(req, resp, pair);
            return;
        }
        throw new IllegalArgumentException("unable to map %s".formatted(pathInfo));
    }

    private void processServerCall(HttpServletRequest req, HttpServletResponse resp, Pair<RemotingServerCallHandler<?, ?>, Pair<RemotingDescription, RemotingServerCallDescription>> sc) {
        var rid = sc.value().key().getId();
        var context = new RemotingServerCallContext();
        context.setHttpRequest(req);
        context.setHttpResponse(resp);
        context.setParameter(StandardRemotingParameters.REMOTING_DESCRIPTION, sc.value().key());
        context.setParameter(StandardRemotingParameters.SERVER_CALL_DESCRIPTION, sc.value().value());
        ExceptionUtils.wrapException(() ->processServerCall(context, (RemotingServerCallHandler<Object, Object>) sc.key(), RemotingRegistry.get().getAdvices(), 0));
    }

    private void processServerCall(RemotingServerCallContext context, RemotingServerCallHandler<Object, Object> handler, List<RemotingAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            processServerCallInternal(context,handler);
            return;
        }
        RemotingRegistry.get().getAdvices().get(idx).onServerCall(context, (context2) ->
                processServerCall(context2, handler,  RemotingRegistry.get().getAdvices(), idx + 1)
        );
    }

    private void processServerCallInternal(RemotingServerCallContext context, RemotingServerCallHandler<Object, Object> handler) throws Exception{
        try {
            RemotingServerCallDescription scd = context.getParameter(StandardRemotingParameters.SERVER_CALL_DESCRIPTION);
            Object rq = null;
            if(scd.getRequestClassName() != null){
                try (var is = context.getHttpRequest().getInputStream()) {
                    rq = JsonUnmarshaller.get().unmarshal(Class.forName(scd.getRequestClassName()), is, serializationParameters);
                }
            }
            var rp = handler.service(rq, context);
            if (scd.getResponseClassName() != null) {
                context.getHttpResponse().setStatus(HttpServletResponse.SC_OK);
                context.getHttpResponse().setContentType("application/json");
                context.getHttpResponse().setCharacterEncoding("UTF-8");
                try (var os = context.getHttpResponse().getOutputStream()) {
                    JsonMarshaller.get().marshal((BaseIntrospectableObject) rp, os, false, serializationParameters);
                    os.flush();
                }
            } else {
                context.getHttpResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } catch (Throwable t) {
            log.error("unable to process request", t);
            context.getHttpResponse().setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            context.getHttpResponse().setContentType("application/json");
            context.getHttpResponse().setCharacterEncoding("UTF-8");
            JsonFactory jsonFactory = new JsonFactory();
            try (var generator = jsonFactory.createGenerator(context.getHttpResponse().getOutputStream(), JsonEncoding.UTF8)) {
                generator.writeStartObject();
                var xeption = findXeption(t);
                if (xeption != null) {
                    generator.writeStringField("type", xeption.getType().name());
                    generator.writeStringField(
                            "message",
                            switch (xeption.getType()) {
                                case FOR_END_USER -> Localizer.get().toString(xeption.getEndUserMessage());
                                case FOR_ADMIN -> Localizer.get().toString(xeption.getAdminMessage());
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
}
