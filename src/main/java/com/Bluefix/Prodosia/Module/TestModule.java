/*
 * Copyright (c) 2018 J.S. Boellaard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.Bluefix.Prodosia.Module;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TestModule extends ImgurIntervalRunner
{


    /**
     * Instantiate a new intervalrunner-like object.
     *
     * @param maximumRequests The maximum amount of GET requests allowed per reset.
     */
    public TestModule(int maximumRequests)
    {
        super(maximumRequests);

        counter = 1;
    }

    @Override
    protected void run()
    {
        System.out.println(ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME));
    }

    private int counter;

    @Override
    protected int projectedRequests()
    {
        return counter++;
    }
}
