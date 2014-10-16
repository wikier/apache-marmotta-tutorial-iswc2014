ISWC2014 Apache Marmotta tutorial code
=====================================

Repository containing running code for the [Apache Marmotta tutorial at ISWC2014](http://marmotta.apache.org/events/iswc2014).

![Apache Marmotta](http://marmotta.apache.org/images/Marmotta_Logo_250x102.png)

First, although optional, it's convenient you get the Marmotta source locally build:

    git clone -b develop https://git-wip-us.apache.org/repos/asf/marmotta.git
    cd marmotta
    mvn install -DskipTests -DskipITs

And then for getting this project running, you just need to execute:

    git clone git@github.com:wikier/apache-marmotta-tutorial-iswc2014.git
    cd apache-marmotta-tutorial-iswc2014
    mvn clean tomcat7:run

If you have questions, ask us or send your question to the 
[mailing lists](http://marmotta.apache.org/mail-lists) of the project.
