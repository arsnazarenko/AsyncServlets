package ru.itmo.async.nonAsync;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/non-asynchronous")
public class NonAsyncServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = JsonService.getProcess(req, resp);
        PrintWriter writer = resp.getWriter();
        writer.write(s);
        writer.flush();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = JsonService.postProcess(req, resp);
        PrintWriter writer = resp.getWriter();
        writer.write(s);
        writer.flush();
    }
}
