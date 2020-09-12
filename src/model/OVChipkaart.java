package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class OVChipkaart {
    private int kaartNummer;
    private LocalDate geldigTot;
    private int klasse;
    private float saldo;
    private Reiziger reiziger;

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
        return "OVChipkaart {#" + kaartNummer + ", geldig tot " + geldigTot + ", reist " + klasse + ", €" + saldoAfgerond + ", eigendom van " + reiziger + "}";
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
    //endregion

    //region setters
    public void setKaartNummer(int kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

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
