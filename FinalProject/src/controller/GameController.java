package controller;

import listener.GameListener;
import model.*;
import view.CellComponent;
import view.ChessComponent;
import view.ChessboardComponent;

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
 */
public class GameController implements GameListener {
    int level = 1;
    int levelsteps;
    int showsteps;
    private Chessboard model;
    private ChessboardComponent view;

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
    private ChessboardPoint selectedPoint2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int score = 0;
    private int steps = 0;
    int minscore;
    private String name = "新游戏";

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

    boolean swapdelete = true;

    @Override
    public int onPlayerSwapChess() {
        // TODO: Init your swap function here.
        System.out.println("Implement your swap here.");
        if (swapdelete) {
            if (selectedPoint != null && selectedPoint2 != null) {
                if (model.canSwap(selectedPoint, selectedPoint2)) {
                    model.swapChessPiece(selectedPoint, selectedPoint2);
                    //remove view 中的两个棋子
                    ChessComponent chess1 = view.removeChessComponentAtGrid(selectedPoint);
                    ChessComponent chess2 = view.removeChessComponentAtGrid(selectedPoint2);
                    //在set view中两个棋子
                    view.setChessComponentAtGrid(selectedPoint, chess2);
                    view.setChessComponentAtGrid(selectedPoint2, chess1);
                    //交换过来后需要重新绘制
                    chess1.repaint();
                    chess2.repaint();
                    //重新设置selectedPoint为null
                    selectedPoint = null;
                    selectedPoint2 = null;
                    ++steps;
                    swapdelete = false;
                    return 100;//返回100表示交换成功
                } else return 101;//返回101表示交换失败
            } else return 102;//额 反正不行就返回102呗
        } else return 103;//TODO:失败 江易明12.19更改

    }//江易明

    public void property() {
        Cell grid2[][] = model.getGrid();

//        ChessboardPoint point=new ChessboardPoint();


    }//道具，江易明 12.18

    int nextstep = 1;//用于判断nextstep的线程操作 江易明 12.19

    @Override
    public void onPlayerNextStep() {
        switch (nextstep) {
            case 1:
                boolean grid2[][] = model.candelete();
                Cell grid3[][] = model.getGrid();
                for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                    for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                        if (grid2[i][j]) {
                            ChessboardPoint point = new ChessboardPoint(i, j);
                            model.removeChessPiece(point);
                            score = score + 10;
                        }
                    }
                }
                model.candrap();//掉落棋子
                //先遍历棋盘 删除掉view中各个Point点的Grid
                for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                    for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                        ChessboardPoint point = new ChessboardPoint(i,j);

                        view.removeChessComponentAtGrid(point);
                    }
                }
                view.initiateChessComponent(model);
                System.out.println("刚刚点击的那下是case1");
                nextstep = 2;
                break;

            case 2:
                model.setnewgrid();//生成新的棋子
                //先遍历棋盘 删除掉view中各个Point点的Grid
                for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                    for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                        ChessboardPoint point = new ChessboardPoint(i,j);

                        view.removeChessComponentAtGrid(point);
                    }
                }
                view.initiateChessComponent(model);
                swapdelete = true;
                //TODO:2023/12/12 上面棋子掉落下来
                // TODO: 2023/12/12 添加新棋子
                // TODO: 2023/12/12 view.initiateChessComponent(model);
                System.out.println("现在的分数是：" + score);
                System.out.println("已经走的步数是" + steps);
                isnextlevel();//冯俊铭 不用传参数 score和steps就在GameController里面
                nextstep=1;
                System.out.println("刚刚点击的那下是case2");
                break;
        }
    }//江易明

    public void onPlayerInitiateNextStep() {
        // 这是游戏初始化的时候使用的nextstep方法
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

        isnextlevel();//冯俊铭 不用传参数 score和steps就在GameController里面
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
        for (String line : saveLines) {
            System.out.println(line);
        }
        try {
            Files.write(Path.of(path), saveLines);//写文件
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }//冯俊铭 23/12/10 22：17

    public int loadGameFromFile(String path) {
        List<String> readLines = new ArrayList<>();
        try {
            readLines = Files.readAllLines(Path.of(path));//将文件中的各行读取到saveLines中
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String line : readLines) {
            System.out.println(line);
        }
        if (model.readListToBoard(readLines) == 100) {
            //readListToBoard方法执行成功会返回100，才能进行读取到游戏界面上的操作
            //读入转换好的数组（一个位置存储着棋盘一行的内容）
            //先遍历棋盘 删除掉view中各个Point点的Grid
            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                    ChessboardPoint point = new ChessboardPoint(i, j);
                    view.removeChessComponentAtGrid(point);
                }
            }
            //然后再用下面这个方法重新载入model
            view.initiateChessComponent(model);
            // TODO: 2023/12/10 好像要先消除再重设initiateChessComponent？？？
            return 100;//100表示导入成功
        } else if (model.readListToBoard(readLines) == 102) {
            System.out.println("102导入错误");
            return 102;
        } else if (model.readListToBoard(readLines) == 103) {
            System.out.println("103导入错误");
            return 103;
        } else return 104;
    }//冯俊铭 23/12/11 18：08

    public boolean passorfalse() {
        return false;
    }//TODO:判断步数是否为零


    public void setSteps(int steps) {
        this.steps = steps;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void isnextlevel() {//江易明写 冯俊铭改：不用传参，直接用GameController下的score和step进行判断
        switch (level) {
            case 1:
                System.out.println("现在在第一关");
                minscore = 200;//test
                levelsteps = 20;
                showsteps = levelsteps - steps;
                System.out.println("剩余步数为" + showsteps);
                if (showsteps >= 0) {
                    if (score >= minscore) {
                        score = 0;
                        level++;
                        System.out.println("pass");//TODO:弹窗您成功通过第一关，选择窗口是否进入第二关 or save；
                        steps = 0;
                    }

                }
                if (showsteps == 0 && score < minscore) {
                    System.out.println("Sorry,you failed");//TODO:弹窗对不起您失败了。
                }
                break;
            case 2:
                System.out.println("现在进入第二关");
                minscore = 10000;
                levelsteps = 5;//test
                showsteps = levelsteps - steps;
                if (showsteps >= 0) {
                    if (score >= minscore) {
                        score = 0;
                        level++;
                        System.out.println("pass");//TODO:弹窗您成功通过第二关，选择窗口是否进入第三关 or save；
                        steps = 0;
                    }
                }
                if (showsteps == 0 && score < minscore) {
                    System.out.println("Sorry,you failed");//TODO:弹窗对不起您失败了。
                }
                break;
            case 3:
                System.out.println("现在进入第三关");
                minscore = 10000;
                levelsteps = 10;
                showsteps = levelsteps - steps;
                if (showsteps >= 0) {
                    if (score >= minscore) {
                        score = 0;
                        System.out.println("pass");//TODO:弹窗您成功通过第三关，gratulation；
                    }

                }
                if (showsteps == 0 && score < minscore) {
                    System.out.println("Sorry,you failed");//TODO:弹窗对不起您失败了。
                }
                break;
        }
    }//江易明and冯俊铭 2023.12.18


}