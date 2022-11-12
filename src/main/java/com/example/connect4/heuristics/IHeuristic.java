package com.example.connect4.heuristics;

import com.example.connect4.State;

public interface IHeuristic {
    int getHeuristic(State state);
}
