package org.shirdrn.azk.client.common;

import java.util.Iterator;
import java.util.Properties;

public abstract class AbstractContext implements ContextReadable, ContextWriteable {

    protected final Properties properties = new Properties();

    private <T> void setValue(String key, T value) {
        properties.put(key, value);
    }

    @SuppressWarnings("unchecked")
    private <T> T getValue(String key, T defaultValue) {
        T value = (T) properties.get(key);
        if(value == null) {
            value = defaultValue;
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private <T> T getValue(String key) {
        return (T) properties.get(key);
    }


    @Override
    public void set(String key, String value) {
        setValue(key, value);
    }

    @Override
    public void setByte(String key, byte value) {
        setValue(key, value);
    }

    @Override
    public void setShort(String key, short value) {
        setValue(key, value);
    }

    @Override
    public void setInt(String key, int value) {
        setValue(key, value);
    }

    @Override
    public void setLong(String key, long value) {
        setValue(key, value);
    }

    @Override
    public void setFloat(String key, float value) {
        setValue(key, value);
    }

    @Override
    public void setDouble(String key, double value) {
        setValue(key, value);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        setValue(key, value);
    }

    @Override
    public <T> void setObject(String key, T value) {
        setValue(key, value);
    }

    @Override
    public String get(String key) {
        return getValue(key);
    }

    @Override
    public String get(String key, String defaultValue) {
        return getValue(key, defaultValue);
    }

    @Override
    public byte getByte(String key, byte defaultValue) {
        byte value = defaultValue;
        String v = getValue(key);
        try {
            value = Byte.parseByte(v);
        } catch (Exception e) { }
        return value;
    }

    @Override
    public short getShort(String key, short defaultValue) {
        short value = defaultValue;
        String v = getValue(key);
        try {
            value = Short.parseShort(v);
        } catch (Exception e) { }
        return value;
    }

    @Override
    public int getInt(String key, int defaultValue) {
        int value = defaultValue;
        String v = getValue(key);
        try {
            value = Integer.parseInt(v);
        } catch (Exception e) { }
        return value;
    }

    @Override
    public long getLong(String key, long defaultValue) {
        long value = defaultValue;
        String v = getValue(key);
        try {
            value = Long.parseLong(v);
        } catch (Exception e) { }
        return value;
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        float value = defaultValue;
        String v = getValue(key);
        try {
            value = Float.parseFloat(v);
        } catch (Exception e) { }
        return value;
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        double value = defaultValue;
        String v = getValue(key);
        try {
            value = Double.parseDouble(v);
        } catch (Exception e) { }
        return value;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        boolean value = defaultValue;
        String v = getValue(key);
        try {
            value = Boolean.parseBoolean(v);
        } catch (Exception e) { }
        return value;
    }

    @Override
    public <T> T getObject(String key, T defaultValue) {
        return getValue(key, defaultValue);
    }

    @Override
    public String[] getStringArray(String key, String defaultValue) {
        String v = getValue(key, defaultValue);
        return v.split(",");
    }

    @Override
    public Iterator<Object> keyIterator() {
        return properties.keySet().iterator();
    }
}
