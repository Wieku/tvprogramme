package pl.edu.agh.student.dejakraj.tvprogramme;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
        channelList = new JList<>(channels);
        channelList.setSelectedIndex(0);
        channelList.setBounds(20,20, 200,500);
        channelList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                programList.setModel(getProgramList(dateList.getSelectedValue(), channelList.getSelectedValue()));
            }
        });
        window.add(channelList);

        //date list
        DefaultListModel<String> dates = new DefaultListModel<>();
        for (int i = 0; i < 5; i++) {
            if (i > 0)
                calendar.add(Calendar.DAY_OF_MONTH, 1);

            dates.addElement(formatter.format(calendar.getTime()));
        }

        dateList = new JList<>(dates);
        dateList.setSelectedIndex(0);
        dateList.setBounds(240,20, 700,100);
        dateList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                programList.setModel(getProgramList(dateList.getSelectedValue(), channelList.getSelectedValue()));
            }
        });
        window.add(dateList);

        //program list
        programList = new JList<>(getProgramList(today, channelList.getSelectedValue()));
        programList.setBounds(240,140, 700,380);
        window.add(programList);

        //frame
        window.setSize(980,600);
        window.setLayout(null);
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

