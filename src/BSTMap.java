import java.util.Iterator;
import java.util.Stack;

/**
 * Class that implements a binary search tree which implements the MyMap
 * interface.
 * @author Andrew Jung
 * @version 1.0 December 10, 2022
 * Some methods such as preorder, inorder, and postorder were provided
 * by my professor
 */
public class BSTMap<K extends Comparable<K>, V> implements MyMap<K, V> {
    public static final int PREORDER = 1, INORDER = 2, POSTORDER = 3;
    protected Node<K, V> root;
    protected int size;

    /**
     * Creates an empty binary search tree map.
     */
    public BSTMap() { }

    /**
     * Creates a binary search tree map of the given key-value pairs.
     * @param elements an array of key-value pairs
     */
    public BSTMap(Pair<K, V>[] elements) {
        insertElements(elements);
    }

    /**
     * Creates a binary search tree map of the given key-value pairs. If
     * sorted is true, a balanced tree will be created. If sorted is false,
     * the pairs will be inserted in the order they are received.
     * @param elements an array of key-value pairs
     */
    public BSTMap(Pair<K, V>[] elements, boolean sorted) {
        if (!sorted) {
            insertElements(elements);
        } else {
            root = createBST(elements, 0, elements.length - 1);
            size = elements.length;
        }
    }

    /**
     * Recursively constructs a balanced binary search tree by inserting the
     * elements via a divide-snd-conquer approach. The middle element in the
     * array becomes the root. The middle of the left half becomes the root's
     * left child. The middle element of the right half becomes the root's right
     * child. This process continues until low > high, at which point the
     * method returns a null Node.
     * @param pairs an array of <K, V> pairs sorted by key
     * @param low   the low index of the array of elements
     * @param high  the high index of the array of elements
     * @return      the root of the balanced tree of pairs
     */
    protected Node<K, V> createBST(Pair<K, V>[] pairs, int low, int high) {
        if (low > high) {
            return null;
        }
        int mid = low + (high - low) / 2;
        Node<K, V> parent = new Node<>(pairs[mid].key, pairs[mid].value);
        parent.left = createBST(pairs, low, mid - 1);
        if (parent.left != null) {
            parent.left.parent = parent;
        }
        parent.right = createBST(pairs, mid + 1, high);
        if (parent.right != null) {
            parent.right.parent = parent;
        }
        return parent;
    }

    /**
     * Inserts the pairs into the tree in the order they appear in the given
     * array.
     * @param pairs the array of <K, V> pairs to insert
     */
    protected void insertElements(Pair<K, V>[] pairs) {
        for (Pair<K, V> pair : pairs) {
            put(pair);
        }
    }

