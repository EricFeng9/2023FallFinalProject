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

    Chessboard chessboard;



    protected void startNewGameFrame(ChessGameFrame mainFrame,GameController gameController){
        setTitle("创建一个新存档");
        setLayout(null);
        this.mainFrame=mainFrame;
        addStartButton();
        setSize(200,100);
        this.gameController = gameController;
        this.chessboard = gameController.getModel();
    }
    private void addStartButton(){
        JButton startButton = new JButton("开始游戏！");
        Font font = new Font("雅黑",Font.PLAIN,10);
        startButton.setFont(font);
        startButton.setSize(100,30);
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
            mainFrame.setGameController(gameController);
            mainFrame.setVisible(true);
            this.setVisible(false);
        });
        this.add(startButton);
    }

}// TODO: 2023/12/13 冯俊铭 创建新存档、选择关卡

