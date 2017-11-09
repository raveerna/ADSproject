import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.HashMap;
import java.util.List;

public class fibonacci
{
	public Node maxNode;
    public Node x;
    public Node y1;
    public int numberofNodes;
    Node temporary;
    int freq;
 // returns true if the heap is empty, false otherwise
    
    public boolean isEmpty()
    {
        return maxNode == null;
    }
 /**
     * increases the datafield value for a heap node
     * The structure of the heap may be changed.Cascading cut and cut operations are performed if necessary.
   */
    public void increaseKey(Node x, int k)
    {
        x.datafield = k;
        Node y = x.parentpointer;
        if ((y != null) && (x.datafield > y.datafield)) {
            cut(x, y);
            cascadingCut(y);
        }
        if (x.datafield > maxNode.datafield) {
            maxNode = x;
        }
    }
      /**
     * Inserts a new data element into the heap.
      new node is simply inserted into the root list of this heap.
     */
    public void insert(Node node, int count)
    {
         // concatenate node into max list
        if (maxNode != null) {
            node.leftpointer = maxNode;
            node.rightpointer = maxNode.rightpointer;
            maxNode.rightpointer.leftpointer=node;
            maxNode.rightpointer = node;
            node.rightpointer.leftpointer = node;
            
            if (node.datafield < maxNode.datafield) {
               // maxNode = node;
            }
        } else
        {
            maxNode = node;
        }
         numberofNodes++;
    }
/**
     * Returns the largest element in the heap. The one with the maximum key value (the node with the maximum frequency).
     * This will cause the trees in the heap to be consolidated, if required.
     */
    public Node removeMax()
    {
        Node z = maxNode;

        if (z != null) {
            int numKids = z.degreefield;
            Node x = z.childpointer;
          Node temporaryRight;

            // for every child of z perform:
            while (numKids > 0) {
                temporaryRight = x.rightpointer;

                // remove x from child list of z
                x.leftpointer.rightpointer = x.rightpointer;
                x.rightpointer.leftpointer = x.leftpointer;

                // add x to root list of heap
                x.leftpointer = maxNode;
                x.rightpointer = maxNode.rightpointer;
                maxNode.rightpointer = x;
                x.rightpointer.leftpointer = x;

                // set parent of x now to null as we removed it from the heap
                x.parentpointer = null;
                x = temporaryRight;
                numKids--;
            }

            // remove z from root list of heap
            z.leftpointer.rightpointer = z.rightpointer;
            z.rightpointer.leftpointer = z.leftpointer;

            if (z == z.rightpointer) {
                maxNode = null;
            } else {
                maxNode = z.rightpointer;
                consolidate();
            }

            // decrease the size of heap by 1 as we removed the maximum element from the heap
            numberofNodes--;
        }

        return z;
    }
/**
     * Returns the size of the heap which is the number of elements present in the heap.
     */
    public int size()
    {
        return numberofNodes;
    }
/**
     * Performs a cascading cut operation. This cuts y from its parent, if it finds the mark true and then does the same for its parent,
     *  and goes up the tree until it finds the node which is marked false*/
    public void cascadingCut(Node y)
    {
        Node z = y.parentpointer;

        // if there exists a parent,then
        if (z != null) {
            // if y is unmarked, set it marked as true
            if (!y.ismark) {
                y.ismark = true;
            } else {
                // if the node is marked, cut it from its parent
                cut(y, z);

                // cut its parent as well if it's mark is true
                cascadingCut(z);
            }
        }
    }
    /**This function actually checks if there are 2 trees in the heap having the same degree or not. If it finds a match, then
     * it makes the tree having root node of less datafield the child of tree of root node having higher datafield( as it is a
     *  max heap data structure). It finds the number of root nodes initially, and then finds the degree of each root node.
     */
public void consolidate()
    {
        int arraySize = numberofNodes + 1;

        List<Node> array =
            new ArrayList<Node>(arraySize);

        // Initialize degree array to null
        for (int i = 0; i < arraySize; i++) {
            array.add(null);
        }

        // Find the number of root nodes:
        int numRoots = 0;
        Node x = maxNode;
        if (x != null) {
            numRoots++;
            x = x.rightpointer;

            while (x != maxNode) {
                numRoots++;
                x = x.rightpointer;
            }
        }
        
        // For each node in root list,
        Node next;
        Node y;
        while (numRoots > 0) {
            // Access node's degree
            int d = x.degreefield;
            next = x.rightpointer;

            // and see if there's another node bearing the same degree.
            for(;;) {
               y = array.get(d);
                if (y == null) {
                    // No node of same degree found
                    break;
                }

                // Node of same degree found,so make one of the node a child of the other.
                // Do this based on the key value.
                if (x.datafield < y.datafield){
                  temporary = x;
                    x = y;
                    y = temporary;
                }

                //Node y disappears from root list and has become child of x.
				link(y,x);

                // We've handled this degree, go to next one.
                array.set(d, null);
                d++;
            }

            // Save this node for later as we might encounter another node
            // of the same degree.
			array.set(d, x);

            // Move forward through the array of nodes list.
            x = next;
            numRoots--;
        }

        // Set max to null (effectively losing the root list) and
        // reconstruct the root list from the array entries in array[].
        maxNode = null;

        for (int i = 0; i < arraySize; i++) {
            Node y1 = array.get(i);
            if (y1 == null) {
                continue;
            }

            // We've got a live one, add it to root list.
            if (maxNode != null) {
                // First remove node from root list.
                y1.leftpointer.rightpointer = y1.rightpointer;
                y1.rightpointer.leftpointer = y1.leftpointer;

                // Now add to root list, again.
                y1.leftpointer = maxNode;
                y1.rightpointer = maxNode.rightpointer;
                maxNode.rightpointer = y1;
                y1.rightpointer.leftpointer = y1;

                // Check if this is a new max.
                if (y1.datafield > maxNode.datafield) {
                    maxNode = y1;
                }
            } else {
                maxNode = y1;
            }
        }
    }
     /**
     * The reverse of the link operation: removes x from the child list of y.
     * This method assumes that max is non-null.
     */
	public void cut(Node x,Node y){ // remove x from child list of y and decrement degree of y
    
        x.leftpointer.rightpointer = x.rightpointer;
        x.rightpointer.leftpointer = x.leftpointer;
        y.degreefield--;

        // reset child pointer of y, if necessary
        if (y.childpointer == x) {
            y.childpointer = x.rightpointer;
        }

        if (y.degreefield == 0) {
            y.childpointer = null;
        }

        // add x to root list of heap
        x.leftpointer = maxNode;
        x.rightpointer = maxNode.rightpointer;
        maxNode.rightpointer = x;
        x.rightpointer.leftpointer = x;

        // set parent of x to null
        x.parentpointer = null;

        // mark x to false
        x.ismark = false;
    }
/**
     * Make node y a child of node x.
     */
  public void  link (Node y1, Node x) // make y1 a child of x
    {
        // remove y1 from root list of heap
		y1.leftpointer.rightpointer = y1.rightpointer;
        y1.rightpointer.leftpointer = y1.leftpointer;

        // make y1 a child of x
        y1.parentpointer=x;

        if (x.childpointer == null) {
            x.childpointer = y1;
            y1.rightpointer = y1;
            y1.leftpointer = y1;
        } else
        {
            y1.leftpointer = x.childpointer;
            y1.rightpointer = x.childpointer.rightpointer;
            x.childpointer.rightpointer = y1;
            y1.rightpointer.leftpointer = y1;
        }

        // increase degree of x  by 1
        x.degreefield++;

        // mark y1 as false
        y1.ismark = false;
    }
}



