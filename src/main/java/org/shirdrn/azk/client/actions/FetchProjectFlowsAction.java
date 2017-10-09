package org.shirdrn.azk.client.actions;

import org.shirdrn.azk.client.common.AbstractAction;
import org.shirdrn.azk.client.constants.AjaxCmd;
import org.shirdrn.azk.client.constants.ConfigKeys;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class FetchProjectFlowsAction extends AbstractAction {

    private static final Log LOG = LogFactory.getLog(FetchProjectFlowsAction.class);

    public FetchProjectFlowsAction() {
        super(AjaxCmd.FETCH_PROJECT_FLOWS);
    }

    @Override
    public void operate() {
        String url = context.get(ConfigKeys.AZKABAN_SERVER_URL) +
                "/manager?ajax=fetchprojectflows" +
                "&session.id=" + context.get("session.id") +
                "&project=" + context.get(ConfigKeys.PROJECT_NAME);
        try {
            Request request = new Request.Builder().url(url).get().build();
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                String res = response.body().string();
                logResponse(LOG, res);
                JSONObject j = JSONObject.parseObject(res);
                context.set("projectId", j.getString("projectId"));
                JSONArray a = j.getJSONArray("flows");
                if(!a.isEmpty()) {
                    context.setObject("flows", a);
                }
            }
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }
}
