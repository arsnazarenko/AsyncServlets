package ru.itmo.async.secondAsync;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(urlPatterns = "/async-long", asyncSupported = true)
public class AsyncLongRunningServlet extends HttpServlet {
    private final static int asyncTimeout = 10000;
    private static final Logger logger = Logger.getLogger(AsyncLongRunningServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long startTime = System.currentTimeMillis();
        logger.log(Level.INFO, "Start {0}.", Thread.currentThread().getName());
        AsyncContext asyncContext = req.startAsync();
        asyncContext.addListener(new AppAsyncListener());
        asyncContext.setTimeout(asyncTimeout);
        ExecutorService executor = (ExecutorService) req.getServletContext().getAttribute("executor");

        executor.submit(new AsyncRequestProcessor(asyncContext));
        Long endTime = System.currentTimeMillis();
        logger.log(Level.INFO, "End {0}. Elapsed {1} ms.", new Object[]{Thread.currentThread().getName(), (endTime - startTime)});
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long startTime = System.currentTimeMillis();
        logger.log(Level.INFO, "Start {0}.", Thread.currentThread().getName());
        AsyncContext asyncContext = req.startAsync();
        asyncContext.addListener(new AppAsyncListener());
        asyncContext.setTimeout(asyncTimeout);
        ExecutorService executor = (ExecutorService) req.getServletContext().getAttribute("executor");

        executor.submit(new AsyncRequestProcessor(asyncContext));
        Long endTime = System.currentTimeMillis();
        logger.log(Level.INFO, "End {0}. Elapsed {1} ms.", new Object[]{Thread.currentThread().getName(), endTime - startTime});
    }
}
