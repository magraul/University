package me.repositories;

import me.JdbcUtils;
import me.entities.Angajat;
import me.entities.CazCaritabil;
import me.entities.Donatie;
import me.entities.Donator;
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

public class DonatiiDBRepository implements DonatiiRepository {

    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public DonatiiDBRepository(Properties properties) {
        logger.info("initializing DBRepo {}", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public List<Donatie> findDonatiiDupaSuma(Float suma) {
        logger.traceEntry("getting donations with sum : {}", suma);
        Connection con = jdbcUtils.getConnection();
        List<Donatie> donatii = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.donatii where suma=?");
            stmt.setFloat(1, suma);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Donatie a = new Donatie(rs.getFloat("suma"), rs.getInt("id_donator"), rs.getInt("id_caz"));
                a.setId(rs.getInt("id_donatie"));
                donatii.add(a);
            }
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return donatii;
    }

    @Override
    public List<Donatie> findDonatiiDupaCaz(CazCaritabil cazCaritabil) {
        logger.traceEntry("getting donations with  : {}", cazCaritabil.getDescription());
        Connection con = jdbcUtils.getConnection();
        List<Donatie> donatii = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.donatii where id_caz=?");
            stmt.setInt(1, cazCaritabil.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Donatie a = new Donatie(rs.getFloat("suma"), rs.getInt("id_donator"), rs.getInt("id_caz"));
                a.setId(rs.getInt("id_donatie"));
                donatii.add(a);
            }
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return donatii;
    }

    @Override
    public List<Donatie> findDonatiiDupaDonator(Donator donator) {
        logger.traceEntry("getting donations with sum : {}", donator.getName());
        Connection con = jdbcUtils.getConnection();
        List<Donatie> donatii = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.donatii where id_donator=?");
            stmt.setInt(1, donator.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Donatie a = new Donatie(rs.getFloat("suma"), rs.getInt("id_donator"), rs.getInt("id_caz"));
                a.setId(rs.getInt("id_donatie"));
                donatii.add(a);
            }
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return donatii;
    }

    @Override
    public Donatie save(Donatie elem) {
        logger.traceEntry("saving {} donation", elem);
        Connection con = jdbcUtils.getConnection();


        try {
            if (elem.getId() != null) {
                PreparedStatement st = con.prepareStatement("select * from entitati.public.donatii where id_donatie = ?");
                st.setInt(1, elem.getId());
                ResultSet r = st.executeQuery();
                if (r.next())
                    return elem;
            }
            PreparedStatement stmt = con.prepareStatement("insert into entitati.public.donatii(id_caz, id_donator, suma) values (?,?,?)");
            stmt.setInt(1, elem.getCazCaritabil());
            stmt.setInt(2, elem.getDonator());
            stmt.setFloat(3, elem.getSumaDonata());
            stmt.executeUpdate();
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
            return elem;
        }
        return null;

    }

    @Override
    public Donatie update(Donatie elem) {
        logger.traceEntry("updating {} donation", elem);
        Connection con = jdbcUtils.getConnection();
        if (elem.getId() == null)
            return elem;
        try {
            PreparedStatement st = con.prepareStatement("select * from entitati.public.donatii where id_donatie = ?");
            st.setInt(1, elem.getId());
            ResultSet r = st.executeQuery();
            if (!r.next())
                return elem;
            PreparedStatement stmt = con.prepareStatement("update entitati.public.donatii set id_caz=?,id_donator=?,suma=? where id_donatie=?");
            stmt.setInt(1, elem.getCazCaritabil());
            stmt.setInt(2, elem.getDonator());
            stmt.setFloat(3, elem.getSumaDonata());
            stmt.setInt(4, elem.getId());
            stmt.executeUpdate();
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return null;
    }

    @Override
    public Donatie delete(Integer elem) throws IllegalArgumentException {
        if (elem == null)
            throw new IllegalArgumentException();

        logger.traceEntry("deleting donation with id {}", elem);
        Connection con = jdbcUtils.getConnection();
        Donatie a = null;
        try {
            PreparedStatement st = con.prepareStatement("select * from entitati.public.donatii where id_donatie = ?");
            st.setInt(1, elem);
            ResultSet r = st.executeQuery();
            if (!r.next()) return null;

            a = new Donatie(r.getFloat("suma"), r.getInt("id_donator"), r.getInt("id_caz"));
            a.setId(elem);

            PreparedStatement stmt = con.prepareStatement("delete from entitati.public.donatii where id_donatie=?");
            stmt.setInt(1, elem);
            stmt.executeUpdate();
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }

        return a;
    }

    @Override
    public Donatie get(Integer integer) {
        logger.traceEntry("getting donation{}", integer);
        Connection con = jdbcUtils.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.donatii where id_donatie=?");
            stmt.setInt(1, integer);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Donatie a = new Donatie(rs.getFloat("suma"), rs.getInt("id_donator"), rs.getInt("id_caz"));
                a.setId(rs.getInt("id_donatie"));
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
            PreparedStatement stmt = con.prepareStatement("select count(*) from entitati.public.donatii ");
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
    public Iterable<Donatie> findAll() {
        logger.traceEntry("getting donations ");
        Connection con = jdbcUtils.getConnection();
        List<Donatie> donatii = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.donatii");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Donatie d = new Donatie(rs.getFloat("suma"), rs.getInt("id_donator"), rs.getInt("id_caz"));
                d.setId(rs.getInt("id_donatie"));
                donatii.add(d);
            }
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return donatii;
    }
}
