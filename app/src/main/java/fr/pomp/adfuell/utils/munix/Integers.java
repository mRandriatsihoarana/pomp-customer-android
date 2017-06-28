package fr.pomp.adfuell.utils.munix;

import java.util.Random;

/**
 * Created by munix on 07/04/16.
 */
public class Integers {

    /**
     * Devuelve un número aleatorio entre un mínimo y un máximo
     *
     * @param min
     * @param max
     * @return número aleatorio
     */
    public static int rand( int min, int max ) {
        Random rand = new Random();
        int randomNum = rand.nextInt( max - min + 1 ) + min;
        return randomNum;
    }
}
