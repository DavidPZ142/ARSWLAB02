package edu.eci.arsw.primefinder;

import java.util.Objects;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Control control = Control.newControl();
        
        control.start();
        while (true)
    {

        Scanner entrada = new Scanner(System.in);
        String cadena = entrada.nextLine();
        control.notificar();

    }

    }


	
}
