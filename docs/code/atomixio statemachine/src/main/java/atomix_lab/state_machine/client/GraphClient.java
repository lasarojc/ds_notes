package atomix_lab.state_machine.client;

import io.atomix.cluster.Node;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider;
import io.atomix.core.Atomix;
import io.atomix.core.AtomixBuilder;
import io.atomix.core.map.DistributedMap;
import io.atomix.core.profile.Profile;
import io.atomix.utils.net.Address;

import java.util.LinkedList;
import java.util.List;

public class GraphClient
{
    public static void main( String[] args ){
        int myId = Integer.parseInt(args[0]);
        List<Address> addresses = new LinkedList<>();
        List<Node> nodes = new LinkedList<>();
        Node self = null;

        for(int i = 0; i <args.length/2; i++)
        {
            Address address = new Address(args[i*2+1], Integer.parseInt(args[i*2+2]));
            addresses.add(address);

            Node node = Node.builder().withId("member-"+i)
                                      .withAddress(address)
                                      .build();

            System.out.println("Building node: " + node);
            if (i == 0) {
                self = node;
                System.out.println("Self: " + node);
            } else {
                nodes.add(node);
            }
        }


        AtomixBuilder builder = Atomix.builder()
                                      .withMemberId("client-"+myId)
                                      .withAddress(self.address())
                                      .withMembershipProvider(BootstrapDiscoveryProvider.builder().withNodes(nodes).build());

        System.out.println("Configured");

        Atomix client = builder.build();
        System.out.println("Built");

        client.start().join();
        System.out.println("Joined");




        DistributedMap<String, String> map = client.<String, String>mapBuilder("my-map")
                .withCacheEnabled()
                .build();

        map.put("foo", "Hello world!");
        System.out.println("foo = " +  map.get("foo"));


        /*
        CompletableFuture[] futures = new CompletableFuture[]{
                client.submit(new AddVertexCommand(1,1, "vertice1")),
                client.submit(new AddVertexCommand(2,1, "vertice2")),
                client.submit(new AddVertexCommand(3,1, "vertice3")),
                client.submit(new AddVertexCommand(4,2, "vertice4")),
                client.submit(new AddEdgeCommand(1,2, "atomix_lab.state_machine.types.Edge from 1 to 2")),
                client.submit(new AddEdgeCommand(1,3, "atomix_lab.state_machine.types.Edge")),
                client.submit(new AddEdgeCommand(1,4, "atomix_lab.state_machine.types.Edge")),
                client.submit(new AddEdgeCommand(4,3, "atomix_lab.state_machine.types.Edge"))
        };

        CompletableFuture.allOf(futures).thenRun(() -> System.out.println("Commands completed!"));

        try {
            System.out.println("1: " + client.submit(new GetVertexQuery(1)).get());
            System.out.println("2: " + client.submit(new GetVertexQuery(2)).get());
        } catch (Exception e)
        {
            System.out.println("Commands may have failed.");
            e.printStackTrace();
        }

        client.submit(new GetEdgeQuery(1,2)).thenAccept(result -> {
            System.out.println("1-2: " + result);
        });

         */
    }
}