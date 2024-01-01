package view;

import javax.swing.*;
import java.awt.*;

public class LoadErrorDialog extends JFrame {

    protected void init(){
        this.setLayout(null);
        this.setTitle("载入存档出错了！");
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        addLable();
        this.setSize(200,50);
        this.setVisible(true);
    }
    private void addLable(){
        JLabel errorLable = new JLabel("错误：载入存档不符合棋盘规则");
        errorLable.setFont(new Font("Rockwell", Font.BOLD,20));
        errorLable.setLocation(0,0);
        errorLable.setVisible(true);
        this.add(errorLable);
    }
}
