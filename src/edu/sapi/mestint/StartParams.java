package edu.sapi.mestint;

/**
 * @author
 */
public class StartParams {
    private String readFromFile = "";
    private boolean solseq;
    private boolean pcost;
    private boolean nvisited;
    private int h;

    public String getReadFromFile() {
        return readFromFile;
    }

    public void setReadFromFile(String readFromFile) {
        this.readFromFile = readFromFile;
    }

    public boolean isSolseq() {
        return solseq;
    }

    public void setSolseq(boolean solseq) {
        this.solseq = solseq;
    }

    public boolean isPcost() {
        return pcost;
    }

    public void setPcost(boolean pcost) {
        this.pcost = pcost;
    }

    public boolean isNvisited() {
        return nvisited;
    }

    public void setNvisited(boolean nvisited) {
        this.nvisited = nvisited;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

}
