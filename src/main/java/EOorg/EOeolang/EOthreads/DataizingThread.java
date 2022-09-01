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

import org.eolang.Data;
import org.eolang.Dataized;
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
    private Phi eothread;

    /**
     * Computed "slow".
     */
    private Phi computed;

    /**
     * Ctor.
     * @param eothread Phi thread
     */
    DataizingThread(final Phi eothread) {
        this.eothread = eothread;
    }

    /**
     * Get.
     * @return The computed slow
     */
    public Phi dataized() {
        return this.computed;
    }

    @Override
    public void run() {
        try {
            this.computed = new Data.ToPhi(new Dataized(this.eothread.attr("slow").get()).take());
        } catch (InterruptedException ie){
            ;
        }
    }
}
