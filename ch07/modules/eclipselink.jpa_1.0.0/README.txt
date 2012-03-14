EclipseLink 2.3.2 does not yet fully implement the OSGi persistence specification.
Notably, one has to explicit import the Database driver, and use a JPA-PersistenceUnits manifest header.

Further, following these steps:

1) Download the EclipseLink OSGi Bundles Zip from the EclipseLink web-site: http://www.eclipse.org/eclipselink/downloads/

2) Install EclipseLink bundles in Felix framework by placing in bundle directory.

3) Build eclipselink.jpa bundle in this chapter.

4) Install eclipselink.jpa in Felix.

