import java.util.*;

public class EDLinkedHashSet<T> implements Set<T> {

    private class Node {
        T data;
        Node hnext = null;   // siguiente nodo en la cadena de la cubeta de la tabla
        Node prev = null;    // anterior en la lista de orden de inserción
        Node next = null;    // siguiente en la lista de orden de inserción
    }

    static final private int DEFAULT_CAPACITY = 11;
    static final private double DEFAULT_LOAD_FACTOR = 2.0;

    Node[] table;
    Node header;
    int size = 0;
    int rehashThreshold;
    double loadFactor;

    // ------------------------------------------------------------------
    // Métodos que se dan implementados
    // ------------------------------------------------------------------

    public EDLinkedHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /** Posición de la tabla en la que dispersa item (admite null). */
    private int hash(Object item) {
        if (item == null)
            return 0;
        return (item.hashCode() & Integer.MAX_VALUE) % table.length;
    }

    /** Compara dos elementos teniendo en cuenta que alguno puede ser null. */
    private boolean equalsNull(Object a, Object b) {
        if (a == null)
            return b == null;
        return a.equals(b);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** Recorre directamente la lista de orden desde el nodo cabecera. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node n = header.next;
        while (n != header) {
            sb.append(n.data);
            n = n.next;
            if (n != header)
                sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (T e : this)
            result[i++] = e;
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E[] toArray(E[] a) {
        if (a.length < size)
            a = (E[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        for (T e : this)
            a[i++] = (E) e;
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = false;
        for (T e : c)
            if (add(e))
                changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object e : c)
            if (remove(e))
                changed = true;
        return changed;
    }

    // ------------------------------------------------------------------
    // Ejercicio 1
    // ------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public EDLinkedHashSet(int capacity, double loadFactor) {

       if (capacity == 0 || loadFactor == 0) {
           throw new IllegalArgumentException("Capacidad o factor de carga inválidos");
       }
       this.loadFactor = loadFactor;
       this.rehashThreshold = (int) (capacity * loadFactor);
       this.table = new EDLinkedHashSet.Node[capacity];

       this.header = new Node();
       this.header.next = this.header;
       this.header.prev = this.header;
    }

    // ------------------------------------------------------------------
    // Ejercicio 2
    // ------------------------------------------------------------------

    private void linkLast(Node n) {
        Node last = this.header.prev;
        n.prev = last;
        n.next = this.header;
        last.next = n;
        this.header.prev = n;
    }

    private void unlink(Node n) {
       n.prev.next = n.next;
       n.next.prev = n.prev;
       n.prev = null;
       n.next = null;
    }

    // ------------------------------------------------------------------
    // Ejercicio 3
    // ------------------------------------------------------------------

    private Node findNode(Object item) {
        int indice = hash(item);
        Node actual = table[indice];

        while(actual != null) {
            if (equalsNull(item, actual.data)) {
                return actual;
            }
            actual = actual.next;
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Ejercicio 4
    // ------------------------------------------------------------------

    @Override
    public boolean contains(Object item) {
        return findNode(item) != null;
    }

    // ------------------------------------------------------------------
    // Ejercicio 5
    // ------------------------------------------------------------------

    @Override
    public boolean add(T item) {
        if (contains(item)) {
            return false;
        }
        int indice = hash(item);
        Node aux = new Node();
        aux.data = item;
        aux.hnext  = table[indice];
        table[indice] = aux;
        linkLast(aux);
        this.size++;
        if (size > rehashThreshold) {
            rehash();
        }
        return true;
    }

    // ------------------------------------------------------------------
    // Ejercicio 6
    // ------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private void rehash() {
        int newCapacity = (table.length * 2) + 1;
        table = new EDLinkedHashSet.Node[newCapacity];
        this.rehashThreshold = (int) (newCapacity * this.loadFactor);

        Node actual = this.header.next;

        while(actual != header) {
            int indice = hash(actual.data);
            actual.hnext = table[indice];
            table[indice] = actual;
            actual = actual.next;
        }
    }

    // ------------------------------------------------------------------
    // Ejercicio 7
    // ------------------------------------------------------------------

    @Override
    public boolean remove(Object item) {
        if (! contains(item)) {
            return false;
        }
        int indice = hash(item);
        Node actual = table[indice];
        while (actual != null) {
            if (equalsNull(item, actual.data)) {
                if (actual == table[indice]) {
                    table[indice] = actual.hnext;
                }
            }
            actual = actual.next;
        }

        return true;
    }

    // ------------------------------------------------------------------
    // Ejercicio 8
    // ------------------------------------------------------------------

    @Override
    public void clear() {
        //TODO Ejercicio 8
        throw new UnsupportedOperationException("Ejercicio 8: por implementar");
    }

    // ------------------------------------------------------------------
    // Ejercicio 9
    // ------------------------------------------------------------------

    private class EDIterator implements Iterator<T> {
        Node current = header.next;   // nodo que devolverá next
        Node lastReturned = null;     // último nodo devuelto por next

        @Override
        public boolean hasNext() {
            //TODO Ejercicio 9
            throw new UnsupportedOperationException("Ejercicio 9: por implementar");
        }

        @Override
        public T next() {
            //TODO Ejercicio 9
            //Debe actualizar lastReturned para que lo use remove
            throw new UnsupportedOperationException("Ejercicio 9: por implementar");
        }

        @Override
        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();
            EDLinkedHashSet.this.remove(lastReturned.data);
            lastReturned = null;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new EDIterator();
    }

    // ------------------------------------------------------------------
    // Ejercicio 10
    // ------------------------------------------------------------------

    @Override
    public boolean containsAll(Collection<?> c) {
        //TODO Ejercicio 10
        throw new UnsupportedOperationException("Ejercicio 10: por implementar");
    }

    // ------------------------------------------------------------------
    // Ejercicio 11
    // ------------------------------------------------------------------
    @Override
    public boolean retainAll(Collection<?> c) {
        //TODO Ejercicio 11
        throw new UnsupportedOperationException("Ejercicio 11: por implementar");
    }
}
