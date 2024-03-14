package com.cibertec.ecommerce.model;

public class Response<T> {

    public   boolean success;
    public  String message;
    public  T data ;

    public Response() {

    }

    public Response( T data, String mensaje) {
    	this.success=true;
    	this.message=mensaje;
    	this.data=data;
    }

    public Response(String mensaje) {
        this.success=false;
        this.message=mensaje;
        this.data=null;
    }

}
