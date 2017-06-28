package fr.pomp.adfuell.utils.munix;

import android.os.Looper;

/**
 * Created by munix on 07/04/16.
 */
public class Threads {

    /**
     * Retorna el nombre de la clase dentro de la posición del stacktrace del hilo principal
     *
     * @param stackTracePosition
     * @return String
     */
    public static String getCallerClassName( int stackTracePosition ) {
        String fullClassName = Thread.currentThread()
                .getStackTrace()[stackTracePosition].getClassName();
        return fullClassName.substring( fullClassName.lastIndexOf( "." ) + 1 );
    }

    /**
     * Retorna el nombre del método de la clase dentro de la posición del stacktrace del hilo principal
     *
     * @param stackTracePosition
     * @return String
     */
    public static String getCallerMethodName( int stackTracePosition ) {
        return Thread.currentThread().getStackTrace()[stackTracePosition].getMethodName();
    }

    /**
     * Retorna el número de la línea de la clase dentro de la posición del stacktrace del hilo principal
     *
     * @param stackTracePosition
     * @return int
     */
    public static int getCallerLineNumber( int stackTracePosition ) {
        return Thread.currentThread().getStackTrace()[stackTracePosition].getLineNumber();
    }

    /**
     * Nos indica si estamos ejecutando este método en el hilo principal de la app
     *
     * @return
     */
    public static Boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * Retorna la referencia de un Thread dado por su nombre
     *
     * @param name
     * @return
     */
    public static Thread getThreadByName( String name ) {
        final ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        int numThreads = currentGroup.activeCount();
        Thread[] listOfThreads = new Thread[numThreads];

        currentGroup.enumerate( listOfThreads );
        for ( int i = 0; i < numThreads; i++ ) {
            if ( listOfThreads[i].getName().equals( name ) )
                return listOfThreads[i];
        }
        return null;
    }

    /**
     * Nos indica si un thread dado por su nombre está en ejecución
     *
     * @param name
     * @return
     */
    public static Boolean isThreadRunning( String name ) {
        return getThreadByName( name ) != null;
    }
}
