package me.repositories;

import me.JdbcUtils;
import me.entities.Angajat;
import me.entities.CazCaritabil;
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

public class CazuriDBRepository implements CazuriRepository {

    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public CazuriDBRepository(Properties properties) {
        logger.info("Initializing with properties: {}", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public List<CazCaritabil> findCazuriDupaDescriere(String descriere) {
        logger.traceEntry("find cazuri with description {}", descriere);
        Connection con = jdbcUtils.getConnection();
        List<CazCaritabil> cazuri = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.cazuri where descriere=?");
            stmt.setString(1, descriere);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CazCaritabil c = new CazCaritabil(rs.getString("descriere"));
                c.setId(rs.getInt("id_caz"));
                cazuri.add(c);
            }
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return cazuri;
    }

    @Override
    public CazCaritabil save(CazCaritabil elem) {
        logger.traceEntry("saving {} caz", elem.getDescription());
        Connection con = jdbcUtils.getConnection();
        try {
            if (elem.getId() != null) {
                PreparedStatement st = con.prepareStatement("select * from entitati.public.cazuri where id_caz=?");
                st.setInt(1, elem.getId());
                ResultSet r = st.executeQuery();
                if (r.next())
                    return elem;
            }

            PreparedStatement stmt = con.prepareStatement("insert into entitati.public.cazuri(descriere) values (?)");
            stmt.setString(1, elem.getDescription());
            stmt.executeUpdate();
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
            return elem;
        }
        return null;
    }

    @Override
    public CazCaritabil update(CazCaritabil elem) {
        logger.traceEntry("updating {} caz", elem.getDescription());
        Connection con = jdbcUtils.getConnection();
        if (elem.getId() == null)
            return elem;

        try {
            PreparedStatement st = con.prepareStatement("select * from entitati.public.cazuri where id_caz = ?");
            st.setInt(1, elem.getId());
            ResultSet r = st.executeQuery();
            if (!r.next())
                return elem;
            PreparedStatement stmt = con.prepareStatement("update entitati.public.cazuri set descriere=? where id_caz=?");
            stmt.setString(1, elem.getDescription());
            stmt.setInt(2, elem.getId());
            stmt.executeUpdate();
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return null;
    }


    @Override
    public CazCaritabil delete(Integer elem) throws IllegalArgumentException {
        if (elem == null)
            throw new IllegalArgumentException();

        logger.traceEntry("deleting caz with id {}", elem);
        Connection con = jdbcUtils.getConnection();
        CazCaritabil a = null;
        try {
            PreparedStatement st = con.prepareStatement("select * from entitati.public.cazuri where id_caz=?");
            st.setInt(1, elem);
            ResultSet r = st.executeQuery();
            if (!r.next()) return null;


            a = new CazCaritabil(r.getString("descriere"));
            a.setId(elem);

            PreparedStatement stmt = con.prepareStatement("delete from entitati.public.cazuri where id_caz=?");
            stmt.setInt(1, elem);
            stmt.executeUpdate();
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }

        return a;
    }

    @Override
    public CazCaritabil get(Integer integer) {
        logger.traceEntry("getting caz {}", integer);
        Connection con = jdbcUtils.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.cazuri where id_caz=?");
            stmt.setInt(1, integer);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                CazCaritabil a = new CazCaritabil(rs.getString("descriere"));
                a.setId(rs.getInt("id_caz"));
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
            PreparedStatement stmt = con.prepareStatement("select count(*) from entitati.public.cazuri");
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
    public Iterable<CazCaritabil> findAll() {
        logger.traceEntry("getting cazuri ");
        Connection con = jdbcUtils.getConnection();
        List<CazCaritabil> cazuri = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.cazuri");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CazCaritabil c = new CazCaritabil(rs.getString("descriere"));
                c.setId(rs.getInt("id_caz"));
                cazuri.add(c);
            }
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return cazuri;

    }
}
