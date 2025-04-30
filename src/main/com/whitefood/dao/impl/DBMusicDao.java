package com.whitefood.dao.impl;

import com.whitefood.bean.Music;
import com.whitefood.dao.MusicDao;
import com.whitefood.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBMusicDao implements MusicDao {
    
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
                ps = connection.prepareStatement("select mid, mname, duration from t_mups");
            } else if (music.getMid() > 0){
                ps = connection.prepareStatement("select mid, mname, duration from t_mups where mid = ?");
                ps.setInt(1, music.getMid());
            } else if (music.getName() != null){
                ps = connection.prepareStatement("select mid, mname, duration from t_mups where mname like ?");
                ps.setString(1, "%"+music.getName()+"%");
            }
            
            if (ps != null) {
                List<Music> list = new ArrayList<>();
                rs = ps.executeQuery();
                while (rs.next()) {
                    Music m = new Music();
                    m.setDuration(rs.getInt("duration"));
                    m.setMid(rs.getInt("mid"));
                    m.setName(rs.getString("mname"));
                    list.add(m);
                }
                return list;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
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
            ps = con.prepareStatement("insert into t_mups(mname, duration) values(?, ?)");
            
            ps.setString(1, music.getName());
            ps.setInt(2, music.getDuration());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        } finally {
            this.pool.close(con, ps, null);
        }
        
        return rowAffected != 0;
    }
}
