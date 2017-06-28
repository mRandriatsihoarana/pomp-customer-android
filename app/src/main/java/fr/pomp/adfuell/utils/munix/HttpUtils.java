package fr.pomp.adfuell.utils.munix;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by munix on 9/12/16
 */
public class HttpUtils {

    private static final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    private static final String ipv6Pattern = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";
    private static Pattern VALID_IPV4_PATTERN = null;
    private static Pattern VALID_IPV6_PATTERN = null;

    static {
        try {
            VALID_IPV4_PATTERN = Pattern.compile( ipv4Pattern, Pattern.CASE_INSENSITIVE );
            VALID_IPV6_PATTERN = Pattern.compile( ipv6Pattern, Pattern.CASE_INSENSITIVE );
        } catch ( PatternSyntaxException e ) {
            //logger.severe("Unable to compile pattern", e);
        }
    }

    public static String getLocalIpAddress( boolean removeIPv6 ) {
        try {
            for ( Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for ( Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if ( inetAddress.isSiteLocalAddress() &&
                            !inetAddress.isAnyLocalAddress() &&
                            ( !removeIPv6 || isIpv4Address( inetAddress.getHostAddress()
                                    .toString() ) ) ) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch ( SocketException ignore ) {
        }
        return null;
    }

    public static boolean isIpAddress( String ipAddress ) {
        Matcher m1 = HttpUtils.VALID_IPV4_PATTERN.matcher( ipAddress );
        if ( m1.matches() ) {
            return true;
        }
        Matcher m2 = HttpUtils.VALID_IPV6_PATTERN.matcher( ipAddress );
        return m2.matches();
    }

    public static boolean isIpv4Address( String ipAddress ) {
        Matcher m1 = HttpUtils.VALID_IPV4_PATTERN.matcher( ipAddress );
        return m1.matches();
    }

    public static boolean isIpv6Address( String ipAddress ) {
        Matcher m1 = HttpUtils.VALID_IPV6_PATTERN.matcher( ipAddress );
        return m1.matches();
    }
}
