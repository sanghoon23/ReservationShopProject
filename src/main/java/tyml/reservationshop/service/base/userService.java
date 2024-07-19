package tyml.reservationshop.service.base;


public interface userService {

    public String getAccessToken(String code, String state);
    public void logout(String accessToken);

}
