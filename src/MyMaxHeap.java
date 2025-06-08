/**
 * A max heap implementation for managing posts based on their like counts and post IDs.
 * The heap ensures that the post with the highest like count (or lexicographically largest ID in case of a tie) is at the root.
 */
public class MyMaxHeap {
    // Array to represent the heap
    private Post[] heap;

    // Current number of elements in the heap
    private int size;

    // Maximum capacity of the heap
    private int capacity;

    /**
     * Constructs a max heap with a specified initial capacity.
     *
     * @param capacity the initial capacity of the heap
     */
    public MyMaxHeap(int capacity) {
        this.capacity = capacity + 1; // Reserve space for 1-based indexing
        this.size = 0;
        this.heap = new Post[this.capacity];
    }

    /**
     * Gets the index of the parent of a given node.
     *
     * @param index the index of the child node
     * @return the index of the parent node
     */
    private int parent(int index) {
        return index / 2;
    }

    /**
     * Gets the index of the left child of a given node.
     *
     * @param index the index of the parent node
     * @return the index of the left child node
     */
    private int leftChild(int index) {
        return 2 * index;
    }

    /**
     * Gets the index of the right child of a given node.
     *
     * @param index the index of the parent node
     * @return the index of the right child node
     */
    private int rightChild(int index) {
        return 2 * index + 1;
    }

    /**
     * Swaps two elements in the heap.
     *
     * @param index1 the index of the first element
     * @param index2 the index of the second element
     */
    private void swap(int index1, int index2) {
        Post temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    }

    /**
     * Compares two posts based on their like counts and post IDs.
     *
     * @param post1 the first post to compare
     * @param post2 the second post to compare
     * @return a positive value if post1 > post2, negative if post1 < post2, or 0 if they are equal
     */
    private int compare(Post post1, Post post2) {
        if (post1.likeCount != post2.likeCount) {
            return post1.likeCount - post2.likeCount; // Higher like count is prioritized
        }
        return post1.postId.compareTo(post2.postId); // Lexicographically larger ID is prioritized
    }

    /**
     * Moves an element up the heap to restore the max-heap property.
     *
     * @param index the index of the element to percolate up
     */
    private void percolateUp(int index) {
        while (index > 1 && compare(heap[index], heap[parent(index)]) > 0) {
            swap(index, parent(index)); // Swap with parent
            index = parent(index); // Move up to the parent's index
        }
    }

    /**
     * Moves an element down the heap to restore the max-heap property.
     *
     * @param index the index of the element to percolate down
     */
    private void percolateDown(int index) {
        int largest = index;
        int left = leftChild(index);
        int right = rightChild(index);

        // Check if left child is larger than the current node
        if (left <= size && compare(heap[left], heap[largest]) > 0) {
            largest = left;
        }

        // Check if right child is larger than the largest so far
        if (right <= size && compare(heap[right], heap[largest]) > 0) {
            largest = right;
        }

        // If largest is not the current node, swap and continue
        if (largest != index) {
            swap(index, largest);
            percolateDown(largest);
        }
    }

    /**
     * Doubles the capacity of the heap when it is full.
     */
    private void resizeHeap() {
        capacity *= 2;
        Post[] newHeap = new Post[capacity];
        System.arraycopy(heap, 0, newHeap, 0, size + 1); // Include placeholder at index 0
        heap = newHeap;
    }

    /**
     * Inserts a new post into the heap.
     *
     * @param post the Post object to insert
     */
    public void insert(Post post) {
        // Resize the heap if necessary
        if (size == capacity - 1) {
            resizeHeap();
        }

        // Add the post at the next available position
        size = size + 1;
        heap[size] = post;

        // Restore the heap property
        percolateUp(size);
    }

    /**
     * Retrieves the maximum element (root) without removing it.
     *
     * @return the Post object at the root of the heap, or null if the heap is empty
     */
    public Post peek() {
        if (size == 0) {
            return null;
        }
        return heap[1]; // Root is at index 1
    }

    /**
     * Removes and returns the maximum element (root) from the heap.
     *
     * @return the Post object at the root of the heap, or null if the heap is empty
     */
    public Post extractMax() {
        if (size == 0) {
            return null;
        }

        // Store the root element
        Post max = heap[1];

        // Replace the root with the last element
        heap[1] = heap[size];
        size = size - 1;

        // Restore the heap property
        percolateDown(1);

        return max;
    }

    /**
     * Checks if the heap is empty.
     *
     * @return true if the heap is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Gets the current size of the heap.
     *
     * @return the number of elements in the heap
     */
    public int size() {
        return size;
    }

    /**
     * Returns a string representation of the heap's contents.
     *
     * @return a formatted string showing the heap's contents
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Heap Contents:\n");

        // Include all elements in the array
        for (int i = 0; i <= capacity - 1; i++) {
            sb.append("Index ").append(i).append(": ");
            if (heap[i] != null) {
                sb.append("Post ID: ").append(heap[i].postId).append(" Likes: ").append(heap[i].likeCount);
            } else {
                sb.append("null");
            }
            sb.append("\n");
        }

        if (size == 0) {
            sb.append("The heap is empty.\n");
        }

        return sb.toString();
    }

    /**
     * Manually sets the size of the heap. (Primarily for debugging purposes)
     *
     * @param size the new size of the heap
     */
    public void setSize(int size) {
        this.size = size;
    }
}
