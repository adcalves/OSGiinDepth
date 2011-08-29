Pre-requisitives
================

Make sure you have the following installed and available in your execution PATH:

- ANT

- Java

Setup
=====

- Install your OSGi framework of choice (e.g. Felix Apache, Eclipse Equinox).

- Update build.properties to reflect to install home of the OSGi framework.

Build
=====

Under each chapter directory, you may invoke the following ANT tasks:

- ant clean: cleans all built Java classes and JARs.

- ant [dist]: builds all needed bundles for the book listings and places them under the directory 'dist'.

- ant install: copies the bundles to the bundle directory in the OSGi framework implementation.


