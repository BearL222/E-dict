
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
	
	public ShareUI(SocketStream server) {
		this.server = server;
		start();
	}
	
	public void start() {
		// 布局
		GridPane rootPane = new GridPane();
		
		ListView<String> lstUser = new ListView<>();
		lstUser.setItems(FXCollections.observableList(
			Arrays.asList(server.readArray(new String[] { "GetUser", }))));
		
		rootPane.add(lstUser, 0, 0);
		
		Scene scene = new Scene(rootPane);
		primaryStage.setTitle("User");
		primaryStage.setScene(scene);
		primaryStage.setWidth(300);
		primaryStage.setHeight(150);
		primaryStage.show();
		primaryStage.setResizable(false);
		
	}
	
}
