package hr.java.covidportal.iznimke;

public class DuplikatKontaktiraneOsobe extends Exception{

    /**
     * Stvara iznimku sa zadanom porukom
     *
     * @param message poruka o nastaloj iznimci
     */
    public DuplikatKontaktiraneOsobe(String message){
        super(message);
    }

    /**
     * Stvara iznimku sa zadanom uzrocnom iznimkom
     *
     * @param cause uzrocna iznimka
     */
    public DuplikatKontaktiraneOsobe(Throwable cause){
        super(cause);
    }

    /**
     * Stvara iznimku sa zadanom porukom i uzrocnom iznimkom
     *
     * @param message poruka o nastaloj iznimci
     * @param cause uzrocna iznimka
     */
    public DuplikatKontaktiraneOsobe(String message,Throwable cause){
        super(message,cause);
    }

}
