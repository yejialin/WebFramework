package MVC.Route;

import MVC.Request;
import MVC.Utility;
import MVC.models.Todo;
import MVC.service.TodoService;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.function.Function;

public class RouteTodo {
    public static HashMap<String, Function<Request, byte[]>> routeMap() {
        HashMap<String, Function<Request, byte[]>> map = new HashMap<>();
        map.put("/todo", RouteTodo::index);
        map.put("/todo/add", RouteTodo::add);
        map.put("/todo/delete", RouteTodo::delete);
        map.put("/todo/edit", RouteTodo::edit);
        map.put("/todo/update", RouteTodo::update);

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

    public static byte[] index(Request request) {
        String body = Utility.html("todo_index.html");
        String todos = TodoService.todoListShowString();
        body = body.replace("{todos}", todos);
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "text/html");
        String response = responseWithHeader(200, header, body);
        return response.getBytes(StandardCharsets.UTF_8);
    }


    public static byte[] redirect(String url) {
        String header = "HTTP/1.1 302 move\r\n" +
                "Location: " + url + "\r\n" +
                "\r\n\r\n";
        return header.getBytes();
    }

    public static byte[] add(Request request) {
        HashMap<String, String> form = request.form;
        TodoService.add(form);
        return redirect("/todo");
    }

    public static byte[] delete(Request request) {
        HashMap<String, String> data = request.query;
        Integer todoId = Integer.valueOf(data.get("id"));
        TodoService.delete(todoId);
        Utility.log("todo delete %s", data);
        return redirect("/todo");
    }

    public static byte[] edit(Request request) {
        HashMap<String, String> data = request.query;
        Integer todoId = Integer.valueOf(data.get("id"));
        Todo todo = TodoService.findById(todoId);

        String body = Utility.html("todo_edit.html");
        body = body.replace("{todo_id}", todo.id.toString());
        body = body.replace("{todo_content}", todo.content.toString());
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "text/html");
        String response = responseWithHeader(200, header, body);
        return response.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] update(Request request) {
        HashMap<String, String> data = request.form;
        Integer todoId = Integer.valueOf(data.get("id"));
        String content = data.get("content");
        TodoService.updateContent(todoId,content);
        return redirect("/todo");
    }
}
