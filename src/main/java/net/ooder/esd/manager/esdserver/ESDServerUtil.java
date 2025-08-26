package net.ooder.esd.manager.esdserver;

import net.ooder.config.JDSUtil;
import net.ooder.config.UserBean;
import net.ooder.esd.engine.Project;
import net.ooder.esd.engine.config.LocalServer;
import net.ooder.jds.core.esb.util.ActionContext;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wenzhang
 */

public class ESDServerUtil {

    public static final String tempftlname = "cmd.ftl";
    public static final String stopfile = "stop.bat";
    public static final String startfile = "cmdstart.bat";

    public static void reboot() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName(); // format:"pid@hostname"
        Integer pid = Integer.parseInt(name.substring(0, name.indexOf('@')));
        String projectName = System.getProperty("projectName");
        String appUrl = System.getProperty("appUrl");
        String path = null;
        try {
            if (path == null) {
                path = JDSUtil.getJdsRealPath();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        String proxyUrl = System.getProperty("proxyUrl");

        String proxyPort = System.getProperty("proxyPort");

        if (proxyPort != null && !proxyPort.equals("")) {
            proxyPort = "8090";
        }

        String indexPage = System.getProperty("indexPage");
        ESDServerExe esdServerExe = new ESDServerExe(projectName, appUrl, proxyUrl, indexPage, path, pid.toString(), proxyPort);
        List<ESDServerExe> exes = new ArrayList<ESDServerExe>();
        exes.add(esdServerExe);
        startESDServer(exes, projectName);
    }

    public static void startESDServer(ESDServerExe jse, String projectName) {
        List<ESDServerExe> exes = new ArrayList<ESDServerExe>();
        exes.add(jse);
        startESDServer(exes, projectName);
    }

    public static void startESDServer(Project project, LocalServer server) {

        String userName = UserBean.getInstance().getUsername();
        stopESDServer(server);
        String fileName = server.getPath() + File.separatorChar + userName + "@" + project.getProjectName() + "@" + startfile;
        File startFile = new File(fileName);
        if (startFile.exists()) {
            try {
                Desktop.getDesktop().open(startFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ESDServerExe esdServerExe = new ESDServerExe(project, server);
            List<ESDServerExe> exes = new ArrayList<ESDServerExe>();
            exes.add(esdServerExe);
            startESDServer(exes, project.getProjectName());
        }

    }

    public static void stopESDServer(LocalServer server) {
        String userName = UserBean.getInstance().getUsername();
        String stopFileStr = server.getPath() + File.separatorChar + userName + "@" + stopfile;
        File stopFile = new File(stopFileStr);

        if (stopFile.exists()) {
            try {
                Desktop.getDesktop().open(stopFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void startESDServer(List<ESDServerExe> jses, String projectName) {
        String userName = UserBean.getInstance().getUsername();
        String cmd = "";
        try {
            cmd = getExtStr(tempftlname, jses);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String stopFileStr = jses.get(0).getPath() + File.separatorChar + userName + "@" + projectName + "@" + stopfile;

        File stopFile = new File(stopFileStr);

        if (stopFile.exists()) {
            try {
                Desktop.getDesktop().open(stopFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String fileName = jses.get(0).getPath() + File.separatorChar + userName + "@" + projectName + "@" + startfile;
        createScript(cmd, fileName, true);
    }

    public static void createStopScript(String path, String projectName, Integer pid) {
        String userName = UserBean.getInstance().getUsername();
        String cmd = "taskkill /F /PID " + pid.toString();
        String fileName = path + userName + "@" + projectName + "@" + stopfile;
        createScript(cmd, fileName, false);
    }


    public static void createScript(String str, String fileName, boolean open) {
        try {

            try {
                File file = new File(fileName);
                file.delete();
                FileWriter fileWriter = new FileWriter(fileName, true);
                fileWriter.write(str);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (open) {
                Desktop.getDesktop().open(new File(fileName));
            }


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static String getExtStr(String ftl, List<ESDServerExe> jses) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();

        ActionContext.getContext().getContextMap().put("jses", jses);
        freemarker.template.Configuration configuration = new freemarker.template.Configuration();
        String path = JDSUtil.getJdsRealPath() + "sysapp/ftl" + File.separatorChar;
        System.out.println(path);
        configuration.setDirectoryForTemplateLoading(new File(path));
        Template template = configuration.getTemplate(ftl);
        System.out.println(path + ftl);
        template.process(ActionContext.getContext().getContextMap(), stringWriter);
        String str = stringWriter.toString();
        return str;
    }


    public static void main(String[] args) {
//        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
//        String name = runtime.getName(); // format:"pid@hostname"
//        Integer pid = Integer.parseInt(name.substring(0, name.indexOf('@')));
//        startJSE(new Integer[]{pid});

    }


}
