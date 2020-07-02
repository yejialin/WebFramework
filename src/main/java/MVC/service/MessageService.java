package MVC.service;

import MVC.models.Message;
import MVC.models.ModelFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageService {

    public static Message add(HashMap<String, String> form) {
        Message m = new Message();
        m.author = form.get("author");
        m.message = form.get("message");
        m = checkWork(m);;
        
        ArrayList<Message> all = load();
        all.add(m);
        save(all);
        return m;
    }
    
    public static Message checkWork(Message m) {
        ArrayList<String> hexieWord = new ArrayList<>();
        hexieWord.add("tmd");
        hexieWord.add("fuck");

        for (String word:hexieWord) {
            if (m.message.contains(word)) {
                m.message = m.message.replace(word, "*");
            }
        }
        
        return m;
    }


    public static ArrayList<Message> load() {
        String className = Message.class.getSimpleName();
        ArrayList<Message> rs = ModelFactory.load(className, 2, modelData -> {
            String author = modelData.get(0);
            String message = modelData.get(1);

            Message m = new Message();
            m.author = author;
            m.message = message;

            return m;
        });
        return rs;
    }

    public static void save(ArrayList<Message> list) {
        String className = Message.class.getSimpleName();
        ModelFactory.save(className, list, model -> {
            ArrayList<String> lines = new ArrayList<>();
            lines.add(model.author);
            lines.add(model.message);
            return lines;
        });
    }
}
