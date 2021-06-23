/*
You must NOT change the signatures of classes/methods in this skeleton file.
You are required to implement the methods of this skeleton file according to the requirements.
You are allowed to add classes, methods, and members as required.
 */

import java.util.Random;

/**
 * This class represents a graph that efficiently maintains the heaviest neighborhood over edge addition and
 * vertex deletion.
 *
 */
public class Graph {

    private final HashTable idToNodeTable;
    private final Heap heap;
    private int nodeCount;
    private int edgeCount;
    /**
     * Initializes the graph on a given set of nodes. The created graph is empty, i.e. it has no edges.
     * You may assume that the ids of distinct nodes are distinct.
     * Time Complexity: O(n)
     * @param nodes - an array of node objects
     */
    public Graph(Node [] nodes){
        nodeCount = nodes.length;
        edgeCount = 0;
        heap = new Heap(nodes); //O(n)
        idToNodeTable = new HashTable(nodeCount); //O(n)
        for (Node node : nodes) {
            idToNodeTable.insert(node.getId(), node); //O(1)
        }
    }

    /**
     * This method returns the node in the graph with the maximum neighborhood weight.
     * Note: nodes that have been removed from the graph using deleteNode are no longer in the graph.
     * Time complexity: O(1)
     * @return a Node object representing the correct node. If there is no node in the graph, returns 'null'.
     */
    public Node maxNeighborhoodWeight(){
        return heap.max();
    }

    /**
     * given a node id of a node in the graph, this method returns the neighborhood weight of that node.
     *
     * Expected time complexity: O(1)
     * @param node_id - an id of a node.
     * @return the neighborhood weight of the node of id 'node_id' if such a node exists in the graph.
     * Otherwise, the function returns -1.
     */
    public int getNeighborhoodWeight(int node_id){

        Node node = idToNodeTable.get(node_id);
        if (node == null) return -1;
        return node.getNeighborhoodWeight();
    }

    /**
     * This function adds an edge between the two nodes whose ids are specified.
     * If one of these nodes is not in the graph, the function does nothing.
     * The two nodes must be distinct; otherwise, the function does nothing.
     * You may assume that if the two nodes are in the graph, there exists no edge between them prior to the call.
     * Expected time complexity: O(log n)
     * @param node1_id - the id of the first node.
     * @param node2_id - the id of the second node.
     * @return returns 'true' if the function added an edge, otherwise returns 'false'.
     */
    public boolean addEdge(int node1_id, int node2_id){
        Node node1, node2;
        if (node1_id == node2_id || (node1 = idToNodeTable.get(node1_id)) == null || (node2 = idToNodeTable.get(node2_id)) == null) {
            return false;
        }
        Neighborhood.Edge edge1 = node1.addEdge(node2);
        Neighborhood.Edge edge2 = node2.addEdge(node1);
        edge1.setOtherEdge(edge2);
        edge2.setOtherEdge(edge1);
        heap.update(node1.getIndexInHeap());
        heap.update(node2.getIndexInHeap());
        edgeCount++;
        return true;
    }

    /**
     * Given the id of a node in the graph, deletes the node of that id from the graph, if it exists.
     *
     * Expected time complexity: O((d_v + 1) * log n)
     * @param node_id - the id of the node to delete.
     * @return returns 'true' if the function deleted a node, otherwise returns 'false'
     */
    public boolean deleteNode(int node_id){
        Node node = idToNodeTable.get(node_id);
        if (node == null) return false;
        for (Neighborhood.Edge edge = node.neighbours().getFirst(); edge != node.neighbours().getFirst(); edge = edge.getNext()) { //O(d_v * log n)
            edge.getOtherEdge().delete();
            int i = edge.getOtherEdge().getNode().getIndexInHeap();
            edgeCount--;
            heap.update(i); // O(log n)
        }
        idToNodeTable.delete(node_id);
        heap.delete(node.getIndexInHeap()); //O(log n)
        nodeCount--;
        return true;
    }

    /**
     * Time complexity: O(1)
     * @return
     */
    public int getNumNodes() {return nodeCount;}

    /**
     * Time complexity: O(1)
     * @return
     */
    public int getNumEdges() {return edgeCount;}


    /**
     * This class represents a node in the graph.
     */
    public static class Node implements Comparable<Node>{
        private final int id;
        private final int weight;
        private final Neighborhood neighborhood = new Neighborhood();
        private int indexInHeap;
        /**
         * Creates a new node object, given its id and its weight.
         * Time complexity: O(1)
         * @param id - the id of the node.
         * @param weight - the weight of the node.
         */
        public Node(int id, int weight){
            this.id = id;
            this.weight = weight;
        }


        /**
         * Time complexity: O(1)
         * @return The node's neighbors
         */
        public Neighborhood neighbours() {return this.neighborhood;}

        /**
         * Time complexity: O(1)
         * @return
         */
        public int getNeighborhoodWeight() {
            return weight + neighborhood.getNeighborhoodSum();
        }


        /**
         * Time complexity: O(1)
         * @param other
         * @return
         */
        public Neighborhood.Edge addEdge(Node other) {
            return neighborhood.insert(other);
        }


        /**
         * Returns the id of the node.
         * @return the id of the node.
         */
        public int getId(){
            return id;
        }

        /**
         * Returns the weight of the node.
         * @return the weight of the node.
         */
        public int getWeight(){
            return weight;
        }

        public int getIndexInHeap() {
            return indexInHeap;
        }

        public void setIndexInHeap(int indexInHeap) {
            this.indexInHeap = indexInHeap;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.getNeighborhoodWeight(), o.getNeighborhoodWeight());
        }
    }
}



