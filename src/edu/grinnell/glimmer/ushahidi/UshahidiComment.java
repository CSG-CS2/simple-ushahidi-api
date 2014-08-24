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
 * @version 0.1 of 24 August 2014
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
   * Get the id.
   */
  public int getId()
  {
    return this.id;
  } // getId()

  /**
   * Get the incident.
   */
  public int getIncident()
  {
    return this.incident;
  } // getIncident()

  /**
   * Get the description.
   */
  public String getDescription()
  {
    return this.description;
  } // getDescription()

  /**
   * Get the author.
   *
  public String getAuthor()
  {
    return this.author;
  } // getAuthor()

  /**
   * Get the date.
   */
  public LocalDateTime getDate()
  {
    return this.date;
  } // getDate()
} // UshahidiComment
