package model;

import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class store the real chess information.
 * The Chessboard has 8 * 8 cells, and each cell has a position for chess
 */
public class Chessboard {
    private Cell[][] grid;
    private StringBuilder sb;//创建一个StringBuilder供使用 冯俊铭23/12/10/21：40
    public Chessboard() {
        this.grid =
                new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];

        initGrid();
        initPieces();
        sb = new StringBuilder();//实例化StringBuilder 冯俊铭23/12/10/21：40
    }
    public Chessboard(int randomSeed) {
        this.grid =
                new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];

        initGrid();
        initPieces(randomSeed);
        sb = new StringBuilder();//实例化StringBuilder 冯俊铭23/12/10/21：40
    }

    private void initGrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    private void initPieces(int randomSeed) {
        Random random = new Random(randomSeed);

        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j].setPiece(new ChessPiece( Util.RandomPick(new String[]{"💎", "⚪", "▲", "🔶"})));
            }
        }

    }
    private void initPieces() {

        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j].setPiece(new ChessPiece( Util.RandomPick(new String[]{"💎", "⚪", "▲", "🔶"})));
            }
        }

    }

    private ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }

    private Cell getGridAt(ChessboardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }

    private int calculateDistance(ChessboardPoint src, ChessboardPoint dest) {
        return Math.abs(src.getRow() - dest.getRow()) + Math.abs(src.getCol() - dest.getCol());
    }// 检查是否棋子位置是否相邻 return值<=1则相邻

    private ChessPiece removeChessPiece(ChessboardPoint point) {
        ChessPiece chessPiece = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chessPiece;
    }// 移除棋子的方法

    private void setChessPiece(ChessboardPoint point, ChessPiece chessPiece) {
        getGridAt(point).setPiece(chessPiece);
    }
    //放置棋子的方法

    public void swapChessPiece(ChessboardPoint point1, ChessboardPoint point2) {
        // TODO: 2023/12/8 还要添加一个check方法 来判断交换后有没有三个棋子连在一起 
        var p1 = getChessPieceAt(point1);
        var p2 = getChessPieceAt(point2);
        setChessPiece(point1, p2);
        setChessPiece(point2, p1);
    }


    public Cell[][] getGrid() {
        return grid;
    }
    public List<String> convertBoardToList(){
        List<String> saveLines = new ArrayList<>();
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                sb.setLength(0);//遍历每一行前先清空StringBuilder
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessPiece piece = grid[i][j].getPiece();
                if (piece!= null){
                    sb.append(piece.getName()).append(",");
                }else sb.append("0,");
            }
            saveLines.add(sb.toString());//每行遍历完后，将StringBuilder中的内容加到saveLine数组中（一个位置代表棋盘一行）
        }
            sb.setLength(0);//操作完成，若sb不为空，将sb清零
        return saveLines;//返回保存好的saveLine
    }//冯俊铭 23/12/10/21：55
    public void readListToBoard(List<String> readLines){
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++){
            sb.setLength(0);//遍历每一行前先清空StringBuilder
            //readLines数组中，每一个位置存储着一行棋盘的数据
            String[] readline = readLines.get(i).split(","); //遍历每行时，先将其提取并分割出来
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++){
                ChessPiece piece = new ChessPiece(readline[j]);
                grid[i][j].setPiece(piece);//将棋盘中对应位置的piece放进去
            }
        }
        sb.setLength(0);//操作完成，若sb不为空，将sb清零
    }//冯俊铭 23/12/23：16

}
