package tecsup.edu.pe.dawec01s04;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/notas")
public class NotasServlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://localhost:3306/ec01";
    private static final String USER = "root";
    private static final String PASS = "";

    // Método doGet para manejar la solicitud GET
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("listar".equals(action)) {
            listar(req, resp);
        } else if ("editar".equals(action)) {
            editar(req, resp);
        } else if ("eliminar".equals(action)) {
            eliminar(req, resp);
        } else {
            resp.sendRedirect("notas.html");
        }
    }

    // Método doPost para manejar la solicitud POST (cuando el formulario se envía)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");


        if ("actualizar".equals(accion)) {
            String id = req.getParameter("id_nota");
            String estudiante = req.getParameter("estudiante");
            String curso = req.getParameter("curso");
            double nota = Double.parseDouble(req.getParameter("nota"));
            String fecha = req.getParameter("fecha");
            String observacion = req.getParameter("observacion");

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(URL, USER, PASS);

                // Usamos un UPDATE en lugar de un INSERT
                String query = "UPDATE notas SET estudiante = ?, curso = ?, nota = ?, fecha = ?, observacion = ? WHERE id_nota = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, estudiante);
                stmt.setString(2, curso);
                stmt.setDouble(3, nota);
                stmt.setString(4, fecha);
                stmt.setString(5, observacion);
                stmt.setString(6, id);  // ID de la nota a actualizar

                int result = stmt.executeUpdate();
                if (result > 0) {
                    resp.sendRedirect("notas?action=listar");  // Redirigir a la lista de notas
                } else {
                    resp.getWriter().write("Error al actualizar la nota.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                resp.getWriter().write("Error al procesar la solicitud: " + e.getMessage());
            }
        }
    }

    // Método para listar las notas
    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html; charset=UTF-8");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM notas");

            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("<title>Lista de Notas</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");
            imprimirNavbar(out);
            out.println("<div class='container mt-5'>");
            out.println("<h2 class='mb-4 text-center'>Lista de Notas</h2>");
            out.println("<div class='table-responsive'>");
            out.println("<table class='table table-striped table-hover table-bordered'>");
            out.println("<thead class='table-dark'>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Estudiante</th>");
            out.println("<th>Curso</th>");
            out.println("<th>Nota</th>");
            out.println("<th>Fecha</th>");
            out.println("<th>Acciones</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");

            while (rs.next()) {
                int id = rs.getInt("id_nota");
                String estudiante = rs.getString("estudiante");
                String curso = rs.getString("curso");
                double nota = rs.getDouble("nota");
                String fecha = rs.getString("fecha");
                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + estudiante + "</td>");
                out.println("<td>" + curso + "</td>");
                out.println("<td>" + nota + "</td>");
                out.println("<td>" + fecha + "</td>");
                out.println("<td>");
                out.println("<a href='notas?action=editar&id_nota=" + id + "' class='btn btn-warning btn-sm'>Editar</a>");
                out.println("<a href='notas?action=eliminar&id_nota=" + id + "' class='btn btn-danger btn-sm'>Eliminar</a>");
                out.println("</td>");
                out.println("</tr>");
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
            out.println("<div class='mt-3'>");
            out.println("<a href='notas.html' class='btn btn-primary'>Registrar nueva nota</a>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Método para editar una nota
    private void editar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id_nota");
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html; charset=UTF-8");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM notas WHERE id_nota = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                out.println("<!DOCTYPE html>");
                out.println("<html lang='es'>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<title>Editar Nota</title>");
                out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
                out.println("</head>");
                out.println("<body class='bg-light'>");
                imprimirNavbar(out);
                out.println("<div class='container mt-5'>");
                out.println("<h2 class='mb-4'>Editar Nota</h2>");
                out.println("<form action='notas' method='post'>");
                out.println("<input type='hidden' name='accion' value='actualizar'>");
                out.println("<input type='hidden' name='id_nota' value='" + rs.getString("id_nota") + "'>");
                out.println("<div class='mb-3'>");
                out.println("<label for='estudiante' class='form-label'>Estudiante:</label>");
                out.println("<input type='text' class='form-control' id='estudiante' name='estudiante' value='" + rs.getString("estudiante") + "' required>");
                out.println("</div>");
                out.println("<div class='mb-3'>");
                out.println("<label for='curso' class='form-label'>Curso:</label>");
                out.println("<input type='text' class='form-control' id='curso' name='curso' value='" + rs.getString("curso") + "' required>");
                out.println("</div>");
                out.println("<div class='mb-3'>");
                out.println("<label for='nota' class='form-label'>Nota:</label>");
                out.println("<input type='number' class='form-control' id='nota' name='nota' value='" + rs.getDouble("nota") + "' required>");
                out.println("</div>");
                out.println("<div class='mb-3'>");
                out.println("<label for='fecha' class='form-label'>Fecha:</label>");
                out.println("<input type='date' class='form-control' id='fecha' name='fecha' value='" + rs.getString("fecha") + "' required>");
                out.println("</div>");
                out.println("<div class='mb-3'>");
                out.println("<label for='observacion' class='form-label'>Observación:</label>");
                out.println("<input type='text' class='form-control' id='observacion' name='observacion' value='" + rs.getString("observacion") + "'>");
                out.println("</div>");
                out.println("<button type='submit' class='btn btn-warning'>Actualizar</button>");
                out.println("</form>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            }
        } catch (Exception e) {
            out.println("Error al cargar la nota: " + e.getMessage());
        }
    }

    // Método para eliminar una nota
    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id_nota");
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html; charset=UTF-8");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM notas WHERE id_nota = ?");
            stmt.setString(1, id);
            int result = stmt.executeUpdate();

            if (result > 0) {
                out.println("Nota eliminada exitosamente.");
            } else {
                out.println("Error al eliminar la nota.");
            }
        } catch (Exception e) {
            out.println("Error al eliminar la nota: " + e.getMessage());
        }
    }

    private void imprimirNavbar(PrintWriter out) {
        out.println("<nav class='navbar navbar-expand-lg navbar-dark' style='background-color: #007bff;'>");
        out.println("<a class='navbar-brand' href='#'>Sistema Académico</a>");
        out.println("<ul class='navbar-nav ms-auto'>");
        out.println("<li class='nav-item'><a class='nav-link' href='notas.html'>Registrar Notas</a></li>");
        out.println("<li class='nav-item'><a class='nav-link' href='notas?action=listar'>Ver Notas</a></li>");
        out.println("</ul>");
        out.println("</nav>");
    }
}
