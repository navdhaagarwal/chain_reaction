import java.io.Serializable;
import java.util.*;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Cell implements Serializable {


    private int orbs;
    public Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private int X_Coordinate;
    private int Y_Coordinate;
    //    public int rows = 15;
//    public int cols = 10;
    public Cell[][] grid;

    public Cell(int orbs1) {
        this.orbs = orbs1;
    }

    public Cell(int x1, int y1, int orbs1,int rows,int cols) {
        this.X_Coordinate = x1;
        this.Y_Coordinate = y1;
        this.orbs = orbs1;
        this.grid = new Cell[rows][cols];
    }


    public Cell(int x1, int y1, int orbs1, Cell[][] grid1) {
        this.X_Coordinate = x1;
        this.Y_Coordinate = y1;
        this.orbs = orbs1;
        this.grid = grid1;
    }

    public Cell( Cell[][] grid1) {
        this.grid = grid1;
        this.orbs = 0;
    }

    public Cell() {
        this.orbs = 0;
    }

    public int getOrbs() {
        return orbs;
    }

    public void setOrbs(int orbs) {
        this.orbs = orbs;
    }

    public int getCriticalMass(int i, int j,int rows,int cols) {
        if (isCorner(i, j,rows, cols)){System.out.println("In corner");
            return 2;}
        else if (isEdge(i, j,rows,cols))
            return 3;
        else
            return 4;
    }

    private boolean isCorner(int i, int j,int rows, int cols) {
        return ((i == 0 && j == 0) || (i == rows - 1 && j == 0) || (i == 0 && j == cols - 1) ||
                (i == rows - 1 && j == cols - 1));
    }

    private boolean isEdge(int i, int j,int rows, int cols) {
        return ((i == 0) || (j == 0) || (i == rows - 1) || (j == cols - 1));
    }

    public boolean isValidNeighbour(int i, int j,int rows, int  cols) {
        return (i >= 0 && i < rows && j >= 0 && j < cols);
    }

    @SuppressWarnings("Duplicates")
    public int checkIfWon(Grid g, Players[] p,int playerIndex,int rows, int cols){
        int c = 0;int sum =0;
        Color color = null;
        for(int i = 0;i<rows;i++){
            for(int j = 0;j<cols;j++){
                if(g.root1[i][j].getChildren().size()>0){
                    Sphere x = (Sphere) g.root1[i][j].getChildren().get(0);
                    PhongMaterial ph = (PhongMaterial)x.getMaterial();
                    color = ph.getDiffuseColor();
                    break;
                }
            }
        }
        for(int i = 0;i<rows;i++) {
            for (int j = 0; j < cols; j++) {
                if (g.root1[i][j].getChildren().size() > 0) {
                    sum++;
                }
            }
        }



        System.out.println("Blue = "+color.getBlue());
        System.out.println("Red = "+color.getRed());
        System.out.println("Green = "+color.getGreen());

        for(int i = 0;i<rows;i++){
            for(int j = 0;j<cols;j++) {
                if (g.root1[i][j].getChildren().size() > 0) {
                    Sphere x = (Sphere) g.root1[i][j].getChildren().get(0);
                    PhongMaterial ph = (PhongMaterial) x.getMaterial();
                    System.out.println("Blue1 = "+ph.getDiffuseColor().getBlue());
                    System.out.println("Red1 = "+ph.getDiffuseColor().getRed());
                    System.out.println("Green1 = "+ph.getDiffuseColor().getGreen());
                    if (ph.getDiffuseColor().getBlue() != color.getBlue() || ph.getDiffuseColor().getRed() != color.getRed()
                            || ph.getDiffuseColor().getGreen() != color.getGreen() ) {
                        System.out.println("Not Same");
                        c = 1;
                        break;
                    }

                }
            }
        }
        if(c==0 && sum>=2){
            System.out.println("Same");
            c=2;
        }
        return c;

    }



    public Queue<Coordinates> getNeighbours(int i, int j,int rows, int cols) {
        Deque<Coordinates> queue = new LinkedList<>();
        if (isValidNeighbour(i + 1, j,rows,cols))
            queue.add(new Coordinates(i + 1, j));
        if (isValidNeighbour(i, j + 1,rows,cols))
            queue.add(new Coordinates(i, j + 1));
        if (isValidNeighbour(i - 1, j,rows,cols))
            queue.add(new Coordinates(i - 1, j));
        if (isValidNeighbour(i, j - 1,rows,cols))
            queue.add(new Coordinates(i, j - 1));
        return queue;
    }

    public int explosion(int i, int j, Grid g,int rows, int cols, int playerIndex, Players[] p,GUI gr)
    {
        System.out.println("Inside explosion " + this.grid[i][j].getOrbs() + " " + "Coord " + i + " " + j);
//        this.grid[i][j].setOrbs(this.grid[i][j].getOrbs()+1);

        if (this.grid[i][j].getOrbs() == 0){
            this.grid = g.createSphere(j,i,p,playerIndex,this);
//            this.grid[i][j].setOrbs(this.grid[i][j].getOrbs()+1);
            return 1;
        }
        else if (this.grid[i][j].getOrbs() < getCriticalMass(i, j,rows,cols) -1) {



                Sphere x = (Sphere) g.root1[i][j].getChildren().get(0);
                PhongMaterial ph = (PhongMaterial)x.getMaterial();

                if(p[playerIndex].getColor().equals(ph.getDiffuseColor())){
                    this.grid = g.createSphere(j, i, p, playerIndex, this);
//             this.grid[i][j].setOrbs(this.grid[i][j].getOrbs() + 1);
                    return 1;}


                else{
                    return 0;
                }
         }

        else {

            Sphere x = (Sphere) g.root1[i][j].getChildren().get(0);
            PhongMaterial ph = (PhongMaterial)x.getMaterial();
            if(p[playerIndex].getColor().equals(ph.getDiffuseColor())){
//                this.grid[i][j].setOrbs(this.grid[i][j].getOrbs()+1);


                System.out.println(this.grid[i][j].getOrbs()+"Sfsdfsfesfsdsed");
                Queue<Coordinates> queue = getNeighbours(i, j,rows, cols);
                ArrayList<Coordinates> a = new ArrayList<>(queue);
                for (int f = 0; f < a.size(); f++) {
                    System.out.println("Neighbours " + a.get(f).getX() + " " + a.get(f).getY());
                }
                int length = queue.size();
                System.out.println("Size : " + length);
                for (int l = 0; l < length; l++)
                {
                    Coordinates cxy = queue.poll();
                    g.shiftOrbs(i,j,cxy.getX(),cxy.getY(),p,playerIndex,this);
                    this.grid[i][j].setOrbs(0);
                    g.animation1.setOnFinished(new EventHandler<ActionEvent>()
                    {
                        @Override
                        public void handle(ActionEvent event)
                        {
                            System.out.println(cxy.getX()+"   "+cxy.getY());
                            System.out.println(g.root1[cxy.getX()][cxy.getY()].getChildren().remove(0));
                            for(int i = 0;i<g.root1[cxy.getX()][cxy.getY()].getChildren().size();i++){
                                Sphere x = (Sphere) g.root1[cxy.getX()][cxy.getY()].getChildren().get(i);
                                PhongMaterial ph = new PhongMaterial();
                                ph.setDiffuseColor(p[playerIndex].getColor());
                                x.setMaterial(ph);
                            }
//                        g.root1[cxy.getX()][cxy.getY()].getChildren().remove(g.sphere11);
                            //g.root1[cxy.getX()][cxy.getY()].getChildren().remove(g.line);
                            if( gr.mouseClicks>1 && checkIfWon(g,p,playerIndex,rows,cols)==2) {

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public final void run() {


                                        alert.setTitle("You Won!");
                                        alert.setHeaderText(null);
                                        alert.setContentText(p[playerIndex].getName()+" won!");
                                        ButtonType b1 = new ButtonType("New Game");
                                        ButtonType buttonTypeCancel = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);
                                        alert.getButtonTypes().setAll(b1,buttonTypeCancel);

                                        Optional<ButtonType> result = alert.showAndWait();
                                        if (result.get() == b1) {
                                            gr.mouseClicks = 0;
                                            gr.playerIndex1 = 0;
                                            gr.colorIndex1 = 0;
                                            gr.r  = 1;
                                            gr.scene2 = gr.Grid_GUI();
                                            gr.pstage.setScene(gr.scene2);

                                        } else {
                                            System.exit(0);
                                        }
                                    }

                                });
                            }
                            else{
                            explosion(cxy.getX(), cxy.getY(), g, rows, cols, playerIndex, p, gr);}

                        }
                    });

                }
                return 1;
            }
            else{
                return 0;
            }
        }
    }
}

