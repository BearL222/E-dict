package application.client;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SignUI extends Application {

	@Override
	public void start(Stage primaryStage) {
		// 布局
		GridPane basePane = new GridPane();
		BorderPane namePane = new BorderPane();
		BorderPane codePane = new BorderPane();

		// 顶部图片
		Image banner = new Image("file:image/book.jpg");
		ImageView imageView = new ImageView(banner);

		// 用户名和密码
		TextField name = new TextField("");
		namePane.setLeft(new Label("用户名  "));
		namePane.setCenter(name);
		TextField passcode = new TextField("");
		codePane.setLeft(new Label("密码     "));
		codePane.setCenter(passcode);

		// 登陆和注册按钮
		Button signIn = new Button("",new ImageView("image/signin.png"));
		Button signUp = new Button("", new ImageView("image/signup.png"));

		basePane.add(imageView, 0, 0);
		basePane.add(namePane, 0, 1);
		basePane.add(codePane, 0, 2);
		basePane.add(signIn, 0, 3);
		basePane.add(signUp, 0, 4);

		Scene scene = new Scene(basePane);
		primaryStage.setTitle("登录/注册");
		primaryStage.setScene(scene);
		primaryStage.setMinWidth(500);
		primaryStage.setMinHeight(350);
		primaryStage.show();

	}
}
