package hr.java.covidportal.iznimke;

public class BolestIstihSimptoma extends RuntimeException{


    /**
     * Stvara iznimku bez parametara
     */
    public BolestIstihSimptoma() {}

    /**
     * Stvara iznimku sa zadanom porukom
     *
     * @param message poruka o nastaloj iznimci
     */
    public BolestIstihSimptoma(String message) {
        super(message);
    }

    /**
     * Stvara iznimku sa zadanom porukom i uzrocnom iznimkom
     *
     * @param message poruka o nastaloj iznimci
     * @param cause uzrocna iznimka
     */
    public BolestIstihSimptoma(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Stvara iznimku sa zadanom uzrocnom iznimkom
     *
     * @param cause uzrocna iznimka
     */
    public BolestIstihSimptoma(Throwable cause) {
        super(cause);
    }
}
