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
import com.gridnine.elsa.common.serialization.StringUnmarshaller;
import com.gridnine.elsa.common.utils.ExceptionUtils;
import com.gridnine.elsa.common.utils.IoUtils;
import com.gridnine.elsa.common.utils.TextUtils;
import com.gridnine.elsa.meta.remoting.RemotingDescription;
import com.gridnine.elsa.meta.remoting.RemotingDownloadDescription;
import com.gridnine.elsa.meta.remoting.RemotingMetaRegistry;
import com.gridnine.elsa.meta.remoting.RemotingServerCallDescription;
import com.gridnine.elsa.meta.remoting.RemotingUploadDescription;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RemotingHttpServlet extends HttpServlet {

    private final Map<String, Pair<RemotingServerCallHandler<?, ?>, Pair<RemotingDescription, RemotingServerCallDescription>>> scHandlersCache = new ConcurrentHashMap<>();

    private final Map<String, Pair<Object, Pair<RemotingDescription, RemotingDownloadDescription>>> downloadHandlersCache = new ConcurrentHashMap<>();
    private final Map<String, Pair<RemotingUploadHandler<?>, Pair<RemotingDescription, RemotingUploadDescription>>> uploadHandlersCache = new ConcurrentHashMap<>();

    private final Map<String, Object> serializationParameters = new HashMap<>();

    private final Logger log = LoggerFactory.getLogger(getClass());

    public RemotingHttpServlet() {
        StandardSerializationParameters.setClassSerializationStrategy(StandardSerializationParameters.ClassSerializationStrategy.NAME, serializationParameters);
        StandardSerializationParameters.setEnumSerializationStrategy(StandardSerializationParameters.EnumSerializationStrategy.NAME, serializationParameters);
        StandardSerializationParameters.setEntityReferenceCaptionSerializationStrategy(StandardSerializationParameters.EntityReferenceCaptionSerializationStrategy.ALL, serializationParameters);
        StandardSerializationParameters.setPrettyPrint(false, serializationParameters);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathInfo = req.getPathInfo();
        var dh = uploadHandlersCache.get(pathInfo);
        if (dh != null) {
            processUploadCall(req, resp, dh);
            return;
        }
        var parts = pathInfo.split("/");
        var remoting = RemotingMetaRegistry.get().getRemotings().get(parts[1]);
        var group = remoting.getGroups().get(parts[2]);
        var uploadCall = group.getDownloads().get(parts[3]);
        if (uploadCall != null) {
            var pair = new Pair(ReflectionFactory.get().newInstance(uploadCall.getAttributes().get("handler-class-name")), new Pair<>(remoting, uploadCall));
            uploadHandlersCache.put(pathInfo, pair);
            processUploadCall(req, resp, pair);
            return;
        }
        throw new IllegalArgumentException("unable to map %s".formatted(pathInfo));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var pathInfo = req.getPathInfo();
        var dh = downloadHandlersCache.get(pathInfo);
        if (dh != null) {
            processDownloadCall(req, resp, dh);
            return;
        }
        var parts = pathInfo.split("/");
        var remoting = RemotingMetaRegistry.get().getRemotings().get(parts[1]);
        var group = remoting.getGroups().get(parts[2]);
        var downloadCall = group.getDownloads().get(parts[3]);
        if (downloadCall != null) {
            var pair = new Pair(ReflectionFactory.get().newInstance(downloadCall.getAttributes().get("handler-class-name")), new Pair<>(remoting, downloadCall));
            downloadHandlersCache.put(pathInfo, pair);
            processDownloadCall(req, resp, pair);
            return;
        }
        throw new IllegalArgumentException("unable to map %s".formatted(pathInfo));
    }

    private void processUploadCall(HttpServletRequest req, HttpServletResponse resp, Pair<RemotingUploadHandler<?>, Pair<RemotingDescription, RemotingUploadDescription>> sc) {
        var rid = sc.value().key().getId();
        var context = new RemotingCallContext();
        context.setHttpRequest(req);
        context.setHttpResponse(resp);
        context.setParameter(StandardRemotingParameters.REMOTING_DESCRIPTION, sc.value().key());
        context.setParameter(StandardRemotingParameters.UPLOAD_DESCRIPTION, sc.value().value());
        ExceptionUtils.wrapException(() -> processUploadCall(context, sc.key(), RemotingRegistry.get().getAdvices(), 0));
    }


    private void processUploadCall(RemotingCallContext context, RemotingUploadHandler<?> handler, List<RemotingAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            processUploadCallInternal(context, handler);
            return;
        }
        RemotingRegistry.get().getAdvices().get(idx).onUpload(context, (context2) ->
                processUploadCall(context2, handler, RemotingRegistry.get().getAdvices(), idx + 1)
        );
    }
    private void processUploadCallInternal(RemotingCallContext context, RemotingUploadHandler<?> handler) throws Exception {
        var scd = context.getParameter(StandardRemotingParameters.UPLOAD_DESCRIPTION);
        Object rq = null;
        if (scd.getRequestClassName() != null) {
            try (var is = context.getHttpRequest().getInputStream()) {
                rq = StringUnmarshaller.get().unmarshal(scd.getRequestClassName(), context.getHttpRequest().getQueryString(), serializationParameters);
            }
        }
        HttpServletResponse response = context.getHttpResponse();
        //TODO implement
    }
    private void processDownloadCall(HttpServletRequest req, HttpServletResponse resp, Pair<Object, Pair<RemotingDescription, RemotingDownloadDescription>> sc) {
        var rid = sc.value().key().getId();
        var context = new RemotingCallContext();
        context.setHttpRequest(req);
        context.setHttpResponse(resp);
        context.setParameter(StandardRemotingParameters.REMOTING_DESCRIPTION, sc.value().key());
        context.setParameter(StandardRemotingParameters.DOWNLOAD_DESCRIPTION, sc.value().value());
        ExceptionUtils.wrapException(() -> processDownloadCall(context, sc.key(), RemotingRegistry.get().getAdvices(), 0));
    }


    private void processDownloadCall(RemotingCallContext context, Object handler, List<RemotingAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            processDownloadCallInternal(context, handler);
            return;
        }
        RemotingRegistry.get().getAdvices().get(idx).onDownload(context, (context2) ->
                processDownloadCall(context2, handler, RemotingRegistry.get().getAdvices(), idx + 1)
        );
    }

    private void processDownloadCallInternal(RemotingCallContext context, Object handler) throws Exception {
        RemotingDownloadDescription scd = context.getParameter(StandardRemotingParameters.DOWNLOAD_DESCRIPTION);
        Object rq = null;
        if (scd.getRequestClassName() != null) {
            try (var is = context.getHttpRequest().getInputStream()) {
                rq = StringUnmarshaller.get().unmarshal(scd.getRequestClassName(), context.getHttpRequest().getQueryString(), serializationParameters);
            }
        }
        HttpServletResponse response = context.getHttpResponse();
        if (handler instanceof RemotingDownloadHandler<?>) {
            var rdh = (RemotingDownloadHandler<Object>) handler;
            var rp = rdh.createResource(rq, context);
            if (rp == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            String range = context.getHttpRequest().getHeader("Range");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(context.getHttpRequest().getServletContext().getMimeType(rp.getFileName()));
            response.setHeader("Content-Length", rp.getContentLength().toString());
            // response.setHeader("Content-Disposition", "attachement;filename=\"" + URLEncoder.encode(rp.getFileName(), StandardCharsets.UTF_8) + "\"");
            try (var is = rp.getInputStream()) {
                try (var os = response.getOutputStream()) {
                    IoUtils.copy(is, os);
                    os.flush();
                }
            }
            return;
        }
        var bfh = (RemotingBigFileHandler<Object>) handler;
        var file = bfh.getFile(rq, context);
        PartialDownloadHelper.processPartialDownload(context.getHttpRequest(), response, file);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        var pathInfo = req.getPathInfo();
        var sc = scHandlersCache.get(pathInfo);
        if (sc != null) {
            processServerCall(req, resp, sc);
            return;
        }
        var parts = pathInfo.split("/");
        var remoting = RemotingMetaRegistry.get().getRemotings().get(parts[1]);
        var group = remoting.getGroups().get(parts[2]);
        var serverCall = group.getServerCalls().get(parts[3]);
        if (serverCall != null) {
            var pair = new Pair(ReflectionFactory.get().newInstance(serverCall.getAttributes().get("handler-class-name")), new Pair<>(remoting, serverCall));
            scHandlersCache.put(pathInfo, pair);
            processServerCall(req, resp, pair);
            return;
        }
        throw new IllegalArgumentException("unable to map %s".formatted(pathInfo));
    }

    private void processServerCall(HttpServletRequest req, HttpServletResponse resp, Pair<RemotingServerCallHandler<?, ?>, Pair<RemotingDescription, RemotingServerCallDescription>> sc) {
        var rid = sc.value().key().getId();
        var context = new RemotingCallContext();
        context.setHttpRequest(req);
        context.setHttpResponse(resp);
        context.setParameter(StandardRemotingParameters.REMOTING_DESCRIPTION, sc.value().key());
        context.setParameter(StandardRemotingParameters.SERVER_CALL_DESCRIPTION, sc.value().value());
        ExceptionUtils.wrapException(() -> processServerCall(context, (RemotingServerCallHandler<Object, Object>) sc.key(), RemotingRegistry.get().getAdvices(), 0));
    }

    private void processServerCall(RemotingCallContext context, RemotingServerCallHandler<Object, Object> handler, List<RemotingAdvice> advices, int idx) throws Exception {
        if (idx == advices.size()) {
            processServerCallInternal(context, handler);
            return;
        }
        RemotingRegistry.get().getAdvices().get(idx).onServerCall(context, (context2) ->
                processServerCall(context2, handler, RemotingRegistry.get().getAdvices(), idx + 1)
        );
    }

    private void processServerCallInternal(RemotingCallContext context, RemotingServerCallHandler<Object, Object> handler) throws Exception {
        try {
            RemotingServerCallDescription scd = context.getParameter(StandardRemotingParameters.SERVER_CALL_DESCRIPTION);
            Object rq = null;
            if (scd.getRequestClassName() != null) {
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
