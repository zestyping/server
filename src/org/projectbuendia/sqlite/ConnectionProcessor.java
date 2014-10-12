package org.projectbuendia.sqlite;


import org.projectbuendia.fileops.Logging;

import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Pim de Witte
 */
public final class ConnectionProcessor implements Runnable {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    private final SQLITEConnection connection;
    private final Thread thread;
    private boolean running;
    private final Queue<Item> items = new ConcurrentLinkedQueue<Item>();
    private final Object lock = new Object();


    public ConnectionProcessor(SQLITEConnection connection) {
        this.thread = new Thread(this);
        this.connection = connection;
    }


    public void start() {
        if (running) {
            throw new IllegalStateException("The processor is already running.");
        }
        thread.start();
    }

    public void stop() {
        if (!running) {
            throw new IllegalStateException("The processor is already stopped.");
        }
        running = false;
    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    @Override
    public void run() {

        running = true;
        MainLoop:
        while (running) {
            if (!connection.isConnected()) {
                while (!connection.connect()) {
                }
            }

            while (!items.isEmpty()) {
                //synchronized (lock) {
                Item item = items.peek();
                if (!item.canExecute()) {
                    items.remove();
                    continue;
                }
                try {
                    if (!item.execute(connection)) {
                        continue MainLoop;
                    }
                } catch (SQLException e) {
                    Logging.log("Lost con", e);
                }
                items.remove();
                //}
            }
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
        connection.close();
    }

    public void waitForPendingPackets() {
        while (!items.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public boolean executeStatement(SQLiteStatement statement) {

        if (!connection.isConnected()) {
            synchronized (this) {
                this.notify();
            }
            return false;
        }
        boolean result = items.offer(statement);
        synchronized (this) {
            this.notify();
        }
        return result;
    }

    public boolean executeQuery(SQLiteQuery query) {

        if (!connection.isConnected()) {
            synchronized (this) {
                this.notify();
            }
            return false;
        }
        boolean result = items.offer(query);
        synchronized (this) {
            this.notify();
        }
        return result;
    }

    public boolean forceQuery(SQLiteQuery query) {
        boolean result = items.offer(query);
        synchronized (this) {
            this.notify();
        }
        return result;
    }

    public boolean forceUpdate(SQLiteUpdate update) {

        boolean result = items.offer(update);
        if (!connection.isConnected()) {
            return false;
        }
        return update.execute(connection);
    }

    public boolean executeUpdate(SQLiteUpdate update) {

        boolean result = items.offer(update);
        synchronized (this) {
            this.notify();
        }
        return result;
    }

    public boolean blockingQuery(SQLiteQuery update) throws SQLException {

        synchronized (lock) {
            if (!connection.isConnected()) {
                return false;
            }
            return update.execute(connection);
        }
    }
}
