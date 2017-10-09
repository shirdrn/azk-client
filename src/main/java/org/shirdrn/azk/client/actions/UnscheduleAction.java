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
import java.util.List;

public class UnscheduleAction extends AbstractAction {

    private static final Log LOG = LogFactory.getLog(UnscheduleAction.class);

    public UnscheduleAction() {
        super(AjaxCmd.UNSCHEDULE);
    }

    @Override
    public void operate() {
        List<String> scheduleIds = (List<String>) context.getObject("scheduleIds", null);
        String url = context.get(ConfigKeys.AZKABAN_SERVER_URL)+"/schedule?action=removeSched";
        LOG.info("Requesting to remove scheduleIds: " + scheduleIds);
        if(scheduleIds != null) {
            for(String scheduleId : scheduleIds) {
                try {
                    RequestBody body = new FormBody.Builder()
                            .add("session.id", context.get("session.id"))
                            .add("scheduleId", scheduleId)
                            .build();
                    Request request = new Request.Builder().url(url).post(body).build();
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()) {
                        String res = response.body().string();
                        LOG.info("Schedule removed: scheduleId=" + scheduleId);
                        logResponse(LOG, res);
                    }
                } catch (IOException e) {
                    Throwables.propagate(e);
                }
            }
        }
    }
}
