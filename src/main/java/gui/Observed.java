package gui;

public interface Observed<T> {
    void addObserver(Observer<T> observer);
}
