package com.quizapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
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
            throw new RuntimeException("Error hashing password", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        String hashed = hashPassword(password);
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String style = "<style>body { background-color: #0b0c10; color: #66fcf1; font-family: 'Segoe UI', Tahoma; text-align: center; margin-top: 100px; } a { color: #45a29e; }</style>";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // REPLACE "password" with your MySQL password
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_db", "root", "54742");
            
            String sql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, 'student')";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, hashed);
            
            int rows = pstmt.executeUpdate();
            
            out.println("<html><head>" + style + "</head><body>");
            if(rows > 0) {
                out.println("<h2>IDENTITY REGISTRATION SUCCESSFUL</h2>");
                out.println("<br><br><a href='login.html'>[ PROCEED TO LOGIN ]</a>");
            }
            
            pstmt.close();
            conn.close();
            
        } catch (Exception e) {
            out.println("<html><head>" + style + "</head><body>");
            out.println("<h2>REGISTRATION FAILED: User may already exist.</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<br><br><a href='register.html'>[ TRY AGAIN ]</a>");
        }
        out.println("</body></html>");
    }
}
