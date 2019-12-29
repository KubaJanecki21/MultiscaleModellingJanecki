package Game.Logic;

import Game.Logic.Neighborhood.*;
import Game.Main;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static Game.Main.PERIODIC;
import static Game.Main.editThreshold;
import static Game.Main.listView;
import static java.lang.Math.pow;


public class CellMap {
    long start_time=0;

    public static int aliveCounter=0;
    short x_start=500;
    short y_start=10;

    public static int range=(Main.WIDTH-20)/10;

    public Group cellMap;

    public static String inclusionVariant="single Random";
    public static String inclusionShape="Circle";
    public static String structure="Substructure";

    private Neighborhood strategy=(Neighborhood)new PentagonalRand();
    public static Cell mapArray[][];
    public static String borders="Complete";

    public CellMap(){
        cellMap=new Group();
        mapArray =new Cell[range][range];

        for(short i=0;i<range;i++){
            for(short j=0;j<range;j++){
                mapArray[i][j]=new Cell(x_start+10*j,y_start+10*i,i*range+j);

                cellMap.getChildren().add(mapArray[i][j].cell);
            }
        }

    }



    public void stepAll(){
        start_time = System.nanoTime();

        for(short i=0;i<range;i++){
            for(short j=0;j<range;j++){
                if(mapArray[i][j].isAlive) strategy.neighbours(i,j);
            }
        }
        stepFinish();



    }

    public void stepFinish(){
        for(short i=0;i<range;i++){
            for(short j=0;j<range;j++){

                if(mapArray[i][j].change==true){
                    mapArray[i][j].setAlive();
                    mapArray[i][j].cell.setFill(mapArray[i][j].c);
                    aliveCounter++;
                    mapArray[i][j].change=false;

                    }
            }
        }

        if(aliveCounter==range*range-Main.begin){

            Main.pause=true;
            long end_time = System.nanoTime();

            long difference = end_time-start_time;
            System.out.println("Koniec, upłyneło: "+difference+" milisekund");



            if (inclusionVariant.equals("multiple shaped")) {

                LinkedList<CellCoordinates> borderMap=new LinkedList<>();

                for(short i=0;i<range;i++){
                    for(short j=0;j<range;j++){
                        if(checkIfOnBorder(i,j)) {
                            borderMap.add(new CellCoordinates(i,j,(Color)mapArray[i][j].cell.getFill()));
                            //mapArray[i][j].cell.setFill(Color.color(0, 0, 0));
                            //mapArray[i][j].setColor(Color.color(0, 0, 0));
                        }
                    }
                }
                System.out.println("border created");

                int radius=Integer.parseInt(Main.editRadius.getText());
                int amount=Integer.parseInt(Main.editAreaAmount.getText());
                for (short i = 0; i < amount; i++) {
                    java.util.Random rand = new java.util.Random();
                    int index = rand.nextInt(borderMap.size());
                    CellCoordinates inclusion = borderMap.get(index);


                    for (int k = inclusion.x - radius; k < inclusion.x + 1 + radius; k++) {
                        for (int m = inclusion.y - radius; m < inclusion.y + 1 + radius; m++) {
                            // if ((k == inclusion.x) && (m == inclusion.y)) continue;
                            if ((k < 0) || (k > range - 1) || (m < 0) || (m > range - 1)) continue;
                            if (inclusionShape.equals("Circle")) {
                                if (((k - inclusion.x) * (k - inclusion.x) + (m - inclusion.y) * (m - inclusion.y)) > radius * radius)
                                    continue;
                            }
                            mapArray[k][m].cell.setFill(Color.color(0, 0, 0));
                            mapArray[k][m].setColor(Color.color(0, 0, 0));

                        }
                    }

                    //mapArray[inclusion.x][inclusion.y].cell.setFill(Color.color(0,0,0));
                }

            }


            return;
        }

    }

    public void clearStructure(){
        List<String> colors = listView.getItems();
        if (colors.size()==0){
            for(int i=0;i<range;i++){
                for(int j=0;j<range;j++){

                    mapArray[i][j].Kill();
                    mapArray[i][j].c=Color.color(1,1,1);
                    mapArray[i][j].change=false;
                    Main.pause=true;
                    Main.begin=0;
                    --aliveCounter;
                    mapArray[i][j].unlock();

                }
            }
        } else {

            for (int i = 0; i < range; i++) {
                for (int j = 0; j < range; j++) {

                    final int a = i;
                    final int b = j;

                    boolean isOnList=colors.stream().anyMatch(color -> color.contains(mapArray[a][b].c.toString()));
                    //colors.stream().filter().collect(Collectors.toList());
                    if (!mapArray[a][b].c.equals(Color.color(0,0,0))) {
                        if (!isOnList) {
                            mapArray[a][b].Kill();
                            mapArray[a][b].c = Color.color(1, 1, 1);
                            mapArray[a][b].change = false;
                            Main.pause = true;
                            Main.begin = 0;
                            --aliveCounter;
                            mapArray[a][b].unlock();
                        } else {
                            mapArray[a][b].lock();
                            if (structure.equals("Dual Phase")) {
                                mapArray[a][b].c = Color.PURPLE;
                                mapArray[a][b].cell.setFill(Color.PURPLE);
                            }
                        }
                    }

                }
            }
        }
    }


