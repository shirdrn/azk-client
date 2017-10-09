package org.shirdrn.azk.client.actions;

import org.shirdrn.azk.client.common.AbstractAction;
import org.shirdrn.azk.client.constants.AjaxCmd;
import org.shirdrn.azk.client.constants.ConfigKeys;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class ExecuteFlowAction extends AbstractAction {

    private static final Log LOG = LogFactory.getLog(ExecuteFlowAction.class);

    public ExecuteFlowAction() {
        super(AjaxCmd.EXECUTE_FLOW);
    }

    @Override
    public void operate() {
        Object obj = context.getObject("flows", null);
        if(obj !=null) {
            JSONArray a = (JSONArray) obj;
            for (int i = 0; i < a.size(); i++) {
                JSONObject o = a.getJSONObject(i);
                String flowId = o.getString("flowId");
                LOG.info("Schedule to execute flow: flowId=" + flowId);
                String url = context.get(ConfigKeys.AZKABAN_SERVER_URL) + "/executor?ajax=executeFlow";
                try {
                    RequestBody body = new FormBody.Builder()
                            .add("session.id", context.get("session.id"))
                            .add("project", context.get(ConfigKeys.PROJECT_NAME))
                            .add("flow", flowId)
                            .build();
                    Request request = new Request.Builder().url(url).post(body).build();
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject j = JSONObject.parseObject(res);
                        logResponse(LOG, res);
                        LOG.info("Flow executed: flowId=" + flowId);
                    }
                } catch (IOException e) {
                    Throwables.propagate(e);
                }
            }
        }
    }
}
