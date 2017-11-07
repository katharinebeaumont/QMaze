package qmaze.Controller;

/**
 *
 * @author katharine
 */
public class EpisodeInterruptedException extends Exception {
    
    public EpisodeInterruptedException(Exception e, int step) {
        super("Episode interrupted at step " + step + " due to " + e.getMessage());
    }
    
    public EpisodeInterruptedException(String message, int step) {
        super("Episode interrupted at step " + step + " due to " + message);
    }
}
