module PacManMaven {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    opens com to javafx.fxml;
    opens com.UI to javafx.fxml;
    exports com;
    exports com.UI;
}