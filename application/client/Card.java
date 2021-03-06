
package application.client;

import application.share.*;
import net.SocketStream;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

class Card extends GridPane implements Comparable<Card> {
	private static final String likeR = new String("/image/LikeR.png");
	private static final String likeB = new String("/image/LikeB.png");

	private final Label lblUser;

	private final Integer dictIndex;
	private final Label lblDict;
	private final TextArea txtMsg;
	private final Button btnLike;
	private Integer numLike;

	public Card(SocketStream server, int index, String msg, int like, Label lblUser) {
		super();

		this.lblUser = lblUser;

		dictIndex = index;
		lblDict = new Label(DictInfo.info[dictIndex].name);

		txtMsg = new TextArea(msg);
		txtMsg.setPrefRowCount((txtMsg.getText() + " ").split("\n").length);
		txtMsg.textProperty()
				.addListener((o, s1, s2) -> txtMsg.setPrefRowCount((txtMsg.getText() + " ").split("\n").length));
		txtMsg.setFont(new Font(15));

		btnLike = new Button(new Integer(numLike = like).toString());
		btnLike.setFont(new Font(10));
		btnLike.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btnLike.setOnMouseClicked(e -> {
			String[] msgRecv = server.readArray(new String[] { "Like", dictIndex.toString(), });
			btnLike.setText(msgRecv[0]);
			btnLike.setGraphic(Boolean.parseBoolean(msgRecv[1]) ? new ImageView(likeR) : new ImageView(likeB));
		});
		btnLike.setDisable(lblUser.getText().compareTo("") == 0);

		super.setVgap(5);

		super.getColumnConstraints().addAll(col, new ColumnConstraints(100));
		super.getRowConstraints().addAll(new RowConstraints(30), row);

		super.add(lblDict, 0, 0, 1, 1);
		super.add(btnLike, 1, 0, 1, 1);
		super.add(txtMsg, 0, 1, 2, 1);

	}

	public void setCard(String msg, int like, boolean color) {
		// if (Debug.DEBUG) System.out.println(msg);
		txtMsg.setText(msg);
		btnLike.setText(new Integer(numLike = like).toString());
		btnLike.setGraphic(color ? new ImageView(likeR) : new ImageView(likeB));
		btnLike.setDisable(lblUser.getText().compareTo("") == 0);
	}

	private static ColumnConstraints col = new ColumnConstraints(30, 30, Double.MAX_VALUE);
	private static RowConstraints row = new RowConstraints();
	static {
		col.setHgrow(Priority.ALWAYS);
	}

	@Override
	public int compareTo(Card rhs) {
		int result = -numLike.compareTo(rhs.numLike);
		return result == 0 ? dictIndex.compareTo(rhs.dictIndex) : result;
	}

}
