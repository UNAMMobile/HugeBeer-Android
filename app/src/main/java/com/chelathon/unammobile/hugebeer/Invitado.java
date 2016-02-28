package com.chelathon.unammobile.hugebeer;

/**
 * Created by Safe on 28/02/2016.
 */
public class Invitado {

    private final String nombre;
    private final String fbId;

    public Invitado(String nombre, String fbId){

        this.nombre = nombre;
        this.fbId = fbId;
    }

    public String getFbId() {
        return fbId;
    }

    public String getNombre() {
        return nombre;
    }
}
