package org.shirdrn.azk.client.common;

import java.util.UUID;

/**
 * A in-memory object container, which can hold any key-value
 * pairs between multiple components.
 *
 * @author yanjun
 */
public final class ContextImpl extends AbstractContext implements Context {

    private final String name;

    public ContextImpl() {
        this(UUID.randomUUID().toString());
    }

    public ContextImpl(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
