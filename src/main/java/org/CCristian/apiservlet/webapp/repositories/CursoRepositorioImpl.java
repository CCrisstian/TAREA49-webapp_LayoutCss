package org.CCristian.apiservlet.webapp.repositories;

import org.CCristian.apiservlet.webapp.models.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoRepositorioImpl implements Repositorio<Curso> {

    private Connection conn;

    public CursoRepositorioImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Curso> listar() throws SQLException {
        List<Curso> cursos = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             // Consulta SQL con JOIN para obtener el nombre del instructor
             ResultSet rs = stmt.executeQuery("SELECT c.id, c.nombre, c.descripcion, c.duracion, i.nombre AS instructor " +
                     "FROM cursos AS c " +
                     "JOIN instructores AS i ON c.instructor_id = i.id " +
                     "ORDER BY c.id")) {
            while (rs.next()) {
                Curso curso = getCurso(rs);
                cursos.add(curso);
            }
        }
        return cursos;
    }

    @Override
    public List<Curso> porNombre(String nombre) throws SQLException {
        List<Curso> cursos = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT c.id, c.nombre, c.descripcion, c.duracion, i.nombre AS instructor " +
                "FROM cursos AS c " +
                "JOIN instructores AS i ON c.instructor_id = i.id " +
                "WHERE c.nombre LIKE ?")) {
            stmt.setString(1, "%" + nombre + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cursos.add(getCurso(rs));
                }
            }
        }
        return cursos;
    }

    @Override
    public void guardar(Curso curso) throws SQLException {
        String sql;
        int instructorId = obtenerInstructorIdPorNombre(curso.getInstructor()); // Obtenemos el instructor_id

        if (curso.getId() > 0) {
            // Si el curso ya existe, actualizamos
            sql = "UPDATE cursos SET nombre=?, descripcion=?, instructor_id=?, duracion=? WHERE id=?";
        } else {
            // Si el curso es nuevo, lo insertamos
            sql = "INSERT INTO cursos (nombre, descripcion, instructor_id, duracion) VALUES(?,?,?,?)";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, curso.getNombre());
            stmt.setString(2, curso.getDescripcion());
            stmt.setInt(3, instructorId); // Usamos el instructor_id obtenido
            stmt.setFloat(4, curso.getDuracion());

            if (curso.getId() > 0) {
                stmt.setInt(5, curso.getId());
            }
            stmt.executeUpdate();
        }
    }

    private int obtenerInstructorIdPorNombre(String instructorNombre) throws SQLException {
        String sql = "SELECT id FROM instructores WHERE nombre = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, instructorNombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Instructor no encontrado: " + instructorNombre);
                }
            }
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM cursos WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Curso porId(int id) throws SQLException {
        Curso curso = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT c.id, c.nombre, c.descripcion, c.duracion, i.nombre AS instructor " +
                "FROM cursos AS c " +
                "JOIN instructores AS i ON c.instructor_id = i.id " +
                "WHERE c.id=?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    curso = getCurso(rs);
                }
            }
        }
        return curso;
    }

    private static Curso getCurso(ResultSet rs) throws SQLException {
        Curso c = new Curso();
        c.setId(rs.getInt("id"));
        c.setNombre(rs.getString("nombre"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setInstructor(rs.getString("instructor")); // Ahora obtenemos el nombre del instructor
        c.setDuracion(rs.getFloat("duracion"));
        return c;
    }
}
