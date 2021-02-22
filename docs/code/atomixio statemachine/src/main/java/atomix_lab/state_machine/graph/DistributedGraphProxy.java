package atomix_lab.state_machine.graph;

import io.atomix.primitive.AbstractAsyncPrimitive;
import io.atomix.primitive.PrimitiveRegistry;
import io.atomix.primitive.SyncPrimitive;
import io.atomix.primitive.proxy.ProxyClient;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DistributedGraphProxy
        extends AbstractAsyncPrimitive<AsyncDistributedGraph, DistributedGraphService>
        implements AsyncDistributedGraph, DistributedGraphClient  {

    protected DistributedGraphProxy(ProxyClient<DistributedGraphService> client, PrimitiveRegistry registry) {
        super(client, registry);
    }

    private volatile CompletableFuture<Optional<Vertex>> vertexFuture;
    private volatile CompletableFuture<Optional<Edge>> edgeFuture;

    @Override
    public CompletableFuture<Optional<Edge>> addEdge(Edge edge) {
        edgeFuture = new CompletableFuture<>();
        getProxyClient().acceptBy(name(), distributedGraphService -> distributedGraphService.addEdge(edge))
                .whenComplete((result, error) -> {
                    if (error != null)
                        edgeFuture.completeExceptionally(error);
                });
        return edgeFuture.thenApply(result -> result).whenComplete((r,e) -> edgeFuture = null);
    }

    @Override
    public CompletableFuture<Optional<Vertex>> addVertex(Vertex vertex) {
        vertexFuture = new CompletableFuture<>();
        getProxyClient().acceptBy(name(), distributedGraphService -> distributedGraphService.addVertex(vertex))
                .whenComplete((result, error) -> {
                    if (error != null)
                        vertexFuture.completeExceptionally(error);
                });
        return vertexFuture.thenApply(result -> result).whenComplete((r,e) -> vertexFuture = null);
    }

    @Override
    public CompletableFuture<Optional<Edge>> getEdge(Edge edge) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<Vertex>> getVertex(Vertex vertex) {
        return null;
    }

    @Override
    public DistributedGraph sync() {
        return null;
    }

    @Override
    public SyncPrimitive sync(Duration duration) {
        return null;
    }

    @Override
    public void edgeSet(Edge edge) {

    }

    @Override
    public void vertexSet(Vertex vertex) {

    }
}
