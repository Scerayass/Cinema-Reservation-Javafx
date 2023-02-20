import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class WriteData {
	
	static void write() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("assets\\data\\backup.dat"));
			writer.flush();
			for(User i : Main.users.values()) {
				writer.write("user\t" + i.userName+"\t"+i.getPassword()+"\t"+i.clubMember+ "\t" +i.admin +"\n");
			}
			for(Film i : Main.movies.values()) {
				writer.write("film\t"+ i.name+"\t"+i.directPath+"\t"+i.duration+"\n");
			}
			for (Hall i : Main.halls.values()) {
				writer.write("hall\t"+ i.filmName +"\t"+i.hallName+"\t"+(int)i.price+"\t"+i.row+"\t"+i.col+"\n");
				for(int row = 0;row < i.seats.length; row++) {
					for(int col = 0 ; col < i.seats[0].length ; col++) {
						if(i.seats[row][col] == null) {
							writer.write("seat\t" + i.filmName+"\t"+i.hallName+"\t"+row+"\t"+col+"\tnull\t0\n");
						}
						else {
							writer.write("seat\t" + i.filmName+"\t"+i.hallName+"\t"+row+"\t"+col+"\t" +i.seats[row][col].userName+"\t"+i.seats[row][col].price+"\n");
						}
					}
				}
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
