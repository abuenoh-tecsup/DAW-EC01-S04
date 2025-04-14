package tecsup.edu.pe.dawec01s04;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/solicitudes")
public class SolicitudesServlet extends HttpServlet {
    private final String URL = "jdbc:mysql://localhost:3306/mesa_ayuda";
    private final String USER = "root";
    private final String PASS = "";


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("listar".equals(action)) {
            listar(req, resp);
        } else if ("editar".equals(action)) {
            editar(req, resp);
        } else if ("eliminar".equals(action)) {
            eliminar(req, resp);
        } else {
            resp.sendRedirect("base.html");
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html; charset=UTF-8");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM solicitudes");

            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("    <meta charset='UTF-8'>");
            out.println("    <meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("    <title>Lista de Solicitudes</title>");
            out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("    <link rel='stylesheet' href='css/styles.css'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");
            imprimirNavbar(out);
            out.println("<div class='container mt-5'>");
            out.println("    <h2 class='mb-4 text-center'>Lista de Solicitudes</h2>");
            out.println("    <div class='table-responsive'>");
            out.println("        <table class='table table-striped table-hover table-bordered'>");
            out.println("            <thead class='table-dark'>");
            out.println("                <tr>");
            out.println("                    <th>ID</th>");
            out.println("                    <th>Título</th>");
            out.println("                    <th>Descripción</th>");
            out.println("                    <th>Estado</th>");
            out.println("                    <th>Prioridad</th>");
            out.println("                    <th>Fecha de Creación</th>");
            out.println("                    <th>Acciones</th>");
            out.println("                </tr>");
            out.println("            </thead>");
            out.println("            <tbody>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                String estado = rs.getString("estado");
                String prioridad = rs.getString("prioridad");
                String fechaCreacion = rs.getString("fecha_creacion");

                out.println("                <tr>");
                out.println("                    <td>" + id + "</td>");
                out.println("                    <td>" + titulo + "</td>");
                out.println("                    <td>" + descripcion + "</td>");
                out.println("                    <td>" + estado + "</td>");
                out.println("                    <td>" + prioridad + "</td>");
                out.println("                    <td>" + fechaCreacion + "</td>");
                out.println("                    <td>");
                out.println("                        <div class='btn-group' role='group'>");
                out.println("                            <a href='asistencia?action=editar&id=" + id + "' class='btn btn-warning btn-sm'>Editar</a>");
                out.println("                            <a href='asistencia?action=eliminar&id=" + id + "' class='btn btn-danger btn-sm'>Eliminar</a>");
                out.println("                        </div>");
                out.println("                    </td>");
                out.println("                </tr>");
            }

            out.println("            </tbody>");
            out.println("        </table>");
            out.println("    </div>");
            out.println("    <div class='mt-3'>");
            out.println("        <a href='base.html' class='btn btn-primary'>Registrar nueva solicitud</a>");
            out.println("    </div>");
            out.println("</div>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html; charset=UTF-8");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM solicitudes WHERE id = ?");
            stmt.setString(1, id);
            int resultado = stmt.executeUpdate();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("    <meta charset='UTF-8'>");
            out.println("    <meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("    <title>Eliminar Solicitud</title>");
            out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("    <link rel='stylesheet' href='css/styles.css'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");
            imprimirNavbar(out);
            out.println("<div class='container mt-5'>");
            out.println("    <div class='row justify-content-center'>");
            out.println("        <div class='col-md-6'>");
            out.println("            <div class='card shadow'>");
            out.println("                <div class='card-header bg-" + (resultado > 0 ? "success" : "danger") + " text-white'>");
            out.println("                    <h2 class='mb-0'>Resultado</h2>");
            out.println("                </div>");
            out.println("                <div class='card-body'>");
            if (resultado > 0) {
                out.println("                <div class='alert alert-success'>Solicitud eliminada correctamente.</div>");
            } else {
                out.println("                <div class='alert alert-danger'>Error al eliminar la solicitud.</div>");
            }
            out.println("                </div>");
            out.println("                <div class='card-footer'>");
            out.println("                    <a href='asistencia?action=listar' class='btn btn-primary'>Volver a la lista</a>");
            out.println("                </div>");
            out.println("            </div>");
            out.println("        </div>");
            out.println("    </div>");
            out.println("</div>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("    <meta charset='UTF-8'>");
            out.println("    <meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("    <title>Error</title>");
            out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");
            out.println("<div class='container mt-5'>");
            out.println("    <div class='alert alert-danger'>Error al eliminar solicitud: " + e.getMessage() + "</div>");
            out.println("    <a href='asistencia?action=listar' class='btn btn-secondary'>Volver a la lista</a>");
            out.println("</div>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html; charset=UTF-8");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM solicitudes WHERE id = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                out.println("<!DOCTYPE html>");
                out.println("<html lang='es'>");
                out.println("<head>");
                out.println("    <meta charset='UTF-8'>");
                out.println("    <meta name='viewport' content='width=device-width, initial-scale=1'>");
                out.println("    <title>Editar Solicitud</title>");
                out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
                out.println("    <link rel='stylesheet' href='css/styles.css'>");
                out.println("</head>");
                out.println("<body class='bg-light'>");
                imprimirNavbar(out);
                out.println("<div class='container mt-5'>");
                out.println("    <div class='row justify-content-center'>");
                out.println("        <div class='col-md-6'>");
                out.println("            <div class='card shadow'>");
                out.println("                <div class='card-header bg-warning text-dark'>");
                out.println("                    <h2 class='mb-0'>Editar Solicitud</h2>");
                out.println("                </div>");
                out.println("                <div class='card-body'>");
                out.println("                    <form action='solicitudes' method='post'>");
                out.println("                        <input type='hidden' name='accion' value='actualizar'>");
                out.println("                        <input type='hidden' name='id' value='" + rs.getString("id") + "'>");

                out.println("                        <div class='mb-3'>");
                out.println("                            <label for='titulo' class='form-label'>Título:</label>");
                out.println("                            <input type='text' class='form-control' id='titulo' name='titulo' value='" + rs.getString("titulo") + "' required>");
                out.println("                        </div>");

                out.println("                        <div class='mb-3'>");
                out.println("                            <label for='descripcion' class='form-label'>Descripción:</label>");
                out.println("                            <textarea class='form-control' id='descripcion' name='descripcion' rows='4'>" + rs.getString("descripcion") + "</textarea>");
                out.println("                        </div>");

                out.println("                        <div class='mb-3'>");
                out.println("                            <label for='estado' class='form-label'>Estado:</label>");
                out.println("                            <input type='text' class='form-control' id='estado' name='estado' value='" + rs.getString("estado") + "' required>");
                out.println("                        </div>");

                out.println("                        <div class='mb-3'>");
                out.println("                            <label for='prioridad' class='form-label'>Prioridad:</label>");
                out.println("                            <input type='text' class='form-control' id='prioridad' name='prioridad' value='" + rs.getString("prioridad") + "' required>");
                out.println("                        </div>");

                out.println("                        <div class='mb-3'>");
                out.println("                            <label for='fecha_creacion' class='form-label'>Fecha de Creación:</label>");
                out.println("                            <input type='date' class='form-control' id='fecha_creacion' name='fecha_creacion' value='" + rs.getString("fecha_creacion") + "' required>");
                out.println("                        </div>");

                out.println("                        <div class='d-grid gap-2'>");
                out.println("                            <button type='submit' class='btn btn-warning'>Actualizar</button>");
                out.println("                        </div>");
                out.println("                    </form>");
                out.println("                </div>");
                out.println("                <div class='card-footer'>");
                out.println("                    <a href='solicitudes?action=listar' class='btn btn-secondary'>Volver a la lista</a>");
                out.println("                </div>");
                out.println("            </div>");
                out.println("        </div>");
                out.println("    </div>");
                out.println("</div>");
                out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'></script>");
                out.println("</body>");
                out.println("</html>");
            }
        } catch (Exception e) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("    <meta charset='UTF-8'>");
            out.println("    <meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("    <title>Error</title>");
            out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");
            out.println("<div class='container mt-5'>");
            out.println("    <div class='alert alert-danger'>Error al cargar solicitud: " + e.getMessage() + "</div>");
            out.println("    <a href='solicitudes?action=listar' class='btn btn-secondary'>Volver a la lista</a>");
            out.println("</div>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        String id_solicitud = req.getParameter("id_solicitud");
        String titulo = req.getParameter("titulo");
        String descripcion = req.getParameter("descripcion");
        String estado = req.getParameter("estado");
        String prioridad = req.getParameter("prioridad");
        String fecha_creacion = req.getParameter("fecha_creacion");

        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html; charset=UTF-8");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            boolean exitoso = false;
            if ("actualizar".equals(accion)) {
                PreparedStatement stmt = conn.prepareStatement("UPDATE solicitudes SET titulo=?, descripcion=?, estado=?, prioridad=?, fecha_creacion=? WHERE id_solicitud=?");
                stmt.setString(1, titulo);
                stmt.setString(2, descripcion);
                stmt.setString(3, estado);
                stmt.setString(4, prioridad);
                stmt.setString(5, fecha_creacion);
                stmt.setString(6, id_solicitud);
                int resultado = stmt.executeUpdate();
                exitoso = resultado > 0;
            } else {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO solicitudes (titulo, descripcion, estado, prioridad, fecha_creacion) VALUES(?,?,?,?,?)");
                stmt.setString(1, titulo);
                stmt.setString(2, descripcion);
                stmt.setString(3, estado);
                stmt.setString(4, prioridad);
                stmt.setString(5, fecha_creacion);
                int resultado = stmt.executeUpdate();
                exitoso = resultado > 0;
            }

            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("    <meta charset='UTF-8'>");
            out.println("    <meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("    <title>Confirmación</title>");
            out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("    <link rel='stylesheet' href='css/styles.css'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");
            imprimirNavbar(out);
            out.println("<div class='container mt-5'>");
            out.println("    <div class='row justify-content-center'>");
            out.println("        <div class='col-md-6'>");
            out.println("            <div class='card shadow'>");
            out.println("                <div class='card-header bg-success text-white'>");
            out.println("                    <h2 class='mb-0'>Operación Exitosa</h2>");
            out.println("                </div>");
            out.println("                <div class='card-body'>");
            out.println("                    <div class='alert alert-success'>");
            if ("actualizar".equals(accion)) {
                out.println("                        Solicitud actualizada correctamente.");
            } else {
                out.println("                        Solicitud agregada correctamente.");
            }
            out.println("                    </div>");
            out.println("                </div>");
            out.println("                <div class='card-footer'>");
            out.println("                    <a href='solicitudes?action=listar' class='btn btn-primary'>Ver lista de solicitudes</a>");
            out.println("                </div>");
            out.println("            </div>");
            out.println("        </div>");
            out.println("    </div>");
            out.println("</div>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("    <meta charset='UTF-8'>");
            out.println("    <meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("    <title>Error</title>");
            out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");
            out.println("<div class='container mt-5'>");
            out.println("    <div class='alert alert-danger'>Error al procesar la operación: " + e.getMessage() + "</div>");
            out.println("    <a href='solicitudes.html' class='btn btn-secondary'>Volver al inicio</a>");
            out.println("</div>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void imprimirNavbar(PrintWriter out) {
        out.println("<nav class='navbar navbar-expand-lg navbar-dark' style='background-color: var(--primary-color);'>");
        out.println("    <div class='container'>");
        out.println("        <a class='navbar-brand' href='#'>");
        out.println("            <i class='fas fa-graduation-cap me-2'></i>");
        out.println("            SISTEMA DE SOLICITUDES");
        out.println("        </a>");
        out.println("        <button class='navbar-toggler' type='button' data-bs-toggle='collapse' data-bs-target='#navbarMain'>");
        out.println("            <span class='navbar-toggler-icon'></span>");
        out.println("        </button>");
        out.println("        <div class='collapse navbar-collapse' id='navbarMain'>");
        out.println("            <ul class='navbar-nav ms-auto'>");
        out.println("                <li class='nav-item'>");
        out.println("    <a class='nav-link active' href='solicitudes?action=listar'>");
        out.println("                        <i class='fas fa-clipboard-check me-1'></i> Solicitudes");
        out.println("                    </a>");
        out.println("                </li>");
        out.println("            </ul>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</nav>");
    }
}
