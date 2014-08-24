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

import edu.grinnell.glimmer.ushahidi.UshahidiClientTestHarness;
import edu.grinnell.glimmer.ushahidi.UshahidiIncident;

import java.io.PrintWriter;

/**
 * A simple experiment using the UshahidiClientTestHarndess clients.  
 * Adds a few incidents, prints out a few incidents, adds a few more,
 * prints out the remaining ones.
 *
 * @version     0.3 of 23 August 2014
 * @author      Samuel A. Rebelsky
 */
public class TestHarnessClientExperiment
{
  /**
   * Do the main work.
   */
  public static void main(String[] args)
    throws Exception
  {
    UshahidiClientTestHarness client = new UshahidiClientTestHarness();
    client.addIncident(new UshahidiIncident(11, "Eleven"));
    client.addIncident(new UshahidiIncident(42, "Forty two"));
    UshahidiClientUtils.printIncidents(client);
    client.addIncident(new UshahidiIncident(0, "Zero"));
    UshahidiClientUtils.printIncidents(client);
    System.out.println("*** Dumping Array ***");
    UshahidiClientUtils.printIncidentArray(client);
  } // main(String[])
} // TestHarnessClientExperiment

