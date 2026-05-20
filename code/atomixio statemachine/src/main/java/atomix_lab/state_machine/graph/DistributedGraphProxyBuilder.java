package atomix_lab.state_machine.graph;

import io.atomix.primitive.PrimitiveManagementService;
import io.atomix.primitive.service.ServiceConfig;

import java.util.concurrent.CompletableFuture;

public class DistributedGraphProxyBuilder extends DistributedGraphBuilder {
    public DistributedGraphProxyBuilder(String name, DistributedGraphConfig config, PrimitiveManagementService managementService){
        super(name, config, managementService);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<DistributedGraph> buildAsync() {
        return newProxy(DistributedGraphService.class, new ServiceConfig())
                .thenCompose(Proxy -> new DistributedGraphProxy(proxy, managementService.getPrimitiveRegistry()).connect())
                .thenApply(graph -> new DelegatingAsyncDistributedGraph(graph).sync());
    }
}
