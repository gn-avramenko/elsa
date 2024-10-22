/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.platform.elsa.core.remoting;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.gridnine.platform.elsa.common.core.l10n.Localizer;
import com.gridnine.platform.elsa.common.core.model.common.BaseIntrospectableObject;
import com.gridnine.platform.elsa.common.core.model.common.Xeption;
import com.gridnine.platform.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.platform.elsa.common.core.serialization.JsonMarshaller;
import com.gridnine.platform.elsa.common.core.serialization.JsonUnmarshaller;
import com.gridnine.platform.elsa.common.core.serialization.ParameterMapConverter;
import com.gridnine.platform.elsa.common.core.serialization.SerializationParameters;
import com.gridnine.platform.elsa.common.core.serialization.meta.BaseObjectMetadataProvider;
import com.gridnine.platform.elsa.common.core.serialization.meta.ObjectMetadataProvidersFactory;
import com.gridnine.platform.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.platform.elsa.common.core.utils.IoUtils;
import com.gridnine.platform.elsa.common.core.utils.Pair;
import com.gridnine.platform.elsa.common.core.utils.TextUtils;
import com.gridnine.platform.elsa.common.meta.remoting.*;
import com.gridnine.platform.elsa.core.remoting.standard.BinaryData;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;

import javax.annotation.PostConstruct;
import java.io.Closeable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RemotingHttpServlet extends HttpServlet {

    private final Map<String, EnumMap<HttpMethod, Pair<com.gridnine.platform.elsa.common.core.utils.Lazy<RestHandler<Object, Object>>, ServiceParams>>> scHandlersCache = new ConcurrentHashMap<>();

    private final Map<String, Pair<com.gridnine.platform.elsa.common.core.utils.Lazy<SubscriptionHandler<Object, Object>>, SubscriptionParams>> subHandlersCache = new ConcurrentHashMap<>();

    private final SerializationParameters serializationParameters;


    private static final String ACCESS_CONTROL_ALLOW_HEADERS = String.join(",", "AuthorizationKey", "Authorization", "Content-Type", "Accept", "Origin", "User-Agent", "Cache-Control",
            "Keep-Alive", "X-Requested-With", "If-Modified-Since", "X-Application-Locale");

    private static final Set<HttpMethod> REQUEST_BODY_METHODS = Set.of(HttpMethod.POST, HttpMethod.PUT);

    @Autowired
    @Lazy
    private RemotingHandlersRegistry handlersRegistry;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RemotingMetaRegistry registry;

    @Autowired
    private ReflectionFactory reflectionFactory;

    @Autowired
    private ObjectMetadataProvidersFactory metadataProvidersFactory;

    @Autowired
    private JsonMarshaller jsonMarshaller;

    @Autowired
    private JsonUnmarshaller jsonUnmarshaller;

    @Autowired
    ParameterMapConverter parameterMapConverter;

    @Autowired
    private Localizer localizer;

    public RemotingHttpServlet() {
        serializationParameters = new SerializationParameters().setClassSerializationStrategy(SerializationParameters.ClassSerializationStrategy.NAME)
                .setEntityReferenceCaptionSerializationStrategy(SerializationParameters.EntityReferenceCaptionSerializationStrategy.ALL)
                .setEnumSerializationStrategy(SerializationParameters.EnumSerializationStrategy.NAME)
                .setEmptyListSerializationStrategy(SerializationParameters.EmptyListSerializationStrategy.INCLUDE).setPrettyPrint(false);
    }

    @PostConstruct
    private void populateCaches() {
        registry.getRemotings().values().forEach(remoting -> remoting.getGroups().values().forEach(group -> {
            group.getServices().values().forEach(service -> {
                String key = "/%s/%s/%s".formatted(remoting.getId(), group.getId(), service.getPath() == null ? service.getId() : service.getPath());
                var map = scHandlersCache.computeIfAbsent(key, k -> new EnumMap<>(HttpMethod.class));
                map.put(service.getMethod(), Pair.of(new com.gridnine.platform.elsa.common.core.utils.Lazy<>(() ->
                                Objects.requireNonNull(handlersRegistry.getServiceHandler("%s:%s:%s".formatted(remoting.getId(), group.getId(), service.getId())))),
                        new ServiceParams(remoting, group, service)));
            });
            group.getSubscriptions().values().forEach(subscription -> {
                String key = "/%s/%s/%s".formatted(remoting.getId(), group.getId(), subscription.getId());
                var pair = Pair.of(new com.gridnine.platform.elsa.common.core.utils.Lazy<>(() ->
                                Objects.requireNonNull(handlersRegistry.getSubscriptionHandler("%s:%s:%s".formatted(remoting.getId(), group.getId(), subscription.getId())))),
                        new SubscriptionParams(remoting, group, subscription));
                subHandlersCache.put(key, pair);
                subHandlersCache.put("%s/subscribe".formatted(key), pair);
                subHandlersCache.put("%s/unsibscribe".formatted(key), pair);
            });
        }));
    }

    private String getPathInfo(final HttpServletRequest req){
        String pathInfo = req.getPathInfo();
        if (pathInfo.endsWith("/")) {
            pathInfo = pathInfo.substring(0, pathInfo.length() - 1);
        }
        return pathInfo;
    }

    @Override
    protected void doOptions(final HttpServletRequest req, final HttpServletResponse res) {
        var pathInfo = getPathInfo(req);
        var allowedMethods = new ArrayList<HttpMethod>();
        if(scHandlersCache.containsKey(pathInfo)){
            allowedMethods.addAll(scHandlersCache.get(pathInfo).keySet());
        } else if (subHandlersCache.containsKey(pathInfo)){
            allowedMethods.add(HttpMethod.POST);
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String origin = req.getHeader("Origin");
        if (origin != null) {
            origin = origin.split("\\r|\\n|\\r\\n")[0];
        } else {
            origin = "*";
        }
        res.setHeader("Access-Control-Allow-Origin", origin);
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Methods",
                "OPTIONS," + allowedMethods.stream().map(HttpMethod::name).collect(Collectors.joining(",")));
        res.setHeader("Access-Control-Allow-Headers", ACCESS_CONTROL_ALLOW_HEADERS);
        res.setHeader("Access-Control-Max-Age", "60");
    }

    private void doHandle(final HttpServletRequest req, final HttpServletResponse res) {
        var pathInfo = getPathInfo(req);
        var method = HttpMethod.valueOf(req.getMethod());
        if(scHandlersCache.containsKey(pathInfo)){
            var pair = scHandlersCache.get(pathInfo).get(method);
            if(pair == null){
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            processServiceRequest(req, res, Pair.of(pair.getLeft().getObject(), pair.getRight()));
            return;
        }
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doHandle(req,resp);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doHandle(req,resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        doHandle(req,resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        doHandle(req,resp);
    }


    private void processServiceRequest(HttpServletRequest req, HttpServletResponse resp, Pair<RestHandler<Object, Object>, ServiceParams> sc) {
        var context = new RemotingCallContext();
        context.setHttpRequest(req);
        context.setHttpResponse(resp);
        context.setParameter(StandardRemotingParameters.REMOTING_DESCRIPTION, sc.getRight().remoting());
        context.setParameter(StandardRemotingParameters.GROUP_DESCRIPTION, sc.getRight().group());
        context.setParameter(StandardRemotingParameters.SERVICE_DESCRIPTION, sc.getRight().service());
        ExceptionUtils.wrapException(() -> processServiceRequest(context, sc.getLeft(), handlersRegistry.getAdvices(), 0));
    }

    private void processServiceRequest(RemotingCallContext context, RestHandler<Object, Object> handler, List<RemotingAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            processServerCallInternal(context, handler);
            return;
        }
        handlersRegistry.getAdvices().get(idx).onServerCall(context, (context2) ->
                processServiceRequest(context2, handler, handlersRegistry.getAdvices(), idx + 1)
        );
    }

    @SuppressWarnings("unchecked")
    private void processServerCallInternal(RemotingCallContext context, RestHandler<Object, Object> handler) throws Exception {
        try {
            ServiceDescription scd = context.getParameter(StandardRemotingParameters.SERVICE_DESCRIPTION);
            Object rq = null;
            List<Closeable> toClose = new ArrayList<>();
            if (scd.getRequestClassName() != null) {
                if (!REQUEST_BODY_METHODS.contains(scd.getMethod())) {
                    rq = parameterMapConverter.convert(Class.forName(scd.getRequestClassName()),
                            context.getHttpRequest().getParameterMap());
                } else if (REQUEST_BODY_METHODS.contains(scd.getMethod()) && scd.isMultipartRequest()) {
                    rq = parameterMapConverter.convert(Class.forName(scd.getRequestClassName()),
                            context.getHttpRequest().getParameterMap());
                    var provider = (BaseObjectMetadataProvider<Object>)
                            metadataProvidersFactory.getProvider(scd.getRequestClassName());
                    Map<String, Part> field2part = extractBinaryDataParts(context.getHttpRequest().getParts());
                    for (var entry : field2part.entrySet()) {
                        var bd = new BinaryData();
                        var is = entry.getValue().getInputStream(); // opens new InputStream
                        toClose.add(is);
                        bd.setMimeType(entry.getValue().getContentType());
                        bd.setContentLength(entry.getValue().getSize());
                        bd.setName(entry.getValue().getSubmittedFileName());
                        bd.setInputStream(is);
                        provider.setPropertyValue(rq, entry.getKey(), bd);
                    }
                } else {
                    try (var is = context.getHttpRequest().getInputStream()) {
                        rq = jsonUnmarshaller.unmarshal(Class.forName(scd.getRequestClassName()), is, serializationParameters);
                    }
                }
            }
            var rp = handler.service(rq, context);
            toClose.forEach(IoUtils::closeQuietly);
            if (BinaryData.class.getName().equals(scd.getResponseClassName())){
                var bd = (BinaryData) rp;
                context.getHttpResponse().setStatus(HttpServletResponse.SC_OK);
                context.getHttpResponse().setContentType("application/octet-stream");
                context.getHttpResponse().setContentLengthLong(bd.getContentLength());
                if (context.getHttpResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION) == null) {
                    context.getHttpResponse().setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''"
                            + URLEncoder.encode(bd.getName(), StandardCharsets.UTF_8));
                }
                try (var os = context.getHttpResponse().getOutputStream()) {
                    bd.getInputStream().transferTo(os);
                    os.flush();
                }
            } else if (scd.getResponseClassName() != null) {
                context.getHttpResponse().setStatus(HttpServletResponse.SC_OK);
                context.getHttpResponse().setContentType("application/json");
                context.getHttpResponse().setCharacterEncoding("UTF-8");
                try (var os = context.getHttpResponse().getOutputStream()) {
                    jsonMarshaller.marshal((BaseIntrospectableObject) rp, os, false, serializationParameters);
                    os.flush();
                }
            } else {
                context.getHttpResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
                context.getHttpResponse().setHeader("Cache-Control", "no-cache");
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

    private static Map<String, Part> extractBinaryDataParts(Collection<Part> parts) {
        return parts.stream()
                .filter(part -> part.getSubmittedFileName() != null)
                .collect(Collectors.toMap(Part::getName, Function.identity()));
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

    record SubscriptionParams(RemotingDescription remoting, RemotingGroupDescription group,
                              RemotingSubscriptionDescription subscription) {
    }


    record ServiceParams(RemotingDescription remoting, RemotingGroupDescription group, ServiceDescription service) {
    }


}
