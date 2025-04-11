package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class ConnectionController {
  @FXML // fx:id="connectBtn"
  private Button connectBtn; // Value injected by FXMLLoader

  @FXML // fx:id="ipTF"
  private TextField ipTF; // Value injected by FXMLLoader

  @FXML // fx:id="portTF"
  private TextField portTF; // Value injected by FXMLLoader

  @FXML
  private Label errorLabel;

  @FXML
  void connect(ActionEvent event) throws IOException {
    String host = "localhost";
    int port = 3000;
    if (!ipTF.getText().isEmpty()) {
      host = ipTF.getText();
    }
    if (!portTF.getText().isEmpty()) {
      port = Integer.parseInt(portTF.getText());
    }
    SimpleClient client = SimpleClient.createClient(host, port);
    client.openConnection();
    client.sendToServer("add client");
  }

  public void printError(String error) {
    errorLabel.setText(error);
  }
}
