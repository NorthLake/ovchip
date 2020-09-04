import model.Reiziger;
import persistence.ReizigerDAO;
import persistence.ReizigerDAOPsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CRUDReiziger {
    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip", "ovchip", "ovchip");
        ReizigerDAO reizigerDAO = new ReizigerDAOPsql(connection);
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
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", null, "Boers", LocalDate.parse(gbdatum), null);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Haal alle reizigers op uit de database die als geboortedatum 2002-12-03 hebben
        List<Reiziger> reizigersgb = rdao.findByGbdatum("2002-12-03");
        System.out.println("[Test] ReizigerDAO.findByGbdatum(\"2002-12-03\") geeft de volgende reizigers:");
        for (Reiziger r : reizigersgb) {
            System.out.println(r);
        }
        System.out.println();

        // Haal uit de database de reiziger met id 77 op
        sietske = rdao.findById(77);
        System.out.println("[Test] ReizigerDAO.findById(77) geeft de volgende reiziger:");
        System.out.println(sietske);
        System.out.println();

        // Verander de achternaam van de reiziger met id 77 naar van Dijk
        sietske.setTussenvoegsel("van");
        sietske.setAchternaam("Dijk");
        if (!rdao.update(sietske)) {
            System.out.println("[Test] ReizigerDAO.update(Reiziger) geeft geen true terug");
        }
        Reiziger reizigerByIdUpdated = rdao.findById(77);
        System.out.println("[Test] ReizigerDAO.findById(77) geeft na het wijzigen de volgende reiziger:");
        System.out.println(reizigerByIdUpdated);
        System.out.println();

        // Verwijder de eerder aangemaakte reiziger uit de database
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }
}
