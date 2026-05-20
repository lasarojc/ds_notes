package atomix_lab.state_machine.graph;

import io.atomix.primitive.PrimitiveManagementService;
import io.atomix.primitive.protocol.ProxyCompatibleBuilder;

public abstract class DistributedGraphBuilder
        extends DistributedPrimitiveBuilder<DistributedGraphBuilder, DitributedGraphConfig, DistributedGraph>
        implements ProxyCompatibleBuilder<DistributedGraphBuilder> {

    protected DistributedGraphBuilder(String name, DistributedGraphConfig config, PrimitiveManagementService managementService){
        super(DistributedGraphType.instance(), name, config, managementService);
    }
}
