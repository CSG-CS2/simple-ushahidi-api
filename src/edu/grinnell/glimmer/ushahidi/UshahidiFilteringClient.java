/**
 * Copyright (c) 2014 Samuel A. Rebelsky.  All rights reserved.
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

import java.util.function.Predicate;

/**
 * More complex Ushahidi clients that filter their input.
 * 
 * @version 0.1.0 of 25 September 2014
 * @author Samuel A. Rebelsky
 */
public interface UshahidiFilteringClient
  extends UshahidiClient
{
  /**
   * Determine if any unseen incidents that meet the predicate remain.
   *
   * @param pred
   *            A predicate that determines whether or not an incident
   *            is acceptable.
   *
   * @return true, if incidents remain; false, otherwise.
   */
  public boolean hasMoreIncidents(Predicate<? super UshahidiIncident> pred);

  /**
   * Get the next unseen incident.
   * 
   * @param pred
   *            A predicate that determines whether or not an incident
   *            is acceptable.
   *
   * @return
   *            Some incident that has not been previously returned,
   *            assuming such an incident exists.
   *
   * @throws Exception
   *             If no incidents remain.
   */
  public UshahidiIncident nextIncident(Predicate<? super UshahidiIncident> pred)
    throws Exception;

} // UshahidiClient

