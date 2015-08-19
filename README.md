# Tournament
A tournament management system (german language)

This software was developed during my computer science studies in a two-week project in collaboration with other students.
It aims to provide a fairly functional management system for tournaments

I've obtained the old sources from svn and set up a new git repository for any future changes or improvements.
Feel free to use and modify this software to your needs, it's GNU public license!

## Features
- Tournament creation
- Team signup
- Role system with Manager, Referees, Players etc
- Quite powerful tournament planning with variably combinable rounds
- Live tournament game tracking and result editing
- Creation of HTML for current tournament status
- Creation of PDF files for winner's certificates
- .. and more i can't remember now

## Requirements
- a MySQL server somewhere with complete access to a database (create tables etc) and according login data
- java jdk 1.6 or newer
- your wits

## Installation instructions
 - clone the repo
 - import the project into Eclipse (or use any other IDE etc to compile everything)
 - edit the <code>META-INF/persistence.xml</code> and insert your database access data
 - insert sample data by running some of the mains in <code>models.sampledata</code>
 - start the application at <code>control.MainApplication</code>
