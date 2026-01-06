package br.com.fabricio.gestao_vagas.exceptions;

public class UserFoundException extends RuntimeException{
    public UserFoundException() {
        super("User already exists"); //calls the constructor of parent class(RuntimeException)
    }
}
