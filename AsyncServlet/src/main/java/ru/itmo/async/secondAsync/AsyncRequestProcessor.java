package ru.itmo.async.secondAsync;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsyncRequestProcessor implements Runnable {
    private static final int actionsLimit = 10;
    private static final int ms = 500;

    private AsyncContext asyncContext;

    public AsyncRequestProcessor(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    @Override
    public void run() {
        try {
            ServletResponse response = asyncContext.getResponse();
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            writer.write("Processing begin!<br /> ");
            for (int action = 0; action < actionsLimit; action++) {
                longProcessing(ms);
                writer.printf("Action #%d completed by %s.<br />", action, Thread.currentThread().getName());
                writer.flush();
            }
            writer.write("Processing complete");
            writer.flush();

        } catch (IOException e) {
            Logger.getLogger(AsyncRequestProcessor.class.getName()).log(Level.INFO, null, e);
        } finally {
            asyncContext.complete();
        }
    }

    private void longProcessing(int sec) {
        try {
            Thread.sleep(sec);
        } catch (InterruptedException e) {
            Logger.getLogger(AsyncRequestProcessor.class.getName()).log(Level.INFO, null, e);
        }
    }

}
