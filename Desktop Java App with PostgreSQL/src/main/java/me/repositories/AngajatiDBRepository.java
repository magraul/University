package me.repositories;

import me.JdbcUtils;
import me.entities.Angajat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AngajatiDBRepository implements AngajatiRepository {

    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public AngajatiDBRepository(Properties properties) {
        logger.info("Initializing DBRepo with properties: {}", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public List<Angajat> findAngajatiDupaNume(String nume) {
        logger.traceEntry("getting employes with name : {}", nume);
        Connection con = jdbcUtils.getConnection();
        List<Angajat> angajati = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.angajati where name=?");
            stmt.setString(1, nume);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Angajat a = new Angajat(rs.getString("name"), rs.getString("phone_number"), rs.getString("address"), rs.getString("username"), rs.getString("password"));
                a.setId(rs.getInt("id_angajat"));
                angajati.add(a);
            }
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);

        }
        return angajati;
    }

    @Override
    public Angajat save(Angajat elem) {
        logger.traceEntry("saving {} employee", elem.getName());
        Connection con = jdbcUtils.getConnection();


        try {
            if (elem.getId() != null) {
                PreparedStatement st = con.prepareStatement("select * from entitati.public.angajati where id_angajat = ?");
                st.setInt(1, elem.getId());
                ResultSet r = st.executeQuery();
                if (r.next())
                    return elem;
            }

            PreparedStatement stmt = con.prepareStatement("insert into entitati.public.angajati(name, phone_number, address, username, password) values (?,?,?,?,?)");
            stmt.setString(1, elem.getName());
            stmt.setString(2, elem.getPhoneNumber());
            stmt.setString(3, elem.getAddress());
            stmt.setString(4, elem.getUsername());
            stmt.setString(5, elem.getPassword());
            stmt.executeUpdate();
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
            return elem;
        }
        return null;
    }

    @Override
    public Angajat update(Angajat elem) {
        logger.traceEntry("updating {} employee", elem.getName());
        Connection con = jdbcUtils.getConnection();

        try {
            PreparedStatement st = con.prepareStatement("select * from entitati.public.angajati where id_angajat = ?");
            st.setInt(1, elem.getId());
            ResultSet r = st.executeQuery();
            if (!r.next())
                return elem;
            PreparedStatement stmt = con.prepareStatement("update entitati.public.angajati set name=?, phone_number=?,address=?,username=?,password=? where id_angajat = ?");
            stmt.setString(1, elem.getName());
            stmt.setString(2, elem.getPhoneNumber());
            stmt.setString(3, elem.getAddress());
            stmt.setString(4, elem.getUsername());
            stmt.setString(5, elem.getPassword());
            stmt.setInt(6, elem.getId());
            stmt.executeUpdate();
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return null;
    }

    @Override
    public Angajat delete(Integer elem) throws IllegalArgumentException {
        if (elem == null)
            throw new IllegalArgumentException();

        logger.traceEntry("deleting Employee with id {}", elem);
        Connection con = jdbcUtils.getConnection();
        Angajat a = null;
        try {
            PreparedStatement st = con.prepareStatement("select * from entitati.public.angajati where id_angajat = ?");
            st.setInt(1, elem);
            ResultSet r = st.executeQuery();
            if (!r.next()) return null;

            a = new Angajat(r.getString("name"), r.getString("phone_number"), r.getString("address"), r.getString("username"), r.getString("password"));
            a.setId(elem);

            PreparedStatement stmt = con.prepareStatement("delete from entitati.public.angajati where id_angajat=?");
            stmt.setInt(1, elem);
            stmt.executeUpdate();
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }

        return a;
    }

    @Override
    public Angajat get(Integer integer) {
        logger.traceEntry("gitting employee {}", integer);
        Connection con = jdbcUtils.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.angajati where id_angajat=?");
            stmt.setInt(1, integer);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Angajat a = new Angajat(rs.getString("name"), rs.getString("phone_number"), rs.getString("address"), rs.getString("username"), rs.getString("password"));
                a.setId(rs.getInt("id_angajat"));
                return a;
            }
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }

        return null;
    }

    @Override
    public int size() {
        logger.traceEntry("getting size");
        Connection con = jdbcUtils.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement("select count(*) from entitati.public.angajati ");
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return rs.getInt(1);
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }

        return 0;
    }

    @Override
    public Iterable<Angajat> findAll() {
        logger.traceEntry("getting employes ");
        Connection con = jdbcUtils.getConnection();
        List<Angajat> angajati = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.angajati");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Angajat a = new Angajat(rs.getString("name"), rs.getString("phone_number"), rs.getString("address"), rs.getString("username"), rs.getString("password"));
                a.setId(rs.getInt("id_angajat"));
                angajati.add(a);
            }
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return angajati;
    }
}
