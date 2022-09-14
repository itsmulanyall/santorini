package it.polimi.ingsw.client.view.cli;

import java.io.InputStreamReader;
import java.util.Scanner;

import static it.polimi.ingsw.client.view.cli.CliConstants.*;

public class AsyncReader implements Runnable{

    private final Scanner scanner;
    private String input=null;
    private final String regExToMatch;
    private final TimerThread tt;
    private final ViewCLI view;
    private boolean done=false;

    /**
     * reads asynchronously with a timer some data
     * @param view to print messages
     * @param regExToMatch regular expression to match in the input
     * @param timerSeconds seconds available
     */
    public AsyncReader(ViewCLI view, String regExToMatch, int timerSeconds) {
        scanner = new Scanner(new InputStreamReader(System.in));
        tt = new TimerThread(view, timerSeconds);
        this.regExToMatch = regExToMatch;
        this.view = view;
    }

    /**
     * creates the timer thread and starts reading until is interrupted or user puts a input that matches the regular expression
     */
    @Override
    public void run() {
        Thread t0 = new Thread(tt);
        tt.setLockable(Thread.currentThread());
        t0.start();
        while(input==null || !input.matches(regExToMatch)) {
            view.setCursor();
            try {
                do {
                    Thread.sleep(100);
                } while(!scanner.hasNextLine());
                input = scanner.nextLine();
                view.getCliFramework().printScreen();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        tt.lock();
        done = true;
    }


    /**
     * checks if the input has been set
     * @return true if it's set
     */
    public boolean isDone() {
        return done;
    }

    /**
     * gets the input
     * @return the input
     */
    public String getInput() {
        return input;
    }

    /**
     * gets the input as an int, needs to be checked that isDone before
     * @return parsed input
     */
    public int getInputAsInt() {
        return Integer.parseInt(input);
    }


    private static class TimerThread implements Runnable {

        private int timeLeft;
        private boolean running = true;
        private Thread lockable;
        private final ViewCLI view;

        /**
         * creates a timed clock that wakes up step down a counter and then goes to sleep again until 0 or interrupted with lock
         * @param view to print seconds remaining to
         * @param time to set
         */
        private TimerThread(ViewCLI view, int time) {
            timeLeft = time;
            this.view = view;
        }

        /**
         * sets the reader to lock if timer runs out
         * @param lockable thread reader to stop
         */
        private void setLockable(Thread lockable) {
            this.lockable = lockable;
        }

        /**
         * lowers the timer then sleeps for 1 sec, until zero or stopped from running, if run out of time interrupts the reader
         */
        @Override
        public void run() {
            while(running) {
                if(timeLeft > 0) {
                    if(timeLeft % 20 == 0)
                        view.printLiveTimerMessage(TIME_LEFT + " " + timeLeft / 60 + MINUTES + " " + timeLeft % 60 + SECONDS);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {}
                    timeLeft--;
                } else {
                    lockable.interrupt();
                    view.printLiveTimerMessage(TIME_EXPIRED);
                    running = false;
                }
            }
        }

        /**
         * lock to stop timer
         */
        private void lock() {
            running = false;
        }

    }
}
