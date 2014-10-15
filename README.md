ISWC2014 Apache Marmotta tutorial code
======================================

Repository containing running code for the [Apache Marmotta tutorial at ISWC2014](http://marmotta.apache.org/events/iswc2014).

First it is convenient you get the Marmotta source build:

    git clone -b develop https://git-wip-us.apache.org/repos/asf/marmotta.git
    cd marmotta
    mvn install -DskipTests -DskipITs

And then for getting this project running, you just need to execute:

    git clone git@github.com:wikier/apache-marmotta-tutorial-iswc2014.git
    cd apache-marmotta-tutorial-iswc2014
    mvn install
    cd webapp
    mvn tomcat7:run

If you have questions, ask in the [mailing lists](http://marmotta.apache.org/mail-lists) of the project.
