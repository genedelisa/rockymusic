package com.rockhoppertech.music.fx.cmn.model;

/*
 * #%L
 * Rocky Music FX
 * %%
 * Copyright (C) 1996 - 2014 Rockhopper Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javafx.geometry.Point2D;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class SymbolFactory {
    private static final Logger logger = LoggerFactory
            .getLogger(SymbolFactory.class);

    static {
        readJson();
    }

    private static JsonObject glyphs;
    private static JsonObject fontMetaData;
    private static JsonObject engravingDefaults;

    /**
     * No instantiation.
     */
    private SymbolFactory() {

    }

    public static final String getGlyph(final String name) {
        if (glyphs == null) {
            logger.debug("null glyphs!");
            return null;
        }
        String s = glyphs.getJsonObject(name).getString(
                "codepoint");
        // the string will have this prefix: E+, so skip that.
        int i = Integer.parseInt(s.substring(1), 16);
        return unicodeToString(i);
    }

    public static final String unicodeToString(final int codePoint) {
        // for equivalence with U+n, below would be 0xnnnn
        // converting to char[] pair
        char[] charPair = Character.toChars(codePoint);
        // and to String, containing the character we want
        String symbol = new String(charPair);
        return symbol;
    }

    // symbol accessors
    public static final String flat() {
        return getGlyph("accidentalFlat");
    }
    
    public static final String sharp() {
        return getGlyph("accidentalSharp");
    }

    public static final String noteQuarterUp() {
        return getGlyph("noteQuarterUp");
    }

    public static final String noteQuarterDown() {
        return getGlyph("noteQuarterDown");
    }

    public static final String noteQuarterUpFlat() {
        return flat() + getGlyph("noteQuarterUp");
    }

    public static final String noteQuarterDownFlat() {
        return flat() + getGlyph("noteQuarterDown");
    }
    
    public static final String noteQuarterUpSharp() {
        return sharp() + getGlyph("noteQuarterUp");
    }

    public static final String noteQuarterDownSharp() {
        return sharp() + getGlyph("noteQuarterDown");
    }
    
    public static final String noteheadBlack() {
        return getGlyph("noteheadBlack");
    }
    
    public static final String gClef() {
        return getGlyph("gClef");
    }
    public static final String fClef() {
        return getGlyph("fClef");
    }
    public static final String cClef() {
        return getGlyph("cClef");
    }
    
    public static final String staff5Lines() {
        return getGlyph("staff5Lines");
    }
    public static final String staff1Line() {
        return getGlyph("staff1Line");
    }
    public static final String staff1LineWide() {
        return getGlyph("staff1LineWide");
    }
    public static final String augmentationDot() {
        return getGlyph("augmentationDot");
    }
    
    public static final String timeSig4() {
        return getGlyph("timeSig4");
    }
    
    // 
    public static final String flag8thDown() {
        return getGlyph("flag8thDown");
    }
    public static final String flag8thUp() {
        return getGlyph("flag8thUp");
    }
    public static final String flag16thUp() {
        return getGlyph("flag16thUp");
    }
    public static final String flag16thDown() {
        return getGlyph("flag16thDown");
    }
    
    
    
    
    
    public static final String noteWhole() {
        return getGlyph("noteWhole");
    }
    public static final String noteheadHalf() {
        return getGlyph("noteheadHalf");
    }
    public static final String noteHalfUp() {
        return getGlyph("noteHalfUp");
    }
    public static final String noteHalfDown() {
        return getGlyph("noteHalfDown");
    }
    public static final String note16thUp() {
        return getGlyph("note16thUp");
    }
    public static final String note16thDown() {
        return getGlyph("note16thDown");
    }
    public static final String note8thDown() {
        return getGlyph("note8thDown");
    }
    public static final String note8thUp() {
        return getGlyph("note8thUp");
    }
    
    // flag8thUp 
    
    public static final String rest1024th() {
        return getGlyph("rest1024th");
    }
    public static final String rest128th() {
        return getGlyph("rest128th");
    }
    public static final String rest16th() {
        return getGlyph("rest16th");
    }
    public static final String restWhole() {
        return getGlyph("restWhole");
    }
    public static final String restQuarter() {
        return getGlyph("restQuarter");
    }
    public static final String restHalf() {
        return getGlyph("restHalf");
    }
    
    public static final String restDoubleWhole() {
        return getGlyph("restDoubleWhole");
    }

    public static final String rest8th() {
        return getGlyph("rest8th");
    }

    public static final String rest32nd() {
        return getGlyph("rest32nd");
    }

    public static final String rest64th() {
        return getGlyph("rest64th");
    }

    public static final String rest256th() {
        return getGlyph("rest256th");
    }
    

