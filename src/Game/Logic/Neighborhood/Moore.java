package Game.Logic.Neighborhood;

import Game.Logic.CellMap;
import javafx.scene.paint.Color;

import static Game.Logic.CellMap.map;
import static Game.Logic.CellMap.mapArray;

public class Moore implements Neighborhood {
    @Override
    public void neighbours(int i,int j) {

        Color c=(Color) CellMap.mapArray[i][j].cell.getFill();
        Color black=Color.color(0,0,0);
        if ((!c.equals(black))&&(!mapArray[i][j].isLocked())) {
            for (int k = i - 1; k < i + 2; k++) {
                for (int m = j - 1; m < j + 2; m++) {
                    if ((k == i) && (m == j)) continue;
                    else map(k, m, c);

                }
            }
        }



    }


}
