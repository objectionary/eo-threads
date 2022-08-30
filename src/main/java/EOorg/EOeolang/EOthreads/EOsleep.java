package EOorg.EOeolang.EOthreads;

import org.eolang.*;

/**
 * SLEEP.
 *
 * @since 0.0
 * @checkstyle TypeNameCheck (5 lines)
 */
@XmirObject(oname = "sleep")
public class EOsleep extends PhDefault {

    /**
     * Ctor.
     * @param sigma Sigma
     */
    public EOsleep(final Phi sigma) {
        super(sigma);
        this.add("millis", new AtFree());
        this.add(
                "φ",
                new AtComposite(
                        this,
                        rho -> {
                            long millis = new Param(rho, "millis").strong(Long.class);
                            if (millis < 0){
                                throw new ExFailure(
                                        "You can not sleep for negative millis value"
                                );
                            }
                            Thread.sleep(millis);
                            return new Data.ToPhi(true);
                        }
                )
        );
    }

}