    /**
     * Returns the number of key-value mappings in this map.
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if this map contains no key-value mappings.
     * @return true if this map contains no key-value mappings
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns a String of the key-value pairs visited with a preorder
     * traversal. Uses a StringBuilder for efficiency.
     * @return a String of the key-value pairs visited with a preorder
     *         traversal
     */
    public String preorder() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        preorder(root, builder, 0);
        builder.append("]");
        return builder.toString();
    }

    /**
     * Visits the Nodes of the tree in a preorder traversal. Each Node's
     * toString() return value should be appended to the StringBuilder. A ", "
     * must appear between each Node's data in the final String.
     * @param n            the current Node
     * @param builder      the StringBuilder used to build up the output
     * @param nodesVisited the number of nodes visited so far. Useful for
     *                     determining when to append ", ".
     * @return the number of nodes visited after each recursive call
     */
    private int preorder(Node<K, V> n, StringBuilder builder,
                         int nodesVisited) {
        if (n != null) {
            if (nodesVisited != 0) {
                builder.append(", ");
            }
            builder.append(n);
            nodesVisited = preorder(n.left, builder, nodesVisited + 1);
            nodesVisited = preorder(n.right, builder, nodesVisited);
        }
        return nodesVisited;
    }

    /**
     * Returns a String of the key-value pairs visited with an inorder
     * traversal. Uses a StringBuilder for efficiency.
     * @return a String of the key-value pairs visited with an inorder
     *         traversal
     */
    public String inorder() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        inorder(root, builder, 0);
        builder.append("]");
        return builder.toString();
    }

    /**
     * Visits the Nodes of the tree in an inorder traversal. Each Node's
     * toString() return value should be appended to the StringBuilder. A ", "
     * must appear between each Node's data in the final String.
     * @param n            the current Node
     * @param builder      the StringBuilder used to build up the output
     * @param nodesVisited the number of nodes visited so far. Useful for
     *                     determining when to append ", ".
     * @return the number of nodes visited after each recursive call
     */
    private int inorder(Node<K, V> n, StringBuilder builder,
                        int nodesVisited) {
        if (n != null) {
            nodesVisited = inorder(n.left, builder, nodesVisited);
            if (nodesVisited != 0) {
                builder.append(", ");
            }
            builder.append(n);
            nodesVisited = inorder(n.right, builder, nodesVisited + 1);
        }
        return nodesVisited;
    }

    /**
     * Returns a String of the key-value pairs visited with a postorder
     * traversal. Uses a StringBuilder for efficiency.
     * @return a String of the key-value pairs visited with a postorder
     *         traversal
     */
    public String postorder() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        postorder(root, builder, 0);
        builder.append("]");
        return builder.toString();
    }

    /**
     * Visits the Nodes of the tree in a postorder traversal. Each Node's
     * toString() return value should be appended to the StringBuilder. A ", "
     * must appear between each Node's data in the final String.
     * @param n            the current Node
     * @param builder      the StringBuilder used to build up the output
     * @param nodesVisited the number of nodes visited so far. Useful for
     *                     determining when to append ", ".
     * @return the number of nodes visited after each recursive call
     */
    private int postorder(Node<K, V> n, StringBuilder builder,
                          int nodesVisited) {
        if (n != null) {
            nodesVisited = postorder(n.left, builder, nodesVisited);
            nodesVisited = postorder(n.right, builder, nodesVisited);
            if (nodesVisited != 0) {
                builder.append(", ");
            }
            builder.append(n);
            nodesVisited++;
        }
        return nodesVisited;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     * @param  key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this
     *         map contains no mapping for the key
     */
    public V get(K key) {
        Node<K, V> x = iterativeSearch(key);
        return x != null ? x.value : null;
    }

    /**
     * Determines if the supplied key is found in the tree. If so, it returns a
     * reference to the Node containing the key. Otherwise, null is returned.
     * @param key key whose mapping is to be removed from the map
     * @return a reference to the Node containing the specified key
     */
    protected Node<K, V> iterativeSearch(K key) {
        Node<K, V> p = root;
        if(p == null){
            return null;
        }
        while(p != null){
            int compare = key.compareTo(p.key);
            if(compare == 0){
                return p;
            }
            else if(compare < 0){
                p = p.left;
            }
            else{
                p = p.right;
            }
        }
        return null;
    }

    /**
     * Associates the specified value with the specified key in this map. If the
     * map previously contained a mapping for the key, the old value is replaced
     * by the specified value.
     * @param pair  the key-value mapping to insert into the tree
     * @return the previous value associated with key, or null if there was no
     *         mapping for key
     */
    public V put(Pair<K, V> pair) {
        return put(pair.key, pair.value);
    }

    /**
     * Associates the specified value with the specified key in this map. If the
     * map previously contained a mapping for the key, the old value is replaced
     * by the specified value.
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the previous value associated with key, or null if there was no
     *         mapping for key
     */
    public V put(K key, V value) {
        Node<K, V> x = root, y = null;
        while (x != null) {
            y = x;
            int comparison = key.compareTo(x.key);
            if (comparison < 0) {
                x = x.left;
            } else if (comparison > 0) {
                x = x.right;
            } else {
                // The key was found in the tree. Return the previous value
                // associated with the key.
                V oldValue = x.value;
                x.value = value;
                return oldValue;
            }
        }
        Node<K, V> n = new Node<>(key, value);
        n.parent = y;
        if (y == null) {
            root = n;
        } else if (key.compareTo(y.key) < 0) {
            y.left = n;
        } else {
            y.right = n;
        }
        size++;
        // There was no previous mapping for this key.
        return null;
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with key, or null if there was no
     *         mapping for key
     */
    public V remove(K key) {
        //find and return node that is to be removed
        Node<K, V> toBeRemovedNode = iterativeSearch(key);

        //return null if node is not found
        if(toBeRemovedNode == null){
            return null;
        }

        //store old value of the node
        V oldValue = toBeRemovedNode.value;

        //case for when the node to be removed has two children
        if(toBeRemovedNode.left != null && toBeRemovedNode.right != null){
            //find the minimum node of the right child, which will replace
            //the removed node
            Node<K, V> replacement = treeMinimum(toBeRemovedNode.right);
            Node<K, V> parent = replacement.parent;
            //if the replacement node has a right child
            if(replacement.right != null){
                //if replacement node is the right child of the removed node
                if(parent == toBeRemovedNode){
                    //then set the right child of the toBeRemoved node to the right child of
                    //the replacement node
                    toBeRemovedNode.right = replacement.right;
                    //then set the parent pointer of that child accordingly
                    replacement.right.parent = toBeRemovedNode;
                }
                //if replacement node is not the right child of the removed node
                else{
                    //set the left child parent of the replacement node to be
                    //the replacement's right child (since replacement node is minimum,
                    //we don't have to consider replacement's left child)
                    parent.left = replacement.right;
                    //set parent pointer of the right child accordingly
                    replacement.right.parent = parent;
                }
            }
            //if the replacement node does not have a right child, and the parent
            //of the replacement is not the root, it means that the replacement node is a leaf.
            //Set the left child of the parent of the replacement to be null
            else if(parent != root){
                parent.left = null;
            }
            //else there is only one right child of the root, so set the right child
            //of the parent to be null
            else{
                parent.right = null;
            }
            //replace the toBeRemoved node key and value with replacement's
            toBeRemovedNode.key = replacement.key;
            toBeRemovedNode.value = replacement.value;
            replacement = null;
            size--;
            return oldValue;
        }

        //case for when the toBeRemoved node is a leaf
        else if(toBeRemovedNode.left == null && toBeRemovedNode.right == null){
            //if node is root, then set root to null
            if(toBeRemovedNode == root){
                root = null;
                size--;
                return oldValue;
            }
            //if the toBeRemoved node is the left child
            //then set .parent.left to null
            if(toBeRemovedNode == toBeRemovedNode.parent.left){
                toBeRemovedNode.parent.left = null;
            }
            //if the toBeRemoved node is the right child
            //then set .parent.right to null
            else if(toBeRemovedNode == toBeRemovedNode.parent.right){
                toBeRemovedNode.parent.right = null;
            }
            size--;
            return oldValue;
        }

        //case for when the toBeRemoved node has one child
        else{
            Node<K, V> parent = toBeRemovedNode.parent;
            Node<K, V> replacement;
            //see which child becomes the replacement
            if(toBeRemovedNode.left == null){
                replacement = toBeRemovedNode.right;
            }
            else{
                replacement = toBeRemovedNode.left;
            }
            //if the toBeRemoved node is the root, then
            //set replacement.parent to null and set root to replacement
            //pointers of replacement will carry over
            if(toBeRemovedNode == root){
                root = replacement;
                replacement.parent = null;
                return oldValue;
            }
            //if toBeRemoved is the left child, then set
            //the left child of parent to be the replacement node
            //and the parent of the replacement to the parent
            if(toBeRemovedNode == parent.left){
                parent.left = replacement;
                replacement.parent = parent;
            }
            //same story here but for right child
            else{
                parent.right = replacement;
                replacement.parent = parent;
            }
            size--;
            return oldValue;
        }
    }

    /**
     * Returns a reference to the Node whose key value is the minimum key in the
     * tree.
     * @param x the Node at which to start the traversal
     * @return a reference to the Node whose key value is the minimum key in the
     *         tree
     */
    protected Node<K, V> treeMinimum(Node<K, V> x) {
        while (x.left != null) {
            x = x.left;
        }
        return x;
    }

    /**
     * Returns a String representation of the tree, where the Nodes are visited
     * with an inorder traversal.
     * @return a String representation of the tree
     */
    public String toString() {
        return inorder();
    }

    /**
     * Returns an ASCII drawing of the tree.
     * @return an ASCII drawing of the tree
     */
    public String toAsciiDrawing() {
        BinarySearchTreePrinter<K, V> printer =
                new BinarySearchTreePrinter<>();
        printer.createAsciiTree(root);
        return printer.toString();
    }

    public void printTraversal(int type) {
        switch (type) {
            case PREORDER -> {
                System.out.print("Preorder traversal:       ");
                System.out.println(preorder());
            }
            case INORDER -> {
                System.out.print("Inorder traversal:        ");
                System.out.println(inorder());
            }
            case POSTORDER -> {
                System.out.print("Postorder traversal:      ");
                System.out.println(postorder());
            }
        }
    }

    /**
     * Returns an iterator over the Entries in this map in the order
     * in which they appear.
     * @return an iterator over the Entries in this map
     */
    public Iterator<Entry<K, V>> iterator() {
        return new BinaryTreeItr();
    }

    private class BinaryTreeItr implements Iterator<Entry<K, V>> {
        private Node<K, V> current;
        private final Stack<Node<K, V>> parentStack = new Stack<>();

        BinaryTreeItr() {
            current = root;
        }

        @Override
        public boolean hasNext() {
            return !parentStack.isEmpty() || current != null;
        }

        @Override
        public Entry<K, V> next() {
            while (hasNext()) {
                if (current != null) {
                    parentStack.push(current);
                    current = current.left;
                } else {
                    Node<K, V> toReturn = parentStack.pop();
                    current = toReturn.right;
                    return toReturn;
                }
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns the height of the tree. If the tree is null, the height is -1.
     * @return the height of the tree
     */
    public int height() {
        return height(root) ;
    }

    protected int height(Node<K, V> node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Returns the number of null references in the tree. Uses a recursive
     * helper method to count the null references.
     * @return the number of null references in the tree
     */
    public int nullCount() {
        return nullCount(root);
    }

    private int nullCount(Node<K, V> node) {
        if (node == null) {
            return 1;
        }
        return nullCount(node.left) + nullCount(node.right);
    }

    /**
     * Returns the sum of the levels of each non-null node in the tree starting
     * at the root.
     * For example, the tree
     *   5 <- level 0
     *  / \
     * 2   8 <- level 1
     *      \
     *       10 <- level 2
     * has sum 0 + 2(1) + 2 = 4.
     * @return the sum of the levels of each non-null node in the tree starting
     *         at the root
     */
    public int sumLevels() {
        return sumLevels(root, 0);
    }

    private int sumLevels(Node<K, V> node, int level) {
        if (node == null) {
            return 0;
        }
        return sumLevels(node.left, level + 1) +
                sumLevels(node.right, level + 1) + level;
    }

    /**
     * Returns the sum of the levels of each null node in the tree starting at
     * the root.
     * For example, the tree
     *    5 <- level 0
     *   / \
     *  2   8 <- level 1
     * / \ / \
     * * * * * 10 <- level 2
     *        / \
     *        * * <- level 3
     * has sum 3(2) + 2(3) = 12.
     * @return the sum of the levels of each null node in the tree starting at
     *         the root
     */
    public int sumNullLevels() {
        return sumNullLevels(root, 0);
    }

    private int sumNullLevels(Node<K, V> node, int level) {
        if (node == null) {
            return level;
        }
        return sumNullLevels(node.left, level + 1) +
                sumNullLevels(node.right, level + 1);
    }

    public double successfulSearchCost() {
        return size == 0 ? 0 : 1 + (double)sumLevels() / size;
    }

    public double unsuccessfulSearchCost() {
        return (double)sumNullLevels() / nullCount();
    }

    public int diameter() {
        return diameter(root);
    }

    private int diameter(Node<K, V> node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(
                height(node.left) + height(node.right) + 2,
                Math.max(diameter(node.left), diameter(node.right)));
    }

    private void iterativeInorder() {
        iterativeInorder(root);
    }

    private void iterativeInorder(Node<K, V> node) {
        Stack<Node<K, V>> stack = new Stack<>();
        Node<K, V> n = root;
        while (n != null || !stack.empty()) {
            while (n != null) {
                // Store a reference to the current node before visiting its
                // left subtree.
                stack.push(n);
                n = n.left;
            }
            n = stack.pop();
            System.out.println(n);
            n = n.right;
        }
    }

    /**
     * Main method to facilitate testing your code.
     * Either a map of <Integer, Integer> or <String, String> will be created.
     * If the first command line argument parses to an int, the map will be of
     * type <Integer, Integer>.
     * @param args the values to insert into the tree
     */
    public static void main(String[] args) {
        boolean usingInts = true;
        if (args.length > 0) {
            try {
                Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                usingInts = false;
            }
        }

        BSTMap bst;
        if (usingInts) {
            @SuppressWarnings("unchecked")
            Pair<Integer, Integer>[] pairs = new Pair[args.length];
            for (int i = 0; i < args.length; i++) {
                try {
                    int val = Integer.parseInt(args[i]);
                    pairs[i] = new Pair<>(val, val);
                } catch (NumberFormatException nfe) {
                    System.err.println("Error: Invalid integer '" + args[i]
                            + "' found at index " + i + ".");
                    System.exit(1);
                }
            }
            bst = new BSTMap<>(pairs);
        } else {
            @SuppressWarnings("unchecked")
            Pair<String, String>[] pairs = new Pair[args.length];
            for (int i = 0; i < args.length; i++) {
                pairs[i] = new Pair<>(args[i], args[i]);
            }
            bst = new BSTMap<>(pairs);
        }

        System.out.println(bst.toAsciiDrawing());
        System.out.println();
        System.out.println("Height:                   " + bst.height());
        System.out.println("Total nodes:              " + bst.size());
        System.out.printf("Successful search cost:   %.3f\n",
                          bst.successfulSearchCost());
        System.out.printf("Unsuccessful search cost: %.3f\n",
                          bst.unsuccessfulSearchCost());
        bst.printTraversal(PREORDER);
        bst.printTraversal(INORDER);
        bst.printTraversal(POSTORDER);
        System.out.println(bst.diameter());
        bst.iterativeInorder();
    }
}
