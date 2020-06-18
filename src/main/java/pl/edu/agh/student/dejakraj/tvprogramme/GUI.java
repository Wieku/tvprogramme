package pl.edu.agh.student.dejakraj.tvprogramme;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.logging.log4j.Logger;

public class GUI {
    private static final Logger logger = Logging.getLogger("GUI");
    private JFrame f;
    private LinkedHashMap<String, LinkedHashMap<String, ArrayList<Program>>> map = new LinkedHashMap<String, LinkedHashMap<String, ArrayList<Program>>>();
    private JList<String> list1; //channels
    private JList<String> list2; //dates
    private JList<String> list3; //programs

    public GUI(){
        f = new JFrame("TVProgramme");
    }

    public void createAndShowGUI(){
        long milis = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date(milis));
        LinkedHashMap<String, ArrayList<Program>> data = JsonDownloader.JsonDownload(today);
        map.put(today, data);

        //channel list
        DefaultListModel<String> channels = new DefaultListModel<>();
        map.get(today).forEach((key, value) -> {
            channels.addElement(key);
        });
        list1 = new JList<>(channels);
        list1.setSelectedIndex(0);
        list1.setBounds(20,20, 200,500);
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                list3.setModel(GetProgramList(list2.getSelectedValue(), list1.getSelectedValue()));
            }
        });
        f.add(list1);

        //date list
        DefaultListModel<String> dates = new DefaultListModel<>();
        for (int i = 0; i < 5; i++) {
            dates.addElement(format.format(new Date(milis + i * 86400000)));
        }
        list2 = new JList<>(dates);
        list2.setSelectedIndex(0);
        list2.setBounds(240,20, 700,100);
        list2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                list3.setModel(GetProgramList(list2.getSelectedValue(), list1.getSelectedValue()));
            }
        });
        f.add(list2);

        //program list
        list3 = new JList<>(GetProgramList(today, list1.getSelectedValue()));
        list3.setBounds(240,140, 700,380);
        f.add(list3);

        //frame
        f.setSize(980,600);
        f.setLayout(null);
        f.setVisible(true);
    }

    private DefaultListModel<String> GetProgramList(String date, String channel){
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

