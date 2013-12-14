package gui.observer;

public interface Observable {

	abstract void registerObserver(Observer observer);
	abstract void deregisterObserver(Observer observer);
	abstract void notifyObservers(Observable observable);
}
