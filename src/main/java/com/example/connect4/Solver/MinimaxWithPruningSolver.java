package com.example.connect4.Solver;

import com.example.connect4.State;
import com.example.connect4.heuristics.IHeuristic;
import com.example.connect4.minimaxTree.MinimaxTree;
import com.example.connect4.minimaxTree.Node;

public class MinimaxWithPruningSolver implements ISolver {

    private final IHeuristic heuristic;
    private int bestChoice;
    private State choice;
    private final MinimaxTree minimaxTree;
    private final int maxLevel;
    private Node[] finalPath;

    public MinimaxWithPruningSolver(IHeuristic heuristic, int maxLevel, State initialState) {
        this.maxLevel = maxLevel;
        this.minimaxTree = new MinimaxTree(new Node(null, initialState));
        this.heuristic = heuristic;
        this.finalPath = new Node[maxLevel + 1];
    }

    public int minValue(Node parentNode, int alpha, int beta, int currLevel) {
        if (currLevel == this.maxLevel) return heuristic.getHeuristic(parentNode.getState());
        int currStateValue = Integer.MAX_VALUE, nextStatesValue;
        State[] states = parentNode.getState().generateChildStates();
        for (State nextState : states) {
            Node child = new Node(parentNode, nextState);
            parentNode.addChild(child);
            nextStatesValue = maxValue(child, alpha, beta, currLevel + 1);
            child.setHeuristics(nextStatesValue);
            if (nextStatesValue < currStateValue) {
                currStateValue = nextStatesValue;
            }
            if (nextStatesValue <= alpha) return currStateValue;
            if (nextStatesValue < beta) beta = nextStatesValue;
        }
        return currStateValue;
    }

    /*
    Node temp = finalPath[currLevel + 1];
    if(temp == null) finalPath[currLevel + 1] = child;
    else if (temp.getHeuristics() < child.getHeuristics()) finalPath[currLevel + 1] = child;
    */
    public int maxValue(Node parentNode, int alpha, int beta, int currLevel) {
        if (currLevel == this.maxLevel) return heuristic.getHeuristic(parentNode.getState());
        int currStateValue = Integer.MIN_VALUE, nextStatesValue;
        State[] states = parentNode.getState().generateChildStates();
        for (State nextState : states) {
            Node child = new Node(parentNode, nextState);
            parentNode.addChild(child);
            nextStatesValue = minValue(child, alpha, beta, currLevel + 1);
            child.setHeuristics(nextStatesValue);
            if (nextStatesValue > currStateValue) {
                currStateValue = nextStatesValue;
            }
            if (nextStatesValue >= beta) return currStateValue;
            if (nextStatesValue > alpha) alpha = nextStatesValue;
        }
        return currStateValue;
    }

    public void solve() {
        finalPath[0] = minimaxTree.getRoot();
        bestChoice = maxValue(minimaxTree.getRoot(), Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        minimaxTree.getRoot().setHeuristics(bestChoice);
    }

    @Override
    public MinimaxTree getTree() {
        return minimaxTree;
    }

    @Override
    public int getChangedColumn() {
        return 0;
    }

    @Override
    public int getBestChoice() {
        return bestChoice;
    }
}
