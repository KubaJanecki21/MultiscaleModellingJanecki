package Game.Logic;

import javafx.scene.paint.Color;


public class CellCoordinates{
    int x;
    int y;
    Color color;

    public CellCoordinates(short i, short j, Color fill) {
        this.x = i;
        this.y = j;
        this.color = fill;
    }
}
