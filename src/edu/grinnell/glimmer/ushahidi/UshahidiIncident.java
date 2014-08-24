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

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * A simple representation of Ushahidi incidents. Currently gives access only to
 * the primary fields.
 * 
 * @version 0.4 of 23 August 2014
 * @author Samuel A. Rebelsky
 * @author Daniel Torres
 */
public class UshahidiIncident
{

  // +-----------+------------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The constant used to indicate an invalid location.
   */
  public static final int INVALID_INCIDENT_ID = 0;

  /**
   * The format in which Ushahidi seems to report dates. 
   */
  static final DateTimeFormatter dateInputFormat =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * The format in which we want to report dates.
   */
  static final DateTimeFormatter dateOutputFormat = 
      DateTimeFormatter.ofPattern("yyyy-MM-dd");

  // +--------+---------------------------------------------------------
  // | Fields |
  // +--------+

  /*
   * Required fields. Taken from
   * https://wiki.ushahidi.com/display/WIKI/Ushahidi+Public+API
   */

  /**
   * The identification number that corresponds to this incident. Set to -1 to
   * indicate "not yet initialized.
   */
  int id = -1;

  /**
   * The title of the incident. Set to "" by default.
   */
  String title = "";

  /**
   * A description of the incident. Set to "" by default.
   */
  String description = "";

  /**
   * The date that the incident was reported. Does not appear to include the
   * time of day.
   */
  LocalDateTime date;

  /**
   * The mode of the incident. (???)
   */
  int mode;

  /**
   * Is the incident active?
   */
  int active = 0;

  /**
   * Is the incident verified?
   */
  int verified = 0;

  /**
   * Where did the incident occur? Uses null for an unknown location.
   */
  UshahidiLocation location = null;

  /**
   * The array of category IDs (or more?).
   */
  JSONArray categories = null;

  /**
   * The array of media.
   */
  JSONArray media = null;

  /**
   * Additional comments about the incident.
   */
  JSONArray comments = null;

  /**
   * Any errors involving this incident.
   */
  JSONArray error = null; // Is there an error involving this incident.

  /**
   * Any additional fields provided by this Ushahidi installation.
   */
  JSONArray customFields = null;

  // +--------------+---------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create an empty incident. Used mostly for testing.
   */
  public UshahidiIncident()
  {
  } // UshahidiIncident()

  /**
   * Create a basic incident with only id and title. Also used primarily for
   * testing.
   */
  public UshahidiIncident(int id, String title)
  {
    this.id = id;
    this.title = title;
  } // UshahidiIncident(int, String)

  /**
   * Create an incident with many of the important fields. The incident is
   * marked as active and verified.
   */
  public UshahidiIncident(int id, String title, LocalDateTime date,
                          UshahidiLocation location, String description)
  {
    this.id = id;
    this.title = title;
    this.date = date;
    this.location = location;
    this.mode = 0;
    this.active = 1;
    this.verified = 1;
    this.description = description;
  } // UshahidiIncident(int, String, LocalDateTime , Location)

