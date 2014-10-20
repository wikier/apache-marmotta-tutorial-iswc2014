package org.apache.marmotta.tutorial.model;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;

import javax.ws.rs.BadRequestException;

/**
 * ...
 * <p/>
 * Author: Thomas Kurz (tkurz@apache.org)
 */
public class Rectangle {

    int x,y,w,h;

    public Rectangle(String s) {

        Integer[] split = FluentIterable.from(Splitter.on(",").split(s)).transform(new Function<String,Integer>(){
            @Override
            public Integer apply(String s) {
                return Integer.parseInt(s);
            }
        }).toArray(Integer.class);

        if(split.length < 4 || split.length > 4) throw new BadRequestException("xywh does not have a proper format");

        this.x = split[0];
        this.y = split[1];
        this.w = split[2];
        this.h = split[3];

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

}
