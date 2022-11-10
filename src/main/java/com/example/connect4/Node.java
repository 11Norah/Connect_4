package com.example.connect4;
import java.util.ArrayList;
import java.util.List;
public class Node {
    State state ;
    int score;
    private List<Node> Children=new ArrayList<Node>();
    private Node Parent;
    public Node(List<Node> children,Node parent,State s,int score){
        this.Children=children;
        this.Parent=parent;
        this.state=s;
        this.score=score;
    }
    public List<Node>getChildren(){
        return Children;
    }

    public void setParent(Node parent) {
        Parent = parent;
    }

    public void addChild(Node child){
        child.setParent(this);
        this.Children.add(child);
    }
    public boolean isRoot(){
        return (this.Parent==null);
    }
    public boolean isLeaf(){
        return (this.Children.size()==0);
    }
}