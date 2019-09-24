package com.redphase.httpserver;

import com.alibaba.fastjson.JSON;
import com.redphase.api.setup.ISetupService;
import com.redphase.api.task.ITaskHistoryService;
import com.redphase.controller.BaseController;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.dto.task.TaskHistoryDto;
import com.redphase.framework.Response;
import com.redphase.framework.util.DateUtil;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IpUtil;
import com.redphase.framework.util.ValidatorUtil;
import com.redphase.view.AlertView;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

//启动服务，监听来自客户端的请求
@Component
@Slf4j
public class MyFileServer {
    @Autowired
    protected AlertView ialert;
    @Autowired
    private ISetupService setupService;
    @Autowired
    private ITaskHistoryService taskHistoryService;
    @Autowired
    private GzipHandler gzipHandler;
    @Autowired
    private TaskPageHandler taskPageHandler;
    @Autowired
    private TaskDownHandler taskDownHandler;
    @Value("${file.port}")
    private Integer filePort;
    protected SysVariableDto variablePassword;
    String gzipPath;
    HttpServer server = null;

//    @Bean
    public HttpServer fileHttpServer() {
        try {
            if(server!=null){
                return server;
            }
            SysVariableDto variableDir = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-path-reports");
            }});
            SysVariableDto variablePort = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-socket-wifi-port");
            }});
            variablePassword = setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-socket-password");
            }});
            File file = new File(variableDir.getValue());
            gzipPath = file.getParent() + "/gzip/";
            File gzipPathFile = new File(gzipPath);
            if (!gzipPathFile.exists()) {
                gzipPathFile.mkdirs();
            }
            if (variablePort != null && variablePort.getValue() != null) {
                filePort = Integer.parseInt(variablePort.getValue());
            }
            server = HttpServer.create(new InetSocketAddress(filePort), 100);
            server.createContext("/gzip", gzipHandler);
            server.createContext("/task/page", taskPageHandler);
            server.createContext("/task/down", taskDownHandler);
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            log.info("文件接收服务启动!{}:{} 密码:{}", IpUtil.getLocalHostLANAddress().getHostAddress(), filePort, variablePassword.getValue());
        } catch (Exception e) {
            log.error("文件服务器创建失败..", e);
            Platform.runLater(() -> {
                ialert.error("文件服务器创建..端口:" + filePort + "," + e.getMessage());
            });
        }
        return server;
    }

    @Component
    class GzipHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Response result = new Response(0, "success");
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");
            try {
                verifyAuthorization(exchange);
                InputStream in = exchange.getRequestBody();
                saveFile(in, gzipPath + (String) DateUtil.getCurDateStr("yyyyMMddHHmmsss"));

            } catch (Exception e) {
                result = Response.error("服务器异常!" + e.getMessage());
            }
            OutputStream os = exchange.getResponseBody();
            exchange.sendResponseHeaders(200, 0);
            os.write(JSON.toJSONString(result).getBytes());
            os.close();
        }

        public void saveFile(InputStream is, String fileName) throws IOException {
            BufferedInputStream in = null;
            BufferedOutputStream out = null;
            try {
                in = new BufferedInputStream(is);
                out = new BufferedOutputStream(new FileOutputStream(fileName + ".zip"));
                int len = -1;
                byte[] b = new byte[1024];
                while ((len = in.read(b)) != -1) {
                    out.write(b, 0, len);
                }
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    private String getHeaderValue(Headers requestHeaders, String name) {
        String value = null;
        List headers = requestHeaders.get(name);
        if (headers != null && headers.size() != 0) {
            value = (String) headers.get(0);
        }
        return value;
    }

    @Component
    class TaskPageHandler extends BaseController implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Response result = new Response(0, "success");
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");
            try {
                verifyAuthorization(exchange);
                result.data = getPageDto(taskHistoryService.findDataIsPage(new TaskHistoryDto() {{
                    Map<String, Object> parametersMap = getParameters(exchange);
                    String pageNum = (String) parametersMap.get("pageNum");
                    if (ValidatorUtil.notEmpty(pageNum) && ValidatorUtil.isInteger(pageNum)) {
                        setPageNum(Integer.parseInt(pageNum));//页码
                    }
                    String dateDetection = (String) parametersMap.get("dateDetection");
                    if (ValidatorUtil.notEmpty(dateDetection)) {
                        setDateDetection(dateDetection);//检测日期
                    }
                    String testTechnology = (String) parametersMap.get("testTechnology");
                    if (ValidatorUtil.notEmpty(testTechnology)) {
                        setTestTechnology(testTechnology);//检测技术
                    }
                    String electricBureau = (String) parametersMap.get("electricBureau");
                    if (ValidatorUtil.notEmpty(electricBureau)) {
                        setElectricBureau(electricBureau);//电业局
                    }
                    String substation = (String) parametersMap.get("substation");
                    if (ValidatorUtil.notEmpty(substation)) {
                        setSubstation(substation);//变电站
                    }
                    String voltageLevel = (String) parametersMap.get("voltageLevel");
                    if (ValidatorUtil.notEmpty(voltageLevel) && ValidatorUtil.isInteger(voltageLevel)) {
                        setVoltageLevel(Integer.parseInt(voltageLevel));//电压等级
                    }
                    String deviceType = (String) parametersMap.get("deviceType");
                    if (ValidatorUtil.notEmpty(deviceType) && ValidatorUtil.isInteger(deviceType)) {
                        setDeviceType(Integer.parseInt(deviceType));//设备类型
                    }
                    String dateBegin = (String) parametersMap.get("dateBegin");
                    if (ValidatorUtil.notEmpty(dateBegin)) {
                        setDateBegin(dateBegin);//起始日期
                    }
                    String dateEnd = (String) parametersMap.get("dateEnd");
                    if (ValidatorUtil.notEmpty(dateEnd)) {
                        setDateBegin(dateEnd);//截止时间
                    }
                }}));
            } catch (Exception e) {
                log.error("服务器异常!", e);
                result = Response.error("服务器异常!" + e.getMessage());
            }
            OutputStream os = exchange.getResponseBody();
            exchange.sendResponseHeaders(200, 0);
            os.write(JSON.toJSONString(result).getBytes());
            os.close();
        }

        @Override
        public void dispose() {
        }
    }

    @Component
    class TaskDownHandler extends BaseController implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Response result = new Response(0, "success");
            Headers responseHeaders = exchange.getResponseHeaders();
            try {
                verifyAuthorization(exchange);
                Map<String, Object> parametersMap = getParameters(exchange);
                String id = (String) parametersMap.get("id");
                if (ValidatorUtil.notEmpty(id) && ValidatorUtil.isInteger(id)) {
                    TaskHistoryDto dto = new TaskHistoryDto();
                    dto.setId(Long.parseLong(id));
                    TaskHistoryDto taskHistoryDto = taskHistoryService.findDataById(dto);
                    if (taskHistoryDto == null) {
                        throw new RuntimeException("任务单不存在");
                    }
                    File taskFile = new File(taskHistoryDto.getFilePath());
                    if (!taskFile.exists()) {
                        throw new RuntimeException("任务单文件已删除!");
                    }
//                    responseHeaders.set("Content-Type", "application/octet-stream");
                    responseHeaders.set("Content-Type", "text/plain");
                    OutputStream os = exchange.getResponseBody();
                    exchange.sendResponseHeaders(200, taskFile.length());
                    FileInputStream inputStream = new FileInputStream(taskFile);
                    byte[] bytes = new byte[1024];
                    int len = -1;
                    while ((len = inputStream.read(bytes)) > 0) {
                        os.write(bytes, 0, len);
                    }
                    os.close();
                    return;
                } else {
                    throw new RuntimeException(I18nUtil.get("error.paramError"));
                }
            } catch (Exception e) {
                log.error("服务器异常!", e);
                result = Response.error("服务器异常!" + e.getMessage());
            }
            responseHeaders.set("Content-Type", "application/json");
            OutputStream os = exchange.getResponseBody();
            exchange.sendResponseHeaders(200, 0);
            os.write(JSON.toJSONString(result).getBytes());
            os.close();
        }

        @Override
        public void dispose() {
        }
    }

    private void verifyAuthorization(HttpExchange exchange) throws Exception {
        Headers requestHeaders = exchange.getRequestHeaders();
        List authorizationHeaders = requestHeaders.get("Authorization");
        if (authorizationHeaders == null || authorizationHeaders.size() == 0) {
            throw new RuntimeException("通讯密码获取失败!");
        }
        String authorization = (String) authorizationHeaders.get(0);
        if (ValidatorUtil.isEmpty(authorization) || !authorization.equals(variablePassword.getValue())) {
            throw new RuntimeException("通讯密码错误!");
        }
    }

    private Map<String, Object> getParameters(HttpExchange exchange) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
            parameters = parsePostParameters(exchange);
        } else {
            parameters = parseGetParameters(exchange);
        }
        return parameters;
    }

    private Map<String, Object> parseGetParameters(HttpExchange exchange) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);
        exchange.setAttribute("parameters", parameters);
        return parameters;
    }


    private Map<String, Object> parsePostParameters(HttpExchange exchange) throws Exception {
        Map<String, Object> parameters = (Map<String, Object>) exchange.getAttribute("parameters");
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        return parseQuery(query, parameters);
    }

    private Map<String, Object> parseQuery(String query, Map parameters) throws Exception {
        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                }
                if (param.length > 1) {
                    value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                }
                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List) {
                        List values = (List) obj;
                        values.add(value);
                    } else if (obj instanceof String) {
                        List values = new ArrayList();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
        return parameters;
    }
}
