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

import com.Bluefix.Prodosia.DataType.Taglist;
import com.Bluefix.Prodosia.SQLite.SqlDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Handler for taglist management.
 */
public class TaglistHandler extends LocalStorageHandler<Taglist>
{
    //region Singleton and Constructor

    private static TaglistHandler me;

    public static TaglistHandler handler()
    {
        if (me == null)
            me = new TaglistHandler();

        return me;
    }

    private TaglistHandler()
    {
        super(true);
    }

    //endregion


    //region Local Storage Handler implementation

    /**
     * Add an item to the storage.
     *
     * @param t
     */
    @Override
    protected void addItem(Taglist t) throws Exception
    {
        dbAddTaglist(t);
    }

    /**
     * Remove an item from the storage.
     *
     * @param t
     */
    @Override
    protected void removeItem(Taglist t) throws Exception
    {
        dbRemoveTaglist(t);
    }

    /**
     * Retrieve all items from the storage in no particular order.
     *
     * @return
     */
    @Override
    protected ArrayList<Taglist> getAllItems() throws Exception
    {
        return dbGetTaglists();
    }

    //endregion

    //region Database management

    private synchronized static void dbAddTaglist(Taglist t) throws SQLException
    {
        if (t == null)
            return;

        // insert the tracker into the database.
        String query =
                "INSERT INTO Taglist " +
                "(abbreviation, description, hasRatings) " +
                "VALUES (?,?, ?);";

        PreparedStatement prep = SqlDatabase.getStatement(query);
        prep.setString(1, t.getAbbreviation());
        prep.setString(2, t.getDescription());
        prep.setBoolean(3, t.hasRatings());

        SqlDatabase.execute(prep);
    }

    private synchronized static void dbRemoveTaglist(Taglist t) throws SQLException
    {
        // if the abbreviation fits, remove the taglist.
        String query =
                "DELETE FROM Taglist " +
                "WHERE abbreviation = ?;";

        PreparedStatement prep = SqlDatabase.getStatement(query);
        prep.setString(1, t.getAbbreviation());

        SqlDatabase.execute(prep);
    }

    private synchronized static ArrayList<Taglist> dbGetTaglists() throws Exception
    {
        String query =
                "SELECT id, abbreviation, description, hasRatings " +
                "FROM Taglist;";

        PreparedStatement prep = SqlDatabase.getStatement(query);
        ArrayList<ResultSet> result = SqlDatabase.query(prep);

        if (result.size() != 1)
            throw new Exception("SqlDatabase exception: Expected result size did not match (was " + result.size() + ")");

        ResultSet rs = result.get(0);
        ArrayList<Taglist> taglists = new ArrayList<>();

        while (rs.next())
        {
            long id = rs.getLong(1);
            String abbr = rs.getString(2);
            String desc = rs.getString(3);
            boolean hasRatings = rs.getBoolean(4);

            taglists.add(new Taglist(id, abbr, desc, hasRatings));
        }

        return taglists;
    }

    //endregion


    //region Helper methods

    /**
     * Retrieve the taglist that corresponds to the id.
     * @param taglistId
     * @return
     */
    public static synchronized Taglist getTaglist(long taglistId) throws Exception
    {
        // TODO: if this methods is used frequently, it might be worth making a HashMap<long, Taglist>
        ArrayList<Taglist> taglists = handler().getAll();

        for (Taglist tl : taglists)
        {
            if (taglistId == tl.getId())
                return tl;
        }

        return null;
    }

    //endregion

}
