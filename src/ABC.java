public class ABC {
    static String ch = "A";

    public static void main(String[] args) {

        Object lock = new Object();

        class Homework4 implements Runnable {
            private String str;
            private String nextStr;

            public Homework4(String str, String nextB) {
                this.str = str;
                this.nextStr = nextB;
            }

            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    synchronized (lock) {
                        try {
                            while (!ch.equals(str))
                                lock.wait();
                            System.out.print(str);
                            ch = nextStr;
                            Thread.sleep(100);
                            lock.notifyAll();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        new Thread(new Homework4("A", "B")).start();
        new Thread(new Homework4("B", "C")).start();
        new Thread(new Homework4("C", "A")).start();
    }
}
