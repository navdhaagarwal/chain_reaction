import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Grid
{

	/**
	 * Drop down menu
	 */
	ComboBox<String> comboBox;

	/**
	 * Parameters for positioning of grid
	 */
	public int cellOffset, xLeftShift, yBottomShift,xGridStart,yGridStart,cellSize;
	PathTransition path1,path2,path3;

	/**
	 * Each cell has a group of orbs in root1[][]
	 */
	Group[][] root1;
	Group root = new Group();

	/**
	 * Contains grid lines and orbs
	 */
	ObservableList<Node> list;

	/**
	 * Contains backend of grid displayed
	 */
	int array[][];

	GUI grc = new GUI();
	int rows = grc.rows;
	int cols = grc.cols;
	int radius = 9;

	public Grid(int rows, int cols){
	    this.cols=cols;
	    this.rows=rows;
		root1 = new Group[rows][cols];
		list = root.getChildren();
		array = new int[cols][rows];
		Cell[][] cellArray = new Cell[rows][cols];
		for(int i = 0 ;i<rows; i++){
			for(int j = 0;j<cols ;j++){

				root1[i][j] = new Group();
				cellArray[i][j] = new Cell(0);
			}
		}

	}

	/**
	 * Calculates parameters for displaying grid
	 */
	public void calculateOffsetsForGrid(int rows, int cols){
		if(rows == 9 && cols == 6){
			cellSize = 60;
			xGridStart = 20;
			yGridStart = 60;
			xLeftShift = 12;
			yBottomShift = 18;
			cellOffset = 4;
			radius = 12;

		}
		else
		{
			cellSize = 44;
			xGridStart = 20;
			yGridStart = 44;
			xLeftShift = 10;
			yBottomShift = 16;	
			cellOffset =2;	//xLeftShift, yBottomShift and CellOffset are proportional, eg XL=15,YBS=24,CO=3
		}
	}

	Duration DURATION = Duration.seconds(2);
	Animation animation, animation1;
	Sphere sphere11;
	Line line;

	/**
	 * Returns the transition required after explosion
	 */
	Transition createTransition1(Shape path, Sphere node)
	{
		PathTransition t = new PathTransition(Duration.seconds(0.4), path, node);
		t.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
		t.setCycleCount(1);
		t.setInterpolator(Interpolator.LINEAR);
		return t;
	}

	/**
	 * Uses PathTransition to move orbs along a straight line form one coordinate to another
	 */
	public void move(int inX, int inY, int toX, int toY,ArrayList<Players> p, int playerIndex)
	{
		line= new Line();
		line.setStartX((inY + 1) * cellSize);
		line.setStartY((inX + 1) * cellSize + (cellSize / 2));
    	line.setEndX((toY + 1) * cellSize);
    	line.setEndY((toX + 1) * cellSize + (cellSize / 2));
    	line.setStroke(Color.TRANSPARENT);

    	sphere11 = new Sphere();
		sphere11.setRadius(radius);
		sphere11.setTranslateX((inX + 1) * cellSize);
		sphere11.setTranslateY((inY + 1) * cellSize + (cellSize / 2));
		PhongMaterial p1 = new PhongMaterial();
		p1.setDiffuseColor(p.get(playerIndex).getColor());
		sphere11.setMaterial(p1);
		
		root1[toX][toY].getChildren().add(sphere11);

//		updateColorOfOrbsAfterExplosion(toX,toY,p,playerIndex);

		//root1[toX][toY].getChildren().add(line);

    	animation1 = new ParallelTransition(createTransition1(line, sphere11));
		animation1.play();

	}

	public void updateColorOfOrbsAfterExplosion(int toX, int toY, ArrayList<Players> p, int playerIndex){
		PhongMaterial p1 = new PhongMaterial();
		p1.setDiffuseColor(p.get(playerIndex).getColor());
		for(int i = 0;i<root1[toX][toY].getChildren().size();i++){
			Sphere s = (Sphere) root1[toX][toY].getChildren().get(i);
			s.setMaterial(p1);
		}
	}

	/**
	 * Shifts orbs from one coordinate to another
	 */
	public void shiftOrbs(int inX, int inY, int toX, int toY,ArrayList<Players> p, int playerIndex,Cell c)
	{

		move(inX, inY,toX,toY,p,playerIndex);

		System.out.println("Array val :"+array[inY][inX]);
		if(array[inY][inX]>0) 
		{
			System.out.println("Shifting");
			System.out.println(root1[inX][inY].getChildren().size() + " is the size");
			System.out.println("coords are " + inX + " " + inY + " " + toX + " " + toY);
			Node x = root1[inX][inY].getChildren().get(0);
			root1[inX][inY].getChildren().remove(x);
			array[inY][inX]--;


		}
	}

	Transition createTransition(Shape path, Sphere node)
	{
		PathTransition t = new PathTransition(DURATION, path, node);
		t.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
		t.setCycleCount(Timeline.INDEFINITE);
		t.setInterpolator(Interpolator.LINEAR);
		return t;
	}


    //Serial serial= new Serial(rows, cols);

	/**
	 * Stores color of orbs in a 2d array
	 */
	public String[][] color(int rows, int cols)
    {
        String color_name[][]= new String[rows][cols];
        for(int i=0; i<rows; i++)
        {
            for(int j=0; j<cols; j++)
            {
                if(array[j][i]>0)
                {
                    Sphere sphere= (Sphere) root1[i][j].getChildren().get(0);
                    PhongMaterial ph= (PhongMaterial) sphere.getMaterial();
                    String s= String.valueOf(ph.getDiffuseColor().getBlue()+" "+ph.getDiffuseColor().getGreen()+" "+ph.getDiffuseColor().getRed());
                    color_name[i][j]=s;

                }
            }
        }
        //serial.initialize_color(color_name);
        return color_name;
    }


	/**
	 * Used to undo the creation of a sphere
	 */
    public Cell[][] createSphere_undo(double x, double y,Cell c, int array1[][], String col[][])
    {

        Sphere sphere = new Sphere();
        sphere.setRadius(radius);
        String s=col[(int)y][(int)x];

        String bgr[]=s.split(" ");
        double b = Double.parseDouble(bgr[0]);
        double g = Double.parseDouble(bgr[1]);
        double r = Double.parseDouble(bgr[2]);
        PhongMaterial ph = new PhongMaterial();
        Color c1= new Color(r,g,b,1);
        ph.setDiffuseColor(c1);
        sphere.setMaterial(ph);
        //System.out.println(array1[(int)x][(int)y]);
        if (array1[(int) x][(int) y] == 0)
        {
            System.out.println((int) x + " " + (int) y);
            sphere.setTranslateX((x + 1) * cellSize);
            sphere.setTranslateY((y + 1) * cellSize + (cellSize / 2));
            array1[(int) x][(int) y] = 1;
            c.grid[(int)y][(int)x].setOrbs(1);
            root1[(int)y][(int)x].getChildren().add(sphere);

        }

        else if (array1[(int) x][(int) y] == 1 ) {
            sphere.setTranslateX((x + 1) * cellSize);
            sphere.setTranslateY((y + 1) * cellSize + (cellSize / 2));
            array1[(int) x][(int) y] = 2;
            c.grid[(int)y][(int)x].setOrbs(2);
            System.out.println("entered to create 2nd sphere");

            animate((int)x,(int)y, sphere, array1);

            root1[(int)y][(int)x].getChildren().add(sphere);

        }

        else if (array1[(int) x][(int) y] == 2)
        {
            sphere.setTranslateX((x + 1) * cellSize);
            sphere.setTranslateY((y + 1) * cellSize + (cellSize / 2));
            array1[(int) x][(int) y] = 3;
            c.grid[(int)y][(int)x].setOrbs(3);

            animate((int)x,(int)y, sphere, array1);

            root1[(int)y][(int)x].getChildren().add(sphere);

        }

        for(int i=0; i<rows; i++)
        {
            for(int j=0;j<cols; j++)
            {
                array[j][i]=array1[j][i];
            }
        }



        return c.grid;
    }

	/**
	 * Used to create a sphere at a coordinate
	 */
	public Cell[][] createSphere(double x, double y, ArrayList<Players> p, int playerIndex, Cell c)
	{
		if(c.grid[(int)y][(int)x].getOrbs()==c.getCriticalMass((int)y,(int)x,rows,cols))
			DURATION = Duration.seconds(2);
		else
			DURATION = Duration.seconds(4);
		
		Sphere sphere = new Sphere();
		sphere.setRadius(radius);
		PhongMaterial ph = new PhongMaterial();
		ph.setDiffuseColor(p.get(playerIndex).getColor());
		sphere.setMaterial(ph);
		
		if (array[(int) x][(int) y] == 0) 
		{
			System.out.println((int) x + " " + (int) y);
			sphere.setTranslateX((x + 1) * cellSize);
			sphere.setTranslateY((y + 1) * cellSize + (cellSize / 2));
			array[(int) x][(int) y] = 1;
    		c.grid[(int)y][(int)x].setOrbs(1);
			root1[(int)y][(int)x].getChildren().add(sphere);

		} 

		else if (array[(int) x][(int) y] == 1 ) {
			sphere.setTranslateX((x + 1) * cellSize);
			sphere.setTranslateY((y + 1) * cellSize + (cellSize / 2));
			array[(int) x][(int) y] = 2;
			c.grid[(int)y][(int)x].setOrbs(2);

			
			animate((int)x,(int)y, sphere, array);

			root1[(int)y][(int)x].getChildren().add(sphere);

		} 

		else if (array[(int) x][(int) y] == 2) 
		{
			sphere.setTranslateX((x + 1) * cellSize);
			sphere.setTranslateY((y + 1) * cellSize + (cellSize / 2));
			array[(int) x][(int) y] = 3;
			c.grid[(int)y][(int)x].setOrbs(3);

			animate((int)x,(int)y, sphere, array);

			root1[(int)y][(int)x].getChildren().add(sphere);

		}

		

		return c.grid;
	}


	/**
	 * Used for animation such as rotation and translation
	 */
	public void animate(int x, int y, Sphere sphere, int array[][])
		{

			if(array[x][y]==2)
			{
				Circle circle = new Circle(12);
				circle.setFill(Color.TRANSPARENT);
				circle.setTranslateX((x + 1) * cellSize);
				circle.setTranslateY((y + 1) * cellSize + (cellSize / 2));
				Rotate rotate = new Rotate();
				rotate.setAngle(180);
				rotate.setPivotX(0);
				rotate.setPivotY(0);
				rotate.setAxis(Rotate.Y_AXIS);
				circle.getTransforms().addAll(rotate);
				
				animation = new ParallelTransition(createTransition(circle, sphere));
				animation.play();
			}
			else if(array[x][y]==3)
			{
				Circle circle = new Circle(12);
				circle.setFill(Color.TRANSPARENT);
				circle.setTranslateX((x + 1) * cellSize);
				circle.setTranslateY((y + 1) * cellSize + (cellSize / 2));
				Rotate rotate = new Rotate();
				rotate.setAngle(180);
				rotate.setAxis(Rotate.X_AXIS);
				rotate.setPivotX(2);
				rotate.setPivotY(2);
				circle.getTransforms().addAll(rotate);

				animation = new ParallelTransition(createTransition(circle, sphere));
				animation.play();
			}
		}



	/**
	 * Changes gridColor with every turn
	 */
	public void changeGridColor(Color color){
		for(int i = 0;i<list.size();i++){
			if(list.get(i) instanceof Line){
				((Line) list.get(i)).setStroke(color);
			}
		}
	}

	/**
	 * Undo Button
	 */
	public Button undoBtn;


	/**
	 * Creates grid using Lines
	 */
	public void createGrid(int rows, int cols,Color color1)
	{	undoBtn  = new Button("Undo");
		undoBtn.setPrefSize(120,40);
//		undoBtn.setFont(Font.font("Arial", FontWeight.SEMI_BOLD,18));
		undoBtn.setStyle("-fx-background-color: rgba(0,0,0,0.75),\n" +
				"                           rgba(255,255,255,0.75),\n" +
				"                           linear-gradient(to bottom,#383838 0%,#181818 100%);"+
				"-fx-background-insets: 5 5 5 5, 5 5 5 5, 1;"+
				"-fx-padding: 5px;"+
				"-fx-background-radius: 5px;"+
				"-fx-font: 15px \"Cambria\";" +
				"-fx-font-weight: 500;"+
				"-fx-border-radius: 2px;"+
				"-fx-text-fill: white");
		undoBtn.setLayoutX(400);
		undoBtn.setLayoutY(0);
		list.add(undoBtn);

		comboBox = new ComboBox<>();
		comboBox.setLayoutX(525);
		comboBox.setLayoutY(0);
		comboBox.getItems().addAll("New Game","Go to Main Menu");
		comboBox.setValue("Options");
		comboBox.setPrefSize(150,34);
		comboBox.setStyle("-fx-background-color:  #ffffff;"+
				"-fx-border-color: #383838;"+
				"-fx-border-width: 2px;"+
				"-fx-text-fill: white;"+
				"-fx-highlight-text-fill: white;"+
				"-fx-background-radius: 3px, 3px, 2px, 1px;"+
				"-fx-background-insets: 5 5 5 5, 5 5 5 5, 1;"+
				"-fx-padding: 5px;"+

				"-fx-font: 15px \"Cambria\";" +
				"-fx-border-radius: 5px");
		list.add(comboBox);

		calculateOffsetsForGrid(rows,cols);

		for(int i=0; i<rows+1; i++)
		{
			Line line= new Line();
			line.setStroke(color1);
			line.setStartX(xGridStart);
			line.setStartY(yGridStart+(cellSize*i));
			line.setEndX(xGridStart+(cellSize*cols));
			line.setEndY(yGridStart+(cellSize*i));
			list.add(line);
		}

		for(int i=0; i<rows+1; i++)
		{
			Line line= new Line();
			line.setStroke(color1);
			line.setStartX(xGridStart+xLeftShift);
			int effCell = cellSize - cellOffset;
			line.setStartY(yGridStart+yBottomShift+(effCell*i));
			line.setEndX((effCell*(cols)+(xLeftShift+xGridStart)));
			line.setEndY(yGridStart+yBottomShift+(effCell*i));
			list.add(line);
		}

		for(int i=0; i<cols+1; i++)
		{
			Line line= new Line();
			line.setStroke(color1);
			line.setStartX(xGridStart+(cellSize*i));
			line.setStartY(yGridStart);
			line.setEndX(xGridStart+(cellSize*i));
			line.setEndY(cellSize*(rows+1));
			list.add(line);
		}

		for(int i=0; i<cols+1; i++)
		{
			Line line= new Line();
			line.setStroke(color1);
			int effCell = cellSize - cellOffset;
			line.setStartX(xGridStart+xLeftShift+(effCell*i));
			line.setStartY(yGridStart+yBottomShift);
			line.setEndX(xGridStart+xLeftShift+(effCell*i));
			line.setEndY(effCell*(rows)+yGridStart+yBottomShift);
			list.add(line);
		}

		for(int i=0; i<rows+1; i++)
		{
			for(int j=0; j<cols+1; j++)
			{
				Line line= new Line();
				line.setStroke(color1);
				line.setStartX(xGridStart+(cellSize*j));
				line.setStartY(yGridStart+(cellSize*i));
				int effCell = cellSize - cellOffset;
				line.setEndX(xGridStart+xLeftShift+(effCell*j));
				line.setEndY(yGridStart+yBottomShift+(effCell*i));
				list.add(line);
			}
		}
		for(int i=0; i<rows; i++) {
			for (int j = 0; j < cols; j++) {
				list.add(root1[i][j]);
			}
		}

	}



}