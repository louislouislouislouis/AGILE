package org.hexanome.model;

import java.util.Comparator;
import java.util.List;

public class Node implements Comparator<Node> {

    private Long id;
    private Double currentLength;
    private List<String> previousSegments;

    public Node() {}

    public Node(Long id, Double currentLength) {
        this.id = id;
        this.currentLength = currentLength;
    }

    public Double getCurrentLength() {
        return this.currentLength;
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
