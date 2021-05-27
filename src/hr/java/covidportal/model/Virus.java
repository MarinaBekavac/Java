package hr.java.covidportal.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Virus extends Bolest implements Zarazno, Serializable {

    private String naziv;
    private Long id;
    private Set<Simptom> simptomi;

    /**
     * Inicijalizira podatak o nazivu i simptomima
     *
     * @param naziv podatak o nazivu
     * @param simptomi podatak o listi simptoma
     */
    public Virus(String naziv, Set<Simptom> simptomi,Long id) {
        super(naziv, simptomi,id);
    }

    public Virus(String naziv, List<Simptom> simptomi,Long id)
    {
        super(naziv,new HashSet<>(simptomi),id);
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Provjerava jesu li 2 Virusa jednaka
     *
     * @param o Objekt koji usporedujemo s Virusom
     * @return true if equal false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Virus)) return false;
        if (!super.equals(o)) return false;
        Virus virus = (Virus) o;
        return getNaziv().equals(virus.getNaziv()) &&
                getSimptomi().equals(virus.getSimptomi());
    }

    /**
     * Vraca virus kao string
     *
     * @return virus kao string
     */
    @Override
    public String toString() {
        return "Virus-->" +
                " naziv=" + super.getNaziv() + "\n" ; //+
                /*", simptomi = \n" + super.getSimptomi() + "\n" ;*/
    }

    /**
     * Generira hash code za Virus
     *
     * @return  Integer hash vrijednost
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNaziv(), getSimptomi());
    }

    @Override
    public void prelazakZarazeNaOsobu(Osoba osoba) {
        osoba.setZarazenBolescu(this);
    }

    public String convertSimptomeToStringForController(){

        StringBuilder string = new StringBuilder();

        for(Simptom s : this.getSimptomi())
            string.append(s.getNaziv() + " ");

        return String.valueOf(string);

    }

}
