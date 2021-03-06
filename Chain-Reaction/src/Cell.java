import java.io.IOException;
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

/**
 * This class is symbolic of the features of a cell in the grid displayed in the game.
 */
public class Cell implements Serializable {

    /**
     * Number of Orbs
     */
    private int orbs;
    /**
     * Alert if a player has won the game.
     */
    public Alert alert = new Alert(Alert.AlertType.INFORMATION);

    /**
     * Holds result of game
     */
    Optional<ButtonType> result;
    /**
     * Button for starting a new game
     */
    ButtonType b1;
    private int X_Coordinate;
    private int Y_Coordinate;
    static boolean check_freeze=false;
    //    public int rows = 15;
//    public int cols = 10;

    /**
     * 2d array which acts as backend of grid displayed
     */
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

    /**
     * Getter()
     */
    public int getOrbs() {
        return orbs;
    }

    /**
     * Setter()
     */
    public void setOrbs(int orbs) {
        this.orbs = orbs;
    }

    /**
     * Returns Critical mass of orb at (i,j)
     */
    public int getCriticalMass(int i, int j,int rows,int cols) {
        if (isCorner(i, j,rows, cols)){System.out.println("In corner");
            return 2;}
        else if (isEdge(i, j,rows,cols))
            return 3;
        else
            return 4;
    }

    /**
     * Evaluates if (i,j) is a corner
     */
    private boolean isCorner(int i, int j,int rows, int cols) {
        return ((i == 0 && j == 0) || (i == rows - 1 && j == 0) || (i == 0 && j == cols - 1) ||
                (i == rows - 1 && j == cols - 1));
    }

    /**
     * Evaluates if (i,j) is an edge
     */
    private boolean isEdge(int i, int j,int rows, int cols) {
        return ((i == 0) || (j == 0) || (i == rows - 1) || (j == cols - 1));
    }

    /**
     * Evaluates if (i,j) is a valid neighbour
     */
    public boolean isValidNeighbour(int i, int j,int rows, int  cols) {
        return (i >= 0 && i < rows && j >= 0 && j < cols);
    }

    @SuppressWarnings("Duplicates")

