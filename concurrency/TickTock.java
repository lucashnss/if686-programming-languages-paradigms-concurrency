class TickTock {
    String state; // contains the state of the clock

    synchronized void tick(boolean running) {
        if (!running) { // stop the clock
            state = "ticked";
            notify(); // notify any waiting thread
            return;
        }
        System.out.print("Tick ");
        state = "ticked"; // set the current state to ticked
        try {
            Thread.sleep(500); // wait for half a second
        } catch (InterruptedException exc) {
            System.out.println("Thread interrupted.");
        }
        notify(); // let tock() run
        try {
            while (!state.equals("tocked"))
                wait(); // wait until tock() is done
        } catch (InterruptedException exc) {
            System.out.println("Thread interrupted.");
        }
    }

    synchronized void tock(boolean running) {
        if (!running) { // stop the clock
            state = "tocked";
            notify(); // notify any waiting thread
            return;
        }
        System.out.println("Tock");
        state = "tocked"; // set the current state to tocked
        try {
            Thread.sleep(500); // wait for half a second
        } catch (InterruptedException exc) {
            System.out.println("Thread interrupted.");
        }
        notify(); // let tick() run
        try {
            while (!state.equals("ticked"))
                wait(); // wait until tick() is done
        } catch (InterruptedException exc) {
            System.out.println("Thread interrupted.");
        }
    }

    public static void main(String[] args) {
        TickTock tt = new TickTock();

        MyThread mt1 = new MyThread("Tick", tt);
        MyThread mt2 = new MyThread("Tock", tt);

        try {
            mt1.t.join();
            mt2.t.join();
        } catch (InterruptedException exc) {
            System.out.println("Main thread interrupted.");
        }
    }
}

class MyThread implements Runnable {
    Thread t;
    TickTock tt;

    MyThread(String name, TickTock tt) {
        t = new Thread(this, name);
        this.tt = tt;
        t.start();
    }

    public void run() {
        if (t.getName().equals("Tick")) {
            for (int i = 0; i < 5; i++) tt.tick(true);
            tt.tick(false);
        } else {
            for (int i = 0; i < 5; i++) tt.tock(true);
            tt.tock(false);
        }
    }
}
