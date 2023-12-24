package controller;

import listener.GameListener;
import model.*;
import view.CellComponent;
import view.ChessComponent;
import view.ChessGameFrame;
import view.ChessboardComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private ChessGameFrame mainFrame;

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
    private ChessboardPoint selectedPoint2;

    public Boolean isIronMode=false;//fjm 用来判断是不是铁人模式
    public String getName() {
        return name;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int score = 0;
    private int steps = 0;
    int minscore;
    private String name = "新游戏";
    private int mode = 1;//游戏模式，1是手动模式、2是自动模式
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

    public GameController(ChessboardComponent view, Chessboard model,ChessGameFrame mainFrame) {
        this.view = view;
        this.model = model;
        //!!注意理解 这里是调用而非生成新的
        view.registerController(this);
        //使view可以调用gameController
        registerMainFrame(mainFrame);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
    }
    private void registerMainFrame(ChessGameFrame mainFrame){
        this.mainFrame = mainFrame;
    }//冯俊铭 23/12/21 使GameController也可以调用mainFrame相关的方法
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

    boolean swapdelete = true;// TODO: 2023/12/20

    @Override
    public int onPlayerSwapChess() {
        if (selectedPoint != null && selectedPoint2 != null && !model.ismatch()) {
            //必须得选中两个点、且棋盘上没有可以消除的棋子才可以执行交换消除方法
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
                {//交换后执行消除
                    boolean grid2[][] = model.candelete();
                    Cell grid3[][] = model.getGrid();
                    for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                        for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                            if (grid2[i][j]) {
                                ChessboardPoint point = new ChessboardPoint(i, j);
                                model.removeChessPiece(point);
                                view.removeChessComponentAtGrid(point);
                                score = score + 10;
                            }
                        }
                    }
                }//消除完毕
                view.repaint();//冯俊铭 !!交换后执行repaint()重绘该组件 避免摇一摇更新
                ++steps;
                //swapdelete = false;
                System.out.println("成功执行了交换");
                isnextlevel();//冯俊铭 判断是否进入下一关！！之前没加这个所以换不了关
                nextstep =1;//fjm !!一旦交换，则将nextstep必须改成1 避免交换后nextstep执行case2方法导致棋子不下落而直接生成新的
                return 100;//返回100表示交换且消除成功
            } else return 101;//返回101表示交换失败
        } else if (model.ismatch()) {
            //如果棋盘中还有能消除的棋子则先消除 这里可能会报错，但是无所谓，不影响
            boolean grid2[][] = model.candelete();
            Cell grid3[][] = model.getGrid();
            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                    if (grid2[i][j]) {
                        ChessboardPoint point = new ChessboardPoint(i, j);
                        model.removeChessPiece(point);
                        view.removeChessComponentAtGrid(point);
                        score = score + 10;
                    }
                }
            }
            //消除完毕
            view.repaint();//冯俊铭 !!交换后执行repaint()重绘该组件 避免摇一摇更新
            System.out.println("交换未执行，消除了棋盘上还可以消除的棋子");
            nextstep =1;//fjm !!一旦交换，则将nextstep必须改成1 避免交换后nextstep执行case2方法导致棋子不下落而直接生成新的
            return 103;//返回103表示没有交换，消除了棋盘上还能消除的棋子
        } else return 102;//返回102表示没有选择两个点，且棋盘上已经没有棋子可以消除了


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
                /*boolean grid2[][] = model.candelete();
                Cell grid3[][] = model.getGrid();
                for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                    for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                        if (grid2[i][j]) {
                            ChessboardPoint point = new ChessboardPoint(i, j);
                            model.removeChessPiece(point);
                            view.removeChessComponentAtGrid(point);
                            score = score + 10;
                        }
                    }
                }*/ //冯俊铭 12.20 把这段去掉了 交换消除放在swap里面
                model.candrap();//掉落棋子
                //先遍历棋盘 删除掉view中各个Point点的Grid
                for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                    for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                        ChessboardPoint point = new ChessboardPoint(i,j);

                        view.removeChessComponentAtGrid(point);
                    }
                }
                view.initiateChessComponent(model);
                view.repaint();//冯俊铭 !!交换后执行repaint()重绘该组件 避免摇一摇更新
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
                view.repaint();//冯俊铭 !!交换后执行repaint()重绘该组件 避免摇一摇更新
                swapdelete = true;
                System.out.println("现在的分数是：" + score);
                System.out.println("已经走的步数是" + steps);
                //isnextlevel();//冯俊铭 不用传参数 score和steps就在GameController里面
                //冯俊铭 12.20 把上面的注释掉了，判断是否进入下一关应该放在swap中
                nextstep=1;
                System.out.println("刚刚点击的那下是case2");
                break;
        }
    }//江易明

    public void onPlayerAutoNextStep() {
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
        saveLines.add("当前存档名:"+name);
        saveLines.add("当前关卡:"+level);
        saveLines.add("当前步数:"+steps);
        saveLines.add("当前分数:"+score);
        saveLines.add("当前模式:"+mode);
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

        //先将棋盘读入
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
            //读取readLines最后一行，存的是模式（这个必须单独读取）
            String[] modeinfo = readLines.get(readLines.size()-1).split(":");
            mode = Integer.parseInt(modeinfo[1]);
            //再读入游戏的其他参数
            for (int i = Constant.CHESSBOARD_ROW_SIZE.getNum(); i < readLines.size() - 1; i++) {
                String[] info = readLines.get(i).split(":");
                switch (info[0]) {
                    case "当前存档名" -> {
                        name = info[1];
                        System.out.println("导入成功，当前存档名" + name);
                    }
                    case "当前关卡" -> {
                        level = Integer.parseInt(info[1]);
                        System.out.println("导入成功，当前关卡" + level);
                    }
                    case "当前步数" -> {
                        steps = Integer.parseInt(info[1]);
                        System.out.println("导入成功，当前步数" + steps);
                    }
                    case "当前分数" -> {
                        score = Integer.parseInt(info[1]);
                        System.out.println("导入成功，当前分数" + score);
                    }
                }
            }
            if (mode==1){
                return 1001;//1001表示导入成功，模式为手动模式
            }else if (mode==2){
                return 1002;//1002表示导入成功，模式为自动模式
            }else return 1003;//1003表示棋盘导入成功，但模式违法

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

    public void Auto() {
        //这个自动方法只有在模式2才生效，模式1不生效 且 只有选中了两个格子才能生效
        if (mode ==2 && selectedPoint!=null && selectedPoint2!=null){
            System.out.println("调用了自动化方法");
            onPlayerSwapChess();//先执行交换消除
            onPlayerNextStep();//掉落
            onPlayerNextStep();//生成新的
            mainFrame.updateLables();//mainFrame更新标签
            while (model.ismatch()){
                //如果棋盘上还有可以消除的，则循环运行这三个方法
                onPlayerSwapChess();//先执行交换消除
                onPlayerNextStep();//掉落
                onPlayerNextStep();//生成新的
                mainFrame.updateLables();//mainFrame更新标签
            }

        }
    }//冯俊铭
}