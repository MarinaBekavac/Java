package main.java.sample;

import hr.java.covidportal.load.BazaPodataka;
import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Virus;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static hr.java.covidportal.load.Loaders.ucitajBolesti;
import static hr.java.covidportal.main.Glavna.unesiBolesti;
import static hr.java.covidportal.main.Glavna.unesiSimptome;

public class PretragaBolestiController {

    //private List<Bolest> bolesti = new ArrayList<>(unesiBolesti(unesiSimptome()));
    //private static List<Bolest> bolesti = ucitajBolesti();
    private static List<Bolest> bolesti= BazaPodataka.dohvatiBolesti();


    @FXML
    private TextField nazivBolesti;
    @FXML
    private TableView<Bolest> bolestiTable;
    @FXML
    private TableColumn<Bolest,String> nazivBolestiColumn;
    @FXML
    private TableColumn<Bolest,String> simptomiBolestiColumn;

    @FXML
    public void pretragaBolesti(){

        /*
        List<Bolest> bolestiBezVirusa = new ArrayList<>();
        for(Bolest b : this.bolesti)
            if(!(b instanceof Virus))
                bolestiBezVirusa.add(b);

            List<Bolest> filtriraneBolesti = new ArrayList<>();

        String trazenaBolest =  nazivBolesti.getText();

        if(trazenaBolest.equals("")){

            filtriraneBolesti = bolestiBezVirusa;

        }else{

            for(Bolest b : bolestiBezVirusa)
                if(b.getNaziv().toLowerCase().contains(trazenaBolest.toLowerCase()))
                    filtriraneBolesti.add(b);

        }

         */
        //////////////////////

        String trazenaBolest =  nazivBolesti.getText();
        List<Bolest> filtriraneBolesti = new ArrayList<>();

        if(trazenaBolest.equals("")){

            filtriraneBolesti = bolesti;

        }else {

            for (Bolest b : bolesti)
                if (b.getNaziv().toLowerCase().contains(trazenaBolest.toLowerCase()))
                    filtriraneBolesti.add(b);
        }

        ////////////////////////

        ObservableList<Bolest> listaBolesti = FXCollections.observableArrayList(filtriraneBolesti);
        bolestiTable.setItems(listaBolesti);

    }

    public static void dodajBolest(Bolest b){

        bolesti.add(b);

    }

    public static Long getId(){

        return Long.valueOf(bolesti.size());

    }

    @FXML
    public void initialize(){

        nazivBolestiColumn.setCellValueFactory(new PropertyValueFactory<Bolest,String>("naziv"));
        simptomiBolestiColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Bolest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Bolest, String> bolest) {
                return new SimpleStringProperty(bolest.getValue().convertSimptomeToStringForController());
            }
        });
/*
        List<Bolest> bolestiBezVirusa = new ArrayList<>();
        for(Bolest b : this.bolesti)
            if(!(b instanceof Virus))
                bolestiBezVirusa.add(b);

 */

        ObservableList<Bolest> listaBolesti = FXCollections.observableArrayList(bolesti);
        bolestiTable.setItems(listaBolesti);

    }

}
