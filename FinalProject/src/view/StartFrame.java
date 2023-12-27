package view;

import controller.GameController;
import model.Chessboard;
import view.ChessGameFrame;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;

public class StartFrame extends JFrame {
    private final int WIDTH;
    private final int HEIGHT;
    GameController gameController;
    ChessGameFrame mainFrame;
    JTextPane notiecLable;
    JLabel background;
    public StartFrame(int width, int height, GameController gameController, ChessGameFrame mainFrame){
        this.WIDTH=width;//900
        this.HEIGHT = height;//600
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        this.gameController = gameController;
        this.mainFrame=mainFrame;
        this.notiecLable = addNoticeLabel();
        this.background= addBackgroundLabel();
        add(notiecLable);
        addLoadButton();
        addStartButton();
        add(background);
        background.setVisible(true);
        this.setResizable(false);//冯俊铭 设置窗口不能改变大小
    }
    private JTextPane addNoticeLabel(){
        Font font = new Font("雅黑", Font.BOLD, 15);
        JTextPane textArea = new JTextPane();
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setFocusable(false);
        //textArea.setText("<html>这是南方科技大学2023秋JavaA期末project<br>开发者：冯俊铭、江易明<br>这就是一个测试版，请勿外传<br>当前exe版本:1.0Alpha</html>");
        textArea.setText("这是南方科技大学2023秋JavaA期末project\n               开发者：冯俊铭、江易明\n                      测试版，请勿外传\n                  当前exe版本:1.1Alpha");
        textArea.setSize(300,90);
        textArea.setLocation((WIDTH-textArea.getWidth())/2,400);
        textArea.setFont(font);
        textArea.setAlignmentY(JTextPane.CENTER_ALIGNMENT);
        textArea.setAlignmentX(JTextPane.CENTER_ALIGNMENT);
        textArea.setForeground(Color.white);

        return textArea;
    }
    private void addLoadButton() {
        JButton button = new JButton("加载游戏");
        button.setSize(200, 50);
        button.setLocation((WIDTH-button.getWidth())/2,HEIGHT/2-100);
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
                if (returnError==1001){
                    //1001表示导入成功且为手动模式
                    mainFrame.getGameControllerToLoadViewMode(gameController);
                    System.out.println("——————游戏导入化完毕——————");
                    System.out.println("当前游戏存档名为："+gameController.getName());//试输出存档名
                    System.out.println("当前游戏模式为："+mainFrame.getViewMode());//试输出模式
                    //更新标签
                    mainFrame.updateLables();
                    mainFrame.setVisible(true);
                    this.setVisible(false);
                }if (returnError==1002){
                    //1002表示导入成功且为自动模式
                    mainFrame.getGameControllerToLoadViewMode(gameController);
                    System.out.println("——————游戏导入化完毕——————");
                    System.out.println("当前游戏存档名为："+gameController.getName());//试输出存档名
                    System.out.println("当前游戏模式为："+mainFrame.getViewMode());//试输出模式
                    //更新标签
                    mainFrame.updateLables();
                    mainFrame.setVisible(true);
                    this.setVisible(false);
                    //更新标签
                    /*mainFrame.updateLables();
                    mainFrame.setVisible(true);
                    this.setVisible(false);*/
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
        button.setSize(200, 50);
        button.setLocation((WIDTH-button.getWidth())/2,HEIGHT/2+100-100);
        button.setFont(new Font("雅黑", Font.PLAIN, 20));
        add(button);
        button.addActionListener(e -> {
            System.out.println("Click Start");
            StartNewGameFrame startNewGameFrame = new StartNewGameFrame();
            startNewGameFrame.startNewGameFrame(mainFrame,gameController);
            startNewGameFrame.setVisible(true);
        });
    }//冯俊铭 23/12/11/17:56
    private JLabel addBackgroundLabel(){
        JLabel backgroundLabel = new JLabel(new ImageIcon("./icons/startFrame.png"));
        backgroundLabel.setSize(900,600);
        backgroundLabel.setLocation(0,0);
        return backgroundLabel;
    }//fjm 设置背景图片
}

