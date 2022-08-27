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
import org.eolang.Param;
import org.eolang.Phi;

/**
 * A thread that takes and Dataizes while running
 *
 * @since 0.0
 */
final class DataizingThread extends Thread{
    private Phi phi_thread;
    private Phi result;
    public DataizingThread(Phi phi_thread){
        this.phi_thread = phi_thread;
    }

    public Phi GetResult() {
        return result;
    }

    @Override
    public void run(){
        System.out.println("Started, go sleeping...");
        if (this.phi_thread == null){
            System.out.println("ERROR1, phi_thread == null");
            this.result = new Data.ToPhi(new Dataized(phi_thread.attr("slow").get()).take());
        }
        else {
            this.result = phi_thread.attr("slow").get();
        }
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Wake up!!");
    }
}