package controller;

import listener.GameListener;
import model.*;
import view.CellComponent;
import view.ChessComponent;
import view.ChessGameFrame;
import view.ChessboardComponent;

import javax.swing.*;
import java.io.File;
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
    private ChessboardPoint selectedPoint;//每一次点击的位置
    private ChessboardPoint selectedPoint2;

    public Boolean isIronMode = false;//fjm 用来判断是不是铁人模式

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
    private boolean canUseProp =true;
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

    public int getsteps() {
        return steps;
    }


    public void setFootsteps(int footsteps) {
        this.steps = steps;
    }

    public GameController(ChessboardComponent view, Chessboard model, ChessGameFrame mainFrame) {
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

    private void registerMainFrame(ChessGameFrame mainFrame) {
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
                if (!isIronMode){
                    autoSaveGameToFile();//每走一步自动保存
                }//加步数和自动保存必须同步，且必须在棋盘做出改变前保存
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
                    boolean grid3[][] = model.addscore();
                    for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                        for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                            if (grid2[i][j]) {
                                ChessboardPoint point = new ChessboardPoint(i, j);
                                model.removeChessPiece(point);
                                view.removeChessComponentAtGrid(point);
                                score = score + 10;
                            }
                            if (grid3[i][j]){
                                score = score + 10;
                            }
                        }
                    }
                }//消除完毕
                view.repaint();//冯俊铭 !!交换后执行repaint()重绘该组件 避免摇一摇更新
                steps++;
                //swapdelete = false;
                System.out.println("成功执行了交换");
                isnextlevel();//冯俊铭 判断是否进入下一关！！之前没加这个所以换不了关
                nextstep = 1;//fjm !!一旦交换，则将nextstep必须改成1 避免交换后nextstep执行case2方法导致棋子不下落而直接生成新的
                return 100;//返回100表示交换且消除成功
            } else return 101;//返回101表示交换失败
        } else if (model.ismatch()) {
            //如果棋盘中还有能消除的棋子则先消除 这里可能会报错，但是无所谓，不影响
            boolean grid2[][] = model.candelete();
            boolean grid3[][] = model.addscore();
            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                    if (grid2[i][j]) {
                        ChessboardPoint point = new ChessboardPoint(i, j);
                        model.removeChessPiece(point);
                        view.removeChessComponentAtGrid(point);
                        score = score + 10;
                    }
                    if (grid3[i][j]){
                        score = score + 10;
                    }
                }
            }
            //消除完毕
            view.repaint();//冯俊铭 !!交换后执行repaint()重绘该组件 避免摇一摇更新
            System.out.println("交换未执行，消除了棋盘上还可以消除的棋子");
            nextstep = 1;//fjm !!一旦交换，则将nextstep必须改成1 避免交换后nextstep执行case2方法导致棋子不下落而直接生成新的
            isnextlevel();
            return 103;//返回103表示没有交换，消除了棋盘上还能消除的棋子
        } else return 102;//返回102表示没有选择两个点，且棋盘上已经没有棋子可以消除了


    }//江易明

    public void property() {
        Cell grid2[][] = model.getGrid();

//        ChessboardPoint point=new ChessboardPoint();


    }//道具，江易明 12.18

    int nextstep = 1;//用于判断nextstep的线程操作 江易明 12.19
    public int getNextstep() {
        return nextstep;
    }
    @Override
    public void onPlayerNextStep() {
        switch (nextstep) {
            case 1:
                model.candrap();//掉落棋子
                //先遍历棋盘 删除掉view中各个Point点的Grid
                for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                    for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                        ChessboardPoint point = new ChessboardPoint(i, j);

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
                        ChessboardPoint point = new ChessboardPoint(i, j);

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
                nextstep = 1;
                System.out.println("刚刚点击的那下是case2");

                isdeadend();//生成新棋子后判断是不是死局 todo 死局弹窗
                break;
            case 3:
                nextstep=2;
                onPlayerNextStep();
                nextstep=2;
                onPlayerNextStep();
                nextstep=1;
                //冯俊铭 这是专门给全屏炸弹用的
        }
    }//江易明

    public void onPlayerAutoNextStep() {
        // 这是游戏初始化的时候使用的nextstep方法
        boolean grid2[][] = model.candelete();
        boolean grid3[][] = model.addscore();
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                if (grid2[i][j]) {
                    //ChessPiece chess=grid3[i][j].getPiece();
                    ChessboardPoint point = new ChessboardPoint(i, j);
                    model.removeChessPiece(point);
                    score = score + 10;
                }
                if (grid3[i][j]){
                    score = score + 10;
                }
            }
        }
        model.candrap();//掉落棋子
        model.setnewgrid();//生成新的棋子
        //先遍历棋盘 删除掉view中各个Point点的Grid
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint point = new ChessboardPoint(i, j);
                view.removeChessComponentAtGrid(point);
            }
        }
        view.initiateChessComponent(model);
        //TODO:2023/12/12 上面棋子掉落下来
        // TODO: 2023/12/12 添加新棋子
        // TODO: 2023/12/12 view.initiateChessComponent(model);
        System.out.println("现在的分数是：" + score);
        System.out.println("已经走的步数是" + steps);

        //isnextlevel();//冯俊铭 这个方法开始也会用，不要在这里判断是否下一关 否则会在开始新游戏的时候报窗口
    }//江易明
    public void onPlayerAutoNextStep(int re) {
        // 这是游戏重排的时候使用的nextstep方法
        boolean grid2[][] = model.candelete();
        boolean grid3[][] = model.addscore();
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                if (grid2[i][j]) {
                    //ChessPiece chess=grid3[i][j].getPiece();
                    ChessboardPoint point = new ChessboardPoint(i, j);
                    model.removeChessPiece(point);
                }

            }
        }
        model.candrap();//掉落棋子
        model.setnewgrid();//生成新的棋子
        //先遍历棋盘 删除掉view中各个Point点的Grid
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint point = new ChessboardPoint(i, j);
                view.removeChessComponentAtGrid(point);
            }
        }
        view.initiateChessComponent(model);
        //TODO:2023/12/12 上面棋子掉落下来
        // TODO: 2023/12/12 添加新棋子
        // TODO: 2023/12/12 view.initiateChessComponent(model);
        System.out.println("现在的分数是：" + score);
        System.out.println("已经走的步数是" + steps);

        //isnextlevel();//冯俊铭 这个方法开始也会用，不要在这里判断是否下一关 否则会在开始新游戏的时候报窗口
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
        saveLines.add("当前存档名:" + name);
        saveLines.add("当前关卡:" + level);
        saveLines.add("当前步数:" + steps);
        saveLines.add("当前分数:" + score);
        saveLines.add("当前剩余超级交换步数:" + supersteps);
        saveLines.add("当前RemoveRow:" + mainFrame.getViewRemoveRow());
        saveLines.add("当前Remove33:" + mainFrame.getViewRemove33());
        saveLines.add("当前RefreshAll:" + mainFrame.getViewRefreshAll());
        saveLines.add("当前SuperSwap:" + mainFrame.getViewSuperSwap());
        saveLines.add("当前皮肤:" + mainFrame.getSkin());
        saveLines.add("是否能使用道具:" + canUseProp);
        saveLines.add("当前模式:" + mode);//模式必须存在最后一行
        try {
            Files.write(Path.of(path), saveLines);//写文件
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }//冯俊铭 23/12/10 22：17

    ArrayList<String> autoSaves = new ArrayList<>();//fjm
    public void autoSaveGameToFile() {
        List<String> saveLines = model.convertBoardToList();//读入转换好的数组（一个位置存储着棋盘一行的内容）
        for (String line : saveLines) {
            System.out.println(line);
        }
        saveLines.add("当前存档名:" + name);
        saveLines.add("当前关卡:" + level);
        saveLines.add("当前步数:" + steps);
        saveLines.add("当前分数:" + score);
        saveLines.add("当前剩余超级交换步数:" + supersteps);
        saveLines.add("当前RemoveRow:" + mainFrame.getViewRemoveRow());
        saveLines.add("当前Remove33:" + mainFrame.getViewRemove33());
        saveLines.add("当前RefreshAll:" + mainFrame.getViewRefreshAll());
        saveLines.add("当前SuperSwap:" + mainFrame.getViewSuperSwap());
        saveLines.add("当前皮肤:" + mainFrame.getSkin());
        saveLines.add("是否能使用道具:" + canUseProp);
        saveLines.add("当前模式:" + mode);//模式必须存在最后一行
        File file = new File("./package/"+name); //以某路径实例化一个File对象
        String url =  "./package/"+name+"/"+level+"_"+steps+".txt";
        autoSaves.add(url);
        if (!file.exists()){ //如果不存在
            boolean dr = file.mkdirs(); //创建目录
        }
        try {
            Files.write(Path.of(url), saveLines);//写文件
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(autoSaves.toString());
    }//冯俊铭 23/12/10 22：17

    public void setBackgameChance(int backgameChance) {
        this.backgameChance = backgameChance;
    }

    private int backgameChance = 2;//记得在新游戏开始前重新设置backgameChance
    public void isbackGame() {
        if (backgameChance > 0) {
            int option = JOptionPane.showConfirmDialog(mainFrame, "悔棋将消耗次数，是否悔棋？\n 当前剩余悔棋次数:" + backgameChance, "再次确认", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                backGame();
                JOptionPane.showMessageDialog(mainFrame, "当前剩余悔棋次数：" + backgameChance, "提示", JOptionPane.WARNING_MESSAGE);
            }
        }else JOptionPane.showMessageDialog(mainFrame, "无条件悔棋次数用完啦！\n 只有死局才能悔棋噢", "悔棋失败", JOptionPane.WARNING_MESSAGE);


    }
    public void backGame(){
        try {
            System.out.println(autoSaves.toString());
            loadGameFromFile(autoSaves.get(autoSaves.size()-1));//加载上一步的存档
            Files.delete(Path.of(autoSaves.get(autoSaves.size()-1)));//删掉上一步已经存好的存档文件
            autoSaves.remove(autoSaves.size()-1);//删除最新的url
            mainFrame.updateLables();
            backgameChance--;
            System.out.println(autoSaves.toString());
        }catch (Exception e){
            System.out.println(e);
            JOptionPane.showMessageDialog(mainFrame, "没有存档可以读取啦", "悔棋失败", JOptionPane.WARNING_MESSAGE);
        }
    }

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
            String[] modeinfo = readLines.get(readLines.size() - 1).split(":");
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
                    case "当前剩余超级交换步数"->{
                        int temp = Integer.parseInt(info[1]);
                        supersteps = temp;
                        System.out.println("导入成功，当前剩余超级交换步数:" + temp);
                    }case "当前RemoveRow"->{
                        int temp = Integer.parseInt(info[1]);
                        mainFrame.setViewRemoveRow(temp);
                        System.out.println("导入成功，当前剩余RemoveRow:" + temp);
                    }case "当前Remove33"->{
                        int temp = Integer.parseInt(info[1]);
                        mainFrame.setViewRemove33(temp);
                        System.out.println("导入成功，当前剩余Remove33:" + temp);
                    }case "当前RefreshAll"->{
                        int temp = Integer.parseInt(info[1]);
                        mainFrame.setViewRefreshAll(temp);
                        System.out.println("导入成功，当前剩余RefreshAll:" + temp);
                    }case "当前SuperSwap"->{//道具数
                        int temp = Integer.parseInt(info[1]);
                        mainFrame.setViewSuperSwap(temp);
                        System.out.println("导入成功，当前剩余SuperSwap:" + temp);
                    }case "当前皮肤"->{
                        String temp = info[1];
                        mainFrame.setSkin(temp);
                        System.out.println("导入成功，当前皮肤:" + temp);
                    }case "是否能使用道具"->{
                        String temp = info[1];
                        if (temp.equals("false")){
                            canUseProp=false;
                        }else {
                            canUseProp=true;
                        }
                        System.out.println("导入成功，当前能否使用道具:" + temp);
                    }
                }
            }
            view.repaint();
            mainFrame.updateLables();
            if (mode == 1) {
                return 1001;//1001表示导入成功，模式为手动模式
            } else if (mode == 2) {
                return 1002;//1002表示导入成功，模式为自动模式
            } else return 1003;//1003表示棋盘导入成功，但模式违法

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

    public String getCurrentLevelInfo(){
        int currentMinScore = 0;
        int currentlevelStep = 0;
        switch (level){
            case 1 ->{
                currentMinScore = minscore1;
                currentlevelStep = levelsteps1;
            }case 2 ->{
                currentMinScore = minscore2;
                currentlevelStep = levelsteps2;
            }case 3 ->{
                currentMinScore = minscore3;
                currentlevelStep = levelsteps3;
            }case 4 ->{
                currentMinScore = minscore4;
                currentlevelStep = levelsteps4;
            }case 5 ->{
                currentMinScore = minscore5;
                currentlevelStep = levelsteps5;
            }
        }
        return "当前关卡要求分数:"+currentMinScore+"  当前关卡最大步数:"+currentlevelStep;
    }
    private int minscore1 = 200;//test
    private int levelsteps1 = 15;
    private int minscore2 = 400;
    private int levelsteps2 = 15;//test
    private int minscore3 = 500;
    private int levelsteps3 = 15;
    private int minscore4 = 800;
    private int levelsteps4 = 15;
    private int minscore5 = 1000;
    private int levelsteps5 = 10;
    public void isnextlevel() {//江易明写 冯俊铭改：不用传参，直接用GameController下的score和step进行判断
        switch (level) {
            case 1:
                System.out.println("现在在第一关");

                if (steps<=levelsteps1) {
                    if (score >= minscore1) {
                        mainFrame.startplayNextLevelMusic();//播放成功进入下一关的音乐
                        System.out.println("pass");//TODO:弹窗您成功通过第一关，选择窗口是否进入第二关 or save；
                        yesOrNoNextLevel();
                    }

                }
                if (steps>=levelsteps1 && score < minscore1) {
                    mainFrame.startplayFailedMusic();//播放失败音乐
                    System.out.println("Sorry,you failed");//TODO:弹窗对不起您失败了。
                    yesOrNoRestartLevel();
                }
                break;
            case 2:
                System.out.println("现在进入第二关");
                if (steps<=levelsteps2 ) {
                    if (score >= minscore2) {
                        mainFrame.startplayNextLevelMusic();//播放成功进入下一关的音乐
                        System.out.println("pass");//TODO:弹窗您成功通过第二关，选择窗口是否进入第三关 or save；
                        yesOrNoNextLevel();
                    }
                }
                if (steps>=levelsteps2 && score < minscore2) {
                    mainFrame.startplayFailedMusic();//播放失败音乐
                    System.out.println("Sorry,you failed");//TODO:弹窗对不起您失败了。
                    yesOrNoRestartLevel();
                }
                break;
            case 3:
                System.out.println("现在进入第三关");

                if (steps<=levelsteps3) {
                    if (score >= minscore3) {
                        mainFrame.startplayNextLevelMusic();//播放成功进入下一关的音乐
                        System.out.println("pass");//TODO:弹窗您成功通过第三关，gratulation；
                        yesOrNoNextLevel();
                    }

                }
                if (steps>=levelsteps3 && score < minscore3) {
                    mainFrame.startplayFailedMusic();//播放失败音乐
                    System.out.println("Sorry,you failed");//TODO:弹窗对不起您失败了。
                    yesOrNoRestartLevel();
                }
                break;
            case 4:
                System.out.println("现在进入第四关");

                if (steps<=levelsteps4) {
                    if (score >= minscore4) {
                        mainFrame.startplayNextLevelMusic();//播放成功进入下一关的音乐
                        score = 0;
                        System.out.println("pass");//TODO:弹窗您成功通过第四关，gratulation；
                        yesOrNoNextLevel();
                    }

                }
                if (steps>=levelsteps4 && score < minscore4) {
                    mainFrame.startplayFailedMusic();//播放失败音乐
                    System.out.println("Sorry,you failed");//TODO:弹窗对不起您失败了。
                }
                break;
            case 5:
                System.out.println("现在进入第五关");

                if (steps<=levelsteps5) {
                    if (score >= minscore5) {
                        mainFrame.startplayNextLevelMusic();//播放成功进入下一关的音乐
                        int option = JOptionPane.showConfirmDialog(mainFrame,"是否重新开始？\n(“是”则在当前棋盘重新开始新的游戏，“否”则停留在当前棋盘)","恭喜!你完成了所有关卡", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (option==JOptionPane.YES_OPTION){
                            if (canUseProp){
                                mainFrame.setViewRemoveRow(2);
                                mainFrame.setViewRemove33(2);
                                mainFrame.setViewRefreshAll(1);
                                mainFrame.setViewSuperSwap(2);
                            }else {
                                mainFrame.setViewRemoveRow(0);
                                mainFrame.setViewRemove33(0);
                                mainFrame.setViewRefreshAll(0);
                                mainFrame.setViewSuperSwap(0);


                            }
                            model.initPieces();//让model棋盘的元素重新生成
                            //遍历棋盘 删除掉view中各个Point点的Grid
                            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                                    ChessboardPoint point = new ChessboardPoint(i,j);
                                    view.removeChessComponentAtGrid(point);
                                }
                            }
                            //再根据初始化好的model棋盘重新生成view
                            view.initiateChessComponent(model);
                            //初始化model棋盘
                            while (model.ismatch()){
                                onPlayerAutoNextStep();
                            }
                            //先遍历棋盘 删除掉view中各个Point点的Grid
                            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                                    ChessboardPoint point = new ChessboardPoint(i,j);
                                    view.removeChessComponentAtGrid(point);
                                }
                            }
                            //再根据初始化好的model棋盘重新生成view
                            backgameChance =2;//悔棋次数重新设置
                            score = 0;
                            level = 1;
                            steps = 0;
                            view.initiateChessComponent(model);
                            mainFrame.updateLables();
                            view.repaint();
                        }

                    }

                }
                if (steps>=levelsteps5 && score < minscore5) {
                    mainFrame.startplayFailedMusic();//播放失败音乐
                    System.out.println("Sorry,you failed");//TODO:弹窗对不起您失败了。
                    yesOrNoRestartLevel();
                }
                break;
        }
    }//江易明and冯俊铭 2023.12.18
    private boolean yesOrNoNextLevel(){
        //主要是弹出对话框确认
        int option = JOptionPane.showConfirmDialog(mainFrame,"是否进入下一关？\n(“是”则进入下一关，“否”则停留在当前棋盘)","恭喜过关", JOptionPane.YES_NO_CANCEL_OPTION);
        if (option == JOptionPane.YES_OPTION){
            score = 0;
            level++;
            steps = 0;
            mainFrame.updateLables();
            /*if (!isIronMode){
                autoSaveGameToFile();//每走一步自动保存
            }//加步数和自动保存必须同步，且必须在棋盘做出改变前保存*/
            return true;
        }else {
            return false;
        }
    }//fjm 代码复用
    private void yesOrNoRestartLevel(){
        int option = JOptionPane.showConfirmDialog(mainFrame,"是否返回主界面？\n(“是”返回主界面，“否”则停留在当前棋盘)","失败啦", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION){
            mainFrame.setVisible(false);
            mainFrame.stopPlayMusic();//把主界面音乐关了
            mainFrame.getSettingFrame().getStartFrame().setVisible(true);
        }
    }
    public void Auto() {
        //这个自动方法只有在模式2才生效，模式1不生效 且 只有选中了两个格子才能生效
        if (mode == 2 && selectedPoint != null && selectedPoint2 != null) {
            System.out.println("调用了自动化方法");
            if (supersteps>0){
                superswap();//执行超级交换
                mainFrame.startplaySuccessSwapMusic();//播放交换成功音乐
            }else {
                onPlayerSwapChess();//先执行交换消除
                mainFrame.startplaySuccessSwapMusic();//播放交换成功音乐
            }
            onPlayerNextStep();//掉落
            onPlayerNextStep();//生成新的
            mainFrame.updateLables();//mainFrame更新标签
            while (model.ismatch()) {
                //如果棋盘上还有可以消除的，则循环运行这三个方法
                onPlayerSwapChess();//先执行交换消除
                onPlayerNextStep();//掉落
                onPlayerNextStep();//生成新的
                mainFrame.updateLables();//mainFrame更新标签
            }

        }
    }//冯俊铭

    public ChessboardPoint getSelectedPoint() {
        return selectedPoint;
    }

    public void removeRow() {
        if (selectedPoint != null) {
            if (!isIronMode){
                autoSaveGameToFile();//每走一步自动保存
            }//加步数和自动保存必须同步，且必须在棋盘做出改变前保存
            int row1 = selectedPoint.getRow();
            int col1 = selectedPoint.getCol();
            //拿到已经点击的点的行和列
            for (int i = 0; i <= 7; i++) {
                if (i != col1) {
                    ChessboardPoint point = new ChessboardPoint(row1, i);
                    model.removeChessPiece(point);
                    view.removeChessComponentAtGrid(point);
                    score = score + 10;
                }
            }
            for (int i = 0; i <= 7; i++) {
                ChessboardPoint point = new ChessboardPoint(i, col1);
                model.removeChessPiece(point);
                view.removeChessComponentAtGrid(point);
                score = score + 10;
            }
            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                    ChessboardPoint point = new ChessboardPoint(i, j);

                    view.removeChessComponentAtGrid(point);
                }
            }
            view.initiateChessComponent(model);
            view.repaint();//交换后执行repaint()重绘该组件 避免摇一摇更新
            steps++;//步数+1
            mainFrame.updateLables();
            nextstep=1;
            isnextlevel();//点击后判断能否下一关
            System.out.println("点击了火爆辣椒按钮");
            mainFrame.setViewRemoveRow(mainFrame.getViewRemoveRow()-1);
        }else {
            JOptionPane.showMessageDialog(null,"请先选择一个点再释放道具","使用失败",JOptionPane.WARNING_MESSAGE);
        }
    }//江易明 2023.12.25

    public void remove33() {
        if (selectedPoint != null) {
            if (!isIronMode){
                autoSaveGameToFile();//每走一步自动保存
            }//加步数和自动保存必须同步，且必须在棋盘做出改变前保存
            int row1 = selectedPoint.getRow();
            int col1 = selectedPoint.getCol();
            if ((row1 >= 1 && row1 <= 6) && (col1 >= 1 && col1 <= 6)) {
                for (int i = row1 - 1; i <= row1 + 1; i++) {
                    for (int j = col1 - 1; j <= col1 + 1; j++) {
                        ChessboardPoint point = new ChessboardPoint(i, j);
                        model.removeChessPiece(point);
                        view.removeChessComponentAtGrid(point);
                        score = score + 10;
                    }
                }
            }
            if (row1 == 0 && col1 == 0) {
                for (int i = 0; i <= 1; i++) {
                    for (int j = 0; j <= 1; j++) {
                        ChessboardPoint point = new ChessboardPoint(i, j);
                        model.removeChessPiece(point);
                        view.removeChessComponentAtGrid(point);
                        score = score + 10;
                    }
                }
            }//左上
            if (row1 == 7 && col1 == 0) {
                for (int i = 6; i <= 7; i++) {
                    for (int j = 0; j <= 1; j++) {
                        ChessboardPoint point = new ChessboardPoint(i, j);
                        model.removeChessPiece(point);
                        view.removeChessComponentAtGrid(point);
                        score = score + 10;
                    }
                }
            }//左下
            if (row1 == 0 && col1 == 7) {
                for (int i = 0; i <= 1; i++) {
                    for (int j = 6; j <= 7; j++) {
                        ChessboardPoint point = new ChessboardPoint(i, j);
                        model.removeChessPiece(point);
                        view.removeChessComponentAtGrid(point);
                        score = score + 10;
                    }
                }
            }//右上
            if (row1 == 7 && col1 == 7) {
                for (int i = 6; i <= 7; i++) {
                    for (int j = 6; j <= 7; j++) {
                        ChessboardPoint point = new ChessboardPoint(i, j);
                        model.removeChessPiece(point);
                        view.removeChessComponentAtGrid(point);
                        score = score + 10;
                    }
                }
            }//右下
            if (col1 == 0 && (row1 >= 1 && row1 <= 6)) {
                for (int i = row1 - 1; i <= row1 + 1; i++) {
                    for (int j = 0; j <= 1; j++) {
                        ChessboardPoint point = new ChessboardPoint(i, j);
                        model.removeChessPiece(point);
                        view.removeChessComponentAtGrid(point);
                        score = score + 10;
                    }
                }
            }//左侧
            if (col1 == 7 && (row1 >= 1 && row1 <= 6)) {
                for (int i = row1 - 1; i <= row1 + 1; i++) {
                    for (int j = 6; j <= 7; j++) {
                        ChessboardPoint point = new ChessboardPoint(i, j);
                        model.removeChessPiece(point);
                        view.removeChessComponentAtGrid(point);
                        score = score + 10;
                    }
                }
            }//右侧
            if (row1 == 0 && (col1 >= 1 && col1 <= 6)) {
                for (int i = 0; i <= 1; i++) {
                    for (int j = col1 - 1; j <= col1 + 1; j++) {
                        ChessboardPoint point = new ChessboardPoint(i, j);
                        model.removeChessPiece(point);
                        view.removeChessComponentAtGrid(point);
                        score=score+10;
                    }
                }
            }//上侧
            if (row1 == 7 && (col1 >= 1 && col1 <= 6)) {
                for (int i = 6; i <= 7; i++) {
                    for (int j = col1 - 1; j <= col1 + 1; j++) {
                        ChessboardPoint point = new ChessboardPoint(i, j);
                        model.removeChessPiece(point);
                        view.removeChessComponentAtGrid(point);
                        score=score+10;
                    }
                }
            }//下侧
            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                    ChessboardPoint point = new ChessboardPoint(i, j);
                    view.removeChessComponentAtGrid(point);
                }
            }
            view.initiateChessComponent(model);
            view.repaint();
            nextstep=1;
            steps++;//步数+1
            mainFrame.updateLables();
            isnextlevel();//点击后判断能否下一关
            System.out.println("点击了樱桃炸弹");
            mainFrame.setViewRemove33(mainFrame.getViewRemove33()-1);
        }else {
            JOptionPane.showMessageDialog(null,"请先选择一个点再释放道具","使用失败",JOptionPane.WARNING_MESSAGE);
        }
    }
    public int supersteps;

    public void RefreshAll(){
        if (!isIronMode){
            autoSaveGameToFile();//每走一步自动保存
        }//加步数和自动保存必须同步，且必须在棋盘做出改变前保存
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                //直接把所有棋子进行消除
                ChessboardPoint point = new ChessboardPoint(i, j);
                model.removeChessPiece(point);
                view.removeChessComponentAtGrid(point);
                score = score + 10;
            }
        }
        nextstep=3;
        steps++;//步数+1
        mainFrame.updateLables();
        isnextlevel();//消除完后判断能否下一关
        //view.initiateChessComponent(model);
        view.repaint();
    }

    public void reArrangeAll(){//死局时进行重排
        model.initPieces();//让model棋盘的元素重新生成
        //遍历棋盘 删除掉view中各个Point点的Grid
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint point = new ChessboardPoint(i,j);
                view.removeChessComponentAtGrid(point);
            }
        }
        //再根据初始化好的model棋盘重新生成view
        view.initiateChessComponent(model);
        //初始化model棋盘
        while (model.ismatch()){
            onPlayerAutoNextStep(1);
        }

        //先遍历棋盘 删除掉view中各个Point点的Grid
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint point = new ChessboardPoint(i,j);
                view.removeChessComponentAtGrid(point);
            }
        }
        //再根据初始化好的model棋盘重新生成view
        view.initiateChessComponent(model);
        System.out.println("重排好啦");
        nextstep=3;
        //view.initiateChessComponent(model);
        view.repaint();
    }
    public int superswap(){
        if (selectedPoint != null && selectedPoint2 != null && !model.ismatch()){
            if (!isIronMode){
                autoSaveGameToFile();//每走一步自动保存
            }//加步数和自动保存必须同步，且必须在棋盘做出改变前保存
            model.swapChessPiece(selectedPoint,selectedPoint2);
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
                boolean grid3[][] = model.addscore();
                for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                    for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                        if (grid2[i][j]) {
                            ChessboardPoint point = new ChessboardPoint(i, j);
                            model.removeChessPiece(point);
                            view.removeChessComponentAtGrid(point);
                            score = score + 10;
                        }
                        if(grid3[i][j]){
                            score = score + 10;
                        }
                    }
                }
            }//消除完毕
            view.repaint();//冯俊铭 !!交换后执行repaint()重绘该组件 避免摇一摇更新
            steps++;
            //swapdelete = false;
            System.out.println("成功执行了交换");
            isnextlevel();//冯俊铭 判断是否进入下一关！！之前没加这个所以换不了关
            nextstep = 1;//fjm !!一旦交换，则将nextstep必须改成1 避免交换后nextstep执行case2方法导致棋子不下落而直接生成新的
            view.repaint();
            supersteps--;//！记得减掉超级步数
            System.out.println("当前剩余超级交换步数："+supersteps);
            return 100;//100表示交换成功
        }else if(model.ismatch()){
            //如果棋盘中还有能消除的棋子则先消除 这里可能会报错，但是无所谓，不影响
            boolean grid2[][] = model.candelete();
            boolean grid3[][] = model.addscore();
            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                    if (grid2[i][j]) {
                        ChessboardPoint point = new ChessboardPoint(i, j);
                        model.removeChessPiece(point);
                        view.removeChessComponentAtGrid(point);
                        score = score + 10;
                    }
                    if(grid3[i][j]){
                        score = score + 10;
                    }
                }
            }
            //消除完毕
            view.repaint();//冯俊铭 !!交换后执行repaint()重绘该组件 避免摇一摇更新
            isnextlevel();//冯俊铭 判断是否进入下一关！！之前没加这个所以换不了关
            System.out.println("交换未执行，消除了棋盘上还可以消除的棋子");
            nextstep = 1;//fjm !!一旦交换，则将nextstep必须改成1 避免交换后nextstep执行case2方法导致棋子不下落而直接生成新的
            return 101;//101表示交换了未交换的棋子
        }else return 102;//表示现在需要选择两个棋子交换

    }


    public boolean isdeadend(){
        boolean result=true;
        /*String[][] stringsgrid=new String[Constant.CHESSBOARD_ROW_SIZE.getNum()+4][Constant.CHESSBOARD_COL_SIZE.getNum()+4];
        for (int i = 2; i <=9 ; i++) {
            for (int j = 2; j <=9 ; j++) {
                stringsgrid[i][j]=model.getGrid()[i-2][j-2].getPiece().getName();
            }
        }
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum()+4; i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum()+4; j++) {
                if (stringsgrid[i][j]==null){
                    stringsgrid[i][j]="0";
                }
            }
        }*/
        //分四个方向
        //向右
        for (int i = 0; i <8 ; i++) {
            for (int j = 0; j <7 ; j++) {
                ChessboardPoint point1=new ChessboardPoint(i,j);
                ChessboardPoint point2=new ChessboardPoint(i,j+1);
                if (model.canSwap(point1, point2)){
                    result=false;
                }
            }
        }
        //向左
        for (int i = 0; i <8 ; i++) {
            for (int j =1; j <8 ; j++) {
                ChessboardPoint point1=new ChessboardPoint(i,j);
                ChessboardPoint point2=new ChessboardPoint(i,j-1);
                if (model.canSwap(point1,point2)){
                    result=false;
                }
            }
        }
        //向下
        for (int i = 0; i <7 ; i++) {
            for (int j = 0; j <8 ; j++) {
                ChessboardPoint point1=new ChessboardPoint(i,j);
                ChessboardPoint point2=new ChessboardPoint(i+1,j);
                if (model.canSwap(point1,point2)){
                    result=false;
                }
            }
        }
        //向上
        for (int i = 1; i <8 ; i++) {
            for (int j = 0; j <8; j++) {
                ChessboardPoint point1=new ChessboardPoint(i,j);
                ChessboardPoint point2=new ChessboardPoint(i-1,j);
                if (model.canSwap(point1,point2)){
                    result=false;
                }
            }
        }
        System.out.println(result);
        return result;
    }
    public void changeSkin(){
        {//为view层执行消除
            for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
                for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                    ChessboardPoint point = new ChessboardPoint(i, j);
                    //model.removeChessPiece(point);
                    view.removeChessComponentAtGrid(point);
                    //score = score + 10;
                }
            }
        }//消除完毕
        view.initiateChessComponent(model);
        view.repaint();
    }
    public void showTipPoints(){
        Cell[][] grid = model.getGrid();
        ChessboardPoint[] tips = findTipPoints();
        CellComponent[][] gridComponents = view.getGridComponents();
        int i1 = tips[0].getRow();
        int j1 = tips[0].getCol();
        int i2 = tips[1].getRow();
        int j2 = tips[1].getCol();

        ChessPiece chessPiece1 = grid[i1][j1].getPiece();
        gridComponents[i1][j1].removeAll();
        ChessComponent chessComponent1 = null;
        chessComponent1 = new ChessComponent(view.getCHESS_SIZE(), chessPiece1,view.getSkin(),1);
        gridComponents[i1][j1].add(chessComponent1);

        ChessPiece chessPiece2 = grid[i2][j2].getPiece();
        gridComponents[i2][j2].removeAll();
        ChessComponent chessComponent2 = null;
        chessComponent2 = new ChessComponent(view.getCHESS_SIZE(), chessPiece2,view.getSkin(),1);
        gridComponents[i2][j2].add(chessComponent2);

        view.repaint();

    }//fjm jym 绘制提示圈
    public ChessboardPoint[] findTipPoints() {
        ChessboardPoint[] tips = new ChessboardPoint[2];
        int row1;
        int col1;
        int row2;
        int col2;
//        ChessboardPoint point1 = new ChessboardPoint(row1, col1);
//        ChessboardPoint point2 = new ChessboardPoint(row2, col2);
        Mean__loop:
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint point = new ChessboardPoint(i, j);//分9个区域
                if ((1 <= i && i <= 6) && (1 <= j && j <= 6)) {
                    ChessboardPoint point1 = new ChessboardPoint(i, j + 1);//向右寻找
                    if (model.canSwap(point, point1)) {
                        tips[0] = point;
                        tips[1] = point1;
                        break Mean__loop;
                        //该点向右寻找
                    }
                    ChessboardPoint point2 = new ChessboardPoint(i, j - 1);//向左寻找
                    if (model.canSwap(point, point2)) {
                        tips[0] = point;
                        tips[1] = point2;
                        break Mean__loop;
                        //向左寻找
                    }
                    ChessboardPoint point3 = new ChessboardPoint(i + 1, j);//向下寻找
                    if (model.canSwap(point, point3)) {
                        tips[0] = point;
                        tips[1] = point3;
                        break Mean__loop;
                        //向下寻找
                    }
                    ChessboardPoint point4 = new ChessboardPoint(i - 1, j);//向上寻找
                    if (model.canSwap(point, point4)) {
                        tips[0] = point;
                        tips[1] = point4;
                        break Mean__loop;
                        //向上寻找
                    }
                }//中间区域
                if ((1 <= j && j <= 6) && (i == 0)) {
                    ChessboardPoint point2 = new ChessboardPoint(i, j - 1);//向左寻找
                    if (model.canSwap(point, point2)) {
                        tips[0] = point;
                        tips[1] = point2;
                        break Mean__loop;
                        //向左寻找
                    }
                    ChessboardPoint point3 = new ChessboardPoint(i + 1, j);//向下寻找
                    if (model.canSwap(point, point3)) {
                        tips[0] = point;
                        tips[1] = point3;
                        break Mean__loop;
                        //向下寻找
                    }
                    ChessboardPoint point1 = new ChessboardPoint(i, j + 1);//向右寻找
                    if (model.canSwap(point, point1)) {
                        tips[0] = point;
                        tips[1] = point1;
                        break Mean__loop;
                        //该点向右寻找
                    }
                }//正上方（左，下，右）
                if ((1 <= j && j <= 6) && (i == 7)) {
                    ChessboardPoint point2 = new ChessboardPoint(i, j - 1);//向左寻找
                    if (model.canSwap(point, point2)) {
                        tips[0] = point;
                        tips[1] = point2;
                        break Mean__loop;
                        //向左寻找
                    }
                    ChessboardPoint point3 = new ChessboardPoint(i + 1, j);//向下寻找
                    if (model.canSwap(point, point3)) {
                        tips[0] = point;
                        tips[1] = point3;
                        break Mean__loop;
                        //向下寻找
                    }
                    ChessboardPoint point1 = new ChessboardPoint(i, j + 1);//向右寻找
                    if (model.canSwap(point, point1)) {
                        tips[0] = point;
                        tips[1] = point1;
                        break Mean__loop;
                        //该点向右寻找
                    }
                }//正下方（左，上，右）
                if ((j==0)&&(1<=i&&i<=6)){
                    ChessboardPoint point4 = new ChessboardPoint(i - 1, j);//向上寻找
                    if (model.canSwap(point, point4)) {
                        tips[0] = point;
                        tips[1] = point4;
                        break Mean__loop;
                        //向上寻找
                    }
                    ChessboardPoint point3 = new ChessboardPoint(i + 1, j);//向下寻找
                    if (model.canSwap(point, point3)) {
                        tips[0] = point;
                        tips[1] = point3;
                        break Mean__loop;
                        //向下寻找
                    }
                    ChessboardPoint point1 = new ChessboardPoint(i, j + 1);//向右寻找
                    if (model.canSwap(point, point1)) {
                        tips[0] = point;
                        tips[1] = point1;
                        break Mean__loop;
                        //该点向右寻找
                    }
                }//左侧（上，下，右）
                if ((j==7)&&(1<=i&&i<=6)){
                    ChessboardPoint point2 = new ChessboardPoint(i, j - 1);//向左寻找
                    if (model.canSwap(point, point2)) {
                        tips[0] = point;
                        tips[1] = point2;
                        break Mean__loop;
                        //向左寻找
                    }
                    ChessboardPoint point4 = new ChessboardPoint(i - 1, j);//向上寻找
                    if (model.canSwap(point, point4)) {
                        tips[0] = point;
                        tips[1] = point4;
                        break Mean__loop;
                        //向上寻找
                    }
                    ChessboardPoint point3 = new ChessboardPoint(i + 1, j);//向下寻找
                    if (model.canSwap(point, point3)) {
                        tips[0] = point;
                        tips[1] = point3;
                        break Mean__loop;
                        //向下寻找
                    }
                }//右侧（左，上，下）
                if (i==0&&j==0){
                    ChessboardPoint point3 = new ChessboardPoint(i + 1, j);//向下寻找
                    if (model.canSwap(point, point3)) {
                        tips[0] = point;
                        tips[1] = point3;
                        break Mean__loop;
                        //向下寻找
                    }
                    ChessboardPoint point1 = new ChessboardPoint(i, j + 1);//向右寻找
                    if (model.canSwap(point, point1)) {
                        tips[0] = point;
                        tips[1] = point1;
                        break Mean__loop;
                        //该点向右寻找
                    }
                }//左上（右，下）
                if (i==0&&j==7){
                    ChessboardPoint point2 = new ChessboardPoint(i, j - 1);//向左寻找
                    if (model.canSwap(point, point2)) {
                        tips[0] = point;
                        tips[1] = point2;
                        break Mean__loop;
                        //向左寻找
                    }
                    ChessboardPoint point3 = new ChessboardPoint(i + 1, j);//向下寻找
                    if (model.canSwap(point, point3)) {
                        tips[0] = point;
                        tips[1] = point3;
                        break Mean__loop;
                        //向下寻找
                    }
                }//右上（左，下）
                if (i==7&&j==0){
                    ChessboardPoint point4 = new ChessboardPoint(i - 1, j);//向上寻找
                    if (model.canSwap(point, point4)) {
                        tips[0] = point;
                        tips[1] = point4;
                        break Mean__loop;
                        //向上寻找
                    }
                    ChessboardPoint point1 = new ChessboardPoint(i, j + 1);//向右寻找
                    if (model.canSwap(point, point1)) {
                        tips[0] = point;
                        tips[1] = point1;
                        break Mean__loop;
                        //该点向右寻找
                    }
                }//左下（上，右）
                if (i==7&&j==7){
                    ChessboardPoint point4 = new ChessboardPoint(i - 1, j);//向上寻找
                    if (model.canSwap(point, point4)) {
                        tips[0] = point;
                        tips[1] = point4;
                        break Mean__loop;
                        //向上寻找
                    }
                    ChessboardPoint point2 = new ChessboardPoint(i, j - 1);//向左寻找
                    if (model.canSwap(point, point2)) {
                        tips[0] = point;
                        tips[1] = point2;
                        break Mean__loop;
                        //向左寻找
                    }
                }//右下（上，左）
            }
        }
        return tips;
    }//江易明 寻找提示点

    public void setNextstep(int nextstep) {
        this.nextstep = nextstep;
    }

    public boolean isCanUseProp() {
        return canUseProp;
    }

    public void setCanUseProp(boolean canUseProp) {
        this.canUseProp = canUseProp;
    }


}