class Heap {
    private final Graph.Node[] heap;
    private int size;

    /**
     * Time complexity: O(n)
     * @param heap
     */
    public Heap(Graph.Node[] heap){
        this.size = heap.length;
        this.heap = copy(heap);
        for (int i = size/2; i>=1; i--) {
            heapifyDown(i);
        }
    }

    /**
     * Time complexity: O(log n)
     * @param i
     */
    public void update(int i) {
        heapifyUp(i);
        heapifyDown(i); // This will guarantee that the element in the i'th position will be where it is ought to be
    }

    /**
     * Time complexity: O(log n)
     * @param i
     */
    public void delete(int i) {
        indexSwitch(i, size-1);
        heap[size-1] = null;
        size--;
        heapifyDown(i);
    }

    /**
     * Time complexity: O(n)
     * @param heap
     * @return
     */
    private Graph.Node[] copy(Graph.Node[] heap) {
        Graph.Node[] res = new Graph.Node[heap.length];
        for (int i = 0; i < heap.length; i++) {
            res[i] = heap[i];
            res[i].setIndexInHeap(i);
        }
        return res;
    }

    /**
     * Time complexity: O(depth)
     * @param i
     */
    private void heapifyUp(int i) {
        while (i > 0 && heap[i].compareTo(heap[parent(i)]) > 0) {
            indexSwitch(i, parent(i));
            i = parent(i);
        }
    }

    /**
     * Time complexity: O(height)
     * @param i
     */
    private void heapifyDown(int i) {
        int left = leftChild(i);
        int right = rightChild(i);
        int largest = i;
        if (left < size && heap[left].compareTo(heap[largest]) > 0)
            largest = left;
        if (right < size && heap[right].compareTo(heap[largest]) > 0)
            largest = right;
        if (largest > i) {
            indexSwitch(i, largest);
            heapifyDown(largest);
        }
    }


    private void indexSwitch(int i, int j) {
        Graph.Node temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        heap[i].setIndexInHeap(i);
        heap[j].setIndexInHeap(j);
    }

    public Graph.Node max() {
        if (size == 0) return null;
        return heap[0];
    }

    private int leftChild(int i) {
        return 2 * (i + 1) - 1;
    }
    private int rightChild(int i) {
        return 2 * (i + 1);
    }
    private int parent(int i) {
        return (i+1)/2 -1 ;
    }
}


class HashTable {
    private final HashTableNode[] table;
    private final int a, b, size;
    public static final int P = 1_000_000_009;
    public static final double scaleFactor = 2;


    /**
     * Time complexity: O(n)
     * @param size
     */
    public HashTable(int size) {
        this.size = (int) (scaleFactor * size);
        table = new HashTableNode[this.size];
        Random random = new Random();
        a = random.nextInt(P - 1) + 1;
        b = random.nextInt(P);
    }

    /**
     * Time complexity: O(1)
     * @param n
     * @return
     */
    private int hash(int n) {
        return Math.floorMod(Math.floorMod(a*n+b, P), size);
    }

    /**
     * Time complexity: O(1)
     * @param id
     * @param node
     */
    public void insert(int id, Graph.Node node) {
        int i = hash(id);
        HashTableNode tableNode = new HashTableNode(id, node, table[i]);
        table[i] = tableNode;
    }

    /**
     * Expected time complexity: O(1)
     * @param id
     * @return
     */
    public Graph.Node get(int id) {
        int i = hash(id);
        HashTableNode tableNode = table[i];
        while (tableNode != null) {
            if (tableNode.getId() == id) return tableNode.getNode();
            tableNode = tableNode.getNext();
        }
        return null;
    }

    /**
     * Expected time complexity: O(1)
     * @param id
     */
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

    private static class HashTableNode {
        private final int id;
        private final Graph.Node node;
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
class Neighborhood{
    private Edge first;


    public int getNeighborhoodSum() {
        return neighborhoodSum;
    }

    private int neighborhoodSum;

    /**
     * Time Complexity: O(1)
     * @param node
     * @return the edge that was inserted
     */
    public Edge insert(Graph.Node node) {
        if (first == null) {
            first = new Edge(node);
            first.setNext(first);
            first.setPrev(first);
            neighborhoodSum += node.getWeight();
            return first;
        }
        Edge newEdge = new Edge(node);
        newEdge.setNext(first);
        newEdge.setPrev(first.getPrev());
        newEdge.getPrev().setNext(newEdge);
        newEdge.getNext().setPrev(newEdge);
        first = newEdge;
        neighborhoodSum += node.getWeight();
        return first;
    }


    public Edge getFirst() {
        return first;
    }


    public class Edge {
        private final Graph.Node node;

        private Edge next;
        private Edge prev;

        public Edge getOtherEdge() {
            return otherEdge;
        }

        public void setOtherEdge(Edge otherEdge) {
            this.otherEdge = otherEdge;
        }

        public Graph.Node getNode() {
            return node;
        }

        private Edge otherEdge;

        public Edge(Graph.Node node) {
            this.node = node;
        }

        public Edge getNext() {
            return next;
        }

        public void setNext(Edge next) {
            this.next = next;
        }

        public Edge getPrev() {
            return prev;
        }

        public void setPrev(Edge prev) {
            this.prev = prev;
        }

        /**
         * Time complexity: O(1)
         */
        public void delete() {
            if (next == this) {
                first = null;
                neighborhoodSum = 0;
                return;
            }
            neighborhoodSum -= node.getWeight();
            next.setPrev(prev);
            prev.setNext(next);

        }
    }
}
