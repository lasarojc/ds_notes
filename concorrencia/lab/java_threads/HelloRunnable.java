import java.util.*;

public class HelloRunnable implements Runnable {
    static Counter c = new Counter();

    public void run() {
        for (int i = 0; i < 20; i ++)
        {
            synchronized(this)
            {
            System.out.println(Thread.currentThread().getName() + " counter " + c.increment());
        }
        }
    }

    public static void main(String args[]) {
        Set<Thread> threads = new HashSet<>();
        for (int i = 0; i < 10; i ++)
        {
            Thread t = new Thread(new HelloRunnable(), "Hello-" + i);
            //t.setDaemon(true);
            threads.add(t);
            t.start();
        }

        for (Thread t: threads)
        {
            try {
                t.join();
            } catch (InterruptedException ie) {
                System.out.println("got tired of waiting");
            }
        }
    }
}


class Counter {
    private int c = 0;

    public int increment() {
        return ++c;
    }

    public int decrement() {
        return --c;
    }

    public int value() {
        return c;
    }
}