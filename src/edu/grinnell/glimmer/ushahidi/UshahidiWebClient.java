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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An Ushahidi client that gets data from the Web. The most typical form of
 * Ushahidi client.
 * 
 * @version 0.4.1 of 24 September 2014
 * @author Samuel A. Rebelsky
 * @author Daniel Torres
 */
public class UshahidiWebClient
    implements UshahidiClient
{
  // +-------+----------------------------------------------------------
  // | Notes |
  // +-------+
  
  /*

Right now, this code assumes that the server has no more than
DEFAULT_NUM_INCIDENTS incidents. If the server has more than
DEFAULT_NUM_INCIDENTS incidents, the client is likely to miss the ones
with lower ids.

Right now, this code assumes that the server returns incidents
from highest id to lowest id, since that's the behavior we've
seen.

   */

  // +-----------+------------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default number of incidents we fetch from the server.
   */
  static final int DEFAULT_NUM_INCIDENTS = 5000;

  // +--------+---------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * All of the incidents that we've loaded so far.
   */
  UshahidiIncidentList incidents = null;

  /**
   * The base URL of the server that we're using (protocol plus hostname or IP
   * address).
   */
  String server;

  /**
   * The largest id we've seen so far.
   */
  int maxId;

  /**
   * The smallest id we've seen so far.
   */
  int minId;

  /**
   * The number of incidents we fetch.
   */
  int numIncidents;

  // +--------------+---------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new client that connects to server to obtain data.
   * 
   * @param server
   *            A string that gives the prefix of the URL, including the
   *            protocol and the hostname. For example,
   *            https://farmersmarket.crowdmap.com/
   * @exception Exception
   *                when we cannot connect to the server.
   * @pre The constructor must be a valid URL for an Ushahidi server.
   * @post The server is available for obtaining values.
   */
  public UshahidiWebClient(String server) throws Exception
  {
    this(server, DEFAULT_NUM_INCIDENTS);
  } // UshahidiWebClient

  /**
   * Create a new client that connects to the specified server to obtain 
   * up to numIncidents incidents.
   * 
   * @param server
   *            A string that gives the prefix of the URL, including the
   *            protocol and the hostname. For example,
   *            <code>https://farmersmarket.crowdmap.com/</code> or
   *            <code>http://ushahidi1.grinnell.edu/sandbox/</code>
   * @param numIncidents
   *            The number of incidents we grab from the server, more or less.
   *            Must be a non-negative integer.
   * @exception Exception
   *                when we cannot connect to the server.
   * @pre The constructor must be a valid URL for an Ushahidi server.
   * @post The server is available for obtaining values.
   */
  public UshahidiWebClient(String server, int numIncidents) throws Exception
  {
    // Fill in the fields
    this.server = server;
    this.numIncidents = numIncidents;
    this.maxId = 0;
    this.minId = Integer.MAX_VALUE;
    this.incidents = new UshahidiIncidentList();
    this.fetchIncidents();
  } // UshahidiWebClient

  // +---------+--------------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Fetch the next set of incidents from the server.
   * 
   * @return n, The number of incidents fetched
   * 
   * @exception Exception
   *                If we cannot get incidents from the server, or if any of
   *                the incidents is malformed.
   * @pre This object has been initialized.
   * @post This object now contains up to numIncidents additional incidents.
   *       (It will be fewer than numIncidents if the server provides fewer.)
   */
  public int fetchIncidents()
    throws Exception
  {
    BufferedReader input; // Textual input from the server
    String line; // One line of data from the server
    String text = ""; // Data read from the server
    JSONObject data; // Parsed text from the server
    URL serverURL; // The full URL for fetching data

    // Determine the URL to use. We use different URLs depending on
    // whether this is the first time we've tried to fetch incidents
    // or a subsequent time.
    if (this.minId == Integer.MAX_VALUE)
      {
        serverURL =
            new URL(this.server + "/api?task=incidents&by=all&limit="
                    + numIncidents);
      } // if (this.minId == Integer.MAX_VALUE)
    else
      {
        serverURL =
            new URL(this.server + "/api?task=incidents&by=maxid" + "&id="
                    + this.minId + "&limit=" + numIncidents);
      } // if (this.minId != INTEGER.MAX_VALUE)

    // Connect to the server
    HttpURLConnection connection;
    try
      {
        connection = (HttpURLConnection) serverURL.openConnection();
        connection.connect();
      } // try
    catch (Exception e)
      {
        throw new Exception("Could not connect to " + this.server + " because "
                            + e.toString());
      } // catch

    // Read the data from the server
    try
      {
        text = UshahidiUtils.readAll(connection.getInputStream());
      } // try
    catch (Exception e)
      {
        throw new Exception("Could not get Ushahidi data from " + this.server);
      } // catch

    // Experiment: What does the text look like?
    // System.out.println(text);

    // Grab the JSON text, which starts with an open brace
    text = text.substring(text.indexOf("{"));

    // Convert the text to a JSONObject
    data = new JSONObject(text);

    // Check for error codes in the result.  The error code is 0 if we
    // have data, and something else if we don't.
    JSONObject error;
    try
      {
        error = data.getJSONObject("error");
      } // try
    catch (Exception e)
      {
        throw new Exception("Server returned invalid response");
      } // catch
    int code;
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
        throw new Exception("Could not get data from server because "
            + error.getString("message"));
      } // if (code != 0)

    // Grab all of the elements
    JSONArray incidents =
        data.getJSONObject("payload").getJSONArray("incidents");
    int len = incidents.length();
    for (int i = 0; i < len; i++)
      {
        UshahidiIncident nextIncident =
            new UshahidiIncident((JSONObject) incidents.get(i));
        this.incidents.addIncident(nextIncident);
        int id = nextIncident.getId();
        if (id > this.maxId)
          this.maxId = id;
        if (id < this.minId)
          this.minId = id;
      } // for
    return len;
  } // fetchIncidents()

  // +----------------+-------------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Get all of the incidents.
   */
  public UshahidiIncident[] getIncidents()
  {
    // Fetch all remaining incidents into the list.
    try
      {
        while (fetchIncidents() > 0)
          ;
      } // try
    catch (Exception e)
      {
      } // catch (Exception)

    // And then convert to an array
    return this.incidents.getIncidents();
  } // getIncidents()

  /**
   * Determine if any unseen incidents remain.
   */
  public boolean hasMoreIncidents()
  {
    // If the list has more incidents, we're set.
    if (this.incidents.hasMoreIncidents())
      return true;
    // Otherwise, try to fetch some more incidents.
    try
      {
        return (this.fetchIncidents() > 0);
      } // try
    catch (Exception e)
      {
        return false;
      } // catch
  } // hasMoreIncidents()

  /**
   * Get the next unseen incident.
   * 
   * @exception Exception
   *                If no incidents remain.
   */
  public UshahidiIncident nextIncident()
    throws Exception
  {
    // If there aren't any unvisited incidents left
    if (!this.incidents.hasMoreIncidents())
      {
        // Try to get some more
        if (this.fetchIncidents() == 0)
          {
            throw new Exception("No incidents remain.");
          } // if we can't fetch any incidents
      } // if no incidents remain

    return this.incidents.nextIncident();
  } // nextIncident

} // UshahidiWebClient

