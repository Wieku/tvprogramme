package pl.edu.agh.student.dejakraj.tvprogramme;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.util.ArrayList;

public class Logging {
    private static final String consolePattern = "%d{yyyy-MM-dd' 'HH:mm:ss.SSS} [%t] [%c] [%p]: %m%n";
    private static final String filePattern = "%d{yyyy-MM-dd' 'HH:mm:ss.SSS} [%t] [%p]: %m%n";
    private static final String logRoot = "./logs/";

    private static final ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

    private static final LayoutComponentBuilder consoleLayout;
    private static final LayoutComponentBuilder fileLayout;

    private static final ArrayList<String> loggers = new ArrayList<>();

    static {
        consoleLayout = builder.newLayout("PatternLayout");
        consoleLayout.addAttribute("pattern", consolePattern);
        consoleLayout.addAttribute("charset", "UTF-8");

        fileLayout = builder.newLayout("PatternLayout");
        fileLayout.addAttribute("pattern", filePattern);
        fileLayout.addAttribute("charset", "UTF-8");


        AppenderComponentBuilder consoleAppender = builder.newAppender("stdout", "Console");
        consoleAppender.add(consoleLayout);
        builder.add(consoleAppender);
    }

    public static Logger getLogger(final String name) {
        String lowerName = name.toLowerCase();
        if (!loggers.contains(lowerName)) {
            createLogger(lowerName);
            loggers.add(lowerName);
        }

        return LogManager.getLogger(lowerName);
    }

    private static void createLogger(final String name) {
        String lowerName = name.toLowerCase();

        AppenderComponentBuilder file = builder.newAppender(lowerName, "File");
        file.addAttribute("fileName", logRoot + lowerName + ".log");
        file.addAttribute("append", "false");
        file.add(fileLayout);

        builder.add(file);

        LoggerComponentBuilder logger = builder.newLogger(lowerName, Level.ALL);
        logger.add(builder.newAppenderRef("stdout"));
        logger.add(builder.newAppenderRef(lowerName));
        logger.addAttribute("additivity", false);

        builder.add(logger);

        Configurator.reconfigure(builder.build());
    }
}
