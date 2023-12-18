package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;
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
    private GameController gameController;//冯俊铭 23/12/10/22：14
    private ChessboardComponent chessboardComponent;
    private int viewScores = 0;//冯俊铭
    private int viewSteps =0;//冯俊铭 显示在主界面上的分数和步数
    private int viewlevel = 1;//冯俊铭 显示在主界面上的关卡
    private JLabel scoreLabel;//fjm
    private JLabel stepLabel;//fjm
    private JLabel levelLable;//fjm
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
        addChessboard();
        addHelloButton();
        addSwapConfirmButton();
        addNextStepButton();
        addSaveButton();//冯俊铭

        this.setResizable(false);//冯俊铭 设置窗口不能改变大小
    }
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
    public ChessboardComponent getChessboardComponent() {
        return chessboardComponent;
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
        button.addActionListener((e) -> JOptionPane.showMessageDialog(this, "Hello, world!"));
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

    private void addSwapConfirmButton() {
        JButton button = new JButton("Confirm Swap");
        button.addActionListener((e) -> {
            if (chessboardComponent.swapChess()){
                //若交换成功就更新步数
                {//更新步数标签
                    this.viewSteps=gameController.getFootsteps();//同理 -> gameController里的steps变量
                    System.out.println("当前应显示步数为："+viewSteps);
                    this.stepLabel.setText("当前步数:"+viewSteps);
                }
            }//fjm

        });
        button.setLocation(HEIGTH, HEIGTH / 10 + 200);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addNextStepButton() {
        JButton button = new JButton("Next Step");
        button.addActionListener((e) -> {
            chessboardComponent.nextStep();
            {//更新分数标签
                this.viewScores=gameController.getScore();//从gameController里直接获得游戏分数 -> gameController里的score变量
                System.out.println("当前应显示分数为："+viewScores);
                this.scoreLabel.setText("当前分数:"+viewScores);//将取到的游戏分数修改到Label上
            }
            {//更新步数标签
                this.viewSteps=gameController.getFootsteps();//同理 -> gameController里的steps变量
                System.out.println("当前应显示步数为："+viewSteps);
                this.stepLabel.setText("当前步数:"+viewSteps);
            }
            {//更新关卡标签
                this.viewlevel=gameController.getLevel();//同理 -> gameController里的level变量
                System.out.println("当前应显示关卡为："+viewlevel);
                this.levelLable.setText("当前关卡："+viewlevel);
            }
        });//fjm
        button.setLocation(HEIGTH, HEIGTH / 10 + 280);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
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
            System.out.println("Click save");
            FileDialog fileDialog = new FileDialog(this,"选择你要保存的路径",FileDialog.SAVE);
            fileDialog.setVisible(true);
            String path = fileDialog.getDirectory()+fileDialog.getFile()+".txt";
            gameController.saveGameToFile(path);
        });
    }//冯俊铭 23/12/10/21：29

}
