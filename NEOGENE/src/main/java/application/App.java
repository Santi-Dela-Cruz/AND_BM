package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/main_view.fxml"));
        Scene scene = new Scene(root);

        // Cargar el archivo de estilos CSS
        scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
        
        stage.setTitle("NEOGENE - DNA Sequence Analyzer");
        stage.setScene(scene);
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.setResizable(true);
        stage.show();
    }
}