package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;

public class StartNewGameFrame extends JFrame {

    GameController gameController;
    ChessGameFrame mainFrame;

    protected void startNewGameFrame(ChessGameFrame mainFrame){
        setTitle("创建一个新存档");
        setLayout(null);
        this.mainFrame=mainFrame;
        addStartButton();
        setSize(200,100);
    }
    private void addStartButton(){
        JButton startButton = new JButton("开始游戏！");
        Font font = new Font("雅黑",Font.PLAIN,10);
        startButton.setFont(font);
        startButton.setSize(100,30);
        startButton.addActionListener(e -> {
            mainFrame.setVisible(true);
            this.setVisible(false);
        });
        this.add(startButton);
    }

}// TODO: 2023/12/13 冯俊铭 创建新存档、选择关卡

