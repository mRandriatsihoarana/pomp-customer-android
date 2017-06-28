package fr.pomp.adfuell.utils.munix;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by munix on 07/04/16.
 */
public class Strings {

    public static final Pattern PARAM_QUOTE_PATTERN = Pattern.compile( "[:;,]|[^\\p{ASCII}]" );
    public static final String LINE_SEPARATOR = "\r\n";
    private static final Pattern ESCAPE_PUNCTUATION_PATTERN = Pattern.compile( "([,;])" );
    private static final Pattern UNESCAPE_PUNCTUATION_PATTERN = Pattern.compile( "\\\\([,;\"])" );
    private static final Pattern ESCAPE_NEWLINE_PATTERN = Pattern.compile( "\r?\n" );
    private static final Pattern UNESCAPE_NEWLINE_PATTERN = Pattern.compile( "(?<!\\\\)\\\\n" );
    private static final Pattern ESCAPE_BACKSLASH_PATTERN = Pattern.compile( "\\\\" );
    private static final Pattern UNESCAPE_BACKSLASH_PATTERN = Pattern.compile( "\\\\\\\\" );

    /**
     * Comprueba si un string tiene formato válido de email
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail( String email ) {
        Pattern emailPattern = Pattern.compile( ".+@.+\\.[a-z]+" );
        Matcher m = emailPattern.matcher( email );
        return m.matches();
    }

    /**
     * Convenience method for adding quotes. The specified
     * object is converted to a string representation by
     * calling its <code>toString()</code> method.
     *
     * @param aValue an object to quote
     * @return a quoted string
     */
    public static String quote( final Object aValue ) {
        if ( aValue != null ) {
            return "\"" + aValue + "\"";
        }
        return "\"\"";
    }

    /**
     * Convenience method for removing surrounding quotes
     * from a string value.
     *
     * @param aValue a string to remove quotes from
     * @return an un-quoted string
     */
    public static String unquote( final String aValue ) {
        if ( aValue != null && aValue.startsWith( "\"" ) && aValue.endsWith( "\"" ) ) {
            return aValue.substring( 0, aValue.length() - 1 ).substring( 1 );
        }
        return aValue;
    }

    /**
     * Convenience method for escaping special characters.
     *
     * @param aValue a string value to escape
     * @return an escaped representation of the specified
     * string
     */
    public static String escape( final String aValue ) {
        return escapePunctuation( escapeNewline( escapeBackslash( aValue ) ) );
    }

    /**
     * Convenience method for replacing escaped special characters
     * with their original form.
     *
     * @param aValue a string value to unescape
     * @return a string representation of the specified
     * string with escaped characters replaced with their
     * original form
     */
    public static String unescape( final String aValue ) {
        return unescapeBackslash( unescapeNewline( unescapePunctuation( aValue ) ) );
    }

    private static String escapePunctuation( String value ) {
        if ( value != null ) {
            return ESCAPE_PUNCTUATION_PATTERN.matcher( value ).replaceAll( "\\\\$1" );
        }
        return value;
    }

    private static String unescapePunctuation( String value ) {
        if ( value != null ) {
            return UNESCAPE_PUNCTUATION_PATTERN.matcher( value ).replaceAll( "$1" );
        }
        return value;
    }

    public static String escapeNewline( String value ) {
        if ( value != null ) {
            return ESCAPE_NEWLINE_PATTERN.matcher( value ).replaceAll( "\\\\n" );
        }
        return value;
    }

    private static String unescapeNewline( String value ) {
        if ( value != null ) {
            return UNESCAPE_NEWLINE_PATTERN.matcher( value ).replaceAll( "\n" );
        }
        return value;
    }

    private static String escapeBackslash( String value ) {
        if ( value != null ) {
            return ESCAPE_BACKSLASH_PATTERN.matcher( value ).replaceAll( "\\\\\\\\" );
        }
        return value;
    }

    private static String unescapeBackslash( String value ) {
        if ( value != null ) {
            return UNESCAPE_BACKSLASH_PATTERN.matcher( value ).replaceAll( "\\\\" );
        }
        return value;
    }

    /**
     * Wraps <code>java.lang.String.valueOf()</code> to return an empty string
     * where the specified object is null.
     *
     * @param object an object instance
     * @return a string representation of the object
     */
    public static String valueOf( final Object object ) {
        if ( object == null ) {
            return "";
        }
        return object.toString();
    }

