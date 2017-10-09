package org.shirdrn.azk.client.common;

/**
 * A writeable context protocol.
 *
 * @author yanjun
 */
public interface ContextWriteable {

    void set(String key, String value);
    void setByte(String key, byte value);
    void setShort(String key, short value);
    void setInt(String key, int value);
    void setLong(String key, long value);
    void setFloat(String key, float value);
    void setDouble(String key, double value);
    void setBoolean(String key, boolean value);
    <T> void setObject(String key, T value);
}
