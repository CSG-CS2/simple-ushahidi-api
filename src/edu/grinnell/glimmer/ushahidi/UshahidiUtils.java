/**
 * Copyright (c) 2013 Samuel A. Rebelsky.   All rights reserved.
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
 * Utilities for working with Ushahidi.
 *
 * @version     0.3 of 16 September 2013
 * @author      Daniel Torres
 * @author      Samuel A. Rebelsky
 */
public class UshahidiUtils {

    // +-----------+------------------------------------------------------    
    // | Constants |
    // +-----------+

    /**
     * A sample Ushahidi location.
     */
    public static UshahidiLocation SAMPLE_LOCATION =
        sampleLocation();

    /**
     * A sample Ushahidi incident.
     */
    public static final UshahidiIncident SAMPLE_INCIDENT =
        sampleIncident();

    // +----------------+-------------------------------------------------    
    // | Static Methods |
    // +----------------+

    /**
     * Create a "random" location.
     */
    public static UshahidiLocation randomLocation() {
        UshahidiLocation random = new UshahidiLocation(1000 + random(1000),
                "Somewhere", (Math.random() * 180) - 90,
                (Math.random() * 360) - 180);
        return random;
    } // randomLocation

    /**
     * Create a sample location.
     */
    public static UshahidiLocation sampleLocation() {
        UshahidiLocation sample = new UsahidiLocation(100, "Grinnell",
                41.7436, 92.7247);
        return sample;
    } // sampleLocation

    /**
     * Create a "random" incident.
     */
    public static UshahidiIncident randomIncident() {
        UsahidiIncident random = 
                UshahidiIncident(1000 + Math.random(1000), "Something happened");
        random.mode = Math.random(1);
        random.date = new Date(2012 + Math.random(5), 1 + Math.random(12), Math.random(30));
        random.active = Math.random(2);
        random.verified = Math.random(2);
        random.location = randomLocation();
        return random;
    } // randomIncident

    /**
     * Create a sample indicent.
     */
    static UshahidiIncident sampleIncident() {
         UshahidiIncident sample = new UshahidiIncident(1, "Sample Incident");
         sample.mode = 0;
         sample.date = new Date(2013, 09, 01);
         sample.active = 1;
         sample.verified = 1;
         sample.location = sampleLocation();
         return sample;
    } // sampleIncident

    // +--------+---------------------------------------------------------    
    // | Fields |
    // +--------+

    /*  
     * Required fields.  Taken from
     *   https://wiki.ushahidi.com/display/WIKI/Ushahidi+Public+API
     */

    /**
     * The identification number that corresponds to this incident.  Set to
     * -1 to indicate "not yet initialized.
     */
    int id = -1; 

    /**
     * The title of the incident.  Set to "" by default.
     */
    String title = ""; 

    /**
     * A description of the incident.  Set to "" by default.
     */
    String description = ""; 

    /**
     * The date that the incident was reported.  Does not appear to
     * include the time of day.
     */
    Date date; 

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
     * Where did the incident occur?  Uses null for an unknown location.
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
    JSONArray error = null; //Is there an error involving this incident.

    /**
     * Any additional fields provided by this Ushahidi installation.
     */
    JSONArray customFields = null;
   

    // +--------------+---------------------------------------------------    
    // | Constructors |
    // +--------------+

    /**
     * Create an empty incident.  Used mostly for testing. 
     */
    public UshahidiIncident() {
    } // UshahidiIncident()
    
    /**
     * Create a basic incident with only id and title.  Also used primarily
     * for testing.
     */
    public UshahidiIncident(int id, String title) {
        this.incidentId = id;
        this.incidentTitle = title;
    } // UshahidiIncident(int, String)
    
