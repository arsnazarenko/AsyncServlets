package ru.itmo.async.nonAsync;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class JsonService {
    private final static Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(JsonService.class.getName());

    public static String getProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer
                .append("url: ").append(req.getRequestURI()).append("\n")
                .append("method: ").append(req.getMethod()).append("\n");
        req.getParameterMap().entrySet().stream()
                .forEach(e -> stringBuffer.append(e.getKey()).append(": ").append(Arrays.toString(e.getValue())).append("\n"));
        return stringBuffer.toString();
    }

    public static String postProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringBuffer respText = new StringBuffer();
        StringBuffer json = new StringBuffer();
        respText
                .append("url: ").append(req.getRequestURI()).append("\n")
                .append("method: ").append(req.getMethod()).append("\n")
                .append("body: {\n");
        String line;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (IOException e) {
            logger.warning("IO error");
        }
        JsonObject jsonObject = gson.fromJson(json.toString(), JsonObject.class);
        JsonElement name = jsonObject.get("name");
        if (name != null) {
            logger.info(req.getMethod() + " " + name.getAsString());
        }
        respText.append(jsonObject).append("\n}");
        return respText.toString();
    }

}
