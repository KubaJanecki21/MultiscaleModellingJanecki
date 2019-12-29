package Game;

import Game.Logic.CellMap;
import Game.Logic.Randomize.*;
import Game.Logic.Neighborhood.*;
import javafx.scene.Group;


import java.io.File;

import static Game.Logic.CellMap.mapArray;
import static Game.Logic.CellMap.range;
import static Game.Main.listView;

public class Controller {

    Group root;
    CellMap cells;


    Randomize randSeed=new Random();


    public Controller(Group root, CellMap cells){
        this.root=root;
        this.cells=cells;
    }

    public void handleStart(){
        Main.pause=false;
        //cells.stepAll();
        for(int i=0;i<range;i++){
            for(int j=0;j<range;j++){
                if(mapArray[i][j].isAlive) Main.begin++;
            }
        }
        Runnable r=new MyThread(cells);
        new Thread(r).start();

        System.out.println("ended");
    }

    public void handleClearStructure(){
        cells.clearStructure();
    }

    public void handleSetBorders(){
        cells.setBorders();
    }

    public void handleRand(int i){
        randSeed.strategy(i);
    }

    public void handleClr(){
        /*for(int i=0;i<range;i++){
            for(int j=0;j<range;j++){
                CellMap.mapArray[i][j].Kill();
                CellMap.mapArray[i][j].c=Color.color(1,1,1);
                CellMap.mapArray[i][j].change=false;
                Main.pause=true;
                Main.begin=0;
                CellMap.aliveCounter=0;
            }
        }*/

        listView.getItems().clear();
    }

    public void handleNeighbours(int index){
        switch (index){
            case 0:
                cells.setNeighbourhood(new Moore());
                break;
            case 1:
                cells.setNeighbourhood(new Neumann());
                break;
            case 2:
                cells.setNeighbourhood(new HexagonalLeft());
                break;
            case 3:
                cells.setNeighbourhood(new HexagonalRight());
                break;
            case 4:
                cells.setNeighbourhood(new HexagonalRand());
                break;
            case 5:
                cells.setNeighbourhood(new PentagonalRand());
                break;
        }
    }


    public void handleClearInsideBorders() {

        cells.clearInsideBorders();
    }

    public void handleExport(File target) {
        if (target.getName().contains("txt")){
            cells.exportToTxt(target);
        } else if (target.getName().contains("bmp")){
            cells.exportToBmp(target);
        }
    }

    public void handleImportFromTxt(File target) {
        cells.importFromTxt(target);
    }

    public void handleImportFromBmp(File target) {
        cells.importFromBmp(target);
    }
}
