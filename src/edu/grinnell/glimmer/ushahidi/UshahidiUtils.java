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

import java.time.LocalDateTime;

/**
 * Utilities for working with Ushahidi.
 * 
 * @version 0.4 of 23 August 2014
 * @author Daniel Torres
 * @author Samuel A. Rebelsky
 */
public class UshahidiUtils
{
  // +-----------+------------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The smallest date in the range of random dates.
   */
  static final LocalDateTime EARLIEST_DATE = LocalDateTime.of(2012,1,1,0,0);

  /**
   * The number of seconds between the earliest date and now, used for
   * generating random dates.
   */
  static final long DATE_RANGE_IN_SECONDS = 
      1 + LocalDateTime.now().getSecond() - EARLIEST_DATE.getSecond();

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
  static int random(int n)
  {
    return (int) Math.floor(n * Math.random());
  } // random(int)

  /**
   * Get a random number between 0 and n-1.
   */
  static long random(long n)
  {
    return (long) Math.floor(n * Math.random());
  } // random(long)

  /**
   * Create a "random" date.
   */
  public static LocalDateTime randomDate()
  {
    return EARLIEST_DATE.plusSeconds(random(DATE_RANGE_IN_SECONDS));
  } // randomDate()

  /**
   * Create a "random" location.
   */
  public static UshahidiLocation randomLocation()
  {
    UshahidiLocation random =
        new UshahidiLocation(1000 + random(1000), "Somewhere",
                             random(180) - 90, random(360) - 180);
    return random;
  } // randomLocation()

  /**
   * Create a sample location.
   */
  static UshahidiLocation sampleLocation()
  {
    UshahidiLocation sample =
        new UshahidiLocation(100, "Grinnell", 41.7436, 92.7247);
    return sample;
  } // sampleLocation

  /**
   * Create a "random" incident.
   */
  public static UshahidiIncident randomIncident()
  {
    UshahidiIncident random =
        new UshahidiIncident(1000 + random(1000), "Something happened");
    random.mode = (int) random(1);
    random.date = randomDate();
    random.active = random(2);
    random.verified = random(2);
    random.location = randomLocation();
    random.description = "Wasn't that interesting?";
    return random;
  } // randomIncident

  /**
   * Create a sample incident.
   */
  static UshahidiIncident sampleIncident()
  {
    UshahidiIncident sample = new UshahidiIncident(1, "Sample Incident");
    sample.mode = 0;
    sample.date = LocalDateTime.now();
    sample.active = 1;
    sample.verified = 1;
    sample.location = sampleLocation();
    sample.description = "This is a test.  This is just a test.";
    return sample;
  } // sampleIncident

  /**
   * Create a list of sample incidents.
   */
  static UshahidiIncidentList sampleIncidentList()
  {
    UshahidiIncidentList sample = new UshahidiIncidentList();
    sample.addIncident(new UshahidiIncident(1, "Incident One",
                                            LocalDateTime.of(2013, 9, 1, 1, 0),
                                            sampleLocation(),
                                            "an incident of some sort"));
    sample.addIncident(new UshahidiIncident(18, "Another Incident",
                                            LocalDateTime.of(2013, 10, 1, 1, 0),
                                            sampleLocation(),
                                            "another incident of some sort"));
    sample.addIncident(new UshahidiIncident(3, "Incident Three",
                                            LocalDateTime.of(2013, 8, 1, 1, 0),
                                            sampleLocation(),
                                            "I'm not sure what this represents."));
    sample.addIncident(new UshahidiIncident(2, "Incident Two",
                                            LocalDateTime.of(2013, 8, 1, 1, 0),
                                            sampleLocation(), ""));
    sample.addIncident(new UshahidiIncident(11, "Whatever",
                                            LocalDateTime.of(2013, 6, 1, 1, 0),
                                            sampleLocation(),
                                            "Potentially the last incident we log"));
    return sample;
  } // sampleIncidentList

} // UshahidiUtils
