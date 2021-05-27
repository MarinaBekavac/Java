package main.java.sample;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PocetniEkranController implements Initializable {

    @FXML
    public void prikaziEkranZaPretraguZupanija() throws IOException{

        Parent pretragaZupanijaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaZupanija.fxml"));
        Scene pretragaZupanijaScene = new Scene(pretragaZupanijaFrame, 600, 400);
        Main.getMainStage().setScene(pretragaZupanijaScene);

    }

    @FXML
    public void prikaziEkranZaPretraguSimptoma() throws IOException{

        Parent pretragaSimptomaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaSimptoma.fxml"));
        Scene pretragaSimptomaScene = new Scene(pretragaSimptomaFrame, 600, 400);
        Main.getMainStage().setScene(pretragaSimptomaScene);

    }

    @FXML
    public void prikaziEkranZaPretraguOsoba() throws IOException{

        Parent pretragaOsobaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaOsoba.fxml"));
        Scene pretragaOsobaScene = new Scene(pretragaOsobaFrame, 600, 400);
        Main.getMainStage().setScene(pretragaOsobaScene);

    }

    @FXML
    public void prikaziEkranZaPretraguBolesti() throws IOException{

        Parent pretragaOsobaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaBolesti.fxml"));
        Scene pretragaOsobaScene = new Scene(pretragaOsobaFrame, 600, 400);
        Main.getMainStage().setScene(pretragaOsobaScene);

    }

    @FXML
    public void prikaziEkranZaPretraguVirusa() throws IOException{

        Parent pretragaOsobaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaVirusa.fxml"));
        Scene pretragaOsobaScene = new Scene(pretragaOsobaFrame, 600, 400);
        Main.getMainStage().setScene(pretragaOsobaScene);

    }

    @FXML
    public void dodajNovuZupaniju() throws IOException{

        Parent dodajZupanijuFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveZupanije.fxml"));
        Scene dodajZupanijuScene = new Scene(dodajZupanijuFrame, 600, 400);
        Main.getMainStage().setScene(dodajZupanijuScene);

    }

    @FXML
    public void dodajNoviSimptom() throws IOException{

        Parent dodajSimptomFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNovogSimptoma.fxml"));
        Scene dodajSimptomScene = new Scene(dodajSimptomFrame, 600, 400);
        Main.getMainStage().setScene(dodajSimptomScene);

    }

    @FXML
    public void dodajNoviVirus() throws IOException{

        Parent dodajVirusFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNovogVirusa.fxml"));
        Scene dodajVirusScene = new Scene(dodajVirusFrame, 600, 400);
        Main.getMainStage().setScene(dodajVirusScene);

    }

    @FXML
    public void dodajNovuBolest() throws IOException{

        Parent dodajBolestFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveBolesti.fxml"));
        Scene dodajBolestScene = new Scene(dodajBolestFrame, 600, 400);

        ////////////////////////////
        dodajBolestScene.getStylesheets().add(getClass().getClassLoader().
                getResource("redBorder.css").toExternalForm());
        /////////////////////////////////////////////////

        Main.getMainStage().setScene(dodajBolestScene);

    }

    @FXML
    public void dodajNovuOsobu() throws IOException{

        Parent dodajOsobuFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveOsobe.fxml"));
        Scene dodajOsobuScene = new Scene(dodajOsobuFrame, 600, 400);
        Main.getMainStage().setScene(dodajOsobuScene);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
