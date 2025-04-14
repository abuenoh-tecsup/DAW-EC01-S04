package tecsup.edu.pe.dawec01s04;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.*;

@WebServlet("/asistencia")
public class AsistenciaServlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://localhost:3306/ec01"; // Cambié la base de datos
    private static final String USER = "root";
    private static final String PASS = "";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("listar".equals(action)) {
            listar(req, resp);
        } else if ("editar".equals(action)) {
            editar(req, resp);
        } else if ("eliminar".equals(action)) {
            eliminar(req, resp);
        } else {
            resp.sendRedirect("asistencia.html");
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html; charset=UTF-8");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM asistencia");

            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("    <meta charset='UTF-8'>");
            out.println("    <meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("    <title>Lista de Asistencias</title>");
            out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("    <link rel='stylesheet' href='css/styles.css'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");
            imprimirNavbar(out);
            out.println("<div class='container mt-5'>");
            out.println("    <h2 class='mb-4 text-center'>Lista de Asistencias</h2>");
            out.println("    <div class='table-responsive'>");
            out.println("        <table class='table table-striped table-hover table-bordered'>");
            out.println("            <thead class='table-dark'>");
            out.println("                <tr>");
            out.println("                    <th>ID</th>");
            out.println("                    <th>Fecha</th>");
            out.println("                    <th>Estudiante</th>");
            out.println("                    <th>Curso</th>");
            out.println("                    <th>Estado</th>");
            out.println("                    <th>Acciones</th>");
            out.println("                </tr>");
            out.println("            </thead>");
            out.println("            <tbody>");

            while (rs.next()) {
                int id = rs.getInt("id_asistencia");
                String fecha = rs.getString("fecha");
                String estudiante = rs.getString("estudiante");
                String curso = rs.getString("curso");
                String estado = rs.getString("estado");
                out.println("                <tr>");
                out.println("                    <td>" + id + "</td>");
                out.println("                    <td>" + fecha + "</td>");
                out.println("                    <td>" + estudiante + "</td>");
                out.println("                    <td>" + curso + "</td>");
                out.println("                    <td>" + estado + "</td>");
                out.println("                    <td>");
                out.println("                        <div class='btn-group' role='group'>");
                out.println("                            <a href='asistencia?action=editar&id_asistencia=" + id + "' class='btn btn-warning btn-sm'>Editar</a>");
                out.println("                            <a href='asistencia?action=eliminar&id_asistencia=" + id + "' class='btn btn-danger btn-sm'>Eliminar</a>");
                out.println("                        </div>");
                out.println("                    </td>");
                out.println("                </tr>");
            }

            out.println("            </tbody>");
            out.println("        </table>");
            out.println("    </div>");
            out.println("    <div class='mt-3'>");
            out.println("        <a href='asistencia.html' class='btn btn-primary'>Registrar nueva asistencia</a>");
            out.println("    </div>");
            out.println("</div>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id_asistencia");
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html; charset=UTF-8");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM asistencia WHERE id_asistencia = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                out.println("<!DOCTYPE html>");
                out.println("<html lang='es'>");
                out.println("<head>");
                out.println("    <meta charset='UTF-8'>");
                out.println("    <meta name='viewport' content='width=device-width, initial-scale=1'>");
                out.println("    <title>Editar Asistencia</title>");
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
                out.println("                    <h2 class='mb-0'>Editar Asistencia</h2>");
                out.println("                </div>");
                out.println("                <div class='card-body'>");
                out.println("                    <form action='asistencia' method='post'>");
                out.println("                        <input type='hidden' name='accion' value='actualizar'>");
                out.println("                        <input type='hidden' name='id_asistencia' value='" + rs.getString("id_asistencia") + "'>");

                out.println("                        <div class='mb-3'>");
                out.println("                            <label for='fecha' class='form-label'>Fecha:</label>");
                out.println("                            <input type='date' class='form-control' id='fecha' name='fecha' value='" + rs.getString("fecha") + "' required>");
                out.println("                        </div>");

                out.println("                        <div class='mb-3'>");
                out.println("                            <label for='estudiante' class='form-label'>Estudiante:</label>");
                out.println("                            <input type='text' class='form-control' id='estudiante' name='estudiante' value='" + rs.getString("estudiante") + "' required>");
                out.println("                        </div>");

                out.println("                        <div class='mb-3'>");
                out.println("                            <label for='curso' class='form-label'>Curso:</label>");
                out.println("                            <input type='text' class='form-control' id='curso' name='curso' value='" + rs.getString("curso") + "' required>");
                out.println("                        </div>");

                out.println("                        <div class='mb-3'>");
                out.println("                            <label for='estado' class='form-label'>Estado:</label>");
                out.println("                            <input type='text' class='form-control' id='estado' name='estado' value='" + rs.getString("estado") + "' required>");
                out.println("                        </div>");

                out.println("                        <div class='mb-3'>");
                out.println("                            <label for='observacion' class='form-label'>Observación:</label>");
                out.println("                            <input type='text' class='form-control' id='observacion' name='observacion' value='" + rs.getString("observacion") + "'>");
                out.println("                        </div>");

                out.println("                        <div class='d-grid gap-2'>");
                out.println("                            <button type='submit' class='btn btn-warning'>Actualizar</button>");
                out.println("                        </div>");
                out.println("                    </form>");
                out.println("                </div>");
                out.println("                <div class='card-footer'>");
                out.println("                    <a href='asistencia?action=listar' class='btn btn-secondary'>Volver a la lista</a>");
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
            out.println("    <div class='alert alert-danger'>Error al cargar asistencia: " + e.getMessage() + "</div>");
            out.println("    <a href='asistencia?action=listar' class='btn btn-secondary'>Volver a la lista</a>");
            out.println("</div>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id_asistencia");
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html; charset=UTF-8");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM asistencia WHERE id_asistencia = ?");
            stmt.setString(1, id);
            int resultado = stmt.executeUpdate();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("    <meta charset='UTF-8'>");
            out.println("    <meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("    <title>Eliminar Asistencia</title>");
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
                out.println("                <div class='alert alert-success'>Asistencia eliminada correctamente.</div>");
            } else {
                out.println("                <div class='alert alert-danger'>Error al eliminar la asistencia.</div>");
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
            out.println("    <div class='alert alert-danger'>Error al eliminar asistencia: " + e.getMessage() + "</div>");
            out.println("    <a href='asistencia?action=listar' class='btn btn-secondary'>Volver a la lista</a>");
            out.println("</div>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        String id_asistencia = req.getParameter("id_asistencia");
        String fecha = req.getParameter("fecha");
        String estudiante = req.getParameter("estudiante");
        String curso = req.getParameter("curso");
        String estado = req.getParameter("estado");
        String observacion = req.getParameter("observacion");

        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html; charset=UTF-8");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            boolean exitoso = false;
            if ("actualizar".equals(accion)) {
                PreparedStatement stmt = conn.prepareStatement("UPDATE asistencia SET fecha=?, estudiante=?, curso=?, estado=?, observacion=? WHERE id_asistencia=?");
                stmt.setString(1, fecha);
                stmt.setString(2, estudiante);
                stmt.setString(3, curso);
                stmt.setString(4, estado);
                stmt.setString(5, observacion);
                stmt.setString(6, id_asistencia);
                int resultado = stmt.executeUpdate();
                exitoso = resultado > 0;
            } else {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO asistencia (fecha, estudiante, curso, estado, observacion) VALUES(?,?,?,?,?)");
                stmt.setString(1, fecha);
                stmt.setString(2, estudiante);
                stmt.setString(3, curso);
                stmt.setString(4, estado);
                stmt.setString(5, observacion);
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
                out.println("                        Asistencia actualizada correctamente.");
            } else {
                out.println("                        Asistencia agregada correctamente.");
            }
            out.println("                    </div>");
            out.println("                </div>");
            out.println("                <div class='card-footer'>");
            out.println("                    <a href='asistencia?action=listar' class='btn btn-primary'>Ver lista de asistencias</a>");
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
            out.println("    <a href='asistencia.html' class='btn btn-secondary'>Volver al inicio</a>");
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
        out.println("            SISTEMA ACADÉMICO");
        out.println("        </a>");
        out.println("        <button class='navbar-toggler' type='button' data-bs-toggle='collapse' data-bs-target='#navbarMain'>");
        out.println("            <span class='navbar-toggler-icon'></span>");
        out.println("        </button>");
        out.println("        <div class='collapse navbar-collapse' id='navbarMain'>");
        out.println("            <ul class='navbar-nav ms-auto'>");
        out.println("                <li class='nav-item'>");
        out.println("                    <a class='nav-link active' href='asistencia.html'>");
        out.println("                        <i class='fas fa-clipboard-check me-1'></i> ASISTENCIAS");
        out.println("                    </a>");
        out.println("                </li>");
        out.println("                <li class='nav-item'>");
        out.println("                    <a class='nav-link' href='solicitud.html'>");
        out.println("                        <i class='fas fa-user-graduate me-1'></i>SERVICIO TÉCNICO ");
        out.println("                    </a>");
        out.println("                </li>");
        out.println("                <li class='nav-item'>");
        out.println("                    <a class='nav-link' href='#'>");
        out.println("                        <i class='fas fa-book me-1'></i>Pagina3");
        out.println("                    </a>");
        out.println("                </li>");
        out.println("            </ul>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</nav>");
    }

}