package application;

import java.util.LinkedList;

import algorithm.BTNode;
import algorithm.BTree;
import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;

import javax.swing.*;


public class Main extends Application {

    Label verticesText;
    Label heightText;
    Label historyText;
    public static double sliderValue;
    private int key;//entered value stored here
    private static int vertices = 0;
    private BTreePane btPane;
    private TextField keyText = new TextField();//input text box

    Button clearScreenButton = new Button("Clear Screen");
    Button insertButton = new Button("Insert");
    Button deleteButton = new Button("Delete");
    Button searchButton = new Button("Search");
    private int index = 0;
    private LinkedList<BTree<Integer>> bTreeLinkedList = new LinkedList<BTree<Integer>>();
    private String x;
    private BTree<Integer> bTree = new BTree<Integer>(3);//change order of tree
    public static Slider sliderBar = new Slider(1, 10, 5.5);//min, max, initial value
    String orderValue[] = {"3","4","5","6","7"};
    ComboBox dropDownMenu = new ComboBox(FXCollections.observableArrayList(orderValue));

    public void setOrder(int x){
        bTree = new BTree<Integer>(x);//change order of tree
        reset();
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        BackgroundFill backgroundFill = new BackgroundFill(Color.PEACHPUFF, CornerRadii.EMPTY, Insets.EMPTY);
        // create Background
        Background background = new Background(backgroundFill);

        // set background colour
        root.setBackground(background);
        sliderBar.setStyle("-fx-control-inner-background: teal;");


        // Create button HBox for bottom bar
        HBox hBox = new HBox(15);//15 pixel distance between elements
        VBox vBox = new VBox(15);//15 pixel distance between elements

        Text heading = new Text("B-Tree Implementation");
        //heading font
        Font f1 = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20);
        heading.setUnderline(true);
        heading.setFont(f1);//setting heading font

        root.setTop(heading);//heading on top of border pane
        root.setAlignment(heading, Pos.CENTER);
        //paddings for hbox and vbox
        BorderPane.setMargin(hBox, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(vBox, new Insets(10, 10, 10, 10));

        //height text box cotents
        heightText = new Label("Height\n     " + bTree.getHeight());
        heightText.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");

        //height box contents
        Rectangle heightRectangle = new Rectangle();
        //font for bottom box
        Font f3 = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 13);

        Label animationSpeed = new Label("Animation speed:");
        animationSpeed.setFont(f3);

