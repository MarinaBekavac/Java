package main.java.sample;

import hr.java.covidportal.load.BazaPodataka;
import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Osoba;
import hr.java.covidportal.model.Zupanija;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static hr.java.covidportal.load.Loaders.*;

public class DodavanjeNoveOsobeController {

    @FXML
    private TextField imeField;
    @FXML
    private TextField prezimeField;
    @FXML
    private DatePicker starostField;
    @FXML
    ComboBox<Zupanija> zupanijaBox;
    @FXML
    ComboBox<Bolest> zarazaBox;
    @FXML
    ListView<Osoba> kontaktiList;

    @FXML
    public void initialize(){

        zupanijaBox.setPromptText("Odabrana zupanija");
        //ObservableList<Zupanija> zupanije = FXCollections.observableList(ucitajZupanije());
        ObservableList<Zupanija> zupanije = null;
        try {
            zupanije = FXCollections.observableList(BazaPodataka.dohvatiSveZupanije());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        zupanijaBox.setItems(zupanije);

        zarazaBox.setPromptText("OdabranaZaraza");
        //ObservableList<Bolest> bolesti = FXCollections.observableList(getListuBolestiIVirusa());
        ObservableList<Bolest> bolesti = null;
        try {
            bolesti = FXCollections.observableList(BazaPodataka.dohvatiSveBolesti());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        zarazaBox.setItems(bolesti);

        //ObservableList<Osoba> osobe = FXCollections.observableList(ucitajOsobe());
        ObservableList<Osoba> osobe = null;
        try {
            osobe = FXCollections.observableList(BazaPodataka.dohvatiSveOsobe());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        kontaktiList.getItems().setAll(osobe);
        kontaktiList.getSelectionModel().
                setSelectionMode(SelectionMode.MULTIPLE);

    }

    @FXML
    public void spremiOsobu(){

        String ime = imeField.getText();
        String prezime = prezimeField.getText();
        LocalDate starost = starostField.getValue();
        Zupanija zupanija = zupanijaBox.getValue();
        Bolest zarazenBolescu = zarazaBox.getValue();
        Long id = PretragaOsobaController.getId();

        ObservableList<Osoba> kontakti = kontaktiList.getSelectionModel().getSelectedItems();;

        List<Osoba> kontaktiraneOsobe = new ArrayList<>();

        for(Osoba o : kontakti)
            kontaktiraneOsobe.add(o);

        Osoba o = new Osoba(ime,prezime,zupanija,zarazenBolescu,starost,kontaktiraneOsobe);

        //int correct = dodajOsobuUDatoteku(o);
        int correct = 1;

        if(ime.equals("") || prezime.equals(""))
            correct = 0;

        if(correct==1)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert window");
            alert.setHeaderText("Uspjesno dodavanje osobe;");
            alert.setContentText("Vasa osoba je uspjesno dodana u listu osoba");
            alert.showAndWait();
            //dodajOsobuUDatoteku(o);
            try {
                BazaPodataka.spremiNovuOsobu(o);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert window");
            alert.setHeaderText("Neuspjesno dodavanje osobe;");
            alert.setContentText("Vasa osoba NIJE uspjesno dodana u listu osoba");
            alert.showAndWait();
        }

        PretragaOsobaController.dodajOsobu(o);


    }

}
