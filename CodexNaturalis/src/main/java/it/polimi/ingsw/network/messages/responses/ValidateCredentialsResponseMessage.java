package it.polimi.ingsw.network.messages.responses;

public class ValidateCredentialsResponseMessage extends GenericResponseMessage{
    private final boolean isValid;

    public ValidateCredentialsResponseMessage(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }
}