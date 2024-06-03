package com.example.asistify;

public class Asistencia {
    private String nombre;
    private String correo;
    private String fecha;

    public Asistencia(String asistenciaId, String nombre, String email, String timestamp) {
        // Constructor vacío requerido para Firebase
    }

    public Asistencia(String nombre, String correo, String fecha) {
        this.nombre = nombre;
        this.correo = correo;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
