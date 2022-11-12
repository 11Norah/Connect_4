package com.example.connect4.heuristics;

import com.example.connect4.State;

public class Heuristic implements IHeuristic{

    static  int counter = 48;
    @Override
    public int getHeuristic(State state) {
        System.out.println(counter);
        return counter--;
    }
}
