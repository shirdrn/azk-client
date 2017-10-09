package org.shirdrn.azk.client.constants;

public enum AzkCmd {
    PUBLISH("publish"),
    EXEC("exec"),
    DELETE("delete");

    private String cmd;

    AzkCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }
}
