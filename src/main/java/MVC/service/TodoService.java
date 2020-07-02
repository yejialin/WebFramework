package MVC.service;

import MVC.Utility;
import MVC.models.ModelFactory;
import MVC.models.Todo;

import java.util.ArrayList;
import java.util.HashMap;

public class TodoService {

    public static Todo add(HashMap<String, String> form) {
        Todo m = new Todo();
        m.content = form.get("content");
        m.completed = false;

        ArrayList<Todo> all = load();
        if (all.isEmpty()) {
            m.id = 1;
        } else {
            m.id = all.get(all.size() - 1).id + 1;
        }

        all.add(m);
        save(all);
        return m;
    }

    public static ArrayList<Todo> load() {
        String className = Todo.class.getSimpleName();
        ArrayList<Todo> rs = ModelFactory.load(className, 3, modelData -> {
            Utility.log("modelData.get(0): %s", modelData.get(0));
            Integer id = Integer.valueOf(modelData.get(0));
            String content = modelData.get(1);
            Boolean completed = Boolean.valueOf(modelData.get(2));

            Todo m = new Todo();
            m.id = id;
            m.content = content;
            m.completed = completed;

            return m;
        });
        return rs;
    }

    public static void save(ArrayList<Todo> list) {
        String className = Todo.class.getSimpleName();
        ModelFactory.save(className, list, model -> {
            ArrayList<String> lines = new ArrayList<>();
            Utility.log("id: %s", model.id);
            lines.add(model.id.toString());
            lines.add(model.content);
            lines.add(model.completed.toString());
            return lines;
        });
    }
    
    public static String todoListShowString() {
        ArrayList<Todo> all = load();
        StringBuilder sb = new StringBuilder();

        for (Todo m:all) {
            String s = String.format(
                    "<h3>\n" +
                    " %s: %s\n" +
                            "<a href=\"/todo/edit?id=%s\">编辑</a>\n" +
                            " <a href=\"/todo/delete?id=%s\">删除</a>\n" +
                            " <a href=\"/todo/complete?id=%s\">完成</a>\n" +
                            "</h3>",
                    m.id,
                    m.content,
                    m.id,
                    m.id,
                    m.id
                    );
            sb.append(s);
        }
        return sb.toString();
    }
    
    public static void delete(Integer todoId) {
        ArrayList<Todo> all = load();

        for (int i = 0; i < all.size(); i++) {
            Todo e = all.get(i);
            if (e.id.equals(todoId)) {
                all.remove(e);
            }
        }

        save(all);
    }
    
    public static Todo findById(Integer todoId) {
        ArrayList<Todo> all = load();
        
        for (int i = 0; i < all.size(); i++) {
            Todo e = all.get(i);
            if (e.id.equals(todoId)) {
                return e;
            }
        }
        
        return null;
    }

    public static void updateContent(Integer todoId, String content) {
        ArrayList<Todo> all = load();

        for (int i = 0; i < all.size(); i++) {
            Todo e = all.get(i);
            if (e.id.equals(todoId)) {
                e.content = content;
            }
        }

        save(all);
    }
}
