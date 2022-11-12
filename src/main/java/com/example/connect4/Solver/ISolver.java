package com.example.connect4.Solver;
import com.example.connect4.minimaxTree.MinimaxTree;

public interface ISolver {

    void solve();
    MinimaxTree getTree();
    int getChangedColumn();
    int getBestChoice();

}
