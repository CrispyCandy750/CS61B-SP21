package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        for (int i = 0, N = 1000; i < 7; i++, N *= 2) {
            runWithDataSizeOf(N, Ns, times, opCounts);
        }

        printTimingTable(Ns, times, opCounts);
    }

    /**
     * Run with data size of n and populate the Ns, times and opCounts.
     */
    private static void runWithDataSizeOf(int N, AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        SLList<Integer> list = new SLList<>();
        for (int i = 0; i < N; i++) {
            list.addFirst(i);
        }

        int opCount = 0;
        Stopwatch stopwatch = new Stopwatch();
        for (int i = 0; i < N; i++) {
            list.getLast();
            opCount++;
        }
        double timeInSeconds = stopwatch.elapsedTime();
        Ns.addLast(N);
        times.addLast(timeInSeconds);
        opCounts.addLast(opCount);
    }
}
