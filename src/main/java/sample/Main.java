package main.java.sample;


import hr.java.covidportal.load.BazaPodataka;
import hr.java.covidportal.main.Glavna;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    public static final Logger loggerMain = LoggerFactory.getLogger(Main.class);

    private static Stage mainStage;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        primaryStage.setTitle("Bekavac-10");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        mainStage = primaryStage;
        BazaPodataka.dohvatiZupanijuById((long) 2);
    }

    public static Stage getMainStage(){
        return  mainStage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
