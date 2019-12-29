package Game.Logic.Neighborhood;

import javafx.scene.paint.Color;

import static Game.Logic.CellMap.map;
import static Game.Logic.CellMap.mapArray;

public class HexagonalRight implements Neighborhood {
    @Override
    public void neighbours(int i, int j) {
        Color c=(Color) mapArray[i][j].cell.getFill();
        for(int k=i-1;k<i+2;k++) {
            for(int m=j-1;m<j+2;m++) {
                if(((k==i)&&(m==j))||((k==i-1)&&(m==j-1))||((k==i+1)&&(m==j+1))) continue;
                else map(k,m,c);

            }
        }
    }
}