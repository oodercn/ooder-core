package net.ooder.esd.tool;

import freemarker.template.Configuration;

public class TempConfigFactory {
    private Configuration cfg;
    static TempConfigFactory factory;
    static final String THREAD_LOCK = "Thread Lock";


    public static TempConfigFactory getInstance() {
        if (factory == null) {
            synchronized (THREAD_LOCK) {
                if (factory == null) {
                    factory = new TempConfigFactory();
                }
            }
        }
        return factory;
    }

    public TempConfigFactory() {

    }

    public Configuration getCfg() {
        if (cfg == null) {
            this.cfg = new Configuration();
            cfg.setClassForTemplateLoading(this.getClass(), "/");
            cfg.setDefaultEncoding("UTF-8");
        }

        return cfg;
    }
}
