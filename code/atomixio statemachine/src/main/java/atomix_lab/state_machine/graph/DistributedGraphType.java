package atomix_lab.state_machine.graph;

import io.atomix.primitive.PrimitiveType;

public class DistributedGraphType implements PrimitiveType<DistributedGraphBuilder, DistributedGraphConfig, DistributedGraph> {

        public static PrimitiveType instance() {
                return new DistributedGraphType();
        }

        @Override
        public String id() {
                return "graph";
        }
}