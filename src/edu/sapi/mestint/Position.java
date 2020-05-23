package edu.sapi.mestint;

import java.util.Objects;

/**
 * @author
 */
public class Position {
    private int i;
    private int j;

    public Position(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return getI() == position.getI() &&
                getJ() == position.getJ();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getI(), getJ());
    }
}
