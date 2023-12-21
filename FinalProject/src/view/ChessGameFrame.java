package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FilenameFilter;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;

    private final int ONE_CHESS_SIZE;
    //private GameController gameController;//冯俊铭 23/12/10/22：14
    private ChessboardComponent chessboardComponent;
    private GameController gameController;
    private int viewScores = 0;//冯俊铭
    private int viewSteps =0;//冯俊铭 显示在主界面上的分数和步数
    private int viewlevel = 1;//冯俊铭 显示在主界面上的关卡
    private String viewMode= "手动模式";//冯俊铭 显示在主界面的模式
    private JLabel scoreLabel;//fjm
    private JLabel stepLabel;//fjm
    private JLabel levelLable;//fjm
    private JButton nextStepButton;//fjm
    private JButton swapButton;//fjm
    private JButton modeTransferButton;//fjm
    public ChessGameFrame(int width, int height) {
        setTitle("2023 CS109 Project Demo"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.ONE_CHESS_SIZE = (HEIGTH * 4 / 5) / 9;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);


        this.scoreLabel =loadScoreLabel();
        this.stepLabel=loadStepLabel();
        this.levelLable=loadLevelLabel();
        add(levelLable);
        add(scoreLabel);
        add(stepLabel);
        this.nextStepButton =addNextStepButton();
        add(nextStepButton);
        this.swapButton = addSwapConfirmButton();
        add(swapButton);
        this.modeTransferButton=addModeTransferButton();
        add(modeTransferButton);
        addChessboard();
        addHelloButton();
        addSaveButton();//冯俊铭
        this.setResizable(false);//冯俊铭 设置窗口不能改变大小
    }
    public void getGameControllerToLoadViewMode(GameController gameController) {
        //取出gameController用来判断游戏模式
        chessboardComponent.setGameController(gameController);
        //从gameController中导入游戏模式
        if (chessboardComponent.getGameController().getMode()==1){
            this.viewMode = "手动模式";
        }else if (chessboardComponent.getGameController().getMode()==2){
            this.viewMode = "自动模式";
        }else this.viewMode="模式没有导入成功";
    }//冯俊铭
    public ChessboardComponent getChessboardComponent() {
        return chessboardComponent;
    }

    public String getViewMode() {
        return viewMode;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
    }

    public void setChessboardComponent(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE);
        chessboardComponent.setLocation(HEIGTH / 5, HEIGTH / 10);
        add(chessboardComponent);
    }

    /**
     * 在游戏面板中添加标签
     */
    private JLabel loadScoreLabel() {
        Font font = new Font("雅黑", Font.PLAIN, 20);
        JLabel scoreLabel = new JLabel("当前分数:"+viewScores);
        scoreLabel.setLocation(HEIGTH, HEIGTH / 10);
        scoreLabel.setSize(200, 60);
        scoreLabel.setFont(font);
        return scoreLabel;
    }
    private JLabel loadStepLabel(){
        Font font = new Font("雅黑", Font.PLAIN, 20);
        JLabel stepLabel = new JLabel("当前步数:"+viewSteps);
        stepLabel.setLocation(HEIGTH, (HEIGTH / 10)+60);
        stepLabel.setSize(200, 60);
        stepLabel.setFont(font);
        return stepLabel;
    }
    private JLabel loadLevelLabel(){
        Font font = new Font("雅黑", Font.PLAIN, 20);
        JLabel levelLabel = new JLabel("当前关卡:"+viewlevel);
        levelLabel.setLocation(HEIGTH, (HEIGTH / 10)-60);
        levelLabel.setSize(200, 60);
        levelLabel.setFont(font);
        return levelLabel;
    }


    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addHelloButton() {
        JButton button = new JButton("Show Hello Here");
        button.addActionListener((e) -> {
            chessboardComponent.getGameController().onPlayerSwapChess();
        });
        button.setLocation(HEIGTH, HEIGTH / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    public int getViewScores() {
        return viewScores;
    }

    public void setViewScores(int viewScores) {
        this.viewScores = viewScores;
    }

    public int getViewSteps() {
        return viewSteps;
    }

    public void setViewSteps(int viewSteps) {
        this.viewSteps = viewSteps;
    }

    private JButton addSwapConfirmButton() {
        JButton button = new JButton("Confirm Swap");
        button.addActionListener((e) -> {
            if (viewMode.equals("手动模式")) {
            //手动模式调用手动模式的方法
                int swapreturnvalue = 0;
                swapreturnvalue = chessboardComponent.swapChess();
            /*try{
                swapreturnvalue = chessboardComponent.swapChess();
            }catch (Exception e1){
                //如果抛出异常（一般是null），则说明当前棋盘上有空格子，不能进行交换
                System.out.println(swapreturnvalue+"出错了，错误为："+e1.toString());
                JOptionPane.showMessageDialog(this,"当前棋盘上有空格子，请点击“下一步”进行下落棋子和重新生成新的棋子","还不能交换！！！",JOptionPane.WARNING_MESSAGE);
            }*/// TODO: 2023/12/20 这个部分很奇怪 先暂时注释掉
                //如果返回是0，有可能是也消除成功了！就是棋盘上新棋子生成后，不需要交换就存在可以消除的棋子的情况
                //返回100表示交换且消除成功
                //返回101表示交换失败
                //返回102表示只选择了一个点
                if (swapreturnvalue == 101) {
                    JOptionPane.showMessageDialog(this, "这两个格子不能交换噢", "错误啦！！！", JOptionPane.WARNING_MESSAGE);
                } else if (swapreturnvalue == 102) {
                    JOptionPane.showMessageDialog(this, "请选择两个格子进行交换", "棋盘上已经没有棋子可以消除了", JOptionPane.WARNING_MESSAGE);
                } else {
                    //若交换且消除成功就更新标签
                    updateLables();
                }
            } else if (viewMode.equals("自动模式")) {
                //自动模式就调用自动模式的方法
                chessboardComponent.autoSwapChess();
            }
            //fjm
        });
        button.setLocation(HEIGTH, HEIGTH / 10 + 200);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        return button;
    }

    private JButton addNextStepButton() {
        JButton button = new JButton("Next Step");
        button.addActionListener((e) -> {
            if (gameController.getMode()==1){
                //已解决：case1是下落，case2是补充新棋子，现在有一个问题，如果我在交换前先点击一次nextstep，那么就进入case2，则交换后再点击nextstep则不会下落，直接生成新棋子了
                //解决方法：在gameController下的交换方法中进行补充，使得每次交换后nextstep都必须为1，具体可以到gameController下的那个方法去看
                //手动模式就调用手动模式的方法
                chessboardComponent.nextStep();
            } else if (gameController.getMode()==2) {
                //自动模式就调用自动模式的方法
                chessboardComponent.autoNextStep();
            }

            updateLables();
        });//fjm
        button.setLocation(HEIGTH, HEIGTH / 10 + 280);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        return button;
    }//冯俊铭

    /*private void addLoadButton() {
        JButton button = new JButton("Load");
       button.setLocation(HEIGTH, HEIGTH / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            String path = JOptionPane.showInputDialog(this,"Input Path here");
            gameController.loadGameFromFile(path);
        });
    }//冯俊铭 23/12/10/22:56*/
    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGTH, HEIGTH / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            if (!gameController.isIronMode){
                //如果不是铁人游戏，则可以保存
                System.out.println("Click save");
                FileDialog fileDialog = new FileDialog(this,"选择你要保存的路径",FileDialog.SAVE);
                fileDialog.setVisible(true);
                String path = fileDialog.getDirectory()+fileDialog.getFile()+".txt";
                chessboardComponent.getGameController().saveGameToFile(path);
            }if (gameController.isIronMode){
                //如果是铁人游戏，则跳出弹窗提示不能保存
                System.out.println("这是铁人模式，不允许存档");
                JOptionPane.showMessageDialog(this,"这是铁人模式，不能存档！","不能存档！",JOptionPane.WARNING_MESSAGE);
            }

        });
    }//冯俊铭 23/12/10/21：29
    private JButton addModeTransferButton(){
        JButton button = new JButton("模式转换");
        button.setLocation(HEIGTH, HEIGTH / 10 + 440);
        button.setSize(200, 60);
        button.setFont(new Font("雅黑", Font.PLAIN, 20));
        button.addActionListener(e ->{
            if (gameController.getMode()==1){
                gameController.setMode(2);
                this.viewMode="自动模式";
                JOptionPane.showMessageDialog(this,"当前模式为:"+viewMode,"模式转换成功",JOptionPane.WARNING_MESSAGE);
            } else if (gameController.getMode()==2) {
                gameController.setMode(1);
                this.viewMode="手动模式";
                JOptionPane.showMessageDialog(this,"当前模式为:"+viewMode,"模式转换成功",JOptionPane.WARNING_MESSAGE);
            }
        });
        return button;
    }//fjm
    public void updateLables(){
        {//更新分数标签
            this.viewScores=chessboardComponent.getGameController().getScore();//从gameController里直接获得游戏分数 -> gameController里的score变量
            System.out.println("当前应显示分数为："+viewScores);
            this.scoreLabel.setText("当前分数:"+viewScores);//将取到的游戏分数修改到Label上
        }
        {//更新步数标签
            this.viewSteps=chessboardComponent.getGameController().getFootsteps();//同理 -> gameController里的steps变量
            System.out.println("当前应显示步数为："+viewSteps);
            this.stepLabel.setText("当前步数:"+viewSteps);
        }
        {//更新关卡标签
            this.viewlevel=chessboardComponent.getGameController().getLevel();//同理 -> gameController里的level变量
            System.out.println("当前应显示关卡为："+viewlevel);
            this.levelLable.setText("当前关卡："+viewlevel);
        }
    }
    public void registerGameController(GameController gameController){
        this.gameController = gameController;
    }//fjm 注册gameController方便调用里面的方法
}
