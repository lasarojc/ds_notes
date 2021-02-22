package atomix_lab.state_machine.graph;

import com.sun.tools.javac.util.Pair;
import io.atomix.primitive.service.AbstractPrimitiveService;
import io.atomix.primitive.service.BackupInput;
import io.atomix.primitive.service.BackupOutput;

import java.util.HashMap;
import java.util.Map;

public class DefaultDistributedGraphService extends AbstractPrimitiveService<DistributedGraphClient> implements DistributedGraphService {

    Map<Integer, Vertex> vertexMap = new HashMap<>();
    Map<Pair<Integer,Integer>, Edge> edgeMap = new HashMap<>();

    public DefaultDistributedGraphService() {
        super(DistributedGraphType.instance(), DistributedGraphClient.class);
    }


    @Override
    public void addEdge(Edge edge) {
        edgeMap.put(new Pair<>(edge.id, edge.id2), edge);
    }

    @Override
    public void addVertex(Vertex vertex) {
        vertexMap.put(vertex.id, vertex);
    }

    @Override
    public void getEdge(Edge edge) {

    }

    @Override
    public void getVertex(Vertex vertex) {

    }

    @Override
    public void backup(BackupOutput backupOutput) {
        backupOutput.writeObject(edgeMap);
        backupOutput.writeObject(vertexMap);
    }

    @Override
    public void restore(BackupInput backupInput) {
        edgeMap = backupInput.readObject();
        vertexMap = backupInput.readObject();
    }
}
