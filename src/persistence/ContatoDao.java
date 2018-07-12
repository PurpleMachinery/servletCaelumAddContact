package persistence;

import model.Contato;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ContatoDao {
    private Connection connection;

    public ContatoDao() {
        this.connection = new ConnectionFactory().getConnection();
    }

    public void insert(Contato Contato) {
        String sql = "insert into contatos" +
                "(nome, email, endereco, dataNascimento)" +
                "values (?,?,?,?)";
        try {
            PreparedStatement comando = connection.prepareStatement(sql);
            comando.setString(1, Contato.getNome());
            comando.setString(2, Contato.getEmail());
            comando.setString(3, Contato.getEndereco());
            comando.setDate(4, new Date(Contato.getDataNascimento().getTimeInMillis()));
            comando.execute();
            comando.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Contato getContatoById(Contato cc) {
        try {
            Contato contato = new Contato();
            String sql = "select * from contatos where id = ?";
            PreparedStatement comando = this.connection.prepareStatement(sql);
            comando.setLong(1, cc.getId());
            ResultSet rs = comando.executeQuery();
            rs.next();
            contato.setId(rs.getLong("id"));
            contato.setEndereco(rs.getString("endereco"));
            contato.setNome(rs.getString("nome"));
            contato.setEmail(rs.getString("email"));
            Calendar data = Calendar.getInstance();
            data.setTime(rs.getDate("dataNascimento"));
            contato.setDataNascimento(data);
            return contato;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Contato> getLista() {
        try {
            List<Contato> contatos = new ArrayList<Contato>();
            PreparedStatement stmt = this.connection.
                    prepareStatement("select *	from contatos");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                //	criando	o	objeto	Contato
                Contato contato = new Contato();
                contato.setId(rs.getLong("id"));
                contato.setNome(rs.getString("nome"));
                contato.setEmail(rs.getString("email"));
                contato.setEndereco(rs.getString("endereco"));
                //	montando	a	data	através	do	Calendar
                Calendar data = Calendar.getInstance();
                data.setTime(rs.getDate("dataNascimento"));
                contato.setDataNascimento(data);
                //	adicionando	o	objeto	à	lista
                contatos.add(contato);
            }
            rs.close();
            stmt.close();
            return contatos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}