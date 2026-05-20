package atomix_lab.state_machine.server;

import io.atomix.cluster.Node;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider;
import io.atomix.core.Atomix;
import io.atomix.core.AtomixBuilder;
import io.atomix.core.map.DistributedMap;
import io.atomix.protocols.raft.partition.RaftPartitionGroup;
import io.atomix.utils.net.Address;
import atomix_lab.state_machine.graph.Edge;
import atomix_lab.state_machine.graph.Vertex;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphStateMachine //extends StateMachine
{
    class Pair<A,B>{
        A a;
        B b;

        public Pair(A na, B nb)
        {
            a = na;
            b = nb;
        }

        @Override
        public String toString()
        {
            return "("+ a + "," + b + ")";
        }

        @Override
        public boolean equals(Object arg0) {
            if (arg0 instanceof Pair)
            {
                @SuppressWarnings("rawtypes")
                Pair other = (Pair) arg0;
                return a.equals(other.a) && b.equals(other.b);
            }
            else
            {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return a.hashCode() + b.hashCode();
        }
    }

    private Map<Integer, Vertex> vertices = new HashMap<>();
    private Map<Pair<Integer,Integer>, Edge> edges = new HashMap<>();

    /*
    public Boolean AddEdge(Commit<AddEdgeCommand> commit){
        try{
            AddEdgeCommand aec = commit.operation();
            Pair<Integer,Integer> p = new Pair<>(aec.id, aec.id2);
            atomix_lab.state_machine.types.Edge e = new atomix_lab.state_machine.types.Edge(aec.id, aec.id2, aec.desc);

            System.out.println("Adding " + e);

            return edges.putIfAbsent(p, e) == null;
        }finally{
            commit.close();
        }
    }

    public Boolean AddVertex(Commit<AddVertexCommand> commit){
        try{
            AddVertexCommand avc = commit.operation();
            atomix_lab.state_machine.types.Vertex v = new atomix_lab.state_machine.types.Vertex(avc.id, avc.desc);

            System.out.println("Adding " + v);

            return vertices.putIfAbsent(avc.id, v) == null;
        }finally{
            commit.close();
        }
    }

    public atomix_lab.state_machine.types.Edge GetEdge(Commit<GetEdgeQuery> commit){
        try{
            GetEdgeQuery geq = commit.operation();
            Pair<Integer,Integer> p = new Pair<>(geq.id, geq.id2);

            System.out.println("Vertices:" + vertices);
            System.out.println("Edges:" + edges);

            atomix_lab.state_machine.types.Edge result = edges.get(p);
            System.out.println("GetEdge " + p + " = " + result);
            return result;
        }finally{
            commit.close();
        }
    }

    public atomix_lab.state_machine.types.Vertex GetVertex(Commit<GetVertexQuery> commit){
        try{
            GetVertexQuery gvq = commit.operation();
            System.out.println("Vertices:" + vertices);
            System.out.println("Edges:" + edges);

            atomix_lab.state_machine.types.Vertex result = vertices.get(gvq.id);
            System.out.println("GetVertex " + gvq.id + " = " + result);
            return result;
        }finally{
            commit.close();
        }
    }

*/

    public static void main( String[] args ){
        int myId = Integer.parseInt(args[0]);
        List<String> ids = new LinkedList<>();
        List<Address> addresses = new LinkedList<>();
        List<Node> nodes = new LinkedList<>();
        Node self = null;

        for(int i = 0; i < args.length/2; i++)
        {
            Address address = new Address(args[i*2+1], Integer.parseInt(args[i*2+2]));
            String id = "member-"+i;
            Node node = Node.builder().withId(id)
                                      .withAddress(address)
                                      .build();

            System.out.println("Building node: " + node);

            addresses.add(address);

            nodes.add(node);
            ids.add(id);
        }

        self = nodes.get(myId);
        System.out.println("Self " + self);

        AtomixBuilder builder = Atomix.builder()
                                      .withMemberId(self.id().id())
                                      .withAddress(self.address())
                                      .withMembershipProvider(BootstrapDiscoveryProvider.builder()
                                                                                        .withNodes(nodes)
                                                                                        .build())
                                      .withManagementGroup(RaftPartitionGroup.builder("system")
                                                                             .withNumPartitions(1)
                                                                             .withMembers(ids)
                                                                             .withDataDirectory(new File("/tmp/atomix-system/member-"+self.id().id()))
                                                                             .build())
                                      .withPartitionGroups(RaftPartitionGroup.builder("data")
                                                                             //.withPartitionSize(3)
                                                                             .withNumPartitions(1)
                                                                             .withMembers(ids)
                                                                             .withDataDirectory(new File("/tmp/atomix-data/member-"+self.id().id()))
                                                                             .build());
        Atomix server = builder.build();

        server.start().join();

        DistributedMap<String, String> map = server.<String, String>mapBuilder("my-map")
                .withCacheEnabled()
                .build();

        map.put("foo", "Hello world!");
        System.out.println("foo = " +  map.get("foo"));

        System.out.println("Node started");
    }
}