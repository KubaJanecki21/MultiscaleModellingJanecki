package Game.Logic.Neighborhood;

import Game.Logic.CellMap;
import javafx.scene.paint.Color;

import java.util.Random;

import static Game.Logic.CellMap.map;


public class PentagonalRand implements Neighborhood {
    @Override
    public void neighbours(int i, int j) {
        Random rand=new Random();

        Color c=(Color) CellMap.mapArray[i][j].cell.getFill();
        for(int k=i-1;k<i+2;k++) {
            int var=rand.nextInt(4);
            for(int m=j-1;m<j+2;m++) {
                switch (var){
                    case 1:
                        if(m==j-1) continue;
                        break;
                    case 2:
                        if(m==j+1) continue;
                        break;
                    case 3:
                        if(k==i-1) continue;
                        break;
                    case 4:
                        if(k==i+1) continue;
                        break;
                }

                map(k,m,c);

            }
        }
    }
}
