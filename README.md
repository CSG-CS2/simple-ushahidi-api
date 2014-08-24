simple-ushahidi-api
===================

A simple Java API for dealing with Ushahidi servers.  Created as part of
the "Computing for Social Good in CS2" project.  Some more information on
that project can be found at
  http://www.cs.grinnell.edu/~rebelsky/Glimmer/Ushahidi/

The API now takes advantage of some features of Java 8, such as `LocalDate`
and `LocalDateTime`.  In older versions, we found that it seemed to matter
which version of the JDK you used if the server was provided over https.
Oracle JDK 1.7 worked fine, Oracle JDK 1.6 and OpenJDK 1.7 did not.  We have
not yet reverified that https still works with Oracle Java 1.8

Requires Java 8.
