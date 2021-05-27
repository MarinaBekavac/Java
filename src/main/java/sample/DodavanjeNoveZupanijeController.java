package main.java.sample;

import hr.java.covidportal.load.BazaPodataka;
import hr.java.covidportal.model.Zupanija;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.SQLException;

import static hr.java.covidportal.load.Loaders.dodajZupanijuUDatoteku;

public class DodavanjeNoveZupanijeController {

    @FXML
    private TextField nazivZupanijeField;
    @FXML
    private TextField brStanovnikaField;
    @FXML
    private TextField brZarazenihStanovnikaField;

    @FXML
    public void spremiZupaniju(){

        String naziv = nazivZupanijeField.getText();
        Integer brStanovnika = Integer.valueOf(brStanovnikaField.getText());
        Integer brZarazenihStanovnika = Integer.valueOf(brZarazenihStanovnikaField.getText());
        Long id = PretragaZupanijaController.getId()+1;
        Zupanija z = new Zupanija(naziv,brStanovnika,brZarazenihStanovnika,id);

        PretragaZupanijaController.dodajZupaniju(new Zupanija(naziv,brStanovnika,brZarazenihStanovnika,id));
        //int correct = dodajZupanijuUDatoteku(z);

        int correct = 1;

        if(naziv.equals("") || brStanovnika<0 || brZarazenihStanovnika>brStanovnika)
            correct = 0;

        if(correct==1){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert window");
            alert.setHeaderText("Uspjesno dodavanje zupanije;");
            alert.setContentText("Vasa zupanije je uspjesno dodana u listu zupanija");
            alert.showAndWait();
            //dodajZupanijuUDatoteku(z);
            try {
                BazaPodataka.spremiNovuZupaniju(z);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }else{

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert window");
            alert.setHeaderText("Neuspjesno dodavanje zupanije;");
            alert.setContentText("Vasa zupanije NIJE uspjesno dodana u listu zupanija");
            alert.showAndWait();

        }



    }

}
