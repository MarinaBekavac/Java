package hr.java.covidportal.model;

import java.io.Serializable;
import java.util.Objects;

public class Simptom extends ImenovaniEntitet implements Serializable {

    private String naziv;
    private Long id;
    private String vrijednost;

    /**
     * Inicijalizira podatak o nazivu i vrijednosti
     *
     * @param naziv podatak o nazivu
     * @param vrijednost podatak o vrijednosti(Bolest ili virus)
     */
    public Simptom(String naziv, String vrijednost,Long id) {
        super(naziv,id);
        this.vrijednost = vrijednost;
    }


    /**
     * Provjerava jesu li 2 Simptoma jednaka
     *
     * @param o Objekt koji usporedujemo s Simptomom
     * @return true if equal false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Simptom)) return false;
        Simptom simptom = (Simptom) o;
        return getNaziv().equals(simptom.getNaziv()) &&
                getVrijednost().equals(simptom.getVrijednost());
    }

    /**
     * Vraca Simptom kao string
     *
     * @return Simptom kao string
     */
    @Override
    public String toString() {
        return "Simptom  --> " +
                "naziv='" + super.getNaziv() + '\'' +
                ", vrijednost='" + this.vrijednost + '\'' +
                '\n';
    }

    /**
     * Generira hash code za Simptom
     *
     * @return  Integer hash vrijednost
     */
    @Override
    public int hashCode() {
        return Objects.hash(getNaziv(), getVrijednost());
    }

    public String getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Ispisuje simptom u formatu naziv vrijednost
     */
    public void ispisiSimptom(){
        System.out.println(super.getNaziv() + " " + this.vrijednost);
    }
}
