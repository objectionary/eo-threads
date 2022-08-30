package EOorg.EOeolang.EOthreads;

import org.eolang.AtComposite;
import org.eolang.AtFree;
import org.eolang.Data;
import org.eolang.Param;
import org.eolang.PhDefault;
import org.eolang.Phi;
import org.eolang.XmirObject;

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
                            Thread.sleep(millis);
                            return new Data.ToPhi(true);
                        }
                )
        );
    }

}