//
//    "rest512th": {
//    "restHBar": {
//    "restHBarLeft": {
//    "restHBarRight": {
//    "restLonga": {
//    "restQuarterOld": {


    
    
    
    
    
    

    // meta accessors
    public static final double getStemThickness() {
        JsonNumber stemThickness = engravingDefaults
                .getJsonNumber("stemThickness");
        return stemThickness.doubleValue();
    }

    public static double getStaffLineThickness() {
        JsonNumber stemThickness = engravingDefaults
                .getJsonNumber("staffLineThickness");
        return stemThickness.doubleValue();
    }

    public static final Point2D getStemUpNW(final String notehead) {
        JsonObject metag = fontMetaData.getJsonObject("glyphs");
        // JsonObject jobj = metag.getJsonObject("noteheadBlack");
        JsonObject jobj = metag.getJsonObject(notehead);
        JsonArray stemDownNW = jobj.getJsonArray("stemUpNW");
        Point2D p = new Point2D(stemDownNW.getJsonNumber(0).doubleValue(),
                stemDownNW.getJsonNumber(1).doubleValue());
        return p;
    }
    public static final Point2D getStemDownNW(final String notehead) {
        JsonObject metag = fontMetaData.getJsonObject("glyphs");
        // JsonObject jobj = metag.getJsonObject("noteheadBlack");
        JsonObject jobj = metag.getJsonObject(notehead);
        JsonArray stemDownNW = jobj.getJsonArray("stemDownNW");
        Point2D p = new Point2D(stemDownNW.getJsonNumber(0).doubleValue(),
                stemDownNW.getJsonNumber(1).doubleValue());
        return p;
    }

    public static final Point2D getStemUpSE(final String notehead) {
        JsonObject metag = fontMetaData.getJsonObject("glyphs");
        logger.debug("metag '{}'", metag);
        JsonObject jobj = metag.getJsonObject(notehead);
        logger.debug("for glyph '{}'", notehead);
        logger.debug("jobj for glyph '{}'", jobj);
        JsonArray jarray = jobj.getJsonArray("stemUpSE");
        // System.err.println(stemDownNW.getJsonNumber(0));
        // System.err.println(stemDownNW.getJsonNumber(1));
        Point2D p = new Point2D(jarray.getJsonNumber(0).doubleValue(),
                jarray.getJsonNumber(1).doubleValue());
        return p;
    }

    public static Point2D stemDownSW(String string) {
        JsonObject metag = fontMetaData.getJsonObject("glyphs");
        logger.debug("metag '{}'", metag);
        JsonObject jobj = metag.getJsonObject(string);
        logger.debug("for glyph '{}'", string);
        logger.debug("jobj for glyph '{}'", jobj);
        JsonArray jarray = jobj.getJsonArray("stemDownSW");
        Point2D p = new Point2D(jarray.getJsonNumber(0).doubleValue(),
                jarray.getJsonNumber(1).doubleValue());
        return p;
    }

    private static void readJson() {
        URL url = SymbolFactory.class.getResource("/fonts/glyphnames.json");
        InputStream is;
        try {
            is = url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        JsonReader reader = Json.createReader(is);
        glyphs = reader.readObject();
        reader.close();

        url = SymbolFactory.class.getResource("/fonts/bravura_metadata.json");
        try {
            is = url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        reader = Json.createReader(is);
        fontMetaData = reader.readObject();
        reader.close();
        engravingDefaults = fontMetaData
                .getJsonObject("engravingDefaults");

        // "noteheadBlack": {
        // "stemDownNW": [
        // 0.0,
        // -0.184
        // ],
        // "stemUpSE": [
        // 1.328,
        // 0.184
        // ]
        // },

        // for (String key : glyphs.keySet()) {
        // JsonObject result = glyphs.getJsonObject(key);
        // System.err.printf(
        // "bravuraFontMap.put(\"%s\", 0x%s);\n",
        // key,
        // result.getString("codepoint").substring(2));
        // }
        // System.err.println(result.getString("alternateCodepoint"));
    }

   
}
