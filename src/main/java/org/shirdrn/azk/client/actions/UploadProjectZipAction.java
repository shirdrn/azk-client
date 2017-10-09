package org.shirdrn.azk.client.actions;

import org.shirdrn.azk.client.common.AbstractAction;
import org.shirdrn.azk.client.constants.AjaxCmd;
import org.shirdrn.azk.client.constants.ConfigKeys;
import com.google.common.base.Throwables;
import okhttp3.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;

public class UploadProjectZipAction extends AbstractAction {

    private static final Log LOG = LogFactory.getLog(UploadProjectZipAction.class);

    public UploadProjectZipAction() {
        super(AjaxCmd.UPLOAD);
    }

    /**
     * for uploading a project zip file
     * @return
     */
    @Override
    public void operate() {
        String url = context.get(ConfigKeys.AZKABAN_SERVER_URL)+"/manager";
        final MediaType zipMediaType = MediaType.parse("application/zip");
        try {
            final File zipFile = new File(context.get(ConfigKeys.JOB_ZIP_FILE));
            RequestBody fileBody = RequestBody.create(zipMediaType, zipFile);
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("session.id", context.get("session.id"))
                    .addFormDataPart("ajax","upload")
                    .addFormDataPart("project", context.get(ConfigKeys.PROJECT_NAME))
                    .addFormDataPart("file", zipFile.getName(), fileBody)
                    .build();
            Request request = new Request
                    .Builder()
                    .header("Content-Type", "multipart/mixed")
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                String res = response.body().string();
                logResponse(LOG, res);
            }
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }
}
