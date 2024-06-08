/*
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gridnine.platform.elsa.common.core.utils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class IoUtils {
    private static final int COPY_BUF_SIZE = 8024;

    public static byte[] gunzip(byte[] input) {
        return ExceptionUtils.wrapException(() -> {
            try (var is = new GZIPInputStream(new ByteArrayInputStream(input))) {
                var baos = new ByteArrayOutputStream();
                copy(is, baos, COPY_BUF_SIZE);
                return baos.toByteArray();
            }
        });
    }

    public static byte[] gzip(byte[] data) throws IOException {
        var baos3 = new ByteArrayOutputStream();
        try (var os = new GZIPOutputStream(baos3)) {
            copy(new ByteArrayInputStream(data), os, COPY_BUF_SIZE);
        }
        return baos3.toByteArray();
    }

    public static long copy(final InputStream input, final OutputStream output) throws IOException {
        return copy(input, output, COPY_BUF_SIZE);
    }

    public static long copy(final InputStream input, final OutputStream output, final int buffersize) throws IOException {
        if (buffersize < 1) {
            throw new IllegalArgumentException("buffersize must be bigger than 0");
        }
        final byte[] buffer = new byte[buffersize];
        int n;
        long count = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException ignored) {}
    }
}
