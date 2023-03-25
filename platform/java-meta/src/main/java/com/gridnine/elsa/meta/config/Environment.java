/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unchecked")
public class Environment {
    private final static Logger log = LoggerFactory.getLogger(Environment.class);

    private static File rootFolder;

    private static File tempFolder;

    private static boolean test = false;

    private final static Map<Class<?>, PublishedEntry<?>> publishedObjects = new ConcurrentHashMap<>();

    private final static AtomicInteger counter = new AtomicInteger(0);

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void configure(File rootFolder) {
        Environment.rootFolder = rootFolder;
        log.debug("root folder is %s".formatted(rootFolder.getAbsolutePath()));
        tempFolder = new File(rootFolder, "temp");
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
        log.debug("temp folder is %s".formatted(tempFolder.getAbsolutePath()));
        log.info("configured");
    }

    public static File getRootFolder() {
        return rootFolder;
    }

    public static File getTempFolder() {
        return tempFolder;
    }

    public static void initTest(){
        test = true;
    }

    public static boolean isTest() {
        return test;
    }

    public static void dispose() {
        counter.set(0);
        var entries = publishedObjects.values().stream().sorted((a, b) -> b.order - a.order).toList();
        entries.forEach(it -> unpublish(it.cls));
    }


    public static <T, I extends T> void publish(Class<T> cls, I obj) {
        if (publishedObjects.containsKey(obj.getClass())) {
            throw new IllegalStateException("object of class %s is already published".formatted(cls.getName()));
        }
        publishedObjects.put(cls, new PublishedEntry<>(cls, obj, counter.incrementAndGet()));
        log.info("published %s of type %s".formatted(obj, cls.getName()));
    }

    public static <T> void publish(T obj) {
        publish((Class<T>) obj.getClass(), obj);
    }

    public static void unpublish(Class<?> cls) {
        var entry = publishedObjects.get(cls);
        if (entry != null) {
            entry.dispose();
            publishedObjects.remove(cls);
        }
    }

    public static boolean isPublished(Class<?> cls){
        return publishedObjects.containsKey(cls);
    }


    public static <T> T getPublished(Class<T> cls){
        var result = publishedObjects.get(cls);
        if(result == null){
            throw new IllegalStateException("object of class %s is not not published".formatted(cls.getName()));
        }
        return (T) result.object;
    }

    private record PublishedEntry<T>(Class<?> cls, T object, int order) {

        public void dispose() {
                if (object instanceof Disposable disp) {
                    try {
                        disp.dispose();
                    } catch (Throwable t) {
                        log.error("failed disposing %s".formatted(object), t);
                    }
                }
                log.info("disposed %s".formatted(object));
            }
        }
}
