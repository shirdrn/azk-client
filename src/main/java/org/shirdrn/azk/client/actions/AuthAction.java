package org.shirdrn.azk.client.actions;

import org.shirdrn.azk.client.common.AbstractAction;
import org.shirdrn.azk.client.constants.AjaxCmd;
import org.shirdrn.azk.client.constants.ConfigKeys;
import com.google.common.base.Throwables;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class AuthAction extends AbstractAction {

    private static final Log LOG = LogFactory.getLog(AuthAction.class);

    public AuthAction() {
        super(AjaxCmd.AUTH);
    }

    /**
     * to help authenticate a user and provides a session.id in response.
     * @return
     */
    @Override
    public void operate() {
        try {
            RequestBody body = new FormBody.Builder()
                    .add("username", context.get(ConfigKeys.AZKABAN_USER))
                    .add("password", context.get(ConfigKeys.AZKABAN_PASSWORD))
                    .build();
            String url = context.get(ConfigKeys.AZKABAN_SERVER_URL) + "/manager?action=login";
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                context.set("session.id", response.headers().get("Set-Cookie").substring(27, 63));
                logResponse(LOG, response.body().string());
            }
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }
}
