package com.example.connect4.Solver;

import com.example.connect4.State;
import com.example.connect4.heuristics.IHeuristic;
import com.example.connect4.minimaxTree.MinimaxTree;
import com.example.connect4.minimaxTree.Node;

import java.util.HashMap;

public class MinimaxWithoutPruningSolver implements ISolver{
    private  IHeuristic heuristic;
    private int bestChoice;
    private int changedColumn;

    private State chosenState;
    private  MinimaxTree minimaxTree;
    private  int maxLevel;
    private  HashMap<Node, Integer> finalPath;
    private  Node[] pathToGoal;
   public void solve(IHeuristic heuristic, int maxLevel, State prevState, int col){
        this.maxLevel = maxLevel;
        this.minimaxTree = new MinimaxTree(new Node(null, getNewState(prevState, col)));
        this.heuristic = heuristic;
        this.finalPath = new HashMap<>();
        this.pathToGoal = new Node[maxLevel + 1];
        bestChoice = maxValue(minimaxTree.getRoot(),  0);
        minimaxTree.getRoot().setHeuristics(bestChoice);
        generatePathToGoal();
        generateChosenState();
        generateChangedColumn();
    }
    public State getNewState(State prevState, int col) {
        return prevState.generateChildStates()[col];
    }
    public int minValue(Node parentNode, int currLevel) {
        if (parentNode.getState().isEndState()) return parentNode.getState().getStateScore();
        if (currLevel == this.maxLevel) return heuristic.getHeuristic(parentNode.getState());
        int currStateValue = Integer.MAX_VALUE, nextStatesValue;
        State[] states = parentNode.getState().generateChildStates();
        for (int i = 0; i < states.length; i++) {
            State nextState = states[i];
            Node child = new Node(parentNode, nextState);
            parentNode.addChild(child);
            if (nextState != null) {
                nextStatesValue = maxValue(child, currLevel + 1);
                child.setHeuristics(nextStatesValue);
                if (nextStatesValue < currStateValue) {
                    currStateValue = nextStatesValue;
                    finalPath.put(parentNode, i);
                }
            }
        }
        return currStateValue;
    }
    public int maxValue(Node parentNode, int currLevel) {
        if (parentNode.getState().isEndState()) return parentNode.getState().getStateScore();
        if (currLevel == this.maxLevel) return heuristic.getHeuristic(parentNode.getState());
        int currStateValue = Integer.MIN_VALUE, nextStatesValue;
        State[] states = parentNode.getState().generateChildStates();
        for (int i = 0; i < states.length; i++) {
            State nextState = states[i];
            Node child = new Node(parentNode, nextState);
            parentNode.addChild(child);
            if (nextState != null) {
                nextStatesValue = minValue(child, currLevel + 1);
                child.setHeuristics(nextStatesValue);
                if (nextStatesValue > currStateValue) {
                    currStateValue = nextStatesValue;
                    finalPath.put(parentNode, i);
                }
            }
        }
        return currStateValue;
    }

    @Override
    public MinimaxTree getTree() {
        return minimaxTree;
    }

    @Override
    public int getChangedColumn() {
        return this.changedColumn;
    }

    @Override
    public int getBestChoice() {
        return bestChoice;
    }

    @Override
    public Node[] getPathToGoal() {
        return pathToGoal;
    }

    @Override
    public State getChosenState() {
        return this.chosenState;
    }

    public void generatePathToGoal() {
        int c = 0, index;
        Node currNode = minimaxTree.getRoot();
        while (finalPath.get(currNode) != null) {
            pathToGoal[c++] = currNode;
            index = finalPath.get(currNode);
            currNode = currNode.getChildren().get(index);
        }
        pathToGoal[c++] = currNode;
    }


    public void generateChosenState() {
        this.chosenState = pathToGoal[1].getState();
    }

    public void generateChangedColumn() {
        this.changedColumn = finalPath.get(minimaxTree.getRoot());
    }
}
