/**
 * Copyright (c) 2013-14 Samuel A. Rebelsky and Daniel Torres.  All rights
 * reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.grinnell.glimmer.ushahidi;

import java.time.LocalDateTime;

/**
 * A simple representation of comments on Ushahidi incidents.
 * 
 * @version 0.1.1 of 24 September 2014
 * @author Samuel A. Rebelsky
 */
public class UshahidiComment
{
  // +--------+---------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * Which category is it?
   */
  int id;

  /**
   * Which incident does it comment on?
   */
  int incident;

  /**
   * Who wrote the comment?
   */
  String author;

  /**
   * The contents of the comment.
   */
  String description;

  /**
   * The date/time of the comment.
   */
  LocalDateTime date;

  // +--------------+---------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a comment given all of the data.
   */
  public UshahidiComment(int id, int incident, String author, 
      String description, LocalDateTime date)
  {
    this.id = id;
    this.incident = incident;
    this.author = author;
    this.description = description;
    this.date = date;
  } // UshahidiComment(int, int, String, String, LocalDateTime)


  // +-------------------------+----------------------------------------
  // | Standard Object Methods |
  // +-------------------------+

  /**
   * Convert the comment to a string (e.g., for printing).
   *
   * @return
   *            A human-readable version of the comment, including
   *            the id, author, contents, and more.
   */
  public String toString()
  {
    return "Comment " + this.id + " on " + this.incident + ": \"" +
        this.description + "\" by " + this.author + " on " +
        this.date.toString();
  } // toString()

  // +---------+--------------------------------------------------------
  // | Getters |
  // +---------+

  /**
   * Get the id of this comment.
   *
   * @return
   *            The id.
   */
  public int getId()
  {
    return this.id;
  } // getId()

  /**
   * Get the id of the incident this comment reports on.
   *
   * @return
   *            The id.
   */
  public int getIncident()
  {
    return this.incident;
  } // getIncident()

  /**
   * Get the description of the comment (the contents).  Why is this
   * called "description" and not "contents"?  We are following the
   * model of the Ushahidi Web API.
   *
   * @return
   *            A potentially long string.
   */
  public String getDescription()
  {
    return this.description;
  } // getDescription()

  /**
   * Get the author of the comment.
   *
   * @return
   *   A string that gives the name of the author.
   */
  public String getAuthor()
  {
    return this.author;
  } // getAuthor()

  /**
   * Get the date that the comment was submitted.
   *
   * @return
   *   The date the comment was submitted.
   */
  public LocalDateTime getDate()
  {
    return this.date;
  } // getDate()
} // UshahidiComment
