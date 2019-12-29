package Game.Logic.Neighborhood;

import Game.Logic.CellMap;
import javafx.scene.paint.Color;

import static Game.Logic.CellMap.map;

public class Neumann implements Neighborhood {
    @Override
    public void neighbours(int i, int j) {
        Color c=(Color) CellMap.mapArray[i][j].cell.getFill();
        for(int k=i-1;k<i+2;k++) {
            for(int m=j-1;m<j+2;m++) {
                if((k==i)&&(m==j)) continue;
                else if ((k==i)||(m==j))map(k,m,c);

            }
        }
    }
}