    /**
     * Pone en mayúsculas el primer caracter de la cadena
     *
     * @param text
     * @return
     */
    public static String ucfirst( String text ) {
        if ( text.substring( 0, 1 ).equals( "¿" ) ) {
            return text.substring( 0, 1 ) + text.substring( 1, 2 )
                    .toUpperCase() + text.substring( 2 ).toLowerCase();
        } else {
            return text.substring( 0, 1 ).toUpperCase() + text.substring( 1 ).toLowerCase();
        }
    }

    /**
     * Une un array list de strings en un único string mediante un separador
     *
     * @param myList
     * @param separator
     * @return String
     */
    public static String implode( ArrayList<String> myList, String separator ) {
        String newString = "";
        if ( myList.size() > 0 ) {
            for ( Iterator<String> it = myList.iterator(); it.hasNext(); ) {
                newString += it.next();
                if ( it.hasNext() ) {
                    newString += separator;
                }
            }
        } else if ( myList.size() == 1 ) {
            return myList.get( 0 );
        }
        return newString;
    }

    /**
     * Une un array de strings en un único string mediante un separador
     *
     * @param myList
     * @param separator
     * @return String
     */
    public static String implode( String[] myList, String separator ) {
        String newString = "";

        if ( myList.length > 1 ) {

            for ( int i = 0; i < myList.length; i++ ) {
                newString += myList[i];
                if ( i < ( myList.length - 1 ) ) {
                    newString += separator;
                }
            }
        } else if ( myList.length == 1 ) {
            return myList[0];
        }
        return newString;
    }

    /**
     * Reemplaza todas las apariciones del string buscado con el string de reemplazo
     *
     * @param source
     * @param target
     * @param replacement
     * @return String
     */
    public static String replace( String source, String target, String replacement ) {
        StringBuilder sbSource = new StringBuilder( source );
        StringBuilder sbSourceLower = new StringBuilder( source.toLowerCase() );
        String searchString = target.toLowerCase();

        int idx = 0;
        while ( ( idx = sbSourceLower.indexOf( searchString, idx ) ) != -1 ) {
            sbSource.replace( idx, idx + searchString.length(), replacement );
            sbSourceLower.replace( idx, idx + searchString.length(), replacement );
            idx += replacement.length();
        }
        sbSourceLower.setLength( 0 );
        sbSourceLower.trimToSize();

        return sbSource.toString();
    }

    /**
     * Comprueba si un string es posible evaularlo como integer
     *
     * @param string
     * @return true si se ha podido evaluar el string como entero
     */
    public static boolean isInteger( String string ) {
        try {
            Integer.parseInt( string );
        } catch ( NumberFormatException e ) {
            return false;
        }
        return true;
    }


    /**
     * Comprueba si un caracter es posible evaularlo como número
     *
     * @return true si se ha podido evaluar el string como entero
     */
    public static boolean isNumber( char c ) {
        if ( c >= '0' && c <= '9' )
            return true;
        return false;
    }

