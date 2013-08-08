/**
 * Copyright (c) 2013 Samuel A. Rebelsky and Daniel Torres.  All rights
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
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * A simple representation of Ushahidi incidents.
 *
 * @version     0.2 of 7 August 2013
 * @author      Daniel Torres
 * @author      Samuel A. Rebelsky
 */
public class UshahidiIncident {

    // +-----------+------------------------------------------------------    
    // | Constants |
    // +-----------+

    /**
     * The constant used to indicate an invalid location.
     */
    public static final int INVALID_INCIDENT_ID = 0;

    /**
     * The constant used to indicate an invalid location.
     */
    public static final int INVALID_LOCATION_ID = 0;

    /**
     * The constant used to indicate an invalid lattitude.
     */
    public static final double INVALID_LATITUDE = 191919;

    /**
     * The constant used to indicate an invalid longitude.
     */
    public static final double INVALID_LONGITUDE = 191919;
    
    /**
     * The format in which Ushahidi seems to report dates.  (Really?)
     */
    static final SimpleDateFormat dateFormat = 
      new SimpleDateFormat("dd-MM-yy hh:mm:ss");

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
    int incidentId = -1; 

    /**
     * The title of the incident.  Set to "" by default.
     */
    String incidentTitle = ""; 

    /**
     * A description of the incident.  Set to "" by default.
     */
    String incidentDescription = ""; 

    /**
     * The date that the incident was reported.  Does not appear to
     * include the time of day.
     */
    Date incidentDate; 

    /**
     * The mode of the incident. (???)
     */
    int incidentMode; //The mode the incident is in.

    /**
     * Is the incident active?
     */
    int incidentActive = 0; 

    /**
     * Is the incident verified?
     */
    int incidentVerified = 0; 

    /**
     * Where did the incident occur?  Stored as an integer identifier.
     */
    int locationId; 

    /**
     * The name of the location.
     */
    String locationName = ""; 

    /**
     * The lattitude of the location.
     */
    double locationLatitude; // Latitude of the report's location.

    /**
     * The longitude of the location.
     */
    double locationLongitude; 

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
    public UshahidiIncident(JSONObject input) 
            throws JSONException, ParseException {
        JSONObject incident = input.getJSONObject("incident");

        // Get basic fields
        this.incidentId = incident.getInt("incidentid");
        this.incidentTitle = incident.getString("incidenttitle");
        this.incidentDescription = incident.getString("incidentdescription");
        this.incidentDate = 
            dateFormat.parse(incident.getString("incidentdate"));
        this.incidentMode = incident.getInt("incidentmode");
        this.incidentActive = incident.getInt("incidentactive");
        this.incidentVerified = incident.getInt("incidentverified");
        try {
            this.locationName = incident.getString("locationname");
        } catch (Exception e) {
            this.locationName = "";
        }


        // Not all incidents have locations, or may have only partial
        // information on the location.  The following sections deal
        // with most location issues.
        try {
            this.locationId = incident.getInt("incidentid");
            if (this.locationId == 0)
                this.locationId = INVALID_LOCATION_ID;
        } catch (Exception e) {
            locationId = INVALID_LOCATION_ID;
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
            
        // Get compound fields
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
            + "]\n";
    } // toString(String)

    // +---------+--------------------------------------------------------
    // | Getters |
    // +---------+

    /**
     * Get the id of the current incident.  Returns INVALID_INCIDENT_ID
     * for an unitialized or otherwise defective incident.
     */
    public int getIncidentId() {
        return incidentId;
    } // getIncidentId()

    /**
     * Get the title of the incident.
     */
    public String getIncidentTitle() {
        return incidentTitle;
    } // getIncidentTitle()

    /** 
     * Get a description of the incident.  This may be an empty string.
     */
    public String getIncidentDescription() {
        return incidentDescription;
    } // getIncidentDescription()

    /**
     * Get the date the incident was reported.
     */
   public Date getIncidentDate() {
        return incidentDate;
    } // getIncidentDate()

    /**
     * Get the mode of the incident.
     */
    public int getIncidentMode() {
        return incidentMode;
    } // getIncidentMode()
    
    /**
     * Determine whether or not the incident is active.
     */
    public int getIncidentActive() {
        return incidentActive;
    } // getIncidentActive()

    /**
     * Determine whether or not the incident is verified.
     */
    public int getIncidentVerified() {
        return incidentVerified;
    } // getIncidentVerified()

    /**
     * Get the id of the location.
     */
    public int getLocationId() {
        return locationId;
    } // getLocationId()

    /**
     * Get the name of the location.
     */
    public String getLocationName() {
        return locationName;
    } // getLocationName()

    /**
     * Get the lattitude of the location.
     */
    public double getLocationLatitude() {
        return locationLatitude;
    } // getLocationLatitude()

    /**
     * Get the longitude of the location.
     */
    public double getLocationLongitude() {
        return locationLongitude;
    } // getLocationLongitude()

    /**
     * Get the categories to which this incident has been assigned.
     */
    public JSONArray getCategories() {
        return categories;
    } // getCategories()

    /**
     * Get any error messages associated with this incident.
     */
    public JSONArray getError() {
        return error;
    } // getError()
    
    /**
     * Get any comments associated with this incident.
     */
    public JSONArray getComments() {
        return comments;
    } // getComments()
    
    /**
     * Get any additional media associated with this incident.
     */
    public JSONArray getMedia() {
        return media;
    } // getMedia()

    /**
     * Get any custom fields associated with this incident.
     */
    public JSONArray getCustomFields() {
        return customFields;
    } // getCustomFields()
} // UshahidiIncident
