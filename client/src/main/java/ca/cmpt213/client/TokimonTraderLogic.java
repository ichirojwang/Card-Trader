package ca.cmpt213.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class TokimonTraderLogic {

    GridPane tokimonDisplay;

    public TokimonTraderLogic() {
        tokimonDisplay = new GridPane();
        tokimonDisplay.setAlignment(Pos.CENTER);
        tokimonDisplay.setHgap(20);
        tokimonDisplay.setVgap(20);
        tokimonDisplay.setPadding(new Insets(20));
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
            System.out.println("error");
            e.printStackTrace();
        }

        return null;

    }

    // Displaying all Tokimons on window
    public void gridSetUp() {
        tokimonDisplay.getChildren().clear();
        int gridX = 0;
        int gridY = 0;

        List<Tokimon> tokimons = readData();
        if (tokimons != null || !tokimons.isEmpty()) {

            Border borderBlack = new Border(new BorderStroke(
                    Color.BLACK,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(5),
                    new BorderWidths(3)
            ));

            for (Tokimon tokimon : tokimons) {

                Label tokiName = new Label("Name: " + tokimon.getName());
                tokiName.setFont(new Font("Arial", 16));

                ImageView tokiIV = setTokiImage(tokimon);
                tokiIV.setFitHeight(100);
                tokiIV.setPreserveRatio(true);
                VBox tokiIVVbox = new VBox(tokiIV);
                tokiIVVbox.setPrefHeight(120);
                tokiIVVbox.setPrefWidth(180);
                tokiIVVbox.setPadding(new Insets(10, 0, 0, 0));
                tokiIVVbox.setAlignment(Pos.CENTER);

                VBox tokiNameVbox = new VBox(tokiName);
                tokiNameVbox.setAlignment(Pos.CENTER);
                tokiNameVbox.setPadding(new Insets(10));

                Button getTokimon = getTokimonAction(tokimon);
                getTokimon.setPrefWidth(55);
                getTokimon.setTextAlignment(TextAlignment.LEFT);
                getTokimon.setAlignment(Pos.BASELINE_LEFT);
                Button editTokimon = editTokimonAction(tokimon);
                editTokimon.setPrefWidth(55);
                editTokimon.setTextAlignment(TextAlignment.LEFT);
                editTokimon.setAlignment(Pos.BASELINE_LEFT);
                Button deleteTokimon = deleteTokimonAction(tokimon);
                deleteTokimon.setPrefWidth(55);
                deleteTokimon.setTextAlignment(TextAlignment.LEFT);
                deleteTokimon.setAlignment(Pos.BASELINE_LEFT);

                VBox actions = new VBox(10, getTokimon, editTokimon, deleteTokimon);
                actions.setPadding(new Insets(20));
                actions.setAlignment(Pos.CENTER_LEFT);

                VBox tokiCard = new VBox(tokiIVVbox, tokiNameVbox, actions);
                tokiCard.setPrefHeight(256);
                tokiCard.setPrefWidth(192);
                tokiCard.setAlignment(Pos.TOP_CENTER);
                tokiCard.setBorder(borderBlack);
                tokiCard.setPadding(new Insets(5));

                tokimonDisplay.add(tokiCard, gridX, gridY);

                if (++gridX % 4 == 0) {
                    gridX = 0;
                    gridY++;
                }

            }
        }

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

        tokimonDisplay.add(addTokimonVBox, gridX, gridY);

    }

    private Button addTokimonAction() {
        Button addTokimon = new Button("Add Tokimon");
        Button confirmTokimon = new Button("Confirm");
        final boolean[] opened = {false};

        addTokimon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (!opened[0]) {
                    Stage addStage = new Stage();
                    addStage.setTitle("Add Tokimon");

                    Label tokiNameLabel = new Label("Name: ");
                    TextField tokiNameTextField = new TextField();
                    tokiNameTextField.setPromptText("Enter Name...");

                    Label tokiTypeLabel = new Label("Type: ");
                    ComboBox<String> tokiTypeComboBox = new ComboBox<>();
                    tokiTypeComboBox.setPromptText("Select Type");
                    tokiTypeComboBox.getItems().addAll("Water", "Fire", "Grass", "Electric", "Psychic");

                    Label tokiRarityLabel = new Label("Rarity: ");
                    ComboBox<Integer> tokiRarityComboBox = new ComboBox<>();
                    tokiRarityComboBox.setPromptText("Select Rarity");
                    for (int i = 1; i < 11; i++) {
                        tokiRarityComboBox.getItems().add(i);
                    }

                    Label tokiPictureUrlLabel = new Label("Picture URL: ");
                    TextField tokiPictureUrlTextField = new TextField();
                    tokiPictureUrlTextField.setPromptText("Enter Picture URL...");

                    Label tokiHpLabel = new Label("HP: ");
                    TextField tokiHpTextField = new TextField();
                    tokiHpTextField.setPromptText("Enter HP...");
                    tokiHpTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue.matches("\\d{0,4}+")) {
                            tokiHpTextField.setText(oldValue);
                        }
                    });

                    confirmTokimon.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {

                            try {
                                System.out.println("http://localhost:8080/api/tokimon/add");

                                URI uri = new URI("http://localhost:8080/api/tokimon/add");
                                URL url = uri.toURL();
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("POST");
                                connection.setDoOutput(true);
                                connection.setRequestProperty("Content-Type", "application/json");

                                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

                                String tokiAsString = "";

                                if (!tokiNameTextField.getText().trim().isEmpty() &&
                                        tokiTypeComboBox.getSelectionModel().getSelectedItem() != null &&
                                        tokiRarityComboBox.getSelectionModel().getSelectedItem() != null &&
                                        !tokiPictureUrlTextField.getText().trim().isEmpty() &&
                                        !tokiHpTextField.getText().isEmpty())
                                {
                                    tokiAsString = "{\"name\":\"" + tokiNameTextField.getText() + "\",\"type\":\"" +
                                            tokiTypeComboBox.getValue() + "\",\"rarity\":" +
                                            Integer.parseInt(tokiRarityComboBox.getValue().toString()) +
                                            ",\"pictureUrl\":\"" + tokiPictureUrlTextField.getText() + "\",\"hp\":" +
                                            Integer.parseInt(tokiHpTextField.getText()) + "}";
                                }

                                if (!tokiAsString.isEmpty()) {
                                    System.out.println(tokiAsString);
                                    writer.write(tokiAsString);
                                    writer.flush();
                                    writer.close();
                                    connection.connect();

                                    System.out.println(connection.getResponseCode());
                                    if (connection.getResponseCode() == 201) {
                                        System.out.println("added");
                                        gridSetUp();
                                        addStage.close();
                                    }
                                    else {
                                        System.out.println("not added");
                                    }
                                }
                                else {
                                    System.out.println("enter tokimon details");
                                }


                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    VBox tokiAdd = new VBox();
                    tokiAdd.getChildren().addAll(tokiNameLabel, tokiNameTextField, tokiTypeLabel, tokiTypeComboBox,
                            tokiRarityLabel, tokiRarityComboBox, tokiPictureUrlLabel,
                            tokiPictureUrlTextField, tokiHpLabel, tokiHpTextField, confirmTokimon);
                    tokiAdd.setAlignment(Pos.CENTER_LEFT);
                    tokiAdd.setPadding(new Insets(20));

                    Scene addScene = new Scene(tokiAdd, 384, 512);
                    addStage.setScene(addScene);
                    addStage.show();

                    addScene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeAdd);
                    System.out.println("opening add window");
                    opened[0] = true;
                }
                else {
                    System.out.println("already opened");
                }
            }

            private void closeAdd(WindowEvent windowEvent) {
                System.out.println("closing add window");
                opened[0] = false;
            }
        });

        return addTokimon;
    }

    // Button for showing details of Tokimon Card
    private Button getTokimonAction(Tokimon tokimon) {
        Button details = new Button("Details");
        final boolean[] opened = {false};

        details.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!opened[0]) {
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
                    tokiAttributes.setPadding(new Insets(20));
                    tokiAttributes.setAlignment(Pos.CENTER_LEFT);



                    VBox tokiCard = new VBox(15, tokiIVVbox, tokiAttributes);
                    tokiCard.setAlignment(Pos.TOP_CENTER);
                    tokiCard.setPadding(new Insets(40));

                    Scene detailsScene = new Scene(tokiCard, 576, 768);
                    detailsStage.setScene(detailsScene);
                    detailsStage.show();

                    detailsScene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeDetails);
                    System.out.println("opening details");
                    opened[0] = true;
                }
                else {
                    System.out.println("already opened");
                }
            }

            private void closeDetails(WindowEvent windowEvent) {
                System.out.println("closing details");
                opened[0] = false;
            }
        });

        return details;
    }

    private Button editTokimonAction(Tokimon tokimon) {
        Button editTokimon = new Button("Edit");
        Button confirmTokimon = new Button("Confirm Edits");
        final boolean[] opened = {false};

        editTokimon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!opened[0]) {
                    Stage editStage = new Stage();
                    editStage.setTitle("Tokimon Editor");

                    Label tokiNameLabel = new Label("Name: ");
                    TextField tokiNameTextField = new TextField();
                    tokiNameTextField.setPromptText("Enter Name...");
                    tokiNameTextField.setText(tokimon.getName());

                    Label tokiTypeLabel = new Label("Type: ");
                    ComboBox<String> tokiTypeComboBox = new ComboBox<>();
                    tokiTypeComboBox.getItems().addAll("Water", "Fire", "Grass", "Electric", "Psychic");
                    tokiTypeComboBox.getSelectionModel().select(tokimon.getType());

                    Label tokiRarityLabel = new Label("Rarity: ");
                    ComboBox<Integer> tokiRarityComboBox = new ComboBox<>();
                    for (int i = 1; i < 11; i++) {
                        tokiRarityComboBox.getItems().add(i);
                    }
                    tokiRarityComboBox.getSelectionModel().select(tokimon.getRarity());

                    Label tokiPictureUrlLabel = new Label("Picture URL: ");
                    TextField tokiPictureUrlTextField = new TextField();
                    tokiPictureUrlTextField.setPromptText("Enter Picture URL...");
                    tokiPictureUrlTextField.setText(tokimon.getPictureUrl());

                    Label tokiHpLabel = new Label("HP: ");
                    TextField tokiHpTextField = new TextField();
                    tokiHpTextField.setPromptText("Enter HP...");
                    tokiHpTextField.setText(String.valueOf(tokimon.getHp()));
                    tokiHpTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue.matches("\\d{0,4}+")) {
                            tokiHpTextField.setText(oldValue);
                        }
                    });

                    confirmTokimon.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {

                            try {
                                System.out.println("http://localhost:8080/api/tokimon/edit/" + tokimon.getTid());

                                URI uri = new URI("http://localhost:8080/api/tokimon/edit/" + tokimon.getTid());
                                URL url = uri.toURL();
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("PUT");
                                connection.setDoOutput(true);
                                connection.setRequestProperty("Content-Type", "application/json");

                                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

                                String tokiAsString = "";

                                if (!tokiNameTextField.getText().trim().isEmpty() &&
                                        tokiTypeComboBox.getSelectionModel().getSelectedItem() != null &&
                                        tokiRarityComboBox.getSelectionModel().getSelectedItem() != null &&
                                        !tokiPictureUrlTextField.getText().trim().isEmpty() &&
                                        !tokiHpTextField.getText().isEmpty())
                                {
                                    tokiAsString = "{\"name\":\"" + tokiNameTextField.getText() + "\",\"type\":\"" +
                                            tokiTypeComboBox.getValue() + "\",\"rarity\":" +
                                            Integer.parseInt(tokiRarityComboBox.getValue().toString()) +
                                            ",\"pictureUrl\":\"" + tokiPictureUrlTextField.getText() + "\",\"hp\":" +
                                            Integer.parseInt(tokiHpTextField.getText()) + "}";
                                }

                                if (!tokiAsString.isEmpty()) {
                                    System.out.println(tokiAsString);
                                    writer.write(tokiAsString);
                                    writer.flush();
                                    writer.close();
                                    connection.connect();

                                    System.out.println(connection.getResponseCode());

                                    if (connection.getResponseCode() == 200) {
                                        System.out.println("edited");
                                        gridSetUp();
                                        editStage.close();
                                    }
                                    else {
                                        System.out.println("not edited");
                                    }
                                }
                                else {
                                    System.out.println("enter tokimon details");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    });

                    VBox tokiEdit = new VBox();
                    tokiEdit.getChildren().addAll(tokiNameLabel, tokiNameTextField, tokiTypeLabel, tokiTypeComboBox,
                            tokiRarityLabel, tokiRarityComboBox, tokiPictureUrlLabel,
                            tokiPictureUrlTextField, tokiHpLabel, tokiHpTextField, confirmTokimon);
                    tokiEdit.setAlignment(Pos.CENTER_LEFT);
                    tokiEdit.setPadding(new Insets(20));

                    Scene editScene = new Scene(tokiEdit, 384, 512);
                    editStage.setScene(editScene);
                    editStage.show();

                    editScene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeEdit);
                    System.out.println("opening edit window");
                    opened[0] = true;
                }
            }

            private void closeEdit(WindowEvent windowEvent) {
                System.out.println("closing details");
                opened[0] = false;
            }
        });

        return editTokimon;
    }

    private Button deleteTokimonAction(Tokimon tokimon) {
        Button deleteTokimon = new Button("Delete");

        deleteTokimon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    System.out.println("http://localhost:8080/api/tokimon/" + tokimon.getTid());

                    URI uri = new URI("http://localhost:8080/api/tokimon/" + tokimon.getTid());
                    URL url = new URL(uri.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("DELETE");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 204) {
                        System.out.println("removed " + tokimon.getName());
                        gridSetUp();
//                        detailsStage.close();
                    }
                    else {
                        System.out.println("failed to remove " + tokimon.getName());
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

        return new ImageView(tokiImage);
    }
}
