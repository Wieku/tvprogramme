package pl.edu.agh.student.dejakraj.tvprogramme;

import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class Main {

    private static final Logger logger = Logging.getLogger("TVProgramme");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().createAndShowGUI());
    }
}
