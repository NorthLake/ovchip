package model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Reiziger {
    private final int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private LocalDate geboortedatum;
    private Adres adres;
    private Set<OVChipkaart> kaarten = new HashSet<>();

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, LocalDate geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    @Override
    public String toString() {
        String naam = tussenvoegsel == null
                ? voorletters + " " + achternaam
                : voorletters + " " + tussenvoegsel + " " + achternaam;
        return "Reiziger #" + id + " " + naam + ", geb. " + geboortedatum;
    }

    public boolean addKaart(OVChipkaart ovChipkaart) {
        ovChipkaart.setReiziger(this);
        return this.kaarten.add(ovChipkaart);
    }

    public boolean removeKaart(OVChipkaart ovChipkaart) {
        ovChipkaart.setReiziger(null);
        return this.kaarten.remove(ovChipkaart);
    }

    //region getters
    public Integer getId() {
        return id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public LocalDate getGeboortedatum() {
        return geboortedatum;
    }

    public Adres getAdres() {
        return adres;
    }

    public Set<OVChipkaart> getKaarten() {
        return Collections.unmodifiableSet(kaarten);
    }

    //endregion

    //region setters
    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public void setGeboortedatum(LocalDate geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }
    //endregion
}
