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

import java.util.concurrent.ConcurrentHashMap;
import org.eolang.ExFailure;
import org.eolang.Phi;

/**
 * All Acquires.
 *
 * @since 0.0
 */
public final class Acquisitions {
    /**
     * A map with numbers of opened lock as value.
     */
    public static final Acquisitions INSTANCE = new Acquisitions();

    /**
     * All acquires with values.
     * They are contained in a map with Phi EOmutex.EOacquire as keys
     */
    private final ConcurrentHashMap<Phi, Integer> all =
        new ConcurrentHashMap<>(0);

    /**
     * Ctor.
     */
    private Acquisitions() {
        // Singleton
    }

    /**
     * Update.
     *
     * @param acquire Phi acquire as a key
     * @param num Current number of locks
     */
    public void update(final Phi acquire, final int num) {
        if (this.all.containsKey(acquire)) {
            throw new ExFailure(
                String.format("The lock is already added with hashcode = %d", acquire.hashCode())
            );
        }
        this.all.put(acquire, num);
    }

    /**
     * Decrease.
     *
     * @param acquire Phi acquire as a key
     * @param num Number of locks to release
     */
    public void decrease(final Phi acquire, final int num) {
        if (!this.all.containsKey(acquire)) {
            throw new ExFailure("The lock was not acquired yet");
        }
        this.all.compute(
            acquire,
            (key, val) -> {
                final int result = val - num;
                if (result < 0) {
                    throw new ExFailure(
                        String.format(
                            "The lock cannot be released more than acquired. The lock was %d, but wanted decrease by %d",
                            val,
                            num
                        )
                    );
                }
                return result;
            }
        );
    }
}
