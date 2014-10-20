package org.apache.marmotta.tutorial.service;

import org.apache.marmotta.platform.core.api.triplestore.SesameService;
import org.apache.marmotta.platform.ldp.api.LdpBinaryStoreService;
import org.apache.marmotta.platform.ldp.api.LdpService;
import org.apache.marmotta.tutorial.api.ImageService;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * ...
 * <p/>
 * Author: Thomas Kurz (tkurz@apache.org)
 */
public class ImageServiceImpl implements ImageService {

    @Inject
    LdpService ldpService;

    @Inject
    SesameService sesameService;

    @Override
    public BufferedImage getImage(String uri, int x, int y, int w, int h) throws RepositoryException {

        BufferedImage img = getImage(uri);

        return img.getSubimage(x, y, w, h);
    }

    @Override
    public String getMimeType(String uri) throws RepositoryException {
        RepositoryConnection conn = null;

        try {
            conn = sesameService.getConnection();

            if(ldpService.exists(conn, uri)) {

                return ldpService.getMimeType(conn, uri);

            }

            throw new NotFoundException("mimetype for resource not found");

        } catch (RepositoryException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("repository exception");
        } finally {
            conn.close();
        }
    }

    @Inject
    private LdpBinaryStoreService binaryStore;

    private BufferedImage getImage(String uri) throws RepositoryException {

        RepositoryConnection conn = null;

        try {
            conn = sesameService.getConnection();

            if(ldpService.exists(conn, uri)) {

                return ImageIO.read(binaryStore.read(uri));

            }

            throw new NotFoundException("resource not found");

        } catch (RepositoryException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("repository exception");
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("cannot read binary resource");
        } finally {
            conn.close();
        }
    }

}
