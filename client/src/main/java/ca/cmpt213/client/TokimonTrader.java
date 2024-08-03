package ca.cmpt213.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class TokimonTrader extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        Label title = new Label("Tokimon Trading Card Game");
        title.setFont(new Font("Arial", 20));
        title.setPadding(new Insets(15));

        GridPane tokiGrid = getAllTokimon();

        VBox vbox = new VBox(20, title, tokiGrid);
        vbox.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(vbox, 1024, 768);

        stage.setTitle("Tokimon");
        stage.setScene(scene);
        stage.show();
    }

    private GridPane getAllTokimon() {
        GridPane tokiGrid = new GridPane();
        int gridX = 0;
        int gridY = 0;

        try {
            URI uri = new URI("http://localhost:8080/api/tokimon/all");
            URL url = new URL(uri.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            Gson gson = new Gson();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> tokimons = gson.fromJson(response.toString(), listType);

            for (Map<String, Object> tokimon : tokimons) {
                Font primary = new Font("Arial", 16);
                Font secondary = new Font("Arial", 14);

                VBox tokiDetails = new VBox();
                Label tokiName = new Label("Name: " + tokimon.get("name"));
                tokiName.setFont(primary);
                Label tokiType = new Label("Type: " + tokimon.get("type"));
                tokiType.setFont(secondary);
                Label tokiRarity = new Label("Rarity: " + tokimon.get("rarity"));
                tokiRarity.setFont(primary);
                Label tokiHp = new Label("HP: " + tokimon.get("hp"));
                tokiHp.setFont(secondary);

                Image tokiImg = new Image((String) tokimon.get("pictureUrl"));
                ImageView tokiImage = new ImageView(tokiImg);
                tokiImage.setFitHeight(150);
                tokiImage.setPreserveRatio(true);

                Button details = new Button("Details");
                Button delete = new Button("Delete");
                HBox actions = new HBox(10, details, delete);

                tokiDetails.getChildren().addAll(tokiImage, tokiName, tokiType, tokiRarity, tokiHp, actions);

                Border border = new Border(new BorderStroke(
                        Color.BLACK,
                        BorderStrokeStyle.SOLID,
                        null,
                        new BorderWidths(1)
                ));
                tokiDetails.setBorder(border);
                tokiDetails.setPadding(new Insets(15));

                tokiGrid.add(tokiDetails, gridX, gridY);
                tokiGrid.setAlignment(Pos.CENTER);
                tokiGrid.setHgap(20);
                tokiGrid.setVgap(20);

                gridX++;
                if (gridX % 3 == 0) {
                    gridX = 0;
                    gridY++;
                }

            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return tokiGrid;
    }

    private void addAction(Button button) {

    }

    private void getTokimon(long tid) {

    }



    public static void main(String[] args) {
        launch();
    }
}