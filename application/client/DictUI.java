////////////////////////////////////////////////////////////////////////////////

package application.client;

import application.share.DictInfo;

import java.util.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.SocketStream;

public abstract class DictUI extends Application {

	protected static SocketStream server = null;

	final Label lblUser = new Label("");
	final TextField txtInput = new TextField("");
	final VBox boxCards = new VBox();
	final Card[] cardArray = new Card[DictInfo.info.length];
	final CheckBox[] chkDictArray = new CheckBox[DictInfo.info.length];

	protected abstract void btnSearch(String s);

	protected abstract void btnShare();

	protected abstract void btnUser();

	boolean txtInputGotFocused = false;

	protected void updateCards() {
		List<Card> cardShowList = new LinkedList<>();

		int cardLen = 0;
		for (int i = 0; i < cardArray.length; ++i) {
			if (chkDictArray[i].isSelected()) {
				cardShowList.add(cardArray[i]);
				++cardLen;
			}
		}

		if (cardLen == 0) {
			for (int i = 0; i < cardArray.length; ++i) {
				cardShowList.add(cardArray[i]);
			}
		}
		cardShowList.sort(null);

		boxCards.getChildren().clear();
		boxCards.getChildren().addAll(cardShowList);
	}

	public void initializeComponent(final Stage primaryStage) {

		txtInput.setMaxHeight(Double.MAX_VALUE);
		txtInput.setPromptText("Enter your word.");
		txtInput.focusedProperty().addListener((o, b1, b2) -> {
			txtInputGotFocused = true;
		});
		txtInput.setOnMousePressed(e -> {
			if (txtInputGotFocused) {
				txtInput.selectAll();
				txtInputGotFocused = false;
			}
		});

		Button btnSearch = new Button("", new ImageView("/image/Search.png"));
		btnSearch.setMinSize(30, 30);
		btnSearch.setPrefSize(30, 30);
		btnSearch.setOnMouseClicked(o -> btnSearch(txtInput.getText()));
		btnSearch.setDisable(txtInput.getText().compareTo("") == 0);
		Button btnShare = new Button("", new ImageView("/image/Share.png"));
		btnShare.setMinSize(30, 30);
		btnShare.setPrefSize(30, 30);
		btnShare.setOnMouseClicked(o -> btnShare());
		btnShare.setDisable((lblUser.getText().compareTo("") == 0));
//		btnShare.setDisable((txtInput.getText().compareTo("") == 0) || (lblUser.getText().compareTo("") == 0));
		Button btnUser = new Button("", new ImageView("/image/User.png"));
		btnUser.setMinSize(30, 30);
		btnUser.setPrefSize(30, 30);
		btnUser.setOnMouseClicked(o -> btnUser());

		txtInput.textProperty().addListener((o, s1, s2) -> {
			boolean isEng = true;
			for (int i = 0; i < s2.length(); ++i) {
				char c = s2.charAt(i);
				if ((('A' <= c) && (c <= 'Z')) || (('a' <= c) && (c <= 'z')))
					;
				else {
					isEng = false;
				}
			}
			if (!isEng) {
				txtInput.setText(s1);
			}
			btnSearch.setDisable(txtInput.getText().compareTo("") == 0);
			btnShare.setDisable((lblUser.getText().compareTo("") == 0));
		});
		lblUser.textProperty().addListener((o, s1, s2) -> {
			btnShare.setDisable((lblUser.getText().compareTo("") == 0));
			String str = txtInput.getText();
			if (str.compareTo("") != 0) {
				btnSearch(str);
			}
		});

		HBox boxInput = new HBox();
		boxInput.setSpacing(20);
		HBox.setHgrow(txtInput, Priority.ALWAYS);
		boxInput.getChildren().addAll(txtInput, btnSearch, btnShare, btnUser);
		boxInput.setMinHeight(30);

		for (int i = 0; i < chkDictArray.length; ++i) {
			chkDictArray[i] = new CheckBox(DictInfo.info[i].name);
			chkDictArray[i].setMinWidth(60);
			chkDictArray[i].selectedProperty().addListener((o, t1, t2) -> updateCards());
		}

		HBox boxDict = new HBox();
		boxDict.setSpacing(10);
		boxDict.getChildren().addAll(chkDictArray);

		for (int i = 0; i < cardArray.length; ++i) {
			cardArray[i] = new Card(server, i, DictInfo.info[i].name + "1\n" + DictInfo.info[i].name + "2\n", i,
					lblUser);
		}

		double boxCardsInset = 10;
		boxCards.setPadding(new Insets(boxCardsInset, boxCardsInset, boxCardsInset, boxCardsInset));
		boxCards.setSpacing(20);
		// updateCards();

		ScrollPane scrlCards = new ScrollPane();
		scrlCards.setFitToWidth(true);
		scrlCards.setContent(boxCards);

		// Button[] btnSignArray = new Button[3];
		// btnSignArray[0] = new Button("Sign in");
		// btnSignArray[1] = new Button("Sign up");
		// btnSignArray[2] = new Button("Sign out");
		// HBox boxSign = new HBox();
		// boxSign.setSpacing(15);
		// for (int i = 0; i < 3; ++i) {
		// btnSignArray[i].setPrefSize(150, 30);
		// btnSignArray[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		// boxSign.getChildren().add(btnSignArray[i]);
		// }
		//
		// CheckBox[] chkSelectArray = new CheckBox[txtMessageArray.length];
		// chkSelectArray[0] = new CheckBox("百度");
		// chkSelectArray[1] = new CheckBox("有道");
		// chkSelectArray[2] = new CheckBox("金山");
		// HBox boxSelect = new HBox();
		// boxSelect.setSpacing(20);
		// for (int i = 0; i < 3; ++i) {
		// chkSelectArray[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		// boxSelect.getChildren().add(chkSelectArray[i]);
		// }
		//
		// txtMessageArray[0] = new Label("百度begin\n\n\n\n\n\n\n\n\n\n百度end");
		// txtMessageArray[1] = new Label("有道begin\n\n\n\n\n\n\n\n\n\n有道end");
		// txtMessageArray[2] = new Label("金山begin\n\n\n\n\n\n\n\n\n\n金山end");
		// VBox boxMessage = new VBox();
		// boxMessage.setSpacing(20);
		// for (int i = 0; i < 3; ++i) {
		// txtMessageArray[i].setFont(new Font(15));
		// boxMessage.getChildren().add(txtMessageArray[i]);
		// }
		// ScrollPane barMessage = new ScrollPane();
		// barMessage.setContent(boxMessage);
		//
		// final double rootPadding = 20;
		// GridPane rootPane = new GridPane();
		// rootPane.setHgap(rootPadding);
		// rootPane.setVgap(rootPadding);
		// rootPane.setPadding(
		// new Insets(rootPadding, rootPadding, rootPadding, rootPadding));
		//
		// ColumnConstraints column = new ColumnConstraints(100, 100,
		// Double.MAX_VALUE);
		// column.setHgrow(Priority.ALWAYS);
		// rootPane.getColumnConstraints().addAll(column,
		// new ColumnConstraints(150));
		// RowConstraints row = new RowConstraints(100, 100, Double.MAX_VALUE);
		// row.setVgrow(Priority.ALWAYS);
		// rootPane.getRowConstraints().addAll(new RowConstraints(30),
		// new RowConstraints(30), new RowConstraints(30), row);
		//
		// rootPane.add(txtInput, 0, 0, 1, 1);
		// rootPane.add(btnSearch, 1, 0, 1, 1);
		// rootPane.add(boxSign, 0, 1, 2, 1);
		// rootPane.add(boxSelect, 0, 2, 2, 1);
		// rootPane.add(barMessage, 0, 3, 2, 1);

		VBox boxRoot = new VBox();
		double boxRootInset = 20;
		boxRoot.setPadding(new Insets(boxRootInset, boxRootInset, boxRootInset, boxRootInset));
		boxRoot.setSpacing(20);
		VBox.setVgrow(scrlCards, Priority.ALWAYS);
		boxRoot.getChildren().addAll(lblUser, boxInput, boxDict, scrlCards);

		primaryStage.setTitle("E-dict");
		primaryStage.setScene(new Scene(boxRoot, 800, 600));
		primaryStage.setMinWidth(400);
		primaryStage.setMinHeight(300);
		primaryStage.show();
		// primaryStage.setResizable(false);

		// BorderPane basicPane = new BorderPane();
		// BorderPane leftPane = new BorderPane();
		// GridPane rightPane = new GridPane();
		//
		// // 上面板（注册，登陆）
		// FlowPane headPane = new FlowPane();
		// // 注册按钮
		// Button signupButton = new Button("Sign up");
		// // 登陆按钮
		// Button signinButton = new Button("Sign in");
		// // 程序名
		// StackPane programName = new StackPane();
		// programName.getChildren().add(new Label(" E-dictionary"));
		//
		// headPane.getChildren().addAll(signupButton, signinButton,
		// programName);
		//
		// // 左面板
		// ScrollPane userPane = new ScrollPane(lv);
		// leftPane.setCenter(userPane);
		//
		// // 右面板
		// // 输入面板
		// GridPane searchPane = new GridPane();
		// TextField inputText = new TextField();
		// Button inputButton = new Button("Search");
		// searchPane.add(new Label("Input"), 0, 0);
		// searchPane.add(inputText, 1, 0);
		// searchPane.add(inputButton, 2, 0);
		//
		// // 复选面板
		// GridPane selectPane = new GridPane();
		// CheckBox baiduCheck = new CheckBox("Baidu");
		// CheckBox youdaoCheck = new CheckBox("Youdao");
		// CheckBox bingCheck = new CheckBox("Bing");
		// selectPane.add(baiduCheck, 0, 0);
		// selectPane.add(youdaoCheck, 1, 0);
		// selectPane.add(bingCheck, 2, 0);
		//
		// // 结果面板
		// GridPane displayPane = new GridPane();
		// TextArea baiduArea = new TextArea();
		// TextArea youdaoArea = new TextArea();
		// TextArea bingArea = new TextArea();
		// Button baidulikeButton = new Button("like");
		// Button youdaolikeButton = new Button("like");
		// Button binglikeButton = new Button("like");
		//
		// displayPane.add(new Label("Baidu"), 0, 0);
		// displayPane.add(new Label("Youdao"), 0, 1);
		// displayPane.add(new Label("Bing"), 0, 2);
		// displayPane.add(baiduArea, 1, 0);
		// displayPane.add(youdaoArea, 1, 1);
		// displayPane.add(bingArea, 1, 2);
		// displayPane.add(baidulikeButton, 2, 0);
		// displayPane.add(youdaolikeButton, 2, 1);
		// displayPane.add(binglikeButton, 2, 2);
		//
		// rightPane.add(searchPane, 0, 0);
		// rightPane.add(selectPane, 0, 1);
		// rightPane.add(displayPane, 0, 2);
		//
		// // 界面整体设置
		// basicPane.setTop(headPane);
		// basicPane.setLeft(leftPane);
		// basicPane.setRight(rightPane);
		// Scene scene = new Scene(basicPane);
		// primaryStage.setTitle("E-dictionary");
		// primaryStage.setScene(scene);
		// primaryStage.show();
	}

	void setCardsMsg(String[] msgRecv) {
		for (int i = 0; i < cardArray.length; ++i) {
			cardArray[i].setCard(msgRecv[i * 3], Integer.parseInt(msgRecv[i * 3 + 1]),
					Boolean.parseBoolean(msgRecv[i * 3 + 2]));
		}
	}

	private static String turnEnglish(String word) {
		boolean ifEnglish = true;
		for (int i = 0; i < word.length(); i++) {
			if (!(word.charAt(i) >= 'a' && word.charAt(i) <= 'z')
					&& !(word.charAt(i) >= 'A' && word.charAt(i) <= 'Z')) {
				ifEnglish = false;
			}
		}
		return ifEnglish == true ? word : "-1";
	}

}
