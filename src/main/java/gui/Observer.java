package gui;

public interface Observer<T> {
    void notify(T message);
}
