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

If you have questions, ask [us](http://marmotta.apache.org/events/iswc2014.html#Presenters) 
or send your question to the [mailing lists](http://marmotta.apache.org/mail-lists) of the project.

## Slides

  * [Introduction](http://www.slideshare.net/wastl/apache-marmotta-introduction)
  * [Linked Data Platform](http://www.slideshare.net/Wikier/introduction-to-ldp-in-apache-marmotta)
  * [Semantic Media Management](http://www.slideshare.net/thkurz1/semantic-media-managent-with-apache-marmotta)

## Acknowledgements

This tutorial is partially supported by [MICO](http://mico-project.eu) (grant no. 610480) and [Fusepool P3](http://p3.fusepool.eu) (grant no. 609696) FP7 projects, as well as the [Apache Software Foundation](http://www.apache.org).

![MICO](http://marmotta.apache.org/images/mico.png)

![Fusepool P3](http://marmotta.apache.org/images/fusepoolp3.png)
