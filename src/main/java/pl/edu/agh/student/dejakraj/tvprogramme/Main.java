package pl.edu.agh.student.dejakraj.tvprogramme;

import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class Main {

    private static final Logger logger = Logging.getLogger("TVProgramme");
    private static GUI gui = new GUI();

    public static void main(String[] args) {
        LinkedHashMap<String, ArrayList<Program>> sh2 = JsonDownloader.JsonDownload("2020-06-20");
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.createAndShowGUI(sh2);
            }
        });
        LinkedHashMap<String, ArrayList<Program>> sh = Downloader.Download();
        //Schedule sh2 = JsonDownloader.JsonDownload("2020-06-20");
        logger.info(sh.keySet().toString());
        logger.info(sh2.keySet().toString());
        /*for (int i = 0; i < 5; i++) {
            logger.info(sh.programs.get(i).get(i).getName());
            logger.info(sh2.programs.get(i).get(i).getName());
        }*/
        sh2.forEach((key, value) -> {
            for (int j = 0; j < value.size(); j++)
                logger.info(j + " " + value.get(j).getName() + "   " + value.get(j).getHour());
        });
    }
}
