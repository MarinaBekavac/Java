package hr.java.covidportal.enums;

import java.util.ArrayList;
import java.util.List;

public enum VrijednostSimptoma {

    RIJETKO(1,"RIJETKO"),
    SREDNJE(2,"SREDNJE"),
    CESTO(3,"CESTO");

    Integer broj;
    String vrijednost;


    /**
     * Inicijalizira VrijednostSimptoma
     *
     * @param i broj VrijednostiSimptoma
     * @param s poruka o VrijednostiSimptoma
     */
    VrijednostSimptoma(int i, String s) {
        this.broj = i;
        this.vrijednost = s;
    }

    public Integer getBroj() {
        return broj;
    }

    public String getVrijednost() {
        return vrijednost;
    }


}
