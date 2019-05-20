package mergesort;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


public class ParallelMergeSort {
 
    private static final ForkJoinPool threadPool = new ForkJoinPool() ;
    private static final int SIZE_THRESHOLD = 16; // Threshold before switcher to insertionsort
 
    public static void sort(int[] a) {
        sort(a, 0, a.length-1);
    }
 
    public static void sort(int[] a, int lo, int hi) {
        if (hi - lo < SIZE_THRESHOLD) {
            insertionsort(a, lo, hi);
            return;
        }
 
        int[] tmp = new int[a.length];
        threadPool.invoke(new SortTask(a, tmp, lo, hi));
    }
 

    public static class SortTask extends RecursiveAction {
        int[] a;
        int[] tmp;
        int lo, hi;
        public SortTask(int[] a, int[] tmp, int lo, int hi) {
            this.a = a;
            this.lo = lo;
            this.hi = hi;
            this.tmp = tmp;
        }
 
        @Override
        protected void compute() {
            if (hi - lo < SIZE_THRESHOLD) {
                insertionsort(a, lo, hi);
                return;
            }
 
            int m = (lo + hi) / 2;
            invokeAll(new SortTask(a, tmp, lo, m), new SortTask(a, tmp, m+1, hi));
            merge(a, tmp, lo, m, hi);
        }
    }
 
    private static void merge(int[] a, int[] b, int lo, int m, int hi) {
        if (a[m] <= a[m+1])
            return;
 
        System.arraycopy(a, lo, b, lo, m-lo+1);
 
        int i = lo;
        int j = m+1;
        int k = lo;
 
        while (k < j && j <= hi) {
            if (b[i] <= a[j]) {
                a[k++] = b[i++];
            } else {
                a[k++] = a[j++];
            }
        }
 
        System.arraycopy(b, i, a, k, j-k);
    }
 
    // Insertionsort te be used for small tasks
    private static void insertionsort(int[] a, int lo, int hi) {
        for (int i = lo+1; i <= hi; i++) {
            int j = i;
            int t = a[j];
            while (j > lo && t < a[j - 1]) {
                a[j] = a[j - 1];
                --j;
            }
            a[j] = t;
        }
    }
}
