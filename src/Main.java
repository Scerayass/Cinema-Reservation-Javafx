import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javafx.stage.Stage;


public class Main {
	static int maxError;
	static int discountPercentage;
	static String title;
	static int blockTime;
	
	// these datas are held for writing data and using in javafx class.
	static ArrayList<String> movienames =  new ArrayList<>();
	static LinkedHashMap<String, User> users =  new LinkedHashMap<>();
	static LinkedHashMap<String, Film> movies =  new LinkedHashMap<>();
	static LinkedHashMap<String, Hall> halls =  new LinkedHashMap<>();
	static ArrayList<String> hallNames =  new ArrayList<>();
	
	public static void main(String[] args) {
		
		ReadData.start();
		JavaFx javafx =  new JavaFx();
		javafx.main(args);
		WriteData.write();
	}

} 
