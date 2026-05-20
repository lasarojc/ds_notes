package atomix_lab.state_machine.graph;

import io.atomix.primitive.AsyncPrimitive;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface AsyncDistributedGraph extends AsyncPrimitive {
    CompletableFuture<Optional<Edge>> addEdge(Edge edge);
    CompletableFuture<Optional<Vertex>> addVertex(Vertex vertex);
    CompletableFuture<Optional<Edge>> getEdge(Edge edge);
    CompletableFuture<Optional<Vertex>> getVertex(Vertex vertex);

    @Override
    DistributedGraph sync();
}