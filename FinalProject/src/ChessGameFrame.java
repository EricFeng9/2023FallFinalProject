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
    private JLabel background;//fjm
    private JLabel settingButton;//fjm
    private SettingFrame settingFrame;//冯俊铭

    public ChessGameFrame(int width, int height) {
        setTitle("2023 CS109 Project Demo"); //设置标题
        this.WIDTH = width;//1100
        this.HEIGTH = height;//810
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
        addHelloButton2();
        addHelloButton3();
        //addSaveButton();//冯俊铭
        this.settingButton = addSettingsButton();
        add(settingButton);
        this.background=addBackgroundLabel();
        add(background);
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
        JButton button = new JButton(" ");//设置显示在按钮上的文字
        button.addActionListener((e) -> {
            //点击监听器 鼠标点击后执行的代码
            gameController.removeRow();
        });
        button.setLocation(15, 120);
        button.setSize(100, 50);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }
    private void addHelloButton2() {
        JButton button = new JButton(" ");//设置显示在按钮上的文字
        button.addActionListener((e) -> {
            //点击监听器 鼠标点击后执行的代码
            gameController.remove2();
        });
        button.setLocation(15, 120+190);
        button.setSize(100, 50);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addHelloButton3() {
        JButton button = new JButton(" ");//设置显示在按钮上的文字
        button.addActionListener((e) -> {
            //点击后 将超级交换步数设置为3
            gameController.supersteps=3;

        });
        button.setLocation(15, 120+190+190);
        button.setSize(100, 50);
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
                if (gameController.supersteps>0){
                    gameController.superswap();
                }else {
                    int swapreturnvalue = 0;
                    swapreturnvalue = chessboardComponent.swapChess();
                    if (swapreturnvalue == 101) {
                        JOptionPane.showMessageDialog(this, "这两个格子不能交换噢", "错误啦！！！", JOptionPane.WARNING_MESSAGE);
                    } else if (swapreturnvalue == 102) {
                        JOptionPane.showMessageDialog(this, "请选择两个格子进行交换", "棋盘上已经没有棋子可以消除了", JOptionPane.WARNING_MESSAGE);
                    } else {
                        //若交换且消除成功就更新标签
                        updateLables();
                    }
                }
            //手动模式调用手动模式的方法

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

    /*protected JButton addSaveButton() {
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
        return button;
    }//冯俊铭 23/12/10/21：29*/
    protected void Saving(){
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
    }//冯俊铭 把save方法整理到这里，这样每个按钮就只用调用这一个方法

    protected void Load() {
        System.out.println("Click load");
        FileDialog fileDialog = new FileDialog(this, "选择你的存档", FileDialog.LOAD);
        fileDialog.setVisible(true);
        String path = fileDialog.getDirectory() + fileDialog.getFile();
        String savingname = fileDialog.getFile();//获取读到的文件名
        System.out.println("用户选中了:" + path);
        System.out.println("用户选中了:" + savingname);
        String[] cutname = savingname.split("\\.");//根据.把文件名分开，拿到后缀名
        String nameExtension = cutname[cutname.length - 1];//最后一个部分就是.txt后缀名
        if (!nameExtension.equals("txt")) {
            JOptionPane.showMessageDialog(this, "错误：存档文件格式应该为txt文本", "错误101:载入错误", JOptionPane.WARNING_MESSAGE);
        } else {
            int returnError = gameController.loadGameFromFile(path);//执行文件载入方法并且得到个返回值
            if (returnError == 1001) {
                //1001表示导入成功且为手动模式
                getGameControllerToLoadViewMode(gameController);
                System.out.println("——————游戏导入化完毕——————");
                System.out.println("当前游戏存档名为：" + gameController.getName());//试输出存档名
                System.out.println("当前游戏模式为：" + getViewMode());//试输出模式
                //更新标签
                updateLables();
                setVisible(true);
            }
            if (returnError == 1002) {
                //1002表示导入成功且为自动模式
                getGameControllerToLoadViewMode(gameController);
                System.out.println("——————游戏导入化完毕——————");
                System.out.println("当前游戏存档名为：" + gameController.getName());//试输出存档名
                System.out.println("当前游戏模式为：" + getViewMode());//试输出模式
                //更新标签
                updateLables();
                setVisible(true);
                //更新标签
                    /*mainFrame.updateLables();
                    mainFrame.setVisible(true);
                    this.setVisible(false);*/
            } else if (returnError == 102) {
                JOptionPane.showMessageDialog(this, "错误：载入存档不符合棋盘规则", "错误102:载入错误", JOptionPane.WARNING_MESSAGE);
                //如果加载不成功 就弹出报错框
            } else if (returnError == 103) {
                JOptionPane.showMessageDialog(this, "错误：载入存档中存在违规棋子", "错误103:载入错误", JOptionPane.WARNING_MESSAGE);
                //如果加载不成功 就弹出报错框
            } else if (returnError == 104) {
                JOptionPane.showMessageDialog(this, "错误：未预料的错误，请联系开发者处理", "错误104:载入错误", JOptionPane.WARNING_MESSAGE);
                //如果加载不成功 就弹出报错框
            }
        }
    }//fjm 把load方法整理到这里，这样每个按钮就只用调用这一个方法
    private JButton addModeTransferButton(){
        JButton button = new JButton("模式转换");
        button.setLocation(HEIGTH, HEIGTH / 10 + 440);
        button.setSize(200, 60);
        button.setFont(new Font("雅黑", Font.PLAIN, 20));
        button.addActionListener(e ->{
            Boolean canTransfergame = true;
            if (gameController.getMode() == 1) {
                //手动模式转自动模式
                try {
                    canTransfergame = !gameController.getModel().ismatch();
                }catch (Exception e1){
                    canTransfergame = false;
                }
                if (gameController.getNextstep() == 1 && canTransfergame) {
                    gameController.setMode(2);
                    this.viewMode = "自动模式";
                    JOptionPane.showMessageDialog(this, "当前模式为:" + viewMode, "模式转换成功", JOptionPane.WARNING_MESSAGE);
                }else if (gameController.getNextstep() == 2 || !canTransfergame){
                    JOptionPane.showMessageDialog(this, "请确保棋盘上没有可以消除的棋子且棋盘无空格再转换模式", "模式转换失败", JOptionPane.WARNING_MESSAGE);
                }else {
                    JOptionPane.showMessageDialog(this, "未知错误", "模式转换失败", JOptionPane.WARNING_MESSAGE);
                }
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
    private JLabel addBackgroundLabel(){
        JLabel backgroundLabel = new JLabel(new ImageIcon("./icons/chessGameFrame.png"));
        backgroundLabel.setSize(1090,800);
        backgroundLabel.setLocation(0,0);
        return backgroundLabel;
    }//fjm 设置背景图
    private JLabel addSettingsButton(){
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon("./icons/settingButton.png"));
        label.setSize(40,40);
        label.setLocation(5,5);
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("鼠标已点击设置!");
                settingFrame.setVisible(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                label.setIcon(new ImageIcon("./icons/settingButton_press.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                label.setIcon(new ImageIcon("./icons/settingButton.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        return label;
    }//冯俊铭 用JLabel添加设置按钮

    public void registerSettingFrame(SettingFrame settingFrame){
        this.settingFrame = settingFrame;
    }//冯俊铭 给主窗口绑定设置窗口
}