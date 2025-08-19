package com.example.Literatura.model;

public enum Idioma {
    EN("Inglés"),
    ES("Español"),
    FR("Francés"),
    DE("Alemán"),
    PT("Portugués"),
    IT("Italiano"),
    ZH("Chino"),
    OTRO("Otro");

    private final String nombre;

    private Idioma(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
    public static Idioma fromString(String text) {
        try {
            return Idioma.valueOf(text.toUpperCase()); // <- cambia aquí
        } catch (IllegalArgumentException e) {
            return OTRO;
        }
    }

//    public static Idioma fromString(String text) {
//        for(Idioma idioma : Idioma.values()){
//            if(idioma.nombre.equalsIgnoreCase(text)){
//                return idioma;
//            }
//        }
//        throw new IllegalArgumentException("Ningun idioma encontrado"+text);
////        try {
////            return Idioma.valueOf(codigo.toUpperCase());
////        } catch (IllegalArgumentException e) {
////            return OTRO;
////        }
//    }

}
