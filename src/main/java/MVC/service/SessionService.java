package MVC.service;

import MVC.models.Session;
import MVC.models.ModelFactory;

import java.util.ArrayList;

public class SessionService {

    public static Session add(Integer userId, String sessionId) {
        Session m = new Session();
        m.userId = userId;
        m.sessionId = sessionId;
        
        ArrayList<Session> all = load();
        all.add(m);
        save(all);
        return m;
    }


    public static ArrayList<Session> load() {
        String className = Session.class.getSimpleName();
        ArrayList<Session> rs = ModelFactory.load(className, 2, modelData -> {
            String sessionId = modelData.get(0);
            Integer userId = Integer.valueOf(modelData.get(1));

            Session m = new Session();
            m.sessionId = sessionId;
            m.userId = userId;

            return m;
        });
        return rs;
    }

    public static void save(ArrayList<Session> list) {
        String className = Session.class.getSimpleName();
        ModelFactory.save(className, list, model -> {
            ArrayList<String> lines = new ArrayList<>();
            lines.add(model.sessionId);
            lines.add(model.userId.toString());
            return lines;
        });
    }

    public static Session findBySessioId(String sessionId) {
        ArrayList<Session> all = load();

        for (Session m:all) {
            if (m.sessionId.equals(sessionId)) {
                return m;
            }
        }

        return null;
    }
}
