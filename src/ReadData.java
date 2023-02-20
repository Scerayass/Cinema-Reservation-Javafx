import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadData {
	static void start() {}
	static {
		String line;
		String line2;
		try {
			
			FileReader file = new FileReader("assets\\data\\backup.dat");
			BufferedReader readerBackup =  new BufferedReader(file);
			
			BufferedReader properties =  new BufferedReader(new FileReader("assets\\data\\properties.dat"));
			
			while((line = readerBackup.readLine()) != null ) {
				
				String id =  line.split("\t")[0];
				String[] info =  line.split("\t");
				switch (id) {
				case "user":
					User newUser = new User(info[1], info[2],Boolean.parseBoolean(info[3]), Boolean.parseBoolean(info[4]));
					Main.users.put(info[1],newUser);
					break;
				case "film":
					Film newFilm =  new Film(info[1], "assets\\trailers\\"+info[2], Integer.parseInt(info[3]),info[2]); 
					Main.movies.put(info[1], newFilm);
					Main.movienames.add(info[1]);
					break;
				case "hall":
					
					Hall newHall =  new Hall(info[1], info[2], Integer.parseInt(info[3]), Integer.parseInt(info[4]),Integer.parseInt(info[5]));
					Main.halls.put(info[2], newHall);
					Main.hallNames.add(info[2]);
					break;
				case "seat":
					Hall myHall = Main.halls.get(info[2]);
					if(info[5].equals("null")) {
						myHall.seats[Integer.parseInt(info[3]) ][Integer.parseInt(info[4])] = null;
					}
					else {
						myHall.seats[Integer.parseInt(info[3])][Integer.parseInt(info[4])] =  new Seat(info[5], Integer.parseInt(info[6]), info[2],Integer.parseInt(info[3]),Integer.parseInt(info[4]));
						
					}
					break;
				}
			}
			while((line2 = properties.readLine()) != null){
				if (line2.startsWith("maximum-error-without-getting-blocked=")) {
					int number = Integer.parseInt(line2.split("=")[1]);
					Main.maxError = number;
				}
				else if (line2.startsWith("title")) {
					String title = line2.split("=")[1];
					Main.title = title;
				}
				else if (line2.startsWith("discount-percentage")) {
					String discount =  line2.split("=")[1];
					Main.discountPercentage = Integer.parseInt(discount);
				}
				else if (line2.startsWith("block-time")) {
					String time =  line2.split("=")[1];
					Main.blockTime = Integer.parseInt(time);
				}
			}
			
		} catch (FileNotFoundException e) {
			User admin =  new User("admin", "password",true, true);
			Main.users.put("admin", admin);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
