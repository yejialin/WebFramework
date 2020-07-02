package MVC.Route;

import MVC.Request;
import MVC.Utility;
import MVC.models.User;
import MVC.service.SessionService;
import MVC.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

public class RouteUser {
    public static HashMap<String, Function<Request, byte[]>> routeMap() {
        HashMap<String, Function<Request, byte[]>> map = new HashMap<>();
        map.put("/login", RouteUser::login);
        map.put("/register", RouteUser::register);
        return map;
    }        
    
        public static String responseWithHeader(int code, HashMap<String, String> headerMap, String body) {
            String header = String.format("HTTP/1.1 %s\r\r", code);

            for (String key: headerMap.keySet()) {
                String value = headerMap.get(key);
                String item = String.format("%s: %s \r\n", key, value);
                header = header + item;
            }
            String response =  String.format("%s\r\n\r\n%s", header, body);
            return response;
        }

        public static byte[] login(Request request) {
            HashMap<String, String> header = new HashMap<>();

            HashMap<String, String> data = null;
            if (request.method.equals("POST")) {
                data = request.form;
            } else if (request.method.equals("GET")) {
                data = request.query;
            } else {
                String m = String.format("unknown method (%s)", request.method);
                throw new RuntimeException(m);
            }

            String loginResult = "";
            if (data != null) {
                String username =  data.get("username");
                String password = data.get("password");
                if (UserService.validLogin(username, password)) {
                    User user = UserService.findByName(username);

                    String sessionId = UUID.randomUUID().toString();
                    header.put("Set-Cookie", String.format("session_id=%s", sessionId));
                    SessionService.add(user.id, sessionId);
                    loginResult = "登录成功";
                } else {
                    loginResult = "登录失败";
                }
            }

            String body = Utility.html("login.html");
            body = body.replace("{loginResult}", loginResult);
            header.put("Content-Type", "text/html");
            String response = responseWithHeader(200, header, body);
            return response.getBytes(StandardCharsets.UTF_8);
        }

    public static byte[] register(Request request) {
        Utility.log("register Route");
        String registerResult = "";

        if (request.method.equals("POST")) {
            HashMap<String, String> form = request.form;
            Utility.log("register form: %s", form);
            UserService.add(form);
        }
        String body = Utility.html("register.html");
        body = body.replace("{registerResult}", registerResult);

        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "text/html");
        String response = responseWithHeader(200, header, body);
        return response.getBytes(StandardCharsets.UTF_8);
    }

}
