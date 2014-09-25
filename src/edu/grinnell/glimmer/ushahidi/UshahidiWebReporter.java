/**
 * Copyright (c) 2013-14 Samuel A. Rebelsky.  All rights reserved.
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

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.json.JSONObject;

/**
 * A mechanism for sending reports to Ushahidi Web servers.
 * 
 * @version 0.1.1 of 24 September 2014
 * @author Samuel A. Rebelsky
 */
public class UshahidiWebReporter
{
  // +-----------+------------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The format that Ushahidi likes dates reported in, even if it doesn't
   * return them in that format.
   */
  static final DateTimeFormatter dateFormat =
      DateTimeFormatter.ofPattern("MM/dd/yyyy");

  // +----------------+--------------------------------------------------
  // | Static Helpers |
  // +----------------+

  /**
   * Convert an array of categories into a string listing their ids,
   * separated by commas. 
   *
   * @return
   *            The string described above.
   */
  static String categoriesToString(UshahidiCategory[] categories)
  {
    // Should probably be rewritten to use a functional style, but 
    // the functional style is verbose in Java.

    // Sanity check
    if ((categories == null) || (categories.length == 0))
      return "";

    StringBuilder result =  new StringBuilder();
    result.append(categories[0].id);
    for (int i = 1; i < categories.length; i++)
      {
        result.append(",");
        result.append(categories[i].id);
      } // for

    return result.toString();
  } // categoriesToString

  // +----------------+-------------------------------------------------
  // | Static Methods |
  // +----------------+

  /**
   * Submit an incident to a server.
   *
   * @param server
   *            The full URL of the server, including the protocol.
   *            Must not be null.  Must include all the major fields.
   * 
   * @param incident
   *            The incident to submit.  Must be non-null.
   *
   * @throws Exception
   *            If there is not enough data to submit a full report.
   *            If the server is unavailable.
   *            If the server refuses to accept the incident.
   *            And a host of other reasons.
   */
  public static void submit(String server, UshahidiIncident incident)
    throws Exception
  {
    // Sanity check the data
    if ((incident.title == null) || (incident.title.equals("")))
      throw new Exception("Cannot submit an incident with no title");
    if ((incident.description == null) || (incident.description.equals("")))
      throw new Exception("Cannot submit an incident with no description");
    if (incident.date == null)
      throw new Exception("Cannot submit an incident with no date");
    if (incident.location == null)
      throw new Exception("Cannot submit an incident with no location");
    if (incident.location.getLatitude() == UshahidiLocation.NO_LATITUDE)
      throw new Exception("Cannot submit an incident with an invalid latitude");
    if (incident.location.getLongitude() == UshahidiLocation.NO_LONGITUDE)
      throw new Exception("Cannot submit an incident with an invalid longitude");
    if ((incident.categories == null) || (incident.categories.length == 0))
      throw new Exception("Cannot submit an incident with no categories");

    // Determine the hour in 12-hour format
    int hour = (incident.date.getHour() - 1) % 12 + 1;

    // Build the data
    String fields[] = new String[] {
        "task=report",
        "incident_title=" + URLEncoder.encode(incident.title, "UTF-8"),
        "incident_description=" + URLEncoder.encode(incident.description, "UTF-8"),
        "incident_date=" + URLEncoder.encode(incident.date.format(dateFormat), "UTF-8"),
        "incident_hour=" + Integer.toString(hour),
        "incident_ampm=" + ((hour < 12) ? "am" : "pm"),
        "incident_minute=" + Integer.toString(incident.date.getMinute()),
        "incident_category=" + categoriesToString(incident.categories),
        "latitude=" + Double.toString(incident.location.getLatitude()),
        "longitude=" + Double.toString(incident.location.getLongitude()),
        "location_name=" + URLEncoder.encode(incident.location.getName(), "UTF-8")
    };
    String data = Arrays.stream(fields).collect(Collectors.joining("&"));

    // Optional: Check the data
    // System.err.println("SENDING: " + data);

    // Set up the connection to the server
    URL serverURL = new URL(server + "/api");
    HttpURLConnection connection = 
        (HttpURLConnection) serverURL.openConnection();
    connection.setDoOutput(true);
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", 
        "application/x-www-form-urlencoded");
    connection.setRequestProperty("Content-Length", 
        Integer.toString(data.length()));

    // Write the data
    OutputStream os = connection.getOutputStream();
    os.write(data.getBytes());
    connection.connect();

    // Get the response
    String response = UshahidiUtils.readAll(connection.getInputStream());

    // More experimentation
    // System.err.println("RESPONSE: " + response);

    // Parse the response to figure out whether or not we succeeded.
    JSONObject json;
    JSONObject error;
    int code;
    try
      {
        json = new JSONObject(response);
        error = json.getJSONObject("error");
      } // try
    catch (Exception e)
      {
        throw new Exception("Server returned invalid response");
      } // catch
    try  
      {
        code = error.getInt("code");
      } // try
    catch (Exception e)
      {
        throw new Exception("Server failed to return status");
      } // catch
    if (code != 0)
      {
        throw new Exception("Could not add report because "
            + error.getString("message"));
      } // if (code != 0)
  } // submit(String)
} // class UshahidiWebReporter
