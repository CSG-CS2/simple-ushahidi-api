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
import java.util.Date;

/**
 * A simple representation of locations of Ushahidi incidents.
 *
 * @version     0.2 of 7 August 2013
 * @author      Daniel Torres
 * @author      Samuel A. Rebelsky
 */
public class UshahidiLocation {

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
     * Where did the incident occur?  Stored as an integer identifier.
     */
    int id = INVALID_LOCATION; 

    /**
     * The name of the location.
     */
    String name = ""; 

    /**
     * The lattitude of the location.
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
     * Create an empty location.  Used mostly for testing. 
     */
    public UshahidiLocation() {
    } // UshahidiLocation()
    
    /**
     * Create a location given only id and name.
     */
    public UshahidiLocation(int id, String name) {
        this.id = id;
        this.name = name;
    } // UshahidiLocation(int, String)
    
    /**
     * Create a location given all the main components.
     */
    public UshahidiLocation(int id, String name, int latitude, int longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    } // UshahidiLocation

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
        return "LOCATION ["
                + "ID: " + this.id
                + sep + "Name: " + this.name
                + sep + "Latitude: " + this.latitude 
                + sep + "Longitude: " + this.longitude 
            + "]";
    } // toString(String)

    // +---------+--------------------------------------------------------
    // | Getters |
    // +---------+

    /**
     * Get the id.
     */
    public int getId() {
        return this.id;
    } // getId()

    /**
     * Get the name.
     */
    public String getName() {
        return this.name;
    } // getName()

    /**
     * Get the lattitude.
     */
    public double getLatitude() {
        return this.latitude;
    } // getLatitude()

    /**
     * Get the longitude.
     */
    public double getLongitude() {
        return this.longitude;
    } // getLongitude()

} // UshahidiIncident
