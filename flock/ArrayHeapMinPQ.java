package flock;
import java.util.*;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T>{

    private ArrayList<T> heap;
    private ArrayList<Double> pheap;
    private int inicapa = 16;
    private int size = 0;
    private int sizecell = 1;

    public ArrayHeapMinPQ() {
        heap = new ArrayList<>(inicapa);
        pheap = new ArrayList<>(inicapa);
    }

//    public ArrayHeapMinPQ(T item, Double p) {
//        heap = new ArrayList<>(inicapa);
//        pheap = new ArrayList<>(inicapa);
//
//        heap.add(item);
//        pheap.add(p);
//        size += 1;
//        if (size >= sizecell) {
//            sizecell += (sizecell + 1);
//            height += 1;
//        }
//    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            return;
            // throw new IllegalArgumentException();
        }
        size += 1;
        heap.add(item);
        pheap.add(priority);
        if (size >= sizecell) {
            sizecell += (sizecell + 1);
        }
        if (size == inicapa) {
            resize();
        }
        if (size == 1) {return;}
        up(size-1);
    };

    @Override
    public boolean contains(T item) {
        return heap.contains(item);
    };

    @Override
    public T getSmallest(){
        if (heap.get(0) == null) {
            throw new NoSuchElementException();
        }
        return heap.get(0);
    };

    @Override
    public T removeSmallest(){
        if (size == 0) {
            throw new NoSuchElementException();
        }
        T smt = heap.get(0);
        heap.remove(0);
        pheap.remove(0);
        size -= 1;
        if(size == 0) {
            return smt;
        }
        heap.add(0, heap.get(size-1));
        pheap.add(0, pheap.get(size-1));
        heap.remove(size);
        pheap.remove(size);
        if (size < (sizecell+1)/2) {
            sizecell -= (sizecell + 1)/2;
        }
        down(0);
        return smt;
    }

    @Override
    public int size(){
        return size;
    };

    @Override
    public void changePriority(T item, double priority){
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int ind = heap.indexOf(item);
        pheap.add(ind, priority);

        if (priority > pheap.get(ind + 1)) {
            down(ind);
        }
        else if (priority < pheap.get(ind - 1)) {
            up(ind);
        }
    };

    private void resize(){
        inicapa = inicapa * 2;
        ArrayList<T> newheap = new ArrayList<>(inicapa);
        ArrayList<Double> newpheap = new ArrayList<>(inicapa);
        for (int i = 0; i < size; i++) {
            newheap.add(heap.get(i));
            newpheap.add(pheap.get(i));
        }
    }

    private void swap(int i1, int i2){
        T tempitem1 = heap.get(i1);
        Double tempp1 = pheap.get(i1);
        T tempitem2 = heap.get(i2);
        Double tempp2 = pheap.get(i2);

        heap.remove(i1);
        pheap.remove(i1);
        heap.add(i1, tempitem2);
        pheap.add(i1, tempp2);

        heap.remove(i2);
        pheap.remove(i2);
        heap.add(i2, tempitem1);
        pheap.add(i2, tempp1);
    }

    private void down(int ind) {
        int son1;
        int son2;
        int parent = ind;
        double priority = pheap.get(ind);
        while (parent < (sizecell - 1)/2) {
            son1 = parent * 2 + 1;
            son2 = parent * 2 + 2;
            if (son1 >= size){
                break;
            }
            else if(son2 >= size) {
                if (pheap.get(son1) < priority) {
                    swap(parent, son1);
                    parent = son1;
                }
                else {break;}
            }
            else if (pheap.get(son1) <= pheap.get(son2) && pheap.get(son1) < priority){
                swap(parent, son1);
                parent = son1;
            }
            else if (pheap.get(son1) > pheap.get(son2) && pheap.get(son2) < priority){
                swap(parent, son2);
                parent = son2;
            }
            else {
                break;
            }
        }
    }


    private void up(int ind) {
        int parent = ind;
        int son;
        double checkp = 1;
        double priority = pheap.get(ind);
        while (parent > 0) {
            son = parent;
            parent = (parent - 1)/2;
            checkp = pheap.get(parent);
            if (checkp > priority) {
                swap(parent, son);
            }
            else {
                break;
            }
        }
    }
}
