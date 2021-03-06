package org.hexanome.model;

import java.util.Comparator;

public class Node implements Comparator<Node> {

    private Long id;
    private Double currentWeight;

    public Node() {}

    public Node(Long id, Double currentLength) {
        this.id = id;
        this.currentWeight = currentLength;
    }

    public Double getCurrentLength() {
        return this.currentWeight;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public int compare(Node o1, Node o2) {
        if(o1.getCurrentLength() < o2.getCurrentLength()) {
            return -1;
        }
        if (o1.getCurrentLength() > o2.getCurrentLength()) {
            return 1;
        }
        return 0;
    }
}
