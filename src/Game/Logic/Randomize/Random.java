package Game.Logic.Randomize;

import Game.Main;
import Game.Main.*;
import javafx.scene.paint.Color;

import java.io.File;

import static Game.Logic.CellMap.*;


public class Random extends Randomize {


    @Override
    public void strategy(int n) {
        for(short i=0;i<n;i++){
            java.util.Random rand=new java.util.Random();
            int index=rand.nextInt(range*range);
            if(mapArray[index%range][index/range].getC().equals(Color.color(1,1,1))) {
                Color c = Color.color(Math.random(), Math.random(), Math.random());
                mapArray[index % range][index / range].cell.setFill(c);
                mapArray[index % range][index / range].setAlive();
                mapArray[index % range][index / range].setColor(c);
            }
        }




       // String selectedInclusion=(String)Main.comboInclusions.getValue();
        if (inclusionVariant.equals("single Random")) {
            int amount= Integer.parseInt(Main.editAreaAmount.getText()) ;

            for (short i = 0; i < amount; i++) {
                java.util.Random rand = new java.util.Random();
                int index = rand.nextInt(range * range);
                if(mapArray[index%range][index/range].getC().equals(Color.color(1,1,1))) {
                    Color black = Color.color(0, 0, 0);
                    mapArray[index % range][index / range].cell.setFill(black);
                    mapArray[index % range][index / range].setAlive();
                    mapArray[index % range][index / range].setColor(black);
                }
            }
        }

        exportToBmp(new File("lastRandom.bmp"));

    }
}
