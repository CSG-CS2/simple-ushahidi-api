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
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * A simple representation of Ushahidi incidents. Currently gives access only to
 * the primary fields.
 * 
 * @version 0.5.1 of 24 September 2014
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
  UshahidiCategory[] categories = null;

  /**
   * The array of media.
   */
  JSONArray media = null;

  /**
   * Additional comments about the incident.
   */
  UshahidiComment[] comments = null;

  /**
   * Any errors involving this incident.
   */
  JSONArray error = null; // Is there an error involving this incident.

  /**
   * Any additional fields provided by this Ushahidi installation.
   */
  JSONObject customFields = null;

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
   * testing.  The incident may or may not be marked as active and verified.
   *
   * @param id
   *            The id of the new incident.
   * @param title
   *            The title of the incident.
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
                          UshahidiLocation location, String description,
                          UshahidiCategory[] categories)
  {
    this.id = id;
    this.title = title;
    this.date = date;
    this.location = location;
    this.mode = 0;
    this.active = 1;
    this.verified = 1;
    this.description = description;
    this.categories = categories;
  } // UshahidiIncident(...)

  /**
   * Create an incident from a partially parsed JSON response.
   *
   * @param input
   *            A piece JSON code returned by the Ushahidi Web API
   *            and then parsed by the standard JSON parser.
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

    // Get the categories.
    JSONArray categories = input.getJSONArray("categories");
    int len = categories.length();
    this.categories = new UshahidiCategory[len];
    for (int i = 0; i < len; i++)
      {
        JSONObject category = 
            categories.getJSONObject(i).getJSONObject("category");
        try 
          {
            this.categories[i] = new UshahidiCategory(category.getInt("id"),
                category.getString("title"));
          } // try
        catch (Exception e)
          {
          } // catch
      } // for

    // Get the comments.
    //   STUB! 
    //   Note: It looks like with the 2.7.4 server we don't get the
    //   comments directly.  Instead, we need to send a query of the form
    //   task=comments&by=reportid&id=###.  
    this.comments = null;

    // Get other compound fields. Right now, we don't reveal these to 
    // the client.
    this.media = input.getJSONArray("media");

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
        customFields = input.getJSONObject("customfields");
      } // try
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
   *
   * @return
   *            A string containing most of the important fields (both
   *            name and value), separated by commas.
   */
  public String toString()
  {
    return this.toString(", ");
  } // toString()

  /**
   * Convert the incident to a string, using sep to separate the items.
   *
   * @param     sep
   *            A string used to separate the items.
   * @return
   *            A string containing most of the important fields (both
   *            name and value), separated by commas.
   */
  public String toString(String sep)
  {
    return "INCIDENT [" + "Title: " + this.title + sep + "ID: " + this.id + sep
           + "Description: " + this.description + sep + "Date: "
           + formatDate(this.date) + sep + "Location: " + this.location 
           + sep + "Categories: " + Arrays.toString(this.categories) + "]";
  } // toString(String)

  // +---------+--------------------------------------------------------
  // | Getters |
  // +---------+

  /**
   * Get the id of the current incident. 
   *
   * @return
   *            The id of the incident, if it has one.
   *            <code>INVALID_INCIDENT_ID</code> if the inciddent is
   *            unitialized or otherwise defective.
   */
  public int getId()
  {
    return this.id;
  } // getId()

  /**
   * Get the title of the incident.
   *
   * @return
   *            The title of the incident, if it was specified.
   *            Otherwise, the empty string.
   */
  public String getTitle()
  {
    return this.title;
  } // getTitle()

  /**
   * Get a description of the incident. 
   *
   * @return
   *            A description of the incident, if it was specified.
   *            Otherwise, the empty string.
   */
  public String getDescription()
  {
    return this.description;
  } // getDescription()

  /**
   * Get the date the incident was reported.
   *
   * @return
   *            The date the incident was reported, if it is available.
   *            <code>null</code>, otherwise.
   */
  public LocalDateTime getDate()
  {
    return this.date;
  } // getDate()

  /**
   * Get the mode of the incident.  (No, I don't know what a mode is.
   * But it's in the spec.)
   *
   * @return
   *            Your guess is as good as mine.
   */
  public int getMode()
  {
    return this.mode;
  } // getMode()

  /**
   * Determine whether or not the incident is active.  
   *
   */
  public boolean getActive()
  {
    return this.active != 0;
  } // getActive()

  /**
   * Determine whether or not the incident is verified.
   */
  public boolean getVerified()
  {
    return this.verified != 0;
  } // getVerified()

  /**
   * Get the location of the incident. 
   *
   * @return
   *            The location of the incident, if one has been assigned.
   *            <code>null</code>, if no location has been assigned.
   */
  public UshahidiLocation getLocation()
  {
    return this.location;
  } // getLocation

  /**
   * Get the names of all the custom fields.
   *
   * @return
   *            An array of strings.
   */
  public String[] getCustomFieldNames()
  {
    // Sanity check
    if (this.customFields == null)
      {
        return new String[0];
      } // if (this.customFields == null)

    // The customFields JSON is an object indexed by auto-generated
    // keys.  Grab the field_name from each one.
    String[] names = JSONObject.getNames(this.customFields);
    String[] fieldnames = new String[names.length];
    for (int i = 0; i < names.length; i++)
      {
        JSONObject obj = this.customFields.getJSONObject(names[i]);
        fieldnames[i] = obj.getString("field_name");
       } // for
     return fieldnames;
  } // getCustomFieldNames()

  /**
   * Get a custom field with a particular name.
   *
   * @return 
   *   Some undetermined representation of the value of that field.
   * @throws Exception
   *   If the field is not available.
   */
  public Object getCustomField(String name)
    throws Exception
  {
    // Sanity check
    if (this.customFields == null)
      {
        throw new Exception("No custom fields available");
      } // if (this.customFields == null)

    // The customFields JSON is an object indexed by auto-generated
    // keys.  Search for the entry that contains our desired field.
    String[] names = JSONObject.getNames(this.customFields);
    for (int i = 0; i < names.length; i++)
      {
        JSONObject obj = this.customFields.getJSONObject(names[i]);
        String fieldname = obj.getString("field_name");
        if (name.equalsIgnoreCase(fieldname))
          {
            return obj.get("field_response");
          } // if (name.equals(fieldname)
      } // for

    // At this point, we can be pretty sure that the field does not
    // exist.
    throw new Exception("No such custom field: " + name);
  } // getCustomField

} // UshahidiIncident
