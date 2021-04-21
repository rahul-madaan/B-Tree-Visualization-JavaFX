package algorithm;

import java.io.*;
import java.util.LinkedList;

public class BTree<K extends Comparable<K>> implements Serializable {
    private BTNode<K> root = null;
    private int order, index, treeSize;
    private final int halfNumber;
    public final BTNode<K> nullBTNode = new BTNode<K>();

    private LinkedList<BTree<K>> stepTrees = new LinkedList<BTree<K>>();


    /**
     *
     * @param order of B-tree
     */
    public BTree(int order) {
        if (order < 3) {
            try {
                throw new Exception("B-tree's order can not lower than 3");
            } catch (Exception e) {
                e.printStackTrace();
            }
            order = 3;
        }
        this.order = order;
        halfNumber = (order - 1) / 2;
    }

    /**
     * @return true, if tree is empty
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * @return root node
     */
    public BTNode<K> getRoot() {
        return root;
    }

    public void setRoot(BTNode<K> root) {
        this.root = root;
    }

    /**
     * @return size of nodes in the tree
     */

    public int getTreeSize() {
        return treeSize;
    }

    public int getHalfNumber() {
        return halfNumber;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public LinkedList<BTree<K>> getStepTrees() {
        return stepTrees;
    }

    public void setStepTrees(LinkedList<BTree<K>> stepTrees) {
        this.stepTrees = stepTrees;
    }


    /**
     * @return height of tree
     */
    public int getHeight() {
        if (isEmpty()) {
            return 0;
        } else {
            return getHeight(root);
        }
    }

    public static void getHeights(){
        return;
    }

    /**
     * @param node , the node
     * @return the height of the node position
     */
    public int getHeight(BTNode<K> node) {
        int height = 0;
        BTNode<K> currentNode = node;
        while (!currentNode.equals(nullBTNode)) {
            currentNode = currentNode.getChild(0);
            height++;
        }
        return height;
    }

    /**
     * @param key , use key to find node
     * @return the node which contains of the key
     */
    public BTNode<K> getNode(K key) {
        if (isEmpty()) {
            return nullBTNode;
        }
        BTNode<K> currentNode = root;
        while (!currentNode.equals(nullBTNode)) {
            int i = 0;
            while (i < currentNode.getSize()) {
                if (currentNode.getKey(i).equals(key)) {
                    index = i;
                    return currentNode;
                } else if (currentNode.getKey(i).compareTo(key) > 0) {
                    currentNode = currentNode.getChild(i);
                    i = 0;
                } else {
                    i++;
                }
            }
            if (!currentNode.isNull()) {
                currentNode = currentNode.getChild(currentNode.getSize());
            }
        }
        return nullBTNode;
    }

    /**
     * @param key      , the key
     * @param fullNode , full node
     * @return half of the full node after inserting inside
     */
    private BTNode<K> getHalfKeys(K key, BTNode<K> fullNode) {
        int fullNodeSize = fullNode.getSize();

        // Add the node to the appropriate location within that node
        for (int i = 0; i < fullNodeSize; i++) {
            if (fullNode.getKey(i).compareTo(key) > 0) {
                fullNode.addKey(i, key);
                break;
            }
        }
        if (fullNodeSize == fullNode.getSize())
            fullNode.addKey(fullNodeSize, key);

        //System.out.println("\n" + "Insert the key in the appropriate location");
//		stepMess.add("Insert " + key + " into the right place");
        stepTrees.add(cloneCreate.clone(this));

        return getHalfKeys(fullNode);
    }

    /**
     * @param fullNode , full node
     * @return half of the full node
     */
    private BTNode<K> getHalfKeys(BTNode<K> fullNode) {
        BTNode<K> newNode = new BTNode<K>(order);
        for (int i = 0; i < halfNumber; i++) {
            newNode.addKey(i, fullNode.getKey(0));
            fullNode.removeKey(0);
        }
        return newNode;
    }

    /**
     * @param halfNode , the rest of the full node
     * @return the left keys of full node
     */
    private BTNode<K> getRestOfHalfKeys(BTNode<K> halfNode) {
        BTNode<K> newNode = new BTNode<K>(order);
        int halfNodeSize = halfNode.getSize();
        for (int i = 0; i < halfNodeSize; i++) {
            if (i != 0) {
                newNode.addKey(i - 1, halfNode.getKey(1));
                halfNode.removeKey(1);
            }
            newNode.addChild(i, halfNode.getChild(0));
            halfNode.removeChild(0);
        }
        return newNode;
    }

    /**
     * @param childNode , merge childNode with its fatherNode
     * @param index     , where to add node
     */
    private void mergeWithFatherNode(BTNode<K> childNode, int index) {
        childNode.getFather().addKey(index, childNode.getKey(0));
        childNode.getFather().removeChild(index);
        childNode.getFather().addChild(index, childNode.getChild(0));
        childNode.getFather().addChild(index + 1, childNode.getChild(1));
    }

    /**
     * @param childNode , merge childNode with its fatherNode
     */
    private void mergeWithFatherNode(BTNode<K> childNode) {
        int fatherNodeSize = childNode.getFather().getSize();
        for (int i = 0; i < fatherNodeSize; i++) {
            if (childNode.getFather().getKey(i).compareTo(childNode.getKey(0)) > 0) {
                mergeWithFatherNode(childNode, i);
                break;
            }
        }
        if (fatherNodeSize == childNode.getFather().getSize()) {
            mergeWithFatherNode(childNode, fatherNodeSize);
        }
        for (int i = 0; i <= childNode.getFather().getSize(); i++)
            childNode.getFather().getChild(i).setFather(childNode.getFather());
    }

    /**
     * @param node , set father for split node
     */
    private void setSplitFatherNode(BTNode<K> node) {
        for (int i = 0; i <= node.getSize(); i++)
            node.getChild(i).setFather(node);
    }

    /**
     * @param currentNode , process node if the keys size is overflow
     */
    private void processOverflow(BTNode<K> currentNode) {
        BTNode<K> newNode = getHalfKeys(currentNode);
        for (int i = 0; i <= newNode.getSize(); i++) {
            newNode.addChild(i, currentNode.getChild(0));
            currentNode.removeChild(0);
        }
        BTNode<K> originalNode = getRestOfHalfKeys(currentNode);
        currentNode.addChild(0, newNode);
        currentNode.addChild(1, originalNode);
        originalNode.setFather(currentNode);
        newNode.setFather(currentNode);
        setSplitFatherNode(originalNode);
        setSplitFatherNode(newNode);

        //System.out.println("Bring up the middle key");

        stepTrees.add(cloneCreate.clone(this));
    }


    public void insert(K key) {
        // If tree is empty
        if (isEmpty()) {
            root = new BTNode<K>(order);
            root.addKey(0, key);
            treeSize++;
            root.setFather(nullBTNode);
            root.addChild(0, nullBTNode);
            root.addChild(1, nullBTNode);

            //System.out.println("Insert root");

            stepTrees.add(cloneCreate.clone(this));
            return;
        }

        BTNode<K> currentNode = root;

        // If tree is not empty

        //Navigate to the location to insert the key
        while (!currentNode.isLastInternalNode()) {
            int i = 0;
            while (i < currentNode.getSize()) {
                // break if currentNode is leaf
                if (currentNode.isLastInternalNode()) {
                    i = currentNode.getSize();
                } else if (currentNode.getKey(i).compareTo(key) > 0) {
                    currentNode = currentNode.getChild(i);
                    i = 0;
                } else {
                    i++;
                }
            }
            if (!currentNode.isLastInternalNode())
                currentNode = currentNode.getChild(currentNode.getSize());
        }

        // If node is not full then insert non full
        if (!currentNode.isFull()) {
            int i = 0;
            while (i < currentNode.getSize()) {
                //insert somewhere with the key > insertKey
                if (currentNode.getKey(i).compareTo(key) > 0) {
                    currentNode.addKey(i, key);
                    currentNode.addChild(currentNode.getSize(), nullBTNode);
                    treeSize++;

                    //System.out.println("Insert non full");
//					stepMess.add("Insert " + key + "into the right place");
                    stepTrees.add(cloneCreate.clone(this));
                    return;
                } else {
                    i++;
                }
            }

            //insert at the end of the node
            currentNode.addKey(currentNode.getSize(), key);
            currentNode.addChild(currentNode.getSize(), nullBTNode);
            treeSize++;

            //System.out.println("Insert non full v2");
//			stepMess.add("Insert " + key + "into the right place");
            stepTrees.add(cloneCreate.clone(this));
        } else {
            // If node is full
            //Insert the node in the appropriate position within that node
            //Then take 1/2 key + child in the node (just inserted)
            BTNode<K> newChildNode = getHalfKeys(key, currentNode);
            for (int i = 0; i < halfNumber; i++) {
                newChildNode.addChild(i, currentNode.getChild(0));
                currentNode.removeChild(0);
            }
            newChildNode.addChild(halfNumber, nullBTNode);

            //Get half a half, thus, the current node will have only the middle key left
            // Uploading n to 1 item (to be a father)
            BTNode<K> originalFatherNode = getRestOfHalfKeys(currentNode);
            currentNode.addChild(0, newChildNode);
            currentNode.addChild(1, originalFatherNode);
            originalFatherNode.setFather(currentNode);
            newChildNode.setFather(currentNode);
            treeSize++;

            //System.out.println("Bring up the middle key");

            stepTrees.add(cloneCreate.clone(this));

            //If on the current child node the higher level node
            //and node upload on top ...
            if (!currentNode.getFather().equals(nullBTNode)) {
                while (!currentNode.getFather().isOverflow() && !currentNode.getFather().equals(nullBTNode)) {
                    boolean flag = currentNode.getSize() == 1 && !currentNode.getFather().isOverflow();
                    if (currentNode.isOverflow() || flag) {
                        mergeWithFatherNode(currentNode);
                        currentNode = currentNode.getFather();

                        //System.out.println("The Insert key is inserted into the appropriate location");

                        stepTrees.add(cloneCreate.clone(this));

                        // If it's full again, repeat the action earlier
                        if (currentNode.isOverflow()) {
                            processOverflow(currentNode);
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    /**
     * @param node , the node
     * @return the number of the node's father child index which matches the node
     */
    private int findChild(BTNode<K> node) {
        if (!node.equals(root)) {
            BTNode<K> fatherNode = node.getFather();

            for (int i = 0; i <= fatherNode.getSize(); i++) {
                if (fatherNode.getChild(i).equals(node))
                    return i;
            }
        }
        return -1;
    }

    /**
     * @param node , the node's father have different height of right and left
     *             subtree balance the unbalanced tree
     */
    private BTNode<K> balanceDeletedNode(BTNode<K> node) {
        boolean flag;
        int nodeIndex = findChild(node);
        K pair;
        BTNode<K> fatherNode = node.getFather();
        BTNode<K> currentNode;
        if (nodeIndex == 0) {
            currentNode = fatherNode.getChild(1);
            // Is the (deleted) node on the left-most side (index 0)
            flag = true;
        } else {
            currentNode = fatherNode.getChild(nodeIndex - 1);
            flag = false;
        }

        int currentSize = currentNode.getSize();
        if (currentSize > halfNumber) {
            if (flag) {
                pair = fatherNode.getKey(0);
                node.addKey(node.getSize(), pair);
                fatherNode.removeKey(0);
                pair = currentNode.getKey(0);
                currentNode.removeKey(0);
                node.addChild(node.getSize(), currentNode.getChild(0));
                currentNode.removeChild(0);
                fatherNode.addKey(0, pair);
                if (node.isLastInternalNode()) {
                    node.removeChild(0);
                }
//				System.out.println("BA1");
                //System.out.println("Case 2a:");
//				stepMess.add(" ");
                stepTrees.add(cloneCreate.clone(this));

            } else {
                pair = fatherNode.getKey(nodeIndex - 1);
                node.addKey(0, pair);
                fatherNode.removeKey(nodeIndex - 1);
                pair = currentNode.getKey(currentSize - 1);
                currentNode.removeKey(currentSize - 1);
                node.addChild(0, currentNode.getChild(currentSize));
                currentNode.removeChild(currentSize);
                fatherNode.addKey(nodeIndex - 1, pair);
                if (node.isLastInternalNode()) {
                    node.removeChild(0);
                }
//				System.out.println("BA2");
               // System.out.println("Case 2a:");
//				stepMess.add(" ");
                stepTrees.add(cloneCreate.clone(this));
            }
            return node;
        } else {
            if (flag) {
                currentNode.addKey(0, fatherNode.getKey(0));
                fatherNode.removeKey(0);
                fatherNode.removeChild(0);
                if (root.getSize() == 0) {
                    root = currentNode;
                    currentNode.setFather(nullBTNode);
                }
                if (node.getSize() == 0) {
                    currentNode.addChild(0, node.getChild(0));
                    currentNode.getChild(0).setFather(currentNode);
                }
                for (int i = 0; i < node.getSize(); i++) {
                    currentNode.addKey(i, node.getKey(i));
                    currentNode.addChild(i, node.getChild(i));
                    currentNode.getChild(i).setFather(currentNode);
                }
                // Case 2b.1
                //System.out.println("Case 2b: Merging");

                stepTrees.add(cloneCreate.clone(this));
            } else {
                currentNode.addKey(currentNode.getSize(), fatherNode.getKey(nodeIndex - 1));
                fatherNode.removeKey(nodeIndex - 1);
                fatherNode.removeChild(nodeIndex);
                if (root.getSize() == 0) {
                    root = currentNode;
                    currentNode.setFather(nullBTNode);
                }
                int currentNodeSize = currentNode.getSize();
                if (node.getSize() == 0) {
                    currentNode.addChild(currentNodeSize, node.getChild(0));
                    currentNode.getChild(currentNodeSize).setFather(currentNode);
                }
                for (int i = 0; i < node.getSize(); i++) {
                    currentNode.addKey(currentNodeSize + i, node.getKey(i));
                    currentNode.addChild(currentNodeSize + i, node.getChild(i));
                    currentNode.getChild(currentNodeSize + i).setFather(currentNode);
                }
                // Case 2b.2
                //System.out.println("Case 2b: Merging");

                stepTrees.add(cloneCreate.clone(this));
            }
            return fatherNode;
        }
    }

    /**
     * @param node , use the last internal node to replace the node
     * @return the last internal node
     */
    private BTNode<K> replaceNode(BTNode<K> node) {
        BTNode<K> currentNode = node.getChild(index + 1);
        while (!currentNode.isLastInternalNode()) {
            currentNode = currentNode.getChild(0);
        }

        if (currentNode.getSize() - 1 < halfNumber) {
            // Replaced with the nearest (oldest) son
            currentNode = node.getChild(index);
            int currentNodeSize = currentNode.getSize();
            while (!currentNode.isLastInternalNode()) {
                currentNode = currentNode.getChild(currentNodeSize);
            }
            node.addKey(index, currentNode.getKey(currentNodeSize - 1));
            currentNode.removeKey(currentNodeSize - 1);
            currentNode.addKey(currentNodeSize - 1, node.getKey(index + 1));
            node.removeKey(index + 1);
            index = currentNode.getSize() - 1;
            // Case 3a
            //System.out.println("Case 3a: Replaced with the nearest son (largest)");

            stepTrees.add(cloneCreate.clone(this));
        } else {
            // Replace with the nearest right child (minimum)
            node.addKey(index + 1, currentNode.getKey(0));
            currentNode.removeKey(0);
            currentNode.addKey(0, node.getKey(index));
            node.removeKey(index);
            index = 0;
            // Case 3b
            //System.out.println("Case 3b: Replace with the nearest right child (minimum)");

            stepTrees.add(cloneCreate.clone(this));
        }
        return currentNode;
    }

    /**
     * @param key , the key to be deleted
     */

    /*
     * Case 1: If k is in the node x which is a leaf and x.size -1 >= halfNumber
     * Case 2: If k is in the node x which is a leaf and x.size -1 < halfNumber Case
     * 3: If k is in the node x and x is an internal node (not a leaf)
     */
    public void delete(K key) {
        //System.out.println("--------------------------------------\nDelete\n--------------------------------------");

        stepTrees.add(cloneCreate.clone(this));
        // Find the node containing the key
        BTNode<K> node = getNode(key);
        BTNode<K> deleteNode = null;
        if (node.equals(nullBTNode))
            return;

        // If it is root, tree 1 node 1 key -> Delete always
        if (node.equals(root) && node.getSize() == 1 && node.isLastInternalNode()) {
            root = null;
            treeSize--;

            //System.out.println("erase corner");
            stepTrees.add(cloneCreate.clone(this));
        } else {
            boolean flag = true;
            boolean isReplaced = false;
            // TODO: case 3
            if (!node.isLastInternalNode()) {
                node = replaceNode(node);
                deleteNode = node;
                isReplaced = true;
            }

            // If delete affects the height of the tree
            if (node.getSize() - 1 < halfNumber) {
//				System.out.println("Case 2:");
                // TODO: case 2
                node = balanceDeletedNode(node);
                if (isReplaced) {
                    for (int i = 0; i <= node.getSize(); i++) {
                        for (int j = 0; i < node.getChild(i).getSize(); j++) {
                            if (node.getChild(i).getKey(j).equals(key)) {
                                deleteNode = node.getChild(i);
                                break;
                            }
                        }
                    }
                }
            } else if (node.isLastInternalNode()) {
                // TODO: Case 1
               // System.out.println("Case 1: Delete");
                node.removeChild(0);
            }

            while (!node.getChild(0).equals(root) && node.getSize() < halfNumber && flag) {
//				System.out.println("Debug3");
                //System.out.println("This is case 3c: Recursively delete");
                if (node.equals(root)) {
                    for (int i = 0; i <= root.getSize(); i++) {
                        if (root.getChild(i).getSize() == 0) {
                            flag = true;
                            break;
                        } else {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    node = balanceDeletedNode(node);
                }
            }

            if (deleteNode == null) {
                // Check whether previously deleted or just rebalance / replace
                node = getNode(key);
            } else {
                node = deleteNode;
            }

            if (!node.equals(nullBTNode)) {
                //After replace is completed, delete node di (then, node has become)
                for (int i = 0; i < node.getSize(); i++) {
                    if (node.getKey(i) == key) {
                        node.removeKey(i);
                    }
                }
                treeSize--;

                //System.out.println("delete " + key);
                stepTrees.add(cloneCreate.clone(this));
            }
        }
    }


}

class cloneCreate {

    public static <T extends Serializable> T clone(T object) {
        T cloneObject = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cloneObject = (T) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloneObject;
    }
}
