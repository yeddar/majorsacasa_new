package es.uji.ei1027.majorsacasa.dao;

import es.uji.ei1027.majorsacasa.model.Voluntario;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VoluntarioRowMapper implements RowMapper <Voluntario> {
    @Override
    public Voluntario mapRow(ResultSet resultSet, int i) throws SQLException {
        Voluntario v = new Voluntario();
        v.setNick(resultSet.getString("nick"));
        v.setNombre(resultSet.getString("nombre"));
        v.setEdad(resultSet.getInt("edad"));
        v.setTlf(resultSet.getString("tlf"));
        v.setCorreo(resultSet.getString("correo"));
        v.setEsActivo(resultSet.getBoolean("es_activo"));
        return v;
    }
}
