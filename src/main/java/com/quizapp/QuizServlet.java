package com.quizapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/QuizServlet")
public class QuizServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        String userName = (String) session.getAttribute("username");
        
        // Full 10 Answers
        String[] correctAnswers = { "Sun Microsystems", "32 bit", "main()", "Bytecode", "extends", "false", "ArithmeticException", "HyperText Transfer Protocol", "doPost()", ".class" };
        int score = 0;
        int totalQuestions = correctAnswers.length;
        
        for (int i = 0; i < totalQuestions; i++) {
            String userAnswer = request.getParameter("q" + (i + 1));
            if (userAnswer != null && userAnswer.equals(correctAnswers[i])) score++;
        }
        double percentage = ((double) score / totalQuestions) * 100;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // REPLACE "password" with your MySQL password
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_db", "root", "54742"); 
            
            String sql = "INSERT INTO quiz_results (username, score, percentage) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            pstmt.setLong(2, score);
            pstmt.setDouble(3, percentage);
            pstmt.executeUpdate();
            
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String message, color;
        if (percentage >= 80) { message = "EVALUATION: OUTSTANDING"; color = "#39ff14"; } 
        else if (percentage >= 50) { message = "EVALUATION: ACCEPTABLE"; color = "#f39c12"; } 
        else { message = "EVALUATION: SUB-OPTIMAL"; color = "#ff003c"; }
        
        out.println("<!DOCTYPE html><html><head><title>Results</title>");
        out.println("<style>");
        out.println("body { background-color: #0b0c10; color: #c5c6c7; font-family: 'Courier New', Courier, monospace; display: flex; justify-content: center; align-items: center; min-height: 100vh; margin: 0; }");
        out.println(".card { background: #1f2833; padding: 40px; border: 1px solid #66fcf1; border-radius: 8px; box-shadow: 0 0 30px rgba(102, 252, 241, 0.2); text-align: center; max-width: 500px; width: 100%; }");
        out.println("h2 { color: #66fcf1; border-bottom: 1px solid #45a29e; padding-bottom: 10px; text-transform: uppercase; }");
        out.println(".score { font-size: 3em; font-weight: bold; color: " + color + "; margin: 20px 0; text-shadow: 0 0 10px " + color + "; }");
        out.println("a { display: inline-block; padding: 12px 25px; background: transparent; border: 2px solid #66fcf1; color: #66fcf1; text-decoration: none; text-transform: uppercase; font-weight: bold; transition: 0.3s; margin-top: 20px; }");
        out.println("a:hover { background: #66fcf1; color: #0b0c10; box-shadow: 0 0 15px #66fcf1; }");
        out.println("</style></head><body>");
        
        out.println("<div class='card'>");
        out.println("<h2>// TASK COMPLETE //</h2>");
        out.println("<p>SUBJECT: " + userName + "</p>");
        out.println("<div class='score'>" + score + " / " + totalQuestions + "</div>");
        out.println("<p style='color:" + color + ";'>" + message + "</p>");
        out.println("<p style='color: #45a29e; font-size: 0.8em;'>[ DATA LOGGED TO MAINFRAME ]</p>");
        out.println("<a href='login.html'>[ INITIATE LOGOUT ]</a>");
        out.println("</div>");
        
        out.println("</body></html>");
    }
}
