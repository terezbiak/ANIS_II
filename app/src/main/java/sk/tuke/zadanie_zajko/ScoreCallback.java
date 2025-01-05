package sk.tuke.zadanie_zajko;

public interface ScoreCallback {
    void onScoreReceived(Long score);
    void onError(String message);
}

