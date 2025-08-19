package com.example.Literatura.service;

public interface IconvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
