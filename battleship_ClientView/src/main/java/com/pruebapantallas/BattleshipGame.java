/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.pruebapantallas;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BattleshipGame extends Application {
    private Stage primaryStage;
    // Ajustamos el tamaño de la ventana para que sea más vertical
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showStartScreen();
    }

    private void showStartScreen() {
        // Cargar y configurar la imagen de fondo
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/PantallaInicio.jpg"));
        ImageView background = new ImageView(backgroundImage);
        background.setFitWidth(WINDOW_WIDTH);
        background.setFitHeight(WINDOW_HEIGHT);

        // Cargar y configurar la imagen del título
        Image titleImage = new Image(getClass().getResourceAsStream("/images/titulo.png"));
        ImageView titleImageView = new ImageView(titleImage);
        titleImageView.setFitWidth(500); // Ajustamos el ancho para que sea más grande
        titleImageView.setPreserveRatio(true);

        // Configurar el texto de "PRESS START" con animación
        Text startText = new Text("PRESS START");
        startText.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        startText.setFill(Color.YELLOW);

        // Animación de parpadeo para "PRESS START"
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.8), startText);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.3);
        fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();

        // Interactividad para el inicio del juego
        startText.setOnMouseClicked(e -> showEnterNameScreen());

        // Layout de la pantalla inicial, con el título más cerca de la parte superior
        VBox startBox = new VBox(50, titleImageView, startText); // Espacio adicional entre título y texto
        startBox.setAlignment(Pos.CENTER);

        // Alineamos el VBox al centro superior de la pantalla
        StackPane.setAlignment(startBox, Pos.TOP_CENTER);

        // Crear la escena principal con fondo y contenido
        StackPane root = new StackPane(background, startBox);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        primaryStage.setTitle("Batalla Naval");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void showEnterNameScreen() {
        // Cargar y configurar la imagen de fondo
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/PantallaInicio.jpg"));
        ImageView background = new ImageView(backgroundImage);
        background.setFitWidth(WINDOW_WIDTH);
        background.setFitHeight(WINDOW_HEIGHT);

        // Título con la imagen en lugar de texto
        Image titleImage = new Image(getClass().getResourceAsStream("/images/titulo.png"));
        ImageView titleImageView = new ImageView(titleImage);
        titleImageView.setFitWidth(500); // Mismo tamaño que en la pantalla de inicio
        titleImageView.setPreserveRatio(true);

        // Campo de texto para nombre
        TextField nameField = new TextField();
        nameField.setPromptText("Nombre");
        nameField.setMaxWidth(200);

        // Texto para selección de color
        Text colorText = new Text("Seleccionar Color:");
        colorText.setFill(Color.WHITE);
        colorText.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Botones de selección de color
        HBox colorOptions = new HBox(10);
        colorOptions.setAlignment(Pos.CENTER);

        Button blueButton = new Button();
        blueButton.setStyle("-fx-background-color: blue; -fx-min-width: 50px; -fx-min-height: 50px;");
        
        Button redButton = new Button();
        redButton.setStyle("-fx-background-color: red; -fx-min-width: 50px; -fx-min-height: 50px;");
        
        colorOptions.getChildren().addAll(blueButton, redButton);

        // Texto de inicio con animación de parpadeo
        Text startText = new Text("PRESS START");
        startText.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        startText.setFill(Color.YELLOW);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.8), startText);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.3);
        fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();

        
        // Evento para el texto de inicio
        startText.setOnMouseClicked(e -> showLoadingScreen());

        // Layout de la pantalla de selección de nombre y color
        VBox nameScreenBox = new VBox(20, titleImageView, nameField, colorText, colorOptions, startText);
        nameScreenBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(background, nameScreenBox);
        Scene nameScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(nameScene);
    }

    private void showLoadingScreen() {
        VBox loadingScreen = new VBox(10);
        loadingScreen.setStyle("-fx-background-color: #000080;");
        loadingScreen.setAlignment(Pos.CENTER);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(0);
        progressBar.setPrefWidth(300);

        Text loadingText = new Text("Cargando...");
        loadingText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        loadingText.setFill(Color.WHITE);

        loadingScreen.getChildren().addAll(loadingText, progressBar);

        Scene loadingScene = new Scene(loadingScreen, WINDOW_WIDTH, WINDOW_HEIGHT);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> progressBar.setProgress(1.0)));
        timeline.setCycleCount(1);
        timeline.play();

        primaryStage.setScene(loadingScene);
        timeline.setOnFinished(e -> System.out.println("Cargando completado"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
