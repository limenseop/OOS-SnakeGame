package src.controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class Recordingname {
    private String name;
    public class SimpleJButton {
        SimpleJButton(){
            JFrame f=new JFrame("Player Name");
            f.setLocationRelativeTo(null);

            JButton b=new JButton("Submit");
            b.setBounds(100,100,140, 40);

            JLabel label = new JLabel();
            label.setText("Enter your name :");
            label.setBounds(10, 10, 100, 100);

            JLabel label1 = new JLabel();
            label1.setBounds(10, 110, 200, 100);

            JTextField textfield= new JTextField();
            textfield.setBounds(110, 50, 130, 30);

            f.add(label1);
            f.add(textfield);
            f.add(label);
            f.add(b);
            f.setSize(300,300);
            f.setLayout(null);
            f.setVisible(true);
            //action listener
            b.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    name = textfield.getText();
                    f.dispose();
                }
            });
        }
    }
    Recordingname() {}
    public String getName() {
        return name;
    }
}