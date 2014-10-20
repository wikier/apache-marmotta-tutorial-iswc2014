package org.apache.marmotta.tutorial.api;

import org.openrdf.repository.RepositoryException;

import java.awt.image.BufferedImage;

/**
 * ...
 * <p/>
 * Author: Thomas Kurz (tkurz@apache.org)
 */
public interface ImageService {

    /**
     * returns a cropped buffered image for an uri and xywh parameters
     * @param uri
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     * @throws RepositoryException
     */
    public BufferedImage getImage(String uri, int x, int y, int w, int h) throws RepositoryException;

    public String getMimeType(String uri) throws RepositoryException;
}
