package com.projectgalen.dbmaster.prefs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonPropertyOrder({})
public class Preferences {
    private static final Properties  props         = new Properties();
    private static       Preferences __preferences = null;

    @JsonProperty("MainFrameSize") protected String mainFrameSize;

    public Preferences() {
    }

    public Dimension getMainFrameSize() {
        if(mainFrameSize == null) mainFrameSize = "1024x1000";
        Matcher m = Pattern.compile("([0-9]+)x([0-9]+)").matcher(mainFrameSize);
        if(!m.matches()) {
            Dimension d = new Dimension(1024, 1000);
            setMainFrameSize(d);
            return d;
        }
        return new Dimension(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
    }

    public void setMainFrameSize(Dimension size) {
        mainFrameSize = String.format("%dx%d", size.width, size.height);
    }

    public static synchronized Preferences getPreferences() {
        if(__preferences == null) {
            try {
                File file = new File("./DBMasterPrefs.json".replace('/', File.separatorChar));
                try(Reader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                    __preferences = new ObjectMapper().readValue(reader, Preferences.class);
                }
            }
            catch(Exception e) {
                __preferences = new Preferences();
            }
        }
        return __preferences;
    }
}
