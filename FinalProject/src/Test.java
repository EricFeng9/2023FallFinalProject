import controller.GameController;
import model.Cell;
import model.Chessboard;
import view.ChessGameFrame;

import javax.swing.*;

import controller.GameController;
import model.Chessboard;
import view.ChessGameFrame;

import javax.swing.*;

public class Test {
    public static void main(String[] args){
        ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
        GameController gameController = new GameController(mainFrame.getChessboardComponent(), new Chessboard());
        mainFrame.setGameController(gameController);
        gameController.setStatusLabel(mainFrame.getStatusLabel());
        gameController.loadGameFromFile("game.txt");


    }
}
