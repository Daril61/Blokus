module blokus.blokus {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens blokus.game to javafx.fxml;

    exports blokus.game;
    exports blokus.utils;
    exports blokus.utils.eventArgs;
    exports blokus.utils.message;
}