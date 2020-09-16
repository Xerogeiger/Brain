package com.ajax.brain.utils.sorts;

import java.util.Stack;

public final class TreeSort<T> extends Sort<T>{

    /**
     * Creates a new {@code TreeSort} with the {@code Representor}
     *
     * @param representor the {@code Representor} for converting objects to integers
     * @throws NullPointerException if the representor is null
     */
    private TreeSort(Representor<T> representor) {
        super(representor);
    }

    /**
     * Sorts the provided array with a TreeSort
     *
     * @param arr the array to sort
     */
    @Override
    public void sort(T[] arr) {
        if(arr.length == 0) {
            return;
        }

        Node head = new Node(arr[0]);

        for(int x = 1; x < arr.length; x++) {
            head.place(arr[x]);
        }

        head.getInOrder(arr);
    }

    /**
     * Sorts the provided array by constructing a new {@code TreeSort} object then using it
     * @param arr the array to sort
     * @param representor the {@code Representor} for the provided object type
     * @param <T> the object type of the array
     */
    public static <T> void sort(T[] arr, Representor<T> representor) {
        new TreeSort<T>(representor).sort(arr);
    }

    /**
     * Returns a new {@code TreeSort} object to be used multiple times
     * @param representor the {@code Representor} for the provided object type
     * @param <T> the object type of the array
     */
    public static <T> TreeSort<T> getInstance(Representor<T> representor) {
        return new TreeSort<>(representor);
    }

    /**
     * The {@code Node} class represents a Binary Tree node
     *
     * A binary tree is a structure for storing elements in a fast and sorted way
     * If a element is less than or equal the head it goes to the left
     *        3
     *       / \
     *      1   4
     *     /\    \
     *    0  2    5
     */
    private class Node {
        /**
         * The node to the left of this one
         *
         * The left node is less than or equal to this node
         */
        Node left;

        /**
         * The node to the right of this one
         *
         * The right node is greater than this one
         */
        Node right;

        /**
         * The stored value of this node
         */
        T value;

        /**
         * Creates a new Binary Tree node
         *
         * @param value the value for the node to store
         */
        Node(T value) {
            this.value = value;
        }

        /**
         * Places the node in the correct spot for the Binary Tree
         *
         * @param other the value to place
         */
        void place(T other) {
            if(representor.convert(other) > representor.convert(this.value)) {
                if(right != null) {
                    right.place(other);
                } else {
                    right = new Node(other);
                }
            } else {
                if(left != null) {
                    left.place(other);
                } else {
                    left = new Node(other);
                }
            }
        }

        /**
         * Puts the Binary Tree into the array in order
         *
         * @param arr the array to put the Binary Tree in
         */
        void getInOrder(T[] arr) {
            Stack<Node> nodeStack = new Stack<>();

            Node current = this;
            int index = 0;

            while (!nodeStack.isEmpty() || current != null){
                if(current != null) {
                    nodeStack.push(current);
                    current = current.left;
                } else {
                    Node n = nodeStack.pop();
                    arr[index++] = n.value;
                    current = n.right;
                }
            }
        }
    }
}
