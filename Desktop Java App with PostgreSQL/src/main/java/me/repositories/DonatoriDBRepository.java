package me.repositories;

import me.JdbcUtils;
import me.entities.Angajat;
import me.entities.Donator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.util.resources.ext.CalendarData_da;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DonatoriDBRepository implements DonatoriRepository {

    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public DonatoriDBRepository(Properties properties) {
        logger.info("Initializing DbRepo with properties: {}", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public List<Donator> findDonatoriDupaNume(String nume) {
        logger.traceEntry("getting donators with name : {}", nume);
        Connection con = jdbcUtils.getConnection();
        List<Donator> donatri = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.donatori where name=?");
            stmt.setString(1, nume);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Donator a = new Donator(rs.getString("name"), rs.getString("phone_number"), rs.getString("address"));
                a.setId(rs.getInt("id_donator"));
                donatri.add(a);
            }
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return donatri;

    }

    @Override
    public List<Donator> findDonatoriDupaAdresa(String adresa) {
        logger.traceEntry("getting employes with address : {}", adresa);
        Connection con = jdbcUtils.getConnection();
        List<Donator> donatri = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.donatori where address=?");
            stmt.setString(1, adresa);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Donator a = new Donator(rs.getString("name"), rs.getString("phone_number"), rs.getString("address"));
                a.setId(rs.getInt("id_donator"));
                donatri.add(a);
            }
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return donatri;

    }

    @Override
    public Donator save(Donator elem) {
        logger.traceEntry("saving {} donator", elem.getName());
        Connection con = jdbcUtils.getConnection();

        try {
            if (elem.getId() != null) {
                PreparedStatement st = con.prepareStatement("select * from entitati.public.donatori where id_donator = ?");
                st.setInt(1, elem.getId());
                ResultSet r = st.executeQuery();
                if (r.next())
                    return elem;
            }

            PreparedStatement stmt = con.prepareStatement("insert into entitati.public.donatori(name, phone_number, address) values (?,?,?)");
            stmt.setString(1, elem.getName());
            stmt.setString(2, elem.getPhoneNumber());
            stmt.setString(3, elem.getAddress());
            stmt.executeUpdate();
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
            return elem;
        }
        return null;
    }

    @Override
    public Donator update(Donator elem) {
        logger.traceEntry("updating {} donator", elem.getName());
        Connection con = jdbcUtils.getConnection();
        if (elem.getId() == null)
            return elem;
        try {
            PreparedStatement st = con.prepareStatement("select * from entitati.public.donatori where id_donator = ?");
            st.setInt(1, elem.getId());
            ResultSet r = st.executeQuery();
            if (!r.next())
                return elem;
            PreparedStatement stmt = con.prepareStatement("update entitati.public.donatori set name=?, phone_number=?,address=? where id_donator = ?");
            stmt.setString(1, elem.getName());
            stmt.setString(2, elem.getPhoneNumber());
            stmt.setString(3, elem.getAddress());
            stmt.setInt(4, elem.getId());
            stmt.executeUpdate();
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return null;


    }

    @Override
    public Donator delete(Integer elem) throws IllegalArgumentException {
        if (elem == null)
            throw new IllegalArgumentException();

        logger.traceEntry("deleting donator with id {}", elem);
        Connection con = jdbcUtils.getConnection();
        Donator a = null;
        try {
            PreparedStatement st = con.prepareStatement("select * from entitati.public.donatori where id_donator = ?");
            st.setInt(1, elem);
            ResultSet r = st.executeQuery();
            if (!r.next()) return null;

            a = new Donator(r.getString("name"), r.getString("phone_number"), r.getString("address"));
            a.setId(elem);

            PreparedStatement stmt = con.prepareStatement("delete from entitati.public.donatori where id_donator=?");
            stmt.setInt(1, elem);
            stmt.executeUpdate();
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }

        return a;
    }

    @Override
    public Donator get(Integer integer) {
        logger.traceEntry("gitting donator {}", integer);
        Connection con = jdbcUtils.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.donatori where id_donator=?");
            stmt.setInt(1, integer);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Donator a = new Donator(rs.getString("name"), rs.getString("phone_number"), rs.getString("address"));
                a.setId(rs.getInt("id_donator"));
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
            PreparedStatement stmt = con.prepareStatement("select count(*) from entitati.public.donatori ");
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
    public Iterable<Donator> findAll() {
        logger.traceEntry("getting donators");
        Connection con = jdbcUtils.getConnection();
        List<Donator> donatori = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement("select * from entitati.public.donatori");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Donator a = new Donator(rs.getString("name"), rs.getString("phone_number"), rs.getString("address"));
                a.setId(rs.getInt("id_donator"));
                donatori.add(a);
            }
            con.close();
        } catch (SQLException e) {
            logger.traceExit(e);
        }
        return donatori;
    }
}
