package MVC;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Utility {
    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static InputStream fileStream(String path) throws FileNotFoundException {
        String resource = String.format("%s.class", Utility.class.getSimpleName());
        Utility.log("resource %s", resource);
        Utility.log("resource path %s", Utility.class.getResource(""));
        var res = Utility.class.getResource(resource);
        if (res != null && res.toString().startsWith("jar:")) {
            path = String.format("/%s", path);
            InputStream is = Utility.class.getResourceAsStream(path);
            if (is == null) {
                throw new FileNotFoundException(String.format("在 jar 里面找不到 %s", path));
            } else {
                return is;
            }
        } else {
            path = String.format("build/resources/main/%s", path);
            return new FileInputStream(path);
        }
    }

    public static void save(String path, String data) {
        try (FileOutputStream os = new FileOutputStream(path)) {
            os.write(data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            String s = String.format(
                    "Save file <%s> error <%s>",
                    path,
                    e
            );
            throw new RuntimeException(s);
        }
    }

    public static String load(String path) {
        byte[] body = new byte[1];
        try (FileInputStream is = new FileInputStream(path)) {
            body = is.readAllBytes();
        } catch (IOException e) {
            String s = String.format(
                    "Load file <%s> error <%s>",
                    path,
                    e
                    );
            throw new RuntimeException(s);
        }
        String r = new String(body, StandardCharsets.UTF_8);
        return r;
    }

    public static String html(String fileName) {
        String dir = "templates";
        String path = String.format("%s/%s", dir, fileName);
        byte[] body = new byte[1];
        try (InputStream is = Utility.fileStream(path)) {
            body = is.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String r = new String(body, StandardCharsets.UTF_8);
        return r;
    }

}
