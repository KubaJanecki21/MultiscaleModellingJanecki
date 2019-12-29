package Game.Logic;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.text.DecimalFormat;

import static Game.Logic.CellMap.mapArray;
import static Game.Logic.CellMap.range;
import static Game.Main.listView;
import static java.lang.Math.pow;


public class Cell {


    public boolean locked;

    public int id;

    public boolean change;

    public Rectangle cell;

    public boolean isAlive;

    public Color getC() {
        return c;
    }

    public Color c;



    public Cell(float x, float y, int id){


        cell=new Rectangle();
        cell.setX(x);
        cell.setY(y);
        cell.setWidth(10);
        cell.setHeight(10);
        cell.setFill(Color.WHITE);
        cell.setStroke(Color.BLUE);
        c=Color.WHITE;

        this.id=id;
        change=false;
        Kill();

        cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                    if(isAlive) {
                        Color current=(Color)cell.getFill();
                        Integer count=0;
                        //ObservableList<String> items = listView.getItems();
                        for(short i=0;i<range;i++) {
                            for (short j = 0; j < range; j++) {
                                if(mapArray[i][j].cell.getFill().equals(current)){
                                    ++count;
                                }
                            }
                        }
                        Double percentage=((double)count / (pow(range,2)))*100;
                        String temp=percentage.toString();
                        DecimalFormat dec = new DecimalFormat("#0.00");

                        listView.getItems().add(current.toString()+" - "+count+" - "+dec.format(percentage));
                    }
            }
        });

    }


    public void setAlive(){
        isAlive=true;
    }

    public void setColor(Color c){
        this.c=c;
    }

    public void Kill(){
        isAlive=false;
        cell.setFill(Color.WHITE);
    }

    public boolean isLocked(){
        return locked;
    }

    public void lock(){
        locked=true;
    }

    public void unlock(){
        locked=false;
    }

}
