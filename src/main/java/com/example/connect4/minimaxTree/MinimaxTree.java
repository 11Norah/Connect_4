package com.example.connect4.minimaxTree;

public class MinimaxTree {
    private final Node root;
    private int expandedNodes;


    public MinimaxTree(Node root) {
        this.root = root;
        expandedNodes = 0;
    }

    public Node getRoot() {
        return root;
    }

    public void addNode() {
        expandedNodes++;
    }

    public int getExpandedNodes() {
        return expandedNodes;
    }
}
