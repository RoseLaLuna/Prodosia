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

package com.Bluefix.Prodosia.DataHandler;

import com.Bluefix.Prodosia.DataType.Tracker.Tracker;
import com.Bluefix.Prodosia.DataType.Tracker.TrackerPermissions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;


public class TrackerHandlerTest extends DataHandlerTest<Tracker>
{
    private static final String TestImgurName = "mashedstew";
    private static final long TestImgurId = 33641050;

    private Tracker tracker;

    private TrackerPermissions perm;




    //region abstract method implementation

    public TrackerHandlerTest(boolean useLocalStorage)
    {
        super(useLocalStorage);
    }

    @Override
    protected LocalStorageHandler<Tracker> getHandler()
    {
        return TrackerHandler.handler();
    }

    @Override
    protected Tracker getItem()
    {
        return this.tracker;
    }

    //endregion



    @Before
    public void setUp()
    {
        perm = new TrackerPermissions(TrackerPermissions.TrackerType.ADMIN);

        tracker = new Tracker(
                TestImgurName,
                TestImgurId,
                "",
                "",
                "",
                perm);


    }

    @After
    public void tearDown() throws Exception
    {
        ArrayList<Tracker> trackers = new ArrayList<>(getHandler().getAll());

        for (Tracker t : trackers)
        {
            if (t.getImgurName().equals(TestImgurName))
                getHandler().remove(t);
        }
    }




    @Test
    public void testGetTrackerByImgurId() throws Exception
    {
        Tracker t = TrackerHandler.getTrackerByImgurId(TestImgurId);

        Assert.assertNull(t);

        getHandler().set(tracker);

        t = TrackerHandler.getTrackerByImgurId(TestImgurId);
        Assert.assertNotNull(t);
        Assert.assertEquals(TestImgurName, t.getImgurName());
        Assert.assertEquals(TestImgurId, t.getImgurId());
    }
}