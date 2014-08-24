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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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
  // +-----------+-------------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default buffer size (e.g., for reading data)
   */
  static final int DEFAULT_BUFFER_SIZE = 1024;

  /**
   * The smallest date in the range of random dates.
   */
  static final LocalDateTime EARLIEST_DATE = LocalDateTime.of(2012, 1, 1, 0, 0);

  /**
   * The number of seconds between the earliest date and now, used for
   * generating random dates.
   */
  static final long DATE_RANGE_IN_SECONDS = 1 + LocalDateTime.now().getSecond()
                                            - EARLIEST_DATE.getSecond();

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

  // +-----------------------+-------------------------------------------
  // | Static Random Methods |
  // +-----------------------+
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

  // +--------------------------------+----------------------------------
  // | Static Data Generation Methods |
  // +--------------------------------+

  /**
   * Create a sample location.
   */
  static UshahidiLocation sampleLocation()
  {
    UshahidiLocation sample =
        new UshahidiLocation(100, "Grinnell", 41.7436, -92.7247);
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
    sample.categories =
        new UshahidiCategory[] { new UshahidiCategory(1),
                                new UshahidiCategory(2) };

    return sample;
  } // sampleIncident

  /**
   * Create a list of sample incidents.
   */
  static UshahidiIncidentList sampleIncidentList()
  {
    UshahidiIncidentList sample = new UshahidiIncidentList();
    UshahidiCategory[] categories =
        new UshahidiCategory[] { new UshahidiCategory(1) };
    sample.addIncident(new UshahidiIncident(1, "Incident One",
                                            LocalDateTime.of(2013, 9, 1, 1, 0),
                                            sampleLocation(),
                                            "an incident of some sort",
                                            categories));
    sample.addIncident(new UshahidiIncident(
                                            18,
                                            "Another Incident",
                                            LocalDateTime.of(2013, 10, 1, 1, 0),
                                            sampleLocation(),
                                            "another incident of some sort",
                                            categories));
    sample.addIncident(new UshahidiIncident(
                                            3,
                                            "Incident Three",
                                            LocalDateTime.of(2013, 8, 1, 1, 0),
                                            sampleLocation(),
                                            "I'm not sure what this represents.",
                                            categories));
    sample.addIncident(new UshahidiIncident(2, "Incident Two",
                                            LocalDateTime.of(2013, 8, 1, 1, 0),
                                            sampleLocation(), "", categories));
    sample.addIncident(new UshahidiIncident(
                                            11,
                                            "Whatever",
                                            LocalDateTime.of(2013, 6, 1, 1, 0),
                                            sampleLocation(),
                                            "Potentially the last incident we log",
                                            categories));
    return sample;
  } // sampleIncidentList

  // +---------------+---------------------------------------------------
  // | I/O Utilities |
  // +---------------+

  /**
   * Read all of the data from a source using a specified buffer size.
   *
   * @throws IOException
   *    If an I/O error occurs.
   *
   * Based on code by Paul de Vrieze and found at
   *   http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
   */
  public static String readAll(Reader source, final int bufsize)
    throws IOException
  {
    final char[] buf = new char[bufsize];
    final StringBuilder result = new StringBuilder();
    int size;

    while ((size = source.read(buf, 0, bufsize)) >= 0)
      {
        result.append(buf, 0, size);
      } // while

    return result.toString();
  } // readAll(Reader, int)

  /**
   * Read all of the data from a source.
   */
  public static String readAll(Reader source)
    throws IOException
  {
    return readAll(source, DEFAULT_BUFFER_SIZE);
  } // readAll(Reader)

  /**
   * Read all of the data from a source, using a particular encoding.
   */
  public static String readAll(InputStream source, String encoding)
    throws IOException
  {
    InputStreamReader reader = new InputStreamReader(source, encoding);
    String result = readAll(reader);
    reader.close();
    return result;
  } // readAll(InputStream,String)

  /**
   * Read all of the data from a source, using the default (UTF-8)
   * encoding.
   */
  public static String readAll(InputStream source)
    throws IOException
  {
    return readAll(source, "UTF-8");
  } // readAll(InputStream)

  // +----------------------+--------------------------------------------
  // | Additional Utilities |
  // +----------------------+

} // UshahidiUtils
