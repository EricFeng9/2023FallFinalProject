package view;

import controller.GameController;
import model.Chessboard;
import view.ChessGameFrame;

import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {
    private final int WIDTH;
    private final int HEIGHT;
    GameController gameController;
    ChessGameFrame mainFrame;

    public StartFrame(int width, int height, GameController gameController, ChessGameFrame mainFrame){
        this.WIDTH=width;
        this.HEIGHT = height;
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        this.gameController = gameController;
        this.mainFrame=mainFrame;
        addLoadButton();
        addStartButton();
        this.setResizable(false);//冯俊铭 设置窗口不能改变大小
    }
    private void addLoadButton() {
        JButton button = new JButton("加载游戏");
        button.setLocation(50,200);
        button.setSize(200, 50);
        button.setFont(new Font("雅黑", Font.PLAIN, 20));
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click load");
            FileDialog fileDialog = new FileDialog(this,"选择你的存档",FileDialog.LOAD);
            fileDialog.setVisible(true);
            String path = fileDialog.getDirectory()+fileDialog.getFile();
            String savingname = fileDialog.getFile();//获取读到的文件名
            System.out.println("用户选中了:"+path);
            System.out.println("用户选中了:"+savingname);
            String[] cutname = savingname.split("\\.");//根据.把文件名分开，拿到后缀名
            String nameExtension = cutname[cutname.length-1];//最后一个部分就是.txt后缀名
            if (!nameExtension.equals("txt")){
                JOptionPane.showMessageDialog(this,"错误：存档文件格式应该为txt文本","错误101:载入错误",JOptionPane.WARNING_MESSAGE);
            }else{
                int returnError = gameController.loadGameFromFile(path);//执行文件载入方法并且得到个返回值
                if (returnError==100){
                    System.out.println("导入成功");
                    mainFrame.setVisible(true);
                    this.setVisible(false);
                } else if (returnError==102) {
                    JOptionPane.showMessageDialog(this,"错误：载入存档不符合棋盘规则","错误102:载入错误",JOptionPane.WARNING_MESSAGE);
                    //如果加载不成功 就弹出报错框
                } else if (returnError==103){
                    JOptionPane.showMessageDialog(this,"错误：载入存档中存在违规棋子","错误103:载入错误",JOptionPane.WARNING_MESSAGE);
                    //如果加载不成功 就弹出报错框
                } else if (returnError==104) {
                    JOptionPane.showMessageDialog(this,"错误：未预料的错误，请联系开发者处理","错误104:载入错误",JOptionPane.WARNING_MESSAGE);
                    //如果加载不成功 就弹出报错框
                }
            }

        });

    }//冯俊铭 23/12/11/17:56
    private void addStartButton() {
        JButton button = new JButton("新游戏");
        button.setLocation(50,300);
        button.setSize(200, 50);
        button.setFont(new Font("雅黑", Font.PLAIN, 20));
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click Start");
            StartNewGameFrame startNewGameFrame = new StartNewGameFrame();
            startNewGameFrame.startNewGameFrame(mainFrame,gameController);
            startNewGameFrame.setVisible(true);
            this.setVisible(false);
        });
    }//冯俊铭 23/12/11/17:56

}

