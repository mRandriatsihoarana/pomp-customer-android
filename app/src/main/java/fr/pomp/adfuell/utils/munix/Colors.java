package fr.pomp.adfuell.utils.munix;

/**
 * Created by munix on 07/04/16.
 */
public class Colors {

    /**
     * Convierte un entero en un código de color único
     *
     * @param i
     * @return
     */
    public static String intToARGB( int i ) {
        return Integer.toHexString( ( ( i >> 16 ) & 0xFF ) ) +
                Integer.toHexString( ( ( i >> 8 ) & 0xFF ) ) +
                Integer.toHexString( ( i & 0xFF ) );
    }
}
