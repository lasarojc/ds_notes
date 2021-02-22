package atomix_lab.state_machine.graph;

import io.atomix.primitive.event.Event;

public interface DistributedGraphClient {
    @Event
    void edgeSet(Edge edge);

    @Event
    void vertexSet(Vertex vertex);
}
