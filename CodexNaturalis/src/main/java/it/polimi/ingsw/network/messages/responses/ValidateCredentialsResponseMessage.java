package it.polimi.ingsw.network.messages.responses;

public class ValidateCredentialsResponseMessage extends GenericResponseMessage{
    private final boolean isValid;
    private final String username;
    private final String password;

    public ValidateCredentialsResponseMessage(String username, String password, boolean isValid) {
        this.isValid = isValid;
        this.username = username;
        this.password = password;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}