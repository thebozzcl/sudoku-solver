package cl.bozz.sudokusolver.algorithm.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class NoRepeatStack<E> {
    final Stack<E> innerStack = new Stack<>();
    final Set<Integer> hashes = new HashSet<>();

    public boolean push(final E element) {
        if (hashes.contains(element.hashCode())) {
            return false;
        }
        hashes.add(element.hashCode());
        innerStack.push(element);
        return true;
    }

    public E pop() {
        return innerStack.pop();
    }

    public E peek() {
        return innerStack.peek();
    }

    public boolean isEmpty() {
        return innerStack.isEmpty();
    }

    public int size() {
        return innerStack.size();
    }

    public int hashesSize() {
        return hashes.size();
    }
}
