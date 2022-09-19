/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2022 Objectionary.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * EO org.eolang.threads package.
 *
 * @since 0.0
 * @checkstyle PackageNameCheck (4 lines)
 */
package EOorg.EOeolang.EOthreads;

import java.util.Optional;
import org.eolang.Data;
import org.eolang.Dataized;
import org.eolang.ExAbstract;
import org.eolang.Phi;

/**
 * A thread that takes and Dataizes while running.
 *
 * @since 0.0
 */
final class DataizingThread extends Thread {
    /**
     * EOthread.
     */
    private final Phi thread;

    /**
     * Computed "slow".
     */
    private Phi computed;

    /**
     * Exception if dataization goes wrong.
     */
    private ExAbstract failure;

    /**
     * Ctor.
     * @param thread Phi thread
     */
    DataizingThread(final Phi thread) {
        this.thread = thread;
    }

    /**
     * Dataized.
     * @return The computed slow
     */
    public Phi dataized() {
        return Optional.ofNullable(this.computed).orElseThrow(
            () -> this.failure
        );
    }

    // @todo #14:90min Implement handling of
    //  Interrupted Exaption when the get method
    //  will throw it. We need to wait for changes
    //  in eo repository to do that.
    @Override
    public void run() {
        try {
            this.computed = new Data.ToPhi(new Dataized(this.thread.attr("slow").get()).take());
        } catch (final ExAbstract ex) {
            this.failure = ex;
        }
    }
}
