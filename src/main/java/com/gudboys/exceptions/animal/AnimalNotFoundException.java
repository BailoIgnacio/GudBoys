package com.gudboys.exceptions.animal;

public class AnimalNotFoundException extends RuntimeException{
    public AnimalNotFoundException(String msg){
        super(msg);
    }
    public AnimalNotFoundException(Long id){
        super("Animal no encontrado con el id:" + id);
    }

}
