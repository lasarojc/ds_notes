package atomix_lab.state_machine.graph;

import java.io.Serializable;

public class Vertex implements Serializable {
    int id;
    String desc;

    public Vertex(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    @Override
    public String toString()
    {
        return "(" + id + "," + desc + ")";
    }

}
