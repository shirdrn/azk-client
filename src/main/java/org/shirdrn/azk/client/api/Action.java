package org.shirdrn.azk.client.api;

import org.shirdrn.azk.client.common.Context;
import org.shirdrn.azk.client.constants.AjaxCmd;

public interface Action {

    void operate();
    AjaxCmd getCmd();
    void setContext(Context context);
}
