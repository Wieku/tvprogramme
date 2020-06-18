package pl.edu.agh.student.dejakraj.tvprogramme;

import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = Logging.getLogger("TVProgramme");
    private static GUI gui = new GUI();

    public static void main(String[] args) {
        Schedule sh2 = JsonDownloader.JsonDownload("2020-06-20");
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.createAndShowGUI(sh2);
            }
        });
        Schedule sh = Downloader.Download();
        //Schedule sh2 = JsonDownloader.JsonDownload("2020-06-20");
        logger.info(sh.channelList.toString());
        logger.info(sh2.channelList.toString());
        /*for (int i = 0; i < 5; i++) {
            logger.info(sh.programs.get(i).get(i).getName());
            logger.info(sh2.programs.get(i).get(i).getName());
        }*/
        for (int i = 0; i < sh2.channelList.size(); i++)
            for (int j = 0; j < sh2.programs.get(i).size(); j++)
                logger.info(i + " " + j + " " + sh2.programs.get(i).get(j).getName() + "   " + sh2.programs.get(i).get(j).getHour());
    }
}
