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

import edu.grinnell.glimmer.ushahidi.UshahidiWebClient;

/**
 * A simple experiment with Ushahidi Web clients.  Takes the server from 
 * the command line, connects, retrieves all of the incidents as an array,
 * and prints out the array.
 *
 * @version     0.3 of 23 August 2014
 * @author      Samuel A. Rebelsky
 */
public class WebClientArrayExperiment
{
  /**
   * Connect to the server given on the command line.
   */
  public static void main(String[] args)
    throws Exception
  {
    UshahidiWebClient client = new UshahidiWebClient(args[0], 3);
    UshahidiClientUtils.printIncidentArray(client);
  } // main(String[])
} // WebClientArrayExperiment

