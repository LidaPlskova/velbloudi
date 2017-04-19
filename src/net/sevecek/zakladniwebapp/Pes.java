package net.sevecek.zakladniwebapp;

public class Pes {

    private Integer velikost;
    private Integer srst;
    private Integer usi;
    private Integer roky;

    public Pes(Integer velikost, Integer srst, Integer usi, Integer roky) {
        this.velikost = velikost;
        this.srst = srst;
        this.usi = usi;
        this.roky = roky;
    }

    public Pes() {
    }

    public Integer getVelikost() {
        return velikost;
    }

    public void setVelikost(Integer velikost) {
        this.velikost = velikost;
    }

    public Integer getSrst() {
        return srst;
    }

    public void setSrst(Integer srst) {
        this.srst = srst;
    }

    public Integer getUsi() {
        return usi;
    }

    public void setUsi(Integer usi) {
        this.usi = usi;
    }

    public Integer getRoky() {
        return roky;
    }

    public void setRoky(Integer roky) {
        this.roky = roky;
    }

    @Override
    public String toString() {
        return "Pes{" +
                "velikost=" + velikost +
                ", srst=" + srst +
                ", usi=" + usi +
                ", roky=" + roky +
                '}';
    }
}
