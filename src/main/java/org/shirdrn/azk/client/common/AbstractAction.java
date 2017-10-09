package org.shirdrn.azk.client.common;

import org.shirdrn.azk.client.api.Action;
import org.shirdrn.azk.client.constants.AjaxCmd;
import okhttp3.OkHttpClient;
import org.apache.commons.logging.Log;

public abstract class AbstractAction implements Action {

    protected final AjaxCmd command;
    protected Context context;
    protected OkHttpClient client = new OkHttpClient();

    public AbstractAction(AjaxCmd command) {
        super();
        this.command = command;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    public OkHttpClient getClient() {
        return client;
    }

    @Override
    public AjaxCmd getCmd() {
        return command;
    }

    protected void logResponse(Log log, String response) {
        log.info("Response(" + command + "): " + response);
    }
}
