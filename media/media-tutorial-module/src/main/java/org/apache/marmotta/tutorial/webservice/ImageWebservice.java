package org.apache.marmotta.tutorial.webservice;

import org.apache.marmotta.platform.ldp.webservices.LdpWebService;
import org.apache.marmotta.platform.ldp.webservices.PreferHeader;
import org.apache.marmotta.tutorial.api.ImageService;
import org.apache.marmotta.tutorial.model.Rectangle;
import org.openrdf.repository.RepositoryException;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * ...
 * <p/>
 * Author: Thomas Kurz (tkurz@apache.org)
 */
@ApplicationScoped
@Path(LdpWebService.PATH + "{local:.*}")
public class ImageWebservice extends LdpWebService {

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

                BufferedImage image = imageService.getImage(uri,
                        rectangle.getX(),
                        rectangle.getY(),
                        rectangle.getW(),
                        rectangle.getH()
                );

                String mimetype = imageService.getMimeType(uri);

                System.out.println("New image has size: " + image.getWidth() + "," + image.getHeight());
                System.out.println("image has mimetype: " + mimetype);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                byte[] imageData = baos.toByteArray();

                return Response.ok().header("Content-Type", mimetype).entity(imageData).build();

            } catch (IllegalArgumentException e) {
                return super.GET(uriInfo, type, preferHeader);
            }

        }

        return super.GET(uriInfo, type, preferHeader);
    }

}
