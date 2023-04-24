module blokus.blokus {
    requires javafx.controls;
    requires javafx.fxml;

    opens blokus.game to javafx.fxml;

    exports blokus.game;
    exports blokus.utils;
}