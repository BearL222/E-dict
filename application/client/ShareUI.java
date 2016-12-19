
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

		lstShow.getSelectionModel().selectedItemProperty().addListener((o, s1, s2) -> {
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
				btnShare.setText("Send");
				lstShow.getSelectionModel().select(0);
				show = "";
			}
			lstShow.setItems(
					FXCollections.observableList(Arrays.asList(server.readArray(new String[] { "GetUser", }))));
			showState = ShowUser;
			btnShare.setDisable(false);
		});
		btnWord.setOnMouseClicked(e -> {
			if (showState != ShowWord) {
				btnShare.setText("Show");
				lstShow.getSelectionModel().select(0);
				show = "";
			}
			lstShow.setItems(
					FXCollections.observableList(Arrays.asList(server.readArray(new String[] { "GetWord", me.trim(), }))));
			showState = ShowWord;
			btnShare.setDisable(false);
		});

		btnShare.setOnMouseClicked(e -> {
			switch (showState) {
			case ShowUser:
				if ((me.compareTo("") != 0) && (show.compareTo("") != 0) && (word.getText().compareTo("") != 0)) {
					System.out.println(me.trim() + " " + show.trim() + " " + word.getText());
					server.printArray(new String[] { "Share", me, show, word.getText() });
					primaryStage.close();
				}
				break;
			case ShowWord:
				if (show.compareTo("") != 0) {
					word.setText(show);
					primaryStage.close();
				}
				break;
			}
		});

		rootPane.add(btnUser, 0, 0);
		rootPane.add(btnWord, 1, 0);
		rootPane.add(btnShare, 2, 0);
		rootPane.add(lstShow, 0, 1, 3, 1);

		Scene scene = new Scene(rootPane);
		primaryStage.setTitle("User");
		primaryStage.setScene(scene);
		primaryStage.setWidth(300);
		primaryStage.setHeight(800);
		primaryStage.show();
		primaryStage.setResizable(false);

	}

}
