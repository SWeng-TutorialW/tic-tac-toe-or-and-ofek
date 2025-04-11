package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.ArrayList;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;
import java.util.Random;

public class SimpleServer extends AbstractServer {
  private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
  private int current;
  private String[] marks = new String[2];
  private String[][] board = new String[3][3];
  private int count = 0;

  public SimpleServer(int port) {
    super(port);
  }

  @Override
  protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
    String msgString = msg.toString();
    String reply = "";
    Random random = new Random();
    if (msgString.startsWith("add client")) {
      if (SubscribersList.size() == 2) {
        System.out.println("connection refused: full");
        reply = "full";
      } else {
        System.err.println("new connection");
        SubscribedClient connection = new SubscribedClient(client);
        SubscribersList.add(connection);
        if (SubscribersList.size() == 1) {
          reply = "waiting";
        } else {
          current = random.nextInt(2);
          try {
            marks[current] = "x";
            SubscribersList.get(current).getClient().sendToClient("x");
            marks[(current + 1) % 2] = "o";
            SubscribersList.get((current + 1) % 2).getClient().sendToClient("o");
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          return;
        }
      }
      try {
        client.sendToClient(reply);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else if (msgString.startsWith("remove client")) {
      if (!SubscribersList.isEmpty()) {
        for (SubscribedClient subscribedClient : SubscribersList) {
          if (subscribedClient.getClient().equals(client)) {
            SubscribersList.remove(subscribedClient);
            break;
          }
        }
      }
    } else if (client.equals(SubscribersList.get(current).getClient())) {
      String[] indexStrings = msgString.split(" ");
      int row = Integer.parseInt(indexStrings[0]);
      int column = Integer.parseInt(indexStrings[1]);
      if (board[row][column] == null) {
        ++count;
        board[row][column] = marks[current];
        boolean win = checkWin();
        if (win) {
          try {
            SubscribersList.get(current).getClient().sendToClient("win");
            SubscribersList.get((current + 1) % 2).getClient().sendToClient("lose");
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        } else {
          current = (current + 1) % 2;
          try {
            SubscribersList.get(current).getClient().sendToClient(msgString);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          if (count == 9) {
            sendToAllClients("draw");
          }
        }
      }
    }
  }

  public void sendToAllClients(String message) {
    try {
      for (SubscribedClient subscribedClient : SubscribersList) {
        subscribedClient.getClient().sendToClient(message);
      }
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  private boolean checkWin() {
    for (int i = 0; i < 3; ++i) {
      if (board[i][0] != null && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
        return true;
      }
    }
    for (int i = 0; i < 3; ++i) {
      if (board[0][i] != null && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
        return true;
      }
    }
    if (board[0][0] != null && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
      return true;
    }
    if (board[0][2] != null && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
      return true;
    }
    return false;
  }

}
