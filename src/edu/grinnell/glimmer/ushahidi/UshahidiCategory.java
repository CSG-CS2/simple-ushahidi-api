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

/**
 * A simple representation of categories for Ushahidi incidents.
 * 
 * @version 0.1.1 of 24 September 2014
 * @author Samuel A. Rebelsky
 */
public class UshahidiCategory
{
  // +--------+---------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * Which category is it?
   */
  int id;

  /**
   * The name of the category.
   */
  String name;

  // +--------------+---------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a category with just an id
   *
   * @param id
   *            A unique number used to identify the category.
   */
  public UshahidiCategory(int id)
  {
    this.id = id;
    this.name = "";
  } // UshahidiCategory()

  /**
   * Create a category given both id and name.
   *
   * @param id
   *            A unique number used to identify the category.
   * @param name
   *            A string that humans can use to refer to the category.
   */
  public UshahidiCategory(int id, String name)
  {
    this.id = id;
    this.name = name;
  } // UshahidiCategory(int, String)

  // +-------------------------+----------------------------------------
  // | Standard Object Methods |
  // +-------------------------+

  /**
   * Convert the category to a string (e.g., for printing).
   */
  public String toString()
  {
    return this.name + "/" + this.id;
  } // toString()

  // +---------+--------------------------------------------------------
  // | Getters |
  // +---------+

  /**
   * Get the id.
   *
   * @return
   *            The unique identifier that this Ushahidi installation
   *            uses for this category.
   */
  public int getId()
  {
    return this.id;
  } // getId()

  /**
   * Get the name.
   *
   * @return
   *            A string humans use to refer to the category.
   */
  public String getName()
  {
    return this.name;
  } // getName()

} // UshahidiCategory
