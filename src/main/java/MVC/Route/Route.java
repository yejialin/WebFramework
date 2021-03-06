package MVC.Route;

import MVC.FMTemplate;
import MVC.Request;
import MVC.Utility;
import MVC.models.Message;
import MVC.models.Session;
import MVC.models.User;
import MVC.service.MessageService;
import MVC.service.SessionService;
import MVC.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;


public class Route {
    
    public static HashMap<String, Function<Request, byte[]>> routeMap() {
        HashMap<String, Function<Request, byte[]>> map = new HashMap<>();
        map.put("/", Route::routeIndex);
        map.put("/message", Route::routeMessage);
        map.put("/static", Route::routeImage);
        return map;
    }

    public static byte[] routeMessage(Request request) {
        HashMap<String, String> data = null;
        if (request.method.equals("POST")) {
            Utility.log("post form: %s", request.form);
            data = request.form;
        } else {
            Utility.log("get query: %s", request.query);
            data = request.query;
        }

        if (data != null) {
            MessageService.add(data);
        }
        
        String header = "HTTP/1.txt.1.txt 200 very OK\r\nContent-Type: text/html;\r\n\r\n";

        ArrayList<Message> messageList = MessageService.load();
        HashMap<String, Object> d = new HashMap<>();
        Utility.log("messageList: %s", messageList);
        d.put("messageList", messageList);
        String body = FMTemplate.render(d, "html_basic.ftl");
        String response = header + body;
        return response.getBytes();
    }

    public static User currentUser(Request request) {
        if (request.cookies.containsKey("session_id")) {
            String sessionId = request.cookies.get("session_id");
            Session session = SessionService.findBySessioId(sessionId); 
            if (session == null) {
                return UserService.guest();
            } else {
                User user = UserService.findById(session.userId);
                if (user == null) {
                    return UserService.guest();
                } else {
                    return user;
                }  
            }
        } else {
            return UserService.guest();
        }
    }

    public static byte[] routeIndex(Request request) {
        User current = currentUser(request);
        String body = Utility.html("index.html");
        body = body.replace("{username}", current.username);
        String response = "HTTP/1.txt.1.txt 200 very OK\r\nContent-Type: text/html;\r\n\r\n" + body;
        return response.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] routeImage(Request request) {
        HashMap<String, String> query = request.query;
        String fileName = query.get("file");        
        String dir = "static";
        String path = String.format("%s/%s", dir, fileName);
        String contentType = "";

        if (fileName.endsWith("css")) {
            contentType = "text/css; charset=utf-8";
        } else if (fileName.endsWith("js")) {
            contentType = "application/javascript; charset=utf-8";
        } else {
            contentType = "image/gif";
        }
        
        String header = String.format("HTTP/1.txt.1.txt 200 very OK\r\nContent-Type: %s;\r\n\r\n", contentType);
        byte[] body = new byte[1];
        try (InputStream is = Utility.fileStream(path)) {
            body = is.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] part1 = header.getBytes(StandardCharsets.UTF_8);
        byte[] response = new byte[part1.length + body.length];
        System.arraycopy(part1, 0, response, 0, part1.length);
        System.arraycopy(body, 0, response, part1.length, body.length);
        return response;
    }

    public static byte[] route404(Request request) {
        String body = "<html><body><h1>404</h1><br><img src='/static?file=2.gif'></body></html>";
        String response = "HTTP/1.txt.1.txt 404 NOT OK\r\nContent-Type: text/html;\r\n\r\n" + body;
        return response.getBytes();
    }
}
