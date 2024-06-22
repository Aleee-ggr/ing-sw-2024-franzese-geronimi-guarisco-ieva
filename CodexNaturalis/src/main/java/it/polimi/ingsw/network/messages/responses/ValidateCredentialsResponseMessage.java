package it.polimi.ingsw.network.messages.responses;

/**
 * This class represents a response message indicating the validity of provided credentials.
 * It extends the GenericResponseMessage class.
 */
public class ValidateCredentialsResponseMessage extends GenericResponseMessage{
    private final boolean isValid;
    private final String username;
    private final String password;

    /**
     * Constructs a new ValidateCredentialsResponseMessage with the specified username, password, and validity status.
     *
     * @param username the username associated with the credentials validation.
     * @param password the password associated with the credentials validation.
     * @param isValid  true if the provided credentials are valid; false otherwise.
     */
    public ValidateCredentialsResponseMessage(String username, String password, boolean isValid) {
        this.isValid = isValid;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns whether the provided credentials are valid.
     *
     * @return true if the provided credentials are valid; false otherwise.
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Retrieves the username associated with the credentials validation.
     *
     * @return the username associated with the credentials validation.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the password associated with the credentials validation.
     * Note: For security reasons, sensitive information like passwords
     * should not typically be transmitted in plain text in responses.
     *
     * @return the password associated with the credentials validation.
     */
    public String getPassword() {
        return password;
    }
}