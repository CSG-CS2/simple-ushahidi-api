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
 * A simple representation of Ushahidi clients.  Provides incidents in an
 * iterator style (but without using Iterators, so that novices can
 * use it) and as an array.
 *
 * @version     0.3 of 25 September 2013
 * @author      Samuel A. Rebelsky
 */
public interface UshahidiClient {
    /**
     * Get all of the incidents associated with this instance.
     */
    public UshahidiIncident[] getIncidents();

    /**
     * Determine if any unseen incidents remain.
     */
    public boolean hasMoreIncidents();
    
    /** 
     * Get the next unseen incident.
     *
     * @throws Exception     If no incidents remain.
     */
    public UshahidiIncident nextIncident() throws Exception;

} // UshahidiClient

