package me;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Properties;


public class DBRepo {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public DBRepo(Properties porps){
        logger.info("Initializing DBRepo with properties: {}",porps);
        dbUtils = new JdbcUtils(porps);
    }

    public Student getStudent(Integer id) {
        logger.traceEntry("reading student: {}", id);
        Connection con = dbUtils.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement("select  * from entitati.public.studenti where id = " + id);
            ResultSet Rs = stmt.executeQuery();
            if(Rs.next())
                return new Student(Rs.getInt(1),Rs.getString(2),Rs.getString(3),Rs.getDouble(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.traceExit();
        return null;
    }

    public void save(Student s){
        logger.traceEntry("saving student {} ",s);
        Connection con = dbUtils.getConnection();
        try {
            Statement stmt = con.createStatement();
            String sql = "insert into entitati.public.studenti(id, nume, prenume, media)  values (" + s.getId() +", '" + s.getNume() + "' ,'" + s.getPrenume() + "',5" + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void delete(Integer idS){
        Connection con = dbUtils.getConnection();
        try {
            Statement stmt = con.createStatement();
            String sql = "delete from entitati.public.studenti where id = " + idS + "; ";
            stmt.executeUpdate(sql);
            stmt.close();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Student s) {
        Connection con = dbUtils.getConnection();
        try {
            Statement stmt = con.createStatement();
            String sql = " update entitati.public.studenti" +
                    " set nume = '"+ s.getNume()+"', prenume = '" + s.getPrenume() +"'" +
                    "where id = " + s.getId()+ ";";
            String sqll = "update entitati.public.studenti set nume = '" + s.getNume() + "', prenume = '" + s.getPrenume() + "', media = " + s.getMedia().toString() + " where id = " + s.getId().toString() + ";";
            stmt.executeUpdate(sqll);
            stmt.close();
            con.commit();
        } catch (SQLException e) {
        }
    }
}
