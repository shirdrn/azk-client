package org.shirdrn.azk.client.actions;

import org.shirdrn.azk.client.common.AbstractAction;
import org.shirdrn.azk.client.constants.AjaxCmd;
import org.shirdrn.azk.client.constants.ConfigKeys;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.List;

public class FetchScheduleAction extends AbstractAction {

    private static final Log LOG = LogFactory.getLog(FetchScheduleAction.class);

    public FetchScheduleAction() {
        super(AjaxCmd.FETCH_SCHEDULE);
    }

    @Override
    public void operate() {
        Object obj = context.getObject("flows", null);
        List<String> scheduleIds = Lists.newArrayList();
        if(obj !=null) {
            JSONArray a = (JSONArray) obj;
            for (int i = 0; i < a.size(); i++) {
                JSONObject o = a.getJSONObject(i);
                String flowId = o.getString("flowId");
                LOG.info("Fetch schedules for: flowId=" + flowId);
                String url = context.get(ConfigKeys.AZKABAN_SERVER_URL) +
                        "/schedule?ajax=fetchSchedule" +
                        "&session.id=" + context.get("session.id") +
                        "&projectId=" + context.get("projectId") +
                        "&flowId=" + flowId;
                try {
                    Request request = new Request.Builder().url(url).get().build();
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject j = JSONObject.parseObject(res);
                        logResponse(LOG, res);
                        JSONObject jsonSchedule = j.getJSONObject("schedule");
                        // A scheduleId for delete actions are not present!
						if (jsonSchedule != null) { // prevent for NPE by delete actions
							String scheduleId = jsonSchedule.getString("scheduleId");
							LOG.info("Schedule fetched: scheduleId=" + scheduleId);
							scheduleIds.add(scheduleId);
						}
                        
                    }
                } catch (IOException e) {
                    Throwables.propagate(e);
                }
            }
            context.setObject("scheduleIds", scheduleIds);
        }
    }
}