  /**
   * Create an incident from a partially parsed JSON response.
   * 
   * @exception JSONException
   *                if it is unable to get one of the required fields from the
   *                JSON object.
   * @exception ParseException
   *                if it is unable to convert the required field to the
   *                appropriate type.
   */
  UshahidiIncident(JSONObject input) throws JSONException, ParseException
  {
    JSONObject incident = input.getJSONObject("incident");

    // Get basic fields
    this.id = incident.getInt("incidentid");
    this.title = incident.getString("incidenttitle");
    this.description = incident.getString("incidentdescription");
    this.date =
        LocalDateTime.parse(incident.getString("incidentdate"), dateInputFormat);
    this.mode = incident.getInt("incidentmode");
    this.active = incident.getInt("incidentactive");
    this.verified = incident.getInt("incidentverified");

    // Not all incidents have locations, or may have only partial
    // information on the location. The following sections deal
    // with most location issues.
    int locationId;
    String locationName;
    double locationLatitude;
    double locationLongitude;

    try
      {
        locationName = incident.getString("locationname");
      }
    catch (Exception e)
      {
        locationName = "";
      }
    try
      {
        locationId = incident.getInt("incidentid");
        if (locationId == 0)
          locationId = UshahidiLocation.INVALID_LOCATION_ID;
      }
    catch (Exception e)
      {
        locationId = UshahidiLocation.INVALID_LOCATION_ID;
      } // catch (Exception)
    try
      {
        locationLatitude = incident.getDouble("locationlatitude");
      }
    catch (Exception e)
      {
        locationLatitude = UshahidiLocation.NO_LATITUDE;
      } // catch (Exception)
    try
      {
        locationLongitude = incident.getDouble("locationlongitude");
      }
    catch (Exception e)
      {
        locationLongitude = UshahidiLocation.NO_LONGITUDE;
      } // catch (Exception)

    this.location =
        new UshahidiLocation(locationId, locationName, locationLatitude,
                             locationLongitude);

    // Get compound fields. Right now, we don't reveal this to the client.
    categories = input.getJSONArray("categories");
    media = input.getJSONArray("media");
    comments = input.getJSONArray("comments");

    // Get the error messages
    try
      {
        error = input.getJSONArray("error");
      } // try{error = input.getJSONArray("error")}
    catch (JSONException e)
      {
        error = null;
      } // catch(JSONException e)

    // Get the array of custom fields
    try
      {
        customFields = input.getJSONArray("customfields");
      } // try{customFields = input.getJSONArray("customfields")}
    catch (JSONException e)
      {
        customFields = null;
      } // catch(JSONException e)
  } // UshahidiIncident(JSONObject)

  // +-----------------+------------------------------------------------
  // | Local Utilities |
  // +-----------------+

  /**
   * Format the date nicely.
   */
  String formatDate(LocalDateTime date)
  {
    if (date == null)
      {
        return "<no date>";
      } // if there is no date
    else
      {
        return date.format(dateOutputFormat);
      } // if there is a date
  } // formatDate

  // +-------------------------+----------------------------------------
  // | Standard Object Methods |
  // +-------------------------+

  /**
   * Convert the incident to a string (e.g., for printing).
   */
  public String toString()
  {
    return this.toString(", ");
  } // toString()

  /**
   * Convert the incident to a string, using sep to separate the items.
   */
  public String toString(String sep)
  {
    return "INCIDENT [" + "Title: " + this.title + sep + "ID: " + this.id + sep
           + "Description: " + this.description + sep + "Date: "
           + formatDate(this.date) + sep + "Location: " + this.location + "]";
  } // toString(String)

  // +---------+--------------------------------------------------------
  // | Getters |
  // +---------+

  /**
   * Get the id of the current incident. Returns INVALID_INCIDENT_ID for an
   * unitialized or otherwise defective incident.
   */
  public int getId()
  {
    return this.id;
  } // getId()

  /**
   * Get the title of the incident.
   */
  public String getTitle()
  {
    return this.title;
  } // getTitle()

  /**
   * Get a description of the incident. This may be an empty string.
   */
  public String getDescription()
  {
    return this.description;
  } // getDescription()

  /**
   * Get the date the incident was reported.
   */
  public LocalDateTime getDate()
  {
    return this.date;
  } // getDate()

  /**
   * Get the mode of the incident.
   */
  public int getMode()
  {
    return this.mode;
  } // getMode()

  /**
   * Determine whether or not the incident is active.
   */
  public int getActive()
  {
    return this.active;
  } // getActive()

  /**
   * Determine whether or not the incident is verified.
   */
  public int getVerified()
  {
    return this.verified;
  } // getVerified()

  /**
   * Get the location of the incident. Returns null if no location has been
   * assigned.
   */
  public UshahidiLocation getLocation()
  {
    return this.location;
  } // getLocation
} // UshahidiIncident
