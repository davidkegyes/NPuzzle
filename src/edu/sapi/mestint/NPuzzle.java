package edu.sapi.mestint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NPuzzle {

    private static final Node DEFAULT_GOAL = new Node(new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}});
    private static int SIZE = 3;
    private StartParams startParams;

    public NPuzzle(StartParams startParams) {
        this.startParams = startParams;
    }

    public static void main(String[] args) {
        new NPuzzle(getStartParams(args)).solve();
    }

    private static StartParams getStartParams(String[] args) {
        StartParams startParams = new StartParams();
        if (args.length != 0) {
            int i = 0;
            while (i < args.length) {
                switch (args[i]) {
                    case "-input": {
                        startParams.setReadFromFile(args[++i]);
                        break;
                    }
                    case "-solseq": {
                        startParams.setSolseq(true);
                        break;
                    }
                    case "-pcost": {
                        startParams.setPcost(true);
                        break;
                    }

                    case "-nvisited": {
                        startParams.setNvisited(true);
                        break;
                    }

                    case "-h": {
                        startParams.setH(Integer.parseInt(args[++i]));
                        break;
                    }
                }
                i++;
            }
        }
        return startParams;
    }

    public void solve() {
        Node startState = getStartState();
        System.out.println("Start node");
        printState(startState);
        System.out.println("Goal to reach");
        printState(DEFAULT_GOAL);

        List<Node> openList = new ArrayList<>();
        List<Node> closedList = new ArrayList<>();

        startState.setHeuristicCost(getHeuristic(startState, DEFAULT_GOAL));
        openList.add(startState);
        int visitedStates = 1;
        while (!openList.isEmpty()) {
            Node currentState = getLowestPathCostState(openList);
            closedList.add(currentState);
            openList.remove(currentState);
            visitedStates++;
            if (isPuzzleEqual(currentState.getPuzzle(), DEFAULT_GOAL.getPuzzle())) {
                printSolution(currentState, visitedStates);
                return;
            }
            if (startParams.isSolseq()) {
                System.out.println("Sequence");
                printState(currentState);
            }
            for (SlideDirection direction : SlideDirection.values()) {
                Node child = slide(currentState, direction);
                if (child == null) {
                    continue;
                }
                child.setHeuristicCost(getHeuristic(child, DEFAULT_GOAL));

                Node X = findSimilarPuzzleState(child, openList);
                if (X != null) {
                    if (child.getFinalCost() <= X.getFinalCost()) {
                        continue;
                    } else {
                        openList.remove(X);
                    }
                }
                Node Y = findSimilarPuzzleState(child, closedList);
                if (Y != null) {
                    if (child.getFinalCost() <= Y.getFinalCost()) {
                        continue;
                    } else {
                        closedList.remove(Y);
                    }
                }
                openList.add(child);
            }
        }
    }

    private boolean isPuzzleEqual(int[][] puzzle1, int[][] puzzle2) {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (puzzle1[i][j] != puzzle2[i][j])
                    return false;
        return true;
    }

    private Node findSimilarPuzzleState(Node node, List<Node> nodes) {
        for (Node s : nodes) {
            if (isPuzzleEqual(node.getPuzzle(), s.getPuzzle())) {
                return s;
            }
        }
        return null;
    }

    private Node slide(Node parent, SlideDirection direction) {
        int[][] puzzle = copyPuzzle(parent.getPuzzle());
        Position p = getZeroPosition(parent.getPuzzle());
        switch (direction) {
            case UP: {
                if (p.getI() >= 1) {
                    int up = puzzle[p.getI() - 1][p.getJ()];
                    puzzle[p.getI()][p.getJ()] = up;
                    puzzle[p.getI() - 1][p.getJ()] = 0;
                } else {
                    return null;
                }
                break;
            }
            case DOWN: {
                if (p.getI() < 2) {
                    int down = puzzle[p.getI() + 1][p.getJ()];
                    puzzle[p.getI()][p.getJ()] = down;
                    puzzle[p.getI() + 1][p.getJ()] = 0;
                } else {
                    return null;
                }
                break;
            }
            case LEFT: {
                if (p.getJ() >= 1) {
                    int left = puzzle[p.getI()][p.getJ() - 1];
                    puzzle[p.getI()][p.getJ()] = left;
                    puzzle[p.getI()][p.getJ() - 1] = 0;
                } else {
                    return null;
                }
                break;
            }
            case RIGHT: {
                if (p.getJ() < 2) {
                    int right = puzzle[p.getI()][p.getJ() + 1];
                    puzzle[p.getI()][p.getJ()] = right;
                    puzzle[p.getI()][p.getJ() + 1] = 0;
                } else {
                    return null;
                }
                break;
            }
        }
        Node node = new Node(puzzle);
        node.setPathCost(parent.getPathCost() + 1);

        return node;
    }

    private int[][] copyPuzzle(int[][] puzzle) {
        int[][] newPuzzle = new int[puzzle.length][];
        for (int i = 0; i < puzzle.length; i++) {
            newPuzzle[i] = new int[puzzle[i].length];
            for (int j = 0; j < puzzle[i].length; j++)
                newPuzzle[i][j] = puzzle[i][j];
        }
        return newPuzzle;
    }

    private Position getZeroPosition(int[][] puzzle) {
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                if (puzzle[i][j] == 0)
                    return new Position(i, j);
            }
        }
        return null;
    }

    public int getHeuristic(Node start, Node goal) {
        if (startParams.getH() == 2) {
            return manhattanDistanceHeuristicValue(start.getPuzzle(), goal.getPuzzle());
        }
        return badPositionHeuristicValue(start.getPuzzle(), goal.getPuzzle());
    }

    public int manhattanDistance(int[][] startPuzzle, int[][] goalPuzzle, int x, int y) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (startPuzzle[x][y] == goalPuzzle[i][j]) {
                    return Math.abs(x - i) + Math.abs(y - j);
                }
            }
        }
        return 0;
    }

    private int manhattanDistanceHeuristicValue(int[][] startPuzzle, int[][] goalPuzzle) {
        int val = 0;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (startPuzzle[i][j] != 0) {
                    val += manhattanDistance(startPuzzle, goalPuzzle, i, j);
                }
            }
        }
        return val;
    }

    private int badPositionHeuristicValue(int[][] startPuzzle, int[][] goalPuzzle) {
        int val = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (startPuzzle[i][j] != goalPuzzle[i][j] && startPuzzle[i][j] != 0) {
                    val++;
                }
            }
        }
        return val;

    }

    private void printSolution(Node node, int visitedStates) {
        System.out.println("Puzzle solved");
//        printState(node);
        if (startParams.isNvisited()) {
            System.out.println("Visited node count: " + visitedStates);
        }
        if (startParams.isPcost()) {
            System.out.println("Path cost: " + node.getPathCost());
        }

    }

    private Node getLowestPathCostState(List<Node> openList) {
        Node lcState = null;
        for (Node node : openList) {
            if (lcState == null) {
                lcState = node;
                continue;
            }
            if ((lcState.getFinalCost() > node.getFinalCost()) || (lcState.getFinalCost() > node.getFinalCost() && lcState.getHeuristicCost() > node.getHeuristicCost())) {
                lcState = node;
            }
        }
        return lcState;
    }

    private Node getStartState() {
        Node startState = null;
        if (startParams.getReadFromFile() != "") {
            startState = readStateFromFile(startParams.getReadFromFile());
        } else {
            startState = readFromConsole();
        }
        return startState;
    }

    private Node readFromConsole() {
        System.out.println("Pleas enter the puzzle!");
        Scanner reader = new Scanner(System.in);
        int[][] puzzle = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                puzzle[i][j] = reader.nextInt();
            }
        }
        return new Node(puzzle);
    }

    private Node readStateFromFile(String file) {
        try (Scanner reader = new Scanner(new File(file))) {
            int[][] puzzle = new int[SIZE][SIZE];
            for (int i = 0; i < SIZE; ++i) {
                for (int j = 0; j < SIZE; ++j) {
                    puzzle[i][j] = reader.nextInt();
                }
            }
            return new Node(puzzle);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void printState(Node node) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(node.getPuzzle()[i][j] + "\t");
            }
            System.out.println();
        }
    }

    enum SlideDirection {
        UP, DOWN, LEFT, RIGHT
    }

}
