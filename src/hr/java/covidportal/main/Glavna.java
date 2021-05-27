package hr.java.covidportal.main;


import hr.java.covidportal.enums.VrijednostSimptoma;
import hr.java.covidportal.genericsi.KlinikaZaInfektivneBolesti;
import hr.java.covidportal.iznimke.BolestIstihSimptoma;
import hr.java.covidportal.iznimke.DuplikatKontaktiraneOsobe;
import hr.java.covidportal.model.*;
import hr.java.covidportal.sort.CovidSorter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class Glavna {

    public static final Logger logger = LoggerFactory.getLogger(Glavna.class);


    /**
     * Osigurava unos Integera
     *
     * @param in Scanner za unos podataka
     * @param poruka poruka za ispisati prilikom unosa
     * @return uneseni Integer
     */
    public static Integer unosInta(Scanner in, String poruka){
        //inicijaliziramo uneseniInt da ne dobijemo error variable might
        //not be initialized kad probamo returnat
        Integer uneseniInt = 0;
        boolean isInt = false;

        while(!isInt)
        {
            try{
                System.out.print(poruka);
                uneseniInt = in.nextInt();
                in.nextLine();
                isInt = true;
            }catch (InputMismatchException ex){
                logger.info("Nije unesen int.",ex);
                System.out.println("Pogreska u formatu podataka, moolimo ponovite unos!");
                in.nextLine();
            }
        }
        return uneseniInt;
    }

    /**
     * Unosi listu osoba
     *
     * @param in Scanner za unos podataka
     * @param brOsoba broj osoba koje treba unjeti
     * @param zupanije spisak zupanija s kojeg se bira zupanija osobe koju unosimo
     * @param bolesti spisak bolesti s kojeg se bira bolest osobe koju unosimo
     * @return lista osoba
     */
    public static List<Osoba> unosOsoba(Scanner in, Integer brOsoba, Set<Zupanija> zupanije, Set<Bolest> bolesti){
        List<Osoba> osobe = new ArrayList<>();
        String ime;
        String prezime;
        Zupanija zupanija;
        Bolest bolest;

        //inicijaliziram dob jer error variable might not be initialized
        //zbog while petlje
        Integer dob = 0;

        logger.info("Zapoceo unos osoba.");
        for(int i=0; i<brOsoba; i++)
        {
            //osoba moze imati kontakt samo s vec unesenim osobama
            //dakle za osobu 3 mozemo odabrati max 2 osobe
            //jer krecemo brojati ob 0 duzina liste je i a ne i-1
            List<Osoba> listaKontakata;

            int j=i+1;
            System.out.print("Unesite ime " + j + ". osobe:");
            ime = in.nextLine();
            System.out.print("Unesite prezime osobe:");
            prezime = in.nextLine();

            boolean isValid = false;

            while (!isValid)
            {
                dob = unosInta(in,"Unesite starost osobe:");

                //Najdugovječnija osoba dosad je Francuskinja Jeanne Calment koja je živjela 122 godine i 164 dana.
                //Takoder nema smisla da netko im -30 godina
                if(dob > 0 && dob <= 122)
                    isValid = true;
                else{
                    logger.info("Unesena dob za osobu nije dobra.");
                    System.out.println("Molim unesite starost koja ima smisla.Nitko nije vampir i nitko ne moze imati negativan broj godina.");
                }
            }

            zupanija = odabirZupanije(in,zupanije);

            bolest = odabirBolesti(in,bolesti);

            //unos kontakata(osim za prvu osobu)
            if(i!=0)
            {

                    listaKontakata = odabirKontakata(in, osobe);
                    osobe.add(new Osoba(ime,prezime,dob,zupanija,bolest,listaKontakata));

            }
            else
            {
                //listaKontakata = null;
                osobe.add(new Osoba(ime,prezime,dob,zupanija,bolest,null));
            }


        }
        return  osobe;
    }

    /**
     * Usporeduje liste simptoma
     *
     * @param simptomi1 prva lista
     * @param simptomi2 druga lista
     * @return true ako su liste jednake false inace
     */
    public static boolean UsporediListeSimptoma(Set<Simptom> simptomi1,Set<Simptom> simptomi2){

        for(Simptom simptom1 : simptomi1)
            for(Simptom simptom2 : simptomi2)
                if(!simptom1.equals(simptom2))
                    return false;


        return true;
    }


    /**
     * Provjerava postoji li zaraza na spisku zaraza.Bolesti/virusi se usporeduju
     * po spisku simptoma
     *
     * @param zaraza podatak koji se usporeduje s postojecom listom zaraza
     * @param zaraze postojeca lista zaraza
     * @throws BolestIstihSimptoma iznimka tipa <code>BolestIstihSimptoma</code>
     */
    public static void ProvjeraDuplikataBolesti(Bolest zaraza,Set<Bolest> zaraze) throws BolestIstihSimptoma {

        if(zaraze != null)
            for(Bolest obj : zaraze)
                if(obj != null)
                {
                    if(UsporediListeSimptoma(zaraza.getSimptomi(),obj.getSimptomi()))
                        throw new BolestIstihSimptoma("Unos dvije iste bolesti/virusa");

                }

    }

    /**
     * Provjerava je li osoba u listi osoba osobe
     *
     * @param osoba podatak koji se trazi u listi postojecih ososa
     * @param osobe lista postojecih osoba
     * @throws DuplikatKontaktiraneOsobe iznimka tipa <code>DuplikatKontaktiraneOsobe</code>
     */
    public static void ProvjeraDuplikataKontakta(Osoba osoba,List<Osoba> osobe) throws DuplikatKontaktiraneOsobe{

        if(osobe != null)
            for(Osoba obj : osobe)
                if(obj != null)
                    if(obj.equals(osoba))
                        throw new DuplikatKontaktiraneOsobe("Unos dvije iste osobe");


    }

    /**
     * Stvara listu kontakata sa liste osoba bez duplikata
     *
     * @param in Scanner za unos podataka
     * @param osobe Lista osoba s kojima je moguce biti u kontaktu
     * @return lista kontakata
     */
    public static List<Osoba> odabirKontakata(Scanner in,List<Osoba> osobe)  {

        //inicijalizacij zbog error variable might not be initialized
        Integer brojKontakata=0;

        System.out.println("Unesite broj osoba koje su bile u kontaktu s tom osobom(max " + osobe.size() + "):");

        boolean isValid = false;

        while (!isValid)
        {
            brojKontakata = unosInta(in,"Odabir: ");
            if(brojKontakata >= 0 && brojKontakata <= /*brojNeNullOsoba*/osobe.size())
            {
                isValid = true;
            }
            else{
                logger.info("Unesen naispravan broj kontakata.");
                System.out.println("Unjeli ste neispravan broj kontakata.Try again.");
            }
        }

        List<Osoba> listaKontakata = new ArrayList<>();
        Integer indexKontakta = 0;

        for(int i=0; i< brojKontakata; i++)
        {
            boolean isOriginal = false;

            while(!isOriginal)
            {
                try{
                    int j =i+1;
                    System.out.println("Odaberite " + j + " osobu:");
                    ispisiOsobe(osobe);

                    //odma inicijaliziramo indexKontakta jer fu**ing error variable might not be initialized
                    indexKontakta = 0;
                    boolean isInRange = false;

                    while (!isInRange)
                    {
                        indexKontakta = unosInta(in,"Odabir: ");
                        indexKontakta -= 1;
                        if(indexKontakta >= 0 && indexKontakta < osobe.size())
                        {
                            isInRange = true;
                        }
                        else{
                            logger.info("Unesen krivi index kontakta(vjerojatno out of range)");
                            System.out.println("Krivi index kontakta.Try again.");
                        }
                    }

                    ProvjeraDuplikataKontakta(osobe.get(indexKontakta),listaKontakata);
                    isOriginal = true;

                }catch (DuplikatKontaktiraneOsobe ex){
                    logger.info("Unesena osoba koja vec postoji na listi kontakata",ex);
                    System.out.println("Odabrana osoba se već nalazi među kontaktiranim osobama.Molimo Vas da odaberete " +
                            "neku drugu osobu.");
                }
            }

            listaKontakata.add(osobe.get(indexKontakta));
        }
        return listaKontakata;
    }

    /**
     * Ispisuje listu Osoba s kontaktima
     *
     * @param osobe lista osoba koju treba ispisati
     */
    public static void ispisiListuOsoba(List<Osoba> osobe){
        System.out.println("*******************************************");
        System.out.println("Popis osoba:");
        for(Osoba osoba : osobe)
        {
            osoba.ispisiOsobu();
        }
    }


    /**
     * Ispisuje listu osoba bez kontakata
     *
     * @param osobe lista osoba koju treba ispisati
     */
    public static void ispisiOsobe(List<Osoba> osobe){
        int i=1;
        for(Osoba osoba: osobe)
        {
            //jer je osoba[] array ima max_osoba elemenata dakle ima null elemenata
            //zato moramo provjeravati je li osoba == null
            if(osoba != null)
            {
                System.out.print(i + ". ");
                osoba.ispisiOsobuOsnovno();
            }
            i++;
        }
    }

    /**
     * Odabire bolest sa spiska bolesti
     *
     * @param in Scanner za unos podataka
     * @param bolestiSet lista za izbor bolesti
     * @return odabrana bolest
     */
    public static Bolest odabirBolesti(Scanner in,Set<Bolest>  bolestiSet){
        //inicijaliziram odabranaBolest jer inace dobijemo
        //error variable might not be initialized

        List<Bolest> bolesti = new ArrayList<>(bolestiSet);
        //Bolest odabranaBolest = getBolestByIndex(0,bolesti);
        Bolest odabranaBolest = bolesti.get(0);

        Integer index;

        System.out.println("Unesite bolest osobe:");
        ispisiBolestiBezSimptoma(bolesti);

        boolean isValid = false;

        while (!isValid)
        {
            index = unosInta(in,"Odabir: ");
            index -= 1;
            if(index >=0 && index < bolesti.size())
            {
                //odabranaBolest = getBolestByIndex(index,bolesti);7
                odabranaBolest = bolesti.get(index);
                isValid = true;
            }
            else{
                logger.info("Neispravan unos indexa bolesti prilikom odabira bolesti.");
                System.out.println("Neispravan unos, molimo pokusajte ponovno!");
            }
        }
        return odabranaBolest;
    }

    /**
     * Odabire zupaniju ss spiska zupanija
     *
     * @param in Scanner za unos podataka
     * @param zupanijeSet set zupanija za izbor zupanije
     * @return odabrana zupanija
     */
    public static Zupanija odabirZupanije(Scanner in,Set<Zupanija> zupanijeSet){
        //inicijaliziram odabranaZupanije jer inace dobijem error
        //variable might not be initialized
        List<Zupanija> zupanije = new ArrayList<>(zupanijeSet);

        //Zupanija odabranaZupanija = getZupanijaByIndex(0,zupanije);
        Zupanija odabranaZupanija = zupanije.get(0);
        Integer index;

        System.out.println("Unesite županiju prebivališta osobe:");
        ispisiZupanije(zupanije);

        boolean isValid = false;

        while (!isValid)
        {

            index = unosInta(in,"Odabir: ");
            index -= 1;

            if(index >=0 && index < zupanije.size())
            {

                //odabranaZupanija = getZupanijaByIndex(index,zupanije);
                odabranaZupanija = zupanije.get(index);
                isValid = true;
            }
            else{
                logger.info("Neispravan unos indexa zupanije prilikom odabira zupanije.");
                System.out.println("Neispravan unos, molimo pokusajte ponovno!");
            }
        }

        return  odabranaZupanija;
    }

    /**
     * Unosi brBolseti bolesti i njihove simptome.
     *
     * @param in Scanner za unos podataka
     * @param brBolesti broj bolesti koji treba unjeti
     * @param postojeciSimptomiSet lista za odabir simptoma za bolest koja se unosi
     * @return vraca listu bolesti
     */
    public static Set<Bolest> unosBolesti(Scanner in, Integer brBolesti,Set<Simptom> postojeciSimptomiSet) {
        Set<Bolest> bolesti = new HashSet<>();
        String naziv;

        List<Simptom> postojeciSimptomi = new ArrayList<>(postojeciSimptomiSet);

        //inicijalizacija brSimptoma da izbjegnemo error value might not get initialize
        Integer brSimptoma =0;
        Set<Simptom> simptomi;

        System.out.println("Unesite podatke o " + brBolesti + " bolesti ili virusa:");

        for(int i=0; i< brBolesti; i++)
        {

            Integer odabirZaraze;
            System.out.println("Unosite li virus ili bolest?");
            System.out.println("1) BOLEST");
            System.out.println("2)VIRUS");

            //inicijaliziramo odabirZaraze da izbjegnemo error
            //odabirZaraze might not be initialized
            odabirZaraze = 0;
            boolean isInRange = false;
            while(!isInRange)
            {
                odabirZaraze = unosInta(in,"ODABIR >>");
                if(odabirZaraze < 1 || odabirZaraze >2)
                    System.out.println("Odabrani index ne postoji.Probaj 1 ili 2.");
                else
                    isInRange = true;
            }


            boolean isUnique = false;

            while(!isUnique)
            {
                try{

                    System.out.print("Unesite naziv bolesti ili virusa:");
                    naziv = in.nextLine();

                    boolean isValid = false;
                    while (!isValid)
                    {
                        brSimptoma = unosInta(in,"Unesite broj simptoma(max " + postojeciSimptomi.size() +"):");
                        if(brSimptoma <= postojeciSimptomi.size() && brSimptoma > 0)
                            isValid = true;
                        else{
                            logger.info("Uneseni broj simptoma nije dobar(n+manji od 0 ili veci od broja simptoma).");
                            System.out.println("Unešeni broj nije dobar.Probajte ponovno.");
                        }
                    }


                    simptomi = unosSimptomaSaSpiska(in,postojeciSimptomi,brSimptoma);

                    ProvjeraDuplikataBolesti(new Bolest(naziv,simptomi, (long) i),bolesti);

                    if(odabirZaraze == 1)
                    {
                        bolesti.add(new Bolest(naziv, simptomi, (long) i));
                    }
                    else{
                        bolesti.add(new Virus(naziv,simptomi, (long) i));
                    }
                    isUnique = true;

                }catch (BolestIstihSimptoma ex){
                    logger.info("Unos bolesti/virusa koje ima simptome kao i neka bolest/virus koji se vec nalazi na spisku.");
                    System.out.println("Pogrešan unos,već ste unijeli bolest ili virus s istim simptomima.Molimo ponovite unos.");
                }
            }

        }

        return bolesti;

    }

    /**
     * Unosi simptome sa spiska postojecih simptoma
     *
     * @param in Scanner za unos podataka
     * @param postojeciSimptomi set postojecih simptoma
     * @param brSimptoma broj simptoma koje treba unjeti
     * @return vraca listu simptoma
     */
    public static Set<Simptom> unosSimptomaSaSpiska(Scanner in,List<Simptom> postojeciSimptomi, Integer brSimptoma){

        //List<Simptom> postojeciSimptomi = new ArrayList<>(postojeciSimptomiSet);

        Set<Simptom> simptomi = new HashSet<>();
        Integer odabraniSimptom;

        for(int i = 0; i< brSimptoma; i++)
        {
            int j= i+1;
            System.out.println("Odaberite " + j + ". simptom:");
            ispisiSimptome(postojeciSimptomi);

            boolean isValid = false;

            while(!isValid){
                odabraniSimptom = unosInta(in,"");
                odabraniSimptom -= 1;

                if(odabraniSimptom >= 0 && odabraniSimptom < postojeciSimptomi.size())
                {
                    simptomi.add(postojeciSimptomi.get(odabraniSimptom));
                    //simptomi.add(getSimptomByIndex(odabraniSimptom,postojeciSimptomi));//////////////////////////////////////////////////////////////////////////
                    //System.out.println("Odabrali ste simptom: ");
                    //getSimptomByIndex(odabraniSimptom,postojeciSimptomi).ispisiSimptom();
                    isValid = true;
                }
                else{
                    logger.info("Neispravan unos indexa simptoma prilikom unosa simptoma sa spiska.");
                    System.out.println("Neispravan unos, molimo pokusajte ponovno!");
                }
            }

        }

        return  simptomi;
    }

    /**
     * Ispisuje listu bolesti bez simptoma
     *
     * @param bolesti lista bolesti koju treba ispisati
     */
    public static void ispisiBolestiBezSimptoma(List<Bolest> bolesti){
        int i=1;
        for(Bolest bolest : bolesti)
        {
            System.out.println(i +". " + bolest.getNaziv());
            i++;
        }
    }

    /**
     * Ispisuje listu bolesti sa simptoma
     *
     * @param bolesti set bolesti koju treba ispisati
     */
    public static void ispisiBolesti(Set<Bolest> bolesti){
        int i=1;
        System.out.println("Ispisujem listu zaraza sa simptomima: -->");
        for(Bolest bolest : bolesti)
        {
            System.out.println("-----------------------------");
            System.out.println("Bolest " + i +". " + bolest.getNaziv() + " id:" + bolest.getId());
            System.out.println("Spisak simptoma za tu bolest:");
            ispisiSimptome(bolest.getSimptomi());
            System.out.println("-----------------------------");
            i++;
        }
    }

    /**
     * Ispisuje listu bolesti sa simptoma
     *
     * @param bolesti set bolesti koju treba ispisati
     */
    public static void ispisiBolesti(List<Bolest> bolesti){
        int i=1;
        System.out.println("Ispisujem listu zaraza sa simptomima: -->");
        for(Bolest bolest : bolesti)
        {
            System.out.println("-----------------------------");
            System.out.println("Bolest " + i +". " + bolest.getNaziv() + " id:" + bolest.getId());
            System.out.println("Spisak simptoma za tu bolest:");
            ispisiSimptome(bolest.getSimptomi());
            System.out.println("-----------------------------");
            i++;
        }
    }


    /**
     * Unosi listu simptoma
     *
     * @param in Scanner za unos podataka
     * @param brSimptoma broj simptoma koje treba unjeti
     * @return lista simptoma
     */
    public static Set<Simptom> unosSimptoma(Scanner in,Integer brSimptoma){
        Set<Simptom> simptomi = new HashSet<>();
        String naziv;
        String vrijednost="";

        System.out.println("Unesite podatke o " + brSimptoma  + " simptoma:");

        for (int i =0; i< brSimptoma; i++)
        {
            System.out.print("Unesite naziv simptoma:");
            naziv = in.nextLine();

            boolean isValid = false;

            while(!isValid)
            {
                System.out.print("Unesite vrijednost simptoma(RIJETKO,SREDNJE ILI CESTO):");
                vrijednost = in.nextLine().toUpperCase();
                int count = 0;

                for(VrijednostSimptoma s : VrijednostSimptoma.values())
                    if(vrijednost.equals(s.getVrijednost()))
                    {
                        isValid = true;
                        count++;
                    }
                    if(count == 0)
                    {
                        System.out.println("Unešena vrijednost simptoma ne postoji.Unesite RIJETKO,SREDNJE ILI CESTO." +
                                "Može i malim slovima jer koristim .toUpperCase() so we're good.");
                        logger.info("Unesena vrijednost za simptom koja nije u enumeraciji.");
                    }


            }
            //System.out.print("Unesite vrijednost simptoma(RIJETKO,SREDNJE ILI ČESTO):");
            //vrijednost = in.nextLine();
            simptomi.add( new Simptom(naziv,vrijednost, (long) i));
        }

        return simptomi;

    }

    /**
     * Ispisuje listu simptoma
     *
     * @param simptomi set simptoma koju treba ispisati
     */
    public static void ispisiSimptome(Set<Simptom> simptomi){
        int i = 1;

        for(Simptom simptom : simptomi)
        {
            System.out.println(i + ". " + simptom.getNaziv() + " " + simptom.getVrijednost());
            i++;
        }

    }

    /**
     * Ispisuje listu simptoma
     *
     * @param simptomi set simptoma koju treba ispisati
     */
    public static void ispisiSimptome(List<Simptom> simptomi){
        int i = 1;

        for(Simptom simptom : simptomi)
        {
            System.out.println(i + ". " + simptom.getNaziv() + " " + simptom.getVrijednost());
            i++;
        }

    }

    /**
     * Unosi listu zupanija
     *
     * @param in Scanner za unos podataka
     * @param brZupanija broj zupanije koje treba unjeti
     * @return lista Zupanija
     */
    public static Set<Zupanija> unosZupanija(Scanner in,Integer brZupanija){
        Set<Zupanija> zupanije = new HashSet<>();
        String naziv;

        //inicijalizacija brStanovnika da izbjegnemo error value might not get initialize
        Integer brStanovnika=0;
        Integer brZarazenihOsoba=0;

        System.out.println("Unesite podatke o " + brZupanija + " županije:");

        for(int i=0; i<brZupanija; i++){

            System.out.print("Unesite naziv županije:");
            naziv = in.nextLine();

            //brStanovnika = unosInta(in,"Unesite broj stanovnika:");
            boolean isPositive = false;
             while(!isPositive)
             {
                 brStanovnika = unosInta(in,"Unesite broj stanovnika:");
                 if(brStanovnika < 0)
                 {
                     System.out.println("Broj stanovnika ne moze biti manji od 0");
                     logger.info("Unesen broj stanovnika manji od 0.");
                 }
                 else
                     isPositive = true;
             }

            //brZarazenihOsoba = unosInta(in,"Unesite broj zarazenih ostanovnika:");
            boolean isValid = false;

            while(!isValid)
            {
                brZarazenihOsoba = unosInta(in,"Unesite broj zarazenih ostanovnika:");
                if(brZarazenihOsoba <= brStanovnika)
                    isValid = true;
                else
                {
                    System.out.println("Broj zarazenih ne moze biti veci od broj stanovnika.");
                    logger.info("Unesen broja zarazenih ljudi u zupaniji koji je veci od broja" +
                            " stanovnika u toj zupaniji");
                }
            }

            zupanije.add(new Zupanija(naziv,brStanovnika,brZarazenihOsoba, (long) i));
        }

        return zupanije;
    }

    /**
     * Ispisuje listu zupanija
     *
     * @param zupanije set zupanija koje treba ispisati
     */
    public static void ispisiZupanije(Set<Zupanija> zupanije){
        int i = 1;

        for(Zupanija zupanija : zupanije)
        {
            System.out.println( i + ". Naziv:" + zupanija.getNaziv() + " .Broj stanovnika:" + zupanija.getBrojStanovnika()
                    + " .Broj zarazenih:" + zupanija.getBrZarazenihOsoba() + " Postotak: " + zupanija.getPostotak());/////(float)zupanija.getBrZarazenihOsoba()/zupanija.getBrojStanovnika()*100//////////////////////////////////////////////////////////
            i++;
        }
    }

    /**
     * Ispisuje listu zupanija
     *
     * @param zupanije set zupanija koje treba ispisati
     */
    public static void ispisiZupanije(List<Zupanija> zupanije){
        int i = 1;

        for(Zupanija zupanija : zupanije)
        {
            System.out.println( i + ". Naziv:" + zupanija.getNaziv() + " .Broj stanovnika:" + zupanija.getBrojStanovnika()
                    + " .Broj zarazenih:" + zupanija.getBrZarazenihOsoba() + " Postotak: " + zupanija.getPostotak());/////(float)zupanija.getBrZarazenihOsoba()/zupanija.getBrojStanovnika()*100//////////////////////////////////////////////////////////
            i++;
        }
    }


    /**
     * Ispisuja zupaniju s najvise zarazenih osoba
     *
     * @param zupanije set Zupanija iz kojeg se ispisuje ona s najvecim postotkom zarazenih osoba
     */
    private static void zupanijaSNajviseZarazenih(Set<Zupanija> zupanije) {

        //Zupanija zupaija = null;
        ArrayList<Zupanija> sortiraneZupanije = new ArrayList<>(zupanije);

        //provjeriti jesu li ove sljedece dvije linije ekvivalentne
        //tj da li rade istu stvar
        Collections.sort(sortiraneZupanije,new CovidSorter());
        sortiraneZupanije.sort(new CovidSorter());

        sortiraneZupanije.get(zupanije.size()-1).ispisiZupanijuSPostotkomZarazenih();

    }


    /**
     * Ispisuje zarazu i listu ljudi koji od nje boluju
     *
     * @param mapaZarazaBolesnika mapa s zarazama i listama oboljelih od te zaraze
     */
    private static void ispisiListuBolesnikaSBolestima(Map<Bolest, List<Osoba>> mapaZarazaBolesnika) {

        for(Map.Entry<Bolest,List<Osoba>> entry : mapaZarazaBolesnika.entrySet())
        {
            if(entry.getValue() != null && entry.getValue().isEmpty()!=true)
            {
                System.out.println("Od zaraze " + entry.getKey().getNaziv() + " boluju ");
                ispisiOsobe(entry.getValue());
            }
        }

    }

    /**
     * Ubacije zaraze i liste oboljelih od te zaraze u mapu
     *
     * @param osobe Lista osoba iz koje se uzimaju osobe koje boluju od odredene bolesti
     * @param bolesti set bolesti iz kojeg se iscitavaju bolesti
     * @return mapa tipa zaraza-lista oboljelih od te zaraze
     */
    private static Map<Bolest, List<Osoba>> napuniMapu(List<Osoba> osobe , Set<Bolest> bolesti) {

        //Map<Bolest, ArrayList<Osoba>> mapa = new HashMap<>();

        List<Bolest> listaBolesti = new ArrayList<>(bolesti);
        Map<Bolest, List<Osoba>> mapa = new HashMap<>();


        mapa  = osobe.stream().collect(Collectors.groupingBy(Osoba::getZarazenBolescu));

        return mapa;
    }

    /**
     * Sortira virusa u poretku obrnuto od abecednog s lambdama
     *
     * @param klinika klasa koja sadrzi spisak zaraza iz kojeg se uzimaju virusi
     */
    private static void ispisiViruseObrnutoOdAbecednogPoretka(KlinikaZaInfektivneBolesti<Virus,Osoba> klinika){

        Comparator<Virus> usporedbaVirusa = Comparator.comparing(Virus::getNaziv).reversed();

        klinika.getSviVirusi().stream()
                .sorted(usporedbaVirusa)
                .forEach(System.out::println);

    }

    /**
     * Sortira virusa u poretku obrnuto od abecednog bez lambdi
     *
     * @param klinikaInput klasa koja sadrzi spisak virusa
     */
    public static void sortiranjeBezLambdi(KlinikaZaInfektivneBolesti<Virus,Osoba> klinikaInput){

        int index= 1;
        KlinikaZaInfektivneBolesti<Virus,Osoba> klinika = klinikaInput;

        boolean sorted = false;

        while(!sorted)
        {
            sorted = true;
            for(int i = 0; i< klinika.getSviVirusi().size()-1 ; i++)
            {
                if(klinika.getSviVirusi().get(i).getNaziv().compareTo(klinika.getSviVirusi().get(i+1).getNaziv()) < 0)
                {
                    Virus v = klinika.getSviVirusi().get(i);
                    klinika.getSviVirusi().set(i,klinika.getSviVirusi().get(i+1));
                    klinika.getSviVirusi().set(i+1,v);
                    sorted = false;

                }
            }
        }

        for(Virus v : klinika.getSviVirusi())
        {
            System.out.println( index + ". " + v.getNaziv());
            index++;
        }

    }

    /**
     * Ispisuje osobe koje sadrze string za pretragu
     *
     * @param in Scanner kojim se unosi string za pretragu
     * @param klinika klasa koja sadrzi spisak svih osoba
     */
    public static void pretrazivanjeOsobaOptional(Scanner in,KlinikaZaInfektivneBolesti<Virus,Osoba> klinika){


        System.out.println("Unesite string za pretragu po prezimenu:");
        String zaPretragu = in.nextLine();

        System.out.println("Osobe čije prezime sadrži " + zaPretragu + " su sljedeće: ");
        //List<Osoba> nadeneOsobe = Optional.ofNullable(klinika.getSveOsobe()).filter( o -> )
         List<Osoba> nadeneOsobe = Optional.of(klinika)
                        .map(KlinikaZaInfektivneBolesti::getSveOsobe).orElse(Collections.emptyList()).stream()
                        .filter(o -> o.getPrezime().toLowerCase().contains(zaPretragu.toLowerCase()))
                        .collect(Collectors.toList());

        nadeneOsobe.stream().forEach(System.out::println);

    }

    /**
     * Ispisuja broj simptoma za svaku zarazu(bolest ili virus)
     *
     * @param bolesti set bolesti iz kojeg se iscitavaju zaraze
     */
    public static void brojSimptomaZaSvakuZarazu(Set<Bolest> bolesti){

        List<Bolest> sveBolesti = new ArrayList<>(bolesti);
        List<String> naziviBolesti = sveBolesti.stream().map(b -> b.getNaziv()).collect(Collectors.toList());
        Map<String,Integer> mapa = new HashMap<>();

        for(String b : naziviBolesti)
        {
            Integer count = sveBolesti.stream().filter(x -> x.getNaziv().equals(b)).findAny().get().getSimptomi().size();
            mapa.put(b,count);
        }

        for(String b : mapa.keySet())
            System.out.println(b + " ima " + mapa.get(b) + " simptoma");

    }


    /**
     * Unosi zupanije iz datoteke u set
     *
     * @return set Zupanija
     */
    public static Set<Zupanija> unesiZupanije(){

        Set<Zupanija> zupanijeSet = new HashSet<>();

        File zupanije = new File("dat/zupanije.txt");

        try (FileReader fileReader = new FileReader(zupanije);BufferedReader reader = new BufferedReader(fileReader)) {

            String procitanaLinija;
            String naziv = "";
            Integer brStanovnika = 0,brZarazenihStanovnika = 0;
            Long id = Long.valueOf(0);
            Integer brojLinije = 1;

            while((procitanaLinija=reader.readLine()) != null){

                Integer kategorija = brojLinije%4;

                switch (kategorija){
                    case 1:
                        id = Long.parseLong(procitanaLinija);
                        break;
                    case 2:
                        naziv = procitanaLinija;
                        break;
                    case 3:
                        brStanovnika = Integer.parseInt(procitanaLinija);
                        break;
                    case 0:
                        brZarazenihStanovnika = Integer.parseInt(procitanaLinija);
                        break;

                }

                if((brojLinije%4) == 0){

                    Zupanija zupanija = new Zupanija(naziv,brStanovnika,brZarazenihStanovnika,id);
                    zupanijeSet.add(zupanija);
                }
                brojLinije++;

            }

        } catch (FileNotFoundException ex){
            ex.printStackTrace();
            logger.info("Neuspjesno otvaranje file-a zupanije.txt");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("IO exception za zupanije.txt");
        }

        try (ObjectOutputStream serializator = new ObjectOutputStream(
                new FileOutputStream("dat/serijaliziraneZupanije.dat"))) {

            List<Zupanija> serijaliziraneZupanije = new ArrayList<>();
            for (Zupanija z : zupanijeSet)
                if(z.getPostotak() > 2)
                    serijaliziraneZupanije.add(z);
            serializator.writeObject(serijaliziraneZupanije);

        } catch (FileNotFoundException e) {
            logger.info("dat/serijaliziraneZupanije.dat nije pronadena");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("IO exeption za dat/serijaliziraneZupanije.dat");
        }

        System.out.println("Ucitavam podatke o zupanijama...");

        return zupanijeSet;

    }

    /**
     * Unosi Simptome iz datoteke
     *
     * @return set Zupanija
     */
    public static Set<Simptom> unesiSimptome(){

        Set<Simptom> simptomiSet = new HashSet<>();

        File simptomi = new File("dat/simptomi.txt");

        try (FileReader fileReader = new FileReader(simptomi);BufferedReader reader = new BufferedReader(fileReader)) {

            String procitanaLinija;
            String naziv = "",vrijednost = "";
            Long id = Long.valueOf(0);
            Integer brojLinije = 1;

            while((procitanaLinija=reader.readLine()) != null){

                Integer kategorija = brojLinije%3;

                switch (kategorija){
                    case 1:
                        id = Long.parseLong(procitanaLinija);
                        break;
                    case 2:
                        naziv = procitanaLinija;
                        break;
                    case 0:
                        vrijednost = procitanaLinija;
                        break;
                }

                if((brojLinije%3) == 0){

                    Simptom simptom = new Simptom(naziv,vrijednost,id);
                    simptomiSet.add(simptom);
                }
                brojLinije++;

            }

        } catch (FileNotFoundException ex){
            ex.printStackTrace();
            logger.info("Neuspjesno otvaranje file-a simptomi.txt");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("IO exception za simptomi.txt");
        }

        try (ObjectOutputStream serializator = new ObjectOutputStream(
                new FileOutputStream("dat/serijaliziraniSimptomi.dat"))) {

            serializator.writeObject(new ArrayList<>(simptomiSet));

        } catch (FileNotFoundException e) {
            logger.info("dat/serijaliziraniSimptomi.dat nije pronadena");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("IO exeption za dat/serijaliziraniSimptomi.dat");
        }

        System.out.println("Ucitavam podatke o simptomima...");

        return simptomiSet;

    }

    /**
     * Unosi Bolesti i Viruse iz datoteke
     *
     * @param simptomi set Simptoma iz kojeg se citaju simptomi po njihovom Id-u
     * @return set Bolesti
     */
    public static Set<Bolest> unesiBolesti(Set<Simptom> simptomi){

        Set<Bolest> bolestiSet = new HashSet<>();

        File bolesti = new File("dat/bolesti.txt");

        try (FileReader fileReader = new FileReader(bolesti);BufferedReader reader = new BufferedReader(fileReader)) {

            String procitanaLinija;
            String naziv = "";
            //Integer idSimptoma;
            String[] idSimptoma;
            Long id = Long.parseLong(String.valueOf(0));
            Integer brojLinije = 1;
            List<Simptom> simptomiBolesti = new ArrayList<>();

            while((procitanaLinija=reader.readLine()) != null){

                Integer kategorija = brojLinije%3;

                switch (kategorija){
                    case 1:
                        id = Long.parseLong(procitanaLinija);
                        break;
                    case 2:
                        naziv = procitanaLinija;
                        break;
                    case 0:
                        simptomiBolesti = new ArrayList<>();
                        procitanaLinija = procitanaLinija.replaceAll("[^\\d]", " ");
                        procitanaLinija = procitanaLinija.trim();
                        procitanaLinija = procitanaLinija.replaceAll(" +", " ");
                        idSimptoma = procitanaLinija.split(" ",10);
                        for(String s : idSimptoma)
                            if(!s.equals(""))
                                simptomiBolesti.add(getSimptomById(s,simptomi));
                        break;
                }

                if((brojLinije%3) == 0 && brojLinije != 0){

                    Bolest bolest = new Bolest(naziv,simptomiBolesti,id);
                    bolestiSet.add(bolest);
                    idSimptoma = new String[simptomi.size()];
                }
                brojLinije++;

            }

        } catch (FileNotFoundException ex){
            ex.printStackTrace();
            logger.info("Neuspjesno otvaranje file-a bolesti.txt");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("IO exception za bolesti.txt");
        }

        bolesti = new File("dat/virusi.txt");

        try (FileReader fileReader = new FileReader(bolesti);BufferedReader reader = new BufferedReader(fileReader)) {

            String procitanaLinija;
            String naziv = "";
            Integer bidSimptoma;
            Long id = Long.parseLong(String.valueOf(0));
            Integer brojLinije = 1;
            List<Simptom> simptomiBolesti = new ArrayList<>();

            while((procitanaLinija=reader.readLine()) != null){

                Integer kategorija = brojLinije%3;

                switch (kategorija){
                    case 1:
                        id = Long.parseLong(procitanaLinija);
                        break;
                    case 2:
                        naziv = procitanaLinija;
                        break;
                    case 0:
                        simptomiBolesti = new ArrayList<>();
                        procitanaLinija = procitanaLinija.replaceAll("[^\\d]", " ");
                        procitanaLinija = procitanaLinija.trim();
                        procitanaLinija = procitanaLinija.replaceAll(" +", " ");
                        String[] idSimptoma = procitanaLinija.split(" ",10);
                        for(String s : idSimptoma)
                            if(!s.equals(""))
                                simptomiBolesti.add(getSimptomById(s,simptomi));
                        break;
                }

                if((brojLinije%3) == 0 && brojLinije != 0){

                    Virus virus = new Virus(naziv,simptomiBolesti,id);
                    bolestiSet.add(virus);
                }
                brojLinije++;

            }

        } catch (FileNotFoundException ex){
            ex.printStackTrace();
            logger.info("Neuspjesno otvaranje file-a virusi.txt");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("IO exception za virusi.txt");
        }

        try (ObjectOutputStream serializator = new ObjectOutputStream(
                new FileOutputStream("dat/serijaliziraneBolesti.dat"))) {

            serializator.writeObject(new ArrayList<>(bolestiSet));

        } catch (FileNotFoundException e) {
            logger.info("dat/serijaliziraneBolesti.dat nije pronadena");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("IO exeption za dat/serijaliziraneBolesti.dat");
        }

        System.out.println("Ucitavam podatke o bolestima...");
        System.out.println("Ucitavam podatke o virusima...");

        return bolestiSet;

    }

    /**
     * Dohvaca Simptom s Id-em id iz seta simptoma
     *
     * @param id id simptoma kojeg treba dohvatiti
     * @param simptomiSet set simptoma u kojem se trazi simptom
     * @return simptom s Id-em id
     */
    public static Simptom getSimptomById(String id,Set<Simptom> simptomiSet){

        List<Simptom> simptom = new ArrayList<>(simptomiSet);
        Long idLong = Long.parseLong(id);

        for(Simptom b : simptom)
            if(b.getId().equals(idLong))
                return b;

        return null;

    }

    /**
     * Unosi Osobe iz datoteke
     *
     * @param zupanijeSet set zupanija iz kojeg se dohvaca zupanija prebivalista osobe(po ID-u)
     * @param bolestiSet set bolesti iz gojeg se dohvaca bolest osobe(po ID-u)
     * @return listu Osoba
     */
    public static List<Osoba> unesiOsobe(Set<Zupanija> zupanijeSet,Set<Bolest> bolestiSet){

        File osobe = new File("dat/osobe.txt");
        List<Osoba> osobeList = new ArrayList<>();

        try (FileReader fileReader = new FileReader(osobe); BufferedReader reader = new BufferedReader(fileReader)) {

            String ime="",prezime="";
            Long id = Long.parseLong(String.valueOf(0)),idZupanije,idBolesti;
            String[] idKontakata;
            String procitanaLinija;
            Integer starost = 0;
            Integer brLinije = 1;
            List<Bolest> bolesti = new ArrayList<>(bolestiSet);
            List<Zupanija> zupanije = new ArrayList<>(zupanijeSet);
            List<Osoba> kontakti = new ArrayList<>();
            Zupanija zupanijaPrebivalista = zupanije.get(0);
            Bolest zarazenBolescu = bolesti.get(0);

            while((procitanaLinija = reader.readLine()) != null){

                Integer kategorija = brLinije%7;

                switch (kategorija){

                    case 1:
                        id = Long.parseLong(procitanaLinija);
                        break;
                    case 2:
                        ime = procitanaLinija;
                        break;
                    case 3:
                        prezime = procitanaLinija;
                        break;
                    case 4:
                        starost = Integer.parseInt(procitanaLinija);
                        break;
                    case 5:
                        idZupanije = Long.parseLong(procitanaLinija);
                        zupanijaPrebivalista = getZupanijaById(idZupanije,zupanije);
                        break;
                    case 6:
                        idBolesti = Long.parseLong(procitanaLinija);
                        zarazenBolescu = getBolestById(idBolesti,bolesti);
                        break;
                    case 0:
                        kontakti = new ArrayList<>();
                        procitanaLinija = procitanaLinija.replaceAll("[^\\d]", " ");
                        procitanaLinija = procitanaLinija.trim();
                        procitanaLinija = procitanaLinija.replaceAll(" +", " ");
                        idKontakata = procitanaLinija.split(" ",10);
                        for(String s : idKontakata)
                            if(!s.equals("") && !osobeList.equals(null))
                                kontakti.add(getOsobaById(s,osobeList));
                        break;

                }

                if((brLinije%7)==0 && brLinije!=0)
                    if(id.equals(Long.parseLong(String.valueOf(1))))
                        osobeList.add(new Osoba(ime,prezime,starost,zupanijaPrebivalista,zarazenBolescu,null,id));
                    else
                        osobeList.add(new Osoba(ime,prezime,starost,zupanijaPrebivalista,zarazenBolescu,kontakti,id));


                brLinije++;

            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.info("File not found za osobe.txt");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("IO exception za osobe.txt");
        }

        try (ObjectOutputStream serializator = new ObjectOutputStream(
                new FileOutputStream("dat/serijaliziraneOsobe.dat"))) {

            serializator.writeObject(osobeList);

        } catch (FileNotFoundException e) {
            logger.info("dat/serijaliziraneOsobe.dat nije pronadena");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("IO exeption za dat/serijaliziraneOsobe.dat");
        }

        return osobeList;

    }

    /**
     * Dohvaca Osobu s ID-em id iz liste osoba
     *
     * @param s id trazene osobe
     * @param osobe lista osoba iz koje trazimo osobu s Id-em s
     * @return osoba s id-em s
     */
    private static Osoba getOsobaById(String s, List<Osoba> osobe) {

        Long id = Long.parseLong(s);

        for(Osoba o : osobe)
            if(o.getId().equals(id))
                return o;

        return null;

    }

    /**
     * Dohvaca Bolest s ID-em id iz liste bolesti
     *
     * @param idBolesti id bolesti koju trazimo
     * @param bolesti lista bolesti iz koje trazimo bolest
     * @return bolest s Id-em idBolesti
     */
    private static Bolest getBolestById(Long idBolesti, List<Bolest> bolesti) {

        for(Bolest b : bolesti)
            if(b.getId().equals(idBolesti))
                return b;

        return null;

    }

    /**
     * Dohvaca Zupaniju s ID-em id iz liste zupanija
     *
     * @param idZupanije id zupanije koju trazimo
     * @param zupanije lista zupanija iz koje trazimo zupaniju
     * @return zupanija s Id-em idZupanije
     */
    private static Zupanija getZupanijaById(Long idZupanije, List<Zupanija> zupanije) {

        for(Zupanija z : zupanije)
            if(z.getId().equals(idZupanije))
                return z;

        return null;

    }


    public static void main(String[] args) {

        Set<Zupanija> zupanije = new TreeSet<>(new CovidSorter());
        Set<Simptom> simptomi = new HashSet<>();
        Set<Bolest> bolesti = new HashSet<>();
        List<Osoba> osobe/* = new ArrayList<>()*/;
        Map<Bolest,List<Osoba>> mapaZarazaBolesnika = new HashMap<>();
        KlinikaZaInfektivneBolesti<Virus,Osoba> klinika = new
                KlinikaZaInfektivneBolesti<>(new ArrayList<Virus>(),new ArrayList<Osoba>());

        Scanner in = new Scanner(System.in);

        logger.info("Pocetak programa.");

        Integer brZupanija,brBolesti,brVirusa,brSimptoma,brOsoba;

        /*
        brZupanija = unosInta(in,"Unesite broj zupanija koje zelite unijeti:");
        zupanije = unosZupanija(in,brZupanija);
        */

        zupanije = unesiZupanije();

        ispisiZupanije(zupanije);


        /*
        System.out.println("----------------------------------------------");
        brSimptoma = unosInta(in,"Unesite broj simptoma koje zelite unijeti:");
        simptomi = unosSimptoma(in,brSimptoma);
        */

        simptomi = unesiSimptome();

        ispisiSimptome(simptomi);

/*
        System.out.println("----------------------------------------------");
        brBolesti = unosInta(in,"Unesite broj bolesti koje zelite unijeti:");
        brVirusa = unosInta(in,"Unesite broj virusa koje zelite unijeti:");
        bolesti = unosBolesti(in,brBolesti+brVirusa,simptomi);

 */
        bolesti = unesiBolesti(simptomi);

        ispisiBolesti(bolesti);
        klinika.setSviVirusi(bolesti);

        /*
        System.out.println("----------------------------------------------");
        brOsoba = unosInta(in,"Unesite broj osoba koje zelite unijeti:");
        osobe = unosOsoba(in,brOsoba,zupanije,bolesti);*/

        osobe = unesiOsobe(zupanije,bolesti);

        ispisiListuOsoba(osobe);
        klinika.setSveOsobe(osobe);

        System.out.println("----------------------------------------------");
        System.out.println("***");
        mapaZarazaBolesnika = napuniMapu(osobe,bolesti);
        ispisiListuBolesnikaSBolestima(mapaZarazaBolesnika);
        System.out.println("***");


        System.out.println("----------------------------------------------");
        System.out.println("Najvise zaraženih osoba ima u županiji :");
        //zupanijaSNajviseZarazenih(zupanije);
        Zupanija maxZarazenih = zupanije.stream().max(Comparator.comparing(Zupanija::getPostotak)).get();
        maxZarazenih.ispisiZupanijuSPostotkomZarazenih();


        System.out.println("----------------------------------------------");
        System.out.println("Generics klasa:");
        System.out.println(klinika.toString());

        System.out.println("----------------------------------------------");
        System.out.println("Virusi sortirani po nazivu suprotno od poretka abecede:");
        Instant startSLambdom = Instant.now();
        ispisiViruseObrnutoOdAbecednogPoretka(klinika);
        Instant krajSLambdom = Instant.now();
        System.out.println("----------------------------------------------");
        System.out.println("Bez lambdi:");
        Instant startrBezLambde = Instant.now();
        sortiranjeBezLambdi(klinika);
        Instant krajBezLambdi = Instant.now();

        System.out.println("Sortiranje objekata korištenjem lambdi traje " + Duration.between(startSLambdom,krajSLambdom).toMillis() +
                " milisekundi, a bez lambda traje " + Duration.between(startrBezLambde,krajBezLambdi).toMillis() +
                " milisekundi");

        pretrazivanjeOsobaOptional(in,klinika);

        System.out.println("Broj simptoma za svaku zarazu:");
        brojSimptomaZaSvakuZarazu(bolesti);

        logger.info("Kraj programa.");
        in.close();

    }


}
