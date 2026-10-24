package com.quizapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html><html><head><title>Admin - Mainframe</title>");
        out.println("<style>");
        out.println("body { background-color: #0b0c10; color: #c5c6c7; font-family: 'Courier New', Courier, monospace; padding: 40px; }");
        out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; border: 1px solid #45a29e; }");
        out.println("th, td { padding: 15px; border-bottom: 1px solid #1f2833; text-align: left; }");
        out.println("th { background-color: #1f2833; color: #66fcf1; text-transform: uppercase; border-bottom: 2px solid #45a29e; }");
        out.println("tr:hover { background-color: rgba(69, 162, 158, 0.1); }");
        out.println("a { padding: 10px 20px; background: transparent; border: 1px solid #ff003c; color: #ff003c; text-decoration: none; display: inline-block; transition: 0.3s; }");
        out.println("a:hover { background: #ff003c; color: #0b0c10; box-shadow: 0 0 10px #ff003c; }");
        out.println("</style></head><body>");
        
        out.println("<h2 style='color: #66fcf1; border-bottom: 1px dashed #45a29e; padding-bottom: 10px;'>// ADMIN OVERVIEW //</h2>");
        out.println("<table>");
        out.println("<tr><th>Attempt ID</th><th>Subject ID</th><th>Raw Score</th><th>Accuracy %</th></tr>");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // REPLACE "password" with your MySQL password
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_db", "root", "54742"); 
            
            String sql = "SELECT * FROM quiz_results ORDER BY score DESC, id ASC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td style='color:#fff;'>" + rs.getString("username") + "</td>");
                out.println("<td>" + rs.getInt("score") + "/10</td>");
                out.println("<td>" + rs.getDouble("percentage") + "%</td>");
                out.println("</tr>");
            }
            conn.close();
        } catch (Exception e) {
            out.println("<tr><td colspan='4' style='color: red;'>SYS_ERROR: " + e.getMessage() + "</td></tr>");
        }
        
        out.println("</table>");
        out.println("<br><br><a href='login.html'>[ TERMINATE SESSION ]</a>");
        out.println("</body></html>");
    }
}
