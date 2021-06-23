import java.util.HashSet;
import java.util.Objects;
import java.util.Random;


public class GraphTester {
    public static void main(String[] args) {
        experiment();
    }

    private static void experiment() {
        for (int i = 6; i < 22; i++) {

            Graph g = new Graph(createNodes((int) Math.pow(2, i)));
            randomEdges((int) Math.pow(2, i)).forEach(p -> g.addEdge(p.getA(), p.getB()));
            System.out.printf("i = %d | n = %d | max deg = %d\n", i,(int) Math.pow(2, i), g.getNeighborhoodWeight(g.maxNeighborhoodWeight().getId()) - 1);
        }
    }

    public static HashSet<Pair> randomEdges(int n) {
        HashSet<Pair> set = new HashSet<>();
        Random rnd = new Random();
        int a, b;
        while (set.size() <  n) {
            a = rnd.nextInt(n) + 1; b = rnd.nextInt(n) + 1;
            if (a != b) {
                set.add(new Pair(a, b));
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

    private static class Pair {
        int a;
        int b;

        public Pair(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return (a == pair.a && b == pair.b) || (a == pair.b && b == pair.a);
        }

        @Override
        public int hashCode() {
            int max = Math.max(a, b), min = Math.min(a, b);
            return Objects.hash(max * (max + 1) / 2 + min);
        }
    }

}