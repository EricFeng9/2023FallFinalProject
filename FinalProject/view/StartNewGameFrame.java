package view;

import controller.GameController;
import model.Chessboard;
import model.ChessboardPoint;
import model.Constant;

import javax.swing.*;
import java.awt.*;

public class StartNewGameFrame extends JFrame {

    GameController gameController;
    ChessGameFrame mainFrame;
    Chessboard chessboard;

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public ChessGameFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(ChessGameFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }

    public void setChessboard(Chessboard chessboard) {
        this.chessboard = chessboard;
    }





    protected void startNewGameFrame(ChessGameFrame mainFrame,GameController gameController){
        {//这部分代码是让窗口居中出现
            Dimension screenSize   =   Toolkit.getDefaultToolkit().getScreenSize();
            int scwidth = (int)screenSize.getWidth();
            int scheight = (int)screenSize.getHeight();
            setLocation((scwidth-300)/2,  (scheight-400)/2);
        }
        setTitle("创建一个新存档");
        setLayout(null);
        this.mainFrame=mainFrame;
        addStartButton();
        setSize(300,400);
        addstartLabel();
        addnameTextField();
        addsavingLabel();
        addmodeLabel();
        addCheckBox1();
        addCheckBox2();
        addchoice();
        this.setResizable(false);//冯俊铭 设置窗口不能改变大小
        this.gameController = gameController;
        this.chessboard = gameController.getModel();

    }
    private void addStartButton(){
        JButton startButton = new JButton("开始游戏！");
        Font font = new Font("雅黑",Font.PLAIN,20);
        startButton.setFont(font);
        startButton.setLocation(77,280);
        startButton.setSize(146,46);
        startButton.addActionListener(e -> {
            while (chessboard.ismatch()){
               gameController.onPlayerNextStep();
            }
            ChessboardComponent view = gameController.getView();
            //先遍历棋盘 删除掉view中各个Point点的Grid
            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                    ChessboardPoint point = new ChessboardPoint(i,j);
                    view.removeChessComponentAtGrid(point);
                }
            }
            view.initiateChessComponent(gameController.getModel());
            gameController.setView(view);
            gameController.setScore(0);
            gameController.setSteps(0);
            gameController.setLevel(1);
            mainFrame.setGameController(gameController);
            mainFrame.setVisible(true);
            this.setVisible(false);
        });
        this.add(startButton);
        startButton.setVisible(true);
    }
    private void addstartLabel(){
        JLabel startLabel = new JLabel();
        Font font =new Font("雅黑",Font.PLAIN,25);
        startLabel.setFont(font);
        startLabel.setText("创建一个新的游戏");
        startLabel.setLocation(40,15);
        startLabel.setSize(240,25);
        this.add(startLabel);
        startLabel.setVisible(true);
    }

    private void addsavingLabel(){
        JLabel startLabel = new JLabel();
        Font font =new Font("雅黑",Font.PLAIN,20);
        startLabel.setFont(font);
        startLabel.setText("存档名称");
        startLabel.setLocation(20,70);
        startLabel.setSize(80,20);
        this.add(startLabel);
        startLabel.setVisible(true);
    }
    private void addnameTextField(){
        JTextField nameTextField = new JFormattedTextField();
        Font font =new Font("雅黑",Font.PLAIN,16);
        nameTextField.setFont(font);
        nameTextField.setText("请输入你的存档名称");
        nameTextField.setLocation(110,70);
        nameTextField.setSize(170,20);
        this.add(nameTextField);
        nameTextField.setVisible(true);
    }
    private void addmodeLabel(){
        JLabel startLabel = new JLabel();
        Font font =new Font("雅黑",Font.PLAIN,20);
        startLabel.setFont(font);
        startLabel.setText("游戏模式");
        startLabel.setLocation(20,120);
        startLabel.setSize(80,20);
        this.add(startLabel);
        startLabel.setVisible(true);
    }
    private void addchoice(){
        Choice choice = new Choice();
        Font font =new Font("雅黑",Font.PLAIN,15);
        choice.add("简单模式");
        choice.add("普通模式");
        choice.add("困难模式");
        choice.setFont(font);
        choice.setName("请选择你的模式");
        choice.setLocation(110,120);
        choice.setSize(170,15);
        this.add(choice);
        choice.setVisible(true);
    }

    private void addCheckBox1(){
        JCheckBox checkBox1 = new JCheckBox();
        Font font =new Font("雅黑",Font.PLAIN,15);
        checkBox1.setFont(font);
        checkBox1.setText("铁人游戏（无法存档）");
        checkBox1.setLocation(20,180);
        checkBox1.setSize(175,15);
        this.add(checkBox1);
        checkBox1.setVisible(true);
    }
    private void addCheckBox2(){
        JCheckBox checkBox2 = new JCheckBox();
        Font font =new Font("雅黑",Font.PLAIN,15);
        checkBox2.setFont(font);
        checkBox2.setText("禁用道具");
        checkBox2.setLocation(20,220);
        checkBox2.setSize(175,15);
        this.add(checkBox2);
        checkBox2.setVisible(true);
    }
}// TODO: 2023/12/13 冯俊铭 创建新存档、选择关卡

