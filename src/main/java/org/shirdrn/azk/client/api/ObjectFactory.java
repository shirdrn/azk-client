package org.shirdrn.azk.client.api;

public interface ObjectFactory<T> {

    public T createObject(Enum<?> cmd);
}
