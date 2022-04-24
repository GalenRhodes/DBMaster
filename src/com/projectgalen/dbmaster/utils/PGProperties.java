package com.projectgalen.dbmaster.utils;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class PGProperties extends Properties {

    public static final @Language("RegExp") String  DEFAULT_SEP_PATTERN = "\\s*,\\s*";
    public static final @Language("RegExp") String  DEFAULT_KV_PATTERN  = "\\s*:\\s*";
    public static final                     Pattern MACRO_REGEX         = Pattern.compile("(?<!\\\\)\\$\\{([}]+)}");

    public PGProperties() {
        super();
    }

    public PGProperties(Properties defaults) {
        super(defaults);
    }

    public PGProperties(InputStream in) {
        this(in, null, false);
    }

    public PGProperties(InputStream in, Properties defaults) {
        this(in, defaults, false);
    }

    public PGProperties(InputStream in, boolean isXML) {
        this(in, null, isXML);
    }

    public PGProperties(InputStream in, Properties defaults, boolean isXML) {
        super(defaults);
        try {
            if(isXML) loadFromXML(in);
            else load(in);
        }
        catch(Exception e) { throw new RuntimeException(e); }
    }

    public byte getByte(@NotNull String key, byte def) {
        try { return Byte.parseByte(getProperty(key, String.valueOf(def))); } catch(Exception e) { return def; }
    }

    public byte getByte(@NotNull String key) {
        return getByte(key, (byte)0);
    }

    public double getDouble(@NotNull String key, double def) {
        try { return Double.parseDouble(getProperty(key, String.valueOf(def))); } catch(Exception e) { return def; }
    }

    public double getDouble(@NotNull String key) {
        return getDouble(key, 0);
    }

    public float getFloat(@NotNull String key, float def) {
        try { return Float.parseFloat(getProperty(key, String.valueOf(def))); } catch(Exception e) { return def; }
    }

    public float getFloat(@NotNull String key) {
        return getFloat(key, (float)0);
    }

    public int getInt(@NotNull String key, int def) {
        try { return Integer.parseInt(getProperty(key, String.valueOf(def))); } catch(Exception e) { return def; }
    }

    public int getInt(@NotNull String key) {
        return getInt(key, 0);
    }

    public List<String> getList(@NotNull @NonNls String key, @NotNull @Language("RegExp") @NonNls String sepPattern, @Nullable List<String> defaults) {
        String data = getProperty(key);
        if(data == null) return defaults;
        return new ArrayList<>(Arrays.asList(data.split(sepPattern, -1)));
    }

    public @NotNull List<String> getList(@NotNull @NonNls String key) {
        return getList(key, DEFAULT_SEP_PATTERN, new ArrayList<>());
    }

    public @NotNull List<String> getList(@NotNull @NonNls String key, @NotNull @Language("RegExp") @NonNls String sepPattern) {
        return getList(key, sepPattern, new ArrayList<>());
    }

    public List<String> getList(@NotNull @NonNls String key, @Nullable List<String> defaults) {
        return getList(key, DEFAULT_SEP_PATTERN, defaults);
    }

    public long getLong(@NotNull String key, long def) {
        try { return Long.parseLong(getProperty(key, String.valueOf(def))); } catch(Exception e) { return def; }
    }

    public long getLong(@NotNull String key) {
        return getLong(key, 0);
    }

    public Map<String, String> getMap(@NotNull @NonNls String key,
                                      @NotNull @Language("RegExp") @NonNls String sepPattern,
                                      @NotNull @Language("RegExp") @NonNls String kvPattern,
                                      @Nullable Map<String, String> defaults) {
        List<String> list = getList(key, sepPattern, null);
        if(list == null) return defaults;
        Map<String, String> map = new LinkedHashMap<>();
        for(String s : list) {
            String[] kv = s.split(kvPattern, 2);
            if(kv.length == 2) map.put(kv[0], kv[1]);
        }
        return map;
    }

    public @NotNull Map<String, String> getMap(@NotNull @NonNls String key) {
        return getMap(key, DEFAULT_SEP_PATTERN, DEFAULT_KV_PATTERN, new LinkedHashMap<>());
    }

    public @NotNull Map<String, String> getMap(@NotNull @NonNls String key,
                                               @NotNull @Language("RegExp") @NonNls String sepPattern,
                                               @NotNull @Language("RegExp") @NonNls String kvPattern) {
        return getMap(key, sepPattern, kvPattern, new LinkedHashMap<>());
    }

    public Map<String, String> getMap(@NotNull @NonNls String key, @Nullable Map<String, String> defaults) {
        return getMap(key, DEFAULT_SEP_PATTERN, DEFAULT_KV_PATTERN, defaults);
    }

    public short getShort(@NotNull String key, short def) {
        try { return Short.parseShort(getProperty(key, String.valueOf(def))); } catch(Exception e) { return def; }
    }

    public short getShort(@NotNull String key) {
        return getShort(key, (short)0);
    }

    @Override
    public synchronized void load(Reader reader) throws IOException {
        try { super.load(reader); } finally { reader.close(); }
    }

    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        try { super.load(inStream); } finally { inStream.close(); }
    }

    @Override
    public synchronized void loadFromXML(InputStream in) throws IOException {
        try { super.loadFromXML(in); } finally { in.close(); }
    }

    public synchronized void loadFromXML(Reader reader, Charset cs) throws IOException {
        ExecutorService svc = Executors.newSingleThreadExecutor();
        try(PipedOutputStream out = new PipedOutputStream(); PipedInputStream in = new PipedInputStream(out, 65536); OutputStreamWriter w = new OutputStreamWriter(out, cs)) {
            IO.asyncCopy(svc, reader, w, true, false);
            super.loadFromXML(in);
        }
        finally {
            svc.shutdown();
        }
    }
}
