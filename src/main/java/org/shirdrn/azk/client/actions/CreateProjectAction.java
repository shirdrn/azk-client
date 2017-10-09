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

public class CreateProjectAction extends AbstractAction {

    private static final Log LOG = LogFactory.getLog(CreateProjectAction.class);

    public CreateProjectAction() {
        super(AjaxCmd.CREATE);
    }

    @Override
    public void operate() {
        String url = context.get(ConfigKeys.AZKABAN_SERVER_URL)+"/manager?action=create";
        try {
            RequestBody body = new FormBody.Builder()
                    .add("session.id", context.get("session.id"))
                    .add("name", context.get(ConfigKeys.PROJECT_NAME))
                    .add("description", context.get(ConfigKeys.PROJECT_DESCRIPTION))
                    .build();
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            logResponse(LOG, response.body().string());
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }
}
