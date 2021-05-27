package main.java.sample;

import hr.java.covidportal.load.BazaPodataka;
import hr.java.covidportal.load.Loaders;
import hr.java.covidportal.main.Glavna;
import hr.java.covidportal.model.Zupanija;
import hr.java.covidportal.niti.NajviseZarazenihNit;
import hr.java.covidportal.sort.CovidSorter;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static hr.java.covidportal.load.Loaders.*;
import static hr.java.covidportal.main.Glavna.unesiZupanije;


public class PretragaZupanijaController {

    private static final Logger logger = LoggerFactory.getLogger(PretragaZupanijaController.class);

    //private static List<Zupanija> zupanije = new ArrayList<>( unesiZupanije());
    //private static List<Zupanija> zupanije = ucitajZupanije();
    public static List<Zupanija> zupanije;

    static {
        try {
            zupanije = BazaPodataka.dohvatiSveZupanije();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    private TextField nazivZupanije;
    @FXML
    private TableView<Zupanija> zupanijeTable;
    @FXML
    private TableColumn<Zupanija,String> nazivZupanijeColumn;
    @FXML
    private TableColumn<Zupanija,Integer> brStanovnikaZupanijeColumn;
    @FXML
    private TableColumn<Zupanija,Integer> brZarazenihZupanijeColumn;

    public static void dodajZupaniju(Zupanija z){

        zupanije.add(z);
        //dodajZupanijuUDatoteku(z);
        //System.out.println(ucitajZupanije());

    }

    public static Long getId(){

        return Long.valueOf(zupanije.size());

    }


    @FXML
    public void pretraziZupanije(){

        //List<Zupanija> zupanije = new ArrayList<>( unesiZupanije());
        List<Zupanija> filtriraneZupanije = new ArrayList<>();

        String trazenaZupanija = nazivZupanije.getText();

        if(trazenaZupanija.equals("")){

            filtriraneZupanije = this.zupanije;

        }else{

            for(Zupanija z : this.zupanije)
                if(z.getNaziv().toLowerCase().contains(trazenaZupanija.toLowerCase()))
                    filtriraneZupanije.add(z);

        }

        ObservableList<Zupanija> listaZupanija = FXCollections.observableArrayList(filtriraneZupanije);
        zupanijeTable.setItems(listaZupanija);
    }

    @FXML
    public void initialize(){
        nazivZupanijeColumn.setCellValueFactory(new PropertyValueFactory<Zupanija,String>("naziv"));
        brStanovnikaZupanijeColumn.setCellValueFactory(new PropertyValueFactory<Zupanija,Integer>("brojStanovnika"));
        brZarazenihZupanijeColumn.setCellValueFactory(new PropertyValueFactory<Zupanija,Integer>("brZarazenihOsoba"));
        //ObservableList<Zupanija> listaZupanija = FXCollections.observableArrayList(unesiZupanije());
        ObservableList<Zupanija> listaZupanija = FXCollections.observableArrayList(this.zupanije);
        zupanijeTable.setItems(listaZupanija);

        NajviseZarazenihNit thread = new NajviseZarazenihNit();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(thread);

    }

}
