import java.lang.Thread;

class MyThread extends Thread {

    public static int amount = 0;

    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
        }

        System.out.println("Main: " + amount);
        amount++;
        System.out.println("Main: " + amount);
    }

    public void run() {
        amount++;
    }
}
