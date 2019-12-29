package Game.Logic.Neighborhood;

import java.util.Random;

public class HexagonalRand implements Neighborhood{
    @Override
    public void neighbours(int i, int j) {
        Random rand=new Random();
        if(rand.nextBoolean()==true){
            HexagonalLeft hL=new HexagonalLeft();
            hL.neighbours(i,j);
        } else {
            HexagonalRight hR=new HexagonalRight();
            hR.neighbours(i,j);
        }
    }
}
