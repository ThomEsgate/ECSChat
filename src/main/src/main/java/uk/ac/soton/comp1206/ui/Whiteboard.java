package uk.ac.soton.comp1206.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Whiteboard extends Canvas {

    private int width;//Width and height specified when showWhiteboard is called
    private int height;

    public  Boolean isActive = false;
    private double lineWidth = 3;

    //Colours
    private int red = 255, grn = 255, blu = 255, alp = 1; //RED, GREEN, BLUE, APHA
    private Color penColour = Color.rgb(red, grn, blu);

    //
    Stage window;
    Scene scene;

    public Whiteboard(int width, int height){
        setWidth(width);
        setHeight(height);

        window = new Stage();

        System.out.println("Creating whiteboard...");

        setMode(DrawingMode.DRAWING);

        window.setTitle("Whiteboard");
        window.setMinWidth(width);
        window.setMinHeight(height);

        GraphicsContext gc = getGraphicsContext2D();
        gc.setLineWidth(lineWidth);

        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: #CCDCDC; -fx-border-color: black; -fx-border-width: 8 8 8 8");
        stackPane.setAlignment(Pos.CENTER);

        stackPane.getChildren().add(this);

        scene = new Scene(stackPane, width, height);
        isActive = true;
        window.setScene(scene);

    }

    public void showWhiteboard() {
        isActive = true;
        window.setScene(scene);
        window.showAndWait(); //Waits for window to be hidden or closed before it goes back to the other window
    }

    public void hideWhiteboard(){
        isActive = false;
    }

    public void setMode(DrawingMode mode){
        GraphicsContext gc = getGraphicsContext2D();
        gc.setLineWidth(lineWidth);

        switch (mode) {
            case DRAWING -> {//Click down on the whiteboard
                setOnMousePressed(e -> {
                    gc.setStroke(penColour);
                    gc.beginPath();
                    gc.lineTo(e.getX(), e.getY());
                });

                setOnMouseDragged(e -> {
                    gc.lineTo(e.getX(), e.getY());
                    gc.stroke();
                });

                setOnMouseReleased(e -> {
                    gc.lineTo(e.getX(), e.getY());
                    gc.stroke();
                    gc.closePath();
                });
                System.out.println("Current Mode: Drawing");
                break;
            }
            case FILL -> System.out.println("Current Mode: FILL");
            case RUBBER -> System.out.println("Current Mode: RUBBER");
            default -> System.err.println("~WRONG DRAWINGMODE~");
        }
        
    }

    public void setActive(Boolean active){
        isActive = !isActive;
    }

}
