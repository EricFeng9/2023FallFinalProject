package view;

import controller.GameController;
import model.Chessboard;
import model.ChessboardPoint;
import model.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

public class StartNewGameFrame extends JFrame {

    GameController gameController;
    ChessGameFrame mainFrame;
    Chessboard chessboard;
    String gamename;
    Choice modechoice;
    JCheckBox isIronCheckbox;
    JLabel background;
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
        setSize(310,420);//300 400
        //addstartLabel();
        addnameTextField();
        addsavingLabel();
        addmodeLabel();
        this.isIronCheckbox=addCheckBox1();
        add(isIronCheckbox);
        addCheckBox2();
        this.modechoice = addmodechoice();
        add(modechoice);
        this.background =addBackgroundLabel();
        add(background);
        this.setResizable(false);//冯俊铭 设置窗口不能改变大小
        this.gameController = gameController;
        this.chessboard = gameController.getModel();

    }
    private void addStartButton(){
        JButton startButton = new JButton("开始游戏！");
        Font font = new Font("雅黑",Font.PLAIN,20);
        startButton.setFont(font);
        startButton.setLocation(77,300);
        startButton.setSize(146,46);
        startButton.addActionListener(e -> {
            //先把gameController中的view取出来（无所谓这个view长什么样）
            ChessboardComponent view = gameController.getView();

            gameController.getModel().initPieces();//让model棋盘的元素重新生成

            //遍历棋盘 删除掉view中各个Point点的Grid
            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                    ChessboardPoint point = new ChessboardPoint(i,j);
                    view.removeChessComponentAtGrid(point);
                }
            }
            //再根据初始化好的model棋盘重新生成view
            view.initiateChessComponent(gameController.getModel());
            //初始化model棋盘
            while (chessboard.ismatch()){
               gameController.onPlayerAutoNextStep();
            }

            //先遍历棋盘 删除掉view中各个Point点的Grid
            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                    ChessboardPoint point = new ChessboardPoint(i,j);
                    view.removeChessComponentAtGrid(point);
                }
            }
            //再根据初始化好的model棋盘重新生成view
            view.initiateChessComponent(gameController.getModel());
            //再将view传回gameController
            gameController.setView(view);
            //初始化参数、导入棋盘和控制器
            gameController.setScore(0);
            gameController.setSteps(0);
            gameController.setLevel(1);
            mainFrame.updateLables();//初始化参数后要更新分数标签
            gameController.setName(gamename);//设置存档名
            gameController.isIronMode= isIronCheckbox.isSelected();//设置铁人模式
            if (isIronCheckbox.isSelected()){
                mainFrame.setTitle(gamename+"的游戏 【铁人模式】");
            }else mainFrame.setTitle(gamename+"的游戏");

            if (modechoice.getSelectedItem().equals("手动模式") && gamename!=null){
                gameController.setMode(1);//手动模式为模式1
                mainFrame.getGameControllerToLoadViewMode(gameController);
                System.out.println("——————游戏初始化完毕——————");
                System.out.println("当前游戏存档名为："+gamename);//试输出存档名
                System.out.println("当前游戏模式为："+mainFrame.getViewMode());//试输出模式
                mainFrame.setVisible(true);
                this.setVisible(false);
            }else if(modechoice.getSelectedItem().equals("自动模式")&& gamename!=null){
                gameController.setMode(2);//自动模式为模式2
                mainFrame.getGameControllerToLoadViewMode(gameController);
                System.out.println("——————游戏初始化完毕——————");
                System.out.println("当前游戏存档名为："+gamename);//试输出存档名
                System.out.println("当前游戏模式为："+mainFrame.getViewMode());//试输出模式
                mainFrame.setVisible(true);
                this.setVisible(false);
            } else if (gamename==null) {
                JOptionPane.showMessageDialog(this,"还没有输入存档名","还不能开始噢",JOptionPane.WARNING_MESSAGE);
            } else{
                JOptionPane.showMessageDialog(this,"还未选择模式","还不能开始噢",JOptionPane.WARNING_MESSAGE);
            }

        });
        this.add(startButton);
        startButton.setVisible(true);
    }//fjm
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
        Font font =new Font("雅黑",Font.BOLD,20);
        startLabel.setFont(font);
        startLabel.setText("存档名称");
        startLabel.setLocation(10,90);
        startLabel.setSize(90,20);
        this.add(startLabel);
        startLabel.setVisible(true);
    }
    private void addnameTextField(){
        TextField nameTextField = new TextField();
        Font font =new Font("雅黑",Font.PLAIN,16);
        nameTextField.setFont(font);
        nameTextField.setText("请输入你的存档名称");
        nameTextField.setLocation(110,90);
        nameTextField.setSize(170,20);
        this.add(nameTextField);
        nameTextField.addTextListener(new TextListener() {
            @Override
            public void textValueChanged(TextEvent e) {
                System.out.println("当前内容："+nameTextField.getText());
                gamename =nameTextField.getText();
            }
        });

        nameTextField.setVisible(true);
    }
    private void addmodeLabel(){
        JLabel startLabel = new JLabel();
        Font font =new Font("雅黑",Font.BOLD,20);
        startLabel.setFont(font);
        startLabel.setText("游戏模式");
        startLabel.setLocation(10,140);
        startLabel.setSize(90,20);
        this.add(startLabel);
        startLabel.setVisible(true);
    }
    private Choice addmodechoice(){
        Choice choice = new Choice();
        Font font =new Font("雅黑",Font.PLAIN,15);
        choice.add("手动模式");
        choice.add("自动模式");
        choice.setFont(font);
        choice.setName("请选择你的模式");
        choice.setLocation(110,140);
        choice.setSize(170,15);
        choice.setVisible(true);
        return choice;
    }

    private JCheckBox addCheckBox1(){
        JCheckBox checkBox1 = new JCheckBox();
        Font font =new Font("雅黑",Font.PLAIN,17);
        checkBox1.setFont(font);
        checkBox1.setText("铁人游戏（无法存档）");
        checkBox1.setLocation(20,200);
        checkBox1.setSize(200,17);
        checkBox1.setOpaque(false);//设置组件为透明
        return checkBox1;
    }
    private void addCheckBox2(){
        JCheckBox checkBox2 = new JCheckBox();
        Font font =new Font("雅黑",Font.PLAIN,17);
        checkBox2.setFont(font);
        checkBox2.setText("禁用道具");
        checkBox2.setLocation(20,240);
        checkBox2.setSize(200,17);
        checkBox2.setOpaque(false);//组件为透明
        this.add(checkBox2);
        checkBox2.setVisible(true);
    }
    private JLabel addBackgroundLabel(){
        JLabel backgroundLabel = new JLabel(new ImageIcon("./icons/startNewGameFrame.png"));
        backgroundLabel.setSize(300,400);
        backgroundLabel.setLocation(0,0);
        return backgroundLabel;
    }//fjm 设置背景图
}// TODO: 2023/12/13 冯俊铭 创建新存档、选择关卡

