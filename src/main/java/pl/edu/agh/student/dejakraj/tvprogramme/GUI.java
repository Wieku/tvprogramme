package pl.edu.agh.student.dejakraj.tvprogramme;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

import org.apache.logging.log4j.Logger;

public class GUI {
    private final Logger logger = Logging.getLogger("GUI");
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private LinkedHashMap<String, LinkedHashMap<String, ArrayList<Program>>> map = new LinkedHashMap<>();

    private JList<String> channelList; //channels
    private JList<String> dateList; //dates
    private JList<String> programList; //programs

    public void createAndShowGUI(){
        Calendar calendar = Calendar.getInstance();

        String today = formatter.format(calendar.getTime());
        LinkedHashMap<String, ArrayList<Program>> data = JsonDownloader.JsonDownload(today);
        map.put(today, data);

        //channel list
        DefaultListModel<String> channels = new DefaultListModel<>();
        map.get(today).forEach((key, value) ->
            channels.addElement(key)
        );


        logger.info("Creating main panel");

        JPanel mainPanel = new JPanel(new GridBagLayout());

        logger.info("Creating channel list panel");

        channelList = new JList<>(channels);
        channelList.setSelectedIndex(0);
        channelList.addListSelectionListener( e ->
                updateProgramList(dateList.getSelectedValue(), channelList.getSelectedValue())
        );

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1f;
        constraints.weighty = 1f;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridheight = 3;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;

        mainPanel.add(new JScrollPane(channelList), constraints);

        //date list
        DefaultListModel<String> dates = new DefaultListModel<>();
        for (int i = 0; i < 5; i++) {
            if (i > 0)
                calendar.add(Calendar.DAY_OF_MONTH, 1);

            dates.addElement(formatter.format(calendar.getTime()));
        }

        logger.info("Creating date list panel");

        dateList = new JList<>(dates);
        dateList.setSelectedIndex(0);
        dateList.addListSelectionListener(e ->
                updateProgramList(dateList.getSelectedValue(), channelList.getSelectedValue())
        );

        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 0;

        mainPanel.add(new JScrollPane(dateList), constraints);

        //program list

        logger.info("Creating program list panel");

        programList = new JList<>();

        constraints.gridheight = 2;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 1;

        mainPanel.add(new JScrollPane(programList), constraints);

        //frame

        logger.info("Creating window");

        JFrame window = new JFrame("TVProgramme");
        window.add(mainPanel);
        window.setSize(980,600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);

        updateProgramList(today, channelList.getSelectedValue());
    }

    private void updateProgramList(String date, String channel) {
        logger.info("Updating channel list...");

        DefaultListModel<String> programs = new DefaultListModel<>();

        Runnable subRoutine = () -> {
            map.get(date).get(channel).forEach((Program p) -> {
                programs.addElement(p.getHour() + " - " + p.getName());
            });

            programList.setModel(programs);
            logger.info("Channel list updated!");
        };

        if(map.containsKey(date)) {
            subRoutine.run();
            return;
        }

        new Thread(() -> {
            LinkedHashMap<String, ArrayList<Program>> data = JsonDownloader.JsonDownload(date);
            if (data != null && !data.isEmpty()){
                map.put(date, data);
                subRoutine.run();
            }
        }).start();
    }
}

