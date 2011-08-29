Introduction
============

http://www.manning.com/alves/alves_cover150.jpg

OSGi is an emerging Java-based technology for developing modular enterprise applications that can grow and change easily without stopping them entirely. OSGi in Depth picks up where OSGi in Action leaves off, covering important enterprise OSGi services, such as management, configuration, event handling, and software component models. Along the way, the reader learns software engineering best practices that result in systems with flexibility, extensibility, and maintainability. This book also describes how a developer can custom-tailor the OSGi platform, which is in itself modular, by picking and choosing the appropriate services to create domain-specific application frameworks.

This repository includes the source code and the appropriate build files for the book.

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

Follow the instrunctions in the book to properly manage the bundles in the OSGi framework implementation of choosing.

