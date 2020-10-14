package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OVChipkaart {
    private final int kaartNummer;
    private LocalDate geldigTot;
    private int klasse;
    private float saldo;
    private Reiziger reiziger;
    private Set<Product> producten = new HashSet<>();

    public OVChipkaart(int kaartNummer, LocalDate geldigTot, int klasse, float saldo, Reiziger reiziger) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }

    @Override
    public String toString() {
        BigDecimal saldoAfgerond = new BigDecimal(Float.toString(saldo)).setScale(2, RoundingMode.HALF_UP);
        return "OVChipkaart #" + kaartNummer + ", geldig tot " + geldigTot + ", klasse " + klasse + ", €" + saldoAfgerond;
    }

    public boolean addProduct(Product product) {
        return producten.add(product);
    }

    public boolean removeProduct(Product product) {
        return producten.remove(product);
    }

    //region getters
    public int getKaartNummer() {
        return kaartNummer;
    }

    public LocalDate geldigTot() {
        return geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public float getSaldo() {
        return saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public Set<Product> getProducten() {
        return Collections.unmodifiableSet(producten);
    }

    //endregion

    //region setters
    public void setGeldigTot(LocalDate geldigTot) {
        this.geldigTot = geldigTot;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }
    //endregion
}
