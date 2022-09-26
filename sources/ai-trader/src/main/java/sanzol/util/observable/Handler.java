package sanzol.util.observable;

@FunctionalInterface
public interface Handler<T>
{
	void handle(T t);
}

