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

import org.eolang.Dataized;
import org.eolang.Phi;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class Semaphores {

    /**
     * Semaphores.
     */
    public static final Semaphores INSTANCE = new Semaphores();

    /**
     * All Semaphores.
     * They are contained in a map with Phi mutexes as keys
     */
    private final ConcurrentHashMap<Phi, Semaphore> all =
            new ConcurrentHashMap<>(0);

    /**
     * Ctor.
     */
    private Semaphores() {
        // Singleton
    }

    public Semaphore get(final Phi mutex) {
        return this.all.computeIfAbsent(
            mutex,
            key -> {
                long permits = new Dataized(key.attr("permits").get()).take(Long.class);
                System.out.println("Created new semaphore with permits = "  + permits);
                return new Semaphore((int) permits);
            }
        );
    }
}
