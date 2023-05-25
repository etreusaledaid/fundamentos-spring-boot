package com.fundamentos.springboot.fundamentos.bean;

public class MyBeanPropertiesImplement implements MyBeanWithProperties{
    private String nombre;
    private String apellido;

    public MyBeanPropertiesImplement(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    @Override
    public String function() {
        return nombre+" - "+apellido;
    }
}
