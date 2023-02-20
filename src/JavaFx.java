import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import javax.activation.CommandObject;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.AudioEqualizer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JavaFx extends Application{
	Stage fx;
	Scene scene;
	int myError = 1;
	String selectedUser;
	ComboBox<String> adminBox;
	Long firstTime;
	Long endTime;
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		fx = primaryStage;
		
		Image image =  new Image("file:assets\\icons\\logo.png");
		fx.getIcons().add(image);
		fx.setTitle(Main.title);
		fx.setX(300);
		fx.setY(200);
		
		loginScene(new Label());
	}
	private void errorSound() {
		Media error = new Media(new File("assets\\effects\\error.mp3").toURI().toString());
		MediaPlayer sound =  new MediaPlayer(error);
		sound.play();
	}
	/**
	 * checks it is a integer or not and 
	 * returns boolean
	 * @param text
	 * @param durationTime
	 * @return
	 */
	private Boolean isInt(String text,Integer durationTime) {
		try {
			durationTime = Integer.parseInt(text);
			return false;
		} catch (Exception e) {
			return true;
		}
	}
	public void loginScene(Label error) {
		
		VBox vbox =  new VBox();
		Label label1 =  new Label("    Welcome to the HUCS Cinema Reservation System!\n"
				+ "   Please enter your credentials below and click LOGIN.\n"
				+ "You can create a new account by clicking SIGN UP button.");
		
		Button signupbt = new Button("SIGN UP");
		Button loginbt =  new Button("LOG IN");
		Label userlb = new Label("Username: ");
		TextField userNametx =  new TextField();
		userNametx.setPromptText("Username");
		Label passwordlb =  new Label("Password: ");
		PasswordField passwordtx =  new PasswordField();
		passwordtx.setPromptText("Password");
		GridPane FirstPage =  new GridPane();
		FirstPage.setPadding(new Insets(10,10,10,10));
		FirstPage.setVgap(10);
		FirstPage.setHgap(10);
		FirstPage.setAlignment(Pos.CENTER);
		FirstPage.add(userlb, 0, 1);;
		FirstPage.add(userNametx, 1, 1);
		FirstPage.add(passwordlb, 0, 2);
		FirstPage.add(passwordtx, 1, 2);
		FirstPage.add(loginbt, 0, 3);
		FirstPage.add(signupbt, 1, 3);
		
		vbox.getChildren().addAll(label1,FirstPage,error);
		vbox.setAlignment(Pos.CENTER);
		vbox.autosize();
		
		Scene loginScene =  new Scene(vbox,450,300);
		scene = loginScene;
		fx.setScene(scene);
		fx.show();
		
		signupbt.setOnAction(e -> {signupScene(new Label());});
		loginbt.setOnAction(e -> {
			endTime =  System.currentTimeMillis();
			
			if(myError == 1  && firstTime != null && (endTime-firstTime)/1000.0 < Main.blockTime) {
				errorSound();
				firstTime = System.currentTimeMillis();
				loginScene(new Label("ERROR: Please wait until end of the " + Main.blockTime+" seconds to make a new operation!"));
			}
			else if(myError == Main.maxError) {
				
				firstTime = System.currentTimeMillis();
				
				myError = 1;
				errorSound();
				loginScene(new Label("ERROR: Please wait until end of the " + Main.blockTime+" seconds to make a new operation!"));
			}
			else {
				firstTime = null;
				if (userNametx.getText().isEmpty()) {
					myError++;
					loginScene(new Label("ERROR: Username cannot be empty!"));
					errorSound();
				}
				else if (passwordtx.getText().isEmpty()) {
					myError++;
					loginScene(new Label("ERROR: Password cannot be empty!"));
					errorSound();
				}
				else if( Main.users.keySet().contains(userNametx.getText()) &&  User.hashPassword(passwordtx.getText()).equals(Main.users.get(userNametx.getText()).getPassword())) {
					myError = 1;
					if(Main.users.get(userNametx.getText()).admin) {
						adminLogin(userNametx.getText());
					}
					else {
						userLogin(userNametx.getText());
					}
				}
				else {
					myError++;
					loginScene(new Label("ERROR: There is no such a credential!"));
					errorSound();
			}
			}
		} );
		
	}
	public void signupScene(Label error) {
		VBox vBox= new VBox();
		
		Button signupbt = new Button("SIGN UP");
		Button loginbt =  new Button("LOG IN");
		Label userlb = new Label("Username: ");
		TextField userNametx =  new TextField();
		userNametx.setPromptText("Username");
		Label passwordlb1 =  new Label("Password: ");
		Label passwordlb2 =  new Label("Password: ");
		PasswordField passwordtx1 =  new PasswordField();
		passwordtx1.setPromptText("Password");
		PasswordField passwordtx2 =  new PasswordField();
		passwordtx2.setPromptText("Password");
		Label myLabel = new Label(" Welcome to the HUCS Cinema Reservation System!\n"
				+ "     Fill the form below to create a new account\n"
				+ "You can go Log In page by clicking LOG IN Button.");
		GridPane newGrid =  new GridPane();
		newGrid.setPadding(new Insets(10,10,10,10));
		newGrid.setVgap(10);
		newGrid.setHgap(10);
		newGrid.setAlignment(Pos.CENTER);
		newGrid.add(userlb, 0, 1);
		newGrid.add(userNametx, 1, 1);
		newGrid.add(passwordlb1, 0, 2);
		newGrid.add(passwordtx1, 1, 2);
		newGrid.add(passwordlb2, 0, 3);
		newGrid.add(passwordtx2, 1, 3);
		newGrid.add(loginbt, 0, 4);
		newGrid.add(signupbt, 1, 4);

		vBox.getChildren().addAll(myLabel,newGrid,error);
		vBox.setAlignment(Pos.CENTER);
		loginbt.setOnAction(e -> loginScene(new Label()));
		signupbt.setOnAction(e -> {
			if(!Main.users.keySet().contains(userNametx.getText()) & !userNametx.getText().isEmpty())
				if(passwordtx1.getText().equals(passwordtx2.getText())&& !passwordtx1.getText().isEmpty()) {
					User newUser = new User(userNametx.getText(), passwordtx1.getText(), false, false);
					Main.users.put(userNametx.getText(), newUser);
					signupScene(new Label("SUCCESS: You have successfully registered with your new credentials!"));
				}
				else if (passwordtx1.getText().isEmpty()) {
					signupScene(new Label("ERROR: Password cannot be empty!"));
					errorSound();
				}
				else {
					signupScene(new Label("ERROR: Passwords do not match!"));
					errorSound();
				}
			else if (userNametx.getText().isEmpty()) {
				signupScene(new Label("ERROR: Username cannot be empty!"));
				errorSound();
			}
			else {
				signupScene(new Label("ERROR: This username already exists!"));
				errorSound();
			}
			
		});
		Scene myScene =  new Scene(vBox,500,350);
		fx.setScene(myScene);
		fx.show();
		
		
	}
	public void adminLogin(String Username) {
		HBox top = new HBox();
		HBox center1 =  new HBox();
		HBox center2 =  new HBox();
		HBox bottom =  new HBox();
		center1.setSpacing(20);
		center2.setSpacing(20);;
		Label welcome =  new Label("  Welcome admin (Admin -Club Member)!\n"
				+ "You can eiher select film below or do edits.");
		ComboBox<String> comboBox = new ComboBox<>();
		if (Main.movienames.size()!=0) {
			comboBox.getItems().addAll(Main.movies.keySet());
			comboBox.setPromptText(Main.movienames.get(0));
			comboBox.getSelectionModel().selectFirst();
		}
		
		Button ok =  new Button("OK");
		Button addFilm =  new Button("Add Film");
		Button removeFilm =  new Button("Remove Film");
		Button edit = new Button("Edit Users");
		Button logOut =  new Button("LOG OUT");
		
		center1.autosize();
		top.getChildren().add(welcome);top.setAlignment(Pos.CENTER);
		center1.getChildren().addAll(comboBox,ok);center1.setAlignment(Pos.CENTER);
		center2.getChildren().addAll(addFilm,removeFilm,edit);center2.setAlignment(Pos.CENTER);
		bottom.getChildren().add(logOut);bottom.setAlignment(Pos.CENTER);
		
		VBox vBox =  new VBox();
		vBox.getChildren().addAll(top,center1,center2,bottom);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);
		Scene scene = new Scene(vBox,450,225);  
		fx.setScene(scene);
		fx.show();
		logOut.setOnAction(e -> loginScene(new Label()));
		addFilm.setOnAction(e -> addFilm(Username,new Label()));	
		removeFilm.setOnAction(e -> removeFilm(Username));
		edit.setOnAction(e -> editUser(Username));
		ok.setOnAction(e -> {try {
			okAdmin(Username, comboBox.getValue());
		} catch (Exception e2) {
			adminLogin(Username);
		}});
	}
	public void userLogin(String username) {
		HBox top =  new HBox();
		HBox center =  new HBox();
		HBox bottom =  new HBox();
		center.setSpacing(20);
		Label welcome =  new Label("                       Welcome " + username +"!\n"
				+ "Select a film and then click OK to continue.");
		ComboBox<String> comboBox = new ComboBox<>();
		if (Main.movienames.size()!=0) {
			comboBox.getItems().addAll(Main.movies.keySet());
			comboBox.setPromptText(Main.movienames.get(0));
			comboBox.getSelectionModel().selectFirst();
		}
		Button ok = new Button("OK");
		Button logOut =  new Button("LOG OUT");
		top.getChildren().add(welcome);top.setAlignment(Pos.CENTER);
		center.getChildren().addAll(comboBox,ok);center.setAlignment(Pos.CENTER);
		bottom.getChildren().add(logOut); bottom.setAlignment(Pos.CENTER);
		
		VBox vBox =  new VBox();
		vBox.getChildren().addAll(top,center,bottom);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);
		Scene scene = new Scene(vBox,450,225);  
		logOut.setOnAction(e -> {
			loginScene(new Label());
		});
		ok.setOnAction(e ->{try {
			okUser(username, comboBox.getValue());
		} catch (Exception e2) {
			userLogin(username);
		}} );
		fx.setScene(scene);
		fx.show();
		
	}
	public void addFilm(String Username,Label error) {
		GridPane newGrid  =  new GridPane();
		
		Label welcome =  new Label("Please give name, relative path of the trailer and duration of the film.");
		Label name = new Label("Name: ");
		Label trailer =  new Label("Trailer(Path):");
		Label duration =  new Label("Duration(m)");
		
		Button back =  new Button("◀ BACK");
		Button OK = new Button("OK");
		
		TextField nametx = new TextField();
		nametx.setPromptText("Name");
		TextField trailertx =  new TextField();
		trailertx.setPromptText("Trailer");
		TextField durationtx =  new TextField();
		durationtx.setPromptText("Duration(positive number)");
		Integer durationTime = 0;
		
		
		OK.setOnAction(e -> {
			if (nametx.getText().isEmpty()) {
				errorSound();
				addFilm(Username, new Label("ERROR: Film name could not be empty!"));
			}
			else if (trailertx.getText().isEmpty()) {
				errorSound();
				addFilm(Username, new Label("ERROR: Trailer path could not be empty!"));
			}
			else if (durationtx.getText().isEmpty() | isInt(durationtx.getText(), durationTime) ) {
				errorSound();
				addFilm(Username, new Label("ERROR: Duration has to be a positive integer!"));
			}
			else {
				try {
					
					Film newFilm =  new Film(nametx.getText(), "assets\\trailers\\"+ trailertx.getText(),Integer.parseInt(durationtx.getText()), trailertx.getText());
					File error2 =  new File("assets\\trailers\\"+ trailertx.getText());
					if(!error2.exists()) {throw new Exception();}
					
					Main.movies.put(nametx.getText(), newFilm);
					Main.movienames.add(nametx.getText());
					addFilm(Username, new Label("SUCCESS: Film added successfully!"));
				} catch (Exception e2) {
					
					errorSound();
					addFilm(Username,new Label("ERROR: There is no such trailer!"));	
				}
			}
			
		});
		newGrid.setHgap(10);
		newGrid.setVgap(10);
		newGrid.add(name, 0, 1);
		newGrid.add(nametx, 1, 1);
		newGrid.add(trailer, 0, 2);
		newGrid.add(trailertx, 1, 2);
		newGrid.add(duration, 0, 3);
		newGrid.add(durationtx, 1, 3);
		newGrid.add(back, 0, 4);
		newGrid.add(OK, 1, 4);
		newGrid.setAlignment(Pos.CENTER);
		
		VBox vBox =  new VBox();
		vBox.getChildren().addAll(welcome,newGrid,error);
		vBox.setAlignment(Pos.CENTER);
		Scene newScene =  new Scene(vBox,490,240);
		fx.setScene(newScene);
		fx.show();
		back.setOnAction(e -> adminLogin(Username));
	}
	public void removeFilm(String Username) {
		HBox hBox =  new HBox();
		
		Label welcome =  new Label("Select the film that you desire to remove and then click OK.");
		ComboBox<String> comboBox = new ComboBox<>();
		
		if (Main.movienames.size()!=0) {
			comboBox.getItems().addAll(Main.movies.keySet());
			comboBox.setPromptText(Main.movienames.get(0));
			comboBox.getSelectionModel().selectFirst();
		}
		
		Button back =  new Button("◀ BACK");
		Button ok =  new Button("OK");
		hBox.getChildren().addAll(back,ok);
		hBox.setSpacing(15);hBox.setAlignment(Pos.CENTER);
		
		VBox vBox =  new VBox();
		vBox.getChildren().addAll(welcome,comboBox,hBox);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(12);
		
		back.setOnAction(e -> adminLogin(Username));
		ok.setOnAction(e -> {
			if (Main.movienames.size()>= 1) {
				Film thisFilm= Main.movies.get(comboBox.getValue());
				//for()
				
				for(String i : thisFilm.hallNames) {
					Main.halls.remove(i);
					Main.hallNames.remove(i);
				}
				Main.movies.remove(comboBox.getValue());
				Main.movienames.remove(comboBox.getValue());
				
				removeFilm(Username);
			}
			
		});
		
		Scene newScene = new Scene(vBox , 450,200);
		fx.setScene(newScene);
		fx.show();
		
		
	}
	public void editUser(String Username) {
		TableView<User> table =  new TableView<>();
		
		ObservableList<User> users = FXCollections.observableArrayList();
		users.addAll(Main.users.values());
		users.remove(Main.users.get(Username));
		
		TableColumn<User,String> NameCol = new TableColumn<>("Username");
		NameCol.setMinWidth(150);
		NameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
		
		TableColumn<User,String> clubMemberCol =  new TableColumn("Club Member");
		clubMemberCol.setMinWidth(150);
		clubMemberCol.setCellValueFactory(new PropertyValueFactory<>("clubMember"));
		
		TableColumn<User,String> adminCol =  new TableColumn("Admin");
		adminCol.setMinWidth(100);
		adminCol.setCellValueFactory(new PropertyValueFactory<>("admin"));
		
		table.setItems(users);
		table.getColumns().addAll(NameCol,clubMemberCol,adminCol);
		table.getSelectionModel().selectFirst();
		
		Button back =  new Button("◀ BACK");
		Button clubbt =  new Button("Promote/Demote Club Member");
		Button adminbt =  new Button("Promote/Demote Admin");
		HBox hBox =  new HBox();
		hBox.getChildren().addAll(back,clubbt,adminbt);
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(10);
		
		VBox vBox =  new VBox();
		vBox.getChildren().addAll(table,hBox);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(15));
		
		back.setOnAction(e -> adminLogin(Username));
		clubbt.setOnAction(e -> {
			User userSelected =  table.getSelectionModel().getSelectedItem();
			if (userSelected.clubMember == true) {
				userSelected.clubMember = false;
			}
			else {
				userSelected.clubMember = true;
			}
			editUser(Username);
		});
		adminbt.setOnAction(e -> {
			User userSelected =  table.getSelectionModel().getSelectedItem();
			if (userSelected.admin == true) {
				userSelected.admin = false;
			}
			else {
				userSelected.admin = true;
			}
			editUser(Username);
		});
		Scene newScene =  new Scene(vBox,600,450);
		fx.setScene(newScene);
		fx.show();
		
	}
	public void okAdmin(String Username,String film) {
		
		String MediaUrl =  Main.movies.get(film).path;
		String duration =  Integer.toString(Main.movies.get(film).duration);
		
		selectedUser = Username;
		adminBox =  new ComboBox<>();
		adminBox.getItems().addAll(Main.users.keySet());
		adminBox.setPromptText(selectedUser);
		adminBox.getSelectionModel().select(selectedUser);
		Media m = new Media(Paths.get(MediaUrl).toUri().toString());
		//Media media = new Media(MediaUrl);
		MediaPlayer mediaPlayer =  new MediaPlayer(m);
		MediaView mediaView =  new MediaView(mediaPlayer);
		
		VBox buttonsVBox =  new VBox();
		VBox mediaVbox =  new VBox();
		HBox bottomButtons =  new HBox();
		
		Label welcome =  new Label(film + " (" + duration +" minutes)");
		Button back =  new Button("◀ BACK");
		Button addHall =  new Button("Add Hall");
		Button removeHall =  new Button("Remove Hall");
		Button ok =  new Button("OK");
		Button play =  new Button("  ▶  ");
		Button goBack =  new Button("<<");
		Button goForward =  new Button(">>");
		Button goZero = new Button("|<<");
		
		back.setOnAction(e -> {adminLogin(Username);mediaPlayer.pause();} );
		play.setOnAction(e -> {
			if(play.getText().equals("  ▶  ")) {
				mediaPlayer.play();
				play.setText(" | | ");
			}
			else {
				mediaPlayer.pause();
				play.setText("  ▶  ");
			}
		});
		
		Slider vlSlider =  new Slider(0,1,0.5);
		vlSlider.setPrefHeight(150);
		vlSlider.setMaxWidth(Region.USE_PREF_SIZE);
		vlSlider.setMinHeight(50);
		vlSlider.setOrientation(Orientation.VERTICAL);
		mediaPlayer.volumeProperty().bind(vlSlider.valueProperty().divide(1));
		
		ComboBox<String> comboBox = new ComboBox<>();
		if (Main.movies.get(film).hallNames.size() != 0) {
			comboBox.getItems().addAll(Main.movies.get(film).halls.keySet());
			comboBox.setPromptText(Main.movies.get(film).hallNames.get(0));
			comboBox.getSelectionModel().selectFirst();
		}
		
		bottomButtons.getChildren().addAll(back,addHall,removeHall,comboBox,ok);
		bottomButtons.setSpacing(10);
		bottomButtons.setAlignment(Pos.CENTER);
		
		buttonsVBox.getChildren().addAll(play,goBack,goForward,goZero,vlSlider);
		buttonsVBox.setSpacing(10);
		buttonsVBox.setAlignment(Pos.CENTER);
		
		mediaVbox.getChildren().addAll(welcome,mediaView,bottomButtons);
		mediaVbox.setSpacing(10);
		mediaVbox.setAlignment(Pos.CENTER);
		
		goZero.setOnAction(e -> mediaPlayer.seek(Duration.ZERO));
		goForward.setOnAction(e -> mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(5))));
		goBack.setOnAction(e -> mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-5))));
		addHall.setOnAction(e ->{addHall(Username, film,new Label());mediaPlayer.pause();} );
		removeHall.setOnAction(e -> {removeHall(Username, film);mediaPlayer.pause();});
		ok.setOnAction(e -> {
			try {
				adminHallRoom(Username, film,new Label("\n"),new Label("\n"),comboBox.getValue());
			} catch (Exception e2) {
				errorSound();
				okAdmin(Username, film);
			}
			mediaPlayer.pause();});
		
		HBox sceneHBox =  new HBox();
		sceneHBox.getChildren().addAll(mediaVbox,buttonsVBox);
		sceneHBox.setSpacing(10);
		sceneHBox.setAlignment(Pos.CENTER);
		
		Scene newScene =  new Scene(sceneHBox,1400,830);
		fx.setScene(newScene);
		fx.setAlwaysOnTop(true);
		fx.show();
	}
	public void okUser(String Username,String film) {

		String MediaUrl =  Main.movies.get(film).path;
		String duration =  Integer.toString(Main.movies.get(film).duration);
		
		Media m = new Media(Paths.get(MediaUrl).toUri().toString());
		//Media media = new Media(MediaUrl);
		MediaPlayer mediaPlayer =  new MediaPlayer(m);
		MediaView mediaView =  new MediaView(mediaPlayer);
		
		VBox buttonsVBox =  new VBox();
		VBox mediaVbox =  new VBox();
		HBox bottomButtons =  new HBox();
		
		Label welcome =  new Label(film + " (" + duration +" minutes)");
		Button back =  new Button("◀ BACK");
		Button ok =  new Button("OK");
		Button play =  new Button("  ▶  ");
		Button goBack =  new Button("<<");
		Button goForward =  new Button(">>");
		Button goZero = new Button("|<<");
		
		ComboBox<String> comboBox = new ComboBox<>();
		if (Main.movies.get(film).hallNames.size() != 0) {
			comboBox.getItems().addAll(Main.movies.get(film).halls.keySet());
			comboBox.setPromptText(Main.movies.get(film).hallNames.get(0));
			comboBox.getSelectionModel().selectFirst();
		}

		back.setOnAction(e -> {userLogin(Username);mediaPlayer.pause();} );
		play.setOnAction(e -> {
			if(play.getText().equals("  ▶  ")) {
				mediaPlayer.play();
				play.setText(" | | ");
			}
			else {
				mediaPlayer.pause();
				play.setText("  ▶  ");
			}
		});
		goZero.setOnAction(e -> mediaPlayer.seek(Duration.ZERO));
		goForward.setOnAction(e -> mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(5))));
		goBack.setOnAction(e -> mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-5))));
		ok.setOnAction(e -> {
			try {
				 userHallRoom(Username, film, new Label("\n"),comboBox.getValue()); mediaPlayer.pause();
			} catch (Exception e2) {
				errorSound();
				okUser(Username, film);
			}mediaPlayer.pause();});
		
		
		Slider vlSlider =  new Slider(0,1,0.5);
		vlSlider.setPrefHeight(150);
		vlSlider.setMaxWidth(Region.USE_PREF_SIZE);
		vlSlider.setMinHeight(50);
		vlSlider.setOrientation(Orientation.VERTICAL);
		mediaPlayer.volumeProperty().bind(vlSlider.valueProperty().divide(1));
		
		
		
		bottomButtons.getChildren().addAll(back,comboBox,ok);
		bottomButtons.setSpacing(10);
		bottomButtons.setAlignment(Pos.CENTER);
		
		buttonsVBox.getChildren().addAll(play,goBack,goForward,goZero,vlSlider);
		buttonsVBox.setSpacing(10);
		buttonsVBox.setAlignment(Pos.CENTER);
		
		mediaVbox.getChildren().addAll(welcome,mediaView,bottomButtons);
		mediaVbox.setSpacing(10);
		mediaVbox.setAlignment(Pos.CENTER);
		
		HBox sceneHBox =  new HBox();
		sceneHBox.getChildren().addAll(mediaVbox,buttonsVBox);
		sceneHBox.setSpacing(10);
		sceneHBox.setAlignment(Pos.CENTER);
		
		Scene newScene =  new Scene(sceneHBox,1400,830);
		fx.setScene(newScene);
		fx.setAlwaysOnTop(true);
		fx.show();
	}
	public void addHall(String Username,String film,Label error) {
		GridPane myGrid =  new GridPane();
		
		ArrayList<String> rowNumbers = new ArrayList<>();
		ArrayList<String> colNumbers =  new ArrayList<>();
		for(int i =3; i < 11; i++) {
			rowNumbers.add(Integer.toString(i));
			colNumbers.add(Integer.toString(i));
		}
		Label welcome =  new Label(film +" (" + Main.movies.get(film).duration+" minutes)");
		Label rowlb = new Label("Row:         ");
		Label collb =  new Label("Column:         ");
		Label name =  new Label("Name: ");
		Label price =  new Label("Price: ");
		
		ComboBox<String> rowBox = new ComboBox<>();
		rowBox.getItems().addAll(rowNumbers);
		rowBox.setPromptText(rowNumbers.get(0));
		rowBox.getSelectionModel().selectFirst();
		
		ComboBox<String> colBox =  new ComboBox<>();
		colBox.getItems().addAll(colNumbers);
		colBox.setPromptText(colNumbers.get(0));
		colBox.getSelectionModel().selectFirst();
		
		TextField nametx =  new TextField();
		nametx.setPromptText("Name");
		TextField pricetx  =  new TextField();
		pricetx.setPromptText("Price");
		
		Button back = new Button("◀ BACK");
		Button OK =  new Button("OK");
		back.setOnAction(e -> okAdmin(Username, film));
		myGrid.add(rowlb, 0, 1);
		myGrid.add(rowBox, 1, 1);
		myGrid.add(collb, 0, 2);
		myGrid.add(colBox, 1, 2);
		myGrid.add(name, 0, 3);
		myGrid.add(nametx, 1, 3);
		myGrid.add(price, 0, 4);
		myGrid.add(pricetx, 1, 4);
		myGrid.setVgap(15);
		myGrid.setHgap(15);
		myGrid.setAlignment(Pos.CENTER);
		
		HBox hBox =  new HBox();
		hBox.getChildren().addAll(back,OK);
		hBox.setSpacing(150);hBox.setAlignment(Pos.CENTER);
		
		VBox vBox =  new VBox();
		vBox.getChildren().addAll(welcome,myGrid,hBox,error);
		vBox.setSpacing(15);
		vBox.setAlignment(Pos.CENTER);
		OK.setOnAction(e -> {
			if (!Main.hallNames.contains(nametx.getText()) && !nametx.getText().equals("")) {
				try {
					int newPrice =  Integer.parseInt(pricetx.getText());
					if(newPrice > 1) {
						Hall newHall =  new Hall(film, nametx.getText(), newPrice, Integer.parseInt(rowBox.getValue()),Integer.parseInt(colBox.getValue()));
						Main.halls.put(nametx.getText(), newHall);
						Main.hallNames.add(nametx.getText());
						addHall(Username, film, new Label("SUCCESS: " + nametx.getText()+" succesfully added to film's halls!"));
					}
					else {
						throw new Exception();
					}
				} catch (Exception e2) {
					errorSound();
					addHall(Username, film, new Label("ERROR: Price has to be a positive integer!"));
				}
			}
			else {
				errorSound();
				if(nametx.getText().equals("")) {
					addHall(Username, film,new Label("ERROR: Hallname cannot be empty!"));
				}
				else {
					addHall(Username, film,new Label("ERROR: This Hallname is already used!"));
				}
				
			}
		});
		Scene newScene =  new Scene(vBox,400,380);
		fx.setScene(newScene);
		fx.show();
	}
	public void removeHall(String Username,String film) {
		VBox vBox =  new VBox();
		HBox hBox =  new HBox();
		
		Label welcome =  new Label("Select the hall that you desire to remove from " + film+" and then click OK.");
		Button back =  new Button("◀ BACK");
		Button ok =  new Button("OK");
		back.setOnAction(e -> okAdmin(Username, film));
		
		
		ComboBox<String> comboBox = new ComboBox<>();
		if (Main.movies.get(film).hallNames.size() != 0) {
			comboBox.getItems().addAll(Main.movies.get(film).halls.keySet());
			comboBox.setPromptText(Main.movies.get(film).hallNames.get(0));
			comboBox.getSelectionModel().selectFirst();
		}
		ok.setOnAction(e -> {
			if(Main.movies.get(film).hallNames.size() != 0){
				Main.movies.get(film).hallNames.remove(comboBox.getValue());
				Main.movies.get(film).halls.remove(comboBox.getValue());
				Main.halls.remove(comboBox.getValue());
				Main.hallNames.remove(comboBox.getValue());
				removeHall(Username, film);
			}
		});
		
		hBox.getChildren().addAll(back,ok);
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(10);
		
		vBox.getChildren().addAll(welcome,comboBox,hBox);
		vBox.setSpacing(10);
		vBox.setAlignment(Pos.CENTER);
		
		Scene newScene =  new Scene(vBox,590,195);
		fx.setScene(newScene);
		fx.show();
	}
	public void adminHallRoom (String Username,String film,Label label1,Label label2,String hallName) throws Exception{
		int duration = Main.movies.get(film).duration;
		Label welcome =  new Label(film + " ("+ duration+" Minutes) Hall: "+hallName);
		Button back =  new Button("◀ BACK");
		selectedUser = adminBox.getValue();
		
		back.setAlignment(Pos.BASELINE_LEFT);
		back.setOnAction(e -> okAdmin(Username, film));
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(20,20,20,20));
		vBox.getChildren().add(welcome);
		
 		Seat[][] seats =  Main.halls.get(hallName).seats;
 		HBox[] hBoxs =  new HBox[seats.length];
 		for(int i = 0 ; i < seats.length; i++) {
 			hBoxs[i] =  new HBox();
 		}
 		Image empty = new Image("file:assets\\icons\\empty_seat.png",50,50,true,true);
 		Image reserved =  new Image("file:assets\\icons\\reserved_seat.png",50,50,true,true);
 		for(int i = 0 ; i < seats.length; i++ ) {
			HBox hBox =  hBoxs[i];
			hBox.setSpacing(10);
			hBox.setAlignment(Pos.CENTER);
			
 			for(int j = 0 ; j < seats[0].length; j++) {
 				ImageView reservedSeat = new ImageView(reserved);
 				ImageView emptySeat =  new ImageView(empty);
 				Button newButton =  new Button();
 				hBox.getChildren().add(newButton);
 				final Integer k = i;
				final Integer l = j;
				//String user = adminBox.getValue();
				Label notBought=  new Label("Not bought yet!");
 				//String notBought=  new Label("Not bought yet!");
				newButton.setOnMouseMoved(e -> {
 					if(seats[k][l] == null) {
 						//adminHallRoom(Username, film, notBought, label2, hallName);
 						label1.setText("Not bought yet!");
 					}
 					else if(seats[k][l] != null){
 						Label bought = new Label("Bought by "+seats[k][l].userName+ " for "+ seats[k][l].price+" TL!");
						label1.setText("Bought by "+seats[k][l].userName+ " for "+ seats[k][l].price+" TL!");
 						//adminHallRoom(Username, film, bought, label2, hallName);
					}
				});
 				newButton.setOnMouseExited(e ->label1.setText("\n"));
 				if(seats[k][l] == null) {
					newButton.setGraphic(emptySeat);
					newButton.setOnAction(e ->{
						double  price = Main.halls.get(hallName).price;
						
						if (Main.users.get(adminBox.getValue()).clubMember) {
							price = Math.round(Main.halls.get(hallName).price -  (Main.halls.get(hallName).price*Main.discountPercentage)/100);
						}
						seats[k][l] =  new Seat(adminBox.getValue(), (int)price,hallName,k,l);
						newButton.setGraphic(reservedSeat);
						Label newLabel2 = new Label("Seat at "+(k+1)+"-"+(l+1)+" bought for "+adminBox.getValue()+" for "+(int)price+ " TL successfully!");
						//label2.setText("Seat at "+(k+1)+"-"+(l+1)+" bought for "+user+" for "+(int)price+ " TL successfully!");
						try {
							adminHallRoom(Username, film, notBought, newLabel2, hallName);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					});
				}
				else if(seats[k][l] != null){
					newButton.setGraphic(reservedSeat);
					newButton.setOnAction(e -> {
						newButton.setGraphic(emptySeat);
						Label newLabel2 =  new Label("Seat at "+(k+1)+"-"+(l+1)+" is refunded to "+seats[k][l].userName +" successfully!");
						seats[k][l] = null;
						//label2.setText("Seat at "+(k+1)+"-"+(l+1)+" is refunded to "+seats[k][l].userName +" successfully!");
						try {
							adminHallRoom(Username, film, notBought, newLabel2, hallName);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					});
				}
			}
 			vBox.getChildren().add(hBox);
		}
 		Button Stats =  new Button("Stats");
 		Stats.setOnAction(e -> stats(Username, film, hallName, seats));
 		
 		HBox newhBox = new HBox();
 		newhBox.setAlignment(Pos.CENTER);
 		newhBox.setSpacing(20);
 		newhBox.getChildren().addAll(adminBox,Stats);
 		
 		vBox.getChildren().addAll(newhBox,label1,label2,back);
 		Scene myScene =  new Scene(vBox);
 		fx.setScene(myScene);
 		fx.show();
	}
	public void userHallRoom(String Username,String film,Label label,String hallName) throws Exception {
		int duration = Main.movies.get(film).duration;
		Label welcome =  new Label(film + " ("+ duration+" Minutes) Hall: "+hallName);
		Button back =  new Button("◀ BACK");
		back.setOnAction(e -> okUser(Username, film));
		
		VBox vBox =  new VBox();
		vBox.setSpacing(10);
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(20,20,20,20));
		vBox.getChildren().add(welcome);
		
		Seat[][] seats =  Main.halls.get(hallName).seats;
		HBox[] hBoxs =  new HBox[seats.length];
		Image emptyImage = new Image("file:assets\\icons\\empty_seat.png",50,50,true,true);
 		Image reservedImage =  new Image("file:assets\\icons\\reserved_seat.png",50,50,true,true);
		for(int i = 0 ; i < seats.length; i++) {
 			hBoxs[i] =  new HBox();
 			for(int j = 0; j < seats[0].length ; j++) {
 				
 			}
 		}
		for(int i = 0 ; i < seats.length; i++ ) {
			HBox hBox =  hBoxs[i];
			hBox.setSpacing(10);
			hBox.setAlignment(Pos.CENTER);
			
 			for(int j = 0 ; j < seats[0].length; j++) {
 				final Integer k = i;
				final Integer l = j;
 				Button newButton =  new Button();
 				ImageView empty = new ImageView(emptyImage);
 				ImageView reserved = new ImageView(reservedImage);
 				hBox.getChildren().add(newButton);
 				
 				if(seats[i][j] == null) {
 					newButton.setGraphic(empty);
 					newButton.setOnAction(e -> {
 						newButton.setGraphic(empty);
 						double  price = Main.halls.get(hallName).price;
						if (Main.users.get(Username).clubMember) {price = Math.round(Main.halls.get(hallName).price -  (Main.halls.get(hallName).price*Main.discountPercentage)/100);}
 						seats[k][l] =  new Seat(Username, (int)price, hallName,k,l);
 						try {
							userHallRoom(Username, film, new Label("Seat at "+(k+1)+"-"+(l+1)+" bought for "+Username+" for "+(int)price+ " TL successfully!"), hallName);
						} catch (Exception e1) {
							e1.printStackTrace();
						}	
 					});
 				}
 				else {
 					if(seats[i][j].user == Main.users.get(Username)) {
 						newButton.setGraphic(reserved);
 						newButton.setOnAction(e -> {
 							ImageView newEmpty = new ImageView(emptyImage);
 							newButton.setGraphic(newEmpty);
 							seats[k][l] = null;
 							Label refund = new Label("Seat at "+(k+1)+"-"+(l+1)+" is refunded to "+Username +" successfully!");
 							try {
								userHallRoom(Username, film ,refund, hallName);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
 						});
 					}
 					else {
						newButton.setGraphic(reserved);
						newButton.setDisable(true);
					}
				}
 			}
 			vBox.getChildren().add(hBox);
 		}
		vBox.getChildren().addAll(label,back);
		
		Scene myScene =  new Scene(vBox);
 		fx.setScene(myScene);
 		fx.show();
	}
	public void stats(String Username,String film,String hallName,Seat[][] seats) {
		Label welcome =  new Label(hallName + " stats page");

		TableView<Seat> table =  new TableView<>();
		ObservableList<Seat> TotalSeat = FXCollections.observableArrayList();
		int index = 0;
		int income = 0;
		for(Seat[] i : seats) {
			for(Seat j: i) {
				if(j != null) {TotalSeat.add(j); index++; income+= j.price;}
			}
		}
		TableColumn<Seat, String> userNames =  new TableColumn<>("Users");
		userNames.setMinWidth(140);
		userNames.setCellValueFactory(new PropertyValueFactory<>("userName"));
		
		TableColumn<Seat, String> row =  new TableColumn<>("Row");
		row.setMinWidth(140);
		row.setCellValueFactory(new PropertyValueFactory<>("row"));
		
		TableColumn<Seat, String> col =  new TableColumn<>("Column");
		col.setMinWidth(140);
		col.setCellValueFactory(new PropertyValueFactory<>("col"));
		
		TableColumn<Seat, String> price =  new TableColumn<>("price");
		price.setMinWidth(140);
		price.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		table.setItems(TotalSeat);
		table.getColumns().addAll(userNames,row,col,price);
		
		Label seat =  new Label("Total sold seat : " + index);
		Label incomelb =  new Label("Total income : " + income +"TL");
		
		Button back =  new Button("◀ BACK");
		back.setOnAction(e -> {try {
			adminHallRoom(Username, film, new Label("\n"), new Label("\n"), hallName);
		} catch (Exception e2) {
			okAdmin(Username, film);
		}});
		back.setAlignment(Pos.CENTER_LEFT);
		VBox vBox =  new VBox();
		vBox.getChildren().addAll(welcome,table,seat,incomelb,back);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(15));
		
		Scene newScene =  new Scene(vBox,600,450);
		fx.setScene(newScene);
		fx.show();
	}
}


