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
import java.util.Collections;
import java.util.List;

public class DBMusicDao extends MusicDao {
    
    private static final String SELECT_TEMPLATE = "select mid, mname, duration, martists, type from t_mups";
    
    ConnectionPool pool = ConnectionPool.getConnectionPool();
    
    public DBMusicDao() {}
    
    @Override
    protected List<Music> selectById(Music music) {
        return DBSelect(music, (m, connection) -> {
            PreparedStatement ps = connection.prepareStatement(SELECT_TEMPLATE + " where mid = ?");
            ps.setInt(1, music.getMid());
            return ps;
        });
    }
    
    @Override
    protected List<Music> selectByName(Music music) {
        return DBSelect(music, (m, connection) -> {
            PreparedStatement ps = connection.prepareStatement(SELECT_TEMPLATE + " where mname like ?");
            ps.setString(1, "%"+music.getName()+"%");
            return ps;
        });
    }
    
    @Override
    protected List<Music> selectByArtist(Music music) {
        return DBSelect(music, (m, connection) -> {
            PreparedStatement ps = connection.prepareStatement(SELECT_TEMPLATE + " where martists like ?");
            ps.setString(1, "%"+String.join("%", music.getArtists())+"%");
            return ps;
        });
    }
    
    @Override
    protected List<Music> selectByType(Music music) {
        return DBSelect(music, (m, connection) -> {
            PreparedStatement ps = connection.prepareStatement(SELECT_TEMPLATE + " where type=?");
            ps.setString(1, music.getType());
            return ps;
        });
    }
    
    @Override
    protected List<Music> selectAll() {
        return DBSelect(null, (m, connection) ->
                connection.prepareStatement(SELECT_TEMPLATE));
    }
    
    /**
     * 接收一个 lambda 用于指定 sql 语句
     * @param music 传递给 lambda 的 music 对象
     * @param sqlBuilder lambda
     * @return
     */
    private List<Music> DBSelect(Music music, SqlStatementBuilder<Music> sqlBuilder){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            connection = this.pool.getConnection();
            ps = sqlBuilder.get(music, connection);
            
            if (ps != null) {
                List<Music> list = new ArrayList<>();
                rs = ps.executeQuery();
                while (rs.next()) {
                    Music m = new Music();
                    m.setDuration(rs.getInt("duration"));
                    m.setMid(rs.getInt("mid"));
                    m.setName(rs.getString("mname"));
                    m.setArtists(rs.getString("martists").split("/"));
                    m.setType(rs.getString("type"));
                    list.add(m);
                }
                return list;
            }
            
        } catch (SQLException e) {
            AppContextListener.getServletContext().log("while accessing db: ", e);
        } finally {
            this.pool.close(connection, ps, rs);
        }
        
        return Collections.emptyList();
    }
    
    @Override
    public int insert(Music music) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = this.pool.getConnection();
            ps = con.prepareStatement("insert into t_mups(mname, duration, martists, type) values(?, ?, ?, ?)");
            
            ps.setString(1, music.getName());
            ps.setInt(2, music.getDuration());
            ps.setString(3, String.join("/", music.getArtists()));
            ps.setString(4, music.getType());
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
    
    @FunctionalInterface
    private interface SqlStatementBuilder<T> {
        /**
         * build a PreparedStatement
         * @param t argument
         * @throws SQLException
         */
        PreparedStatement get(T t, Connection connection) throws SQLException;
    }
}
