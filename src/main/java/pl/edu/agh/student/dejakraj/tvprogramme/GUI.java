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
        JList<String> list = new JList<>(l1);
        list.setBounds(100,100, 75,75);
        f.add(list);
        f.setSize(400,400);
        f.setLayout(null);
        f.setVisible(true);
    }
}

