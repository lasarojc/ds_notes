package atomix_lab.state_machine.graph;

import io.atomix.primitive.operation.Command;
import io.atomix.primitive.operation.Query;

public interface DistributedGraphService {
    @Command
    void addEdge(Edge edge);
    @Command
    void addVertex(Vertex vertex);

    @Query
    void getEdge(Edge edge);
    @Query
    void getVertex(Vertex vertex);

}