package src.controller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Recordingname {
    private String name;
    public class SimpleJButton {
        SimpleJButton() {
            JFrame f = new JFrame("Player Name");
            f.setLocationRelativeTo(null);

            JButton b = new JButton("Submit");
            b.setBounds(100, 100, 140, 40);

            JLabel label = new JLabel();
            label.setText("Enter your name :");
            label.setBounds(10, 10, 100, 100);

            JLabel label1 = new JLabel();
            label1.setBounds(10, 110, 200, 100);

            JTextField textfield = new JTextField();
            textfield.setBounds(110, 50, 130, 30);

            f.add(label1);
            f.add(textfield);
            f.add(label);
            f.add(b);
            f.setSize(300, 300);
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
        public class SimpleRanking {
            SimpleRanking(List<String> rankers) {

                int numofRankers = rankers.size();

                JFrame f = new JFrame("MVP");
                f.setLocationRelativeTo(null);
                f.setSize(300, 50 * numofRankers);

                JButton b = new JButton("Close");


                JLabel[] labels = new JLabel[numofRankers];
                for (int i = 0; i < numofRankers; i++) {
                    labels[i].setText(rankers.get(i));
                    labels[i].setBounds(10, 10 * (i + 1), 100, 100);
                    f.add(labels[i]);
                }
                b.setBounds(10, 10 * (numofRankers + 2), 250, 30);

                JLabel label1 = new JLabel();
                label1.setBounds(10, 110, 200, 100);

                JTextField textfield = new JTextField();
                textfield.setBounds(110, 50, 130, 30);

                f.add(label1);
                f.add(textfield);
                f.add(b);
                f.setLayout(null);
                f.setVisible(true);
                //action listener
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        f.dispose();
                    }
                });
            }
        }

        //랭크정보 출력하는 프레임 추가. 추후 랭킹정보 입력받게 변경예정
    public class Rankbar {
        JPanel jp_label, jp_btn;
        GridBagLayout Gbag = new GridBagLayout();
        GridBagConstraints gbc1;
        private List<String> names;
        private JLabel[] labels;
        private final int NUM = 50;
        Rankbar() {
            JFrame f = new JFrame();
            f.setLayout(null);    // 레이아웃을 NULL로 설정한다
            f.setSize(400, 300);
            f.setLocationRelativeTo(null);
            names = new ArrayList<>();
            labels = new JLabel[NUM];
            for (int i = 0; i < NUM; i++) {
                labels[i] = new JLabel();
                names.add("12345678");
            }
            jp_label = new JPanel();
            jp_label.setLayout(Gbag);
            jp_label.setBackground(Color.white);

            for (int i = 0; i < NUM; i++) {
                //레이아웃에 텍스트 추가
                labels[i].setText(names.get(i));
                create_form(labels[i], 0, 30 * i, 30, 10);
            }

            JScrollPane scroll = new JScrollPane(jp_label);  // 스크롤패널을 선언
            scroll.setBounds(10, 10, 250, f.getHeight() - 60);

            jp_btn = new JPanel();   // 버튼 패널
            jp_btn.setBounds(300, 0, 100, f.getHeight() - 60);

            JButton btn = new JButton("Exit");  // 버튼 생성
            btn.setBounds(300, 10, 100, f.getHeight() - 10);
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    f.dispose();
                    //
                    System.exit(0);
                    //
                }
            });
            jp_btn.add(btn);

            f.add(scroll);   // 스크롤패널 추가
            f.add(jp_btn);  // 버튼 패널 추가
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        }

        // 라벨 추가
        public void create_form(Component cmpt, int x, int y, int w, int h) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridx = x;
            gbc.gridy = y;
            gbc.gridwidth = w;
            gbc.gridheight = h;
            this.Gbag.setConstraints(cmpt, gbc);
            jp_label.add(cmpt);
            jp_label.updateUI();
        }
    }
    Recordingname() {}
    public String getName() {
        return name;
    }
}