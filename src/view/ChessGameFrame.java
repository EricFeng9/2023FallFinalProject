package view;

import controller.GameController;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Random;

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
    private int viewRemove33 = 0;//fjm 显示道具数目

    private int viewRemoveRow = 0;//fjm 显示道具数目
    private int viewRefreshAll = 0;//fjm 显示道具数目
    private int viewSuperSwap = 0;//fjm 显示道具数目

    private String viewMode= "手动模式";//冯俊铭 显示在主界面的模式
    private JLabel scoreLabel;//fjm
    private JLabel stepLabel;//fjm
    private JLabel levelLable;//fjm
    private JButton nextStepButton;//fjm
    private JButton swapButton;//fjm
    private JButton modeTransferButton;//fjm
    private JButton backButton;//fjm
    private JLabel background;//fjm
    private JLabel settingButton;//fjm
    private JLabel viewSuperSteps;//fjm
    private JLabel removeRowButton;//fjm
    private JLabel remove33Button;//fjm
    private JLabel refreshAllButton;//fjm
    private JLabel reArrangeAllButton;//fjm
    private JLabel superSwapButton;//fjm
    private JLabel viewRemove33Label;//fjm
    private JLabel viewRemoveRowLabel;//fjm
    private JLabel viewRefreshAllLabel;//fjm
    private JLabel viewSuperSwapLabel;//fjm
    private SettingFrame settingFrame;//冯俊铭
    private String skin = "default";//fjm
    Thread musicPlayer = new Thread(()->{
        while(true) {playMusic();}
    });// Lambda表达式 fjm播放音乐用的
    public ChessGameFrame(int width, int height) {
        setTitle("2023 CS109 Project Demo"); //设置标题
        this.WIDTH = width;//1090
        this.HEIGTH = height;//810
        this.ONE_CHESS_SIZE = (HEIGTH * 4 / 5) / 9;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        addLevelDemandLabel();
        this.scoreLabel =loadScoreLabel();
        this.stepLabel=loadStepLabel();
        this.levelLable=loadLevelLabel();
        this.viewSuperSteps=addViewSuperStepsLabel();
        add(levelLable);
        add(scoreLabel);
        add(stepLabel);
        add(viewSuperSteps);
        this.viewRemoveRowLabel=addViewRemoveRowLabel();
        this.viewRefreshAllLabel=addViewRefreshAllLabel();
        this.viewRemove33Label = addViewRemove33Label();
        this.viewSuperSwapLabel = addViewSuperSwapLabel();
        add(viewRemoveRowLabel);
        add(viewRemove33Label);
        add(viewSuperSwapLabel);
        add(viewRefreshAllLabel);
        this.nextStepButton =addNextStepButton();
        add(nextStepButton);
        this.swapButton = addSwapConfirmButton();
        add(swapButton);
        this.modeTransferButton=addModeTransferButton();
        add(modeTransferButton);
        addChessboard();
        this.backButton=addBackButton();
        add(backButton);
        this.removeRowButton = addRemoveRowButton();
        add(removeRowButton);
        this.remove33Button =addRemove33Button();
        add(remove33Button);
        this.refreshAllButton = addRefreshALlButton();
        add(refreshAllButton);
        this.reArrangeAllButton=addIsDeadEnd();
        add(reArrangeAllButton);
        this.superSwapButton = addSuperSwapButton();
        add(superSwapButton);
        addRefreshALlButton();
        addTipButton();
        addMusicLabel();
        addMusicChangeLabel();
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
        Font font = new Font("雅黑", Font.BOLD, 20);
        JLabel scoreLabel = new JLabel("当前分数:"+viewScores);
        scoreLabel.setLocation(HEIGTH, HEIGTH / 10);
        scoreLabel.setSize(200, 60);
        scoreLabel.setFont(font);
        return scoreLabel;
    }
    private JLabel loadStepLabel(){
        Font font = new Font("雅黑", Font.BOLD, 20);
        JLabel stepLabel = new JLabel("当前步数:"+viewSteps);
        stepLabel.setLocation(HEIGTH, (HEIGTH / 10)+60);
        stepLabel.setSize(200, 60);
        stepLabel.setFont(font);
        return stepLabel;
    }
    private JLabel loadLevelLabel(){
        Font font = new Font("雅黑", Font.BOLD, 20);
        JLabel levelLabel = new JLabel("当前关卡:"+viewlevel);
        levelLabel.setLocation(HEIGTH, (HEIGTH / 10)-60);
        levelLabel.setSize(200, 60);
        levelLabel.setFont(font);
        return levelLabel;
    }
    private JLabel addViewSuperStepsLabel(){
        Font font = new Font("雅黑", Font.PLAIN, 20);
        JLabel label = new JLabel();
        try{
            label.setText("当前剩余超级交换步数为："+gameController.supersteps);
        }catch (Exception e){
            label.setText("当前剩余超级交换步数为：0");
        }
        label.setLocation(HEIGTH, (HEIGTH / 10)+120);
        label.setSize(400, 60);
        label.setFont(font);
        label.setForeground(Color.red);
        label.setVisible(true);
        return label;
    }

    private JLabel addViewRemoveRowLabel(){
        JLabel label = new JLabel("×"+viewRemoveRow);
        Font font = new Font("雅黑", Font.BOLD, 30);
        label.setFont(font);
        label.setSize(90,30);
        label.setLocation(15+80, 120+80-30);
        return label;
    }
    private JLabel addViewRemove33Label(){
        JLabel label = new JLabel("×"+viewRemove33);
        Font font = new Font("雅黑", Font.BOLD, 30);
        label.setFont(font);
        label.setSize(90,30);
        label.setLocation(15+80, 120+120+80-30);
        return label;
    }
    private JLabel addViewRefreshAllLabel(){
        JLabel label = new JLabel("×"+viewRefreshAll);
        Font font = new Font("雅黑", Font.BOLD, 30);
        label.setFont(font);
        label.setSize(90,30);
        label.setLocation(15+80, 120+120+120+80-30);
        return label;
    }
    private JLabel addViewSuperSwapLabel(){
        JLabel label = new JLabel("×"+viewSuperSwap);
        Font font = new Font("雅黑", Font.BOLD, 30);
        label.setFont(font);
        label.setSize(90,30);
        label.setLocation(15+80, 120+120+120+120+80-30);
        return label;
    }
    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private JLabel addRemoveRowButton() {
        JLabel label = new JLabel(" ");//设置显示在按钮上的文字
        label.setIcon(new ImageIcon("./icons/"+skin+"/removeRowButton.png"));
        label.setToolTipText("可以消除十字路径上的棋子");//冯俊铭 提示信息
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //点击监听器 鼠标点击后执行的代码
                if (viewRemoveRow>0){
                    if (viewMode.equals("手动模式")){
                        gameController.removeRow();
                    } else if (viewMode.equals("自动模式")) {
                        gameController.removeRow();
                        gameController.setNextstep(1);
                        gameController.onPlayerNextStep();
                        gameController.onPlayerNextStep();
                    }
                    //viewRemoveRow--;放在gamecontroller里了
                    startplayRemoveRowMusic();//播放removerow的音乐
                    updateLables();
                }else{
                    JOptionPane.showMessageDialog(null,"没有该道具啦！","使用失败",JOptionPane.WARNING_MESSAGE);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                label.setIcon(new ImageIcon("./icons/"+skin+"/removeRowButton_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                label.setIcon(new ImageIcon("./icons/"+skin+"/removeRowButton.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        label.setLocation(15, 120-30);
        label.setSize(100, 100);
        label.setFont(new Font("Rockwell", Font.BOLD, 20));
        return label;
    }
    private JLabel addRemove33Button() {
        JLabel label = new JLabel(" ");//设置显示在按钮上的文字
        label.setIcon(new ImageIcon("./icons/"+skin+"/remove33.png"));
        label.setToolTipText("可以消除3*3范围内的棋子");//冯俊铭 提示信息
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (viewRemove33>0){
                    if (viewMode.equals("手动模式")){
                        gameController.remove33();
                    } else if (viewMode.equals("自动模式")) {
                        gameController.remove33();
                        gameController.setNextstep(1);
                        gameController.onPlayerNextStep();
                        gameController.onPlayerNextStep();
                    }
                    //viewRemove33--;放在gamecontroller里了
                    startplayRemove33Music();//播放remove33的音乐
                    updateLables();
                }else{
                    JOptionPane.showMessageDialog(null,"没有该道具啦！","使用失败",JOptionPane.WARNING_MESSAGE);
                }
                //点击监听器 鼠标点击后执行的代码
            }

            @Override
            public void mousePressed(MouseEvent e) {
                label.setIcon(new ImageIcon("./icons/"+skin+"/remove33_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                label.setIcon(new ImageIcon("./icons/"+skin+"/remove33.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        label.setLocation(15, 120+120-30);
        label.setSize(100, 100);
        label.setFont(new Font("Rockwell", Font.BOLD, 20));
        return label;
    }

    private JLabel addRefreshALlButton() {
        JLabel label = new JLabel(" ");//设置显示在按钮上的文字
        label.setIcon(new ImageIcon("./icons/"+skin+"/RefreshAll.png"));
        label.setToolTipText("可以清除全屏的棋子");//冯俊铭 提示信息
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (viewRefreshAll>0){
                    if (viewMode.equals("手动模式")){
                        gameController.RefreshAll();
                    } else if (viewMode.equals("自动模式")) {
                        gameController.RefreshAll();
                        gameController.onPlayerNextStep();
                    }
                    viewRefreshAll--;
                    startplayRefreshAllMusic();//播放refreshall的音乐
                    updateLables();
                }else{
                    JOptionPane.showMessageDialog(null,"没有该道具啦！","使用失败",JOptionPane.WARNING_MESSAGE);
                }
                //点击监听器 鼠标点击后执行的代码
            }

            @Override
            public void mousePressed(MouseEvent e) {
                label.setIcon(new ImageIcon("./icons/"+skin+"/RefreshAll_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                label.setIcon(new ImageIcon("./icons/"+skin+"/RefreshAll.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        label.setLocation(15, 120+120+120-30);
        label.setSize(100, 100);
        label.setFont(new Font("Rockwell", Font.BOLD, 20));
        return label;
    }

    private JLabel addSuperSwapButton() {
        JLabel label = new JLabel(" ");//设置显示在按钮上的文字
        label.setIcon(new ImageIcon("./icons/"+skin+"/SuperSwap.png"));
        label.setToolTipText("可以为您提供3步超级交换步数（无限制交换棋子）");//冯俊铭 提示信息
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (viewSuperSwap>0){
                    //点击后 将超级交换步数设置为3
                    gameController.supersteps=3;
                    JOptionPane.showMessageDialog(null,"超级交换步数已重置为3步","使用成功",JOptionPane.WARNING_MESSAGE);
                    System.out.println("进入超级交换模式");
                    viewSuperSteps.setText("当前剩余超级交换步数为："+gameController.supersteps);
                    viewSuperSwap--;
                    updateLables();
                }else{
                    JOptionPane.showMessageDialog(null,"没有该道具啦！","使用失败",JOptionPane.WARNING_MESSAGE);
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                label.setIcon(new ImageIcon("./icons/"+skin+"/SuperSwap_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                label.setIcon(new ImageIcon("./icons/"+skin+"/SuperSwap.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        label.setLocation(15, 120+120+120+120-30);
        label.setSize(100, 100);
        return label;
    }

    private JLabel addIsDeadEnd() {
        JLabel label = new JLabel(" ");//设置显示在按钮上的文字
        label.setIcon(new ImageIcon("./icons/isdeadend.png"));
        label.setToolTipText("死局检测，若死局则自动重排棋盘");//冯俊铭 提示信息
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameController.isdeadend()){
                    JOptionPane.showMessageDialog(null,"检测到当前棋盘死棋，已重新生成棋盘","重排成功",JOptionPane.WARNING_MESSAGE);
                    gameController.reArrangeAll();//执行重排方法
                }else{
                    JOptionPane.showMessageDialog(null,"当前还有可交换棋子，不能重排","使用失败",JOptionPane.WARNING_MESSAGE);
                }
                //点击监听器 鼠标点击后执行的代码
            }

            @Override
            public void mousePressed(MouseEvent e) {
                label.setIcon(new ImageIcon("./icons/isdeadend_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                label.setIcon(new ImageIcon("./icons/isdeadend.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        label.setLocation(15, 120+120+120+120+120-30);
        label.setSize(100, 100);
        label.setFont(new Font("Rockwell", Font.BOLD, 20));
        return label;
    }//冯俊铭 添加重排按钮

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
        JButton button = new JButton("确认交换");
        button.addActionListener((e) -> {
            if (viewMode.equals("手动模式")) {
                if (gameController.supersteps>0){
                    //如果还有剩余的超级交换步数 则使用超级交换方法
                    int n=gameController.superswap();
                    if(n==100){
                        startplaySuccessSwapMusic();//播放交换成功的音效
                        System.out.println("使用了超级交换");
                    } else if (n==101) {
                        startplaySuccessSwapMusic();//播放交换成功的音效
                        System.out.println("交换了未交换的棋子");
                    } else if (n==102) {
                        JOptionPane.showMessageDialog(this, "请选择两个格子进行交换", "棋盘上已经没有棋子可以消除了", JOptionPane.WARNING_MESSAGE);
                    }
                    updateLables();
                }else {
                    int swapreturnvalue = 0;
                    swapreturnvalue = chessboardComponent.swapChess();
                    if (swapreturnvalue == 101) {
                        JOptionPane.showMessageDialog(this, "这两个格子不能交换噢", "错误啦！！！", JOptionPane.WARNING_MESSAGE);
                    } else if (swapreturnvalue == 102) {
                        JOptionPane.showMessageDialog(this, "请选择两个格子进行交换", "棋盘上已经没有棋子可以消除了", JOptionPane.WARNING_MESSAGE);
                    } else {
                        startplaySuccessSwapMusic();//播放交换成功的音效
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
            updateLables();
            gameController.isnextlevel();
        });
        button.setLocation(HEIGTH, HEIGTH / 10 + 200);
        button.setSize(200, 60);
        button.setFont(new Font("雅黑", Font.BOLD, 20));
        return button;
    }

    private JButton addNextStepButton() {
        JButton button = new JButton("下一步");
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
        button.setFont(new Font("雅黑", Font.BOLD, 20));
        return button;
    }//冯俊铭

    private JButton addTipButton() {
        JButton button = new JButton("来点提示");
        button.addActionListener((e) -> {
            if (gameController.isdeadend()){
                JOptionPane.showMessageDialog(this, "点击左下角死局检测按钮进行重排", "没有可以交换的棋子啦！", JOptionPane.WARNING_MESSAGE);
            }else {
                gameController.showTipPoints();
            }

        });//fjm
        button.setLocation(HEIGTH, HEIGTH / 10 + 280+80);
        button.setSize(200, 60);
        button.setFont(new Font("雅黑", Font.BOLD, 20));
        add(button);
        return button;
    }//冯俊铭
    protected void Saving(){
        if (!gameController.isIronMode){
            //如果不是铁人游戏，则可以保存
            System.out.println("Click save");
            FileDialog fileDialog = new FileDialog(this,"选择你要保存的路径",FileDialog.SAVE);
            fileDialog.setVisible(true);
            String path = fileDialog.getDirectory()+fileDialog.getFile()+".txt";
            gameController.saveGameToFile(path);
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
        button.setFont(new Font("雅黑", Font.BOLD, 20));
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
    private JButton addBackButton(){
        JButton button = new JButton("悔棋");
        button.setLocation(HEIGTH, HEIGTH / 10 + 440+80);
        button.setSize(200, 60);
        button.setFont(new Font("雅黑", Font.BOLD, 20));
        button.addActionListener(e ->{
            gameController.isbackGame();
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
            this.viewSteps=chessboardComponent.getGameController().getsteps();//同理 -> gameController里的steps变量
            System.out.println("当前应显示步数为："+viewSteps);
            this.stepLabel.setText("当前步数:"+viewSteps);
        }
        {//更新关卡标签
            this.viewlevel=chessboardComponent.getGameController().getLevel();//同理 -> gameController里的level变量
            System.out.println("当前应显示关卡为："+viewlevel);
            this.levelLable.setText("当前关卡："+viewlevel);
        }
        {//更新超级步数标签
            this.viewSuperSteps.setText("当前剩余超级交换步数为："+gameController.supersteps);
            System.out.println("当前应显示超级步数为："+gameController.supersteps);
        }
        {//更新道具标签
            this.viewRemoveRowLabel.setText("×"+viewRemoveRow);
            this.viewRemove33Label.setText("×"+viewRemove33);
            this.viewRefreshAllLabel.setText("×"+viewRefreshAll);
            this.viewSuperSwapLabel.setText("×"+viewSuperSwap);
        }
    }
    public void registerGameController(GameController gameController){
        this.gameController = gameController;
    }//fjm 注册gameController方便调用里面的方法
    private JLabel addBackgroundLabel(){
        JLabel backgroundLabel = new JLabel(new ImageIcon("./icons/"+skin+"/chessGameFrame.png"));
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

    public String getSkin() {
        return skin;
    }//fjm

    public void setSkin(String skinName){
        if (skinName.equals("默认")||skinName.equals("default")){
            this.skin = "default";
        }else if (skinName.equals("PVZ主题")||skinName.equals("PVZ")){
            this.skin = "PVZ";
        }
        chessboardComponent.setSkin(skin);
        removeRowButton.setIcon(new ImageIcon("./icons/"+skin+"/removeRowButton.png"));
        remove33Button.setIcon(new ImageIcon("./icons/"+skin+"/remove33.png"));
        refreshAllButton.setIcon(new ImageIcon("./icons/"+skin+"/RefreshAll.png"));
        superSwapButton.setIcon(new ImageIcon("./icons/"+skin+"/SuperSwap.png"));
        background.setIcon(new ImageIcon("./icons/"+skin+"/chessGameFrame.png"));
        gameController.changeSkin();//使棋盘的棋子改变
    }//fjm 这是用来设置、更改皮肤的方法

    private void addLevelDemandLabel(){
        JLabel label = new JLabel(new ImageIcon("./icons/levelDemand.png"));
        label.setSize(40,40);
        label.setLocation(HEIGTH+130, (HEIGTH / 10)-50);
        String currentInfo = "";
        try{currentInfo = gameController.getCurrentLevelInfo();}
        catch (Exception e){
            currentInfo = "null";
        }
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                label.setToolTipText(gameController.getCurrentLevelInfo());
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        add(label);
    }//fjm 随机切换音乐的按钮
    private void addMusicLabel(){
        JLabel label = new JLabel(new ImageIcon("./icons/startFrameMusic.png"));
        label.setSize(40,40);
        label.setLocation(5+40,5);
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPlayMusic){
                    label.setIcon(new ImageIcon("./icons/startFrameMusic_close.png"));
                    stopPlayMusic();
                }else {
                    label.setIcon(new ImageIcon("./icons/startFrameMusic.png"));
                    restartPlayMusic();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isPlayMusic){
                    label.setIcon(new ImageIcon("./icons/startFrameMusic_pressed.png"));
                } else {
                    label.setIcon(new ImageIcon("./icons/startFrameMusic_close_pressed.png"));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isPlayMusic){
                    label.setIcon(new ImageIcon("./icons/startFrameMusic.png"));
                } else{
                    label.setIcon(new ImageIcon("./icons/startFrameMusic_close.png"));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        add(label);
    }//fjm 设置音乐控制按钮
    private void addMusicChangeLabel(){
        JLabel label = new JLabel(new ImageIcon("./icons/startFrameChangeMusic.png"));
        label.setSize(40,40);
        label.setLocation(5+40+40,5);
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPlayMusic){
                    changeMusic();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                    label.setIcon(new ImageIcon("./icons/startFrameChangeMusic_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                    label.setIcon(new ImageIcon("./icons/startFrameChangeMusic.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        add(label);
    }//fjm 随机切换音乐的按钮
    static boolean isPlayMusic = true;
    private static int musicNum = 0;
    static void playMusic() {// 背景音乐播放
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("./music/mainFrame"+musicNum+".wav"));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            int writeByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {// 判断 播放/暂停 状态

                if(isPlayMusic) {

                    nByte = ais.read(buffer, 0, SIZE);

                    sdl.write(buffer, 0, nByte);
                }else {

                    nByte = ais.read(buffer, 0, 0);

                }

            }
            sdl.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//背景音乐播放的方法

    static void playSuccessSwapMusic() {// 交换成功音乐播放
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("./music/successSwapChess.wav"));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            int writeByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {// 判断 播放/暂停 状态

                if(isPlayMusic) {

                    nByte = ais.read(buffer, 0, SIZE);

                    sdl.write(buffer, 0, nByte);
                }else {

                    nByte = ais.read(buffer, 0, 0);

                }

            }
            sdl.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//fjm 交换成功音乐播放的方法

    public void startplaySuccessSwapMusic(){
        Thread swapSuccessfulmusicPlayer = new Thread(()->{
            playSuccessSwapMusic();
        });
        swapSuccessfulmusicPlayer.start();
    }// Lambda表达式 fjm 开始播放交换成功的音乐

    // 成功进入下一关音乐播放
    static void playNextLevelMusic() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("./music/nextLevel.wav"));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            int writeByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {// 判断 播放/暂停 状态

                if(isPlayMusic) {

                    nByte = ais.read(buffer, 0, SIZE);

                    sdl.write(buffer, 0, nByte);
                }else {

                    nByte = ais.read(buffer, 0, 0);

                }

            }
            sdl.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//fjm 成功下一关音乐播放的方法

    public void startplayNextLevelMusic(){
        Thread musicPlayer = new Thread(()->{
            playNextLevelMusic();
        });
        musicPlayer.start();
    }// Lambda表达式 fjm 开始播放成功下一关的音乐


    // 失败音乐播放
    static void playFailedMusic() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("./music/failed.wav"));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            int writeByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {// 判断 播放/暂停 状态

                if(isPlayMusic) {

                    nByte = ais.read(buffer, 0, SIZE);

                    sdl.write(buffer, 0, nByte);
                }else {

                    nByte = ais.read(buffer, 0, 0);

                }

            }
            sdl.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//fjm 失败成功音乐播放的方法

    public void startplayFailedMusic(){
        Thread musicPlayer = new Thread(()->{
            playFailedMusic();
        });
        musicPlayer.start();
    }// Lambda表达式 fjm 开始播放失败的音乐

    //removerow音效
    static void playRemoveRowMusic() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("./music/removeRow.wav"));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            int writeByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {// 判断 播放/暂停 状态

                if(isPlayMusic) {

                    nByte = ais.read(buffer, 0, SIZE);

                    sdl.write(buffer, 0, nByte);
                }else {

                    nByte = ais.read(buffer, 0, 0);

                }

            }
            sdl.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//fjm removerow音乐播放的方法

    public void startplayRemoveRowMusic(){
        Thread musicPlayer = new Thread(()->{
            playRemoveRowMusic();
        });
        musicPlayer.start();
    }// Lambda表达式 fjm 开始removerow的音乐


    //remove33音效
    static void playRemove33Music() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("./music/remove33.wav"));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            int writeByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {// 判断 播放/暂停 状态

                if(isPlayMusic) {

                    nByte = ais.read(buffer, 0, SIZE);

                    sdl.write(buffer, 0, nByte);
                }else {

                    nByte = ais.read(buffer, 0, 0);

                }

            }
            sdl.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//fjm remove33音乐播放的方法

    public void startplayRemove33Music(){
        Thread musicPlayer = new Thread(()->{
            playRemove33Music();
        });
        musicPlayer.start();
    }// Lambda表达式 fjm 开始播放remove33的音乐

    //refreshAll音效
    static void playRefreshAllMusic() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("./music/refreshAll.wav"));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            int writeByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {// 判断 播放/暂停 状态

                if(isPlayMusic) {

                    nByte = ais.read(buffer, 0, SIZE);

                    sdl.write(buffer, 0, nByte);
                }else {

                    nByte = ais.read(buffer, 0, 0);

                }

            }
            sdl.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//fjm remove33音乐播放的方法

    public void startplayRefreshAllMusic(){
        Thread musicPlayer = new Thread(()->{
            playRefreshAllMusic();
        });
        musicPlayer.start();
    }// Lambda表达式 fjm 开始播放remove33的音乐

    public void startPlayMusic(){//这个是第一次开始播放时使用的方法
        musicPlayer.start();
    }

    public void stopPlayMusic(){
        isPlayMusic=false;
    }
    public void restartPlayMusic(){//这个是音乐线程启动后才使用的方法
        isPlayMusic=true;
    }
    public void changeMusic(){

        while (true){
            int newmusicNum = (int)((Math.random()/2)*10);//math.random是返回[0,1.0]之间的小数，/2就是[0,0.4],再*10就是[0-4]
            if (newmusicNum!=musicNum){
                musicNum=newmusicNum;
                musicPlayer.stop();
                musicPlayer=null;
                musicPlayer=new Thread(()->{
                    while(true) {playMusic();}
                });// Lambda表达式 fjm播放音乐用的
                musicPlayer.start();
                break;
            }
        }
        JOptionPane.showMessageDialog(this, "当前音乐编号为：" + musicNum, "背景音乐切换成功", JOptionPane.WARNING_MESSAGE);
        System.out.println("当前新生成的音乐编号为："+musicNum);
    }//fjm 更改正在播放的音乐
    public void registerSettingFrame(SettingFrame settingFrame){
        this.settingFrame = settingFrame;
    }//冯俊铭 给主窗口绑定设置窗口
    public int getViewRemove33() {
        return viewRemove33;
    }

    public void setViewRemove33(int viewRemove33) {
        this.viewRemove33 = viewRemove33;
    }

    public int getViewRemoveRow() {
        return viewRemoveRow;
    }

    public void setViewRemoveRow(int viewRemoveRow) {
        this.viewRemoveRow = viewRemoveRow;
    }

    public int getViewRefreshAll() {
        return viewRefreshAll;
    }

    public void setViewRefreshAll(int viewRefreshAll) {
        this.viewRefreshAll = viewRefreshAll;
    }

    public int getViewSuperSwap() {
        return viewSuperSwap;
    }

    public void setViewSuperSwap(int viewSuperSwap) {
        this.viewSuperSwap = viewSuperSwap;
    }

    public SettingFrame getSettingFrame() {
        return settingFrame;
    }

    public int getViewlevel() {
        return viewlevel;
    }

    public void setViewlevel(int viewlevel) {
        this.viewlevel = viewlevel;
    }
}
