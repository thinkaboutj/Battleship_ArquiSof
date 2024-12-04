package Cliente;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * Created by Patka on 2017-08-04.
 */
public class StatusController {
    private Label statusLabel;

    StatusController(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void setPlayersLabel(String status) {
        Platform.runLater(() -> statusLabel.setText(status));
    }
}
