
package application.client;

import javafx.stage.Stage;

public class DictEvent extends DictUI {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initializeComponent(primaryStage);
	}
}
