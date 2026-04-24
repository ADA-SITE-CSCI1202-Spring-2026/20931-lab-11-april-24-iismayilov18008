public class WaitNotifyDemo {

    static class SharedResource {
        private int value;
        private boolean changed = false;

        public synchronized int get() {
            while (!changed) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            changed = false;
            notify();
            return value;
        }

        public synchronized void set(int value) {
            while (changed) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            this.value = value;
            changed = true;
            notify();
        }
    }

    static class Producer implements Runnable {
        private final SharedResource resource;

        Producer(SharedResource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                resource.set(i * 10);
                System.out.println("Produced: " + (i * 10));
            }
        }
    }

    static class Consumer implements Runnable {
        private final SharedResource resource;

        Consumer(SharedResource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                int val = resource.get();
                System.out.println("Consumed: " + val);
            }
        }
    }

    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Thread producer = new Thread(new Producer(resource));
        Thread consumer = new Thread(new Consumer(resource));

        consumer.start();
        producer.start();
    }
}
