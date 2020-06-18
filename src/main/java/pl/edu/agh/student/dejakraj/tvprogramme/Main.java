package pl.edu.agh.student.dejakraj.tvprogramme;

import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = Logging.getLogger("TVProgramme");
    private static GUI gui = new GUI();

    public static void main(String[] args) {


        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.createAndShowGUI();
            }
        });
    }
}
