package com.example.guill.fhisa_servicio2.Objetos;

import android.location.Location;

/**
 * Created by guill on 03/10/2017.
 */

public class MyLocation extends Location {

    public String provider;

    public MyLocation(String provider) {
        super(provider);
        this.provider = provider;
    }

    public MyLocation() {
        super("provider");

    }

}
