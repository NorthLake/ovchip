import model.Adres;
import model.OVChipkaart;
import model.Product;
import model.Reiziger;
import persistence.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

public class CRUDProduct {
    private static Connection connection;
    public static void main(String[] args) throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip", "ovchip", "ovchip");
        OVChipkaartDAO odao = new OVChipkaartDAOPsql(connection);
        ReizigerDAO rdao = new ReizigerDAOPsql(connection);
        AdresDAO adao = new AdresDAOPsql(connection);
        ProductDAO pdao = new ProductDAOPsql(connection);

        // Set die hele zooi
        odao.setReizigerDAO(rdao);
        rdao.setAdresDAO(adao);
        rdao.setOVChipkaartDAO(odao);
        adao.setReizigerDAO(rdao);
        pdao.setOVChipkaartDAO(odao);
        odao.setProductDAO(pdao);

        testOVChipkaartDAO(odao, pdao);
    }

    /**
     * P4. OVChipkaart DAO: persistentie van twee klassen met een één-op-veel-relatie
     *
     * Deze methode test de CRUD-functionaliteit van de OVChipkaart DAO
     *
     * @throws SQLException -
     */
    private static void testOVChipkaartDAO(OVChipkaartDAO odao, ProductDAO pdao) throws SQLException {
        System.out.println("\n---------- Test ProductDAO -------------");

        // Haal alle OV-chipkaarten op uit de database
        Set<OVChipkaart> ovChipkaarten = odao.findAll();
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende OV-chipkaarten:");
        for (OVChipkaart o : ovChipkaarten) {
            System.out.println(o);
        }
        System.out.println();

        // Haal alle producten op uit de database
        Set<Product> producten = pdao.findAll();
        System.out.println("[Test] ProductDAO.findAll() geeft de volgende producten:");
        for (Product p : producten) {
            System.out.println(p);
        }
        System.out.println();

        Reiziger reiziger = new Reiziger(6, "S", null, "Boers", LocalDate.parse("1981-03-14"));
        Adres adres = new Adres(6, "1234AB", "56", "Hoofdstraat", "Utrecht");
        reiziger.setAdres(adres);
        adres.setReiziger(reiziger);
        if (!new ReizigerDAOPsql(connection).save(reiziger) || !new AdresDAOPsql(connection).save(adres))
            System.out.println("Setup mislukt");

        // Maak een nieuwe OV-chipkaart en een nieuw product aan en persisteer deze in de database
        OVChipkaart ovChipkaart = new OVChipkaart(25831, LocalDate.parse("2020-10-15"), 1, 11.50f, reiziger);
        reiziger.addKaart(ovChipkaart);
        Product product = new Product(7, "Testproduct", "Dit is een testproduct.", 5f);
        System.out.print("[Test] Eerst " + producten.size() + " OV-chipkaarten en " + ovChipkaarten.size() + " producten, na OVChipkaartDAO.save() ");
        pdao.save(product);
        producten = pdao.findAll();
        System.out.println(producten.size() + " OV-chipkaarten en " + ovChipkaarten.size() + " producten\n");

        // Haal alle producten op uit de database die horen bij de OV-chipkaart met nummer 25831
        Set<Product> productenByOVChipkaart = pdao.findByOVChipKaart(ovChipkaart);
        System.out.println("[Test] ProductDAO.findByOVChipKaart(ovChipkaart) geeft de volgende producten:");
        for (Product p : productenByOVChipkaart) {
            System.out.println(p);
        }
        System.out.println();

        // Haal uit de database het product met product_nummer 7 op
        product = pdao.findByNummer(7);
        System.out.println("[Test] ProductDAO.findByNummer(7) geeft het volgende product:");
        System.out.println(product);
        System.out.println();

        // Verander de beschrijving van het product met nummer 7 naar Beschrijving veranderd
        product.setBeschrijving("Beschrijving veranderd");
        if (!pdao.update(product)) {
            System.out.println("[Test] ProductDAO.update(product) geeft geen true terug");
        }
        Product productByIdUpdated = pdao.findByNummer(7);
        System.out.println("[Test] ProductDAO.findByNummer(7) geeft na het wijzigen het volgende product:");
        System.out.println(productByIdUpdated);
        System.out.println();

        // Verwijder de eerder aangemaakte OV-chipkaart uit de database
        System.out.print("[Test] Eerst " + producten.size() + " producten, na Product.delete() ");
        pdao.delete(product);
        producten = pdao.findAll();
        System.out.println(producten.size() + " producten\n");

        if (!new AdresDAOPsql(connection).delete(adres) || !new ReizigerDAOPsql(connection).delete(reiziger))
            System.out.println("Cleanup mislukt");
    }
}