    /**
     * Create an incident from a partially parsed JSON response.
     *
     * @exception JSONException
     *   if it is unable to get one of the required fields from the
     *   JSON object.
     * @exception ParseException
     *   if it is unable to convert the required field to the appropriate
     *   type.
     */
    UshahidiIncident(JSONObject input) 
            throws JSONException, ParseException {
        JSONObject incident = input.getJSONObject("incident");

        // Get basic fields
        this.id = incident.getInt("incidentid");
        this.title = incident.getString("incidenttitle");
        this.description = incident.getString("incidentdescription");
        this.date = 
            dateFormat.parse(incident.getString("incidentdate"));
        this.mode = incident.getInt("incidentmode");
        this.active = incident.getInt("incidentactive");
        this.verified = incident.getInt("incidentverified");


        // Not all incidents have locations, or may have only partial
        // information on the location.  The following sections deal
        // with most location issues.
        int locationId;
        String locationName;
        double locationLatitude;
        double locationLongitude;

        try {
            locationName = incident.getString("locationname");
        } catch (Exception e) {
            locationName = "";
        }
        try {
            locationId = incident.getInt("incidentid");
            if (locationId == 0)
                locationId = UshahidiLocation.INVALID_LOCATION_ID;
        } catch (Exception e) {
            locationId = UshahidiLocation.INVALID_LOCATION_ID;
        } // catch (Exception)
        try {
            locationLatitude = incident.getDouble("locationlatitude");
        } catch (Exception e) {
            locationLatitude = INVALID_LATITUDE;
        } // catch (Exception)
        try {
            locationLongitude = incident.getDouble("locationlongitude");
        } catch (Exception e) {
            locationLongitude = INVALID_LONGITUDE;
        } // catch (Exception)

        this.location = 
              new UshahidiLocation(locationId, locationName, locationLatitude,
                       locaitonLongitude);
            
        // Get compound fields.  Right now, we don't reveal this to the client.
        categories = input.getJSONArray("categories");
        media = input.getJSONArray("media");
        comments = input.getJSONArray("comments");
        
        // Get the error messages
        try {error = input.getJSONArray("error");
        } // try{error = input.getJSONArray("error")}
        catch(JSONException e) {
            error = null;
        } // catch(JSONException e)
        
        // Get the array of custom fields
        try {customFields = input.getJSONArray("customfields");
        } // try{customFields = input.getJSONArray("customfields")}
        catch(JSONException e){
            customFields = null;
        } // catch(JSONException e)
    } // UshahidiIncident(JSONObject)

    // +-------------------------+----------------------------------------
    // | Standard Object Methods |
    // +-------------------------+

    /**
     * Convert the incident to a string (e.g., for printing).
     */
    public String toString() {
        return this.toString(", ");
    } // toString()

    /**
     * Convert the incident to a string, using sep to separate the items.
     */
    public String toString(String sep)
    {
        return "INCIDENT ["
            + "Title: " + this.incidentTitle 
            + sep + "ID: " + this.incidentId 
            + sep + "Description: " + this.incidentDescription 
            + sep + "Date: " + this.incidentDate 
            + sep + "Location ID: " + this.locationId 
            + sep + "Location Name: " + this.locationName 
            + sep + "Location Latitude: " + this.locationLatitude 
            + sep + "Location Longitude: " + this.locationLongitude 
            + "]";
    } // toString(String)

    // +---------+--------------------------------------------------------
    // | Getters |
    // +---------+

    /**
     * Get the id of the current incident.  Returns INVALID_INCIDENT_ID
     * for an unitialized or otherwise defective incident.
     */
    public int getId() {
        return incidentId;
    } // getId()

    /**
     * Get the title of the incident.
     */
    public String getTitle() {
        return incidentTitle;
    } // getTitle()

    /** 
     * Get a description of the incident.  This may be an empty string.
     */
    public String getDescription() {
        return incidentDescription;
    } // getDescription()

    /**
     * Get the date the incident was reported.
     */
   public Date getDate() {
        return incidentDate;
    } // getDate()

    /**
     * Get the mode of the incident.
     */
    public int getMode() {
        return incidentMode;
    } // getMode()
    
    /**
     * Determine whether or not the incident is active.
     */
    public int getActive() {
        return incidentActive;
    } // getActive()

    /**
     * Determine whether or not the incident is verified.
     */
    public int getVerified() {
        return incidentVerified;
    } // getVerified()

    /**
     * Get the location of the incident.  Returns null if no
     * location has been assigned.
     */
    public UshahidiLocation getLocation() {
        return this.location;
    } // getLocation
} // UshahidiIncident
