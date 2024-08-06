package tyml.reservationshop.service.util;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtils {

    public static String getBaseUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        try {
            URI uri = new URI(url);
            return uri.getScheme() + "://" + uri.getHost() + (uri.getPort() != -1 ? ":" + uri.getPort() : "");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "";
        }
    }

//    public static String getBaseUrl(HttpServletRequest request) {
//        String url = request.getRequestURL().toString();
//        int index = url.indexOf("/", url.indexOf("://") + 3); // 프로토콜 뒤의 첫 번째 슬래시 인덱스
//        return index == -1 ? url : url.substring(0, index);
//    }

}
