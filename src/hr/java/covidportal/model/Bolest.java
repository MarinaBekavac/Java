package hr.java.covidportal.model;


import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Bolest extends ImenovaniEntitet implements Serializable {

    private String naziv;
    private Long id;
    private Set<Simptom> simptomi;

    /**
     * Inicijalizira podatak o nazivu i simptomima
     *
     * @param naziv podatak o nazivu
     * @param simptomi podatak o listi simptoma
     */
    public Bolest(String naziv, Set<Simptom> simptomi,Long id) {
        super(naziv,id);
        this.simptomi = simptomi;
    }

    public Bolest(String naziv, List<Simptom> simptomi, Long id) {
        super(naziv,id);
        Set<Simptom> simptomiSet = new HashSet<>(simptomi);
        this.simptomi = simptomiSet;
    }

    public Set<Simptom> getSimptomi() {
        return simptomi;
    }

    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSimptomi(Set<Simptom> simptomi) {
        this.simptomi = simptomi;
    }

    /**
     * Provjerava jesu li 2 Bolesti jednake
     *
     * @param o Objekt koji usporedujemo s Bolesti
     * @return true if equal false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bolest)) return false;
        Bolest bolest = (Bolest) o;
        return getNaziv().equals(bolest.getNaziv()) &&
                getSimptomi().equals(bolest.getSimptomi());
    }

    /**
     * Generira hash code za bolest
     *
     * @return  Integer hash vrijednost
     */
    @Override
    public int hashCode() {
        return Objects.hash(getNaziv(), getSimptomi());
    }

    public String convertSimptomeToStringForController(){

        StringBuilder string = new StringBuilder();

        for(Simptom s : this.getSimptomi())
            string.append(s.getNaziv() + " ");

        return String.valueOf(string);

    }

    @Override
    public String toString() {
        return "Bolest -->" +
                "naziv='" + super.getNaziv() + '\'';
    }
}

