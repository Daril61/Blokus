package blokus.utils;

import blokus.game.GameApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameUtils {

    /**
     * Fonction pour changer de scène
     *
     * @param stage Gestionnaire de l'application JavaFX
     * @param fxml Type de la nouvelle fenêtre
     * @throws IOException Envoyé en cas d'impossibilité de récupérer le fxml mis en paramètre
     *
     * @since 15/03/2023
     */
    public static void ChangeScene(Stage stage, FxmlType fxml) throws IOException {
        System.out.println("ChangeScene");
        System.out.println(fxml.getFxmlName());
        System.out.println(fxml.getWindowName());

        // Récupération du fxml
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(
                        GameApplication.class.getResource(fxml.getFxmlName())
                )
        );

        // Création de la nouvelle scène
        Scene scene = new Scene(root);
        scene.setFill(Color.rgb(105, 102, 101));

        stage.setScene(scene);

        stage.setTitle(fxml.getWindowName());
        stage.setMinWidth(scene.getWidth());
        stage.setMinHeight(scene.getHeight());

        stage.show();
    }
}
