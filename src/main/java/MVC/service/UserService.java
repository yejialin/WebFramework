package MVC.service;

import MVC.Utility;
import MVC.models.ModelFactory;
import MVC.models.User;
import MVC.models.UserRole;

import java.util.ArrayList;
import java.util.HashMap;

public class UserService {

    public static User add(HashMap<String, String> form) {
        User m = new User();
        m.username = form.get("username");
        m.password = form.get("password");
        m.role = UserRole.normal;

        ArrayList<User> all = load();
        if (all.isEmpty()) {
            m.id = 1;
        } else {
            m.id = all.get(all.size() - 1).id + 1;
        }

        all.add(m);
        save(all);
        return m;
    }

    public static ArrayList<User> load() {
        String className = User.class.getSimpleName();
        ArrayList<User> rs = ModelFactory.load(className, 4, modelData -> {
            Utility.log("modelData.get(0): %s", modelData.get(0));
            Integer id = Integer.valueOf(modelData.get(0));
            String username = modelData.get(1);
            String password = modelData.get(2);
            UserRole role = UserRole.valueOf(modelData.get(3));

            User m = new User();
            m.id = id;
            m.username = username;
            m.password = password;
            m.role = role;

            return m;
        });
        return rs;
    }

    public static void save(ArrayList<User> list) {
        String className = User.class.getSimpleName();
        ModelFactory.save(className, list, model -> {
            ArrayList<String> lines = new ArrayList<>();
            Utility.log("id: %s", model.id);
            lines.add(model.id.toString());
            lines.add(model.username);
            lines.add(model.password);
            lines.add(model.role.toString());
            return lines;
        });
    }

    public static boolean validLogin(String username, String password) {
        ArrayList<User> all = load();

        for (User u: all) {
            if (u.username.equals(username) && u.password.equals(password)) {
                return true;
            }
        }

        return false;
    }
    

    
    public static User findByName(String username) {
        ArrayList<User> all = load();

        User t = ModelFactory.findBy(all, (model) -> {
            return model.username.equals(username);
        });
        
        return t;
    }
    
    public static User findById(Integer id) {
        ArrayList<User> all = load();

        User t = ModelFactory.findBy(all, (model) -> {
            return model.id.equals(id);
        });
        
        return t;
    }
    public static User guest() {
        User g = new User();
        g.id = -1;
        g.username = "游客";
        g.password = "";
        g.role = UserRole.guest;
        return g;
    }
}
