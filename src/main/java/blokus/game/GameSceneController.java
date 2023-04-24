package blokus.game;

import blokus.utils.ShapeType;
import blokus.utils.Vector2;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSceneController implements Initializable {

    private Vector2 position;
    Shape currentSelectedShape;

    @FXML
    private Pane root;

    private final int[][] boardGrid = new int[20][20];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Récupération de la scène
        Scene scene = root.getScene();

        // Ajout de la détection d'appuyer de touche
        scene.setOnKeyPressed(this::OnKeyPressed);

        currentSelectedShape = new Shape(ShapeType.C3_2);
    }

    private void OnKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.D) {
            currentSelectedShape = currentSelectedShape.RightRotate();
        }
    }

    public void AddShapeToGrid() {
        for(Vector2 coord : currentSelectedShape.GetCoords()) {
            boardGrid[position.GetX() + coord.GetX()][position.GetY() + coord.GetY()] = 1;
        }
    }
}
