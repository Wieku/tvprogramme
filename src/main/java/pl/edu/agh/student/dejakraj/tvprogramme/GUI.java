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

    private final JFrame window;

    private LinkedHashMap<String, LinkedHashMap<String, ArrayList<Program>>> map = new LinkedHashMap<String, LinkedHashMap<String, ArrayList<Program>>>();

    private JPanel mainPanel;

    private JList<String> channelList; //channels
    private JList<String> dateList; //dates
    private JList<String> programList; //programs

    public GUI(){
        window = new JFrame("TVProgramme");
    }

    public void createAndShowGUI(){
        Calendar calendar = Calendar.getInstance();

        String today = formatter.format(calendar.getTime());
        LinkedHashMap<String, ArrayList<Program>> data = JsonDownloader.JsonDownload(today);
        map.put(today, data);

        //channel list
        DefaultListModel<String> channels = new DefaultListModel<>();
        map.get(today).forEach((key, value) -> {
            channels.addElement(key);
        });

        mainPanel = new JPanel(new GridBagLayout());

        channelList = new JList<>(channels);
        channelList.setSelectedIndex(0);
        channelList.addListSelectionListener( e ->
                programList.setModel(getProgramList(dateList.getSelectedValue(), channelList.getSelectedValue()))
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

        dateList = new JList<>(dates);
        dateList.setSelectedIndex(0);
        dateList.addListSelectionListener(e ->
                programList.setModel(getProgramList(dateList.getSelectedValue(), channelList.getSelectedValue()))
        );

        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 0;

        mainPanel.add(new JScrollPane(dateList), constraints);

        //program list
        programList = new JList<>(getProgramList(today, channelList.getSelectedValue()));

        constraints.gridheight = 2;
        constraints.gridwidth = 2;
        constraints.gridx = 1;
        constraints.gridy = 1;

        mainPanel.add(new JScrollPane(programList), constraints);

        //frame
        window.add(mainPanel);
        window.setSize(980,600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    private DefaultListModel<String> getProgramList(String date, String channel){
        DefaultListModel<String> programs = new DefaultListModel<>();
        try {
            if(!map.containsKey(date)) {
                LinkedHashMap<String, ArrayList<Program>> data = JsonDownloader.JsonDownload(date);
                if (data.isEmpty()){
                    throw new Exception("Error while downloading JSON data");
                }
                map.put(date, data);
            }
            map.get(date).get(channel).forEach((Program p) -> {
                programs.addElement(p.getName() + " " + p.getHour());
            });
        }
        catch(Exception e) {
            logger.error(e);
            return null;
        }
        return programs;
    }
}

