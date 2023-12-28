package view;

import controller.GameController;
import model.Chessboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SettingFrame extends JDialog {
    GameController gameController;
    ChessGameFrame mainFrame;
    StartFrame startFrame;
    JLabel background;
    JButton savingButton;
    JButton loadButton;
    JButton exitToStartFrameButton;
    JButton exitToDesktopButton;
    Choice changeSkinChoice;
    public SettingFrame(GameController gameController,ChessGameFrame mainFrame,StartFrame startFrame){
        this.mainFrame =mainFrame;
        this.gameController= gameController;
        this.startFrame = startFrame;
        setLayout(null);
        setSize(250,510);
        setLocationRelativeTo(null); // Center the window.
        this.savingButton=addSavingButton();
        add(savingButton);
        this.exitToStartFrameButton = addExitToStartFrameButton();
        add(exitToStartFrameButton);
        this.loadButton = addLoadButton();
        add(loadButton);
        this.exitToDesktopButton=addExitToDesktopButton();
        add(exitToDesktopButton);
        this.changeSkinChoice = addSkinchoice();
        add(changeSkinChoice);
        addSkinLabel();
        this.background=addBackgroundLabel();
        add(background);
        this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        this.setResizable(false);//冯俊铭 设置窗口不能改变大小
    }
    private JLabel addBackgroundLabel(){
        JLabel backgroundLabel = new JLabel(new ImageIcon("./icons/settingFrame.png"));
        backgroundLabel.setSize(252,500);
        backgroundLabel.setLocation(0,0);
        return backgroundLabel;
    }//fjm 设置背景图

    private void addSkinLabel(){
        JLabel startLabel = new JLabel();
        Font font =new Font("雅黑",Font.BOLD,16);
        startLabel.setFont(font);
        startLabel.setText("棋盘皮肤");
        startLabel.setLocation(10,70);
        startLabel.setSize(90,20);
        this.add(startLabel);
        startLabel.setVisible(true);
    }//fjm
    private Choice addSkinchoice(){
        Choice choice = new Choice();
        Font font =new Font("雅黑",Font.PLAIN,15);
        choice.add("默认");
        choice.add("PVZ主题");
        choice.setFont(font);
        choice.setName("请选择要更换的皮肤");
        choice.setLocation(10+70,70);
        choice.setSize(100,15);
        choice.setVisible(true);
        choice.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                mainFrame.setSkin(choice.getSelectedItem());
            }
        });
        return choice;
    }//fjm
    private JButton addSavingButton(){
        JButton button = new JButton("保存游戏");
        button.setLocation(20, 180);
        button.setSize(95, 35);
        button.setFont(new Font("雅黑", Font.BOLD, 14));
        button.addActionListener(e -> {
            mainFrame.Saving();
        });
        return button;
    }
    private JButton addLoadButton(){
        JButton button = new JButton("加载游戏");
        button.setLocation(130, 180);
        button.setSize(95, 35);
        button.setFont(new Font("雅黑", Font.BOLD, 14));
        button.addActionListener(e -> {
            mainFrame.Load();
        });
        return button;
    }
    private JButton addExitToStartFrameButton(){
        JButton button = new JButton("退出到主菜单");
        button.setLocation(35, 230);
        button.setSize(180, 50);
        button.setFont(new Font("雅黑", Font.BOLD, 20));
        button.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this,"确定退出吗？游戏可能没有保存","请再次确认",JOptionPane.OK_CANCEL_OPTION);
            if (option==JOptionPane.OK_OPTION){
                this.setVisible(false);
                mainFrame.stopPlayMusic();
                mainFrame.setVisible(false);
                startFrame.restartPlayMusic();//使主界面音乐重新播放
            }
        });
        return button;
    }
    private JButton addExitToDesktopButton(){
        JButton button = new JButton("退出到桌面");
        button.setLocation(35, 295);
        button.setSize(180, 50);
        button.setFont(new Font("雅黑", Font.BOLD, 20));
        button.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this,"确定退出吗？游戏可能没有保存","请再次确认",JOptionPane.OK_CANCEL_OPTION);
            if (option==JOptionPane.OK_OPTION){
                System.exit(0);
            }
        });
        return button;
    }

    public StartFrame getStartFrame() {
        return startFrame;
    }

    public void setStartFrame(StartFrame startFrame) {
        this.startFrame = startFrame;
    }
}
