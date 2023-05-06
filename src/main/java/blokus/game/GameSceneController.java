package blokus.game;

import blokus.utils.*;
import blokus.utils.eventArgs.EventArgs;
import blokus.utils.eventArgs.GameRankArgs;
import blokus.utils.eventArgs.ShapePlacedArgs;
import blokus.utils.message.AbandonedMessage;
import blokus.utils.message.GameRankMessage;
import blokus.utils.message.PlaceShapeMessage;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Classe qui s'occupe de la gestion de la scène de jeu
 *
 * @author Romain Veydarier
 * @since 28/04/2023
 */
public class GameSceneController implements Initializable {

    /**
     * La position actuelle du joueur dans la grille.
     */
    private Vector2 position;

    /**
     * La forme actuellement sélectionnée par le joueur.
     */
    Shape currentSelectedShape;

    /**
     * Le conteneur racine de l'interface utilisateur.
     */
    @FXML
    private Pane root;

    /**
     * Le conteneur de la grille de jeu.
     */
    @FXML
    private GridPane grid;

    /**
     * Le conteneur parent des formes de jeu disponibles.
     */
    @FXML
    private FlowPane shapesParent;

    /**
     * Le label affichant le nom du joueur situé à gauche de l'écran.
     */
    @FXML
    private Label leftPlayerName;
    /**
     * Le label affichant le nombre de pièces restantes du joueur situé à gauche de l'écran.
     */
    @FXML
    private Label leftNbPiece;

    /**
     * Le label affichant le nom du joueur situé en bas de l'écran.
     */
    @FXML
    private Label frontPlayerName;
    /**
     * Le label affichant le nombre de pièces restantes du joueur situé en bas de l'écran.
     */
    @FXML
    private Label frontNbPiece;

    /**
     * Le label affichant le nom du joueur situé à droite de l'écran.
     */
    @FXML
    private Label rightPlayerName;
    /**
     * Le label affichant le nombre de pièces restantes du joueur situé à droite de l'écran.
     */
    @FXML
    private Label rightNbPiece;

    /**
     * Le label affichant le temps restant pour le tour du joueur actuel.
     */
    @FXML
    private Label playerTimer;
    /**
     * Le minuteur utilisé pour compter le temps restant.
     */
    private Timer timer;

    /**
     * Couleur du joueur
     */
    private GameColor pColor;

    /**
     * Grille qui contient les pixels du jeu
     */
    private final int[][] boardGrid = new int[20][20];
    /**
     * Dictionnaire qui relie un identifiant à un texte
     */
    private final Map<Integer, Label> playersIdToNbShapeText = new HashMap<>();
    /**
     * Dictionnaire qui relie un type de forme à un groupe de pixel
     */
    private final Map<ShapeType, Group> shapesTypeToGroup = new HashMap<>();

    /**
     * Variable pour savoir si c'est le premier placement
     */
    private boolean firstPlace = true;
    /**
     * Liste des pixels que l'on prévisualise
     */
    private List<Pixel> pixelPreviewed = new ArrayList<>();

