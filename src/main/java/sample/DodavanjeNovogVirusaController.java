package main.java.sample;

import hr.java.covidportal.load.BazaPodataka;
import hr.java.covidportal.model.Virus;
import hr.java.covidportal.model.Simptom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static hr.java.covidportal.load.Loaders.*;

public class DodavanjeNovogVirusaController {

    @FXML
    private TextField nazivVirusaField;
    @FXML
    ListView<Simptom> simptomiVirusaList;



    @FXML
    public void initialize(){

        //ObservableList<Simptom> listaSimptoma = FXCollections.observableList(ucitajSimptome());
        ObservableList<Simptom> listaSimptoma = null;
        try {
            listaSimptoma = FXCollections.observableList(BazaPodataka.dohvatiSveSimptome());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //simptomiVirusaList.setItems(listaSimptoma);
        simptomiVirusaList.getItems().setAll(listaSimptoma);
        simptomiVirusaList.getSelectionModel().
                setSelectionMode(SelectionMode.MULTIPLE);

    }

    @FXML
    public void dodajVirus(){

        String naziv = nazivVirusaField.getText();
        ObservableList<Simptom> odabraniSimptomi = simptomiVirusaList.
                getSelectionModel().getSelectedItems();
        List<Simptom> listaOdabranihSimptoma = new ArrayList<>();

        for(Simptom s : odabraniSimptomi)
            listaOdabranihSimptoma.add(s);

        Virus v = new Virus(naziv,listaOdabranihSimptoma,PretragaBolestiController.getId());

        //int correct = dodajVirusUDatoteku(v);
        int correct = 1;

        if(naziv.equals(""))
            correct = 0;

        if(correct==1)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert window");
            alert.setHeaderText("Uspjesno dodavanje virusa;");
            alert.setContentText("Vas virus je uspjesno dodan u listu virusa");
            alert.showAndWait();
            //dodajVirusUDatoteku(v);
            try {
                BazaPodataka.spremiNovuBolest(v);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert window");
            alert.setHeaderText("Neuspjesno dodavanje virusa;");
            alert.setContentText("Vas virus NIJE uspjesno dodan u listu virusa");
            alert.showAndWait();
        }

        PretragaVirusaController.dodajVirus(v);

    }

}
