import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Film {
	String name;
	String path;
	String directPath;
	int duration;
	LinkedHashMap<String, Hall> halls =  new LinkedHashMap<String, Hall>();
	ArrayList<String> hallNames =  new ArrayList<>();
	
	public Film(String name, String path,int duration,String directPath) {
		this.name =  name;
		this.path = path;
		this.duration = duration;
		this.directPath = directPath;
	}
}
