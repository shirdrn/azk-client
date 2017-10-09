package org.shirdrn.azk.client.actions;

import org.shirdrn.azk.client.common.AbstractAction;
import org.shirdrn.azk.client.constants.AjaxCmd;
import org.shirdrn.azk.client.constants.ConfigKeys;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class ScheduleCronAction extends AbstractAction {

    private static final Log LOG = LogFactory.getLog(ScheduleCronAction.class);

    public ScheduleCronAction() {
        super(AjaxCmd.SCHEDULE);
    }

    @Override
    public void operate() {
        String url = context.get(ConfigKeys.AZKABAN_SERVER_URL)+"/schedule?ajax=scheduleCronFlow";
        try {
            String cronExpr = context.get(ConfigKeys.CRON_EXPR);
            Preconditions.checkArgument(cronExpr != null);
            Object obj = context.getObject("flows", null);
            if(obj !=null) {
                JSONArray a = (JSONArray) obj;
                for (int i = 0; i < a.size(); i++) {
                    JSONObject o = a.getJSONObject(i);
                    String flowId = o.getString("flowId");
                    LOG.info("Schedule flow: flowId=" + flowId + ", cronExpr=" + cronExpr);
                    RequestBody body = new FormBody.Builder()
                            .add("session.id", context.get("session.id"))
                            .add("projectName", context.get(ConfigKeys.PROJECT_NAME))
                            .add("flow", flowId)
                            .add("cronExpression", cronExpr)
                            .build();
                    Request request = new Request.Builder().url(url).post(body).build();
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()) {
                        String res = response.body().string();
                        LOG.info("Flow scheduled: flowId=" + flowId);
                        logResponse(LOG, res);
                    }
                }
            }
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }
}
