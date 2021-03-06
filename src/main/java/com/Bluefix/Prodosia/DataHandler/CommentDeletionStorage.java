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

import com.Bluefix.Prodosia.SQLite.SqlDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * This class keeps a collection of long values for all imgur comments that still need to be deleted.
 */
public class CommentDeletionStorage extends LocalStorageHandler<Long>
{
    //region Singleton and Constructor

    private static CommentDeletionStorage me;

    public static CommentDeletionStorage handler()
    {
        if (me == null)
            me = new CommentDeletionStorage();

        return me;
    }

    private CommentDeletionStorage()
    {
        super(true);
    }

    //endregion



    //region Local Storage Handler implementation

    /**
     * Retrieve the prepared statements necessary for adding an item.
     *
     * @param aLong
     */
    @Override
    protected Long setItem(Long aLong) throws SQLException
    {
        return dbSetDeletion(aLong);
    }

    /**
     * Remove an item from the storage.
     *
     * @param aLong
     */
    @Override
    protected void removeItem(Long aLong) throws SQLException
    {
        dbRemoveDeletion(aLong);
    }

    /**
     * Retrieve all items from the storage in no particular order.
     *
     * @return
     */
    @Override
    protected ArrayList<Long> getAllItems() throws SQLException
    {
        return dbGetDeletions();
    }

    //endregion

    //region Database management

    private static Long dbSetDeletion(Long d) throws SQLException
    {
        if (d < 0)
            return d;

        // if this value was contained in the database, we can skip it.
        if (handler().getAll().contains(d))
            return d;

        String query =
                "INSERT INTO CommentDeletion " +
                "(id) VALUES (?);";

        PreparedStatement prep = SqlDatabase.getStatement(query);
        prep.setLong(1, d);

        SqlDatabase.execute(prep);

        assert(prep.isClosed());

        return d;
    }

    private static void dbRemoveDeletion(Long d) throws SQLException
    {
        String query =
                "DELETE FROM CommentDeletion " +
                "WHERE id = ?;";

        PreparedStatement prep = SqlDatabase.getStatement(query);
        prep.setLong(1, d);

        SqlDatabase.execute(prep);

        assert(prep.isClosed());
    }

    private ArrayList<Long> dbGetDeletions() throws SQLException
    {
        String query =
                "SELECT id FROM CommentDeletion;";

        PreparedStatement prep = SqlDatabase.getStatement(query);
        ArrayList<ResultSet> result = SqlDatabase.query(prep);

        if (result.size() != 1)
            throw new SQLException("SqlDatabase exception: Expected result size did not match (was " + result.size() + ")");

        ResultSet rs = result.get(0);
        ArrayList<Long> output = new ArrayList<>();

        while (rs.next())
        {
            output.add(rs.getLong(1));
        }

        // close the resultset
        rs.close();
        prep.close();
        assert(prep.isClosed());

        return output;
    }

    //endregion
}
