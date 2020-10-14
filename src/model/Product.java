package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Product {
    private final int nummer;
    private String naam;
    private String beschrijving;
    private float prijs;
    private Set<OVChipkaart> kaarten = new HashSet<>();

    public Product(int nummer, String naam, String beschrijving, float prijs) {
        this.nummer = nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public boolean addKaart(OVChipkaart ovChipkaart) {
        return kaarten.add(ovChipkaart);
    }

    public boolean removeKaart(OVChipkaart ovChipkaart) {
        return kaarten.remove(ovChipkaart);
    }

    @Override
    public String toString() {
        BigDecimal prijsAfgerond = new BigDecimal(Float.toString(prijs)).setScale(2, RoundingMode.HALF_UP);
        return "Product #" + nummer + " " + naam + ": \"" + beschrijving + "\", €" + prijsAfgerond;
    }

    //region getters
    public int getProductNummer() {
        return nummer;
    }

    public String getNaam() {
        return naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public float getPrijs() {
        return prijs;
    }

    public Set<OVChipkaart> getKaarten() {
        return Collections.unmodifiableSet(kaarten);
    }

    //endregion

    //region setters
    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public void setPrijs(float prijs) {
        this.prijs = prijs;
    }
    //endregion
}
