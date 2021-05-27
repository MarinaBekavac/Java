package hr.java.covidportal.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Objects;

public class Zupanija extends ImenovaniEntitet implements Serializable {

    private String naziv;
    private Long id;
    private Integer brojStanovnika;
    private Integer brZarazenihOsoba;

    /**
     * Inicijalizira podatak o nazivu i broju stanovnika
     *
     * @param naziv podatak o nazivu
     * @param brojStanovnika podatak o broju stanovnika
     * @param brZarazenihOsoba podatak o broju zarazenih osoba u zupaniji
     */
    public Zupanija(String naziv, Integer brojStanovnika,Integer brZarazenihOsoba,Long id) {
        super(naziv,id);
        this.brojStanovnika = brojStanovnika;
        this.brZarazenihOsoba = brZarazenihOsoba;
    }

    /**
     * Provjerava jesu li 2 Zupanije jednake
     *
     * @param o Objekt koji usporedujemo s Zupanijom
     * @return true if equal false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zupanija)) return false;
        Zupanija zupanija = (Zupanija) o;
        return getNaziv().equals(zupanija.getNaziv()) &&
                getBrojStanovnika().equals(zupanija.getBrojStanovnika()) &&
                getBrZarazenihOsoba().equals(zupanija.getBrZarazenihOsoba());
    }


    /**
     * Generira hash code za Zupaniju
     *
     * @return  Integer hash vrijednost
     */
    @Override
    public int hashCode() {
        return Objects.hash(getNaziv(), getBrojStanovnika(), getBrZarazenihOsoba());
    }

    @Override
    public String getNaziv() {
        return super.getNaziv();
    }

    @Override
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }


    public Integer getBrZarazenihOsoba() {
        return brZarazenihOsoba;
    }

    public void setBrZarazenihOsoba(Integer brZarazenihOsoba) {
        this.brZarazenihOsoba = brZarazenihOsoba;
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
     * Ispisuje zupaniju u formatu naziv broj stanovnika
     */
    public void ispisiZupaniju(){

        System.out.println( super.getNaziv() + " " + this.brojStanovnika);

    }

    /**
     * Ispisuje zupaniju u formatu naziv postotak zarazenih
     */
    public void ispisiZupanijuSPostotkomZarazenih(){

        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(4);

        System.out.println( super.getNaziv() + " " + this.getPostotak() + "%");

    }


    /**
     * Racuna postotak zarazenih u zupaniji
     *
     * @return broj od 0 do 1 koji oznacava postotak zarazenih u zupaniji
     */
    public float getPostotak(){

        float postotak = (float)this.getBrZarazenihOsoba()/this.getBrojStanovnika();

        return postotak*100;

    }

    @Override
    public String toString() {
        return "Id=" + getId() + " " +
                "naziv='" + super.getNaziv() + " " +
                ", brojStanovnika=" + brojStanovnika +
                ", brZarazenihOsoba=" + brZarazenihOsoba +
                "\n";
    }
}