    /**
     * Initialise le contrôleur une fois que la vue a été chargée.
     *
     * @param url              L'URL de la vue.
     * @param resourceBundle   Les ressources associées à la vue.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameApplication.getInstance().OnShapePlacedEvent.addListener(this::OnShapePlaced);
        GameApplication.getInstance().WhenMyTurn.addListener(this::WhenIsMyTurn);
        GameApplication.getInstance().OnGameRankReceivedEvent.addListener(this::OnGameRankReceived);
        currentSelectedShape = new Shape(ShapeType.C3_2);

        pColor = GameColor.values()[GameApplication.getInstance().pId];

        Platform.runLater(() -> {
            InitializeLater();
            InitializeGrid();
            InitializePlayerShape();
        });
    }

    /**
     * Initialise les éléments de l'interface graphique ultérieurement.
     */
    private void InitializeLater() {
        // Récupération de la scène
        Scene scene = root.getScene();

        // Ajout de la détection des clics de souris
        scene.setOnMouseClicked(this::OnMouseClicked);

        // Mise à jour des informations des joueurs
        UpdatePlayersName();
        leftNbPiece.setText(String.valueOf(ShapeType.values().length - 1));
        frontNbPiece.setText(String.valueOf(ShapeType.values().length - 1));
        rightNbPiece.setText(String.valueOf(ShapeType.values().length - 1));
    }
    /**
     * Initialise la grille du jeu.
     */
    private void InitializeGrid() {
        // Affichage de la grille avant l'initialisation
        for (int y = 0; y < boardGrid.length; y++) {
            for (int x = 0; x < boardGrid[y].length; x++) {
                System.out.println(boardGrid[x][y]);
            }
        }

        // Initialisation de la grille avec des valeurs par défaut (-1)
        for (int y = 0; y < boardGrid.length; y++) {
            for (int x = 0; x < boardGrid[y].length; x++) {
                boardGrid[x][y] = -1;
            }
        }

        // Affichage de la grille après l'initialisation
        for (int y = 0; y < boardGrid.length; y++) {
            for (int x = 0; x < boardGrid[y].length; x++) {
                System.out.println(boardGrid[x][y]);
            }
        }

        // Parcours des lignes et des colonnes de la grille
        for (int row = 0; row < grid.getRowCount(); row++) {
            for (int col = 0; col < grid.getColumnCount(); col++) {
                // Création d'une cellule de type Rectangle
                Rectangle cell = new Rectangle(30, 30, Color.WHITE);
                cell.setStroke(Color.BLACK);
                cell.setStrokeWidth(2.0);

                // Gestion de l'événement lorsque la souris entre dans la cellule
                cell.setOnMouseEntered(e -> {
                    Node source = e.getPickResult().getIntersectedNode();
                    Integer colIndex = GridPane.getColumnIndex(source);
                    Integer rowIndex = GridPane.getRowIndex(source);

                    // Vérification si une ligne ou une colonne peut être récupérée à partir du nœud cliqué
                    if(colIndex == null || rowIndex == null) return;

                    // Actions à effectuer lorsque la souris entre dans la cellule
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

                // Gestion de l'événement lorsque la souris quitte la cellule
                cell.setOnMouseExited(event -> {
                    ClearPreviewedList();
                });

                // Ajout de la cellule à la grille
                grid.add(cell, col, row);
            }
        }

        // Application des couleurs aux coins de la grille pour les points de départ
        for(GameColor c : GameColor.values()) {
            Rectangle rect = (Rectangle) grid.getChildren().stream()
                    .filter(node -> GridPane.getRowIndex(node) == c.getGridStartPos().GetY() && GridPane.getColumnIndex(node) == c.getGridStartPos().GetX())
                    .findFirst()
                    .orElse(null);
            if(rect == null) continue;

            rect.setFill(c.getColor().deriveColor(0, 1, 1, 0.5));
        }
    }
    /**
     * Initialise la forme du joueur.
     */
    private void InitializePlayerShape() {
        // Pour chaque type de forme différente dans le jeu
        for(ShapeType type : ShapeType.values()) {
            if(type == ShapeType.NONE) continue;

            // Création d'un groupe pour la forme
            Group group = new Group();

            // Parcours des coordonnées des pixels de la forme
            for(int[] coord : type.getCoordsPixel()) {
                // Création d'un objet Pixel avec le type, la couleur et les dimensions spécifiées
                Pixel pixel = new Pixel(type, pColor, new Vector2(15, 15));
                pixel.getImg().setLayoutX(coord[0] * 15);
                pixel.getImg().setLayoutY(coord[1] * 15);

                // Gestion de l'événement lorsque le pixel est cliqué
                pixel.getImg().setOnMousePressed((e) -> OnShapePressed(type));

                // Ajout de l'image du pixel au groupe
                group.getChildren().add(pixel.getImg());
            }

            // Ajout du groupe correspondant au type de forme à la map
            shapesTypeToGroup.put(type, group);

            // Ajout du groupe à la parent des formes
            shapesParent.getChildren().add(group);
        }
    }


    /**
     * Fonction appelée quand il y a une nouvelle pièce de posé
     *
     * @param args Argument de l'évènement
     */
    private void OnShapePlaced(EventArgs args) {
        ShapePlacedArgs shapePlacedArgs = (ShapePlacedArgs)args;

        UpdatePlayerShapeNumber(shapePlacedArgs.pId);
        GameColor color = GameColor.values()[shapePlacedArgs.pId];
        AddShapeToGrid(shapePlacedArgs.shape, shapePlacedArgs.position, color, false);
    }
    private void OnGameRankReceived(EventArgs args) {
        GameRankArgs gameRankArgs = (GameRankArgs)args;

        Image img;
        if(gameRankArgs.rank == 1) {
            img = new Image(GifType.Victory.getGifFile().toURI().toString());
        } else {
            img = new Image(GifType.Defeat.getGifFile().toURI().toString());
        }
        Platform.runLater(() -> {
            // Création de l'ImageView pour afficher le GIF
            ImageView gifImageView = new ImageView(img);
            gifImageView.setPreserveRatio(true);

            Stage stage = GameApplication.getInstance().getStage();
            double windowWidth = stage.getWidth();
            double windowHeight = stage.getHeight();
            gifImageView.setFitWidth(windowWidth);
            gifImageView.setFitHeight(windowHeight);

            root.getChildren().add(gifImageView);

            Button button = new Button("Menu principal");
            button.setPrefSize(331, 51);
            button.setTranslateX(stage.getScene().getWidth()); // Déplace le bouton en dehors de l'écran à droite
            button.setTranslateY(stage.getScene().getHeight() / 2); // Centre verticalement le bouton

            TranslateTransition transition = new TranslateTransition(Duration.seconds(1), button);
            transition.setToX((stage.getScene().getWidth()/2) - button.getPrefWidth()/2); // Déplace le bouton vers la position X = 0
            transition.play();

            root.getChildren().add(button);

            // Attendez 3 secondes avant d'afficher le bouton
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(event -> {
                button.setVisible(true);
            });
            delay.play();

            button.setOnAction((e) -> {
                try {
                    GameApplication.getInstance().GameFinished();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });
    }
    /**
     * Fonction appelée lorsque c'est au tour du joueur, elle est appelée par l'event WhenMyTurn
     *
     * @param args Argument de l'évènement
     */
    private void WhenIsMyTurn(EventArgs args) {
        StartTimer();

        Platform.runLater(() -> {
            // Création du Label avec le texte souhaité
            Label label = new Label("C'est à votre tour !");

            // Définition du style du Label pour permettre le défilement du texte
            label.setStyle("-fx-background-color: white; -fx-padding: 10px;");
            label.setWrapText(true);

            // Création de la transition de déplacement
            TranslateTransition transition = new TranslateTransition(Duration.seconds(5), label);
            transition.setFromX(-label.getWidth());
            transition.setToX(400); // Définir la distance de déplacement souhaitée

            // Création d'une pause à mi-chemin pour l'arrêt du texte
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> {
                transition.stop();
                root.getChildren().remove(label);
            });

            // Démarrage de la transition
            transition.play();
            pause.play();

            root.getChildren().add(label);
        });
    }

    /**
     * Fonction qui permet de mettre en place le timer
     */
    private void StartTimer() {
        System.out.println("StartTimer()");

        timer = new Timer();

        TimerTask task = new TimerTask() {
            int count = 15; // Nombre total de secondes

            @Override
            public void run() {
                if (count > 0) {
                    Platform.runLater(() -> {
                        System.out.println("Count : " + count);
                        playerTimer.setText("" + count);
                    });
                    count--;
                } else {
                    StopTimer();
                    Abandoned();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }
    /**
     * Fonction qui permet d'arrêter le timer
     */
    private void StopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            Platform.runLater(() -> {
                playerTimer.setText("15");
            });
        }
    }
    /**
     * Fonction qui permet d'abandonner
     */
    private void Abandoned() {
        GameApplication.getInstance().myTurn = false;
        if(GameApplication.getInstance().getIdentity() == NetworkIdentity.CLIENT)
            GameApplication.getInstance().SendMessage(new AbandonedMessage(GameApplication.getInstance().pId));
        else
            GameApplication.getInstance().RemoveTurnPlayer(-1);
    }

    /**
     * Fonction pour mettre à jour le nombre de pièces de chaque joueur
     *
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
            tempPlayersNameTextList.get(i).setText(GameApplication.getInstance().playerStructs.get(index).pName);
            tempPlayersNameTextList.get(i).setBackground(new Background(new BackgroundFill(GameColor.values()[index].getColor(), null, null)));
            index++;
        }
    }


    /**
     * Fonction qui gère l'événement de clic de souris.
     *
     * @param event L'événement de clic de souris
     */
    private void OnMouseClicked(MouseEvent event) {
        if(event.getButton() == MouseButton.SECONDARY) {
            // Rotation vers la droite de la forme sélectionnée
            currentSelectedShape = currentSelectedShape.RightRotate();

            // Effacer la liste de prévisualisation des pixels s'il y en a
            if (pixelPreviewed.size() > 0)
                ClearPreviewedList();

            // Vérifier si la forme sélectionnée dépasse des limites de la grille
            if (CheckIfOutOfBounds()) {
                // Ajouter la forme à la grille pour la prévisualisation
                pixelPreviewed = AddShapeToGrid(currentSelectedShape, position, pColor, true);

                // Rendre les images des pixels de prévisualisation non cliquables
                for (Pixel p : pixelPreviewed) {
                    p.getImg().setMouseTransparent(true);
                }
            }
        }

        if(event.getButton() == MouseButton.PRIMARY) {
            // Placer la forme sélectionnée
            PlaceShape();
        }
        if(event.getButton() == MouseButton.MIDDLE) {
            GameRankArgs args = new GameRankArgs(1);
            GameApplication.getInstance().SendMessage(new GameRankMessage(args));

            if(GameApplication.getInstance().getIdentity() == NetworkIdentity.SERVER) {
                OnGameRankReceived(args);
            }
        }
    }
    /**
     * Fonction appelée quand on sélectionne une forme depuis le menu à droite de la grille
     *
     * @param type Type de la forme
     */
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
    /**
     * Fonction qui permet de savoir si on peut poser la pièce
     *
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
    /**
     * Fonction qui permet de vérifier si la ou passe la souris la forme peut rentrer dans la grille
     *
     * @return Vrai si la forme peut rentrer dans la grille
     */
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
    /**
     * Fonction qui permet d'avoir tous les angles d'une forme
     *
     * @return La liste des angles de la forme que le joueur a sélectionné
     */
    private List<Vector2> GetCornerOfShape() {
        List<Vector2> tempPos = new ArrayList<>();
        List<Vector2> directUP_DOWN_LEFT_RIGHT = new ArrayList<>();

        // Récupération pour chaque pixel de la forme des voisins direct ainsi que des angles
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
    /**
     * Fonction qui permet de démarrer l'action de poser une forme
     */
    public void PlaceShape() {
        // Vérification que c'est le tour du joueur
        if(!GameApplication.getInstance().myTurn) return;
        // Si le joueur n'a rien sélectionner alors on return
        if(currentSelectedShape.getType() == ShapeType.NONE) return;
        // S'il ne peut pas poser la pièce
        if(!CheckIfCanPlace()) return;

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
        GameApplication.getInstance().myTurn = false;
        StopTimer();

        currentSelectedShape = new Shape(ShapeType.NONE);
    }
    /**
     * Fonction qui permet d'ajouter une forme à la grille
     *
     * @param shape La forme à ajouter
     * @param pos La position X et Y de la grille
     * @param color La couleur que la forme doit avoir
     * @param preview Sert à rajouter un élément à la liste de preview
     *
     * @return La liste des pixels ajoutée
     */
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
    /**
     * Fonction pour retirer tous les éléments de la liste de preview
     */
    private void ClearPreviewedList() {
        for(Pixel p : pixelPreviewed) {
            grid.getChildren().remove(p.getImg());
        }
    }
}
