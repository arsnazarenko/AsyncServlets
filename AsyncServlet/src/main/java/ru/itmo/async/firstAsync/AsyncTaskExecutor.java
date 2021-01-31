package ru.itmo.async.firstAsync;

import ru.itmo.async.nonAsync.JsonService;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Logger;

@WebListener
public class AsyncTaskExecutor implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(AsyncTaskExecutor.class.getName());
    private static final BlockingQueue<AsyncContext> QUEUE = new LinkedBlockingDeque<>();

    private volatile Thread thread;

    public static void add(AsyncContext context) {
        QUEUE.add(context);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        thread = new Thread(() -> {
            while (true) {
                AsyncContext context;
                while ((context = QUEUE.poll()) != null) {
                    try {
                        ServletResponse response = context.getResponse();
                           response.setContentType("text/plain");
                        String method = ((HttpServletRequest) context.getRequest()).getMethod();
                        String resp;
                        if (method.equals("POST")) {
                            resp = JsonService.postProcess((HttpServletRequest) context.getRequest(), (HttpServletResponse) context.getResponse());
                        } else if (method.equals("GET")) {
                            resp = JsonService.getProcess((HttpServletRequest) context.getRequest(), (HttpServletResponse) context.getResponse());
                        } else {
                            resp = "method not supported!";
                        }
                        PrintWriter writer = response.getWriter();
                        writer.write(resp);
                        writer.flush();
                    } catch (Exception e) {
                        logger.severe(e.getMessage());
                    } finally {
                        context.complete();
                    }
                }

            }
        });
        thread.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        thread.interrupt();
    }
}
