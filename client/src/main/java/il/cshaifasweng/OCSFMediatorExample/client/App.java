package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {

  private static Scene scene;
  private SimpleClient client;
  private static FXMLLoader fxmlLoader;

  @Override
  public void start(Stage stage) throws IOException {
    EventBus.getDefault().register(this);
    scene = new Scene(loadFXML("connection"), 800, 800);
    stage.setScene(scene);
    stage.show();
  }

  static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFXML(fxml));
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    App.fxmlLoader = fxmlLoader;
    return fxmlLoader.load();
  }

  @Override
  public void stop() throws Exception {
    // TODO Auto-generated method stub
    EventBus.getDefault().unregister(this);
    client.sendToServer("remove client");
    client.closeConnection();
    super.stop();
  }

  @Subscribe
  public void onWarningEvent(WarningEvent event) {
    Platform.runLater(() -> {
      Alert alert = new Alert(AlertType.WARNING,
          String.format("Message: %s\nTimestamp: %s\n",
              event.getWarning().getMessage(),
              event.getWarning().getTime().toString()));
      alert.show();
    });

  }

  public static void main(String[] args) {
    launch();
  }

  public static FXMLLoader getFxmlLoader() {
    return fxmlLoader;
  }
}
