package com.projectgalen.dbmaster.utils;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public final class IO {

    private IO() {
    }

    public static Future<Long> asyncCopy(ExecutorService executor, InputStream in, OutputStream out, boolean closeInput, boolean closeOutput) {
        return executor.submit(() -> copy(in, out, closeInput, closeOutput));
    }

    public static Future<Long> asyncCopy(ExecutorService executor, InputStream in, OutputStream out) {
        return asyncCopy(executor, in, out, true, true);
    }

    public static Future<Long> asyncCopy(ExecutorService executor, Reader in, Writer out, boolean closeInput, boolean closeOutput) {
        return executor.submit(() -> copy(in, out, closeInput, closeOutput));
    }

    public static Future<Long> asyncCopy(ExecutorService executor, Reader in, Writer out) {
        return asyncCopy(executor, in, out, true, true);
    }

    public static void closeQuietly(Closeable c) {
        try { c.close(); } catch(Exception ignore) { }
    }

    public static long copy(Reader in, Writer out) throws IOException {
        return copy(in, out, true, true);
    }

    public static long copy(Reader in, Writer out, boolean closeInput, boolean closeOutput) throws IOException {
        try {
            long   cc = 0;
            char[] b  = new char[65536];
            int    c  = in.read(b);

            while(c >= 0) {
                out.write(b, 0, c);
                cc += c;
                c = in.read(b);
            }

            return cc;
        }
        finally {
            if(closeInput) closeQuietly(in);
            if(closeOutput) closeQuietly(out);
        }
    }

    public static long copy(InputStream in, OutputStream out) throws IOException {
        return copy(in, out, true, true);
    }

    public static long copy(InputStream in, OutputStream out, boolean closeInput, boolean closeOutput) throws IOException {
        try {
            long   cc = 0;
            byte[] b  = new byte[65536];
            int    c  = in.read(b);

            while(c >= 0) {
                out.write(b, 0, c);
                cc += c;
                c = in.read(b);
            }

            return cc;
        }
        finally {
            if(closeInput) closeQuietly(in);
            if(closeOutput) closeQuietly(out);
        }
    }
}
