
package application.client;

import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.SocketStream;

public class ShareUI {
	
	private final Stage primaryStage = new Stage();
	private final SocketStream server;
	private final String me;
	private final TextField word;
	
	public ShareUI(SocketStream server, String me, TextField word) {
		this.server = server;
		this.me = me;
		this.word = word;
		start();
	}
	
	private String show;
	private int showState = 0;
	private static final int ShowUser = 1;
	private static final int ShowWord = 2;
	
	public void start() {
		// 布局
		GridPane rootPane = new GridPane();
		
		ListView<String> lstShow = new ListView<>();
		Button btnUser = new Button("User");
		Button btnWord = new Button("Word");
		Button btnShare = new Button("");
		btnShare.setDisable(true);
		
		lstShow.getSelectionModel().selectedItemProperty()
			.addListener((o, s1, s2) -> {
				switch (showState) {
				case ShowUser:
					show = s2;
					break;
				case ShowWord:
					show = s2;
					break;
				}
			});
		
		btnUser.setOnMouseClicked(e -> {
			if (showState != ShowUser) {
				lstShow.getSelectionModel().select(0);
				show = "";
			}
			lstShow.setItems(FXCollections.observableList(
				Arrays.asList(server.readArray(new String[] { "GetUser", }))));
			showState = ShowUser;
		});
		btnWord.setOnMouseClicked(e -> {
			if (showState != ShowWord) {
				lstShow.getSelectionModel().select(0);
				show = "";
			}
			lstShow.setItems(FXCollections.observableList(
				Arrays.asList(server.readArray(new String[] { "GetWord", }))));
			showState = ShowWord;
		});
		
		btnShare.setOnMouseClicked(e -> {
			switch (showState) {
			case ShowUser:
				server.readArray(
					new String[] { "Share", me, show, word.getText() });
				primaryStage.close();
				break;
			case ShowWord:
				word.setText(show);
				primaryStage.close();
				break;
			}
		});
		
		rootPane.add(btnUser, 0, 0);
		rootPane.add(btnWord, 0, 1);
		rootPane.add(btnShare, 0, 2);
		rootPane.add(lstShow, 1, 0, 1, 3);
		
		Scene scene = new Scene(rootPane);
		primaryStage.setTitle("User");
		primaryStage.setScene(scene);
		primaryStage.setWidth(300);
		primaryStage.setHeight(150);
		primaryStage.show();
		primaryStage.setResizable(false);
		
	}
	
}
