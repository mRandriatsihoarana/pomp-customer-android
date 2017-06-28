package fr.pomp.adfuell.utils.comon;

/**
 * Created by edena on 28/03/2017.
 */

public class Config {
    public static boolean DEBUG = true;
     //MODIFIENA KO NY AO RETROFIT  IRetrofitService.java
     //@GET("/pomp/web/oauth/v2/token") //jacky
    //@GET("/oauth/v2/token") //tena izy

    public static String WS_ULR = "http://dev.backoffice.pompfuel.com";//http://dev.backoffice.pomp.fr
    public static final String WS_PATH = "/api/private/";
    public static String WS_AUTH_CLIENT_ID  = "2_ie3xkrhzl5skkgo0oksgw8s4o4k0gkk0s440wo8owo4s8s4ks";
    public static String WS_AUTH_CLIENT_SECRET  = "40v648psz38kgcog80s8s48ckosww00ggw8o0sog8gs4g4gc04";


    /*public static String WS_ULR = "http://192.168.10.135:8080";
    //public static final String WS_PATH = "/pomp/web/app_dev.php/api/private/";
    public static final String WS_PATH = "/pomp/web/api/private/";
    public static String WS_AUTH_CLIENT_ID  = "1_tuzok1txwrkgggoskgoc4kcgooc8wo4c08wws8wk8gsg8k4oc";
    public static String WS_AUTH_CLIENT_SECRET  = "2gtnfcnpxd448gss8ogw4w8cs8s440socc08wkowo0oc4co8co";*/

    public static String JSON_CODE_COUNTRY  = "code.json";


    /*
    Configuration
    l'accès au GIT : http://gitlab.pomp.fr/
    login : grochedereux@wylog.com
    pwd : A1Z2E3R4

    http://dev.backoffice.pomp.fr
    login : ext@wylog.com
    pwd : ext@wylog.com

    http://dev.account.pomp.fr/fr/login
    http://dev.account.pomp.fr/fr/logout
    wylog@wylog.com

     */


}
