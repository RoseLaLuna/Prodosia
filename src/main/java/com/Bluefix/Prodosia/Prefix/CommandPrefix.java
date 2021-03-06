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

package com.Bluefix.Prodosia.Prefix;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Storage Object for command prefixes.
 *
 * A command prefix is a certain pattern that must occur before a command is called.
 * This allows the bot to distinguish command-comments from non-command-comments.
 */
public class CommandPrefix
{
    /**
     * The type of service that incorporates this command prefix.
     */
    public enum Type
    {
        IMGUR(0),
        DISCORD(1),
        TEST(2);

        private int value;

        Type(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

        public static Type parseType(int value)
        {
            switch (value)
            {
                case 0:
                    return IMGUR;
                case 1:
                    return DISCORD;
                case 2:
                    return TEST;
            }

            throw new IllegalArgumentException("The command prefix type was not recognized.");
        }
    }

    private Type type;

    /**
     * The pattern expression that will recognize a command within a string.
     */
    private String regex;

    public CommandPrefix(Type type, String regex)
    {
        this.type = type;
        this.regex = regex;
    }

    public Type getType()
    {
        return type;
    }

    /**
     * Retrieve the pattern for this CommandPrefix.
     * @return
     */
    public String getRegex()
    {
        return regex;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandPrefix that = (CommandPrefix) o;
        return type == that.type;
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(type);
    }

    /**
     * Retrieve the index after a command prefix. This index is where the
     * command and the arguments will start.
     *
     * Returns -1 if no command was recognized.
     * @param comment The comment with a potential command on it.
     * @return The index after the prefix, or -1 if no command was detected.
     */
    public int matchIndex(String comment)
    {
        // return -1 on invalid regex.
        if (this.regex == null || this.regex.trim().isEmpty())
            return -1;


        Pattern p = Pattern.compile(this.regex);

        Matcher matcher = p.matcher(comment);

        if (matcher.find())
            return matcher.end();

        return -1;
    }


    /**
     * Generate a new CommandPrefix pattern for the specified items.
     * @param items
     * @return
     */
    public static String parsePatternForItems(String... items)
    {
        StringBuilder sb = new StringBuilder();

        // commands are case-insensitive
        sb.append("(?i)");

        // the start of a match should either be the start of a line or after a whitespace.
        sb.append("(^|\\s)");

        // next come the different possible prefixes
        sb.append("(");
        for (String i : items)
        {
            sb.append(i + "|");
        }

        // if there was at least one item, complete the final superfluous `|`
        if (items.length > 0)
            sb.setLength(sb.length()-1);

        // close the set of prefixes
        sb.append(")");

        return sb.toString();
    }

    /**
     * Generate a new CommandPrefix pattern for the specified items.
     * @param items
     * @return
     */
    public static String parsePatternForItems(Iterable<String> items)
    {
        if (items == null)
            return parsePatternForItems();

        LinkedList<String> list = new LinkedList<>();

        Iterator<String> it = items.iterator();

        while (it.hasNext())
            list.add(it.next());

        // if the list is empty, return empty string
        if (list.isEmpty())
            return "";

        return parsePatternForItems(list.toArray(new String[0]));
    }

    /**
     * Assumes a pattern set according to `parsePatternForItems` and will throw
     * if that is not the case.
     * @param pattern
     * @return
     */
    public static LinkedList<String> parseitemsFromPattern(String pattern)
    {
        String error = "The pattern was not recognized as valid.";
        String start = "(?i)(^|\\s)(";

        if (pattern == null)
            throw new IllegalArgumentException(error);

        if (!pattern.startsWith(start))
            throw new IllegalArgumentException(error);

        String entries = pattern.substring(start.length());

        if (!entries.endsWith(")"))
            throw new IllegalArgumentException(error);

        entries = entries.substring(0, entries.length()-1);

        LinkedList<String> entryList = new LinkedList<>();

        String[] entrySplit = entries.split("\\|");

        for (String s : entrySplit)
            if (s != null && !s.isEmpty())
                entryList.add(s);

        return entryList;
    }


}
