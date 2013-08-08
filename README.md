simple-ushahidi-api
===================

A simple Java API for dealing with Ushahidi servers.  Created as part of
the "Computing for Social Good in CS2" project.  Some more information on
that project can be found at
  http://www.cs.grinnell.edu/~rebelsky/Glimmer/Ushahidi/

Right now, if the Ushahidi server uses https, you must use Oracle JDK 1.7.
Neither JDK 1.6 nor OpenJDK 1.7 seem to handle https in the same way.
(For example, while we can connect to https://*.crowdmap.com using Oracle
JDK 1.7, we can' usint JDK 1.6 or OpenJDK 1.7.)
