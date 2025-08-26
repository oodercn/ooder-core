package net.ooder.esd.engine.mcp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.annotation.UserSpace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class McpStdioHandler implements Runnable {
    private DSMFactory dsmFactory ;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private PrintWriter writer = new PrintWriter(System.out, true);

    @Override
    public void run() {
        writer.println(JSON.toJSONString(createResponse("success", "MCP stdio handler started", null)));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                handleCommand(line);
            }
        } catch (IOException e) {
            writer.println(JSON.toJSONString(createResponse("error", e.getMessage(), null)));
        }
    }

    private void handleCommand(String command) {
        try {
            dsmFactory = DSMFactory.getInstance();
            JSONObject request = JSON.parseObject(command);
            String action = request.getString("action");
            Map<String, Object> params = request.getJSONObject("params");
            Map<String, Object> result = new HashMap<>();

            switch (action) {
                case "compileProject":
                    String projectVersionName = (String) params.get("projectVersionName");
                    dsmFactory.compileProject(projectVersionName);
                    result.put("message", "Project compilation started");
                    writer.println(JSON.toJSONString(createResponse("success", null, result)));
                    break;
                case "getDomain":
                    String domainId = (String) params.get("domainId");
                    DomainInst domain = dsmFactory.getDomainInstById(domainId);
                    writer.println(JSON.toJSONString(createResponse("success", null, domain)));
                    break;
                case "createDomain":
                    String projVersion = (String) params.get("projectVersionName");
                    UserSpace userSpace = UserSpace.valueOf((String) params.get("userSpace"));
                    DomainInst newDomain = dsmFactory.newDomain(projVersion, userSpace);
                    result.put("domainId", newDomain.getDomainId());
                    writer.println(JSON.toJSONString(createResponse("success", null, result)));
                    break;
                case "buildProject":
                    String buildProjVersion = (String) params.get("projectVersionName");
                    RepositoryInst repo = dsmFactory.getRepositoryManager().getProjectRepository(buildProjVersion);
                    dsmFactory.buildProject(repo, dsmFactory.getCurrChromeDriver());
                    result.put("message", "Project build completed");
                    writer.println(JSON.toJSONString(createResponse("success", null, result)));
                    break;
                default:
                    writer.println(JSON.toJSONString(createResponse("error", "Unknown action: " + action, null)));
            }
        } catch (Exception e) {
            writer.println(JSON.toJSONString(createResponse("error", e.getMessage(), null)));
        }
    }

    private Map<String, Object> createResponse(String status, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    public static void start() {
        new Thread(new McpStdioHandler()).start();
    }
}