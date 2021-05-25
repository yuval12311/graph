import java.util.Iterator;

public class Neighborhood implements Iterable<Neighborhood.Edge> {
    private Edge first;


    public int getNeighborhoodSum() {
        return neighborhoodSum;
    }

    private int neighborhoodSum;

    /**
     *
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

    @Override
    public Iterator<Edge> iterator() {
        return new Iterator<Edge>() {
            Edge current = first;
            @Override
            public boolean hasNext() {
                return current.getNext() != first;
            }

            @Override
            public Edge next() {
                Edge temp = current;
                current = current.getNext();
                return temp;
            }
        };
    }




    public class Edge {
        private Graph.Node node;

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

        public void delete() {
            if (next == this) {
                first = null;
                neighborhoodSum = 0;
                return;
            }
            next.setPrev(prev);
            prev.setNext(next);

        }
    }
}
