
public class Hall {
	String filmName;
	String hallName;
	double price;
	Seat[][] seats;
	int row;
	int col;
	int income = 0;
	int userNumber = 0;
	public Hall(String filmName , String hallName , int price , int row,int col) {
		this.filmName = filmName;
		Main.movies.get(filmName).halls.put(hallName, this);
		Main.movies.get(filmName).hallNames.add(hallName);
		this.hallName = hallName;
		this.price = price;
		this.row = row;
		this.col = col;
		seats =  new Seat[row][col];
	}
	
}
