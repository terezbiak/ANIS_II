package sk.tuke.zadanie_zajko;

public interface GlobalScoreCallback {
    void onGlobalScoreReceived(Long globalHighestScore);
    void onError(String message);
}
