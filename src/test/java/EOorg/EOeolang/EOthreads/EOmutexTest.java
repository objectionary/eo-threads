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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.eolang.Data;
import org.eolang.PhWith;
import org.eolang.Phi;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.RunsInThreads;
import org.mockito.internal.util.collections.Sets;

/**
 * Test case for {@link EOmutex}.
 *
 * @since 0.0
 */
public class EOmutexTest {

    @Test
    public void DifferentHashCodes(){
        long l = 0L;
        Set<Integer> hashcodes = new HashSet<>(0);
        while (l < 1e4){
            hashcodes.add(
                new PhWith(
                    new EOnumber(Phi.Φ),
                    "n",
                    new Data.ToPhi(l)
                ).hashCode()
            );
            l++;
        }
        MatcherAssert.assertThat(
            (int) l,
            Matchers.equalTo(hashcodes.size())
        );
    }

    @Test
    public void PhiUniqueHashesInDynamic_1() throws InterruptedException {
        int threads = 8;
        Set<Integer> hashes = ConcurrentHashMap.newKeySet();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(1);
        for (long t = 0; t < threads; ++t) {
            final long finalT = t;
            service.submit(
                () -> {
                    latch.await();
                    Phi number = new PhWith(
                        new EOnumber(Phi.Φ),
                        "n",
                        new Data.ToPhi(finalT)
                    );
                    System.out.println("Hashcode = " + number.hashCode() + ", n = " + finalT);
                    if (!hashes.add(number.hashCode())){
                        System.out.println("Repeated hashcode = " + number.hashCode());
                    }
                    return number.hashCode();
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

    @Test
    public void PhiUniqueHashesInDynamic_2() {
        ConcurrentHashMap<Integer, Long> map = new ConcurrentHashMap<>(0);
        Set<Integer> set = map.newKeySet();
        MatcherAssert.assertThat(
                t -> {
                    Long buf = t.getAndIncrement();
                    Phi acquire = new PhWith(
                    new EOnumber(Phi.Φ),
                            "n",
                            new Data.ToPhi(buf)
                    );
                    //System.out.println(acquire.hashCode() + ", buf = " + buf);
                    return set.add(acquire.hashCode());
                },
                new RunsInThreads<>(new AtomicLong(), 1)
        );
    }

    @Test
    public void addsAndRetrieves() {
        int threads = 20;
        ConcurrentHashMap<Integer, Boolean> map = new ConcurrentHashMap<>(0);
        Set<Integer> set = map.newKeySet();
        MatcherAssert.assertThat(
                t -> {
                    Long buf = t.getAndIncrement();
                    //Phi acquire = Phi.Φ;
                    Phi acquire = new PhWith(
                            new EOnumber(Phi.Φ),
                            "n",
                            new Data.ToPhi(buf)
                    );
                    //System.out.println(acquire.hashCode() + ", buf = " + buf);
                    return set.add(buf.hashCode());
                },
                new RunsInThreads<>(new AtomicLong(), threads)
        );
        //System.out.println("size = " + map.size());
    }

    @Test
    public void AcquisitionsSafety() throws NoSuchFieldException, IllegalAccessException {
        Field all = Acquisitions.INSTANCE.getClass().getDeclaredField("all");
        all.setAccessible(true);
        Map<Phi, Integer> map = (ConcurrentHashMap<Phi, Integer>) all.get(Acquisitions.INSTANCE);
        map.clear();
        MatcherAssert.assertThat(
                t -> {
                    long buf = t.getAndIncrement();
                    Phi acquire = new PhWith(
                            new EOmutex$EOacquire(Phi.Φ),
                            "locks",
                            new Data.ToPhi(buf)
                    );
                    //System.out.println("buf = " + buf + ", Hashcode = " + acquire.hashCode());
                    Acquisitions.INSTANCE.update(acquire, 1);
                    //AtomicInteger t1 = new AtomicInteger();
                    //System.out.println("size = " + map.size() + ", buf = " + buf);
                    return true;
                },
                new RunsInThreads<>(new AtomicLong(), 1)
        );

    }

}
