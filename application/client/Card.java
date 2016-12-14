
package application.client;

import application.share.*;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

class Card extends GridPane implements Comparable<Card> {
	private final Label lblDict;
	private final Label lblMsg;
	private final Button btnLike;
	private Integer numLike;
	
	public Card(int dictIndex, String msg, int like) {
		super();
		
		lblDict = new Label(DictInfo.info[dictIndex].name);
		lblMsg = new Label(msg);
		lblMsg.setFont(new Font(15));
		btnLike = new Button(new Integer(numLike = like).toString());
		
		getColumnConstraints().addAll(col,new ColumnConstraints(30));
		getRowConstraints().addAll(new RowConstraints(30),row);
		
		add(lblDict, 0, 0, 1, 1);
		add(btnLike, 1, 0, 1, 1);
		add(lblMsg, 0, 1, 2, 1);
		
		setPrefWidth(Double.MAX_VALUE);
	}
	
	private static ColumnConstraints col =
		new ColumnConstraints(30, 30, Double.MAX_VALUE);
	private static RowConstraints row =
		new RowConstraints(30, 30, Double.MAX_VALUE);
	static {
		col.setHgrow(Priority.ALWAYS);
		row.setVgrow(Priority.ALWAYS);
	}
	
	@Override
	public int compareTo(Card rhs) {
		return numLike.compareTo(rhs.numLike);
	}
	
}