package monkey.event;

/**
 * event source interface
 * 
 * This interface represents the source of event.
 * 
 * This class is adapted from the class MonkeyEventSource of the Android Open Source Project.
 * 
 * @author yifei
 */
public interface EventSource {
    /**
     * @return the next monkey event from the source
     */
    public Event getNextEvent();

    /**
     * set verbose to allow different level of log
     *
     * @param verbose output mode? 1= verbose, 2=very verbose
     */
//     public void setVerbose(int verbose);

    /**
     * check whether precondition is satisfied
     *
     * @return false if something fails, e.g. factor failure in random source or
     *         file can not open from script source etc
     */
    public boolean validate();
}
