package EOorg.EOeolang;

import org.eolang.Phi;

import java.util.concurrent.ConcurrentHashMap;

/**
 * All threads.
 *
 * @since 0.0
 */
final class Threads {
    public static final Threads INSTANCE = new Threads();

    private final ConcurrentHashMap<Phi, EOThread> all = new ConcurrentHashMap<>(0);
}

class EOThread extends Thread{
    @Override
    public void run(){
        System.out.println("Hello world!");
    }
}