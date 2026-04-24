public class DynamicScalingDemo {

    static class MathTask implements Runnable {
        private final int factor;

        MathTask(int factor) {
            this.factor = factor;
        }

        @Override
        public void run() {
            long total = 0;
            for (int i = 0; i < 10_000_000; i++) {
                total += (long) i * i * i + i * factor;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int cores = Runtime.getRuntime().availableProcessors();

        long startSingle = System.currentTimeMillis();
        Thread single = new Thread(new MathTask(2));
        single.start();
        single.join();
        long endSingle = System.currentTimeMillis();

        Thread[] workers = new Thread[cores];

        long startMulti = System.currentTimeMillis();
        for (int i = 0; i < cores; i++) {
            workers[i] = new Thread(new MathTask(i + 1));
            workers[i].start();
        }

        for (Thread t : workers) {
            t.join();
        }
        long endMulti = System.currentTimeMillis();

        System.out.println("Cores: " + cores);
        System.out.println("Single thread time: " + (endSingle - startSingle) + " ms");
        System.out.println("Multi thread time: " + (endMulti - startMulti) + " ms");
    }
}
