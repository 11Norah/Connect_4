package com.example.connect4.minimaxTree;

import com.example.connect4.State;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final State state;
    private int heuristics;

    private List<Node> Children;
    private Node Parent;

    public Node(Node parent, State s) {
        this.Children = new ArrayList<>();
        this.Parent = parent;
        this.state = s;
    }

    public List<Node> getChildren() {
        return Children;
    }

    public void setParent(Node parent) {
        Parent = parent;
    }

    public void addChild(Node child) {
        child.setParent(this);
        this.Children.add(child);
    }

    public void setChildren(List<Node> children) {
        Children = children;
    }

    public boolean isRoot() {
        return (this.Parent == null);
    }

    public boolean isLeaf() {
        return (this.Children.size() == 0);
    }

    public State getState() {
        return state;
    }

    public Node getParent() {
        return Parent;
    }

    public void setHeuristics(int heuristics) {
        this.heuristics = heuristics;
    }

    public int getHeuristics() {
        return heuristics;
    }
}