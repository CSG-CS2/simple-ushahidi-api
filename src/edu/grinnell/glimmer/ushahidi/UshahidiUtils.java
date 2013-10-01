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

import java.util.GregorianCalendar;

/**
 * Utilities for working with Ushahidi.
 * 
 * @version 0.3 of 25 September 2013
 * @author Daniel Torres
 * @author Samuel A. Rebelsky
 */
public class UshahidiUtils {

    // +-----------+------------------------------------------------------
    // | Constants |
    // +-----------+

    /**
     * A sample Ushahidi location.
     */
    public static final UshahidiLocation SAMPLE_LOCATION = sampleLocation();

    /**
     * A sample Ushahidi incident.
     */
    public static final UshahidiIncident SAMPLE_INCIDENT = sampleIncident();

    /**
     * A list of sample incidents.
     */
    public static final UshahidiClient SAMPLE_CLIENT = sampleIncidentList();

    // +----------------+-------------------------------------------------
    // | Static Methods |
    // +----------------+

    /**
     * Get a random number between 0 and n-1.
     */
    static int random(int n) {
	return (int) Math.floor(n * Math.random());
    } // random(int)

    /**
     * Create a "random" location.
     */
    public static UshahidiLocation randomLocation() {
	UshahidiLocation random = new UshahidiLocation(1000 + random(1000),
		"Somewhere", random(180) - 90, random(360) - 180);
	return random;
    } // randomLocation

    /**
     * Create a sample location.
     */
    static UshahidiLocation sampleLocation() {
	UshahidiLocation sample = new UshahidiLocation(100, "Grinnell",
		41.7436, 92.7247);
	return sample;
    } // sampleLocation

    /**
     * Create a "random" incident.
     */
    public static UshahidiIncident randomIncident() {
	UshahidiIncident random = new UshahidiIncident(1000 + random(1000),
		"Something happened");
	random.mode = (int) random(1);
	random.date = new GregorianCalendar(2012 + random(5), 1 + random(12),
		random(30));
	random.active = random(2);
	random.verified = random(2);
	random.location = randomLocation();
	random.description = "Wasn't that interesting?";
	return random;
    } // randomIncident

    /**
     * Create a sample indicent.
     */
    static UshahidiIncident sampleIncident() {
	UshahidiIncident sample = new UshahidiIncident(1, "Sample Incident");
	sample.mode = 0;
	sample.date = new GregorianCalendar(2013, 9, 1);
	sample.active = 1;
	sample.verified = 1;
	sample.location = sampleLocation();
	sample.description = "This is a test.  This is just a test.";
	return sample;
    } // sampleIncident

    /**
     * Create a list of sample incidents.
     */
    static UshahidiIncidentList sampleIncidentList() {
	UshahidiIncidentList sample = new UshahidiIncidentList();
	sample.addIncident(new UshahidiIncident(1, "Incident One",
		new GregorianCalendar(2013, 9, 1), sampleLocation(),
		"an incident of some sort"));
	sample.addIncident(new UshahidiIncident(18, "Another Incident",
		new GregorianCalendar(2013, 10, 1), sampleLocation(),
		"another incident of some sort"));
	sample.addIncident(new UshahidiIncident(3, "Incident Three",
		new GregorianCalendar(2013, 8, 1), sampleLocation(),
		"I'm not sure what this represents."));
	sample.addIncident(new UshahidiIncident(2, "Incident Two",
		new GregorianCalendar(2013, 8, 1), sampleLocation(),
		""));
	sample.addIncident(new UshahidiIncident(11, "Whatever",
		new GregorianCalendar(2013, 6, 1), sampleLocation(),
		"Potentially the last incident we log"));
	return sample;
    } // sampleIncidentList

} // UshahidiUtils
