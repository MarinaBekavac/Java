package hr.java.covidportal.model;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

public class Osoba implements Serializable {

    private String ime;
    private Long id;
    private String prezime;
    private Integer starost;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private List<Osoba> kontaktiraneOsobe;
    private LocalDate datumRodenja;
    private Integer starostDate;

    public Osoba(String ime, Long id, String prezime, Zupanija zupanija, Bolest zarazenBolescu,
                 LocalDate datumRodenja) {
        this.ime = ime;
        this.id = id;
        this.prezime = prezime;
        this.zupanija = zupanija;
        this.zarazenBolescu = zarazenBolescu;
        //this.kontaktiraneOsobe = null;
        this.datumRodenja = datumRodenja;
        this.starostDate = Period.between(datumRodenja,LocalDate.now()).getYears();
    }

    public Osoba(String ime, String prezime, Zupanija zupanija, Bolest zarazenBolescu,
                 LocalDate datumRodenja,List<Osoba> kontaktiraneOsobe) {
        this.ime = ime;
        this.prezime = prezime;
        this.zupanija = zupanija;
        this.kontaktiraneOsobe = kontaktiraneOsobe;
        this.zarazenBolescu = zarazenBolescu;
        this.datumRodenja = datumRodenja;
        this.starostDate = Period.between(datumRodenja,LocalDate.now()).getYears();
    }

    public Osoba(String ime, Long id, String prezime, Zupanija zupanija, Bolest zarazenBolescu,
                 List<Osoba> kontaktiraneOsobe, LocalDate datumRodenja) {
        this.ime = ime;
        this.id = id;
        this.prezime = prezime;
        this.zupanija = zupanija;
        this.zarazenBolescu = zarazenBolescu;
        this.kontaktiraneOsobe = kontaktiraneOsobe;
        this.datumRodenja = datumRodenja;
        this.starostDate = Period.between(datumRodenja,LocalDate.now()).getYears();
    }

    public Osoba(String ime, String prezime, Integer starost, Zupanija zupanija, Bolest zarazenBolescu,
                 List<Osoba> kontaktiraneOsobe, Long id) {
        this.ime = ime;
        this.id = id;
        this.prezime = prezime;
        this.starost = starost;
        this.zupanija = zupanija;
        this.zarazenBolescu = zarazenBolescu;
        this.kontaktiraneOsobe = kontaktiraneOsobe;


        if(kontaktiraneOsobe != null)
            if(zarazenBolescu instanceof Virus virus)
                for(int i=0;i<kontaktiraneOsobe.size();i++)
                    kontaktiraneOsobe.get(i).setZarazenBolescu(virus);
    }

    /**
     * Inuicijalizira podatak o imenu,prezimenu,starosti,zupaniji,bolesti kojom je zarazen te listu kontaktiranih osoba
     *
     * @param ime podatak o imenu
     * @param prezime podatak o prezimenu
     * @param starost podatak o starosti
     * @param zupanija podatak o zupaniji
     * @param zarazenBolescu podatak o bolesti s kojom je osoba zarazena
     * @param kontaktiraneOsobe podatak o listi kontakata osobe
     */
    public Osoba(String ime, String prezime, Integer starost, Zupanija zupanija, Bolest zarazenBolescu, List<Osoba> kontaktiraneOsobe) {
        this.ime = ime;
        this.prezime = prezime;
        this.starost = starost;
        this.zupanija = zupanija;
        this.zarazenBolescu = zarazenBolescu;
        this.kontaktiraneOsobe = kontaktiraneOsobe;

        if(kontaktiraneOsobe != null)
            if(zarazenBolescu instanceof Virus virus)
                for(int i=0;i<kontaktiraneOsobe.size();i++)
                    kontaktiraneOsobe.get(i).setZarazenBolescu(virus);


    }

