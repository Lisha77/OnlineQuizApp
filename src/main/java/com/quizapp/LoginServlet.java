package com.quizapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        String hashedInputPassword = hashPassword(password);
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String style = "<style>body { background-color: #0b0c10; color: #ff003c; font-family: 'Courier New', Courier; text-align: center; margin-top: 100px; } a { color: #66fcf1; }</style>";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // REPLACE "password" with your MySQL password
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_db", "root", "54742");
            
            String sql = "SELECT role FROM users WHERE username = ? AND password_hash = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, hashedInputPassword);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String role = rs.getString("role");
                
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("role", role);
                
                if ("admin".equals(role)) {
                    response.sendRedirect("AdminDashboardServlet");
                } else {
                    response.sendRedirect("quiz.html"); 
                }
            } else {
                out.println("<html><head>" + style + "</head><body>");
                out.println("<h2>[ ACCESS DENIED - INVALID CREDENTIALS ]</h2>");
                out.println("<br><br><a href='login.html'>[ RETURN ]</a>");
                out.println("</body></html>");
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (Exception e) {
            out.println("<html><head>" + style + "</head><body>");
            out.println("<h2>DATABASE CONNECTION ERROR</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<br><br><a href='login.html'>[ RETURN ]</a>");
            out.println("</body></html>");
        }
    }
}
