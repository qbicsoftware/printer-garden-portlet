package life.qbic.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * This class {@link URLValidator} validates if an IP address or URL matches the expected pattern via regular expressions
 *  The used regexs can be found here:
 *  URL Pattern: https://www.neowin.net/forum/topic/853774-java-regex-to-validate-url/
 *  IP Pattern: https://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
 *  @author fhanssen
 */

public final class URLValidator {

    //Wikipedia says ports have at most five digits
    //Source: https://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5]):([0-9]{1,5})$";

    private static final Pattern ipPattern = Pattern.compile(IPADDRESS_PATTERN);

    //from Android code, Source: https://www.neowin.net/forum/topic/853774-java-regex-to-validate-url/
    private static final Pattern urlPattern  = Pattern.compile(
            "((?:(http|https|Http|Https|rtsp|Rtsp):" +
                    "\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)" +
                    "\\,\\;\\?\\&amp;\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_" +
                    "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&amp;\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?" +
                    "((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+" +   // named host
                    "(?:" +   // plus top level domain
                    "(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])" +
                    "|(?:biz|b[abdefghijmnorstvwyz])" +
                    "|(?:cat|com|coop|c[acdfghiklmnoruvxyz])" +
                    "|d[ejkmoz]" +
                    "|(?:edu|e[cegrstu])" +
                    "|f[ijkmor]" +
                    "|(?:gov|g[abdefghilmnpqrstuwy])" +
                    "|h[kmnrtu]" +
                    "|(?:info|int|i[delmnoqrst])" +
                    "|(?:jobs|j[emop])" +
                    "|k[eghimnrwyz]" +
                    "|l[abcikrstuvy]" +
                    "|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])" +
                    "|(?:name|net|n[acefgilopruz])" +
                    "|(?:org|om)" +
                    "|(?:pro|p[aefghklmnrstwy])" +
                    "|qa" +
                    "|r[eouw]" +
                    "|s[abcdeghijklmnortuvyz]" +
                    "|(?:tel|travel|t[cdfghjklmnoprtvwz])" +
                    "|u[agkmsyz]" +
                    "|v[aceginu]" +
                    "|w[fs]" +
                    "|y[etu]" +
                    "|z[amw]))" +
                    "|(?:(?:25[0-5]|2[0-4]" + // or ip address
                    "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]" +
                    "|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]" +
                    "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}" +
                    "|[1-9][0-9]|[0-9])))" +
                    "(?:\\:\\d{1,5})?)" + // plus option port number
                    "(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&amp;\\=\\#\\~" +  // plus option query params
                    "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?" +
                    "(?:\\b|$)"
    );


    public static boolean validate(String url){
        if(url == null || url.isEmpty()){
            return false;
        }
        Matcher matcher = ipPattern.matcher(url);
        Boolean isIP = matcher.matches();

        matcher = urlPattern.matcher(url);
        Boolean isURL = matcher.matches();
        return isIP || isURL;
    }

}
