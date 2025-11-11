package project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskFactory {
    private int counter = 1;
    private final Random rnd = new Random(42);

    public List<task> initial(int n, int minBurst, int maxBurst, int prioMin, int prioMax) {
        List<task> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int burst = rnd.nextInt(Math.max(1, maxBurst - minBurst + 1)) + minBurst;
            int prio  = rnd.nextInt(Math.max(1, prioMax - prioMin + 1)) + prioMin;
            list.add(new task("T" + (counter++), burst, 0, prio));        // arrival=0
        }
        return list;
    }

    public List<task> maybeArrive(int nowTick, int maxNew, int minBurst, int maxBurst, int prioMin, int prioMax) {
        List<task> list = new ArrayList<>();
        int count = rnd.nextInt(maxNew + 1); // 0..maxNew
        for (int i = 0; i < count; i++) {
            int burst = rnd.nextInt(Math.max(1, maxBurst - minBurst + 1)) + minBurst;
            int prio  = rnd.nextInt(Math.max(1, prioMax - prioMin + 1)) + prioMin;
            list.add(new task("T" + (counter++), burst, nowTick, prio));  // arrival=nowTick
        }
        return list;
    }
}
