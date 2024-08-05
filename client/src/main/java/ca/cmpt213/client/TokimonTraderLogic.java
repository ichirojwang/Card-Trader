package ca.cmpt213.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class TokimonTraderLogic {

    GridPane tokimonDisplay;

    public TokimonTraderLogic() {
        tokimonDisplay = new GridPane();
        gridSetUp();
    }

    public GridPane getTokimonDisplay() {
        return tokimonDisplay;
    }

    // Get all Tokimon from JSON
    private List<Tokimon> readData() {
        try {
            System.out.println("http://localhost:8080/api/tokimon/all");

            URI uri = new URI("http://localhost:8080/api/tokimon/all");
            URL url = new URL(uri.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            Gson gson = new GsonBuilder().create();
            Type listType = new TypeToken<List<Tokimon>>() {}.getType();

            return gson.fromJson(response.toString(), listType);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    // Displaying all Tokimons on window
    public void gridSetUp() {
        tokimonDisplay.getChildren().clear();
        int gridX = 0;
        int gridY = 0;

        Button addTokimon = addTokimonAction();
        VBox addTokimonVBox = new VBox(addTokimon);
        addTokimonVBox.setPrefHeight(256);
        addTokimonVBox.setPrefWidth(192);
        addTokimonVBox.setAlignment(Pos.CENTER);

        Border borderRed = new Border(new BorderStroke(
                Color.RED,
                BorderStrokeStyle.DASHED,
                new CornerRadii(5),
                new BorderWidths(3)
        ));
        addTokimonVBox.setBorder(borderRed);

        List<Tokimon> tokimons = readData();
        if (tokimons == null || tokimons.isEmpty()) {
            tokimonDisplay.add(addTokimonVBox, 0, 0);
            tokimonDisplay.setAlignment(Pos.CENTER);
        }

        for (Tokimon tokimon : tokimons) {

            Label tokiName = new Label("Name: " + tokimon.getName());
            tokiName.setFont(new Font("Arial", 16));

            ImageView tokiIV = setTokiImage(tokimon);
            tokiIV.setFitHeight(100);
            tokiIV.setPreserveRatio(true);
            VBox tokiIVVbox = new VBox(tokiIV);
            tokiIVVbox.setPrefHeight(120);
            tokiIVVbox.setPrefWidth(180);
            tokiIVVbox.setAlignment(Pos.CENTER);

            VBox tokiNameVbox = new VBox(tokiName);
            tokiNameVbox.setAlignment(Pos.CENTER_LEFT);
            tokiNameVbox.setPadding(new Insets(10));

            Button getTokimon = getTokimonAction(tokimon);

            VBox tokiCard = new VBox(tokiIVVbox, tokiNameVbox, getTokimon);
            tokiCard.setPrefHeight(256);
            tokiCard.setPrefWidth(192);
            tokiCard.setAlignment(Pos.TOP_CENTER);

            Border borderBlack = new Border(new BorderStroke(
                    Color.BLACK,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(5),
                    new BorderWidths(3)
            ));
            tokiCard.setBorder(borderBlack);
            tokiCard.setPadding(new Insets(5));

            tokimonDisplay.add(tokiCard, gridX, gridY);
            tokimonDisplay.setAlignment(Pos.CENTER);
            tokimonDisplay.setHgap(20);
            tokimonDisplay.setVgap(20);

            if (++gridX % 4 == 0) {
                gridX = 0;
                gridY++;
            }

        }

        tokimonDisplay.add(addTokimonVBox, gridX, gridY);

    }

    private Button addTokimonAction() {
        Button addTokimon = new Button("Add Tokimon");

        addTokimon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage addStage = new Stage();
                addStage.setTitle("Add Tokimon");

            }
        });



        return addTokimon;
    }

    // Button for showing details of Tokimon Card
    private Button getTokimonAction(Tokimon tokimon) {
        Button details = new Button("Details");

        details.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage detailsStage = new Stage();
                detailsStage.setTitle("Tokimon Details");

                ImageView tokiIV = setTokiImage(tokimon);
                tokiIV.setFitHeight(300);
                tokiIV.setPreserveRatio(true);
                VBox tokiIVVbox = new VBox(tokiIV);
                tokiIVVbox.setPrefHeight(360);
                tokiIVVbox.setPrefWidth(550);
                tokiIVVbox.setPadding(new Insets(0, 0, 30, 0));
                tokiIVVbox.setAlignment(Pos.CENTER);

                Font primary = new Font("Arial", 30);
                Font secondary = new Font("Arial", 24);
                Label tokiName = new Label("Name: " + tokimon.getName());
                tokiName.setFont(primary);
                Label tokiType = new Label("Type: " + tokimon.getType());
                tokiType.setFont(secondary);
                Label tokiRarity = new Label("Rarity: " + tokimon.getRarity());
                tokiRarity.setFont(secondary);
                Label tokiHp = new Label("HP: " + tokimon.getHp());
                tokiHp.setFont(secondary);

                VBox tokiAttributes = new VBox(tokiName, tokiType, tokiRarity, tokiHp);
                tokiAttributes.setAlignment(Pos.CENTER_LEFT);

                Button deleteTokimon = deleteTokimonAction(tokimon.getTid(), detailsStage);

                HBox actions = new HBox(deleteTokimon);

                VBox tokiCard = new VBox(tokiIV, tokiAttributes, actions);
                tokiCard.setAlignment(Pos.TOP_CENTER);
                tokiCard.setPadding(new Insets(40));

                Scene detailsScene = new Scene(tokiCard, 576, 768);
                detailsStage.setScene(detailsScene);
                detailsStage.show();
            }
        });

        return details;
    }

    private Button deleteTokimonAction(long tid, Stage detailsStage) {
        Button deleteTokimon = new Button("Delete");

        deleteTokimon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    System.out.println("http://localhost:8080/api/tokimon/" + tid);

                    URI uri = new URI("http://localhost:8080/api/tokimon/" + tid);
                    URL url = new URL(uri.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("DELETE");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 204) {
                        System.out.println("removed");
                        gridSetUp();
                        detailsStage.close();
                    }
                    else {
                        System.out.println("failed to remove");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return deleteTokimon;

    }

    private ImageView setTokiImage(Tokimon tokimon) {
        Image tokiImage;
        try {
            tokiImage = new Image(tokimon.getPictureUrl());
        }
        catch (Exception e) {
            tokiImage = new Image("https://static.vecteezy.com/system/resources/previews/023/051/675/original/invalid-rubber-stamp-seal-vector.jpg");
        }

        ImageView tokiIV = new ImageView(tokiImage);

        return tokiIV;
    }
}
