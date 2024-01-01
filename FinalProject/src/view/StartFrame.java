package view;

import controller.GameController;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class StartFrame extends JFrame {
    private final int WIDTH;
    private final int HEIGHT;
    GameController gameController;
    ChessGameFrame mainFrame;
    JTextPane notiecLable;
    JLabel background;
    Thread musicPlayer = new Thread(()->{
        while(true) {playMusic();}
    });// Lambda表达式

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
        addMusicLabel();//fjm 添加音乐控件
        add(background);
        /*startFrameMusic.start();
        @SuppressWarnings("unused")
        int musicOpenLab = 1;*/
        musicPlayer.start();
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
        textArea.setText("这是南方科技大学2023秋JavaA期末project\n               开发者：冯俊铭、江易明\n                      测试版，请勿外传\n                       当前exe版本:1.2");
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
            mainFrame.Load();

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
            isPlayMusic=false;
        });
    }//冯俊铭 23/12/11/17:56
    private void addMusicLabel(){
        JLabel label = new JLabel(new ImageIcon("./icons/startFrameMusic.png"));
        label.setSize(40,40);
        label.setLocation(40,40);
        label.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPlayMusic){
                    label.setIcon(new ImageIcon("./icons/startFrameMusic_close.png"));
                    isPlayMusic=false;
                }else {
                    label.setIcon(new ImageIcon("./icons/startFrameMusic.png"));
                    isPlayMusic=true;
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
                } else {
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
    static boolean isPlayMusic = true;
    static void playMusic() {// 背景音乐播放

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("./music/StartFrame.wav"));
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
    }//fjm 背景音乐播放的方法
    public void stopPlayMusic(){
        isPlayMusic=false;
    }//fjm
    public void restartPlayMusic(){//这个是音乐线程启动后才使用的方法
        isPlayMusic=true;
    }//fjm
    private JLabel addBackgroundLabel(){
        JLabel backgroundLabel = new JLabel(new ImageIcon("./icons/startFrame.png"));
        backgroundLabel.setSize(900,600);
        backgroundLabel.setLocation(0,0);
        return backgroundLabel;
    }//fjm 设置背景图片
}

