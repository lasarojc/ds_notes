package atomix_lab.state_machine.graph;

import io.atomix.primitive.SyncPrimitive;

import java.util.Optional;

public interface DistributedGraph extends SyncPrimitive {
    Optional<Edge> addEdge(Edge edge);
    Optional<Vertex> addVertex(Vertex vertex);
    Optional<Edge> getEdge(Edge edge);
    Optional<Vertex> getVertex(Vertex vertex);


    @Override
    AsyncDistributedGraph async();
}