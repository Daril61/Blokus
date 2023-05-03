package blokus.game;

import blokus.utils.GameColor;
import blokus.utils.NetworkIdentity;
import blokus.utils.ShapeType;
import blokus.utils.Vector2;
import blokus.utils.eventArgs.EventArgs;
import blokus.utils.eventArgs.ShapePlacedArgs;
import blokus.utils.message.PlaceShapeMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
    private FlowPane shapesParent;

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

    private GameColor pColor;

    private final int[][] boardGrid = new int[20][20];
    private final Map<Integer, Label> playersIdToNbShapeText = new HashMap<>();
    private final Map<ShapeType, Group> shapesTypeToGroup = new HashMap<>();

    private boolean firstPlace = true;
    private List<Pixel> pixelPreviewed = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameApplication.getInstance().OnShapePlacedEvent.addListener(this::OnShapePlaced);
        GameApplication.getInstance().WhenMyTurn.addListener(this::WhenIsMyTurn);
        currentSelectedShape = new Shape(ShapeType.C3_2);

        pColor = GameColor.values()[GameApplication.getInstance().pId];

        Platform.runLater(() -> {
            InitializeLater();
            InitializeGrid();
            InitializePlayerShape();
        });
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
    }
    private void InitializeGrid() {
        for (int y = 0; y < boardGrid.length; y++) {
            for (int x = 0; x < boardGrid[y].length; x++) {
                System.out.println(boardGrid[x][y]);
            }
        }

        for (int y = 0; y < boardGrid.length; y++) {
            for (int x = 0; x < boardGrid[y].length; x++) {
                boardGrid[x][y] = -1;
            }
        }

        for (int y = 0; y < boardGrid.length; y++) {
            for (int x = 0; x < boardGrid[y].length; x++) {
                System.out.println(boardGrid[x][y]);
            }
        }

        for (int row = 0; row < grid.getRowCount(); row++) {
            for (int col = 0; col < grid.getColumnCount(); col++) {
                Rectangle cell = new Rectangle(30, 30, Color.WHITE);
                cell.setStroke(Color.BLACK);
                cell.setStrokeWidth(2.0);

                cell.setOnMouseEntered(e -> {
                    Node source = e.getPickResult().getIntersectedNode();
                    Integer colIndex = GridPane.getColumnIndex(source);
                    Integer rowIndex = GridPane.getRowIndex(source);

                    // Si on ne peut pax récupérer une ligne ou une colonne par rapport au clique
                    if(colIndex == null || rowIndex == null) return;
                    // Action à effectuer lorsque la souris entre dans la cellule

                    position = new Vector2(colIndex, rowIndex);
                    if(currentSelectedShape.getType() != ShapeType.NONE) {
                        if (pixelPreviewed.size() > 0)
                            ClearPreviewedList();

                        if (CheckIfOutOfBounds()) {
                            pixelPreviewed = AddShapeToGrid(currentSelectedShape, position, pColor, true);
                            for (Pixel p : pixelPreviewed) {
                                p.getImg().setMouseTransparent(true);
                            }
                        }
                    }
                });

                cell.setOnMouseExited(event -> {
                    ClearPreviewedList();
                });

                grid.add(cell, col, row);
            }
        }

        // On applique les couleurs au niveau des angles pour le point de départ
        for(GameColor c : GameColor.values()) {
            Rectangle rect = (Rectangle) grid.getChildren().stream()
                    .filter(node -> GridPane.getRowIndex(node) == c.getGridStartPos().GetY() && GridPane.getColumnIndex(node) == c.getGridStartPos().GetX())
                    .findFirst()
                    .orElse(null);
            if(rect == null) continue;

            rect.setFill(c.getColor().deriveColor(0, 1, 1, 0.5));
        }
    }
    private void InitializePlayerShape() {
        // Pour chaque pièce différente dans le jeu
        for(ShapeType type : ShapeType.values()) {
            if(type == ShapeType.NONE) continue;

            Group group = new Group();

            for(int[] coord : type.getCoordsPixel()) {
                Pixel pixel = new Pixel(type, pColor, new Vector2(15, 15));
                pixel.getImg().setLayoutX(coord[0] * 15);
                pixel.getImg().setLayoutY(coord[1] * 15);

                pixel.getImg().setOnMousePressed((e) -> OnShapePressed(type));
                group.getChildren().add(pixel.getImg());
            }

            shapesTypeToGroup.put(type, group);

            shapesParent.getChildren().add(group);
        }
    }

    /**
     * Fonction appelée quand il y a une nouvelle pièce de posé
     * @param args Argument de l'évènement
     */
    private void OnShapePlaced(EventArgs args) {
        ShapePlacedArgs shapePlacedArgs = (ShapePlacedArgs)args;

        UpdatePlayerShapeNumber(shapePlacedArgs.pId);
        GameColor color = GameColor.values()[shapePlacedArgs.pId];
        AddShapeToGrid(shapePlacedArgs.shape, shapePlacedArgs.position, color, false);
    }
    /**
     * Fonction pour mettre à jour le nombre de pièce de chaque joueurs
     * @param pId Identifiant du joueur
     */
    private void UpdatePlayerShapeNumber(int pId) {
        // Si c'est l'identifiant du joueur alors on ne modifie rien
        if(pId == GameApplication.getInstance().pId) return;

        // On récupère l'ancien nombre de pièces que le joueur avait
        int nbPiece = Integer.parseInt(playersIdToNbShapeText.get(pId).getText());
        playersIdToNbShapeText.get(pId).setText("" + nbPiece);
    }
    /**
     * Fonction pour mettre à jour les noms des joueurs
     */
    private void UpdatePlayersName() {
        List<Label> tempPlayersNameTextList = new ArrayList<>(List.of(leftPlayerName, frontPlayerName, rightPlayerName));
        List<Label> tempNbPieceTextList = new ArrayList<>(List.of(leftNbPiece, frontNbPiece, rightNbPiece));

        int index = GameApplication.getInstance().pId + 1;
        for (int i = 0; i < tempPlayersNameTextList.size(); i++) {
            if(index > 3) index = 0;

            playersIdToNbShapeText.put(index, tempNbPieceTextList.get(i));
            tempPlayersNameTextList.get(i).setText(GameApplication.getInstance().playerStructs.get(i).pName);
            index++;
        }
    }

    private void OnKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.D) {
            currentSelectedShape = currentSelectedShape.RightRotate();
            if (pixelPreviewed.size() > 0)
                ClearPreviewedList();

            if (CheckIfOutOfBounds()) {
                pixelPreviewed = AddShapeToGrid(currentSelectedShape, position, pColor, true);
                for (Pixel p : pixelPreviewed) {
                    p.getImg().setMouseTransparent(true);
                }
            }
        }

        if(event.getCode() == KeyCode.P) {
            PlaceShape();
        }
    }
    private void OnShapePressed(ShapeType type) {
        // Récupération des anciennes images qui appartiennent au type
        if(currentSelectedShape.getType() != ShapeType.NONE) {
            Group group = shapesTypeToGroup.get(currentSelectedShape.getType());

            for(Node n : group.getChildren()) {
                ImageView img = (ImageView)n;
                img.setEffect(null);
            }
        }

        // On récupère les différentes images qui appartiennent au type
        Group group = shapesTypeToGroup.get(type);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK); // Couleur du contour
        dropShadow.setWidth(5); // Largeur du contour
        dropShadow.setHeight(5); // Hauteur du contour
        for(Node n : group.getChildren()) {
            ImageView img = (ImageView)n;
            img.setEffect(dropShadow);
        }

        currentSelectedShape = new Shape(type);
    }
    private void WhenIsMyTurn(EventArgs args) {
        // TODO ajouter un texte qui indique que c'est à notre tour avec une animation |-MY TURN-------| |-------MY TURN--|
    }

    /**
     * Fonction qui permet de savoir si on peut poser la pièce
     * @return Vrai (true) si on peut poser la pièce, Faux (false) si on ne peut pas
     */
    private boolean CheckIfCanPlace() {
        boolean canBePlaced = false;
        int i = 0;
        // On regarde s'il n'y a pas un bloc sur les cases de notre futur forme
        for(Vector2 coord : currentSelectedShape.getCoords()) {
            i++;
            int x = position.GetX() + coord.GetX();
            int y = position.GetY() + coord.GetY();

            // Si les coordonnés sont en dehors de la grille
            if(x < 0 || x >= boardGrid.length) return false;
            if(y < 0 || y >= boardGrid[0].length) return false;

            // S'il y a un élément d'une autre couleur au niveau de la pièce
            for (int yy = 0; yy < boardGrid.length; yy++) {
                for (int xx = 0; xx < boardGrid[yy].length; xx++) {
                    System.out.println(boardGrid[xx][yy]);
                }
            }
            if(boardGrid[x][y] != -1) return false;

            // Si c'est le premier placement alors on regarde si le pixel est dans l'angle
            if(firstPlace) {
                if(x == pColor.getGridStartPos().GetX() && y == pColor.getGridStartPos().GetY())
                    canBePlaced = true;
                continue;
            }
            // S'il y a un pixel alors on retourne false
            if(boardGrid[x][y] > 1) return false;
            // Vérification que la pièce n'est pas collée à un pixel de la même couleur
            if(x - 1 >= 0)
                if(boardGrid[x - 1][y] == pColor.ordinal()) return false;
            if(x + 1 < boardGrid.length)
                if(boardGrid[x + 1][y] == pColor.ordinal()) return false;
            if(y - 1 >= 0)
                if(boardGrid[x][y - 1] == pColor.ordinal()) return false;
            if(y + 1 < boardGrid[0].length)
                if(boardGrid[x][y + 1] == pColor.ordinal()) return false;
        }
        // Si c'est le premier placement alors pas besoin de vérifier la suite
        if(firstPlace)
            return canBePlaced;
        // Vérification que l'on a bien un angle de la même couleur du joueur
        List<Vector2> corners = GetCornerOfShape();
        for(Vector2 v : corners) {
            if(boardGrid[v.GetX()][v.GetY()] == pColor.ordinal()) {
                canBePlaced = true;
                break;
            }
        }

        return canBePlaced;
    }
    private boolean CheckIfOutOfBounds() {
        for(Vector2 coord : currentSelectedShape.getCoords()) {
            int x = position.GetX() + coord.GetX();
            int y = position.GetY() + coord.GetY();

            // Si les coordonnés sont en dehors de la grille
            if (x < 0 || x >= boardGrid.length) return false;
            if (y < 0 || y >= boardGrid[0].length) return false;
        }

        return true;
    }
    private List<Vector2> GetCornerOfShape() {
        List<Vector2> tempPos = new ArrayList<>();
        List<Vector2> directUP_DOWN_LEFT_RIGHT = new ArrayList<>();
        for(Vector2 coord : currentSelectedShape.getCoords()) {
            int x = position.GetX() + coord.GetX();
            int y = position.GetY() + coord.GetY();

            directUP_DOWN_LEFT_RIGHT.add(new Vector2(x - 1, y));
            directUP_DOWN_LEFT_RIGHT.add(new Vector2(x + 1, y));
            directUP_DOWN_LEFT_RIGHT.add(new Vector2(x, y - 1));
            directUP_DOWN_LEFT_RIGHT.add(new Vector2(x, y + 1));
            directUP_DOWN_LEFT_RIGHT.add(new Vector2(x, y));

            tempPos.add(new Vector2(x - 1, y + 1));
            tempPos.add(new Vector2(x - 1, y - 1));
            tempPos.add(new Vector2(x + 1, y + 1));
            tempPos.add(new Vector2(x + 1, y - 1));
        }

        List<Vector2> allValues = new ArrayList<>();
        allValues.addAll(directUP_DOWN_LEFT_RIGHT);
        allValues.addAll(tempPos);

        Map<Vector2, Integer> countMap = new HashMap<>();

        // Compter les occurrences de chaque élément
        for (Vector2 value : allValues) {
            countMap.put(value, countMap.getOrDefault(value, 0) + 1);
        }

        List<Vector2> uniqueValuesList = new ArrayList<>();

        // Parcourir la liste et retirer les paires complètes
        for (Vector2 value : allValues) {
            if (countMap.get(value) % 2 == 1) {
                uniqueValuesList.add(value);
            }
        }
        uniqueValuesList.removeAll(directUP_DOWN_LEFT_RIGHT);

        // On retire tous les éléments qui sont hors de la grille
        List<Vector2> posOutOfBounds = new ArrayList<>();
        for(Vector2 v : uniqueValuesList) {
            if(v.GetX() < 0 || v.GetX() >= boardGrid.length) {
                posOutOfBounds.add(v);
                continue;
            }
            if(v.GetY() < 0 || v.GetY() >= boardGrid[0].length) {
                posOutOfBounds.add(v);
            }
        }
        uniqueValuesList.removeAll(posOutOfBounds);

        return uniqueValuesList;
    }

    public void PlaceShape() {
        // Si le joueur n'a rien sélectionner alors on return
        if(currentSelectedShape.getType() == ShapeType.NONE) return;
        boolean c = CheckIfCanPlace();
        System.out.println("C : " + c);
        if(!c) return;

        if(firstPlace) {
            firstPlace = false;
        }

        ShapePlacedArgs args = new ShapePlacedArgs(position, currentSelectedShape, GameApplication.getInstance().pId);
        GameApplication.getInstance().SendMessage(new PlaceShapeMessage(args));

        // Si c'est le serveur alors on ajoute la forme à la grille
        if(GameApplication.getInstance().getIdentity() == NetworkIdentity.SERVER) {
            AddShapeToGrid(args.shape, args.position, pColor, false);
        }

        // Récupération et suppression de la pièce que le joueur a posé de la liste
        Group group = shapesTypeToGroup.get(currentSelectedShape.getType());
        shapesParent.getChildren().remove(group);

        currentSelectedShape = new Shape(ShapeType.NONE);
    }
    private List<Pixel> AddShapeToGrid(Shape shape, Vector2 pos, GameColor color, boolean preview) {
        List<Pixel> pixels = new ArrayList<>();

        for(Vector2 coord : shape.getCoords()) {
            int x = pos.GetX() + coord.GetX();
            int y = pos.GetY() + coord.GetY();
            if(!preview)
                boardGrid[x][y] = color.ordinal();

            Pixel pixel = new Pixel(shape.getType(), color, new Vector2(30, 30));
            pixels.add(pixel);
            grid.add(pixel.getImg(), x, y);
        }

        return pixels;
    }

    private void ClearPreviewedList() {
        for(Pixel p : pixelPreviewed) {
            grid.getChildren().remove(p.getImg());
        }
    }
}
