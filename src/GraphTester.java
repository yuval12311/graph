import javafx.util.Pair;

import java.util.HashSet;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;

public class GraphTester {
    public static void main(String[] args) {
        experiment();
    }

    private static void experiment() {
        for (int i = 6; i < 22; i++) {

            Graph g = new Graph(createNodes((int) Math.pow(2, i)));
            randomEdges((int) Math.pow(2, i)).forEach(p -> g.addEdge(p.getKey(), p.getValue()));
            System.out.printf("i = %d | n = %d | max deg = %d\n", i,(int) Math.pow(2, i), g.getNeighborhoodWeight(g.maxNeighborhoodWeight().getId()));
        }
    }

    public static HashSet<Pair<Integer, Integer>> randomEdges(int n) {
        HashSet<Pair<Integer, Integer>> set = new HashSet<>();
        Random rnd = new Random();
        int a, b;
        while (set.size() <  n) {
            a = rnd.nextInt(n) + 1; b = rnd.nextInt(n) + 1;
            if (a != b) {
                set.add(new Pair<>(rnd.nextInt(n) + 1, rnd.nextInt(n) + 1));
            }

        }
        return set;
    }

    public static Graph.Node[] createNodes(int n) {
       Graph.Node[] nodes = new Graph.Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Graph.Node(i + 1, 1);
        }
        return nodes;
    }
}