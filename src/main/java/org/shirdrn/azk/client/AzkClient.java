package org.shirdrn.azk.client;

import org.shirdrn.azk.client.api.Action;
import org.shirdrn.azk.client.api.ObjectFactory;
import org.shirdrn.azk.client.common.Context;
import org.shirdrn.azk.client.common.ContextImpl;
import org.shirdrn.azk.client.constants.AjaxCmd;
import org.shirdrn.azk.client.constants.AzkCmd;
import org.shirdrn.azk.client.constants.ConfigKeys;
import org.shirdrn.azk.client.utils.ActionFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

// Examples:
// java -jar target/azkaban-app-deployer-0.0.1-SNAPSHOT-jar-with-dependencies.jar -t publish -c /Users/yanjun/Workspaces/github/azk-client/src/main/resources/config.properties
// java -jar target/azkaban-app-deployer-0.0.1-SNAPSHOT-jar-with-dependencies.jar -t exec -c /Users/yanjun/Workspaces/github/azk-client/src/main/resources/config.properties
// java -jar target/azkaban-app-deployer-0.0.1-SNAPSHOT-jar-with-dependencies.jar -t delete -c /Users/yanjun/Workspaces/github/azk-client/src/main/resources/config.properties
public class AzkClient {

    private static final Log LOG = LogFactory.getLog(AzkClient.class);
    private static final ObjectFactory<Action> factory = new ActionFactory();

    public AzkClient() {
        super();
    }

    public static void main(String[] args) throws Exception {
        // create Options object
        Options options = new Options();
        options.addOption("h", "help", false, "Print help detail information.");
        options.addOption("t", "type", true, "Specify operation type: publish|exec|delete");
        options.addOption("c", "config", true, "Specify required configuration file path.");
        options.addOption("o", "overwrite", false, "Specify whether overwrite published job.");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        LOG.info("Input args: " + Arrays.asList(args));

        if(cmd.hasOption("t")) {
            final String type = cmd.getOptionValue("t");
            // create a project
            if(cmd.hasOption("c")) {
                // read configuration file properties
                String config = cmd.getOptionValue("c");
                if(config != null) {
                    final Context context = new ContextImpl();
                    populateContext(config, context);

                    // publish workflow to Azkaban server
                    String op = AzkCmd.PUBLISH.getCmd();
                    if(op.equalsIgnoreCase(type)){
                        List<AjaxCmd> actions = Arrays.asList(new AjaxCmd[] {
                                AjaxCmd.AUTH, AjaxCmd.CREATE, AjaxCmd.UPLOAD,
                                AjaxCmd.FETCH_PROJECT_FLOWS, AjaxCmd.SCHEDULE
                        });
                        execClientCmd(context, AzkCmd.PUBLISH, actions);
                        genAlternativeScheduleCommandLine(context);
                    }

                    // execute a specified workflow
                    op = AzkCmd.EXEC.getCmd();
                    if(op.equalsIgnoreCase(type)){
                        List<AjaxCmd> actions = Arrays.asList(new AjaxCmd[] {
                                AjaxCmd.AUTH, AjaxCmd.FETCH_PROJECT_FLOWS, AjaxCmd.EXECUTE_FLOW
                        });
                        execClientCmd(context, AzkCmd.EXEC, actions);
                    }
                    // delete workflow from Azkaban server
                    op = AzkCmd.DELETE.getCmd();
                    if(op.equalsIgnoreCase(type)){
                        List<AjaxCmd> actions = Arrays.asList(new AjaxCmd[] {
                                AjaxCmd.AUTH, AjaxCmd.FETCH_PROJECT_FLOWS, AjaxCmd.FETCH_SCHEDULE,
                                AjaxCmd.UNSCHEDULE, AjaxCmd.DELETE
                        });
                        execClientCmd(context, AzkCmd.DELETE, actions);
                    }
                }
            }
        }

        if(args.length == 0 || cmd.hasOption("h")) {
            // automatically generate the help statement
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("deployer", options);
        }
    }

    private static void populateContext(String config, Context context) throws IOException {
        final BufferedInputStream in = new BufferedInputStream(new FileInputStream(config));
        ResourceBundle resource = new PropertyResourceBundle(in);
        Enumeration<String> propKeys= resource.getKeys();
        LOG.info("Input config file: " + config);
        LOG.info("Input configured KV pairs: ");
        while(propKeys.hasMoreElements()){
            String key = propKeys.nextElement();
            context.set(key,resource.getString(key));
            LOG.info("  " + key + "\t=\t" + resource.getString(key));
        }
        in.close();
    }

    private static void genAlternativeScheduleCommandLine(Context context) {
        LOG.info("Alternatively, COPY and EXEC the following command, we can also chedule Azkaban workflow:");
        Object obj = context.getObject("flows", null);
        if(obj !=null) {
            JSONArray a = (JSONArray) obj;
            for (int i = 0; i < a.size(); i++) {
                JSONObject o = a.getJSONObject(i);
                String flowId = o.getString("flowId");
                StringBuffer commandLine = new StringBuffer(
                        "------------------------------------------------------------\n\t");
                commandLine.append("curl -k -d ajax=scheduleCronFlow -d projectName=")
                        .append(context.get(ConfigKeys.PROJECT_NAME))
                        .append(" -d flow=" + flowId)
                        .append(" --data-urlencode cronExpression=\"")
                        .append(context.get(ConfigKeys.CRON_EXPR))
                        .append("\" -b \"azkaban.browser.session.id=")
                        .append(context.get("session.id"))
                        .append("\" ")
                        .append(context.get(ConfigKeys.AZKABAN_SERVER_URL))
                        .append("/schedule");
                LOG.info(commandLine.toString());
            }
            LOG.info("------------------------------------------------------------");
        }
    }

    private static void execClientCmd(Context context, AzkCmd clientCmd, List<AjaxCmd> actions) {
        LOG.info("Prepare to " + clientCmd + "(" + actions.size() + " actions): " + actions);
        for(int i=0; i<actions.size(); i++) {
            AjaxCmd actionCmd = actions.get(i);
            LOG.info("Exec action(" + (i + 1) + "): " + actionCmd);
            try {
                Action action = factory.createObject(actionCmd);
                action.setContext(context);
                action.operate();
                Thread.sleep(1000);
            } catch (Exception e) {
                LOG.error("Action(" + (i + 1) + ") failed: " + actionCmd, e);
                break;
            }
            LOG.info("Action(" + (i + 1) + ") succeeded: " + actionCmd);
        }
    }
}
