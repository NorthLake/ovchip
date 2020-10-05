import model.Adres;
import model.Reiziger;
import persistence.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CRUDReiziger {
    private static Connection connection;
    public static void main(String[] args) throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip", "ovchip", "ovchip");
        ReizigerDAO reizigerDAO = new ReizigerDAOPsql(connection);
        AdresDAO adresDAO = new AdresDAOPsql(connection);
        OVChipkaartDAO ovChipDAO = new OVChipkaartDAOPsql(connection);
        adresDAO.setReizigerDAO(reizigerDAO);
        reizigerDAO.setAdresDAO(adresDAO);
        reizigerDAO.setOVChipkaartDAO(ovChipDAO);
        testReizigerDAO(reizigerDAO);
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException -
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        AdresDAO adao = new AdresDAOPsql(connection);
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        Reiziger sietske = new Reiziger(6, "S", null, "Boers", LocalDate.parse("1981-03-14"));
        Adres adres = new Adres(6, "1234AB", "56", "Hoofdstraat", "Utrecht");
        sietske.setAdres(adres);
        adres.setReiziger(sietske);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        if (!adao.save(adres))
            System.out.println("Kon adres niet opslaan");

        // Haal alle reizigers op uit de database die als geboortedatum 2002-12-03 hebben
        List<Reiziger> reizigersgb = rdao.findByGbdatum(LocalDate.of(2002, 12, 3));
        System.out.println("[Test] ReizigerDAO.findByGbdatum(LocalDate.of(2002, 12, 3)) geeft de volgende reizigers:");
        for (Reiziger r : reizigersgb) {
            System.out.println(r);
        }
        System.out.println();

        // Haal uit de database de reiziger met id 6 op
        sietske = rdao.findById(6);
        System.out.println("[Test] ReizigerDAO.findById(6) geeft de volgende reiziger:");
        System.out.println(sietske);
        System.out.println();

        // Verander de achternaam van de reiziger met id 6 naar van Dijk
        sietske.setTussenvoegsel("van");
        sietske.setAchternaam("Dijk");
        if (!rdao.update(sietske)) {
            System.out.println("[Test] ReizigerDAO.update(Reiziger) geeft geen true terug");
        }
        Reiziger reizigerByIdUpdated = rdao.findById(6);
        System.out.println("[Test] ReizigerDAO.findById(6) geeft na het wijzigen van tussenvoegsel/achternaam de volgende reiziger:");
        System.out.println(reizigerByIdUpdated);
        System.out.println();

        if (!adao.delete(adres))
            System.out.println("Kon adres niet verwijderen");

        // Verwijder de eerder aangemaakte reiziger uit de database
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }
}
