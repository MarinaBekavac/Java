package hr.java.covidportal.model;

public abstract class ImenovaniEntitet {

    private String naziv;

    private Long id;

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    /**podatak o nazivu
     * Inicijalizira
     *
     * @param naziv podatak o nazivu
     */
    public ImenovaniEntitet(String naziv,Long id) {

        this.naziv = naziv;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
