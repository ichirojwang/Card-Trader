package ca.cmpt213.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class TokimonTrader extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("Tokimon Trading Card Game");
        title.setFont(new Font("Arial", 20));
        title.setPadding(new Insets(15, 0, 0, 0));

        TokimonTraderLogic logic = new TokimonTraderLogic();

        GridPane tokiGrid = logic.getTokimonDisplay();

        VBox vbox = new VBox(20, title, tokiGrid);
        vbox.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 1024, 768);

        stage.setTitle("Tokimon Trader");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}