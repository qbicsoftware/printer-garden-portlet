package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
Source: http://www.geeksforgeeks.org/check-if-url-is-valid-or-not-in-java/
 */
public final class URLValidator {

    private static Matcher matcher;

    //Wikipedia says ports have at most five digits
    //Source: https://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5]):([0-9]{1,5})$";

    private static Pattern ipPattern = Pattern.compile(IPADDRESS_PATTERN);

    //from Android code, Source: https://www.neowin.net/forum/topic/853774-java-regex-to-validate-url/
    public static final Pattern urlPattern  = Pattern.compile(
            new StringBuilder()
                    .append("((?:(http|https|Http|Https|rtsp|Rtsp):")
                    .append("\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)")
                    .append("\\,\\;\\?\\&amp;\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_")
                    .append("\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&amp;\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?")
                    .append("((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+")   // named host
                    .append("(?:")   // plus top level domain
                    .append("(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])")
                    .append("|(?:biz|b[abdefghijmnorstvwyz])")
                    .append("|(?:cat|com|coop|c[acdfghiklmnoruvxyz])")
                    .append("|d[ejkmoz]")
                    .append("|(?:edu|e[cegrstu])")
                    .append("|f[ijkmor]")
                    .append("|(?:gov|g[abdefghilmnpqrstuwy])")
                    .append("|h[kmnrtu]")
                    .append("|(?:info|int|i[delmnoqrst])")
                    .append("|(?:jobs|j[emop])")
                    .append("|k[eghimnrwyz]")
                    .append("|l[abcikrstuvy]")
                    .append("|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])")
                    .append("|(?:name|net|n[acefgilopruz])")
                    .append("|(?:org|om)")
                    .append("|(?:pro|p[aefghklmnrstwy])")
                    .append("|qa")
                    .append("|r[eouw]")
                    .append("|s[abcdeghijklmnortuvyz]")
                    .append("|(?:tel|travel|t[cdfghjklmnoprtvwz])")
                    .append("|u[agkmsyz]")
                    .append("|v[aceginu]")
                    .append("|w[fs]")
                    .append("|y[etu]")
                    .append("|z[amw]))")
                    .append("|(?:(?:25[0-5]|2[0-4]") // or ip address
                    .append("[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]")
                    .append("|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]")
                    .append("[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}")
                    .append("|[1-9][0-9]|[0-9])))")
                    .append("(?:\\:\\d{1,5})?)") // plus option port number
                    .append("(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&amp;\\=\\#\\~")  // plus option query params
                    .append("\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?")
                    .append("(?:\\b|$)").toString()
    );


    public static boolean validate(String url){
        if(url == null || url.isEmpty()){
            return false;
        }
        matcher = ipPattern.matcher(url);
        Boolean isIP = matcher.matches();

        matcher = urlPattern.matcher(url);
        Boolean isURL = matcher.matches();
        if(isIP || isURL) {
            return true;

        }else{
            return false;
        }
    }

}
