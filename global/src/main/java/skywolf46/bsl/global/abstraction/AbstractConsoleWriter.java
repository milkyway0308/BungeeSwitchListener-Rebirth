package skywolf46.bsl.global.abstraction;

public abstract class AbstractConsoleWriter {
    public abstract void printText(String x);

    public abstract void printError(String x);

    public abstract AbstractConsoleWriter of(String prefix);
}
