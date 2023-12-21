import controller.GameController;
import model.Cell;
import model.Chessboard;
import view.ChessGameFrame;

import javax.swing.*;

import controller.GameController;
import model.Chessboard;
import view.ChessGameFrame;

import javax.swing.*;
import java.util.Arrays;

public class Test {
    public static void main(String[] args){
        ChessGameFrame mainFrame = new ChessGameFrame(1100, 810);
        GameController gameController = new GameController(mainFrame.getChessboardComponent(), new Chessboard(),mainFrame);
        //mainFrame.setGameController(gameController);
        //gameController.setStatusLabel(mainFrame.getStatusLabel());
        gameController.loadGameFromFile("Lekge.txt");
        String test = "当前关卡是:1";
        String[] tests = test.split(":");
        System.out.println(Arrays.toString(tests));

    }
}
