package main.java.sample;

import hr.java.covidportal.load.BazaPodataka;
import hr.java.covidportal.model.Simptom;
import hr.java.covidportal.model.Zupanija;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static hr.java.covidportal.load.Loaders.ucitajSimptome;
import static hr.java.covidportal.main.Glavna.unesiSimptome;


public class PretragaSimptomaController {

    private static final Logger logger = LoggerFactory.getLogger(PretragaSimptomaController.class);

    //private static List<Simptom> simptomi = new ArrayList<>(unesiSimptome());

    private static List<Simptom> simptomi;

    static {
        try {
            simptomi = BazaPodataka.dohvatiSveSimptome();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //private static List<Simptom> simptomi = ucitajSimptome();
    @FXML
    private TextField nazivSimptoma;
    @FXML
    private TableView<Simptom> simptomiTable;
    @FXML
    private TableColumn<Simptom,String> nazivSimptomaColumn;
    @FXML
    private TableColumn<Simptom,String > vrijednostSimptomaColumn;

    public static Long getId(){

        return Long.valueOf(simptomi.size());

    }

    public static void dodajSimptom(Simptom s){

        simptomi.add(s);

    }

    @FXML
    public void pretraziSimptome(){

        //List<Simptom> simptomi = new ArrayList<>(unesiSimptome());
        List<Simptom> filtriraniSimptomi = new ArrayList<>();

        String trazeniSimptom = nazivSimptoma.getText();

        if(trazeniSimptom.equals("")){

            filtriraniSimptomi = this.simptomi;

        }else{

            for(Simptom s : this.simptomi)
                if(s.getNaziv().toLowerCase().contains(trazeniSimptom.toLowerCase()))
                    filtriraniSimptomi.add(s);

        }

        ObservableList<Simptom> listaSimptoma = FXCollections.observableArrayList(filtriraniSimptomi);
        simptomiTable.setItems(listaSimptoma);

    }

    @FXML
    public void initialize(){

        nazivSimptomaColumn.setCellValueFactory(new PropertyValueFactory<Simptom,String>("naziv"));
        vrijednostSimptomaColumn.setCellValueFactory(new PropertyValueFactory<Simptom,String>("vrijednost"));
        //ObservableList<Simptom> listSimptoma = FXCollections.observableArrayList(unesiSimptome());
        ObservableList<Simptom> listSimptoma = FXCollections.observableArrayList(this.simptomi);
        simptomiTable.setItems(listSimptoma);

    }


}
