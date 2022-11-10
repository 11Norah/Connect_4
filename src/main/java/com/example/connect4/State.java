package com.example.connect4;

public class State {
    private String arr[][]=new String[6][7];
    private String color;

    public State(String arr[][],String color) {
        this.arr = arr;
        this.color=color;
    }
}