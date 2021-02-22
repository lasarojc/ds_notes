package atomix_lab.state_machine.graph;

import java.io.Serializable;

public class Edge implements Serializable {
    int id;
    int id2;
    String desc;

    public Edge(int id, int id2, String desc) {
        this.id = id;
        this.id2 = id2;
        this.desc = desc;
    }

    @Override
    public String toString()
    {
        return "(" + id + "," + id2 + "," + desc + ")";
    }
}