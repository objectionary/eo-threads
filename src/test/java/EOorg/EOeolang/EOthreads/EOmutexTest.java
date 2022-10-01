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

import EOorg.EOeolang.EOmath.EOnumber;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.cactoos.Scalar;
import org.cactoos.experimental.Threads;
import org.eolang.Data;
import org.eolang.ExFailure;
import org.eolang.PhWith;
import org.eolang.Phi;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link EOmutex}.
 *
 * @since 0.0
 */
public class EOmutexTest {

    @Test
    public void differentHashCodes() {
        long counter = 0L;
        final Set<Integer> hashcodes = new HashSet<>(0);
        while (counter < 1e4) {
            hashcodes.add(
                (new EOmutex$EOacquire(Phi.Φ)).hashCode()
            );
            ++counter;
        }
        MatcherAssert.assertThat(
            (int) counter,
            Matchers.equalTo(hashcodes.size())
        );
    }

    // @todo #33:90min Uncomment this test when problem
    //  of the of same hashcodes in parallel threads
    //  will be solved. This test proves new Phi objects
    //  created in parallel threads can have repeated
    //  hashcodes
    @Disabled
    @Test
    public void phiUniqueHashesInDynamic() throws InterruptedException {
        final int threads = 8;
        final Set<Integer> hashes = ConcurrentHashMap.newKeySet();
        final ExecutorService service = Executors.newFixedThreadPool(threads);
        final CountDownLatch latch = new CountDownLatch(1);
        for (long counter = 0; counter < threads; ++counter) {
            final long local = counter;
            service.submit(
                () -> {
                    latch.await();
                    final Phi number = new PhWith(
                        new EOnumber(Phi.Φ),
                        "n",
                        new Data.ToPhi(local)
                    );
                    return hashes.add(number.hashCode());
                }
            );
        }
        latch.countDown();
        service.awaitTermination(1, TimeUnit.SECONDS);
        MatcherAssert.assertThat(
            hashes.size(),
            Matchers.equalTo(threads)
        );
    }

    // @todo #33:90min Implement test like the test
    //  below where new Phi objects are created in
    //  parallel instead of one thread. We need to
    //  wait for the solving of hashcode problem
    @Test
    public void acquisitionUpdateDecrease() throws InterruptedException {
        final int threads = 100;
        final Set<Scalar<Boolean>> tasks = new HashSet<>(0);
        for (long counter = 0; counter < 1e2; ++counter) {
            final Phi phi = new EOmutex$EOacquire(Phi.Φ);
            tasks.add(
                () -> {
                    Acquisitions.INSTANCE.update(phi, 4);
                    Acquisitions.INSTANCE.decrease(phi, 2);
                    Acquisitions.INSTANCE.decrease(phi, 2);
                    return true;
                }
            );
        }
        Assertions.assertDoesNotThrow(
            () -> new Threads<>(
                threads,
                tasks
            ).forEach(Boolean::valueOf)
        );
    }

    @Test
    public void differentReleasesOfOneAcquire() {
        final int threads = 100;
        final Phi acquire = new EOmutex$EOacquire(Phi.Φ);
        Acquisitions.INSTANCE.update(acquire, threads);
        new Threads<>(
            threads,
            Stream.generate(
                () -> (Scalar<Boolean>) () -> {
                    Acquisitions.INSTANCE.decrease(acquire, 1);
                    return true;
                }
            ).limit(threads).collect(Collectors.toList())
        ).forEach(Boolean::valueOf);
        Assertions.assertThrows(
            ExFailure.class,
            () -> Acquisitions.INSTANCE.decrease(acquire, 1)
        );
    }

}
