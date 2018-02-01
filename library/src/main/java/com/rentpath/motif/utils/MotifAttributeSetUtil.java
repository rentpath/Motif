package com.rentpath.motif.utils;

import android.util.AttributeSet;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class MotifAttributeSetUtil {

    public static AttributeSet generateAttributeSet(AttributeSet preAttrSet, Map<String, String> additionalAttrs) {
        AttributeSet newAttrs = configureAttributeSet(checkDuplicates(configureAttributesMap(preAttrSet), additionalAttrs), additionalAttrs);
        if (newAttrs != null) {
            return newAttrs;
        }

        return preAttrSet;
    }

    private static Map<String, String> configureAttributesMap(AttributeSet preAttrSet) {
        Map<String, String> values = new HashMap<>();

        for (int i = 0; i < preAttrSet.getAttributeCount(); i++) {
            values.put(preAttrSet.getAttributeName(i), preAttrSet.getAttributeValue(i));
        }

        return values;
    }

    private static AttributeSet configureAttributeSet(Map<String, String> preValues, Map<String, String> postValues) {
        StringBuilder sb = new StringBuilder();
        sb.append("<attribute xmlns:android=\"http://schemas.android.com/apk/res/android\" ");
        sb.append("xmlns:app=\"http://schemas.android.com/apk/res-auto\" ");

        for (String key : preValues.keySet()) {
            sb.append("android:");
            sb.append(key);
            sb.append("=");
            sb.append("\"");
            sb.append(preValues.get(key));
            sb.append("\"");
            sb.append(" ");
        }

        for (String key : postValues.keySet()) {
            sb.append(key);
            sb.append("=");
            sb.append("\"");
            sb.append(postValues.get(key));
            sb.append("\"");
            sb.append(" ");
        }

        sb.append(" />");

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
//            factory.setValidating(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(sb.toString()));
            parser.next();
            return Xml.asAttributeSet(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Map<String, String> checkDuplicates(Map<String, String> preValues, Map<String, String> postValues) {
        for (String key : postValues.keySet()) {
            if (preValues.keySet().contains(key)) {
                preValues.remove(key);
            }
        }

        return preValues;
    }
}
