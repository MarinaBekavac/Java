package hr.java.covidportal.load;

import hr.java.covidportal.model.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static main.java.sample.Main.loggerMain;

public class BazaPodataka {

    ///private static Connection connection;

    private static final String DATABASE_CONFIGURATION = "src/main/resources/database.properties";
    private static Boolean aktivnaVezaSBazomPodataka = false;

    public static synchronized Connection connectToDatabase(){

        while(aktivnaVezaSBazomPodataka == true){

            try {
                /**/BazaPodataka.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        BazaPodataka.aktivnaVezaSBazomPodataka = true;
        
        Connection connection;

        Properties svojstva = new Properties();

        try {
            svojstva.load(new FileReader(DATABASE_CONFIGURATION));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String urlBaze = svojstva.getProperty("bazaURL");
        String username = svojstva.getProperty("username");
        String password = svojstva.getProperty("password");

        try {

            //connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/covid","student","student");
            connection = DriverManager.getConnection(urlBaze,username,password);
            //System.out.println("Spojio sam se na bazu");

            BazaPodataka.aktivnaVezaSBazomPodataka = false;
            BazaPodataka.class.notifyAll();

            return connection;


        } catch (SQLException throwables) {
            //System.out.println("Spajanje na bazu neuspjesno");
            loggerMain.info("Spajanje na bazu neuspjesno");
            throwables.printStackTrace();

            BazaPodataka.aktivnaVezaSBazomPodataka = false;
            BazaPodataka.class.notifyAll();

            return null;
        }

        //return connection;
    }

    public static void closeConnectionToDatabase(Connection connection){

        try {

            connection.close();
            //System.out.println("Zatvaram vezu s bazom");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static List<Simptom> dohvatiSveSimptome() throws SQLException {

        Connection veza = connectToDatabase();

        Statement statement = veza.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM SIMPTOM");

        List<Simptom> simptomi = new ArrayList<>();

        while(rs.next()){

            Long id = rs.getLong("ID");
            String naziv = rs.getString("NAZIV");
            String vrijednost = rs.getString("VRIJEDNOST");

            Simptom simpton = new Simptom(naziv,vrijednost,id);
            simptomi.add(simpton);

        }

        closeConnectionToDatabase(veza);

        return simptomi;

    }

    public static Simptom dohvatiSimptomById(Long id) throws SQLException {

        Connection veza = connectToDatabase();

        PreparedStatement statement = veza.prepareStatement("SELECT * FROM SIMPTOM WHERE ID=?");
        ResultSet rs;

        statement.setLong(1,id);

        rs = statement.executeQuery();
        String naziv = "";
        String vrijednost = "";

        while(rs.next()){

            naziv = rs.getString("NAZIV");
            vrijednost = rs.getString("VRIJEDNOST");

        }

        Simptom simptom = new Simptom(naziv,vrijednost,id);

        closeConnectionToDatabase(veza);

        return simptom;

    }

    public static void spremiNoviSimptom(Simptom simptom) throws SQLException {

        Connection connection = connectToDatabase();

        PreparedStatement statement = connection
                .prepareStatement("INSERT INTO SIMPTOM(NAZIV,VRIJEDNOST) VALUES(?,?)");

        statement.setString(1, simptom.getNaziv());
        statement.setString(2, simptom.getVrijednost());

        statement.executeUpdate();

        closeConnectionToDatabase(connection);

    }

    public static List<Bolest> dohvatiSveBolesti() throws SQLException {

        Connection connection = connectToDatabase();

        List<Bolest> bolesti = new ArrayList<>();

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM BOLEST");

        while(rs.next()){

            String naziv = rs.getString("NAZIV");
            Boolean isVirus = rs.getBoolean("VIRUS");
            Long id = rs.getLong("ID");

            List<Simptom> simptomiBolesti = new ArrayList<>();

            PreparedStatement statementSimptomi = connection.
                    prepareStatement("SELECT * FROM SIMPTOM INNER JOIN BOLEST_SIMPTOM " +
                            "ON SIMPTOM_ID=ID where BOLEST_ID=?");

            statementSimptomi.setLong(1,id);

            ResultSet rsSimptomi = statementSimptomi.executeQuery();

            while(rsSimptomi.next()){

                Long idSimptoma = rsSimptomi.getLong("ID");
                Simptom simptom =dohvatiSimptomById(idSimptoma);
                simptomiBolesti.add(simptom);

            }


            if(isVirus){

                bolesti.add(new Virus(naziv,simptomiBolesti,id));

            }else{

                bolesti.add(new Bolest(naziv,simptomiBolesti,id));

            }

        }

        closeConnectionToDatabase(connection);

        return bolesti;

    }

    public static List<Virus> dohvatiViruse() {

        List<Bolest> sveZaraze = null;
        try {
            sveZaraze = dohvatiSveBolesti();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        List<Virus> virusi = new ArrayList<>();

        for(Bolest b : sveZaraze)
            if(b instanceof Virus)
                virusi.add((Virus)b);

        return virusi;

    }

    public static List<Bolest> dohvatiBolesti(){

        List<Bolest> sveZaraze = null;
        try {
            sveZaraze = dohvatiSveBolesti();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        List<Bolest> bolesti = new ArrayList<>();

        for(Bolest b : sveZaraze)
            if(!(b instanceof Virus))
                bolesti.add(b);

        return bolesti;

    }

    public static Bolest dohvatiBolestById(Long id) throws SQLException {

        Connection connection = connectToDatabase();
        Bolest bolest = null;

        PreparedStatement statement = connection.
                prepareStatement("SELECT * FROM BOLEST WHERE ID=?");

        statement.setLong(1,id);
        ResultSet rs = statement.executeQuery();

        while (rs.next()){

            String naziv = rs.getString("NAZIV");
            List<Simptom> simptomiBolesti = new ArrayList<>();


            PreparedStatement statementSimptomi = connection.
                    prepareStatement("SELECT * FROM SIMPTOM INNER JOIN BOLEST_SIMPTOM " +
                            "ON SIMPTOM_ID=ID where BOLEST_ID=?");

            statementSimptomi.setLong(1,id);

            ResultSet rsSimptomi = statementSimptomi.executeQuery();

            while(rsSimptomi.next()){

                Long idSimptoma = rsSimptomi.getLong("ID");
                Simptom simptom =dohvatiSimptomById(idSimptoma);
                simptomiBolesti.add(simptom);

            }

            bolest = new Bolest(naziv,simptomiBolesti,id);

        }

        //System.out.println(bolest);
        closeConnectionToDatabase(connection);

        return bolest;

    }

    public static void spremiNovuBolest(Bolest bolest) throws SQLException {

        Connection connection = connectToDatabase();

        //connection.setAutoCommit(false);

        PreparedStatement statement = connectToDatabase().
                prepareStatement("INSERT INTO BOLEST(NAZIV,VIRUS) VALUES(?,?)",
                        Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, bolest.getNaziv());

        if(bolest instanceof Virus)
            statement.setBoolean(2,true);
        else
            statement.setBoolean(2,false);

        statement.execute();
        ResultSet rs = statement.getGeneratedKeys();
        Long id = Long.valueOf(0);

        ////////////////////////////////////////////////////////////////////////////
        while (rs.next()){

            id = rs.getLong("ID");
            System.out.println("Id za novu bolest " + id);

        }



        PreparedStatement statementSimptomi = connection.
                prepareStatement("INSERT INTO BOLEST_SIMPTOM(BOLEST_ID,SIMPTOM_ID) " +
                        "VALUES(?,?)");


        for(Simptom s : bolest.getSimptomi()){

            //statementSimptomi.setLong(1,bolest.getId());
            statementSimptomi.setLong(1,id);
            statementSimptomi.setLong(2,s.getId());

            statementSimptomi.executeUpdate();

        }

        /*connection.commit();

        connection.setAutoCommit(true);*/

        closeConnectionToDatabase(connection);

    }

    public static List<String> getVrijednosti() throws SQLException {

        Connection connection = connectToDatabase();

        List<String> vrijednosti = new ArrayList<>();

        Statement statement = connectToDatabase().createStatement();
        ResultSet rs = statement.executeQuery("SELECT DISTINCT VRIJEDNOST FROM SIMPTOM");

        while(rs.next()){

            vrijednosti.add(rs.getString("VRIJEDNOST"));

        }

        //System.out.println(vrijednosti);

        closeConnectionToDatabase(connection);

        return vrijednosti;

    }

    public static List<Zupanija> dohvatiSveZupanije() throws SQLException {

        Connection connestion = connectToDatabase();

        List<Zupanija> zupanije = new ArrayList<>();

        Statement statement = connestion.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM ZUPANIJA");

        while (rs.next()){

            String naziv = rs.getString("NAZIV");
            Integer brStanovnika = rs.getInt("BROJ_STANOVNIKA");
            Integer brZarazenihStanovnika = rs.getInt("BROJ_ZARAZENIH_STANOVNIKA");
            Long id = rs.getLong("ID");

            Zupanija zupanija = new Zupanija(naziv,brStanovnika,brZarazenihStanovnika,id);
            zupanije.add(zupanija);

        }

        closeConnectionToDatabase(connestion);
        //System.out.println(zupanije);

        return  zupanije;

    }

    public static Zupanija dohvatiZupanijuById(Long id) throws SQLException {

        Connection connection = connectToDatabase();

        Zupanija zupanija = null;

        PreparedStatement statement = connection.
                prepareStatement("SELECT * FROM ZUPANIJA WHERE ID=?");

        statement.setLong(1,id);
        ResultSet rs = statement.executeQuery();

        while (rs.next()){

            String naziv = rs.getString("NAZIV");
            Integer brStanovnika = rs.getInt("BROJ_STANOVNIKA");
            Integer brZarazenihStanovnika = rs.getInt("BROJ_ZARAZENIH_STANOVNIKA");

            zupanija = new Zupanija(naziv,brStanovnika,brZarazenihStanovnika,id);

        }

        closeConnectionToDatabase(connection);

        return zupanija;

    }

    public static void spremiNovuZupaniju(Zupanija zupanija) throws SQLException {

        Connection connection = connectToDatabase();

        PreparedStatement statement = connection.
                prepareStatement("INSERT INTO" +
                        " ZUPANIJA(NAZIV,BROJ_STANOVNIKA,BROJ_ZARAZENIH_STANOVNIKA) VALUES(?,?,?)");

        statement.setString(1, zupanija.getNaziv());
        statement.setInt(2,zupanija.getBrojStanovnika());
        statement.setInt(3,zupanija.getBrZarazenihOsoba());

        statement.executeUpdate();

        closeConnectionToDatabase(connection);

    }


    public static List<Osoba> dohvatiSveOsobe() throws SQLException {

        List<Osoba> osobe = new ArrayList<>();

        Connection connection = connectToDatabase();

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM OSOBA");
        PreparedStatement statementKontakti = connection.
                prepareStatement("SELECT * FROM KONTAKTIRANE_OSOBE WHERE OSOBA_ID=?");

        while (rs.next()){

            String ime = rs.getString("IME");
            String prezime = rs.getString("PREZIME");
            LocalDate datumRodenja = rs.getDate("DATUM_RODJENJA").toLocalDate();
            Zupanija zupanija = dohvatiZupanijuById(rs.getLong("ZUPANIJA_ID"));
            Bolest bolest = dohvatiBolestById(rs.getLong("BOLEST_ID"));
            Long id = rs.getLong("ID");


            osobe.add(new Osoba(ime,id,prezime,zupanija,bolest,datumRodenja));

        }

        rs = statement.executeQuery("SELECT * FROM OSOBA");

        while (rs.next()){

            List<Osoba> kontakti = new ArrayList<>();
            Long id = rs.getLong("ID");

            if(osobe.size()==0)
                kontakti = null;


            else{

                statementKontakti.setLong(1,id);
                ResultSet rsKontakti = statementKontakti.executeQuery();

                while (rsKontakti.next()){

                    Long idKontakta = rsKontakti.getLong("KONTAKTIRANA_OSOBA_ID");

                    //kontakti.add(osobe.get((int) (idKontakta-1)));
                    kontakti.add(getOsobaWithIdFromList(osobe,idKontakta));//////

                }

                //osobe.get((int) (id-1)).setKontaktiraneOsobe(kontakti);
                getOsobaWithIdFromList(osobe,id).setKontaktiraneOsobe(kontakti);

            }
            //System.out.println(kontakti);


        }
        //System.out.println(osobe);

        closeConnectionToDatabase(connection);
        return osobe;

    }

    public static Osoba getOsobaFromListWithId(List<Osoba> osobe,Long id){

        for(Osoba o : osobe)
            if(o.getId().equals(id))
                return o;

        return null;

    }

    public static Osoba getOsobaWithIdFromList(List<Osoba> osobe,Long id){

        Osoba osoba = null;

        for(Osoba o : osobe)
            if(o.getId().equals(id))
                osoba = o;

        //System.out.println(osoba);
        return  osoba;

    }

    private static Osoba dohvatiOsobuById(long id) throws SQLException {

        List<Osoba> listaOsoba = dohvatiSveOsobe();

        Osoba osoba = getOsobaWithIdFromList(listaOsoba,id);

        return osoba;

    }

    public static void spremiNovuOsobu(Osoba osoba) throws SQLException {

        Connection connection = connectToDatabase();

        //connection.setAutoCommit(false);

        PreparedStatement statement = connection.
                prepareStatement("INSERT INTO " +
                        "OSOBA(IME,PREZIME,DATUM_RODJENJA,ZUPANIJA_ID,BOLEST_ID) " +
                        "VALUES(?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);

        statement.setString(1,osoba.getIme());
        statement.setString(2, osoba.getPrezime());
        statement.setDate(3, Date.valueOf(osoba.getDatumRodenja()));
        statement.setLong(4,osoba.getZupanija().getId());
        statement.setLong(5,osoba.getZarazenBolescu().getId());

        statement.execute();

        ResultSet rs = statement.getGeneratedKeys();
        Long id = Long.valueOf(0);

        while (rs.next()){

            id = rs.getLong("ID");

        }

        PreparedStatement statementKontakti = connection.
                prepareStatement("INSERT INTO " +
                        "KONTAKTIRANE_OSOBE(OSOBA_ID,KONTAKTIRANA_OSOBA_ID)" +
                        "VALUES(?,?)");

        if(osoba.getKontaktiraneOsobe()!=null)
            for(Osoba o : osoba.getKontaktiraneOsobe())
            {

                statementKontakti.setLong(1,id);
                statementKontakti.setLong(2,o.getId());
                statementKontakti.executeUpdate();

            }

        //connection.commit();

        //connection.setAutoCommit(true);

        closeConnectionToDatabase(connection);

    }
}