    public boolean checkIfOnBorder(int i, int j){
        Color central=(Color) mapArray[i][j].cell.getFill();
        for (int k = i - 1; k < i + 2; k++) {
            for (int m = j; m < j + 2; m++) {
                if ((k == i) && (m == j)) continue;
                if((k<0)||(m<0)||(k>range-1)) {
                    continue;
                }
                if (m>range-1){
                    int n=m-1;
                    Color neighbour=(Color) mapArray[k][n].cell.getFill();
                    if (!central.equals(neighbour)&&(!neighbour.equals(Color.color(0,0,0)))) {
                        return true;
                    }
                } else {
                    Color neighbour = (Color) mapArray[k][m].cell.getFill();
                    if (!central.equals(neighbour) && (!neighbour.equals(Color.color(0, 0, 0)))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static boolean rule1(int i, int j,Color c){
        HashMap<Color,Integer> colors=new HashMap<>();
        int it=0;
        int k,m;
        for(int a=i-1;a<i+2;a++) {
            for(int b=j-1;b<j+2;b++) {
                k=a;
                m=b;
                if(PERIODIC){

                    if(a<0) {
                        k=range+a;
                    }
                    if(b<0){
                        m=range+b;
                    }

                    if(a>range-1) {
                        k=a-range;
                    }
                    if(b>range-1) {
                        m=b-range;
                    }
                }
                else if(((k==i)&&(m==j))||((k<0)||(k>range-1)||(m<0)||(m>range-1))) continue;

                if (!mapArray[k][m].isLocked()) {
                    if (colors.get(mapArray[k][m].c) == null) {
                        colors.put(mapArray[k][m].c, 1);
                    } else {
                        colors.put(mapArray[k][m].c, colors.get(mapArray[k][m].c) + 1);
                    }
                }


            }
        }
        Iterator<Map.Entry<Color, Integer>> entries = colors.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Color, Integer> entry = entries.next();
            String colorString=entry.getKey().toString();
            if (colorString.equals("0xffffffff")|| colorString.equals("0x000000ff")||(mapArray[i][j].isLocked())) {
                continue;
            } else if (entry.getValue()>=5){
                System.out.println("Rule 1");
                mapArray[i][j].change=true;
                mapArray[i][j].c=entry.getKey();
                return true;
            }
        }
        return false;
    }



    public static boolean rule2(int i, int j,Color c){
        HashMap<Color,Integer> colors=new HashMap<>();
        int it=0;
        int k,m;
        for(int a=i-1;a<i+2;a++) {
            for(int b=j-1;b<j+2;b++) {
                if ((a==i-1)&&(b==j-1)) continue;
                if ((a==i-1)&&(b==j+1)) continue;
                if ((a==i+1)&&(b==j-1)) continue;
                if ((a==i+1)&&(b==j+1)) continue;
                k=a;
                m=b;
                if(PERIODIC){

                    if(a<0) {
                        k=range+a;
                    }
                    if(b<0){
                        m=range+b;
                    }

                    if(a>range-1) {
                        k=a-range;
                    }
                    if(b>range-1) {
                        m=b-range;
                    }
                }
                else if(((k==i)&&(m==j))||((k<0)||(k>range-1)||(m<0)||(m>range-1))) continue;

                if (colors.get(mapArray[k][m].c)==null){
                    colors.put(mapArray[k][m].c,1);
                } else {
                    colors.put(mapArray[k][m].c,colors.get(mapArray[k][m].c)+1);
                }


            }
        }
        Iterator<Map.Entry<Color, Integer>> entries = colors.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Color, Integer> entry = entries.next();
            String colorString=entry.getKey().toString();
            if (colorString.equals("0xffffffff")|| colorString.equals("0x000000ff")||(mapArray[i][j].isLocked())) {
                continue;
            }
            else if (entry.getValue()>=3){
                System.out.println("Rule 2");
                mapArray[i][j].change=true;
                mapArray[i][j].c=entry.getKey();
                return true;

            }
        }
        return false;
    }



    public static boolean rule3(int i, int j,Color c){
        HashMap<Color,Integer> colors=new HashMap<>();
        int it=0;
        int k,m;
        for(int a=i-1;a<i+2;a++) {
            for(int b=j-1;b<j+2;b++) {
                if (a==i) continue;
                if (b==j) continue;
                k=a;
                m=b;
                if(PERIODIC){

                    if(a<0) {
                        k=range+a;
                    }
                    if(b<0){
                        m=range+b;
                    }

                    if(a>range-1) {
                        k=a-range;
                    }
                    if(b>range-1) {
                        m=b-range;
                    }
                }
                else if(((k==i)&&(m==j))||((k<0)||(k>range-1)||(m<0)||(m>range-1))) continue;

                if (colors.get(mapArray[k][m].c)==null){
                    colors.put(mapArray[k][m].c,1);
                } else {
                    colors.put(mapArray[k][m].c,colors.get(mapArray[k][m].c)+1);
                }


            }
        }
        Iterator<Map.Entry<Color, Integer>> entries = colors.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Color, Integer> entry = entries.next();
            String colorString=entry.getKey().toString();
            if (colorString.equals("0xffffffff")|| colorString.equals("0x000000ff")||(mapArray[i][j].isLocked())){
                continue;
            }
            else if (entry.getValue()>=3){
                System.out.println("Rule 3");
                mapArray[i][j].change=true;
                mapArray[i][j].c=entry.getKey();
                return true;

            }
        }
        return false;
    }


    public static boolean rule4(int i, int j,Color c){
        HashMap<Color,Integer> colors=new HashMap<>();
        int k,m;
        Random generator = new Random();
        double acceptanceLevel=Double.parseDouble(editThreshold.getText());
        int threshold=generator.nextInt(100);
        //System.out.println("threshold: "+threshold );
        for(int a=i-1;a<i+2;a++) {
            for(int b=j-1;b<j+2;b++) {
                k=a;
                m=b;
                if(PERIODIC) {

                    if (a < 0) {
                        k = range + a;
                    }
                    if (b < 0) {
                        m = range + b;
                    }

                    if (a > range - 1) {
                        k = a - range;
                    }
                    if (b > range - 1) {
                        m = b - range;
                    }

                }else if(((k==i)&&(m==j))||((k<0)||(k>range-1)||(m<0)||(m>range-1))) continue;
                if (!mapArray[k][m].isLocked()) {
                    if (colors.get(mapArray[k][m].c) == null) {
                        colors.put(mapArray[k][m].c, 1);
                    } else {

                        colors.put(mapArray[k][m].c, colors.get(mapArray[k][m].c) + 1);

                    }
                }


            }
        }
        Iterator<Map.Entry<Color, Integer>> entries = colors.entrySet().iterator();
        Map.Entry<Color, Integer> entryMax=null;

        for (Map.Entry<Color, Integer> entry : colors.entrySet())
        {
            String colorString=entry.getKey().toString();
            if (colorString.equals("0xffffffff")|| colorString.equals("0x000000ff")||(mapArray[i][j].isLocked())){
                continue;
            }
            if (entryMax == null || entry.getValue().compareTo(entryMax.getValue()) > 0)
            {
                entryMax = entry;
            }
        }

        if (entryMax!=null) {
            double percentageLevel = (double)threshold / 100.0;
            if (percentageLevel <= (acceptanceLevel)/100) {
                System.out.println("Rule 4");
                mapArray[i][j].change = true;
                mapArray[i][j].c = entryMax.getKey();
                return true;

            }
        }

        return false;
    }


    public void setNeighbourhood(Neighborhood n){
        strategy=n;
    }

    public static void map(int i, int j,Color c){
        int k=i,m=j;

        if(PERIODIC){
           if(i<0) {
               k=range+i;
           }
           if(j<0) m=range+j;

           if(i>range-1) k=i-range;
           if(j>range-1) m=j-range;
            if(mapArray[k][m].isAlive) return;

            if (rule1(k,m,c)) return;

            if (rule2(k,m,c)) return;

            if (rule3(k,m,c)) return;

            if (rule4(k,m,c)) return;

        } else {

            if((i<0)||(i>range-1)||(j<0)||(j>range-1)) {
                return;
            }

            if(mapArray[i][j].isAlive)return;

            if (rule1(i,j,c)) return;

            if (rule2(i,j,c)) return;

            if (rule3(i,j,c)) return;

            if (rule4(i,j,c)) return;

        }
    }


    public void setBorders() {

        LinkedList<CellCoordinates> borderMap=new LinkedList<>();

        if (borders.equals("Complete")) {
            for (short i = 0; i < range; i++) {
                for (short j = 0; j < range; j++) {
                    if (checkIfOnBorder(i, j)) {
                        borderMap.add(new CellCoordinates(i, j, (Color) mapArray[i][j].cell.getFill()));
                        mapArray[i][j].cell.setFill(Color.color(0, 0, 0));
                        mapArray[i][j].setColor(Color.color(0, 0, 0));
                    }
                }
            }
        } else {
            List<String> colors = listView.getItems();


            for (short i = 0; i < range; i++) {
                for (short j = 0; j < range; j++) {
                    final short a=i;
                    final short b=j;
                    boolean isOnList=colors.stream().anyMatch(color -> color.contains(mapArray[a][b].c.toString()));
                    boolean isNextOnList=false;
                    if (b<range-1){
                        isNextOnList=colors.stream().anyMatch(color -> color.contains(mapArray[a][b+1].c.toString()));
                    }
                    if (isOnList||isNextOnList) {
                        if (checkIfOnBorder(a, b)) {
                            borderMap.add(new CellCoordinates(a, b, (Color) mapArray[a][b].cell.getFill()));
                            mapArray[a][b].cell.setFill(Color.color(0, 0, 0));
                            mapArray[a][b].setColor(Color.color(0, 0, 0));
                        }
                    }
                }
            }
        }




        System.out.println("border initialized");

    }

    public void clearInsideBorders() {
        int countDeleted=0;
        for(int i=0;i<range;i++){
            for(int j=0;j<range;j++){
                if(!mapArray[i][j].cell.getFill().equals(Color.color(0,0,0))) {
                    ++countDeleted;
                    mapArray[i][j].Kill();
                    mapArray[i][j].c = Color.color(1, 1, 1);
                    mapArray[i][j].change = false;
                    Main.pause = true;
                    Main.begin = 0;
                    aliveCounter--;
                }
            }
        }
        DecimalFormat dec = new DecimalFormat("#0.00");
        double percentage= ( countDeleted / pow(range,2) ) * 100;
        JOptionPane.showMessageDialog(null, "Percentage of cleared grains: "+ dec.format(percentage));

    }

    public void exportToTxt(File target) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(target);
            writer.write(range+"\n");
            for(int i=0;i<range;i++){
                for(int j=0;j<range;j++){

                    writer.write(i+"-"+j+"-"+mapArray[i][j].c+"\n");

                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void exportToBmp(File target) {


        BufferedImage bufferedImage = new BufferedImage(range*5,range*5,BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < range*5; x++) {
            for (int y = 0; y < range*5; y++) {
               // int red = (rgb >> 16) & 0xFF;
              //  int green = (rgb >> 8) & 0xFF;
               // int blue = rgb & 0xFF;
               // int[] rgb_value={(int)mapArray[x][y].c.getRed(),(int)mapArray[x][y].c.getGreen(),(int)mapArray[x][y].c.getBlue()};
               // bufferedImage.setRGB(x,y,1,1,rgb_value,0,0);
                int colorX=(x-(x%5))/5;
                int colorY=(y-(y%5))/5;
                double redD=mapArray[colorX][colorY].c.getRed()*255;
                double greenD=mapArray[colorX][colorY].c.getGreen()*255;
                double blueD=mapArray[colorX][colorY].c.getBlue()*255;
                int redInt= (int) redD;
                int greenInt= (int) greenD;
                int blueInt= (int) blueD;

                int rgb = (redInt << 16 | greenInt << 8 | blueInt);
                bufferedImage.setRGB(y,x,rgb);
               // int rgb = new Color(mapArray[x][y].c.getRed(), mapArray[x][y].c.getGreen(), mapArray[x][y].c.getBlue()).getRGB();

            }
        }

        try {
            ImageIO.write(bufferedImage, "bmp", target);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void importFromBmp(File target) {


        try {
            BufferedImage bufferedImage = ImageIO.read(target);
            int width=bufferedImage.getWidth();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < width; y++) {
                    int colorY=(x-(x%5))/5;
                    int colorX=(y-(y%5))/5;
                    int rgb=bufferedImage.getRGB(x,y);
                    int red = (rgb >> 16) & 0x000000FF;
                    int green = (rgb >>8 ) & 0x000000FF;
                    int blue = (rgb) & 0x000000FF;
                    double redDouble=red/255.0;
                    double greenDouble=green/255.0;
                    double blueDouble=blue/255.0;
                    Color color=Color.color(redDouble,greenDouble,blueDouble);
                    mapArray[colorX][colorY].c=color;
                    mapArray[colorX][colorY].cell.setFill(color);
                    aliveCounter++;
                    mapArray[colorX][colorY].setAlive();


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void importFromTxt(File target) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(target));

            String st;
            String rangeStr = br.readLine();
            while ((st = br.readLine()) != null) {
                String[] parts=st.split("-");
                int i=Integer.parseInt(parts[0]);
                int j=Integer.parseInt(parts[1]);
                Color color= Color.valueOf(parts[2]);
                mapArray[i][j].c=color;
                mapArray[i][j].cell.setFill(color);
                aliveCounter++;
                mapArray[i][j].setAlive();

            }


        br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
