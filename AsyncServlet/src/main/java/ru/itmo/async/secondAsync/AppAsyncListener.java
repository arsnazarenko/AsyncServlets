package ru.itmo.async.secondAsync;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class AppAsyncListener implements AsyncListener {
    private static final Logger logger = Logger.getLogger(AppAsyncListener.class.getName());
    @Override
    public void onComplete(AsyncEvent asyncEvent) throws IOException {
        logger.info("AsyncListener on complete");
    }

    @Override
    public void onTimeout(AsyncEvent asyncEvent) throws IOException {
        logger.info("AsyncListener on timeout");
        ServletResponse response = asyncEvent.getAsyncContext().getResponse();
        PrintWriter writer = response.getWriter();
        writer.write("Timeout error in processing");

    }

    @Override
    public void onError(AsyncEvent asyncEvent) throws IOException {
        logger.info("AsyncListener on error");

    }

    @Override
    public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
        logger.info("AsyncListener on start async");

    }
}
