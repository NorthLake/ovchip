package model;

import java.time.LocalDate;

public class Reiziger {
    private final int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private LocalDate geboortedatum;
    private Adres adres;

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, LocalDate geboortedatum, Adres adres) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
        this.adres = adres;
    }

    @Override
    public String toString() {
        String naam;
        if (tussenvoegsel == null)
            naam = voorletters + " " + achternaam;
        else
            naam = voorletters + " " + tussenvoegsel + " " + achternaam;
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        if (element.getClassName().equals("model.Adres") && element.getMethodName().equals("toString"))
            return "Reiziger {#" + id + " " + naam + ", geb. " + geboortedatum + "}";
        else
            return "Reiziger {#" + id + " " + naam + ", geb. " + geboortedatum + ", " + adres + "}";
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
