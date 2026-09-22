package supermarket.Models;

public class LinkedList<T> {
    private Node<T> head;
    private int size;

    public interface Condition<T> {
        boolean test(T item);
    }

    public LinkedList() {
        head = null;
        size = 0;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public T get(int index) {
        if (index < 0 || index >= size) return null;

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) return;

        if (index == 0) {
            head = head.next;
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
        }
        size--;
    }

    // search by condition
        public T find(Condition<T> condition) {
        Node<T> current = head;
        while (current != null) {
            if (condition.test(current.data)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

   // search all elements
    public LinkedList<T> findAll(Condition<T> condition) {
        LinkedList<T> results = new LinkedList<>();
        Node<T> current = head;
        while (current != null) {
            if (condition.test(current.data)) {
                results.add(current.data);
            }
            current = current.next;
        }
        return results;
    }
}