package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;

public class SimpleClient extends AbstractClient {

  private static SimpleClient client = null;
  private boolean ingame = false;

  private SimpleClient(String host, int port) {
    super(host, port);
  }

  @Override
  protected void handleMessageFromServer(Object msg) {
    String message = (String) msg;
    if (message.equals("full")) {
      ConnectionController controller = App.getFxmlLoader().getController();
      Platform.runLater(() -> {
        controller.printError("Error: Game is full");
      });
    } else {
      if (!ingame) {
        try {
          App.setRoot("game");
        } catch (IOException e) {
          e.printStackTrace();
        }
        ingame = true;
      }
      GameController gameController = App.getFxmlLoader().getController();
      if (message.equals("waiting")) {
        Platform.runLater(() -> {
          gameController.setStatus("Waiting for second player");
        });
      } else if (message.equals("x")) {
        Platform.runLater(() -> {
          gameController.setMark("x");
          gameController.setStatus("You're X");
          gameController.setTurn("It's your turn");
          gameController.showBoard(true);
          gameController.setOpponent("o");
        });
      } else if (message.equals("o")) {
        Platform.runLater(() -> {
          gameController.setMark("o");
          gameController.setStatus("You're O");
          gameController.setTurn("It's your opponent's turn");
          gameController.disableBoard(true);
          gameController.showBoard(true);
          gameController.setOpponent("x");
        });
      } else if (message.equals("win")) {
        Platform.runLater(() -> {
          gameController.setStatus("YOU WIN");
          gameController.setTurn("");
        });
      } else if (message.equals("lose")) {
        Platform.runLater(() -> {
          gameController.setStatus("YOU LOSE");
          gameController.setTurn("");
          gameController.disableBoard(true);
        });
      } else if (message.equals("draw")) {
        Platform.runLater(() -> {
          gameController.setStatus("IT'S A DRAW");
          gameController.setTurn("");
          gameController.disableBoard(true);
        });
      } else {
        Platform.runLater(() -> {
          gameController.placeOpponent(message);
          gameController.setTurn("It's your turn");
          gameController.disableBoard(false);
        });
      }
    }
  }

  public static SimpleClient getClient() {
    return client;
  }

  public static SimpleClient createClient(String host, int port) {
    client = new SimpleClient(host, port);
    return client;
  }

}
