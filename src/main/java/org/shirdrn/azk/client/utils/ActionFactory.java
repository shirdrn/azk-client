package org.shirdrn.azk.client.utils;

import org.shirdrn.azk.client.api.Action;
import org.shirdrn.azk.client.api.ObjectFactory;
import org.shirdrn.azk.client.constants.AjaxCmd;
import com.google.common.collect.Maps;
import org.shirdrn.azk.client.actions.*;

import java.util.Map;

public class ActionFactory implements ObjectFactory<Action> {

    private final Map<AjaxCmd, Class<? extends Action>> actions = Maps.newHashMap();

    public ActionFactory() {
        super();
        actions.put(AjaxCmd.AUTH, AuthAction.class);
        actions.put(AjaxCmd.CREATE, CreateProjectAction.class);
        actions.put(AjaxCmd.DELETE, DeleteProjectAction.class);
        actions.put(AjaxCmd.UPLOAD, UploadProjectZipAction.class);
        actions.put(AjaxCmd.SCHEDULE, ScheduleCronAction.class);
        actions.put(AjaxCmd.FETCH_SCHEDULE, FetchScheduleAction.class);
        actions.put(AjaxCmd.UNSCHEDULE, UnscheduleAction.class);
        actions.put(AjaxCmd.FETCH_PROJECT_FLOWS, FetchProjectFlowsAction.class);
        actions.put(AjaxCmd.EXECUTE_FLOW, ExecuteFlowAction.class);
    }

    @Override
    public Action createObject(Enum<?> cmd) {
        Class<? extends Action> realClazz = actions.get(cmd);
        return ReflectionUtils.newInstance(realClazz, Action.class);
    }
}
