package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

/**
 * Sample Skeleton for 'game.fxml' Controller Class
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;

public class GameController {

  @FXML // fx:id="board"
  private GridPane board; // Value injected by FXMLLoader

  @FXML // fx:id="statusLabel"
  private Label statusLabel; // Value injected by FXMLLoader

  @FXML // fx:id="turnLabel"
  private Label turnLabel; // Value injected by FXMLLoader

  private String mark;

  private String opponent;

  @FXML
  void place(ActionEvent event) {
    disableBoard(true);
    Button btn = (Button) event.getTarget();
    btn.setDisable(true);
    btn.setText(mark);
    SimpleClient client = SimpleClient.getClient();
    try {
      String message = GridPane.getRowIndex(btn) + " " + GridPane.getColumnIndex(btn);
      client.sendToServer(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
    setTurn("It's your opponent's turn");
  }

  public void setStatus(String status) {
    System.out.println(mark);
    statusLabel.setText(status);
  }

  public void setTurn(String turn) {
    turnLabel.setText(turn);
  }

  public void showBoard(boolean bool) {
    board.setVisible(bool);
  }

  public void disableBoard(boolean bool) {
    board.setDisable(bool);
  }

  public void setMark(String mark) {
    this.mark = mark;
  }

  public void setOpponent(String opponent) {
    this.opponent = opponent;
  }

  public void placeOpponent(String indexString) {
    String[] indexes = indexString.split(" ");
    int row = Integer.parseInt(indexes[0]);
    int column = Integer.parseInt(indexes[1]);
    for (Node node : board.getChildren()) {
      if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
        Button btn = (Button) node;
        btn.setText(opponent);
        btn.setDisable(true);
        break;
      }
    }
  }
}