        StackPane heightPane= new StackPane();
        heightRectangle.setWidth(70);
        heightRectangle.setHeight(40);
        Font f2 = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 15);
        heightText.setFont(f2);
        heightRectangle.setFill(Color.TEAL);
        heightRectangle.setArcHeight(10);
        heightRectangle.setArcWidth(10);
        heightRectangle.setStyle(" -fx-background-radius: 3;\n" +
                "    -fx-border-radius: 3;\n" +
                "    -fx-border-width:1;");
        vBox.getChildren().add(clearScreenButton);//adding clear button on top
        heightPane.getChildren().addAll(heightRectangle,heightText);//adding text on top of rectangle
        vBox.getChildren().add(heightPane);//adding height box and text to right side under clear screen button

        //vertices box content
        verticesText = new Label("Vertices\n      " + Main.vertices);
        verticesText.setFont(f2);
        Rectangle verticesRectangle = new Rectangle();
        StackPane verticesPane= new StackPane();
        verticesRectangle.setWidth(70);
        verticesRectangle.setHeight(40);
        verticesText.setStyle("    -fx-font-weight: bold;\n" +
                " -fx-text-fill: white;");
        verticesRectangle.setFill(Color.TEAL);
        verticesRectangle.setArcHeight(10);//radius of rectangle
        verticesRectangle.setArcWidth(10);
        verticesRectangle.setStyle("fx-background-radius: 3;\n" +
                "    -fx-border-radius: 3;\n" +
                "    -fx-border-width:1;");
        Label order =new Label("Set Order:");
        Label nullLabel1 = new Label();
        nullLabel1.setMaxHeight(50);
        order.setFont(f3);
        verticesPane.getChildren().addAll(verticesRectangle,verticesText);//adding text oon top of the box
        vBox.getChildren().add(verticesPane);//adding vertices box under height box
        dropDownMenu.setMaxWidth(200);
        dropDownMenu.setValue("3");

        vBox.getChildren().addAll(nullLabel1,order,dropDownMenu);
         // (dropDownMenu, Pos.CENTER);
        root.setRight(vBox);//adding vBox to right side of border pane


        // TextField
        keyText.setPrefWidth(100);
        keyText.setAlignment(Pos.BASELINE_RIGHT);
        // Button

        insertButton.setStyle("-fx-background-color: teal;\n" +
                "    -fx-background-radius: 3;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size:12;\n" +
                "    -fx-text-fill:white;\n" +
                "    -fx-border-color: teal;\n" +
                "    -fx-border-radius: 3;\n" +
                "    -fx-border-width:1;");

        deleteButton.setStyle("-fx-background-color: teal;\n" +
                "    -fx-background-radius: 3;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size:12;\n" +
                "    -fx-text-fill:white;\n" +
                "    -fx-border-color: teal;\n" +
                "    -fx-border-radius: 3;\n" +
                "    -fx-border-width:1;");

        searchButton.setStyle("-fx-background-color: teal ;\n" +
                "    -fx-background-radius: 3;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size:12;\n" +
                "    -fx-text-fill:white;\n" +
                "    -fx-border-color: teal;\n" +
                "    -fx-border-radius: 3;\n" +
                "    -fx-border-width:1;");

        clearScreenButton.setStyle("-fx-background-color: darkred;\n" +"    -fx-font-weight: bold;\n" +
                "    -fx-border-color: darkred;\n" +
                "    -fx-border-radius: 3;\n" +
                "    -fx-border-width:1;"  + " -fx-text-fill: white;");

        clearScreenButton.setOnMouseEntered((e -> changeResetColourHoverON()));
        clearScreenButton.setOnMouseExited((e -> changeResetColourHoverOFF()));
        insertButton.setOnMouseEntered((e -> changeButtonColourHoverON(e)));
        deleteButton.setOnMouseEntered((e -> changeButtonColourHoverON(e)));
        searchButton.setOnMouseEntered((e -> changeButtonColourHoverON(e)));
        insertButton.setOnMouseExited((e -> changeButtonColourHoverOFF(e)));
        deleteButton.setOnMouseExited((e -> changeButtonColourHoverOFF(e)));
        searchButton.setOnMouseExited((e -> changeButtonColourHoverOFF(e)));
        Label nullLabel = new Label();
        nullLabel.setPrefWidth(100);
        //null label to create gap between animation slider and buttons
        Text enterANumber = new Text("Enter a number: ");
        enterANumber.setFont(f3);
        //adding all elements to bottom bar
        hBox.getChildren().addAll(enterANumber, keyText, insertButton, searchButton, deleteButton, nullLabel, animationSpeed, sliderBar);
        hBox.setAlignment(Pos.CENTER);//bottom bar in centre always
        VBox bottomVBox = new VBox();//vbox for bottom history and buttons
        historyText = new Label("History appears here");
        historyText.setFont(f2);
        bottomVBox.getChildren().addAll(historyText,hBox);
        checkNext();
        root.setBottom(bottomVBox);
        // Create TreePane in center, size of window
        Scene scene = new Scene(root, 1300, 550);
        primaryStage.setTitle("B-Tree Visualization");
        primaryStage.setScene(scene);

        btPane = new BTreePane( scene.getWidth()/2, 50, bTree);
        root.setCenter(btPane);
        BorderPane.setMargin(bottomVBox, new Insets(10, 10, 10, 10));

        insertButton.setOnMouseClicked(e -> insertValue());
        deleteButton.setOnMouseClicked(e -> deleteValue());
        searchButton.setOnMouseClicked(e -> searchValue());
        clearScreenButton.setOnMouseClicked(e -> reset());
        dropDownMenu.setOnAction(event -> setOrder(Integer.parseInt(dropDownMenu.getValue().toString())));

        MoveKey mk=new MoveKey();
        root.setOnKeyPressed(mk);
        //to set size of startup window
        primaryStage.show();

    }

