package org.shirdrn.azk.client.actions;

import org.shirdrn.azk.client.common.AbstractAction;
import org.shirdrn.azk.client.constants.AjaxCmd;
import org.shirdrn.azk.client.constants.ConfigKeys;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class DeleteProjectAction extends AbstractAction {

    private static final Log LOG = LogFactory.getLog(DeleteProjectAction.class);

    public DeleteProjectAction() {
        super(AjaxCmd.DELETE);
    }

    @Override
    public void operate() {
        String url = context.get(ConfigKeys.AZKABAN_SERVER_URL) +
                "/manager?session.id=" + context.get("session.id") +
                "&delete=true" +
                "&project=" + context.get(ConfigKeys.PROJECT_NAME);
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                JSONObject res = new JSONObject();
                res.put("project", context.get(ConfigKeys.PROJECT_NAME));
                res.put("message", "Delete a project from Azkaban server.");
                res.put("status", "success");
                logResponse(LOG, res.toJSONString());
            }
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }
}
