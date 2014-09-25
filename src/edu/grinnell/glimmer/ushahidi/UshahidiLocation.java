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
 * A simple representation of locations of Ushahidi incidents.
 * 
 * @version 0.4.1 of 24 September 2014
 * @author Samuel A. Rebelsky
 * @author Daniel Torres
 */
public class UshahidiLocation
{

  // +-----------+------------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The constant used to indicate an invalid location.
   */
  public static final int INVALID_LOCATION_ID = 0;

  /**
   * The constant used to indicate that no latitude has been supplied.
   */
  public static final double NO_LATITUDE = 191919;

  /**
   * The constant used to indicate that no longitude has been suplied
   */
  public static final double NO_LONGITUDE = 191919;

  // +--------+---------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * Where did the incident occur? Stored as an integer identifier.
   */
  int id = INVALID_LOCATION_ID;

  /**
   * The name of the location.
   */
  String name = "";

  /**
   * The latitude of the location.
   */
  double latitude = NO_LATITUDE;

  /**
   * The longitude of the location.
   */
  double longitude = NO_LONGITUDE;

  // +--------------+---------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create an empty location. Used mostly for testing.
   */
  public UshahidiLocation()
  {
  } // UshahidiLocation()

  /**
   * Create a location given only id and name.  Such a location has
   * no known latitude and longitude.
   *
   * @param id
   *            The unique identifier used to mark this location.
   * @param name
   *            A human-readable description of the location (e.g., "Boston"
   *            or "4th and Main")
   */
  public UshahidiLocation(int id, String name)
  {
    this.id = id;
    this.name = name;
  } // UshahidiLocation(int, String)

  /**
   * Create a location given all the main components.
   *
   * @param id
   *            The unique identifier used to mark this location.
   * @param name
   *            A human-readable description of the location (e.g., "Boston"
   *            or "4th and Main")
   * @param latitude
   *            Kind of like attitude, but not.
   * @param longitude
   *            Kind of like latitude, but not.
   */
  public UshahidiLocation(int id, String name, double latitude, double longitude)
  {
    this.id = id;
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
  } // UshahidiLocation

  // +-------------------------+----------------------------------------
  // | Standard Object Methods |
  // +-------------------------+

  /**
   * Convert the location to a string (e.g., for printing).
   *
   * @return
   *            A string containing all of the components, with both name 
   *            and value, separated by commas.
   */
  public String toString()
  {
    return this.toString(", ");
  } // toString()

  /**
   * Convert the location to a string, using sep to separate the items.
   *
   * @param sep
   *            A sequence to put between each component.
   * @return
   *            A string containing all of the components, with both name 
   *            and value, separated by sep.
   */
  public String toString(String sep)
  {
    return "LOCATION [" + "ID: " + this.id + sep + "Name: " + this.name + sep
           + "Latitude: " + this.latitude + sep + "Longitude: "
           + this.longitude + "]";
  } // toString(String)

  // +---------+--------------------------------------------------------
  // | Getters |
  // +---------+

  /**
   * Get the id.
   *
   * @return
   *            A unique identifier for this location.
   */
  public int getId()
  {
    return this.id;
  } // getId()

  /**
   * Get the name.
   *
   * @return
   *            A human-readable description of this location.
   */
  public String getName()
  {
    return this.name;
  } // getName()

  /**
   * Get the latitude.
   *
   * @return
   *            The latitude of this location.
   */
  public double getLatitude()
  {
    return this.latitude;
  } // getLatitude()

  /**
   * Get the longitude.
   *
   * @return
   *            The longitude of this location.
   */
  public double getLongitude()
  {
    return this.longitude;
  } // getLongitude()

} // UshahidiLocation
