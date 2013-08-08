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

/**
 * A simple representation of Ushahidi administrators.  Administrators,
 * like clients, provide a list of incidents.  For administrators, they
 * are pending incidents (as compared to the approved incidents provided
 * by clients).  Administrators also provide capabilities to approve and
 * delete incidents.
 *
 * @version     0.2 of 7 August 2013
 * @author      Samuel A. Rebelsky
 */
public interface UshahidiAdmin extends UshahidiClient {
    /**
     * Approve an incident.
     *
     * @exception Exception     if no such incident exists.
     */
    public String approve(int incidentid);

    /**
     * Delete an incident.
     *
     * @exception Exception     if no such incident exists.
     */
    public String delete(int incidentid);
} // UshahidiAdmin

