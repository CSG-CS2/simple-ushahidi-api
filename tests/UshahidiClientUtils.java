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

import java.io.PrintWriter;

import edu.grinnell.glimmer.ushahidi.UshahidiClient;

/**
 * An infrastructure for simple experiments with Ushahidi clients.  
 *
 * @version     0.2 of 7 August 2013
 * @author      Samuel A. Rebelsky
 */
public class UshahidiClientUtils {
     /**
      * Print all of the incidents from an UshahidiClient to a
      * given destination.
      */
     public static void printIncidents(UshahidiClient client, PrintWriter out)
             throws Exception {
         while (client.hasMoreIncidents()) {
             out.println(client.nextIncident().toString());
             out.flush();
         } // while
     } // printIncidents(UshahidiClient, PrintWriter)

     /**
      * Print all of the incidents from an UshahidiClient to standard
      * output.
      */
    public static void printIncidents(UshahidiClient client) throws Exception {
        printIncidents(client, new PrintWriter(System.out));
    } // printIncidents
} // UshahidiClientUtils

