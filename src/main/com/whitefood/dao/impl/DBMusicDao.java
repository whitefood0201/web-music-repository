package com.whitefood.dao.impl;

import com.whitefood.bean.Music;
import com.whitefood.dao.MusicDao;
import com.whitefood.listener.AppContextListener;
import com.whitefood.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBMusicDao implements MusicDao {
    
    private static final String SELECT_TEMPLATE = "select mid, mname, duration, martists from t_mups";
    
    ConnectionPool pool = ConnectionPool.getConnectionPool();
    
    public DBMusicDao() {}
    
    @Override
    public List<Music> select(Music music) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            connection = this.pool.getConnection();
            
            if (music == null || music.isEmpty() || music.getMid() == 0){
                ps = connection.prepareStatement(SELECT_TEMPLATE);
            } else if (music.getMid() > 0){
                ps = connection.prepareStatement(SELECT_TEMPLATE + " where mid = ?");
                ps.setInt(1, music.getMid());
            } else if (music.getName() != null){
                ps = connection.prepareStatement(SELECT_TEMPLATE + " where mname like ?");
                ps.setString(1, "%"+music.getName()+"%");
            } else if (music.getArtists() != null) {
                ps = connection.prepareStatement(SELECT_TEMPLATE + " where martists like ?");
                ps.setString(1, "%"+String.join("%", music.getArtists())+"%");
            }
            
            if (ps != null) {
                List<Music> list = new ArrayList<>();
                rs = ps.executeQuery();
                while (rs.next()) {
                    Music m = new Music();
                    m.setDuration(rs.getInt("duration"));
                    m.setMid(rs.getInt("mid"));
                    m.setName(rs.getString("mname"));
                    m.setArtists(rs.getString("martists").split("/"));
                    list.add(m);
                }
                return list;
            }
            
        } catch (SQLException e) {
            AppContextListener.getServletContext().log("while accessing db: ", e);
        } finally {
            this.pool.close(connection, ps, rs);
        }
        
        return new ArrayList<>();
    }
    
    @Override
    public int insert(Music music) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = this.pool.getConnection();
            ps = con.prepareStatement("insert into t_mups(mname, duration, martists) values(?, ?, ?)");
            
            ps.setString(1, music.getName());
            ps.setInt(2, music.getDuration());
            ps.setString(3, String.join("/", music.getArtists()));
            ps.executeUpdate();
        } catch (SQLException e) {
            AppContextListener.getServletContext().log("while append data to db: ", e);
        } finally {
            this.pool.close(con, ps, null);
        }
        
        return this.getMaxMid();
    }
    
    private int getMaxMid(){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        int mid = 0;
        try {
            con = this.pool.getConnection();
            ps = con.prepareStatement("select max(mid) as max from t_mups");
            rs = ps.executeQuery();
            
            if (rs.next()) {
                mid = rs.getInt("max");
            }
        } catch (SQLException e) {
            AppContextListener.getServletContext().log("while accessing db: ", e);
        } finally {
            this.pool.close(con, ps, rs);
        }
        
        return mid;
    }
    
    @Override
    public boolean delete(Music music) {
        Connection con = null;
        PreparedStatement ps = null;
        
        int rowAffected = 0;
        try {
            con = this.pool.getConnection();
            ps = con.prepareStatement("delete from t_mups where mid = ?");
            
            ps.setInt(1, music.getMid());
            rowAffected = ps.executeUpdate();
        } catch (SQLException e) {
            AppContextListener.getServletContext().log("while dao delete: ", e);
        } finally {
            this.pool.close(con, ps, null);
        }
        
        return rowAffected != 0;
    }
}
