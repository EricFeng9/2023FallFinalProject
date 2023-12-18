import controller.GameController;
import model.Chessboard;
import view.ChessGameFrame;

import javax.swing.*;

import view.StartFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
            GameController gameController = new GameController(mainFrame.getChessboardComponent(), new Chessboard());
            StartFrame startFrame = new StartFrame(300,600,gameController,mainFrame);
            mainFrame.setGameController(gameController);
            //gameController.setStatusLabel(mainFrame.getStatusLabel());
            startFrame.setVisible(true);
            //mainFrame.setVisible(true);//把主界面放在开始界面中打开
        });
    }
}