# LDP Hands-On

After the introduction in [LDP](), let's give it a try.

## Basic Data (LDP-SR and LDPC)

For our scenario, we create a blog about Apache Marmotta:

    curl -iX POST -H "Content-Type: text/turtle" \
        -H "Slug: Apache Marmotta" \
        --data @data/blog.ttl \
        http://localhost:8080/ldp

and add a first post.

    curl -iX POST -H "Content-Type: text/turtle" \
        -H "Slug: ISWC2014 Tutorial" \
        --data @data/post.ttl \
        http://localhost:8080/ldp/Apache-Marmotta

Now, let's have a look at the data;

    curl -i -H "Accept: text/turtle" \
        http://localhost:8080/ldp/Apache-Marmotta

    curl -i -H "Accept: text/turtle" \
        http://localhost:8080/ldp/Apache-Marmotta/ISWC2014-Tutorial

## Binary Data (LDP-NR)

Let's add an image to the blogpost:

    curl -iX POST -H "Content-Type: image/png" \
        -H "Slug: iswc2014" \
         --data-binary @data/iswc2014.png \
         http://localhost:8080/ldp/Apache-Marmotta/ISWC2014-Tutorial

For LDP-NR, Marmotta automatically creates an associated LDP-SR. Now what is present at the server?

    curl -I http://localhost:8080/ldp/Apache-Marmotta/ISWC2014-Tutorial/iswc2014.png

The `Link`-header with `rel=describedBy` refers to the LDP-SR

    curl -i http://localhost:8080/ldp/Apache-Marmotta/ISWC2014-Tutorial/iswc2014

Now the Blogpost `ldp:contains` the image:

    curl -i -H "Accept: text/turtle" \
        http://localhost:8080/ldp/Apache-Marmotta/ISWC2014-Tutorial

## Update LDP-SR (POST)

To refer to the image in the blogpost, we update the post. For that, we need the `ETag` of the post:

    curl -I -H "Accept: text/turtle" \
        http://localhost:8080/ldp/Apache-Marmotta/ISWC2014-Tutorial

and send it with the updated data:

    curl -iX PUT -H "Content-Type: text/turtle" \
        -H 'If-Match: W/"12345"' \
        --data @data/post_with_image.ttl \
        http://localhost:8080/ldp/Apache-Marmotta/ISWC2014-Tutorial

## More on Containers

First, we add two commments:

    curl -iX POST -H "Content-Type: text/turtle" \
        -H "Slug: comment" \
        -H 'Link: <http://www.w3.org/ns/ldp#Resource>; rel="type"' \
        --data @data/comment1.ttl \
        http://localhost:8080/ldp/Apache-Marmotta/ISWC2014-Tutorial
    curl -iX POST -H "Content-Type: text/turtle" \
        -H "Slug: comment" \
        --data @data/comment2.ttl \
        http://localhost:8080/ldp/Apache-Marmotta/ISWC2014-Tutorial

### LDP Interaction Model

There are two interaction models defined in LDP: LDPC and LDPR.

* **LDPC**, the default, creates a
    container which accepts POST requests to create new resources.
* **LDPR**, default for LDP-NR, creates a special resource which *does not* accept
    POST requests to create new resources.

The first comment was created with the **LDPR Interaction Model** so POSTing there will cause an error:

    curl -iX POST -H "Content-Type: text/turtle" \
        --data @data/comment2.ttl \
        http://localhost:8080/ldp/Apache-Marmotta/ISWC2014-Tutorial/comment

### Prefer-Header

By providing a `Prefer`-header, a client can git the server a hint which part of the
resource is most appropriate to the client's needs.

Let's again look at the post:

    curl -i http://localhost:8080/ldp/Apache-Marmotta/ISWC2014-Tutorial

Now that's a lot of data. Since we only want to display the post-content to the user, we are not
interested in the meta-data about the post - we only want the actual content:

    curl -i -H "Accept: text/turtle" \
        -H 'Prefer: return=representation; omit="http://www.w3.org/ns/ldp#PreferMinimalContainer http://www.w3.org/ns/ldp#PreferContainment"' \
        http://localhost:8080/ldp/Apache-Marmotta/ISWC2014-Tutorial
