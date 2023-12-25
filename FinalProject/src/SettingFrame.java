package view;

import controller.GameController;
import model.Chessboard;

import javax.swing.*;
import java.awt.*;

public class SettingFrame extends JDialog {
    GameController gameController;
    ChessGameFrame mainFrame;
    StartFrame startFrame;
    JLabel background;
    JButton savingButton;
    JButton loadButton;
    JButton exitToStartFrameButton;
    JButton exitToDesktopButton;
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
                mainFrame.setVisible(false);
                startFrame.setVisible(true);
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


}
