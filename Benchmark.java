package mergesort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Benchmark {

    public static void main(String[] args) throws IOException {
        // Write the datasets to files
        int[] setSizes = {10000, 100000, 300000, 500000, 800000, 1000000,
            3000000, 5000000, 8000000, 10000000};

        // Generate datasets
       for (int i = 0; i < setSizes.length; i++) {
           int[] set = generateSet(setSizes[i]);
           writeDataset("DataSet_" + (i + 1), set);
       }

        // Available cores
        int cores = Runtime.getRuntime().availableProcessors() - 1;
        System.out.println("Cores available: " + cores);

       long[] benchmark = new long[10];

       // Run benchmarks and print the measured time in ns
       for (int i = 0; i < setSizes.length; i++) {
           String filename = "DataSet_" + (i + 1);
           int[] set = readDataset(filename);

           benchmark[i] = measureParallel(set);
           System.out.println(filename + "\t Parallel \t" + benchmark[i] + " ms");

           benchmark[i] = measureSequential(set);
           System.out.println(filename + "\t Sequential \t" + benchmark[i] + " ms");
       }


       // Write results to CSV file
       BufferedWriter outputWriter;
       outputWriter = new BufferedWriter(new FileWriter("Benchmark-Sequential.csv"));
       for (int j = 0; j < benchmark.length; j++) {
           String filename = "DataSet_" + (j + 1);
           outputWriter.write(setSizes[j] + ",Sequential," + benchmark[j] + "\n");
       }
       outputWriter.flush();
       outputWriter.close();
    }

    public static long measureSequential(int[] unsorted) {
        // Sequential benchmark run      
        MergeSort ob2 = new MergeSort();

        long startTime = System.nanoTime();
        ob2.sort(unsorted, 0, unsorted.length - 1);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;
        return duration;
    }

    public static int measureParallel(int[] unsorted) {
        // Parallel benchmark run
        long startTime = System.nanoTime();
        ParallelMergeSort.sort(unsorted, 0, unsorted.length - 1);
        long endTime = System.nanoTime();

        int duration = (int) (endTime - startTime) / 1000000;
        return duration;
    }

    public static int[] generateSet(int size) {
        Random random = new Random();
        int arr[] = new int[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(size);
        }
        return arr;
    }

    public static void writeDataset(String filename, int[] x) throws IOException {
        BufferedWriter outputWriter;
        outputWriter = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < x.length; i++) {
            outputWriter.write(x[i] + ",");
        }
        outputWriter.flush();
        outputWriter.close();
    }

    public static int[] readDataset(String filename) throws IOException {
        BufferedReader in = new BufferedReader(
                new FileReader(filename));
        String str;
        String[] num = null;

        try {
            while ((str = in.readLine()) != null) {
                num = str.split(",");
            }
            in.close();
        } catch (IOException e) {
            System.out.println("File Read Error");
        }

        int[] result = new int[num.length];

        for (int i = 0; i < num.length; i++) {
            result[i] = Integer.parseInt(num[i]);
        }
        return result;
    }
}