    /**
     * Convierte un string a hash md5
     *
     * @param stringToHash
     * @return md5 del string
     */
    public static String md5( String stringToHash ) {
        if ( stringToHash != null ) {
            try {
                MessageDigest md = MessageDigest.getInstance( "MD5" );
                byte[] bytes = md.digest( stringToHash.getBytes() );
                StringBuilder sb = new StringBuilder( 2 * bytes.length );
                for ( int i = 0; i < bytes.length; i++ ) {
                    int low = ( bytes[i] & 0x0f );
                    int high = ( ( bytes[i] & 0xf0 ) >> 4 );
                    sb.append( Constants.HEXADECIMAL[high] );
                    sb.append( Constants.HEXADECIMAL[low] );
                }
                return sb.toString();
            } catch ( NoSuchAlgorithmException e ) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Reemplaza algunos de los códigos html más comunes
     *
     * @param in
     * @return String
     */
    public static String replaceHTMLCodes( String in ) {
        String[] buenas = {
                "Á",
                "á",
                "É",
                "é",
                "Í",
                "í",
                "Ó",
                "ó",
                "Ú",
                "ú",
                "Ñ",
                "ñ",
                "Ü",
                "ü",
                "«",
                "»",
                "¿",
                "¡",
                "€",
                " ",
                "..."
        };
        String[] catalans = {
                "à",
                "è",
                "ì",
                "ò",
                "ù",
                "À",
                "È",
                "Ì",
                "Ò",
                "Ù",
                "ç",
                "Ç",
                "·",
                "...",
                "–"
        };
        String[] not_catalans = {
                "&#xe0;",
                "&#xe8;",
                "&#xec;",
                "&#xf2;",
                "&#xf9;",
                "&#xc0;",
                "&#xc8;",
                "&#xcc;",
                "&#xd2;",
                "&#xd9;",
                "&#xe7;",
                "&#xc7;",
                "&#xb7;",
                "&#x2026;",
                "&#x2013;"
        };

        String[] not1 = {
                "&#193;",
                "&#225;",
                "&#201;",
                "&#233;",
                "&#205;",
                "&#237;",
                "&#211;",
                "&#243;",
                "&#218;",
                "&#250;",
                "&#209;",
                "&#241;",
                "&#220;",
                "&#252;",
                "&#171;",
                "&#187;",
                "&#191;",
                "&#161;",
                "&#128;",
                " ",
                "..."
        };
        String[] not2 = {
                "&#xc4;",
                "&#xe1;",
                "&#xc9;",
                "&#xe9;",
                "&#xcd;",
                "&#xed;",
                "&#xd3;",
                "&#xf3;",
                "&#xda;",
                "&#xfa;",
                "&#xd1;",
                "&#xf1;",
                "&#xdc;",
                "&#xfc;",
                "&#xab;",
                "&#xbb;",
                "&#xbf;",
                "&#xa1;",
                "&#x80;",
                "&#xA0;",
                "..."
        };
        String[] not3 = {
                "&Aacute;",
                "&aacute;",
                "&Eacute;",
                "&eacute;",
                "&Iacute;",
                "&iacute;",
                "&Oacute;",
                "&oacute;",
                "&Uacute;",
                "&uacute;",
                "&Ntilde;",
                "&ntilde;",
                "&Uuml;",
                "&uuml;",
                "&laquo;",
                "&raquo;",
                "&iquest;",
                "&iexcl;",
                "&euro;",
                " ",
                "&hellip;"
        };

        for ( int i = 0; i < buenas.length; i++ ) {
            in = replace( in, not1[i], buenas[i] );
            in = replace( in, not2[i], buenas[i] );
            in = replace( in, not3[i], buenas[i] );
        }

        for ( int i = 0; i < catalans.length; i++ ) {
            in = replace( in, not_catalans[i], catalans[i] );
        }

        return in;
    }

    /**
     * Devuelve un String envuelto por otro. Ej: substringBetween("*Hola*","*") => Hola
     *
     * @param str
     * @param tag
     * @return
     */
    public static String substringBetween( String str, String tag ) {
        return substringBetween( str, tag, tag );
    }

    /**
     * Devuelve un string contenido entre dos cadenas (open y close)
     *
     * @param str
     * @param open
     * @param close
     * @return
     */
    public static String substringBetween( String str, String open, String close ) {
        if ( str != null && open != null && close != null ) {
            int start = str.indexOf( open );
            if ( start != -1 ) {
                int end = str.indexOf( close, start + open.length() );
                if ( end != -1 ) {
                    return str.substring( start + open.length(), end );
                }
            }

            return null;
        } else {
            return null;
        }
    }

    /**
     * Devuelve todas las coincidencias de strings envueltos por open y close
     *
     * @param str
     * @param open
     * @param close
     * @return
     */
    public static String[] substringsBetween( String str, String open, String close ) {
        if ( str != null && !TextUtils.isEmpty( open ) && !TextUtils.isEmpty( close ) ) {
            int strLen = str.length();
            if ( strLen == 0 ) {
                return new String[]{};
            } else {
                int closeLen = close.length();
                int openLen = open.length();
                ArrayList list = new ArrayList();

                int end;
                for ( int pos = 0; pos < strLen - closeLen; pos = end + closeLen ) {
                    int start = str.indexOf( open, pos );
                    if ( start < 0 ) {
                        break;
                    }

                    start += openLen;
                    end = str.indexOf( close, start );
                    if ( end < 0 ) {
                        break;
                    }

                    list.add( str.substring( start, end ) );
                }

                return list.isEmpty() ? null : (String[]) list.toArray( new String[list.size()] );
            }
        } else {
            return null;
        }
    }

    public static String unescapeUnicode( String escaped ) {
        if ( escaped.indexOf( "\\u" ) == -1 ) {
            return escaped;
        }

        String processed = "";

        int position = escaped.indexOf( "\\u" );
        while ( position != -1 ) {
            if ( position != 0 )
                processed += escaped.substring( 0, position );
            String token = escaped.substring( position + 2, position + 6 );
            escaped = escaped.substring( position + 6 );
            processed += (char) Integer.parseInt( token, 16 );
            position = escaped.indexOf( "\\u" );
        }
        processed += escaped;

        return processed;
    }

    public static String reverse( String s ) {
        return new StringBuilder( s ).reverse().toString();
    }
}
