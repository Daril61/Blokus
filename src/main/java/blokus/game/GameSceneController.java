package blokus.game;

import blokus.utils.ShapeType;
import blokus.utils.Vector2;
import blokus.utils.eventArgs.EventArgs;
import blokus.utils.eventArgs.ShapePlacedArgs;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

/**
 * Classe qui s'occupe de la gestion de la scène de jeu
 *
 * @author Romain Veydarier
 * @since 28/04/2023
 */
public class GameSceneController implements Initializable {

    private Vector2 position;
    Shape currentSelectedShape;

    @FXML
    private Pane root;
    @FXML
    private GridPane grid;
    @FXML
    private HBox shapesParent;

    @FXML
    private Label leftPlayerName;
    @FXML
    private Label leftNbPiece;

    @FXML
    private Label frontPlayerName;
    @FXML
    private Label frontNbPiece;

    @FXML
    private Label rightPlayerName;
    @FXML
    private Label rightNbPiece;

    private final int[][] boardGrid = new int[20][20];
    private final Map<Integer, Label> playersToNbShapeText = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameApplication.getInstance().OnShapePlacedEvent.addListener(this::OnShapePlaced);
        currentSelectedShape = new Shape(ShapeType.C3_2);

        Platform.runLater(this::InitializeLater);
    }

    private void InitializeLater() {
        // Récupération de la scène
        Scene scene = root.getScene();

        // Ajout de la détection d'appuyer de touche
        scene.setOnKeyPressed(this::OnKeyPressed);

        // On met à jour les différentes informations des joueurs
        UpdatePlayersName();
        leftNbPiece.setText("" + (ShapeType.values().length-1));
        frontNbPiece.setText("" + (ShapeType.values().length-1));
        rightNbPiece.setText("" + (ShapeType.values().length-1));

        for (int row = 0; row < grid.getRowCount(); row++) {
            for (int col = 0; col < grid.getColumnCount(); col++) {
                Rectangle cell = new Rectangle(30, 30, Color.WHITE);
                cell.setOnMouseEntered(e -> {
                    Node source = e.getPickResult().getIntersectedNode();
                    Integer colIndex = GridPane.getColumnIndex(source);
                    Integer rowIndex = GridPane.getRowIndex(source);

                    // Si on ne peut pax récupérer une ligne ou une colonne par rapport au clique
                    if(colIndex == null || rowIndex == null) return;
                    // Action à effectuer lorsque la souris entre dans la cellule

                    position = new Vector2(colIndex, rowIndex);
                    cell.setFill(Color.RED);
                });
                cell.setOnMouseExited(event -> {
                    // Action à effectuer lorsque la souris quitte la cellule
                    cell.setFill(Color.WHITE);
                });

                grid.add(cell, col, row);
            }
        }
    }

    private void UpdatePlayersName() {
        List<Label> tempTextList = new ArrayList<>(List.of(leftPlayerName, frontPlayerName, rightPlayerName));

        int index = GameApplication.getInstance().pId + 1;
        for (int i = 0; i < tempTextList.size(); i++) {
            if(index >= 3) index = 0;

            playersToNbShapeText.put(index, tempTextList.get(i));
            tempTextList.get(i).setText(GameApplication.getInstance().playerStructs.get(i).pName);
        }
    }

    private void OnShapePlaced(EventArgs args) {
        ShapePlacedArgs shapePlacedArgs = (ShapePlacedArgs)args;

        UpdatePlayerShapeNumber(shapePlacedArgs.pId);
        AddShapeToGrid(shapePlacedArgs.color);
    }
    private void UpdatePlayerShapeNumber(int pId) {
        // Si c'est l'identifiant du joueur alors on ne modifie rien
        if(pId == GameApplication.getInstance().pId) return;

        // On récupère l'ancien nombre de pièces que le joueur avait
        int nbPiece = Integer.parseInt(playersToNbShapeText.get(pId).getText());
        playersToNbShapeText.get(pId).setText("" + nbPiece);
    }

    private void OnKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.D) {
            currentSelectedShape = currentSelectedShape.RightRotate();
        }

        if(event.getCode() == KeyCode.P) {
            AddShapeToGrid(Color.RED);
        }
    }

    public void AddShapeToGrid(Color color) {
        for(Vector2 coord : currentSelectedShape.getCoords()) {
            int x = position.GetX() + coord.GetX();
            int y = position.GetY() + coord.GetY();
            boardGrid[x][y] = 1;

            Pixel pixel = new Pixel(currentSelectedShape.getType(), color);
            grid.add(pixel.getImg(), x, y);
        }
    }
}
