package org.apache.marmotta.tutorial.webservice;

import org.apache.marmotta.platform.ldp.webservices.LdpWebService;
import org.apache.marmotta.platform.ldp.webservices.PreferHeader;
import org.apache.marmotta.tutorial.api.ImageService;
import org.apache.marmotta.tutorial.model.Rectangle;
import org.jboss.resteasy.spi.NoLogWebApplicationException;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * ...
 * <p/>
 * Author: Thomas Kurz (tkurz@apache.org)
 */
@ApplicationScoped
@Path(LdpWebService.PATH + "{local:.*}")
public class ImageWebservice extends LdpWebService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    ImageService imageService;

    /**
     * returns an (croped) image for an uri
     * @param uriInfo
     * @param type
     * @param preferHeader
     * @param rectangle
     * @return
     * @throws RepositoryException
     * @throws IOException
     */
    @GET
    @Produces("image/*")
    public Response GET(@Context final UriInfo uriInfo,
                        @HeaderParam(HttpHeaders.ACCEPT) @DefaultValue(MediaType.WILDCARD) final String type,
                        @HeaderParam(LdpWebService.HTTP_HEADER_PREFER) PreferHeader preferHeader,
                        @QueryParam("xywh") Rectangle rectangle
    ) throws RepositoryException, IOException {

        String uri = uriInfo.getAbsolutePathBuilder().build().toString();

        if(rectangle != null) {  //crop the image if necessary

            try {

                final BufferedImage image = imageService.getImage(uri,
                        rectangle.getX(),
                        rectangle.getY(),
                        rectangle.getW(),
                        rectangle.getH()
                );

                final String mimetype = imageService.getMimeType(uri);

                log.debug("New image has size: {}x{}", image.getWidth(), image.getHeight());
                log.debug("image has mimetype: {}", mimetype);



                final StreamingOutput entity = new StreamingOutput() {
                    @Override
                    public void write(OutputStream output) throws IOException, WebApplicationException {
                        final String format = mimetype.replaceFirst("^image/", "");
                        if (ImageIO.getImageWritersByFormatName(format).hasNext()) {
                            ImageIO.write(image, format, output);
                        } else {
                            throw new NoLogWebApplicationException(Response.Status.NOT_ACCEPTABLE);
                        }
                    }
                };

                return Response.ok().header("Content-Type", mimetype).entity(entity).build();

            } catch (IllegalArgumentException e) {
                return super.GET(uriInfo, type, preferHeader);
            }

        }

        return super.GET(uriInfo, type, preferHeader);
    }

}
