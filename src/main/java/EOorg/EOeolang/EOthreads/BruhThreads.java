package EOorg.EOeolang.EOthreads;

import org.eolang.Phi;

import java.util.concurrent.ConcurrentHashMap;

/**
 * All threads.
 *
 * @since 0.0
 */
final class BruhThreads {
    public static final BruhThreads INSTANCE = new BruhThreads();

    private final ConcurrentHashMap<Phi, EOThread> all_eothreads = new ConcurrentHashMap<>(0);

    /**
     * Ctor
     */
    private BruhThreads(){
        // Singleton
    }

    public EOThread get(final Phi thread){
        return all_eothreads.get(thread);
    }

    public static class EOThread extends Thread{
        @Override
        public void run(){
            System.out.println("Started, go sleeping...");
            try {
                sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Wake up!!");
        }
    }
}
