import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class QuizServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h2>Question 1: What is Java?</h2>");

        out.println("<form method='post' action='quiz'>");
        out.println("<input type='radio' name='q1' value='a'> Programming Language<br>");
        out.println("<input type='radio' name='q1' value='b'> Coffee<br>");
        out.println("<input type='submit' value='Submit'>");
        out.println("</form>");

        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ans = request.getParameter("q1");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");

        if ("a".equals(ans)) {
            out.println("<h2>Correct Answer!</h2>");
        } else {
            out.println("<h2>Wrong Answer!</h2>");
        }

        out.println("</body></html>");
    }
}
