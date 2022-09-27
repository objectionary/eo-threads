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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.eolang.ExFailure;
import org.eolang.Phi;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
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

    // @todo #33:90min Uncomment this test when
    //  the of same hashcodes in parallel threads
    //  will be solved. This test proves new Phi objects
    //  created in parallel threads can have repeated
    //  hashcodes

    //@Test
    //public void PhiUniqueHashesInDynamic() throws InterruptedException {
    //    int threads = 8;
    //    Set<Integer> hashes = ConcurrentHashMap.newKeySet();
    //    ExecutorService service = Executors.newFixedThreadPool(threads);
    //    CountDownLatch latch = new CountDownLatch(1);
    //    for (long t = 0; t < threads; ++t) {
    //        final long finalT = t;
    //        service.submit(
    //            () -> {
    //                latch.await();
    //                Phi number = new PhWith(
    //                    new EOnumber(Phi.Φ),
    //                    "n",
    //                    new Data.ToPhi(finalT)
    //                );
    //                System.out.println("Hashcode = " + number.hashCode() + ", n = " + finalT);
    //                if (!hashes.add(number.hashCode())){
    //                    System.out.println("Repeated hashcode = " + number.hashCode());
    //                }
    //                return number.hashCode();
    //            }
    //        );
    //    }
    //    latch.countDown();
    //    service.awaitTermination(1, TimeUnit.SECONDS);
    //    MatcherAssert.assertThat(
    //        hashes.size(),
    //        Matchers.equalTo(threads)
    //    );
    //}

    // @todo #33:90min Implement test
    //  like the test PhiUniqueHashesInDynamic
    //  where new Phi objects are created in parallel
    //  instead of by one thread. We need to wait for
    // the solving of hashcode problem
    @Test
    public void acquisitionUpdateDecrease() throws InterruptedException {
        final int threads = 10;
        final ExecutorService service = Executors.newFixedThreadPool(threads);
        final Set<Phi> phis = new HashSet<>();
        for (long counter = 0; counter < 1e2; ++counter) {
            phis.add(
                new EOmutex$EOacquire(Phi.Φ)
            );
        }
        final CountDownLatch latch = new CountDownLatch(1);
        for (final Phi item: phis) {
            service.submit(
                () -> {
                    latch.await();
                    Acquisitions.INSTANCE.update(item, 4);
                    Acquisitions.INSTANCE.decrease(item, 2);
                    Acquisitions.INSTANCE.decrease(item, 2);
                    return true;
                }
            );
        }
        latch.countDown();
        service.shutdown();
        MatcherAssert.assertThat(
            service.awaitTermination(1, TimeUnit.SECONDS),
            Matchers.equalTo(true)
        );
    }

    @Test
    public void differentReleasesOfOneAcquire() throws InterruptedException {
        final int threads = 100;
        final ExecutorService service = Executors.newFixedThreadPool(threads);
        final Phi acquire = new EOmutex$EOacquire(Phi.Φ);
        Acquisitions.INSTANCE.update(acquire, threads);
        final CountDownLatch latch = new CountDownLatch(1);
        for (int counter = 0; counter < threads; ++counter) {
            service.submit(
                () -> {
                    latch.await();
                    Acquisitions.INSTANCE.decrease(acquire, 1);
                    return true;
                }
            );
        }
        latch.countDown();
        service.shutdown();
        service.awaitTermination(1, TimeUnit.SECONDS);
        Assertions.assertThrows(
            ExFailure.class,
            () -> Acquisitions.INSTANCE.decrease(acquire, 1)
        );
    }
}
