package MVC;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;

public class FMTemplate {
    static Configuration config;

    static {
        config = new Configuration(
                Configuration.VERSION_2_3_28);
        String resource = String.format("%s.class", Utility.class.getSimpleName());
        Utility.log("resource %s", resource);
        Utility.log("resource path %s", Utility.class.getResource(""));
        var res = Utility.class.getResource(resource);
        if (res != null && res.toString().startsWith("jar:")) {
            config.setClassForTemplateLoading(Utility.class, "/templates");
        } else {
            try {
                File f = new File("build/resources/main/templates");
                config.setDirectoryForTemplateLoading(f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        config.setDefaultEncoding("utf-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLogTemplateExceptions(true);
        config.setWrapUncheckedExceptions(true);
    }

    public static String render(Object data, String templateFileName) {
        Template template;
        try {
            template = config.getTemplate(templateFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(result);
        try {
            template.process(data, writer);
        } catch (TemplateException | IOException e) {
            String message = String.format("模板 process 失败 <%s> error<%s>", data, e);
            throw new RuntimeException(message, e);
        }
        return result.toString();
    }
}
