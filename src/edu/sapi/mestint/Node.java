package edu.sapi.mestint;

/**
 * @author
 */
public class Node {
    private int[][] puzzle;
    private int pathCost = 0;
    private int heuristicCost = 0;

    public Node(int[][] puzzle) {
        this.puzzle = puzzle;
    }

    public int getFinalCost() {
        return pathCost + heuristicCost;
    }

    public int getPathCost() {
        return pathCost;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    public int getHeuristicCost() {
        return heuristicCost;
    }

    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
    }

}
