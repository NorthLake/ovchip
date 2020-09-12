import model.Adres;
import model.OVChipkaart;
import model.Reiziger;
import persistence.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CRUDovChipkaart {
    private static Connection connection;
    public static void main(String[] args) throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip", "ovchip", "ovchip");
        OVChipkaartDAO odao = new OVChipkaartDAOPsql(connection);
        ReizigerDAO rdao = new ReizigerDAOPsql(connection);
        AdresDAO adao = new AdresDAOPsql(connection);
        odao.setReizigerDAO(rdao);
        rdao.setAdresDAO(adao);
        adao.setReizigerDAO(rdao);
        testOVChipkaartDAO(odao);
    }

    /**
     * P4. OVChipkaart DAO: persistentie van twee klassen met een één-op-veel-relatie
     *
     * Deze methode test de CRUD-functionaliteit van de OVChipkaart DAO
     *
     * @throws SQLException -
     */
    private static void testOVChipkaartDAO(OVChipkaartDAO odao) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        // Haal alle reizigers op uit de database
        List<OVChipkaart> ovChipkaarten = odao.findAll();
        System.out.println("[Test] OVChipkaart.findAll() geeft de volgende OV-chipkaarten:");
        for (OVChipkaart o : ovChipkaarten) {
            System.out.println(o);
        }
        System.out.println();

        Reiziger reiziger = new Reiziger(6, "S", null, "Boers", LocalDate.parse("1981-03-14"));
        Adres adres = new Adres(6, "1234AB", "56", "Hoofdstraat", "Utrecht");
        reiziger.setAdres(adres);
        adres.setReiziger(reiziger);
        if (!new ReizigerDAOPsql(connection).save(reiziger) || !new AdresDAOPsql(connection).save(adres))
            System.out.println("Setup mislukt");

        // Maak een nieuwe OV-chipkaart aan en persisteer deze in de database
        OVChipkaart ovChipkaart = new OVChipkaart(25831, LocalDate.parse("2020-10-15"), 1, 11.50f, reiziger);
        System.out.print("[Test] Eerst " + ovChipkaarten.size() + " OV-chipkaarten, na OVChipkaartDAO.save() ");
        odao.save(ovChipkaart);
        ovChipkaarten = odao.findAll();
        System.out.println(ovChipkaarten.size() + " OV-chipkaarten\n");

        // Haal alle OV-chipkaarten op uit de database die eigendom zijn van reiziger met id 6
        List<OVChipkaart> ovChipkaartenByReiziger = odao.findByReiziger(reiziger);
        System.out.println("[Test] OVChipkaartDAO.findByReiziger(reiziger) geeft de volgende OV-chipkaarten:");
        for (OVChipkaart o : ovChipkaartenByReiziger) {
            System.out.println(o);
        }
        System.out.println();

        // Haal uit de database de ovChipkaart met kaart_nummer 25831 op
        ovChipkaart = odao.findByKaartNummer(25831);
        System.out.println("[Test] OVChipkaartDAO.findByKaartNummer(25831) geeft de volgende reiziger:");
        System.out.println(ovChipkaart);
        System.out.println();

        // Verander het saldo van de OV-chipkaart met kaart_nummer 25831 naar van Dijk
        ovChipkaart.setSaldo(8.50f);
        if (!odao.update(ovChipkaart)) {
            System.out.println("[Test] OVChipkaartDAO.update(ovChipkaart) geeft geen true terug");
        }
        OVChipkaart ovChipkaartByIdUpdated = odao.findByKaartNummer(25831);
        System.out.println("[Test] OVChipkaartDAO.findByKaartNummer(25831) geeft na het wijzigen de volgende OV-chipkaart:");
        System.out.println(ovChipkaartByIdUpdated);
        System.out.println();

        // Verwijder de eerder aangemaakte OV-chipkaart uit de database
        System.out.print("[Test] Eerst " + ovChipkaarten.size() + " OV-chipkaarten, na OVChipkaartDAO.delete() ");
        odao.delete(ovChipkaart);
        ovChipkaarten = odao.findAll();
        System.out.println(ovChipkaarten.size() + " OV-chipkaarten\n");

        if (!new AdresDAOPsql(connection).delete(adres) || !new ReizigerDAOPsql(connection).delete(reiziger))
            System.out.println("Cleanup mislukt");
    }
}
