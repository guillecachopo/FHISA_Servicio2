package com.example.guill.fhisa_servicio2.Objetos;

import java.util.List;

/**
 * Created by guill on 18/09/2017.
 */

public class Camion {

    String id;
    //Location posiciones;
    Posiciones posiciones;

    List<Posiciones> posicionesList;

    public Camion() {
    }

    public Camion(String id, Posiciones posiciones){
        this.id = id;
        this.posiciones = posiciones;
    }

    public Camion(String id, List<Posiciones> posicionesList) {
        this.id = id;
        this.posicionesList = posicionesList;
    }

    public Camion(Posiciones posiciones){

        this.posiciones = posiciones;
    }

    public Camion(String id){

        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Posiciones getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(Posiciones posiciones) {
        this.posiciones = posiciones;
    }

    public List<Posiciones> getPosicionesList() {
        return posicionesList;
    }

    public void setPosicionesList(List<Posiciones> posicionesList) {
        this.posicionesList = posicionesList;
    }




}
