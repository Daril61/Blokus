package blokus.game;

import blokus.utils.Event;
import blokus.utils.eventArgs.EventArgs;
import blokus.utils.eventArgs.EventArgsType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {

    @FXML
    private Group groupJ1;
    @FXML
    private Text networkInfoJ1;

    @FXML
    private Group groupJ2;
    @FXML
    private Text networkInfoJ2;

    @FXML
    private Group groupJ3;
    @FXML
    private Text networkInfoJ3;

    @FXML
    private Group groupJ4;
    @FXML
    private Text networkInfoJ4;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameApplication.getInstance().onClientConnectEvent.addListener(this::OnClientConnect);

        System.out.println("Initialisation du controller du lobby");
    }

    private void OnClientConnect(EventArgs args) {

    }
}
