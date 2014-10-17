# Marmotta Introduction

This directory contains some sample copy and paste data and instructions that can be used in the walkthrough.

## Sample Data

Our sample data is a simple FOAF dataset with 3 people. You can insert it using the SPARQL explorer (Squebi):

    PREFIX : <http://localhost:8080/resource/>
    PREFIX foaf: <http://xmlns.com/foaf/0.1/>

    INSERT DATA {
      :peter a foaf:Person .
      :peter foaf:name "Peter" .
      :peter foaf:age "27"^^xsd:integer .
      :peter foaf:knows :anna .
      :anna a foaf:Person .
      :anna foaf:name "Anna" .
      :anna foaf:knows :bob .
      :anna foaf:knows :charlie .
      :anna foaf:age "23"^^xsd:integer .
      :bob a foaf:Person .
      :bob foaf:name "Bob" .
      :bob foaf:age "24"^^xsd:integer .
      :charlie a foaf:Person .
      :charlie foaf:name "Charlie" .
      :charlie foaf:age "31"^^xsd:integer .
    }


You can play around a bit with the SPARQL user interface to explore the data and the system.


## Linked Data Access

First step is to try out Linked Data access (i.e. accessing resources in different ways based on HTTP content negotiation).

### HTML Linked Data Explorer

Go to your browser and visit e.g. the Linked Data resource describing Peter:

[http://localhost:8080/resource/peter](http://localhost:8080/resource/peter)

Follow any links you want (e.g. `foaf:knows`) and see what happens.

### RDF Linked Data Access

You can also see Peter in different RDF serializations. The curl command

       curl -iL -H "Accept: text/turtle" http://localhost:8080/resource/peter

will ask for the representation of the resource in turtle serialization. The server will first return a redirect to the
direct turtle representation and then follow that redirect to dump the data to the console. Equally, you can try

       curl -iL -H "Accept: application/rdf+xml" http://localhost:8080/resource/peter

or even

       curl -iL -H "Accept: application/ld+json" http://localhost:8080/resource/peter


## Linked Data Caching

Linked Data Caching transparently accesses remote Linked Data (and even some legacy) sources. Add e.g. the following
data to the system (using SPARQL):

    PREFIX : <http://localhost:8080/resource/>

    INSERT DATA {
      :peter :likes <http://dbpedia.org/resource/Salzburg>
    }

Then go to the Linked Data explorer at [http://localhost:8080/resource/peter](http://localhost:8080/resource/peter)
and follow the `local:likes` link to the DBPedia resource of Salzburg.

The same works for certain legacy data sources, e.g. GData (used by Youtube):

    PREFIX : <http://localhost:8080/resource/>

    INSERT DATA {
      :peter :likes <http://www.youtube.com/v/O5fsctPpBTA>
    }

Again, go to the Linked Data explorer at [http://localhost:8080/resource/peter](http://localhost:8080/resource/peter)
and this time follow the `local:likes` link to the Youtube video.


## LDPath Querying

LDPath is a query language specifically designed for querying Linked Data in combination with Linked Data Caching.
It is much better suited for the task than SPARQL. To try it out, first add some more data:

    PREFIX : <http://localhost:8080/resource/>

    INSERT DATA {
      :peter :likes <http://dbpedia.org/resource/The_Name_of_the_Wind> .
      :peter :likes <http://dbpedia.org/resource/Neuromancer> .
    }

Now we might be interested in which literary genres Peter is interested. So go to the [LDPath Query Interface](http://localhost:8080/ldpath/admin/ldpath.html),
add as context resource `http://localhost:8080/resource/peter`, and then insert the following path query:

    @prefix local: <http://localhost:8080/resource/> ;
    @prefix dbp: <http://dbpedia.org/ontology/>;

    likes = local:likes / dbp:literaryGenre / rdfs:label[@en] :: xsd:string;

This path will follow all `local:likes` links, and then continue traversing by retrieving Linked Data resources in the
background until it can return the English labels of the genres.


## Reasoner

Apache Marmotta's rule-based reasoner can be configured with arbitrary rule programs. This can be used e.g. for introducing
new kinds of relations, or defining reflexivity or transitivity (even "unbalanced" transitivity over several different
properties). In this example, we add a new acquaintance relation by following two steps over foaf:knows; we also define it as
reflexive:

    @prefix local: <http://localhost:8080/resource/>
    @prefix foaf: <http://xmlns.com/foaf/0.1/>

    ($1 foaf:knows $2), ($2 foaf:knows $3) -> ($1 local:acquaintance $3) .
    ($1 local:acquaintance $2) -> ($2 local:acquaintance $1) .