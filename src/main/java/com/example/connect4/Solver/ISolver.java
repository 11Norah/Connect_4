package com.example.connect4.Solver;
import com.example.connect4.State;
import com.example.connect4.heuristics.IHeuristic;
import com.example.connect4.minimaxTree.MinimaxTree;
import com.example.connect4.minimaxTree.Node;

public interface ISolver {

    void solve(IHeuristic heuristic, int maxLevel, State prevState, int col);
    MinimaxTree getTree();
    int getChangedColumn();
    int getBestChoice();

    Node[] getPathToGoal();

    State getChosenState();

}
