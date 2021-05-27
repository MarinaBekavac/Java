package main.java.sample;

import hr.java.covidportal.load.BazaPodataka;
import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Simptom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static hr.java.covidportal.load.Loaders.dodajBolestUDatoteku;
import static hr.java.covidportal.load.Loaders.ucitajSimptome;

public class DodavanjeNoveBolestiController {

    @FXML
    private TextField nazivBolestiField;
    @FXML
    ListView<Simptom> simptomiBolestiList;



    @FXML
    public void initialize(){

        //ObservableList<Simptom>  listaSimptoma = FXCollections.observableList(ucitajSimptome());
        ObservableList<Simptom>  listaSimptoma = null;
        try {
            listaSimptoma = FXCollections.
                    observableList(BazaPodataka.dohvatiSveSimptome());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //simptomiBolestiList.setItems(listaSimptoma);
        simptomiBolestiList.getItems().setAll(listaSimptoma);
        simptomiBolestiList.getSelectionModel().
                setSelectionMode(SelectionMode.MULTIPLE);

    }

    @FXML
    public void dodajBolest(){

        String naziv = nazivBolestiField.getText();
        ObservableList<Simptom> odabraniSimptomi = simptomiBolestiList.
                getSelectionModel().getSelectedItems();
        List<Simptom> listaOdabranihSimptoma = new ArrayList<>();

        for(Simptom s : odabraniSimptomi)
            listaOdabranihSimptoma.add(s);

        Bolest b = new Bolest(naziv,listaOdabranihSimptoma,PretragaBolestiController.getId());

        //int correct = dodajBolestUDatoteku(b);
        int correct = 1;

        if(naziv.equals(""))
            correct = 0;

        if(correct==1)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert window");
            alert.setHeaderText("Uspjesno dodavanje bolesti;");
            alert.setContentText("Vasa bolest je uspjesno dodana u listu bolesti");
            alert.showAndWait();
            //dodajBolestUDatoteku(b);
            try {
                BazaPodataka.spremiNovuBolest(b);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert window");
            alert.setHeaderText("Neuspjesno dodavanje bolesti;");
            alert.setContentText("Vasa bolest NIJE uspjesno dodana u listu bolesti");
            alert.showAndWait();
        }

        PretragaBolestiController.dodajBolest(b);

    }

}
