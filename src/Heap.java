

public class Heap {
    private Graph.Node[] heap;
    private int size;

    public Heap(Graph.Node[] heap){
        this.size = heap.length;
        this.heap = copy(heap);
        for (int i = size/2; i>=1; i--) {
            heapifyUp(i);
        }
    }

    public void update(int i) {
        heapifyUp(i);
        heapifyDown(i);
    }

    public void delete(int i) {
        indexSwitch(i, size-1);
        heapifyDown(i);
    }

    private Graph.Node[] copy(Graph.Node[] heap) {
        Graph.Node[] res = new Graph.Node[heap.length];
        for (int i = 0; i < heap.length; i++) {
            res[i] = heap[i];
            res[i].setIndexInHeap(i);
        }
        return res;
    }

    private void heapifyUp(int i) {
        while (i > 0 && heap[i].compareTo(heap[parent(i)]) > 0) {
            indexSwitch(i, parent(i));
            i = parent(i);
        }
    }

    private void heapifyDown(int i) {
        int left = leftChild(i);
        int right = rightChild(i);
        int smallest = i;
        if (left < size && heap[left].compareTo(heap[smallest]) > 0)
            smallest = left;
        if (right < size && heap[right].compareTo(heap[smallest]) > 0)
            smallest = right;
        if (smallest > i) {
            indexSwitch(i, smallest);
            heapifyDown(smallest);
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
