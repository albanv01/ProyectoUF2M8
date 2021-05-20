package com.example.incivisme;

public class Opinion {

    String direccio;
    String problema;
    String nombre;
    String apellido;
    String opinion;
    int valoracion;

    public Opinion(String direccio, String problema, String nombre, String apellido, String opinion, int valoracion) {
        this.direccio = direccio;
        this.problema = problema;
        this.nombre = nombre;
        this.opinion = opinion;
        this.valoracion = valoracion;
    }

    public Opinion() {

    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
