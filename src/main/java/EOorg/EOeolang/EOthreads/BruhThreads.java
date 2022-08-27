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
        return all_eothreads.computeIfAbsent(
            thread,
            key ->{
                System.out.println("Created new thread");
                return new EOThread(null);
            }
        );
    }
    public void put(final Phi phi, final EOThread eoThread){
        all_eothreads.put(phi, eoThread);
    }

    public static class EOThread extends Thread{
        private Phi slow_obj;
        public EOThread(Phi slow_obj){
            this.slow_obj = slow_obj;
        }
        @Override
        public void run(){
            System.out.println("Started, go sleeping...");
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Wake up!!");
        }
    }
}
