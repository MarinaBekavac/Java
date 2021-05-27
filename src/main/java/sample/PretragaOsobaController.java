package main.java.sample;

import hr.java.covidportal.load.BazaPodataka;
import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Osoba;
import hr.java.covidportal.model.Zupanija;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static hr.java.covidportal.load.Loaders.ucitajOsobe;
import static hr.java.covidportal.main.Glavna.*;

public class PretragaOsobaController {

    //private List<Osoba> osobe = new ArrayList<>(unesiOsobe(unesiZupanije(),unesiBolesti(unesiSimptome())));
    //private static  List<Osoba> osobe = ucitajOsobe();
    private static  List<Osoba> osobe;

    static {
        try {
            osobe = BazaPodataka.dohvatiSveOsobe();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    private TextField osobaIme;
    @FXML
    private TextField osobaPrezime;
    @FXML
    private TableView<Osoba> osobeTable;
    @FXML
    private TableColumn<Osoba,String> imeOsobeColumn;
    @FXML
    private TableColumn<Osoba,String> prezimeOsobeColumn;
    @FXML
    private TableColumn<Osoba,String> starostOsobeColumn;
    @FXML
    private TableColumn<Osoba, String> zarazenBolescuColumn;
    @FXML
    private TableColumn<Osoba, String> zupanijaOsobeColumn;

    @FXML
    public void pretragaOsoba(){

        //List<Osoba> osobe = new ArrayList<>(unesiOsobe(unesiZupanije(),unesiBolesti(unesiSimptome())));
        List<Osoba> filtriraneOsobe = new ArrayList<>();

        String trazenaOsobaIme = osobaIme.getText();
        String trazenaOsobaPrezime = osobaPrezime.getText();

        if(trazenaOsobaIme.equals("") && trazenaOsobaPrezime.equals("")){

            filtriraneOsobe = this.osobe;

        }else{

            for(Osoba o : this.osobe)
                {
                    if(!trazenaOsobaIme.equals("") && !trazenaOsobaPrezime.equals(""))
                        if(o.getIme().toLowerCase().contains(trazenaOsobaIme.toLowerCase()) &&
                                o.getPrezime().toLowerCase().contains(trazenaOsobaPrezime.toLowerCase()))
                        {
                            filtriraneOsobe.add(o);
                            break;
                        }

                    if(o.getIme().toLowerCase().contains(trazenaOsobaIme.toLowerCase()) && !trazenaOsobaIme.equals(""))
                        filtriraneOsobe.add(o);
                    if(o.getPrezime().toLowerCase().contains(trazenaOsobaPrezime.toLowerCase()) && !trazenaOsobaPrezime.equals(""))
                        if(!filtriraneOsobe.contains(o))
                            filtriraneOsobe.add(o);
                }



        }

        ObservableList<Osoba> listaOsoba = FXCollections.observableArrayList(filtriraneOsobe);
        osobeTable.setItems(listaOsoba);

    }

    public static void dodajOsobu(Osoba o){

        osobe.add(o);

    }

    public static Long getId(){

        return Long.valueOf(osobe.size());

    }

    @FXML
    public void initialize(){

        imeOsobeColumn.setCellValueFactory(new PropertyValueFactory<Osoba,String>("ime"));
        prezimeOsobeColumn.setCellValueFactory(new PropertyValueFactory<Osoba,String>("prezime"));
        starostOsobeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Osoba, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Osoba, String> osoba) {
                return new SimpleStringProperty(osoba.getValue().getStarostDate().toString());
            }
        });
        zupanijaOsobeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Osoba, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Osoba, String> osoba) {
                return  new SimpleStringProperty(osoba.getValue().getZupanija().getNaziv());
            }
        });
        zarazenBolescuColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Osoba, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Osoba, String> osoba) {
                    return new SimpleStringProperty(osoba.getValue().getZarazenBolescu().getNaziv());
            }
        });
        ObservableList<Osoba> listaOsoba = FXCollections.observableArrayList(this.osobe);
        osobeTable.setItems(listaOsoba);

    }


}
