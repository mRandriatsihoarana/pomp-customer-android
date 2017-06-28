package fr.pomp.adfuell.utils.munix;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by munix on 28/11/16.
 */

public class Net {

    /**
     * Obtiene las cookies de una url
     *
     * @param url
     * @param headers
     * @return
     */
    public static String getCookies( String url, HashMap<String,String> headers ) {
        try {
            URLConnection connection = new URL( url ).openConnection();
            connection.setUseCaches( false );
            if ( headers != null && headers.size() > 0 ) {
                for ( Map.Entry<String,String> entry : headers.entrySet() ) {
                    connection.setRequestProperty( entry.getKey(), entry.getValue() );
                }
            }
            List cookies = connection.getHeaderFields().get( "Set-Cookie" );
            StringBuilder cookiesBuilder = new StringBuilder();

            if ( cookies != null && cookies.size() > 0 ) {
                for ( int i = 0; i < cookies.size(); i++ ) {
                    cookiesBuilder.append( cookies.get( i ).toString() + "; " );
                }
            }
            return cookiesBuilder.toString();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Obtiene las cookies de una url
     *
     * @param url
     * @return
     */
    public static String getCookies( String url ) {
        return getCookies( url, null );
    }

    public static String Get( String uri ) {
        return Get( uri, null );
    }

    public static String Get( String uri, HashMap<String,String> headers ) {
        HttpURLConnection urlConnection = null;
        String result = "";
        //StringBuilder stringBuilder;

        try {
            URL url = new URL( uri );
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches( false );
            if ( headers != null && headers.containsKey( "timeOutConnect" ) ) {
                int timeOut = Integer.parseInt( headers.get( "timeOutConnect" ) ) * 1000;
                urlConnection.setConnectTimeout( timeOut );
                urlConnection.setReadTimeout( timeOut );
            }

            if ( headers != null && headers.size() > 0 ) {
                for ( Map.Entry<String,String> entry : headers.entrySet() ) {
                    urlConnection.setRequestProperty( entry.getKey(), entry.getValue() );
                }
            }

            int code = urlConnection.getResponseCode();

            if ( code == HttpURLConnection.HTTP_OK ) {
                InputStream in = new BufferedInputStream( urlConnection.getInputStream() );

                if ( "gzip".equals( urlConnection.getContentEncoding() ) ) {
                    in = new GZIPInputStream( in );
                }

                //stringBuilder = new StringBuilder();
                if ( in != null ) {
                    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( in ) );
                    String line;
                    while ( ( line = bufferedReader.readLine() ) != null ) {
                        //stringBuilder.append( line );
                        result += line + Strings.LINE_SEPARATOR;
                    }
                    //result = stringBuilder.toString();
                }
                in.close();
            } else if ( code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_SEE_OTHER ) {
                return Get( urlConnection.getHeaderField( "Location" ), headers );
            }

            return result;
        } catch ( Exception e ) {
            //e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return result;
    }

    public static String Post( String uri, HashMap<String,String> params ) {
        return Post( uri, params, null );
    }

    public static String Post( String uri, HashMap<String,String> params, HashMap<String,String> headers ) {
        HttpURLConnection urlConnection = null;
        String result = "";
        //StringBuilder stringBuilder;

        try {
            URL url = new URL( uri );
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches( false );
            if ( headers != null && headers.containsKey( "timeOutConnect" ) ) {
                int timeOut = Integer.parseInt( headers.get( "timeOutConnect" ) ) * 1000;
                urlConnection.setConnectTimeout( timeOut );
                urlConnection.setReadTimeout( timeOut );
            }

            if ( headers != null && headers.size() > 0 ) {
                for ( Map.Entry<String,String> entry : headers.entrySet() ) {
                    urlConnection.setRequestProperty( entry.getKey(), entry.getValue() );
                }
            }

            urlConnection.setRequestMethod( "POST" );
            urlConnection.setDoInput( true );
            urlConnection.setDoOutput( true );

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
            writer.write( getPostDataString( params ) );

            writer.flush();
            writer.close();
            os.close();

            int code = urlConnection.getResponseCode();

            if ( code == HttpURLConnection.HTTP_OK ) {
                InputStream in = new BufferedInputStream( urlConnection.getInputStream() );

                if ( "gzip".equals( urlConnection.getContentEncoding() ) ) {
                    in = new GZIPInputStream( in );
                }

                //stringBuilder = new StringBuilder();
                if ( in != null ) {
                    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( in ) );
                    String line;
                    while ( ( line = bufferedReader.readLine() ) != null ) {
                        //stringBuilder.append( line );
                        result += line + Strings.LINE_SEPARATOR;
                    }
                    //result = stringBuilder.toString();
                }
                in.close();
            } else if ( code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_SEE_OTHER ) {
                return Post( urlConnection.getHeaderField( "Location" ), headers, params );
            }

            return result;
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return result;
    }

    private static String getPostDataString( HashMap<String,String> params ) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for ( Map.Entry<String,String> entry : params.entrySet() ) {
            if ( first )
                first = false;
            else
                result.append( "&" );

            result.append( URLEncoder.encode( entry.getKey(), "UTF-8" ) );
            result.append( "=" );
            result.append( URLEncoder.encode( entry.getValue(), "UTF-8" ) );
        }

        return result.toString();
    }

    public static String GetRedirectUrl( String url ) {
        try {
            URL obj = new URL( url );
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.addRequestProperty( "Accept", "*/*" );
            conn.setConnectTimeout( 4000 );
            conn.setReadTimeout( 4000 );
            conn.setUseCaches( false );
            conn.setInstanceFollowRedirects( false );
            return conn.getHeaderField( "Location" ) != null ? conn.getHeaderField( "Location" )
                    .replace( "[", "" )
                    .replace( "]", "" ) : url;
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return url;
    }

    public static Boolean isUrlAlive( String url ) {
        try {
            URL u = new URL( url );
            HttpURLConnection huc;
            huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod( "GET" );
            huc.setUseCaches( false );
            huc.connect();
            int code = huc.getResponseCode();
            String clen = huc.getHeaderField( "Content-Length" );
            if ( code == HttpURLConnection.HTTP_NOT_FOUND )
                return false;
            if ( code == HttpURLConnection.HTTP_OK && ( !TextUtils.isEmpty( clen ) && !clen.equals( "0" ) ) )
                return true;
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getMimeTypeFromExtensionUrl( String url ) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl( url );
        if ( extension != null ) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension( extension );
            Logs.info( "Mime", "mimeType getMimeTypeFromExtension " + type );
        }
        return type;
    }

    public static String getMimeTypeOnline( String url ) {
        String mimeType = null;
        try {
            URLConnection connection = new URL( url ).openConnection();
            connection.setConnectTimeout( 1500 );
            connection.setReadTimeout( 1500 );
            connection.setUseCaches( false );
            mimeType = connection.getContentType();
            Logs.info( "Mime", "mimeType from content-type " + mimeType );
        } catch ( Exception ignored ) {
        }

        if ( mimeType == null ) {
            try {
                mimeType = URLConnection.guessContentTypeFromName( url );
            } catch ( Exception ignored ) {
            }
            Logs.info( "Mime", "mimeType guessed from url " + mimeType );
        }
        return mimeType;
    }
}
