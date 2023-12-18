package controller;

import listener.GameListener;
import model.Cell;
import model.Constant;
import model.Chessboard;
import model.ChessboardPoint;
import view.CellComponent;
import view.ChessComponent;
import view.ChessboardComponent;
import view.LoadErrorDialog;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and
 * onPlayerClickChessPiece()]
 *
 */
public class GameController implements GameListener {
    public Chessboard getModel() {
        return model;
    }

    public void setModel(Chessboard model) {
        this.model = model;
    }

    public ChessboardComponent getView() {
        return view;
    }

    public void setView(ChessboardComponent view) {
        this.view = view;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getFootsteps() {
        return steps;
    }

    private Chessboard model;
    private ChessboardComponent view;

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
    private ChessboardPoint selectedPoint2;

    private int score=0;
    private int steps=0;

    public void setFootsteps(int footsteps) {
        this.steps = steps;
    }

    public GameController(ChessboardComponent view, Chessboard model) {
        this.view = view;
        this.model = model;

        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
    }

    private void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {

            }
        }
    }

    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
    }

    @Override
    public void onPlayerSwapChess() {
        // TODO: Init your swap function here.
        System.out.println("Implement your swap here.");
        if (selectedPoint != null && selectedPoint2 != null) {
            if (model.canSwap(selectedPoint, selectedPoint2)) {
                model.swapChessPiece(selectedPoint, selectedPoint2);
                //remove view 中的两个棋子
                ChessComponent chess1=view.removeChessComponentAtGrid(selectedPoint);
                ChessComponent chess2=view.removeChessComponentAtGrid(selectedPoint2);
                //在set view中两个棋子
                view.setChessComponentAtGrid(selectedPoint,chess2);
                view.setChessComponentAtGrid(selectedPoint2,chess1);
                //交换过来后需要重新绘制
                chess1.repaint();
                chess2.repaint();
                //重新设置selectedPoint为null
                selectedPoint=null;
                selectedPoint2=null;
                ++steps;
            }
        }
    }//江易明

    @Override
    public void onPlayerNextStep() {
        // TODO: Init your next step function here.
        boolean grid2[][]=model.candelete();
        Cell grid3[][]=model.getGrid();
        for (int i = 0; i <Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j <Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                if (grid2[i][j]){
                    //                ChessPiece chess=grid3[i][j].getPiece();
                    ChessboardPoint point=new ChessboardPoint(i,j);
                    model.removeChessPiece(point);
                    score=score+10;
                }
            }
        }
        model.candrap();//掉落棋子
        model.setnewgrid();//生成新的棋子
        //先遍历棋盘 删除掉view中各个Point点的Grid
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint point = new ChessboardPoint(i,j);
                view.removeChessComponentAtGrid(point);
            }
        }

        view.initiateChessComponent(model);
        //TODO:2023/12/12 上面棋子掉落下来
        // TODO: 2023/12/12 添加新棋子
        // TODO: 2023/12/12 view.initiateChessComponent(model);
        System.out.println("现在的分数是："+score);
        System.out.println("已经走的步数是"+steps);
        if (!model.ismatch()){
            isnextlevel(steps,score);
        }
        System.out.println("Implement your next step here.");
    }//江易明

    public int getScore() {
        return score;
    }

    // click a cell with a chess
    @Override
    public void onPlayerClickChessPiece(ChessboardPoint point, ChessComponent component) {
        if (selectedPoint2 != null) {
            var distance2point1 = Math.abs(selectedPoint.getCol() - point.getCol()) + Math.abs(selectedPoint.getRow() - point.getRow());
            var distance2point2 = Math.abs(selectedPoint2.getCol() - point.getCol()) + Math.abs(selectedPoint2.getRow() - point.getRow());
            var point1 = (ChessComponent) view.getGridComponentAt(selectedPoint).getComponent(0);
            var point2 = (ChessComponent) view.getGridComponentAt(selectedPoint2).getComponent(0);
            if (distance2point1 == 0 && point1 != null) {
                point1.setSelected(false);
                point1.repaint();
                selectedPoint = selectedPoint2;
                selectedPoint2 = null;
            } else if (distance2point2 == 0 && point2 != null) {
                point2.setSelected(false);
                point2.repaint();
                selectedPoint2 = null;
            } else if (distance2point1 == 1 && point2 != null) {
                point2.setSelected(false);
                point2.repaint();
                selectedPoint2 = point;
                component.setSelected(true);
                component.repaint();
            } else if (distance2point2 == 1 && point1 != null) {
                point1.setSelected(false);
                point1.repaint();
                selectedPoint = selectedPoint2;
                selectedPoint2 = point;
                component.setSelected(true);
                component.repaint();
            }
            return;
        }


        if (selectedPoint == null) {
            selectedPoint = point;
            component.setSelected(true);
            component.repaint();
            return;
        }

        var distance2point1 = Math.abs(selectedPoint.getCol() - point.getCol()) + Math.abs(selectedPoint.getRow() - point.getRow());

        if (distance2point1 == 0) {
            selectedPoint = null;
            component.setSelected(false);
            component.repaint();
            return;
        }

        if (distance2point1 == 1) {
            selectedPoint2 = point;
            component.setSelected(true);
            component.repaint();
        } else {
            selectedPoint2 = null;

            var grid = (ChessComponent) view.getGridComponentAt(selectedPoint).getComponent(0);
            if (grid == null) return;
            grid.setSelected(false);
            grid.repaint();

            selectedPoint = point;
            component.setSelected(true);
            component.repaint();
        }



    }
    public void saveGameToFile(String path) {
        List<String> saveLines = model.convertBoardToList();//读入转换好的数组（一个位置存储着棋盘一行的内容）
        for (String line:saveLines){
            System.out.println(line);
        }
        try {
            Files.write(Path.of(path),saveLines);//写文件
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }//冯俊铭 23/12/10 22：17

    public boolean loadGameFromFile(String path) {
        List<String> readLines = new ArrayList<>();
        try {
            readLines = Files.readAllLines(Path.of(path));//将文件中的各行读取到saveLines中
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String line:readLines){
            System.out.println(line);
        }
        if (model.readListToBoard(readLines)){
            //readListToBoard方法执行成功会返回true，才能进行读取到游戏界面上的操作
            //读入转换好的数组（一个位置存储着棋盘一行的内容）
            //先遍历棋盘 删除掉view中各个Point点的Grid
            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                    ChessboardPoint point = new ChessboardPoint(i,j);
                    view.removeChessComponentAtGrid(point);
                }
            }
            //然后再用下面这个方法重新载入model
            view.initiateChessComponent(model);
            // TODO: 2023/12/10 好像要先消除再重设initiateChessComponent？？？
            return true;
        }else {
            System.out.println("导入错误");
            return false;
        }


    }//冯俊铭 23/12/11 18：08

    public boolean passorfalse(){
        return false;
    }//TODO:判断步数是否为零
    int level=1;
    int levelsteps;
    int showsteps;

    public void setSteps(int steps) {
        this.steps = steps;
    }

    int minscore;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void isnextlevel(int steps, int score){
        switch (level){
            case 1:
                System.out.println("现在在第一关");
                minscore=200;//test
                levelsteps=20;
                showsteps=levelsteps-steps;
                System.out.println("剩余步数为"+showsteps);
                if (showsteps>=0){
                    if (score>=minscore){
                        score=0;
                        level++;
                        System.out.println("pass");//TODO:弹窗您成功通过第一关，选择窗口是否进入第二关 or save；
                        steps=0;
                    }

                }if (showsteps==0&&score<minscore){
                    System.out.println("Sorry,you failed");//TODO:弹窗对不起您失败了。
                }
                break;
            case 2:
                System.out.println("现在进入第二关");
                minscore=10000;
                levelsteps=15;
                showsteps=levelsteps-steps;
                if (showsteps>=0){
                    if (score>=minscore){
                        score=0;
                        level++;
                        System.out.println("pass");//TODO:弹窗您成功通过第二关，选择窗口是否进入第三关 or save；
                        steps=0;
                    }
                }if (showsteps==0&&score<minscore){
                    System.out.println("Sorry,you failed");//TODO:弹窗对不起您失败了。
                }
                break;
            case 3:
                System.out.println("现在进入第三关");
                minscore=10000;
                levelsteps=10;
                showsteps=levelsteps-steps;
                if (showsteps>=0){
                    if (score>=minscore){
                        score=0;
                        System.out.println("pass");//TODO:弹窗您成功通过第三关，gratulation；
                    }

                }if (showsteps==0&&score<minscore){
                    System.out.println("Sorry,you failed");//TODO:弹窗对不起您失败了。
                }
                break;
        }
    }//江易明and冯俊铭 2023.12.18

}