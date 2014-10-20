package org.apache.marmotta.tutorial.webservice;

import com.google.common.io.Resources;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.DecoderConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.response.Response;
import org.apache.marmotta.platform.core.test.base.JettyMarmotta;
import org.apache.marmotta.platform.ldp.webservices.LdpWebService;
import org.apache.marmotta.tutorial.vocabularies.OA;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.rio.RDFFormat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.ws.rs.core.HttpHeaders;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * ...
 * <p/>
 * Author: Thomas Kurz (tkurz@apache.org)
 */
public class ImageWebserviceTest {

    private static JettyMarmotta marmotta;

    @BeforeClass
    public static void beforeClass() {
        marmotta = new JettyMarmotta("/media-tutorial-test", 9090, LdpWebService.class, ImageWebservice.class);

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 9090;
        RestAssured.basePath = "/media-tutorial-test";
        RestAssured.config = RestAssuredConfig.newConfig().decoderConfig(DecoderConfig.decoderConfig().defaultContentCharset("UTF-8"));
    }

    @AfterClass
    public static void afterClass() {
        if (marmotta != null) {
            marmotta.shutdown();
        }
    }

    @Test
    public void testWorkflow() throws IOException, InterruptedException {

        byte[] img = Resources.toByteArray(Resources.getResource("lions.jpg"));

        Response response1 = RestAssured
                .given()
                .content(img)
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .expect()
                .statusCode(201)
                .when()
                .post(LdpWebService.PATH)
                .andReturn();

        String resourceUri = response1.getHeader("Location");

        Response response3 = RestAssured.given()
                .header(HttpHeaders.ACCEPT, "image/jpeg")
                .expect()
                .statusCode(200)
                .when()
                .get(resourceUri + "?xywh=333,354,135,115")
                .andReturn();

        BufferedImage image = ImageIO.read(response3.body().asInputStream());

        draw(image);
        Thread.sleep(5000);

        Assert.assertEquals(135, image.getWidth());
        Assert.assertEquals(115, image.getHeight());

        //add annotation
        Response response4 = RestAssured
                .given()
                .content((
                        "<> a <" + OA.Annotation + ">; " +
                                "<" + OA.hasTarget + "> <" + resourceUri + "#xywh=333,354,135,115>; " +
                                "<" + OA.hasBody + "> \"Lion\".").getBytes())
                .header(HttpHeaders.CONTENT_TYPE, RDFFormat.TURTLE.getDefaultMIMEType())
                .expect()
                .statusCode(201)
                .when()
                .post(resourceUri.substring(0,resourceUri.length() - ".jpg".length()))
                .andReturn();

        String annotationUri = response4.getHeader("Location");

        Response response5 = RestAssured.given()
                .header(HttpHeaders.ACCEPT, RDFFormat.TURTLE.getDefaultMIMEType())
                .expect()
                .statusCode(200)
                .get(annotationUri)
                .andReturn();

        String result = response5.getBody().print();
        Assert.assertTrue(result.contains("<http://www.w3.org/ns/oa#hasBody> \"Lion\" ."));
    }

    private JFrame draw(BufferedImage img) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.setVisible(true);
        return frame;
    }

}
