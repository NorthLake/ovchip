import model.Adres;
import model.Reiziger;
import persistence.AdresDAO;
import persistence.AdresDAOPsql;
import persistence.ReizigerDAO;
import persistence.ReizigerDAOPsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CRUDAdres {
    private static Connection connection;
    public static void main(String[] args) throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip", "ovchip", "ovchip");
        ReizigerDAO reizigerDAO = new ReizigerDAOPsql(connection);
        AdresDAO adresDAO = new AdresDAOPsql(connection);
        adresDAO.setReizigerDAO(reizigerDAO);
        reizigerDAO.setAdresDAO(adresDAO);
        testAdresDAO(adresDAO);
    }

    /**
     * P3. Adres DAO: persistentie van een klasse met een één-op-één-relatie
     *
     * Deze methode test de CRUD-functionaliteit van de Adres DAO
     *
     * @throws SQLException -
     */
    private static void testAdresDAO(AdresDAO adao) throws SQLException {
        ReizigerDAO rdao = new ReizigerDAOPsql(connection);
        System.out.println("\n---------- Test AdresDAO -------------");

        // Haal alle adressen op uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // Haal alle adressen op uit de database die als stad Utrecht hebben
        List<Adres> adressenUtrecht = adao.findByStad("Utrecht");
        System.out.println("[Test] AdresDAO.findByStad(\"Utrecht\") geeft de volgende adressen:");
        for (Adres a : adressenUtrecht) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuw adres aan en persisteer deze in de database
        Adres adres = new Adres(6, "1234AB", "56", "Hoofdstraat", "Utrecht");
        Reiziger reiziger = new Reiziger(6, "S", null, "Boers", LocalDate.parse("1981-03-14"));
        adres.setReiziger(reiziger);
        reiziger.setAdres(adres);
        if (!rdao.save(reiziger))
            System.out.println("Kon reiziger niet opslaan");
        System.out.print("[Test] Eerst " + adressen.size() + " reizigers, na AdresDAO.save() ");
        adao.save(adres);
        rdao.update(reiziger);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " reizigers\n");

        // Haal uit de database het adres met id 6 op
        adres = adao.findById(6);
        System.out.println("[Test] AdresDAO.findById(6) geeft het volgende adres:");
        System.out.println(adres);
        System.out.println();

        // Verander postcode en huisnummer van de stad met id 6 naar Zijstraat 78
        adres.setPostcode("2345BC");
        adres.setHuisnummer("78");
        if (!adao.update(adres))
            System.out.println("[Test] AdresDAO.update(Adres) geeft geen true terug");
        Adres adresByIdUpdated = adao.findById(6);
        System.out.println("[Test] AdresDAO.findById(6) geeft na het wijzigen van postcode/huisnummer het volgende adres:");
        System.out.println(adresByIdUpdated);
        System.out.println();

        // Verwijder het eerder aangemaakte adres uit de database
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.delete() ");
        adao.delete(adres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");

        rdao.delete(reiziger);
    }
}