    /**
     * Provjerava jesu li 2 Osobe jednake
     *
     * @param o Objekt koji usporedujemo s Osobom
     * @return true if equal false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Osoba)) return false;
        Osoba osoba = (Osoba) o;
        return getIme().equals(osoba.getIme()) &&
                getPrezime().equals(osoba.getPrezime()) &&
                getStarost().equals(osoba.getStarost()) &&
                getZupanija().equals(osoba.getZupanija()) &&
                getZarazenBolescu().equals(osoba.getZarazenBolescu()) &&
                getKontaktiraneOsobe().equals(osoba.getKontaktiraneOsobe());
    }

    /**
     * Generira hash code za Osobu
     *
     * @return  Integer hash vrijednost
     */
    @Override
    public int hashCode() {
        return Objects.hash(getIme(), getPrezime(), getStarost(), getZupanija(), getZarazenBolescu(), getKontaktiraneOsobe());
    }

    public static class Builder{
        private String ime;
        private String prezime;
        private Integer starost;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private List<Osoba> kontaktiraneOsobe;

        public Builder(){}

        public Builder setIme(String ime){
            this.ime = ime;
            return this;
        }

        public Builder setPrezime(String prezime){
            this.prezime = prezime;
            return this;
        }

        public Builder setStarost(Integer starost){
            this.starost = starost;
            return this;
        }

        public Builder setZupanija(Zupanija zupanija){
            this.zupanija = zupanija;
            return this;
        }

        public Builder setZarazenBolescu(Bolest bolest){
            this.zarazenBolescu = bolest;
            return this;
        }

        public Builder setKontaktiraneOsobe(List<Osoba> osobe){
            this.kontaktiraneOsobe = osobe;
            return this;
        }

        public Osoba build(){
            return new Osoba(ime,prezime,starost,zupanija,zarazenBolescu,kontaktiraneOsobe);
        }
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Integer getStarost() {
        return starost;
    }

    public void setStarost(Integer starost) {
        this.starost = starost;
    }

    public Zupanija getZupanija() {
        return zupanija;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    public Bolest getZarazenBolescu() {
        return zarazenBolescu;
    }

    public void setZarazenBolescu(Bolest zarazenBolescu) {
        this.zarazenBolescu = zarazenBolescu;
    }

    public List<Osoba> getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDatumRodenja() {
        return datumRodenja;
    }

    public void setDatumRodenja(LocalDate datumRodenja) {
        this.datumRodenja = datumRodenja;
    }

    public Integer getStarostDate() {
        return starostDate;
    }

    public void setStarostDate(Integer starostDate) {
        this.starostDate = starostDate;
    }

    /**
     * Ispisuje osobu
     */
    public void ispisiOsobu(){
        if(this.ime!= null && this.prezime!= null)
            System.out.println("Ime i prezime:" + this.ime + " " + this.prezime);
        if(this.starost != null)
            System.out.println("Starost:" + this.starost);
        if(this.zupanija != null)
            System.out.println("Županija prebivališta:" + this.zupanija.getNaziv() );
        if(this.zarazenBolescu != null)
            System.out.println("Zaražen bolešću:" + this.zarazenBolescu.getNaziv());
        System.out.println("Kontaktirane osobe:");
        this.ispisiKontakte();
    }

    /**
     * Ispisuije kontakte osobe
     */
    private void ispisiKontakte() {
        if(this.kontaktiraneOsobe == null)
            System.out.println("Nema kontaktiranih osoba");
        else{
            for(Osoba osoba : this.kontaktiraneOsobe)
                System.out.println(osoba.getIme() + " " + osoba.getPrezime());
        }
        //malo linija cisto da vidim sta je di na ekranu
        System.out.println("-------------------------------------------");
    }

    /**
     * Ispisuje osobu samo kao ime i prezime
     */
    public void ispisiOsobuOsnovno(){
        if(this == null)
            System.out.println("Osoba je null");
        else
            System.out.println(this.ime + " " + this.prezime);
    }

    @Override
    public String toString() {
        String str = "id " + id + ' ' +
                "ime=" + ime + ' ' +
                ", prezime=" + prezime + ' ' +
               /* ", starost=" + starostDate +
                ", zupanija=" + zupanija.getNaziv() +
                " id=" + id +
                "    zarazen " + zarazenBolescu + " kontakti:" +*/
                 "\n" ;
       /* if(this.kontaktiraneOsobe!=null)
            for(Osoba o: kontaktiraneOsobe)
                str  = str + o.getIme() + " " + o.getPrezime() + "\n";

        if(kontaktiraneOsobe.size()==0)
            str = str + " Nema kontakata" + "\n";*/

        return str;

    }
}

