package fr.pomp.adfuell.utils.munix;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by munix on 19/04/16.
 */
public class Logs {

    private static Logs instance;
    private Class crashlyticsClass;

    public Logs( Class crashlyticsClass ) {
        this.crashlyticsClass = crashlyticsClass;
    }

    public static Logs getInstance( Class crashlyticsClass ) {
        if ( instance == null ) {
            instance = new Logs( crashlyticsClass );
        }
        return instance;
    }

    public static Logs getInstance() {
        return instance;
    }

    public static void verbose( String tag, String text ) {
        if ( getInstance() != null && getInstance().getCrashlyticsClass() != null ) {
            logWithCrashlytics( getInstance().getCrashlyticsClass(), Log.VERBOSE, tag, "CL: " + getFormattedLogLine( 5 ) + text );
        } else {
            Log.v( tag, getFormattedLogLine( 5 ) + text );
        }
    }

    public static void warn( String tag, String text ) {
        if ( getInstance() != null && getInstance().getCrashlyticsClass() != null ) {
            logWithCrashlytics( getInstance().getCrashlyticsClass(), Log.WARN, tag, "CL: " + getFormattedLogLine( 5 ) + text );
        } else {
            Log.w( tag, getFormattedLogLine( 5 ) + text );
        }
    }

    public static void info( String tag, String text ) {
        if ( getInstance() != null && getInstance().getCrashlyticsClass() != null ) {
            logWithCrashlytics( getInstance().getCrashlyticsClass(), Log.INFO, tag, "CL: " + getFormattedLogLine( 5 ) + text );
        } else {
            Log.i( tag, getFormattedLogLine( 5 ) + text );
        }
    }

    public static void error( String tag, String text ) {
        if ( getInstance() != null && getInstance().getCrashlyticsClass() != null ) {
            logWithCrashlytics( getInstance().getCrashlyticsClass(), Log.ERROR, tag, "CL: " + getFormattedLogLine( 5 ) + text );
        } else {
            Log.e( tag, getFormattedLogLine( 5 ) + text );
        }
    }

    public static void debug( String tag, String text ) {
        if ( getInstance() != null && getInstance().getCrashlyticsClass() != null ) {
            logWithCrashlytics( getInstance().getCrashlyticsClass(), Log.DEBUG, tag, "CL: " + getFormattedLogLine( 5 ) + text );
        } else {
            Log.d( tag, getFormattedLogLine( 5 ) + text );
        }
    }

    public static void chunkLog( int priority, String tag, String text ) {
        Boolean hasCrashlytics = getInstance() != null && getInstance().getCrashlyticsClass() != null;
        if ( text.length() > 4000 ) {
            int chunkCount = text.length() / 4000;
            for ( int i = 0; i <= chunkCount; i++ ) {
                int max = 4000 * ( i + 1 );
                String log = "Chunk " + ( i + 1 ) + " of " + ( chunkCount + 1 ) + ":" + getFormattedLogLine( 5 ) + text;
                if ( max >= text.length() ) {
                    if ( hasCrashlytics ) {
                        logWithCrashlytics( getInstance().getCrashlyticsClass(), Log.DEBUG, tag, "CL: " + log
                                .substring( 4000 * i ) );
                    } else {
                        Log.println( priority, tag, log.substring( 4000 * i ) );
                    }
                } else {
                    if ( hasCrashlytics ) {
                        logWithCrashlytics( getInstance().getCrashlyticsClass(), Log.DEBUG, tag, "CL: " + log
                                .substring( 4000 * i, max ) );
                    } else {
                        Log.println( priority, tag, log.substring( 4000 * i, max ) );
                    }
                }
            }
        } else {
            if ( hasCrashlytics ) {
                logWithCrashlytics( getInstance().getCrashlyticsClass(), Log.DEBUG, tag, "CL: " + getFormattedLogLine( 5 ) + text );
            } else {
                Log.println( priority, tag, getFormattedLogLine( 5 ) + text );
            }
        }
    }

    public static void logWithCrashlytics( Class crashlyticsClass, int priority, String tag, String text ) {
        try {
            Method[] methods = crashlyticsClass.getMethods();
            for ( Method m : methods ) {
                if ( m.getName().equals( "log" ) && m.getParameterTypes()[0].toString()
                        .equals( "int" ) ) {
                    m.invoke( null, priority, tag, text );
                    break;
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private static String getFormattedLogLine( int pos ) {
        String className = Threads.getCallerClassName( pos );
        String methodName = Threads.getCallerMethodName( pos );
        int lineNumber = Threads.getCallerLineNumber( pos );
        return className + "." + methodName + "():" + lineNumber + ": ";
    }

    public Class getCrashlyticsClass() {
        return crashlyticsClass;
    }
}
