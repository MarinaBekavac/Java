package hr.java.covidportal.genericsi;

import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Osoba;
import hr.java.covidportal.model.Virus;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KlinikaZaInfektivneBolesti<T extends Virus,S extends Osoba> {

    List<T> sviVirusi = new ArrayList<>();
    List<S> sveOsobe = new ArrayList<>();


    /**
     * Instancira listu Virusa i Ososba
     *
     * @param sviVirusi lista virusa
     * @param sveOsobe lista osoba
     */
    public KlinikaZaInfektivneBolesti(List<T> sviVirusi, List<S> sveOsobe) {
        this.sviVirusi = sviVirusi;
        this.sveOsobe = sveOsobe;
    }

    /**
     * Instancira listu Virusa i Ososba
     *
     * @param sviVirusi set virusa
     * @param sveOsobe set osoba
     */
    public KlinikaZaInfektivneBolesti(Set<T> sviVirusi, Set<S> sveOsobe) {
        this.sviVirusi = new ArrayList<>(sviVirusi);
        this.sveOsobe = new ArrayList<>(sveOsobe);
    }

    public List<T> getSviVirusi() {
        return sviVirusi;
    }

    public void setSviVirusi(List<T> sviVirusi) {
        this.sviVirusi = sviVirusi;
    }

    /**
     * Unosi set sviVirusi u private varijablu sviVirusi
     *
     * @param sviVirusi set koji se unosi u nasu listu
     */
    public void setSviVirusi(Set<Bolest> sviVirusi) {
        for(Bolest b : sviVirusi)
            if(b instanceof Virus)
                if(!this.sviVirusi.contains(b))
                    this.sviVirusi.add((T)b);
    }

    public List<S> getSveOsobe() {
        return sveOsobe;
    }

    public void setSveOsobe(List<S> sveOsobe) {
        this.sveOsobe = sveOsobe;
    }

    /**
     * Unosi set sveOsobe u private varijablu sveOsobe
     *
     * @param sveOsobe set koji se unosi u nasu listu
     */
    public void setSveOsobe(Set<S> sveOsobe) {
        for(S o : sveOsobe)
            if(!this.sveOsobe.contains(o))
                this.sveOsobe.add((S)o);
    }

    /**
     * Dodaje virus u listu virusa
     *
     * @param virus virus koji se dodaje u listu
     */
    public void dodajVirus(T virus){
        this.sviVirusi.add(virus);
    }

    /**
     * Dodaje osobu u listu osoba
     *
     * @param osoba osoba koju se dodaje u listu
     */
    public void dodajOsobu(S osoba){
        this.sveOsobe.add(osoba);
    }

    /**
     * Stavlja nazive svih virusa u string
     *
     * @return string koji sadrzi nazive svih virusa
     */
    public String sviVirusiToString(){
        String string = "\n";
        for(T virus : this.sviVirusi)
            string = string + " " + virus.toString();

        return string;
    }

    /**
     * Stavlja sve osobe u string
     *
     * @return string koji sadrzi imena svih  osoba
     */
    public String sveOsobeToString(){
        String string = "\n";
        for(S osoba : this.sveOsobe)
            string = string + " " + osoba.toString();

        return string;
    }

    @Override
    public String toString() {
        return "KlinikaZaInfektivneBolesti --> " + "\n" +
                "sviVirusi= " + this.sviVirusiToString() +
                "\n, sveOsobe= " + this.sveOsobeToString() + "\n";
    }
}
