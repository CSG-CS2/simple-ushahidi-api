/**
 * Copyright (c) 2013 Samuel A. Rebelsky.  All rights
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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An Ushahidi client that gets in data from the Web.  The most typical
 * form of Ushahidi client.
 *
 * @version     0.2 of 7 August 2013
 * @author      Samuel A. Rebelsky
 * @author      Daniel Torres
 */
public class UshahidiWebClient implements UshahidiClient {
    // +-------+----------------------------------------------------------
    // | Notes |
    // +-------+
/*
    Right now, this code assumes that the server has no more than 
    DEFAULT_NUM_INCIDENTS incidents.  If the server has more than 
    DEFAULT_NUM_INCIDENTS incidents, the client is likely to miss 
    the ones with lower ids.
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
     * The base URL of the server that we're using (protocol plus
     * hostname or IP address).
     */
    String server;

    /**
     * The largest id we've seen so far.
     */
    int maxId;

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
     *   A string that gives the prefix of the URL, including the protocol 
     *   and the hostname.  For example,
     *     https://farmersmarket.crowdmap.com/
     * @exception Exception     
     *   when we cannot connect to the server.
     * @pre
     *   The constructor must be a valid URL for an Ushahidi server.
     * @post
     *   The server is available for obtaining values.
     */
    public UshahidiWebClient(String server) throws Exception {
        this(server, DEFAULT_NUM_INCIDENTS);
    } // UshahidiWebClient

    /**
     * Create a new client that connects to server to obtain up to
     * numIncidents incidents.
     * 
     * @param server    
     *   A string that gives the prefix of the URL, including the protocol 
     *   and the hostname.  For example,
     *     <code>https://farmersmarket.crowdmap.com/</code>
     * @param numIncidents
     *   The number of incidents we grab from the server, more or less.
     *   Must be a non-negative integer.
     * @exception Exception     
     *   when we cannot connect to the server.
     * @pre
     *   The constructor must be a valid URL for an Ushahidi server.
     * @post
     *   The server is available for obtaining values.
     */
    public UshahidiWebClient(String server, int numIncidents) throws Exception {
        // Fill in the fields
        this.server = server;
        this.numIncidents = numIncidents;
        this.maxId = 0;
        this.incidents = new UshahidiIncidentList();
        if (this.fetchIncidents () == 0) {
          throw new Exception("No incidents available.");
        } // if (this.fetchIndicents() == 0)
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
     *   If we cannot get incidents from the server, or if any of
     *   the incidents is malformed.
     * @pre
     *   This object has been initialized.
     * @post
     *   This object now contains up to numIncidents additional incidents.
     *   (It will be fewer than numIncidents if the server provides fewer.)
     */
    public int fetchIncidents() throws Exception {
        BufferedReader input;   // Textual input from the server
        String line;            // One line of data from the server
        String text = "";       // Data read from the server
        JSONObject data;        // Parsed text from the server
        String errorNumber;     // The number of the error
        URL serverURL;          // The full URL for fetching data

        // Determine the URL to use.  We use different URLs depending on
        // whether this is the first time we've tried to fetch incidents
        // or a subsequent time.
        if (this.maxId == 0) {
            serverURL = 
                new URL(this.server + "/api?task=incidents&by=all&limit=" 
                        + numIncidents);
        } else {
            serverURL =
                new URL(this.server 
                        + "/api?task=incidents&by=sinceid"
                        + "&id=" + this.maxId
                        + "&limit=" + numIncidents);
        } 

        // Connect to the server
        HttpURLConnection connection;
        try {
            connection = 
                (HttpURLConnection) serverURL.openConnection();
            connection.connect();
        } catch (Exception e) {
            throw new Exception("Could not connect to " + this.server 
                               + " because " + e.toString());
        }
        
        // Read the data from the server
        try {
            input = 
                new BufferedReader(
                    new InputStreamReader(
                        connection.getInputStream()));
        } catch (Exception e) {
            throw new Exception("Could not get Ushahidi data from " 
                                + this.server);
        }

        while ((line = input.readLine()) != null) {
            text += line;
        } // while

        // Testing: What does the text look like?
        // System.out.println(text);

        // Grab the JSON text, which starts with an open brace
        text = text.substring(text.indexOf("{"));

        // Convert the text to a JSONObject
        data = new JSONObject(text);

        // Check to make sure that we succeeded
        try {
            JSONObject error = data.getJSONObject("error");
            if (error.getInt("code") != 0) {
                System.out.println("Could not get data from server because "
                                   + error.getInt("message"));
                return 0;
            } // if the code indicates that the request was invalid
        } catch (Exception e) {
            return 0;
        } // catch (Exception)

        // Grab all of the elements
        JSONArray incidents = 
            data.getJSONObject("payload").getJSONArray("incidents");
        int len = incidents.length();
        for (int i = 0; i < len; i++) {
            UshahidiIncident nextIncident = 
               new UshahidiIncident((JSONObject) incidents.get(i));
            this.incidents.addIncident(nextIncident);
            if (nextIncident.getIncidentId() > this.maxId)
                this.maxId = nextIncident.getIncidentId();
        } // for
        return len;
    } // fetchIncidents()
    
    // +----------------+-------------------------------------------------
    // | Public Methods |
    // +----------------+

    public UshahidiIncident[] getIncidents() {
        return this.incidents.getIncidents();
    } // getIncidents()

    /**
     * Determine if any unseen incidents remain.
     */
    public boolean hasMoreIncidents() {
        // If the list has more incidents, we're set.
        if (this.incidents.hasMoreIncidents())
            return true;
        // Otherwise, try to fetch some more incidents.
        try {
            return (this.fetchIncidents() > 0);
        } catch (Exception e) {
            return false;
        }
    } // hasMoreIncidents()
    
    /** 
     * Get the next unseen incident.
     *
     * @exception Exception     If no incidents remain.
     */
    public UshahidiIncident nextIncident() throws Exception {
        // If there aren't any unvisited incidents left
        if (!this.incidents.hasMoreIncidents()) {
            // Try to get some more
            if (this.fetchIncidents() == 0) {
                throw new Exception("No incidents remain.");
            } // if we can't fetch any incidents
        } // if no incidents rmeain

        return this.incidents.nextIncident();
    } // nextIncident

} // UshahidiClient