    /**
     * Checks if the player has won the game.
     */
    public int checkIfWon(Grid g, ArrayList<Players> p,int playerIndex,int rows, int cols){
        int c = 0;int sum =0;
        Color color = null;
//        for(int i = 0;i<rows;i++)
//        {
//            for (int j = 0; j < cols; j++)
//            {
//                System.out.print(g.root1[i][j].getChildren().size());
//            }
//            System.out.println();
//        }
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
//

                    if (ph.getDiffuseColor().getBlue() != color.getBlue() || ph.getDiffuseColor().getRed() != color.getRed()
                            || ph.getDiffuseColor().getGreen() != color.getGreen() ) {
                        System.out.println("Not Same");
                        check_freeze=false;
                        c = 1;
                        break;
                    }
                }
            }
        }
        if(c==0 && sum>=2){
            check_freeze=true;
            System.out.println("Same");
            c=2;
        }
        else
        {
            check_freeze=false;
        }
        return c;

    }


    /**
     * Checks if player has no orb in the game then remove the player from the game.
     */
    public void matchExistingOrbsToPlayers(int playerIndex, ArrayList<Players> p,GUI gui, int rows , int cols, Grid g ) {
        int k;
        for(k = 0;k<p.size();k++) {

//            if (playerIndex == p.size() - 1)
//                k = 0;
//            else
//                k = playerIndex + 1;

                Color c = p.get(k).getColor();
                System.out.println();
                System.out.println(c.getRed() + " " + c.getGreen() + " " + c.getBlue());
                System.out.println("MouseClicks in matching " + gui.mouseClicks);
//                for(int i = 0;i<rows;i++){
//                    for(int j = 0;j<cols;j++){
//                        if(this.grid[i][j].getOrbs()!=g.root1[i][j].getChildren().size()){
//                            System.out.println("i "+i+" j "+j+" orbs "+this.grid[i][j].getOrbs()+" child "+g.root1[i][j].getChildren().size());
//                        }
//                    }
//                }
                if (!checkOrbsByColorExist(c, rows, cols, g) && gui.mouseClicks >= gui.playersInGame-1 ) {
                    p.remove(k);
                    k--;
                    gui.playersInGame--;
                    System.out.println("Removing");
                    System.out.println("Players in Game = "+gui.playersInGame);
                    System.out.println(p.size() + " =Size");
                    gui.mouseClicks--;

                } else {
                    System.out.println("No removal");
                }
            }

    }


    /**
     * Checks if a player's orb exists in the game.
     */
    public boolean checkOrbsByColorExist(Color c, int rows , int cols, Grid g) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (g.root1[i][j].getChildren().size() > 0) {
//                    System.out.println("i "+i+" j "+j);
                    Sphere x = (Sphere) g.root1[i][j].getChildren().get(0);
                    PhongMaterial ph = (PhongMaterial) x.getMaterial();
                    System.out.println(ph.getDiffuseColor() + " color");
                    if (ph.getDiffuseColor().getBlue() == c.getBlue() && ph.getDiffuseColor().getGreen() == c.getGreen()
                            && ph.getDiffuseColor().getRed() == c.getRed()) {
                        return true;
                        }
                    }
                }
            }
        return false;
    }



    /**
     * Shows alert if game is won
     */
    public Optional<ButtonType> showAlert() throws IllegalStateException{
        return alert.showAndWait();
    }


    /**
     * Sets up alert if the game is won
     */
    public void setupAlert(ArrayList<Players> p, int playerIndex){
        alert.setTitle("You Won!");
        alert.setHeaderText(null);
        alert.setContentText(p.get(playerIndex).getName()+" won!");
        b1 = new ButtonType("New Game");
        ButtonType buttonTypeCancel = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(b1,buttonTypeCancel);


    }


    /**
     * Finds the valid neighbours for (i,j) and returns them in a queue.
     */
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


    /**
     * Determines the state of the grid if an explosion occurs via its recursive definition
     */
    public int explosion(int i, int j, Grid g,int rows, int cols, int playerIndex, ArrayList<Players> p,GUI gr)
    {


//        System.out.println("Inside explosion " + this.grid[i][j].getOrbs() + " " + "Coord " + i + " " + j);
//        this.grid[i][j].setOrbs(this.grid[i][j].getOrbs()+1);

        if (this.grid[i][j].getOrbs() == 0)
        {
            this.grid = g.createSphere(j,i,p,playerIndex,this);

            return 1;
        }
        else if (this.grid[i][j].getOrbs() < getCriticalMass(i, j,rows,cols) -1)
        {
            Sphere x = (Sphere) g.root1[i][j].getChildren().get(0);
            PhongMaterial ph = (PhongMaterial)x.getMaterial();

            if(p.get(playerIndex).getColor().equals(ph.getDiffuseColor()))
            {
                this.grid = g.createSphere(j, i, p, playerIndex, this);

                return 1;
            }
            else
            {
                return 0;
            }

        }

        else
        {
            Sphere x = (Sphere) g.root1[i][j].getChildren().get(0);
            PhongMaterial ph = (PhongMaterial)x.getMaterial();
            if(p.get(playerIndex).getColor().equals(ph.getDiffuseColor()))
            {
//                System.out.println(this.grid[i][j].getOrbs()+"Sfsdfsfesfsdsed");
                Queue<Coordinates> queue = getNeighbours(i, j,rows, cols);
                ArrayList<Coordinates> a = new ArrayList<>(queue);
//
                int length = queue.size();
                System.out.println("length of the neighbour array is "+length);
                for (int l = 0; l < length; l++)
                {
                    Coordinates cxy = queue.poll();
                    g.shiftOrbs(i,j,cxy.getX(),cxy.getY(),p,playerIndex,this);
                    this.grid[i][j].setOrbs(0);

                    g.animation1.setOnFinished(event ->
                    {
                        System.out.println(cxy.getX()+"   "+cxy.getY()+" shifted coords");
                        System.out.println(g.root1[cxy.getX()][cxy.getY()].getChildren().size()+" size after explosion");
//                        g.updateColorOfOrbsAfterExplosion(cxy.getX(),cxy.getY(),p,playerIndex);
                        System.out.println(g.root1[cxy.getX()][cxy.getY()].getChildren().remove(0));
                        for(int i1 = 0; i1 <g.root1[cxy.getX()][cxy.getY()].getChildren().size(); i1++)
                        {
                            Sphere x1 = (Sphere) g.root1[cxy.getX()][cxy.getY()].getChildren().get(i1);
                            PhongMaterial ph1 = new PhongMaterial();
                            ph1.setDiffuseColor(p.get(playerIndex).getColor());
                            x1.setMaterial(ph1);
                        }
//                        explosion(cxy.getX(), cxy.getY(), g, rows, cols, playerIndex, p, gr);
                        System.out.println("Final Array");
                        for(int i1 = 0; i1 <rows; i1++)
                        {
                            for(int j1 = 0; j1 <cols ; j1++)
                            {
                                //System.out.println(gr.array_after_explosion.length+" "+grid.length);
                                gr.array_after_explosion[i1][j1]=grid[i1][j1].getOrbs();
                                //System.out.print(grid[i1][j1].getOrbs()+" ");
                            }
                            //System.out.println();
                        }
                        explosion(cxy.getX(), cxy.getY(), g, rows, cols, playerIndex, p, gr);
                        if( gr.mouseClicks>1 && checkIfWon(g,p,playerIndex,rows,cols)==2)
                        {
                            System.out.println("entered the checkifwon");
                            Platform.runLater(() -> {
                                setupAlert(p,playerIndex);
                                result = showAlert();
                                if (result.get() == b1)
                                {
                                    gr.initialisedInGamePlayers = false;
                                    gr.mouseClicks = 0;
                                    gr.playerIndex1 = 0;
                                    gr.colorIndex1 = 0;
                                    gr.r  = 1;
                                    try
                                    {
                                        gr.playersInGame= gr.spinner.getValue();
                                        gr.initialiseInGamePlayers(gr.playersInGame);
                                        gr.scene2 = gr.Grid_GUI();
                                    }
                                    catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    catch (ClassNotFoundException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    gr.pstage.setScene(gr.scene2);
                                    return;

                                }
                                else
                                {
                                    System.exit(0);
                                }
                            });
                        }

                        System.out.println("Final Array");
                        for(int i1 = 0; i1 <rows; i1++)
                        {
                            for(int j1 = 0; j1 <cols ; j1++)
                            {
                                gr.array_after_explosion[i1][j1]=grid[i1][j1].getOrbs();
                                System.out.print(g.array[j1][i1]+" ");
                            }
                            System.out.println();
                        }

                        String[][] colorsOfPlayers = g.color(rows,cols);
                        try {
                            gr.serialize(rows, cols,g.array,colorsOfPlayers, gr.mouseClicks, gr.playersInGame, gr.serial_color);
                            System.out.println("Serialized");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }




                    });



                    System.out.println("left 1");
                }
                System.out.println("left 2");
                return 1;
            }
            else
            {
                return 0;
            }
        }
    }
}

