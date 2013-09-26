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
 * A test harness for programs that work with UshahidiClient objects.
 * Lets you set up a simulated set of incidents and treat it as you
 * would an UshahidiWebClient.
 *
 * @version     0.3 of 25 September 2013
 * @author      Samuel A. Rebelsky
 */
public class UshahidiClientTestHarness extends UshahidiIncidentList {
    // +--------+---------------------------------------------------------
    // | Notess |
    // +--------+
/*
    Although UshahidiClientTestHarness is much like UshahidiIncidentList,
    it is provided as a separate class in case we want to add some methods
    to make testing easier, such as methods to add random elements.  (I'm
    not sure why random elements would be useful for testing, but, hey, you
    never know.)
 */

    // +--------------+---------------------------------------------------
    // | Constructors |
    // +--------------+

    /**
     * Create a new test harness with no incidents.
     */
    public UshahidiClientTestHarness() {
        super();
    } // UshahidiClientTestHarness

    /**
     * Create a new test harness with a specified set of incidents.
     */
    public UshahidiClientTestHarness(Collection<UshahidiIncident> incidents)
    {
        super(incidents);
    } // UshahidiClientTestHarness(Collection<UshahidiIncident>)

} // UshahidiClientTestHarness

