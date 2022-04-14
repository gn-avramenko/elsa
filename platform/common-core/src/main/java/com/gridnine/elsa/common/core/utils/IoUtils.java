/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.utils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class IoUtils {
    private static final int COPY_BUF_SIZE = 8024;

    public static byte[] gunzip(byte[] input) {
        return ExceptionUtils.wrapException(() ->{
            try(var is = new GZIPInputStream(new ByteArrayInputStream(input))){
                var baos = new ByteArrayOutputStream();
                copy(is, baos, COPY_BUF_SIZE);
                return baos.toByteArray();
            }
        });
    }

    public static byte[] gzip(byte[] data) throws IOException {
        var baos3 = new ByteArrayOutputStream();
        try(var os = new GZIPOutputStream(baos3)){
           copy(new ByteArrayInputStream(data), os, COPY_BUF_SIZE);
        }
        return baos3.toByteArray();
    }
    public static long copy(final InputStream input, final OutputStream output) throws IOException {
        return copy(input, output, COPY_BUF_SIZE);
    }
    private static long copy(final InputStream input, final OutputStream output, final int buffersize) throws IOException {
        if (buffersize < 1) {
            throw new IllegalArgumentException("buffersize must be bigger than 0");
        }
        final byte[] buffer = new byte[buffersize];
        int n = 0;
        long count=0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
