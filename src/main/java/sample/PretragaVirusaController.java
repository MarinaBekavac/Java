package main.java.sample;

import hr.java.covidportal.load.BazaPodataka;
import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Virus;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static hr.java.covidportal.load.Loaders.ucitajViruse;
import static hr.java.covidportal.main.Glavna.unesiBolesti;
import static hr.java.covidportal.main.Glavna.unesiSimptome;

public class PretragaVirusaController {

    //private static List<Bolest> bolesti = new ArrayList<>(unesiBolesti(unesiSimptome()));
    private static List<Bolest> bolesti;

    static {
        try {
            bolesti = BazaPodataka.dohvatiSveBolesti();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //private static List<Virus> virusi = ucitajViruse();
    private static List<Virus> virusi = BazaPodataka.dohvatiViruse();

    @FXML
    private TextField nazivVirusa;
    @FXML
    private TableView<Virus> virusiTable;
    @FXML
    private TableColumn<Virus,String> nazivVirusaColumn;
    @FXML
    private TableColumn<Virus,String > brojSimptomaVirusaColumn;

    @FXML
    public void pretraziViruse(){


         List<Virus> filtriraniVirusi = new ArrayList<>();

        String trazeniVirus = nazivVirusa.getText();

        if(trazeniVirus.equals("")){

            filtriraniVirusi = virusi;

        }else{

            for(Virus v : virusi)
                if(v.getNaziv().toLowerCase().contains(trazeniVirus.toLowerCase()))
                    filtriraniVirusi.add(v);

        }

        ObservableList<Virus> listaVirusa = FXCollections.observableArrayList(filtriraniVirusi);
        virusiTable.setItems(listaVirusa);

    }

    public static void dodajVirus(Virus v){

        virusi.add(v);

    }

    public static Long getId(){

        return Long.valueOf(virusi.size());

    }

    @FXML
    public void initialize(){

        nazivVirusaColumn.setCellValueFactory(new PropertyValueFactory<Virus,String>("naziv"));
        brojSimptomaVirusaColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Virus, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Virus, String> virus) {
                return new SimpleStringProperty(virus.getValue().convertSimptomeToStringForController());
                // SimpleStringProperty(String.valueOf(virus.getValue().getSimptomi().size()));
            }
        });


        ObservableList<Virus> listaVirusa = FXCollections.observableArrayList(virusi);
        virusiTable.setItems(listaVirusa);

    }

}
