package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        for (int n = 0, N = 1000; n < 8; n++, N *= 2) {
            runWithDataSizeOf(N, Ns, times, opCounts);
        }

        printTimingTable(Ns, times, opCounts);
    }

    /**
     * Run with data size of n and populate the Ns, times and opCounts.
     */
    private static void runWithDataSizeOf(int n, AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        AList<Integer> list = new AList<>();
        Stopwatch stopwatch = new Stopwatch();
        int opCount = 0;
        
        for (int i = 0; i < n; i++) {
            list.addLast(i);
            opCount++;
        }

        double timeInSeconds = stopwatch.elapsedTime();

        Ns.addLast(n);
        times.addLast(timeInSeconds);
        opCounts.addLast(opCount);
    }
}
