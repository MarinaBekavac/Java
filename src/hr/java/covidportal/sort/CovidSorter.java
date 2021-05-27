package hr.java.covidportal.sort;

import hr.java.covidportal.model.Zupanija;

import java.util.Comparator;

public class CovidSorter implements Comparator<Zupanija>{


    /**
     * Sortira zupanije po postotku zarazenih uzlazno
     *
     * @param zupanija1 prva zupanija
     * @param zupanija2 druga zupanija
     * @return indikator koja zupanija ima veci postotak zarazenih.1 ako zupanija1 ima veci, -1 ako zupanija2 imaveci
     * 0 ako imaju jednak postotak zarazenih
     */
    @Override
    public int compare(Zupanija zupanija1, Zupanija zupanija2) {

        if(zupanija1.getPostotak() > zupanija2.getPostotak())
            return 1;
        else
        {
            if(zupanija1.getPostotak() < zupanija2.getPostotak())
                return -1;
            else
                return 0;
        }

    }
}
