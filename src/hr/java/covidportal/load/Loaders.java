package hr.java.covidportal.load;

import hr.java.covidportal.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static hr.java.covidportal.main.Glavna.logger;

public class Loaders {

    /**
     * Ucitava zupanije u listu iz datoteke zupanije2.txt koja nema ID-eve
     *
     * @return lista zupanija
     */
    public static List<Zupanija> ucitajZupanije(){

        List<Zupanija> zupanijeList = new ArrayList<>();

        File zupanije = new File("dat/zupanije2.txt");
        Long id = Long.valueOf(1);

        try (FileReader fileReader = new FileReader(zupanije);BufferedReader reader = new BufferedReader(fileReader)) {

            String procitanaLinija;
            String naziv = "";
            Integer brStanovnika = 0,brZarazenihStanovnika = 0;
            //Long id = Long.valueOf(zupanijeList.size()+1);
            Integer brojLinije = 1;

            while((procitanaLinija=reader.readLine()) != null){

                Integer kategorija = brojLinije%3;

                switch (kategorija){
                    case 1:
                        naziv = procitanaLinija;
                        break;
                    case 2:
                        brStanovnika = Integer.parseInt(procitanaLinija);
                        break;
                    case 0:
                        brZarazenihStanovnika = Integer.parseInt(procitanaLinija);
                        break;

                }

                if((brojLinije%3) == 0){

                    Zupanija zupanija = new Zupanija(naziv,brStanovnika,brZarazenihStanovnika,id);
                    zupanijeList.add(zupanija);
                    id++;
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

        return zupanijeList;

    }

    /**
     * Dodaje zupaniju u datoteku zupanije2.txt
     *
     * @param z zupanija koja se dodaje u datoteku
     * @return 1 za uspjesno, 0 za neuspjesno dadavanje
     */
    public static int dodajZupanijuUDatoteku(Zupanija z){

        try (FileWriter fileWriter = new FileWriter("dat/zupanije2.txt",true);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {


                //writer.newLine();
                //writer.write(String.valueOf(z.getId()));
                //writer.newLine();
                writer.write(z.getNaziv());
                writer.newLine();
                writer.write(String.valueOf(z.getBrojStanovnika()));
                writer.newLine();
                writer.write(String.valueOf(z.getBrZarazenihOsoba()));
                writer.newLine();



            return 1;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;

    }

    /**
     * Ucitava simptome iz datoteke simptomi2.txt
     *
     * @return lista simptoma
     */
    public static List<Simptom> ucitajSimptome(){

        List<Simptom> simptomiList = new ArrayList<>();

        File simptomi = new File("dat/simptomi2.txt");
        Long id = Long.valueOf(1);

        try (FileReader fileReader = new FileReader(simptomi);BufferedReader reader = new BufferedReader(fileReader)) {

            String procitanaLinija;
            String naziv = "",vrijednost = "";
            //Long id = Long.valueOf(0);
            Integer brojLinije = 1;

            while((procitanaLinija=reader.readLine()) != null){

                Integer kategorija = brojLinije%2;

                switch (kategorija){
                    case 1:
                        naziv = procitanaLinija;
                        break;
                    case 0:
                        vrijednost = procitanaLinija;
                        break;
                }

                if((brojLinije%2) == 0){

                    Simptom simptom = new Simptom(naziv,vrijednost,id);
                    simptomiList.add(simptom);
                    id++;
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


        System.out.println("Ucitavam podatke o simptomima...");

        return simptomiList;

    }

    /**
     * Dodaje simptom u datoteku simptomi2.txt
     *
     * @param s simptom koji se doaje u datoteku
     * @return 1 za uspjesno, 0 za neuspjesno dodavanje u datoteku
     */
    public static int dodajSimptomUDatoteku(Simptom s){

        try (FileWriter fileWriter = new FileWriter("dat/simptomi2.txt",true);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {


            //writer.newLine();
            //writer.write(String.valueOf(z.getId()));
            //writer.newLine();
            writer.write(s.getNaziv());
            writer.newLine();
            writer.write(s.getVrijednost());
            writer.newLine();



            return 1;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;

    }

    /**
     * Ucitava bolesti iz atoteke bolesti2.txt
     *
     * @return lista bolesti iz datoteke bolesti2.txt
     */
   public static List<Bolest> ucitajBolesti(){

       List<Bolest> bolestLista = new ArrayList<>();

       File bolesti = new File("dat/bolesti2.txt");
       Long id = Long.valueOf(1);
       List<Simptom> simptomi = ucitajSimptome();

       try (FileReader fileReader = new FileReader(bolesti);BufferedReader reader = new BufferedReader(fileReader)) {

           String procitanaLinija;
           String naziv = "";
           //Integer idSimptoma;
           String[] idSimptoma;
           Integer brojLinije = 1;
           List<Simptom> simptomiBolesti = new ArrayList<>();

           while((procitanaLinija=reader.readLine()) != null){

               Integer kategorija = brojLinije%2;

               switch (kategorija){
                   case 1:
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
                               //simptomiBolesti.add(getSimptomById(s,simptomi));
                               for(Simptom x : simptomi)
                                   if(Long.valueOf(s).equals(x.getId()))
                                       simptomiBolesti.add(x);
                       break;
               }

               if((brojLinije%2) == 0 && brojLinije != 0){

                   Bolest bolest = new Bolest(naziv,simptomiBolesti,id);
                   bolestLista.add(bolest);
                   idSimptoma = new String[simptomi.size()];
                   id++;
               }
               brojLinije++;

           }

       } catch (FileNotFoundException ex){
           ex.printStackTrace();
           logger.info("Neuspjesno otvaranje file-a bolesti2.txt");
       } catch (IOException e) {
           e.printStackTrace();
           logger.info("IO exception za bolesti2.txt");
       }

       return bolestLista;

   }

    /**
     * Dodaje bolest u datoteku bolesti2.txt
     *
     * @param b bolest koju treba dodati u datoteku
     * @return 1 za uspjesno, 0 za neuspjesno dodavanje u datoteku
     */
    public static int dodajBolestUDatoteku(Bolest b){

        try (FileWriter fileWriter = new FileWriter("dat/bolesti2.txt",true);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            String indexiSimptoma = "";
            for(Simptom s : b.getSimptomi())
            {
                indexiSimptoma = indexiSimptoma + s.getId() + ",";

            }


            //writer.newLine();
            //writer.write(String.valueOf(z.getId()));
            //writer.newLine();
            writer.write(b.getNaziv());
            writer.newLine();
            writer.write(indexiSimptoma);
            writer.newLine();



            return 1;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;

    }

    /**
     * Ucitava viruse iz datoteke virusi2.txt
     *
     * @return lista virusa
     */
    public static List<Virus> ucitajViruse(){

        List<Virus> virusLista = new ArrayList<>();

        File virusi = new File("dat/virusi2.txt");
        Long id = Long.valueOf(1);
        List<Simptom> simptomi = ucitajSimptome();

        try (FileReader fileReader = new FileReader(virusi);BufferedReader reader = new BufferedReader(fileReader)) {

            String procitanaLinija;
            String naziv = "";
            //Integer idSimptoma;
            String[] idSimptoma;
            Integer brojLinije = 1;
            List<Simptom> simptomiBolesti = new ArrayList<>();

            while((procitanaLinija=reader.readLine()) != null){

                Integer kategorija = brojLinije%2;

                switch (kategorija){
                    case 1:
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
                                //simptomiBolesti.add(getSimptomById(s,simptomi));
                                for(Simptom x : simptomi)
                                    if(Long.valueOf(s).equals(x.getId()))
                                        simptomiBolesti.add(x);
                        break;
                }

                if((brojLinije%2) == 0 && brojLinije != 0){

                    Virus virus = new Virus(naziv,simptomiBolesti,id);
                    virusLista.add(virus);
                    idSimptoma = new String[simptomi.size()];
                    id++;
                }
                brojLinije++;

            }

        } catch (FileNotFoundException ex){
            ex.printStackTrace();
            logger.info("Neuspjesno otvaranje file-a virusi2.txt");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("IO exception za virusi2.txt");
        }

        return virusLista;

    }

    /**
     * Dodaje virus v  udatoteku virusi2.txt
     *
     * @param v virus koji treba dodati u datoteku
     * @return 1 za uspjesno, 0 za neuspjesno dodavanje u datoteku
     */
    public static int dodajVirusUDatoteku(Virus v){

        try (FileWriter fileWriter = new FileWriter("dat/virusi2.txt",true);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            String indexiSimptoma = "";
            for(Simptom s : v.getSimptomi())
            {
                indexiSimptoma = indexiSimptoma + s.getId() + ",";

            }


            //writer.newLine();
            //writer.write(String.valueOf(z.getId()));
            //writer.newLine();
            writer.write(v.getNaziv());
            writer.newLine();
            writer.write(indexiSimptoma);
            writer.newLine();



            return 1;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;

    }

    /**
     * Ucitava osobe iz datoteke osobe2.txt
     *
     * @return lista osoba
     */
    public static List<Osoba> ucitajOsobe(){

        File osobe = new File("dat/osobe2.txt");
        List<Osoba> osobeList = new ArrayList<>();

        try (FileReader fileReader = new FileReader(osobe); BufferedReader reader = new BufferedReader(fileReader)) {

            String ime="",prezime="";
            Long id = Long.parseLong(String.valueOf(1)),idZupanije,idBolesti;
            String[] idKontakata;
            String procitanaLinija;
            Integer starost = 0;
            Integer brLinije = 1;
            List<Bolest> bolesti = ucitajBolesti();

            List<Virus> virusi = ucitajViruse();
            for(Virus v : virusi)
                bolesti.add(v);

            List<Zupanija> zupanije = ucitajZupanije();
            List<Osoba> kontakti = new ArrayList<>();
            Zupanija zupanijaPrebivalista = zupanije.get(0);
            Bolest zarazenBolescu = bolesti.get(0);

            while((procitanaLinija = reader.readLine()) != null){

                Integer kategorija = brLinije%6;

                switch (kategorija){

                    case 1:
                        ime = procitanaLinija;
                        break;
                    case 2:
                        prezime = procitanaLinija;
                        break;
                    case 3:
                        starost = Integer.parseInt(procitanaLinija);
                        break;
                    case 4:
                        idZupanije = Long.parseLong(procitanaLinija);
                        zupanijaPrebivalista = getZupanijaById(idZupanije,zupanije);
                        break;
                    case 5:
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
////////////////////////////////
                        if(procitanaLinija.equals("0"))
                            kontakti= null;
                        ///////////////////////////////////////
                        break;

                }

                if((brLinije%6)==0 && brLinije!=0)
                    if(id.equals(Long.parseLong(String.valueOf(1))))
                    {
                        osobeList.add(new Osoba(ime,prezime,starost,zupanijaPrebivalista,zarazenBolescu,null,id));
                        id++;
                    }
                        // osobeList.add(new Osoba(ime,prezime,starost,zupanijaPrebivalista,zarazenBolescu,null,id));
                    else
                        //osobeList.add(new Osoba(ime,prezime,starost,zupanijaPrebivalista,zarazenBolescu,kontakti,id));
                    {
                        osobeList.add(new Osoba(ime,prezime,starost,zupanijaPrebivalista,zarazenBolescu,kontakti,id));
                        id++;
                    }


                brLinije++;

            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.info("File not found za osobe2.txt");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("IO exception za osobe2.txt");
        }


        return osobeList;


    }

    /**
     * Dodaje osobu o u datoteku osobe2.txt
     *
     * @param o osoba koju treba dodati u datoteku
     * @return 1 za uspjesno, 0 uz neuspjesno dodavanje u datoteku
     */
    public static int dodajOsobuUDatoteku(Osoba o){

        try (FileWriter fileWriter = new FileWriter("dat/osobe2.txt",true);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {


            String indexiKontakata = "";

            if(o.getKontaktiraneOsobe().equals(null))
                indexiKontakata = indexiKontakata + "0";
            else{

                for(Osoba x : o.getKontaktiraneOsobe())
                {
                    indexiKontakata = indexiKontakata + x.getId() + ",";

                }

            }



            //writer.newLine();
            //writer.write(String.valueOf(z.getId()));
            //writer.newLine();
            writer.write(o.getIme());
            writer.newLine();
            writer.write(o.getPrezime());
            writer.newLine();
            writer.write(String.valueOf(o.getStarost()));
            writer.newLine();
            writer.write(String.valueOf(o.getZupanija().getId()));
            writer.newLine();
            writer.write(String.valueOf(o.getZarazenBolescu().getId()));
            writer.newLine();
            writer.write(indexiKontakata);
            writer.newLine();



            return 1;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;

    }


    /**
     * Ucitava listu koja sadrzi i viruse i bolesti
     *
     * @return lista virusa i bolesti
     */
    public static List<Bolest> getListuBolestiIVirusa(){

        List<Bolest> bolesti = ucitajBolesti();
        List<Virus> virusi = ucitajViruse();

        for(Virus v : virusi)
            bolesti.add(v);

        return bolesti;

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
        //return bolesti.get(Math.toIntExact(idBolesti)-1);

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


}
