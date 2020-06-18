package pl.edu.agh.student.dejakraj.tvprogramme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JFrame f;
    public GUI(){
        f = new JFrame("TVProgramme");
    }

    public void createAndShowGUI(Schedule sh){
        DefaultListModel<String> li = new DefaultListModel<>();
        sh.channelList.forEach((String e) -> {
            li.addElement(e);
        });
        JList<String> list = new JList<>(li);
        list.setBounds(20,20, 200,440);
        f.add(list);
        f.setSize(400,600);
        f.setLayout(null);
        f.setVisible(true);
    }
}

