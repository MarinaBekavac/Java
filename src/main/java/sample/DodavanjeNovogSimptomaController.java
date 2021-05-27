package main.java.sample;

import hr.java.covidportal.load.BazaPodataka;
import hr.java.covidportal.model.Simptom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static hr.java.covidportal.load.Loaders.dodajSimptomUDatoteku;

public class DodavanjeNovogSimptomaController{

    @FXML
    private TextField nazivSimptomaField;

    /*
    @FXML
    private String listaVrijednosti[] = {"RIJETKO","SREDNJE","CESTO"};


    private List<String> getListaSimptoma() {

            List<String> lista = new ArrayList<>();
            lista.add(("RIJETKO"));
            lista.add("SREDNJE");
            lista.add("CESTO");

            return lista;
    }*/
    @FXML
    private List<String> listaVrijednosti;

    {
        try {
            listaVrijednosti = BazaPodataka.getVrijednosti();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    ComboBox vrijednostBox;

    @FXML
    public void initialize(){


        vrijednostBox.setPromptText("Vrijednost simptoma");

        ObservableList<String> list = null;
        try {
            list = FXCollections.observableList(BazaPodataka.getVrijednosti());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        vrijednostBox.setItems(list);

    }



    @FXML
    public void spremiSimptom(){

        String naziv = nazivSimptomaField.getText();
        String vrijednost = String.valueOf(vrijednostBox.getValue());
        Long id = PretragaSimptomaController.getId() +1;


        vrijednostBox.setPromptText("Vrijednost simptoma");


        vrijednostBox.getItems().add("SREDNJE");

        Simptom s = new Simptom(naziv,vrijednost,id);
        //int correct = dodajSimptomUDatoteku(s);

        int correct = 1;

        if(naziv.equals(""))
            correct = 0;

        if(correct==1)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert window");
            alert.setHeaderText("Uspjesno dodavanje simptoma;");
            alert.setContentText("Vas simptom je uspjesno dodan u listu simptoma");
            alert.showAndWait();
            //dodajSimptomUDatoteku(s);
            try {
                BazaPodataka.spremiNoviSimptom(new Simptom(naziv,vrijednost,id));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert window");
            alert.setHeaderText("Neuspjesno dodavanje simptoma;");
            alert.setContentText("Vas simptom NIJE uspjesno dodan u listu simptoma");
            alert.showAndWait();
        }

        PretragaSimptomaController.dodajSimptom(s);
        System.out.println(s);

    }

}
