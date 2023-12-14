package model;

import java.awt.*;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipFile;

/**
 * This class store the real chess information.
 * The Chessboard has 8 * 8 cells, and each cell has a position for chess
 */

public class Chessboard {
    private Cell[][] grid;
    boolean[][] grid1;//江易明

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
                //     grid1[i + 2][j + 2] = grid[i][j];
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
    private void initPieces(int randomSeed) {
        Random random = new Random(randomSeed);

        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j].setPiece(new ChessPiece(Util.RandomPick(new String[]{"💎", "⚪", "▲", "🔶"})));
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
    }//计算棋子是否能进行交换，两个棋子的距离应该小于等于1.

    public ChessPiece removeChessPiece(ChessboardPoint point) {
        ChessPiece chessPiece = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chessPiece;
    }//棋子移除，返回空格子。

    private void setChessPiece(ChessboardPoint point, ChessPiece chessPiece) {
        getGridAt(point).setPiece(chessPiece);
    }


    public void swapChessPiece(ChessboardPoint point1, ChessboardPoint point2) {
        var p1 = getChessPieceAt(point1);
        var p2 = getChessPieceAt(point2);
        setChessPiece(point1, p2);
        setChessPiece(point2, p1);
    }//交换棋子，加判断方法，能否交换。

    public boolean canSwap(ChessboardPoint point1, ChessboardPoint point2) {
        boolean result=false;
        if (calculateDistance(point1, point2) > 1) {
            return false;
        }
        else {
            Color a1 = getChessPieceAt(point1).getColor();
            Color b1 = getChessPieceAt(point2).getColor();
            int row1 = point1.getRow();
            int col1 = point1.getCol();
            int row2 = point2.getRow();
            int col2 = point2.getCol();
            //两个交换任一一个满足则true都不满足则false
            //十字架判断，上下左右任一方向满足三个即可消除。
            int counta1 = 0;
            int counta2 = 0;
            int counta3 = 0;
            int counta4 = 0;
            int countb1 = 0;
            int countb2 = 0;
            int countb3 = 0;
            int countb4 = 0;//设置各个方向的count，总共8个若任一一个count达2说明有三个颜色相同的元素

            if((col1>=1&&col1<=6)&&(row1>=1&&row1<=6)){
                if (b1.equals(grid[row1][col1+1].getPiece().getName())&&b1.equals(grid[row1][col1-1].getPiece().getName())){
                    result= true;
                }//向左右寻找
                if (b1.equals(grid[row1-1][col1].getPiece().getName())&&b1.equals(grid[row1+1][col1].getPiece().getName())){
                    result= true;
                }//向上下寻找
            }//对一个交换后的棋子而言

            if((col2>=1&&col2<=6)&&(row2>=1&&row2<=6)){
                if (a1.equals(grid[row2][col2+1].getPiece().getName())&&a1.equals(grid[row2][col2-1].getPiece().getName())){
                    result= true;
                }//向左右寻找
                if (a1.equals(grid[row2-1][col2].getPiece().getName())&&a1.equals(grid[row2+1][col2].getPiece().getName())){
                    result= true;
                }//向上下寻找
            }//对另一个交换后棋子而言

            if ((col1==0||col1==7)&&(row1>=1&&row1<=6)){
                if (b1.equals(grid[row1-1][col1].getPiece().getName())&&b1.equals(grid[row1+1][col1].getPiece().getName())){
                    result= true;
                }//向上下寻找
            }
            if ((col2==0||col2==7)&&(row2>=1&&row2<=6)){
                if (a1.equals(grid[row2-1][col2].getPiece().getName())&&a1.equals(grid[row2+1][col2].getPiece().getName())){
                    result= true;
                }//向上下寻找
            }
            if ((row1==0||row1==7)&&(col1>=1&&col1<=6)){
                if (b1.equals(grid[row1][col1+1].getPiece().getName())&&b1.equals(grid[row1][col1-1].getPiece().getName())){
                    result= true;
                }//向左右寻找
            }
            if ((row2==0||row2==7)&&(col2>=1&&col2<=6)){
                if (a1.equals(grid[row2][col2+1].getPiece().getName())&&a1.equals(grid[row2][col2-1].getPiece().getName())){
                    result= true;
                }//向左右寻找
            }
            for (int i = 1; i <= 2; i++) {
                if (row1 >= 2) {
                    if (grid[row1 - i][col1].getPiece().getColor() == b1) {
                        ++countb1;
                    }
                }
                if (col1 >= 2) {
                    if (grid[row1][col1 - i].getPiece().getColor() == b1) {
                        ++countb2;
                    }
                }
                if (row1 <= 5) {
                    if (grid[row1 + i][col1].getPiece().getColor() == b1) {
                        ++countb3;
                    }
                }
                if (col1 <= 5) {
                    if (grid[row1][col1 + i].getPiece().getColor() == b1) {
                        ++countb4;
                    }
                }


                if (row2 >= 2) {
                    if (grid[row2 - i][col2].getPiece().getColor() == a1) {
                        ++counta1;
                    }
                }
                if (col2 >= 2) {
                    if (grid[row2][col2 - i].getPiece().getColor() == a1) {
                        ++counta2;
                    }
                }
                if (row2 <= 5) {
                    if (grid[row2 + i][col2].getPiece().getColor() == a1) {
                        ++counta3;
                    }
                }
                if (col2 <= 5) {
                    if (grid[row2][col2 + i].getPiece().getColor() == a1) {
                        ++counta4;
                    }
                }
            }
            if (counta1 == 2 || counta2 == 2 || counta3 == 2 || counta4 == 2 || countb1 == 2 || countb2 == 2 || countb3 == 2 || countb4 == 2) {
                result= true;
            } else {
                result= false;
            }
        }
        return result;
    }//江易明 2023.12.11

    public boolean[][] candelete() {
        boolean[][] grid1 = new boolean[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];
        int count = 0;
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {

                if ((2 <= i && i <= 5) && (2 <= j && j <= 5)) {//分五步来写
                    String a = grid[i][j].getPiece().getName();
                    if (a.equals(grid[i][j + 1].getPiece().getName()) && a.equals(grid[i][j + 2].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i][j + 1] = true;
                        grid1[i][j + 2] = true;
                        if (Constant.CHESSBOARD_COL_SIZE.getNum() > j + 3) {
                            for (int k = j + 3; k < Constant.CHESSBOARD_COL_SIZE.getNum(); k++) {
                                if (a.equals(grid[i][k].getPiece().getName())) {
                                    ++count;
                                    grid1[i][k] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向右寻找是否有三个一样的name；

                    if (a.equals(grid[i][j - 1].getPiece().getName()) && a.equals(grid[i][j - 2].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i][j - 1] = true;
                        grid1[i][j - 2] = true;
                        if (j - 3 >= 0) {
                            for (int k = j - 3; k >= 0; k--) {
                                if (a.equals(grid[i][k].getPiece().getName())) {
                                    ++count;
                                    grid1[i][k] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向左寻找是否有三个一样的name

                    if (a.equals(grid[i - 1][j].getPiece().getName()) && a.equals(grid[i - 2][j].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i - 1][j] = true;
                        grid1[i - 2][j] = true;
                        if (i - 3 >= 0) {
                            for (int k = i - 3; k >= 0; k--) {
                                if (a.equals(grid[k][j].getPiece().getName())) {
                                    ++count;
                                    grid1[k][j] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向上寻找是否有三个一样的name

                    if (a.equals(grid[i + 1][j].getPiece().getName()) && a.equals(grid[i + 2][j].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i + 1][j] = true;
                        grid1[i + 2][j] = true;
                        if (i + 3 < Constant.CHESSBOARD_ROW_SIZE.getNum()) {
                            for (int k = i + 3; k < Constant.CHESSBOARD_ROW_SIZE.getNum(); k++) {
                                if (a.equals(grid[k][j].getPiece().getName())) {
                                    ++count;
                                    grid1[k][j] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向下寻找是否有三个一样的name
                }
                //1
                if ((i <= 2 && j < 2) || (i < 2 && j <= 2)) {
                    String a = grid[i][j].getPiece().getName();
                    if (a.equals(grid[i][j + 1].getPiece().getName()) && a.equals(grid[i][j + 2].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i][j + 1] = true;
                        grid1[i][j + 2] = true;
                        if (Constant.CHESSBOARD_COL_SIZE.getNum() > j + 3) {
                            for (int k = j + 3; k < Constant.CHESSBOARD_COL_SIZE.getNum(); k++) {
                                if (a.equals(grid[i][k].getPiece().getName())) {
                                    ++count;
                                    grid1[i][k] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向右寻找是否有三个一样的name；

                    if (a.equals(grid[i + 1][j].getPiece().getName()) && a.equals(grid[i + 2][j].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i + 1][j] = true;
                        grid1[i + 2][j] = true;
                        if (i + 3 < Constant.CHESSBOARD_ROW_SIZE.getNum()) {
                            for (int k = i + 3; k < Constant.CHESSBOARD_ROW_SIZE.getNum(); k++) {
                                if (a.equals(grid[k][j].getPiece().getName())) {
                                    ++count;
                                    grid1[k][j] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向下寻找是否有三个一样的name
                }
//2
                if ((i <= 2 && j > 5) || (i < 2 && j >= 5)) {
                    String a = grid[i][j].getPiece().getName();
                    if (a.equals(grid[i][j - 1].getPiece().getName()) && a.equals(grid[i][j - 2].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i][j - 1] = true;
                        grid1[i][j - 2] = true;
                        if (j - 3 >= 0) {
                            for (int k = j - 3; k >= 0; k--) {
                                if (a.equals(grid[i][k].getPiece().getName())) {
                                    ++count;
                                    grid1[i][k] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向左寻找是否有三个一样的name

                    if (a.equals(grid[i + 1][j].getPiece().getName()) && a.equals(grid[i + 2][j].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i + 1][j] = true;
                        grid1[i + 2][j] = true;
                        if (i + 3 < Constant.CHESSBOARD_ROW_SIZE.getNum()) {
                            for (int k = i + 3; k < Constant.CHESSBOARD_ROW_SIZE.getNum(); k++) {
                                if (a.equals(grid[k][j].getPiece().getName())) {
                                    ++count;
                                    grid1[k][j] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向下寻找是否有三个一样的name
                }
                //3
                if ((i >= 5 && j < 2) || (i > 5 && j <= 2)) {
                    String a = grid[i][j].getPiece().getName();
                    if (a.equals(grid[i][j + 1].getPiece().getName()) && a.equals(grid[i][j + 2].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i][j + 1] = true;
                        grid1[i][j + 2] = true;
                        if (Constant.CHESSBOARD_COL_SIZE.getNum() > j + 3) {
                            for (int k = j + 3; k < Constant.CHESSBOARD_COL_SIZE.getNum(); k++) {
                                if (a.equals(grid[i][k].getPiece().getName())) {
                                    ++count;
                                    grid1[i][k] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向右寻找是否有三个一样的name；

                    if (a.equals(grid[i - 1][j].getPiece().getName()) && a.equals(grid[i - 2][j].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i - 1][j] = true;
                        grid1[i - 2][j] = true;
                        if (i - 3 >= 0) {
                            for (int k = i - 3; k >= 0; k--) {
                                if (a.equals(grid[k][j].getPiece().getName())) {
                                    ++count;
                                    grid1[k][j] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向上寻找是否有三个一样的name
                }
//4
                if ((i >= 5 && j > 5) || (i > 5 && j >= 5)) {
                    String a = grid[i][j].getPiece().getName();
                    if (a.equals(grid[i][j - 1].getPiece().getName()) && a.equals(grid[i][j - 2].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i][j - 1] = true;
                        grid1[i][j - 2] = true;
                        if (j - 3 >= 0) {
                            for (int k = j - 3; k >= 0; k--) {
                                if (a.equals(grid[i][k].getPiece().getName())) {
                                    ++count;
                                    grid1[i][k] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向左寻找是否有三个一样的name

                    if (a.equals(grid[i - 1][j].getPiece().getName()) && a.equals(grid[i - 2][j].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i - 1][j] = true;
                        grid1[i - 2][j] = true;
                        if (i - 3 >= 0) {
                            for (int k = i - 3; k >= 0; k--) {
                                if (a.equals(grid[k][j].getPiece().getName())) {
                                    ++count;
                                    grid1[k][j] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向上寻找是否有三个一样的name
                }

                //2.1
                if ((i >= 0 && i <= 1) && (j >= 3 && j <= 4)) {
                    String a = grid[i][j].getPiece().getName();
                    if (a.equals(grid[i][j - 1].getPiece().getName()) && a.equals(grid[i][j - 2].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i][j - 1] = true;
                        grid1[i][j - 2] = true;
                        if (j - 3 >= 0) {
                            for (int k = j - 3; k >= 0; k--) {
                                if (a.equals(grid[i][k].getPiece().getName())) {
                                    ++count;
                                    grid1[i][k] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向左寻找是否有三个一样的name
                    if (a.equals(grid[i][j + 1].getPiece().getName()) && a.equals(grid[i][j + 2].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i][j + 1] = true;
                        grid1[i][j + 2] = true;
                        if (Constant.CHESSBOARD_COL_SIZE.getNum() > j + 3) {
                            for (int k = j + 3; k < Constant.CHESSBOARD_COL_SIZE.getNum(); k++) {
                                if (a.equals(grid[i][k].getPiece().getName())) {
                                    ++count;
                                    grid1[i][k] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向右寻找是否有三个一样的name；
                    if (a.equals(grid[i + 1][j].getPiece().getName()) && a.equals(grid[i + 2][j].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i + 1][j] = true;
                        grid1[i + 2][j] = true;
                        if (i + 3 < Constant.CHESSBOARD_ROW_SIZE.getNum()) {
                            for (int k = i + 3; k < Constant.CHESSBOARD_ROW_SIZE.getNum(); k++) {
                                if (a.equals(grid[k][j].getPiece().getName())) {
                                    ++count;
                                    grid1[k][j] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向下寻找是否有三个一样的name
                }
                //2.2
                if ((i >= 6 && i <= 7) && (j >= 3 && j <= 4)) {
                    String a = grid[i][j].getPiece().getName();
                    if (a.equals(grid[i][j + 1].getPiece().getName()) && a.equals(grid[i][j + 2].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i][j + 1] = true;
                        grid1[i][j + 2] = true;
                        if (Constant.CHESSBOARD_COL_SIZE.getNum() > j + 3) {
                            for (int k = j + 3; k < Constant.CHESSBOARD_COL_SIZE.getNum(); k++) {
                                if (a.equals(grid[i][k].getPiece().getName())) {
                                    ++count;
                                    grid1[i][k] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向右寻找是否有三个一样的name；
                    if (a.equals(grid[i][j - 1].getPiece().getName()) && a.equals(grid[i][j - 2].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i][j - 1] = true;
                        grid1[i][j - 2] = true;
                        if (j - 3 >= 0) {
                            for (int k = j - 3; k >= 0; k--) {
                                if (a.equals(grid[i][k].getPiece().getName())) {
                                    ++count;
                                    grid1[i][k] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向左寻找是否有三个一样的name
                    if (a.equals(grid[i - 1][j].getPiece().getName()) && a.equals(grid[i - 2][j].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i - 1][j] = true;
                        grid1[i - 2][j] = true;
                        if (i - 3 >= 0) {
                            for (int k = i - 3; k >= 0; k--) {
                                if (a.equals(grid[k][j].getPiece().getName())) {
                                    ++count;
                                    grid1[k][j] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向上寻找是否有三个一样的name
                }
//2.3
                if ((3 <= i && i <= 4) && (0 <= j && j <= 1)) {
                    String a = grid[i][j].getPiece().getName();
                    if (a.equals(grid[i - 1][j].getPiece().getName()) && a.equals(grid[i - 2][j].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i - 1][j] = true;
                        grid1[i - 2][j] = true;
                        if (i - 3 >= 0) {
                            for (int k = i - 3; k >= 0; k--) {
                                if (a.equals(grid[k][j].getPiece().getName())) {
                                    ++count;
                                    grid1[k][j] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向上寻找是否有三个一样的name
                    if (a.equals(grid[i][j + 1].getPiece().getName()) && a.equals(grid[i][j + 2].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i][j + 1] = true;
                        grid1[i][j + 2] = true;
                        if (Constant.CHESSBOARD_COL_SIZE.getNum() > j + 3) {
                            for (int k = j + 3; k < Constant.CHESSBOARD_COL_SIZE.getNum(); k++) {
                                if (a.equals(grid[i][k].getPiece().getName())) {
                                    ++count;
                                    grid1[i][k] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向右寻找是否有三个一样的name；
                    if (a.equals(grid[i + 1][j].getPiece().getName()) && a.equals(grid[i + 2][j].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i + 1][j] = true;
                        grid1[i + 2][j] = true;
                        if (i + 3 < Constant.CHESSBOARD_ROW_SIZE.getNum()) {
                            for (int k = i + 3; k < Constant.CHESSBOARD_ROW_SIZE.getNum(); k++) {
                                if (a.equals(grid[k][j].getPiece().getName())) {
                                    ++count;
                                    grid1[k][j] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向下寻找是否有三个一样的name
                }
                //2.4
                if ((3 <= i && i <= 4) && (6 <= j && j <= 7)) {
                    String a = grid[i][j].getPiece().getName();
                    if (a.equals(grid[i - 1][j].getPiece().getName()) && a.equals(grid[i - 2][j].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i - 1][j] = true;
                        grid1[i - 2][j] = true;
                        if (i - 3 >= 0) {
                            for (int k = i - 3; k >= 0; k--) {
                                if (a.equals(grid[k][j].getPiece().getName())) {
                                    ++count;
                                    grid1[k][j] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向上寻找是否有三个一样的name
                    if (a.equals(grid[i + 1][j].getPiece().getName()) && a.equals(grid[i + 2][j].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i + 1][j] = true;
                        grid1[i + 2][j] = true;
                        if (i + 3 < Constant.CHESSBOARD_ROW_SIZE.getNum()) {
                            for (int k = i + 3; k < Constant.CHESSBOARD_ROW_SIZE.getNum(); k++) {
                                if (a.equals(grid[k][j].getPiece().getName())) {
                                    ++count;
                                    grid1[k][j] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向下寻找是否有三个一样的name
                    if (a.equals(grid[i][j - 1].getPiece().getName()) && a.equals(grid[i][j - 2].getPiece().getName())) {
                        count = 3;
                        grid1[i][j] = true;
                        grid1[i][j - 1] = true;
                        grid1[i][j - 2] = true;
                        if (j - 3 >= 0) {
                            for (int k = j - 3; k >= 0; k--) {
                                if (a.equals(grid[i][k].getPiece().getName())) {
                                    ++count;
                                    grid1[i][k] = true;
                                } else {
                                    break;
                                }
                            }
                        }
                    }//向左寻找是否有三个一样的name
                }
            }
        }
        return grid1;
    }//江易明
    //TODO判断一左一右


    public Cell[][] getGrid2() {
        return getGrid2();
    }//jym 2023.12.11 返回boolean标记可以消除的数组！需要自己删除！

    //2023.12.12 jym 判断能否掉落并掉落？
    public void candrap() {
        for (int i = Constant.CHESSBOARD_ROW_SIZE.getNum() - 1; i > 0; i--) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                if (grid[i][j].getPiece() == null) {
                    for (int l = i-1; l >=0 ; l--) {
                        if (grid[l][j].getPiece()!=null){
                            ChessboardPoint point2=new ChessboardPoint(l,j);
                            ChessboardPoint point1=new ChessboardPoint(i,j);
                            swapChessPiece(point1, point2);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void setnewgrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                if (grid[i][j].getPiece() == null) {
                    Cell cell = new Cell();
                    grid[i][j] = cell;
                    grid[i][j].setPiece(new ChessPiece(Util.RandomPick(new String[]{"💎", "⚪", "▲", "🔶"})));
                }
            }
        }
    }//生成新的棋子，形成新的棋盘

    public boolean ismatch() {
        boolean grid2[][]=this.candelete();
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                if (grid2[i][j]){
                    return true;//如果反馈有一个能消，说明棋盘还能消，返回true
                }
            }
        }
        return false;
    }//江易明 冯俊铭 判断生成的棋盘是否有match！

    private boolean[][] getgrid1() {
        return grid1;
    }


    public Cell[][] getGrid() {
        return grid;
    }
    //返回棋盘，保存？回档
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
    public boolean readListToBoard(List<String> readLines){

        //先保存一遍原来的棋盘
        Cell[][] originGrid=new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++){
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++){
                Cell origingrid = new Cell();
                originGrid[i][j] = origingrid;
                ChessPiece piece =grid[i][j].getPiece();
                originGrid[i][j].setPiece(piece);
            }
        }

        if (readLines.size()==Constant.CHESSBOARD_ROW_SIZE.getNum()){
            //读入的readLines的大小必须与棋盘行数匹配（readLines里一个位置存着棋盘里一行的数据）才能进行下一步逐行读入
            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++){
                sb.setLength(0);//遍历每一行前先清空StringBuilder
                //readLines数组中，每一个位置存储着一行棋盘的数据
                String[] readline = readLines.get(i).split(","); //遍历每行时，先将其提取并分割出来

                if (readline.length==Constant.CHESSBOARD_COL_SIZE.getNum()){
                    //同样，分割后readline里的长度如果与当前棋盘列数匹配才能读入
                    for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++){
                        ChessPiece piece = new ChessPiece(readline[j]);
                        grid[i][j].setPiece(piece);//将棋盘中对应位置的piece放进去
                    }
                }else {
                    //如果发现不匹配，则将棋盘改回原来的样子
                    for (int k = 0; k < Constant.CHESSBOARD_ROW_SIZE.getNum(); k++){
                        for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++){
                            ChessPiece piece =originGrid[k][j].getPiece();
                            grid[k][j].setPiece(piece);
                        }
                    }return false;
                }
            }
            sb.setLength(0);//操作完成，若sb不为空，将sb清零
            return true;
        }else {
            //如果发现不匹配，则将棋盘改回原来的样子
            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++){
                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++){
                    ChessPiece piece =originGrid[i][j].getPiece();
                    grid[i][j].setPiece(piece);
                }
            }return false;
        }


    }//冯俊铭 23/12/11 17：57

}

