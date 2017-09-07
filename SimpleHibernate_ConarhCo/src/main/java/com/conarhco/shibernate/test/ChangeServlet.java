/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.shibernate.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Денис
 */
public class ChangeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        int id=Integer.parseInt(req.getParameter("id"));
        String review=req.getParameter("review");
        Collection<ReviewBean> beans=(Collection<ReviewBean>) getServletContext().getAttribute("elements");
        for(ReviewBean item : beans){
            if(item.getId()==id){
                item.setReview(review);
                break;
            }
        }
        resp.sendRedirect("index.jsp");
    }

}