//pressing enter for inserting value and alt+del for deleting
    class MoveKey implements EventHandler <KeyEvent>{
        @Override
        public void handle(KeyEvent e) {
            switch (e.getCode()) {
                case ENTER: insertValue();
                    break;
                case DELETE: deleteValue();
                    break;


            }
        }
    }

    //for changing colour of button when mouse is moved on buttons
    private void changeButtonColourHoverON(MouseEvent e){
        if(e.getSource().equals(insertButton))
        insertButton.setStyle("-fx-background-color: white;\n" +"    -fx-font-weight: bold;\n" +
                "    -fx-border-color: teal;\n" +
                "    -fx-border-radius: 3;\n" +
                "    -fx-border-width:1;"  + " -fx-text-fill: teal;");
        else if(e.getSource().equals(deleteButton))
            deleteButton.setStyle("-fx-background-color: white;\n"+ "    -fx-font-weight: bold;\n" +"    -fx-border-color: teal;\n" +
                            "    -fx-border-radius: 3;\n" +
                            "    -fx-border-width:1;"+
                    "    -fx-text-fill: teal;");
        else if(e.getSource().equals(searchButton))
            searchButton.setStyle("-fx-background-color: white;\n"+ "    -fx-font-weight: bold;\n" +"    -fx-border-color: teal;\n" +
                    "    -fx-border-radius: 3;\n" +
                    "    -fx-border-width:1;"+
                    "    -fx-text-fill: teal;");
    }

    //for changing colour of button back to original when mouse is moved away from buttons
    private void changeButtonColourHoverOFF(MouseEvent e){
        if(e.getSource().equals(insertButton))
            insertButton.setStyle("-fx-background-color: teal;\n" +
                    "    -fx-background-radius: 3;\n" +
                    "    -fx-font-weight: bold;\n" +
                    "    -fx-font-size:12;\n" +
                    "    -fx-text-fill:white;\n" +
                    "    -fx-border-color: teal;\n" +
                    "    -fx-border-radius: 3;\n" +
                    "    -fx-border-width:1;");
        else if(e.getSource().equals(deleteButton))
            deleteButton.setStyle("-fx-background-color: teal;\n" +
                    "    -fx-background-radius: 3;\n" +
                    "    -fx-font-weight: bold;\n" +
                    "    -fx-font-size:12;\n" +
                    "    -fx-text-fill:white;\n" +
                    "    -fx-border-color: teal;\n" +
                    "    -fx-border-radius: 3;\n" +
                    "    -fx-border-width:1;");
        else if(e.getSource().equals(searchButton))
            searchButton.setStyle("-fx-background-color: teal;\n" +
                    "    -fx-background-radius: 3;\n" +
                    "    -fx-font-weight: bold;\n" +
                    "    -fx-font-size:12;\n" +
                    "    -fx-text-fill:white;\n" +
                    "    -fx-border-color: teal;\n" +
                    "    -fx-border-radius: 3;\n" +
                    "    -fx-border-width:1;");
    }

    //for changing colour of button when mouse is moved on clear screen button
    private void changeResetColourHoverON() {
        clearScreenButton.setStyle("-fx-background-color: white;\n" +"    -fx-font-weight: bold;\n" +
                "    -fx-border-color: darkred;\n" +
                "    -fx-border-radius: 3;\n" +
                "    -fx-border-width:1;"  + " -fx-text-fill: darkred;");
    }

    //for changing colour of button back to original when mouse is moved away from clear screen button
    private void changeResetColourHoverOFF() {
        clearScreenButton.setStyle("-fx-background-color: darkred;\n" +"    -fx-font-weight: bold;\n" +
                "    -fx-border-color: darkred;\n" +
                "    -fx-border-radius: 3;\n" +
                "    -fx-border-width:1;"  + " -fx-text-fill: white;");
    }
    //function to perform next action if  needed
    private void checkNext() {
        if (checkNextCondition()==1) {
            next();
        } else if (checkNextCondition()==2) {
            return;
        } else if (checkNextCondition()==3)
            next();
        else {
            return;
        }
    }

    //conditions for next function
    private int checkNextCondition(){
        if(index > 0 && index < bTreeLinkedList.size() - 1)
            return 1;
        else if (index > 0 && index == bTreeLinkedList.size() - 1) {
            return 2;
        }
        else if (index == 0 && index < bTreeLinkedList.size() - 1) {
            return 3;
        }
        else {
            return 4;
        }
    }

    //function to insert value to the tree
    //calls maketree
    private void insertValue() {
        try {
            if(keyText.getText().equals("")){
                throw new blankInputException();
            }
            key = Integer.parseInt(keyText.getText());
            bTree.setStepTrees(new LinkedList<BTree<Integer>>());

            bTree.insert(key);

            index = 0;
            bTreeLinkedList = bTree.getStepTrees();
            btPane.updatePaneLayout(bTreeLinkedList.get(0));
            checkNext();
            Main.vertices = Main.vertices + 1;
            if(Main.vertices<10)
                verticesText.setText("Vertices\n      " + Main.vertices);
            else
                verticesText.setText("Vertices\n     " + Main.vertices);

            heightText.setText("Height\n     " + bTree.getHeight());
            historyText.setText(">>>Added Node: " + key);

        } catch (NumberFormatException e) {//if something other than number is input
            JOptionPane.showMessageDialog(null, "Illegal input data!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
        } catch (blankInputException blankInputException){//if nothing is input
            JOptionPane.showMessageDialog(null, "No data entered!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {//all other exceptions
            JOptionPane.showMessageDialog(null, e.getMessage(), "Warning!", JOptionPane.INFORMATION_MESSAGE);
        } finally {//finally clear the text box
            keyText.setText("");
        }
    }


    private void deleteValue() {
        try {
            if(keyText.getText().equals("")){
                throw new blankInputException();
            }
            key = Integer.parseInt(keyText.getText());
            if (bTree.getNode(key) == bTree.nullBTNode) {
                throw new Exception("Not in the tree!");
            }
            bTree.setStepTrees(new LinkedList<BTree<Integer>>());

            bTree.delete(key);

            index = 0;
            bTreeLinkedList = bTree.getStepTrees();
            btPane.updatePaneLayout(bTreeLinkedList.get(0));
            checkNext();
            Main.vertices = Main.vertices - 1;
            if(Main.vertices<10)
                verticesText.setText("Vertices\n      " + Main.vertices);
            else
                verticesText.setText("Vertices\n     " + Main.vertices);
            heightText.setText("Height\n     " + bTree.getHeight());
            historyText.setText("Deleted Node: " +key);
        } catch (NumberFormatException e) {//if something other than number is input
            JOptionPane.showMessageDialog(null, "Illegal input data!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
        }catch (blankInputException blankInputException) {//if nothing is input
            JOptionPane.showMessageDialog(null, "No data entered!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {//all other exceptions
            JOptionPane.showMessageDialog(null, e.getMessage(), "Warning!", JOptionPane.INFORMATION_MESSAGE);
        } finally {//finally clear the text box
            keyText.setText("");
        }
    }

    private void searchValue() {
        try {if(keyText.getText().equals("")){
            throw new blankInputException();
        }
        key = Integer.parseInt(keyText.getText());
        btPane.searchPathColoring(bTree, key);
        String speed = "";
        if(sliderBar.getValue()<4)
            speed = "Low";
        else if(sliderBar.getValue()<7)
            speed = "Medium";
        else
            speed = "High";
        historyText.setText(">>>Searching Node: " + key + " :: Animation speed: " +speed);

        } catch (NumberFormatException e) {//if something other than number is input
            JOptionPane.showMessageDialog(null, "Illegal input data!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
        }catch (blankInputException blankInputException) {//if nothing is input
            JOptionPane.showMessageDialog(null, "No data entered!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {//all other exceptions
            JOptionPane.showMessageDialog(null, e.getMessage(), "Warning!", JOptionPane.INFORMATION_MESSAGE);
        } finally {//finally clear the text box
            keyText.setText("");
        }
    }

    //called if tree is not make in one step
    private void next() {
        if (index < bTreeLinkedList.size() - 1) {
            index++;
            btPane.updatePaneLayout(bTreeLinkedList.get(index));
            checkNext();
        }
    }

    //to delete everything from the tree
    private void reset() {
        keyText.setText("");

        bTree.setRoot(null);
        index = 0;
        bTreeLinkedList.clear();
        btPane.updatePaneLayout(bTree);
        Main.vertices =0;
        verticesText.setText("Vertices\n      " + Main.vertices);
        heightText.setText("Height\n     " + bTree.getHeight());
        checkNext();
        historyText.setText(">>>Screen cleared!");

    }
}

//if nothing is input
//custom exception
class blankInputException extends Exception{
    public blankInputException(){}
}


class BTreePane extends Pane {
    private BTree<Integer> bTree;
    private double X;//original x value
    private double Y;//original Y value
    private int fSize = 15; //fixed font size
    private int squareSide = 30; //node ka square side
    private int rowSpace = 70;//space between 2 rows


    private void constructNode(String s, double x, double y) {
        Color color =  Color.FIREBRICK;//colour of nodes rectagle
        Rectangle rect;
        //size of rectangle based on number of digits in input
        if(s.length()<3)
            rect = new Rectangle(x, y, squareSide, squareSide);
        else
            rect = new Rectangle(x, y, squareSide+1, squareSide);
        //making rectangle for node
        rect.setFill(color);
        rect.setStroke(Color.MAROON);
        rect.setArcHeight(20);
        rect.setArcWidth(20);
        Text txt;
        //to add text to rectangle
        //position based on number of digits.
        if(compareLength(s,1))
            txt = new Text(x + 12 - s.length(), y + 20, s);
        else if(compareLength(s,2))
            txt = new Text(x + 9 - s.length(), y + 20, s);
        else
            txt = new Text(x + 6 - s.length(), y + 20, s);
        txt.setFill(Color.WHITE);//nodes ke text ka colour
        txt.setFont(Font.font("Arial", FontWeight.BOLD, fSize));
        this.getChildren().addAll(rect, txt);
    }

    public BTreePane(double x, double y, BTree<Integer> bTree) {
        this.X = x;
        this.Y = y;
        this.bTree = bTree;
    }

    //called as soon as any node is inserted or deleted
    public void updatePaneLayout(BTree<Integer> bTree) {
        this.getChildren().clear();
        this.bTree = bTree;
        DrawBTree(bTree.getRoot(), X, Y);
    }

    //function to draw tree
    private void DrawBTree(BTNode<Integer> root, double x, double y) {

        if (!(root==null)) {
            for (int i = 0; i < root.getSize(); i++) {
                constructNode(root.getKey(i).toString(), x + i * squareSide, y);
            }
            // Drawing lines
            double startY = y + 2 * fSize;
            if (root.isLastInternalNode() == false) {
                int i=0;
                while(i < root.getChildren().size()){
                    double startLine = x + i * squareSide;
                    double startChildNode, endLine;

                    if (compareRootSize(i,root)==1) {
                        startChildNode = startLine + (bTree.getOrder() - 1) * (bTree.getHeight(root.getChild(i))-1) * squareSide / 2;
                        endLine = startChildNode + ((double) root.getChild(i).getSize()) / 2 * squareSide;
                    } else if (compareRootSize(i,root)==0) {
                        endLine = startLine - (bTree.getOrder() - 1) * (bTree.getHeight(root.getChild(i))-1) * squareSide / 2
                                - ((double) root.getChild(i).getSize()) / 2 * squareSide;
                        startChildNode = endLine - ((double) root.getChild(i).getSize()) / 2 * squareSide;
                    } else {
                        startChildNode = startLine - ((double) root.getChild(i).getSize()) / 2 * squareSide;
                        endLine = startLine;
                    }
                    if (i == 0) {
                        startChildNode = startChildNode - squareSide * 2;
                        endLine = endLine - squareSide * 2;
                    } else if (i == root.getSize()) {
                        startChildNode = startChildNode + squareSide * 2;
                        endLine = endLine + squareSide * 2;
                    }

                    if (!root.getChild(i).isNull()) {
                        Line line = new Line(startLine, startY, endLine, y + rowSpace);
                        line.setStrokeWidth(1.5);
                        line.setStroke(Color.TEAL);

                        this.getChildren().add(line);
                    }
                    DrawBTree(root.getChild(i), startChildNode, y + rowSpace);
                    i++;
                }
            }
        }
    }

    //function to compate lengths
    public boolean compareLength(String s,int x){
        if(s.length()==x)
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

    //function to compare root size
    public int compareRootSize(int i,BTNode root){
        if((double) i > ((double) root.getSize()) / 2)
            return 1;
        else if((double) i < ((double) root.getSize()) / 2)
            return 0;
        else return -1;
    }

    //search animation for all the nodes except key node
    private void nodeAnimation(String s, double x, double y, double delay) {
        Main.sliderValue = Main.sliderBar.getValue();
        double sliderVal = Math.abs(Main.sliderValue-9);
        // Draw a node
        Rectangle rect = new Rectangle(x, y, squareSide, squareSide);
        rect.setFill(Color.FIREBRICK);
        rect.setStroke(Color.LIGHTCORAL);
        rect.setArcHeight(20); rect.setArcWidth(20);
        Text txt = new Text(x + 10 - s.length(), y + 20, s);
        txt.setFill(Color.WHITE);
        txt.setFont(Font.font("Arial", FontWeight.BOLD, fSize));
        this.getChildren().addAll(rect, txt);

        // make fill transition
        FillTransition fill = new FillTransition();

        fill.setAutoReverse(true);
        fill.setCycleCount(2);
        fill.setDelay(Duration.millis(delay*sliderVal));
        fill.setDuration(Duration.millis(200*sliderVal));
        fill.setFromValue(Color.FIREBRICK);
        fill.setToValue(Color.LIGHTCORAL);
        fill.setShape(rect);
        fill.play();
    }
    //search animation for the key  node
    private void nodeAnimation1(String s, double x, double y, double delay) {
        Main.sliderValue = Main.sliderBar.getValue();
        double sliderVal = Math.abs(Main.sliderValue-9);

        Rectangle rect = new Rectangle(x, y, squareSide, squareSide);
        rect.setFill(Color.FIREBRICK);
        rect.setStroke(Color.MAROON);
        rect.setArcHeight(20); rect.setArcWidth(20);
        Text txt = new Text(x + 10 - s.length(), y + 20, s);
        txt.setFill(Color.WHITE);
        txt.setFont(Font.font("Arial", FontWeight.BOLD, fSize));
        this.getChildren().addAll(rect, txt);

        // make fill transition
        FillTransition fill = new FillTransition();

        fill.setAutoReverse(false);
        fill.setCycleCount(1);
        fill.setDelay(Duration.millis(delay*sliderVal));
        fill.setDuration(Duration.millis(200*sliderVal));
        fill.setFromValue(Color.FIREBRICK);
        fill.setToValue(Color.LIGHTCORAL);
        fill.setShape(rect);
        fill.play();


    }


    public void searchPathColoring(BTree<Integer> bTree, int key) throws Exception {
        updatePaneLayout(bTree);
        if (!bTree.isEmpty()) {
            BTNode<Integer> currentNode = bTree.getRoot();
            double delay = 0;
            double x = X, y = Y;
            for(;!currentNode.equals(bTree.nullBTNode);) {
                int i = 0;
                for(;i< currentNode.getSize();) {

                    nodeAnimation(currentNode.getKey(i).toString(), x, y, delay);
                    if(currentNode.getKey(i).equals(key))
                        nodeAnimation1(currentNode.getKey(i).toString(), x, y, delay);

                    delay+= 200;
                    // compare with the key to find
                    if (currentNode.getKey(i).equals(key)) {
                        return;
                    }
                    else if (currentNode.getKey(i).compareTo(key) > 0) {
                        y += rowSpace;
                        if ((double) i < ((double) currentNode.getSize()) / 2) {
                            x = x - (bTree.getOrder() - 1) * (bTree.getHeight(currentNode.getChild(i))-1) * squareSide
                                    / 2 - ((double) currentNode.getChild(i).getSize()) * squareSide;
                        }
                        else
                            x = x - ((double) currentNode.getChild(i).getSize()) / 2 * squareSide;
                        if (i == 0)
                            x -= squareSide * 2;
                        currentNode = currentNode.getChild(i);
                        i = 0;
                    } else {
                        // go to the next key in the node
                        i++;
                        x += squareSide;
                    }

                }
                // go down to the key to the right of the node
                if (!currentNode.isNull()) {
                    y += rowSpace;
                    x = x + (bTree.getOrder() - 1) * (bTree.getHeight(currentNode.getChild(i))-1) * squareSide / 2
                            + squareSide * 2;

                    currentNode = currentNode.getChild(currentNode.getSize());
                }
            }
        }
        throw new Exception("Not in the tree!");
    }








}

