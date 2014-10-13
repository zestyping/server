package org.projectbuendia.web.api;

import org.projectbuendia.fileops.Logging;
import org.projectbuendia.web.JettyServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by wwadewitte on 10/11/14.
 */
public abstract class ApiHandler extends HttpServlet {

    private HashMap<String, String> urlVariables = new HashMap<String, String>();

    protected abstract String getBaseUrl();

    private HashMap<String, String> payLoad = new HashMap<String, String>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        callHandler("get", request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("yee");
        try {
            reportPayload(request);
            callHandler("post", request, response);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        reportPayload(request);
        callHandler("put", request, response);
    }
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        reportPayload(request);
        callHandler("delete", request, response);
    }

    private void callHandler(String method, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {

            if (request.getPathInfo() == null) {
                JettyServer.getApiStructure(method).get(getBaseUrl()).call(request, response, urlVariables, request.getParameterMap(), payLoad);
                return;
            }
            String[] pathArray = request.getPathInfo().replaceFirst("/", "").split("/");
            StringBuilder lastFoundHandler = new StringBuilder();

            lastFoundHandler.append(getBaseUrl());

            for (int i = 0; i < pathArray.length; i++) {
                if ((pathArray[i].isEmpty())) {
                    continue;
                } else if (JettyServer.getApiStructure(method).containsKey(lastFoundHandler.toString() + "/" + pathArray[i])) {
                    lastFoundHandler.append("/" + pathArray[i]);
                } else if (JettyServer.getApiStructure(method).containsKey(lastFoundHandler.toString() + "/*")) {
                    lastFoundHandler.append("/*");
                    urlVariables.put(getSpecification()[i], pathArray[i]);
                }
            }

            try {
                JettyServer.getApiStructure(method).get(lastFoundHandler.toString()).call(request, response, urlVariables, request.getParameterMap(), payLoad);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void reportPayload(HttpServletRequest request) {
        String body = null;
        try {
            body = SharedFunctions.getBody(request);
        } catch (IOException e) {
            Logging.log("invalid body", e);
        }
        if(body != null) {
            String[] nodes = body.split(Pattern.quote("&"));
            for(String s : nodes) {
                if(s.length() <= 1) {
                    continue;
                }
                System.out.println("Node:" + s);
                String[] keyValue = s.split(Pattern.quote("="));
                payLoad.put(URLDecoder.decode(keyValue[0]), URLDecoder.decode(keyValue[1]));
            }
        }
    }
;

    protected abstract String[] getSpecification();
}
