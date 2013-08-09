/**
 * Copyright (c) 2013 Samuel A. Rebelsky.  All rights reserved.
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

import java.util.ArrayList;
import java.util.Collection;

/**
 * A simplified list of incidents, provided in a style similar to
 * that used for UshahidiClient.  Used in a variety of classes that
 * implement UshahidiClient.
 *
 * @version     0.2 of 7 August 2013
 * @author      Samuel A. Rebelsky
 */
public class UshahidiIncidentList implements UshahidiClient {
    // +--------+---------------------------------------------------------
    // | Notess |
    // +--------+
/*
    Although we could use an iterator to implement the hasMoreIncidents
    (== hasNext) and nextIncident (== next), "[t]he behavior of an iterator 
    is unspecified if the underlying collection is modified while the 
    iteration is in progress" [1] and we'd like to be able to add
    elements to the collection on the fly.

    [1] http://docs.oracle.com/javase/6/docs/api/java/util/Iterator.html
 */

    // +--------+---------------------------------------------------------
    // | Fields |
    // +--------+

    /**
     * All of the incidents that have been added to the list.
     */
    ArrayList<UshahidiIncident> incidents = null;

    /**
     * An index into the collection of incidents.
     */
    int index;

    // +--------------+---------------------------------------------------
    // | Constructors |
    // +--------------+

    /**
     * Create a new list with no incidents.
     */
    public UshahidiIncidentList() {
        this.incidents = new ArrayList<UshahidiIncident>();
        this.index = 0;
    } // UshahidiIncidentList

    /**
     * Create a new list with a specified set of incidents.
     */
    public UshahidiIncidentList(Collection<UshahidiIncident> incidents)
    {
        this.incidents = new ArrayList<UshahidiIncident>(incidents);
        this.index = 0;
    } // UshahidiIncidentList(Collection<UshahidiIncident>)

    // +------------------------+-----------------------------------------
    // | UshahidiClient Methods |
    // +------------------------+

    /**
     * Get all of the incidents associated with this list.
     */
    public UshahidiIncident[] getIncidents() {
        return (UshahidiIncident[]) this.incidents.toArray();
    } // getIncidents()

    /**
     * Determine if any unseen incidents remain.
     */
    public boolean hasMoreIncidents() {
        return this.index < this.incidents.size();
    } // hasMoreIncidents()
    
    /** 
     * Get the next unseen incident.
     *
     * @exception Exception     If no incidents remain.
     */
    public UshahidiIncident nextIncident() throws Exception {
        return this.incidents.get(this.index++);
    } // nextIncident()

    // +--------------------+---------------------------------------------
    // | Additional Methods |
    // +--------------------+

    /**
     * Add an incident to the list.
     */
    public void addIncident(UshahidiIncident incident)
    {
        this.incidents.add(incident);
    } // addIncident

} // UshahidiIncidentList

