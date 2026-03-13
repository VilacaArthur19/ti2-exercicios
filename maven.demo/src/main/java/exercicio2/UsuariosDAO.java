package exercicio2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuariosDAO {
    private Connection conexao;

    public boolean conectar() {
        String url = "jdbc:postgresql://localhost:5432/teste";
        try {
            conexao = DriverManager.getConnection(url, "ti2cc", "ti@cc");
            return true;
        } catch (SQLException e) {
            System.err.println("ERRO DE CONEXÃO: " + e.getMessage());
            return false;
        }
    }

    public void fechar() {
        try {
            if (conexao != null && !conexao.isClosed()) conexao.close();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public boolean inserir(Usuarios usuario) {
        String sql = "INSERT INTO usuario (codigo, login, senha, sexo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, usuario.getCodigo());
            st.setString(2, usuario.getLogin());
            st.setString(3, usuario.getSenha());
            st.setString(4, String.valueOf(usuario.getSexo()));
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir: " + e.getMessage());
            return false;
        }
    }

    public List<Usuarios> get() {
        String sql = "SELECT * FROM usuario ORDER BY codigo";
        List<Usuarios> lista = new ArrayList<>();
        try (Statement st = conexao.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar: " + e.getMessage());
        }
        return lista;
    }

    public Optional<Usuarios> buscarPorCodigo(int codigo) {
        String sql = "SELECT * FROM usuario WHERE codigo = ?";
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, codigo);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar: " + e.getMessage());
        }
        return Optional.empty();
    }

    public boolean excluir(int codigo) {
        String sql = "DELETE FROM usuario WHERE codigo = ?";
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setInt(1, codigo);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(Usuarios usuario) {
        String sql = "UPDATE usuario SET login = ?, senha = ?, sexo = ? WHERE codigo = ?";
        try (PreparedStatement st = conexao.prepareStatement(sql)) {
            st.setString(1, usuario.getLogin());
            st.setString(2, usuario.getSenha());
            st.setString(3, String.valueOf(usuario.getSexo()));
            st.setInt(4, usuario.getCodigo());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar: " + e.getMessage());
            return false;
        }
    }


    private Usuarios mapear(ResultSet rs) throws SQLException {
        return new Usuarios(
            rs.getInt("codigo"),
            rs.getString("login"),
            rs.getString("senha"),
            rs.getString("sexo").charAt(0)
        );
    }
}