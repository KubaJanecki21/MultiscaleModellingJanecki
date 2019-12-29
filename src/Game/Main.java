package Game;

import Game.Logic.CellMap;
import Game.Logic.Neighborhood.Moore;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.TimeUnit;


class MyThread implements Runnable {

    CellMap map;
    public MyThread(CellMap map) {
        this.map=map;
    }



    public void run() {

        while(true){
            if(!Main.pause) {
                map.stepAll();
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}


public class Main extends Application{

    public static ListView<String> listView;
    public static TextArea editAreaAmount;
    public static TextArea editRadius;
    public static TextArea editThreshold;
    public static TextArea editNucelus;
    public static int WIDTH;
    public static int HEIGHT;
    public static int begin=0;
    public static boolean PERIODIC;

    public static boolean pause=false;

    TextField randomField;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root=new Group();

        String path = (String) JOptionPane.showInputDialog(null, "Set initial size",
                "Set initial size", JOptionPane.QUESTION_MESSAGE,null,null,"400");
        WIDTH=Integer.parseInt(path);
        HEIGHT=WIDTH;


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        CellMap cells=new CellMap();
        root.getChildren().add(cells.cellMap);

        Scene scene=new Scene(root, WIDTH+485, HEIGHT,Color.AZURE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        generateGui(root,cells,primaryStage);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


    public void importFromSource(File target, Stage primaryStage, Group root, String format) {
        BufferedReader br = null;
        try {

            if (format.equals("txt")) {
                br = new BufferedReader(new FileReader(target));

                String st;
                String rangeStr = br.readLine();
                br.close();

                int rangeImported = Integer.parseInt(rangeStr);
                int WIDTH_IMPORTED = rangeImported * 10 + 20;
                WIDTH = WIDTH_IMPORTED;
                HEIGHT = WIDTH;
                CellMap.range = rangeImported;
            } else if (format.equals("bmp")){
                BufferedImage bufferedImage = ImageIO.read(target);
                int width=bufferedImage.getWidth();
                int rangeImported=width/5;
                int WIDTH_IMPORTED = rangeImported * 10 + 20;
                WIDTH = WIDTH_IMPORTED;
                HEIGHT = WIDTH;
                CellMap.range = rangeImported;
            }


            root.getChildren().clear();
            CellMap cells=new CellMap();
            root.getChildren().add(cells.cellMap);
            primaryStage.setWidth(WIDTH+500);
            primaryStage.setHeight(HEIGHT+40);
            primaryStage.setResizable(false);
            generateGui(root,cells,primaryStage);
            primaryStage.show();

            Controller controller=new Controller(root,cells);

            if (format.equals("txt")) controller.handleImportFromTxt(target);
            else if (format.equals("bmp")) controller.handleImportFromBmp(target);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void generateGui(Group root,CellMap cells,Stage primaryStage){

        Controller controller=new Controller(root,cells);

        ObservableList<String> opcje2 =
                FXCollections.observableArrayList(
                        "Circle",
                        "Square"
                );

        final ComboBox comboInclusionShape = new ComboBox(opcje2);

        comboInclusionShape.setOnAction((event) -> {
            cells.inclusionShape = comboInclusionShape.getSelectionModel().getSelectedItem().toString();
        });
        comboInclusionShape.getSelectionModel().select(0);


        ObservableList<String> borders =
                FXCollections.observableArrayList(
                        "Complete",
                        "Chosen"
                );
        final ComboBox comboBorders = new ComboBox(borders);
        comboBorders.setOnAction((event) -> {
            cells.borders = comboBorders.getSelectionModel().getSelectedItem().toString();
        });
        comboBorders.getSelectionModel().select(0);




        ObservableList<String> structures =
                FXCollections.observableArrayList(
                        "Substructure",
                        "Dual Phase"
                );
        final ComboBox comboStructures = new ComboBox(structures);
        comboStructures.setOnAction((event) -> {
            cells.structure = comboStructures.getSelectionModel().getSelectedItem().toString();
        });
        comboStructures.getSelectionModel().select(0);


        ObservableList<String> opcje3 =
                FXCollections.observableArrayList(
                        "single Random",
                        "multiple shaped"
                );
        final ComboBox comboInclusionsVariant = new ComboBox(opcje3);
        comboInclusionsVariant.setOnAction((event) -> {
            cells.inclusionVariant = comboInclusionsVariant.getSelectionModel().getSelectedItem().toString();
        });

        comboInclusionsVariant.getSelectionModel().select(0);


        Button startBtn = new Button();
        startBtn.setText("Start");
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleStart();

            }
        });


        Button clearInsideBorders = new Button();
        clearInsideBorders.setText("clear inside Broders");
        clearInsideBorders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleClearInsideBorders();

            }
        });



        Button randBtn = new Button();
        randBtn.setText("Randomize");

        randomField=new TextField();
        randomField.setMaxWidth(40);
        randomField.setText("10");

        randBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                int nucleusAmount=Integer.parseInt(editNucelus.getText());
                controller.handleRand(nucleusAmount);


            }
        });

        Button setBorders = new Button();
        setBorders.setText("set Borders");
        setBorders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleSetBorders();
            }
        });

        Button stopBtn = new Button();
        stopBtn.setText("Stop");
        stopBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pause=true;
            }
        });

        Button clrStructure = new Button();
        clrStructure.setText("Clear Structure");
        clrStructure.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleClearStructure();
            }
        });

        Button clrBtn = new Button();
        clrBtn.setText("Clear list");
        clrBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                controller.handleClr();

            }
        });

        Button exportButton = new Button();
        exportButton.setText("Export");
        exportButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showSaveDialog(primaryStage);
                if (selectedFile.getName().contains(".txt")||selectedFile.getName().contains(".bmp")) System.out.println("proper extension selected");
                else System.out.println("Select supported file extension");

                controller.handleExport(selectedFile);

            }
        });

        Button importButton = new Button();
        importButton.setText("Import");
        importButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                if (selectedFile.getName().contains(".txt")) importFromSource(selectedFile,primaryStage,root,"txt");
                else if (selectedFile.getName().contains(".bmp")) importFromSource(selectedFile,primaryStage,root,"bmp");

            }
        });

        editRadius = new TextArea();
        editRadius.setText("2");
        editRadius.setPrefSize(20,5);

        editAreaAmount = new TextArea();
        editAreaAmount.setText("5");
        editAreaAmount.setPrefSize(20,5);

        editThreshold = new TextArea();
        editThreshold.setText("40");
        editThreshold.setPrefSize(20,5);

        editNucelus = new TextArea();
        editNucelus.setText("10");
        editNucelus.setPrefSize(20,5);

        ObservableList<String> opcje1 =
                FXCollections.observableArrayList(
                        "Moore",
                        "von Neumann",
                        "Hexagonal Left",
                        "Hexagonal Right",
                        "Hexagonal Rand",
                        "Pentagonal Rand"
                );

        final ComboBox comboNeighbours = new ComboBox(opcje1);
        comboNeighbours.getSelectionModel().select(0);
        cells.setNeighbourhood(new Moore());

        comboNeighbours.setOnAction((event) -> {
            int indexN = comboNeighbours.getSelectionModel().getSelectedIndex();
            controller.handleNeighbours(indexN);

        });

        listView  = new ListView<String>();

        listView.setPrefSize(50, 150);
        listView.setEditable(true);


        CheckBox checkPeriodic=new CheckBox();

        checkPeriodic.setOnAction((event) -> {
            if(checkPeriodic.isSelected()) PERIODIC=true;
            else PERIODIC=false;

        });



        GridPane grid = new GridPane();

        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(20, 25, 25, 20));
        grid.add(startBtn,0,0,1,2);
        grid.add(stopBtn,1,0,1,2);


        grid.add(new Label("Inclusion shape:"),2,0);
        grid.add(comboInclusionShape,2,1);

        grid.add(new Label("Inclusion Variant:"),3,0);
        grid.add(comboInclusionsVariant,3,1);

        grid.add(listView, 0, 9, 2, 1);

        listView.setMaxSize(Double.MAX_VALUE, 100);

        grid.add(new Label("Periodic:"),0,2);
        grid.add(checkPeriodic,1,2);

        grid.add(new Label("Neighbourhood:"),3,2);
        grid.add(comboNeighbours,3,3);

        grid.add(new Label("Threshold:"),0,3);
        grid.add(editThreshold,1,3);

        grid.add(new Label("Structure:"),2,2);
        grid.add(comboStructures,2,3);


        grid.add(new Label("Radius:"),0,4);
        grid.add(editRadius,0,5);

        grid.add(new Label("Inclusions:"),1,4);
        grid.add(editAreaAmount,1,5);

        grid.add(new Label("Nucelus:"),2,4);
        grid.add(editNucelus,2,5);

        grid.add(setBorders,2,7);

        grid.add(new Label("Borders:"),3,4);
        grid.add(comboBorders,3,5);


        grid.add(clearInsideBorders,2,8);

        grid.add(clrStructure,1,7);

        grid.add(clrBtn,0,8);
        grid.add(randBtn,0,7);

        grid.add(exportButton,3,7);
        grid.add(importButton,3,8);

        randBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        clrBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        clearInsideBorders.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setBorders.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        clrStructure.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        stopBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        startBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);



        root.getChildren().add(grid);


    }
}

