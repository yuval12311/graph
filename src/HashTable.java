import java.util.Random;

public class HashTable {
    private HashTableNode[] table;
    private int a, b, size;
    public static int P = 1_000_000_009;

    public HashTable(int size) {
        this.size = size;
        table = new HashTableNode[size];
        Random random = new Random();
        a = random.nextInt(P - 1) + 1;
        b = random.nextInt(P);
    }

    private int hash(int n) {
        return ((a*n+b)%P)%size;
    }

    public void insert(int id, Graph.Node node) {
        int i = hash(id);
        HashTableNode tableNode = new HashTableNode(id, node, table[i]);
        table[i] = tableNode;
    }

    public Graph.Node get(int id) {
        int i = hash(id);
        HashTableNode tableNode = table[i];
        while (tableNode != null) {
            if (tableNode.getId() == id) return tableNode.getNode();
            tableNode = tableNode.getNext();
        }
        return null;
    }

    public void delete(int id) {
        int i = hash(id);
        HashTableNode tableNode = table[i];
        if (tableNode.getId() == id) {
            table[i] = tableNode.getNext();
        } else {
            while (tableNode.next.getId() != id) {
                tableNode = tableNode.getNext();
            }
            tableNode.setNext(tableNode.getNext().getNext());
        }


    }

    private class HashTableNode {
        private int id;
        private Graph.Node node;
        private HashTableNode next;

        public HashTableNode(int id, Graph.Node node, HashTableNode next) {
            this.id = id;
            this.node = node;
            this.next = next;
        }

        public int getId() {
            return id;
        }

        public Graph.Node getNode() {
            return node;
        }

        public HashTableNode getNext() {
            return next;
        }

        public void setNext(HashTableNode next) {
            this.next = next;
        }
    }

}
