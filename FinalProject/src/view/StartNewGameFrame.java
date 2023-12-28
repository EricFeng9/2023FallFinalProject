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
    Choice skinchoice;
    Choice levelchoice;
    JCheckBox isIronCheckbox;
    JCheckBox canUseProp;
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
        setSize(310,420+50);//300 450
        //addstartLabel();
        addnameTextField();
        addsavingLabel();
        addmodeLabel();
        this.isIronCheckbox=addCheckBox1();
        add(isIronCheckbox);
        this.canUseProp = addCheckBox2();
        add(canUseProp);
        this.modechoice = addmodechoice();
        add(modechoice);
        this.skinchoice = addSkinchoice();
        add(skinchoice);
        this.levelchoice=addLevelchoice();
        add(levelchoice);
        addLevelLabel();// TODO: 2023/12/28
        addSkinLabel();
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
        startButton.setLocation(77,300+30+50);
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
            gameController.setName(gamename);//设置存档名
            //设置铁人模式
            gameController.isIronMode= isIronCheckbox.isSelected();
            if (isIronCheckbox.isSelected()){
                mainFrame.setTitle(gamename+"的游戏 【铁人模式】");
            }else mainFrame.setTitle(gamename+"的游戏");
            //设置手动自动模式
            if (modechoice.getSelectedItem().equals("手动模式")){
                gameController.setMode(1);//手动模式为模式1
            }else if(modechoice.getSelectedItem().equals("自动模式")){
                gameController.setMode(2);//自动模式为模式2
            }
            //设置道具
            if (canUseProp.isSelected()){
                gameController.setCanUseProp(false);
                mainFrame.setViewRemoveRow(0);
                mainFrame.setViewRemove33(0);
                mainFrame.setViewRefreshAll(0);
                mainFrame.setViewSuperSwap(0);
            }else {
                mainFrame.setViewRemoveRow(2);
                mainFrame.setViewRemove33(2);
                mainFrame.setViewRefreshAll(1);
                mainFrame.setViewSuperSwap(2);
            }
            //设置关卡
            gameController.setLevel(Integer.parseInt(levelchoice.getSelectedItem()));
            //设置皮肤
            mainFrame.setSkin(skinchoice.getSelectedItem());
            mainFrame.repaint();
            //检查游戏名称是否为空，不为空再启动
            if (gamename==null) {
                JOptionPane.showMessageDialog(this,"还没有输入存档名","还不能开始噢",JOptionPane.WARNING_MESSAGE);
            }else {
                try{
                    mainFrame.startPlayMusic();//fjm 打开主界面音乐播放(第一次打开主界面调用这个方法)
                }catch (Exception e1){
                    //第二次及以上打开主界面时，就不能用start方法，会报错
                    mainFrame.restartPlayMusic();//fjm 第二次打开主界面就要用这个方法
                }
                mainFrame.updateLables();//初始化参数后要更新标签
                mainFrame.getGameControllerToLoadViewMode(gameController);
                System.out.println("——————游戏初始化完毕——————");
                System.out.println("当前游戏存档名为："+gamename);//试输出存档名
                System.out.println("当前游戏模式为："+mainFrame.getViewMode());//试输出模式
                System.out.println("当前游戏主题为："+mainFrame.getSkin());//试输出当前皮肤
                System.out.println("当前游戏关卡为："+mainFrame.getViewlevel());//试输出当前关卡
                mainFrame.setVisible(true);
                this.setVisible(false);
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
        choice.setName("手动模式");
        choice.setLocation(110,140);
        choice.setSize(170,15);
        choice.setVisible(true);
        return choice;
    }
    private void addSkinLabel(){
        JLabel startLabel = new JLabel();
        Font font =new Font("雅黑",Font.BOLD,20);
        startLabel.setFont(font);
        startLabel.setText("棋盘皮肤");
        startLabel.setLocation(10,140+50);
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
        choice.setName("默认");
        choice.setLocation(110,140+50);
        choice.setSize(170,15);
        choice.setVisible(true);
        return choice;
    }//fjm

    private void addLevelLabel(){
        JLabel startLabel = new JLabel();
        Font font =new Font("雅黑",Font.BOLD,20);
        startLabel.setFont(font);
        startLabel.setText("选择关卡");
        startLabel.setLocation(10,140+50+50);
        startLabel.setSize(90,20);
        this.add(startLabel);
        startLabel.setVisible(true);
    }//fjm
    private Choice addLevelchoice(){
        Choice choice = new Choice();
        Font font =new Font("雅黑",Font.PLAIN,15);
        choice.add("1");
        choice.add("2");
        choice.add("3");
        choice.add("4");
        choice.add("5");
        choice.setFont(font);
        choice.setName("默认");
        choice.setLocation(110,140+50+50);
        choice.setSize(170,15);
        choice.setVisible(true);
        return choice;
    }//fjm
    private JCheckBox addCheckBox1(){
        JCheckBox checkBox1 = new JCheckBox();
        Font font =new Font("雅黑",Font.PLAIN,17);
        checkBox1.setFont(font);
        checkBox1.setText("铁人游戏（无法存档）");
        checkBox1.setLocation(20,200+50+50);
        checkBox1.setSize(200,17);
        checkBox1.setOpaque(false);//设置组件为透明
        return checkBox1;
    }
    private JCheckBox addCheckBox2(){
        JCheckBox checkBox2 = new JCheckBox();
        Font font =new Font("雅黑",Font.PLAIN,17);
        checkBox2.setFont(font);
        checkBox2.setText("禁用道具");
        checkBox2.setLocation(20,240+50+50);
        checkBox2.setSize(200,17);
        checkBox2.setOpaque(false);//组件为透明
        return checkBox2;
    }
    private JLabel addBackgroundLabel(){
        JLabel backgroundLabel = new JLabel(new ImageIcon("./icons/startNewGameFrame.png"));
        backgroundLabel.setSize(300,400+50);
        backgroundLabel.setLocation(0,0);
        return backgroundLabel;
    }//fjm 设置背景图
}// TODO: 2023/12/13 冯俊铭 创建新存档、选择关卡

