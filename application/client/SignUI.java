
package application.client;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import net.SocketStream;

public class SignUI {
	
	private final Stage primaryStage = new Stage();
	private final SocketStream server;
	private final Label lblUser;
	
	public SignUI(SocketStream server, Label lblUser) {
		this.server = server;
		this.lblUser = lblUser;
	}
	
	public void start() {
		// 布局
		GridPane basePane = new GridPane();
		BorderPane namePane = new BorderPane();
		BorderPane codePane = new BorderPane();
		
		Label error = new Label("");
		error.setPrefHeight(30);
		// 顶部图片
		// Image banner = new Image("file:image/book.jpg");
		// ImageView imageView = new ImageView(banner);
		
		// 用户名和密码
		TextField name = new TextField("");
		namePane.setLeft(new Label("用户名  "));
		namePane.setCenter(name);
		
		PasswordField passcode = new PasswordField();
		codePane.setLeft(new Label("密码     "));
		codePane.setCenter(passcode);
		
		// 登陆和注册按钮
		Button signIn =
			new Button("Sign in"/* , new ImageView("image/signin.png") */);
		signIn.setOnMouseClicked(e -> {
			if (Integer.parseInt(server.readArray(new String[] { "SignIn",
				name.getText(), passcode.getText(), })[0]) == 0) {
				lblUser.setText(name.getText());
				primaryStage.close();
			} else {
				error.setText("Sign in fail");
			}
		});
		Button signUp =
			new Button("Sign up"/* , new ImageView("image/signup.png") */);
		signUp.setOnMouseClicked(e -> {
			if (Integer.parseInt(server.readArray(new String[] { "SignUp",
				name.getText(), passcode.getText(), })[0]) == 0) {
				lblUser.setText(name.getText());
				primaryStage.close();
			} else {
				error.setText("Sign up fail");
			}
		});
		
		Button signOut =
			new Button("Sign out"/* , new ImageView("image/signup.png") */);
		signOut.setOnMouseClicked(e -> {
			if (Integer.parseInt(server.readArray(
				new String[] { "SignOut", name.getText(), })[0]) == 0) {
				lblUser.setText("");
				primaryStage.close();
			} else {
				error.setText("Sign out fail");
			}
		});
		
		// basePane.add(imageView, 0, 0);
		basePane.add(error, 0, 0);
		basePane.add(namePane, 0, 1);
		basePane.add(codePane, 0, 2);
		basePane.add(signIn, 0, 3);
		basePane.add(signUp, 0, 4);
		basePane.add(signOut, 0, 5);
		
		Scene scene = new Scene(basePane);
		primaryStage.setTitle("登录/注册");
		primaryStage.setScene(scene);
		primaryStage.setWidth(300);
		primaryStage.setHeight(185);
		primaryStage.show();
		primaryStage.setResizable(false);
		
	}
}
