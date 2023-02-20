
public class Seat implements userInterface{
	User user;
	String userName;
	int price;
	Hall hall;
	int row;
	int col;
	public Seat(String userName ,int price,String hall,int row,int col) {
		this.userName = userName;
		this.user = Main.users.get(userName);
		this.price =  price;
		this.hall =  Main.halls.get(hall);
		this.hall.income = price;
		this.hall.userNumber += 1;
		this.row = row;
		this.col = col;
	}
	public String getPrice() {
		
		return Integer.toString(price);
	}
	public String getUserName() {
		return userName;
	}
	public String getRow() {
		return Integer.toString(row+1);
	}
	public String getCol() {
		return Integer.toString(col+1);
	}
}
