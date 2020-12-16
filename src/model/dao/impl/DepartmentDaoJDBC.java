/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.impl;

import com.mysql.jdbc.Statement;
import db.DB;
import db.DbException;
import db.DbIntegrityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import model.dao.DepartmentDao;


import model.entities.Department;


/**
 *
 * @author Antonio
 */
public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());
            
            int rowsAffected = st.executeUpdate();
            
            if(rowsAffected >0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    obj.setId(rs.getInt(1));
                }
                DB.closeResultSet(rs);
            }else{
                throw new DbException("Erro inesperado!");
            }

        } catch (SQLException e) {
            throw new DbException("Não foi possível adicionar um novo departamento!");
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement st = null;
        
        try{
            st = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");
            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());
            
            st.executeUpdate();
            
        }catch(SQLException e){
            throw new DbException("Não foi possível editar o departamento! "+e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        
        
        try{
            st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
            st.setInt(1, id);
            
            
            int rowsAffected = st.executeUpdate();
            if(rowsAffected == 0){
                throw new DbException("Departamento não existe");
            }
            
        }catch(SQLException e){
            throw new DbIntegrityException("Não foi possível deletar o departamento! Error: "+e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM department WHERE Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            
            if (rs.next()) {
                Department dep = instatiateDepartment(rs);
                return dep;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException("Não foi possível buscar um departamento!");
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Department> list = new ArrayList<>();
        try{
            st = conn.prepareStatement("SELECT * FROM department");
            rs = st.executeQuery();
            while(rs.next()){
                Department dp = new Department(rs.getInt("Id"), rs.getString("Name"));
                list.add(dp);
            }
            return list;
            
        }catch(SQLException e){
            throw new DbException("Algo inesperado aconteceu!");
        }
        finally{
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
        
    }

    private Department instatiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));
        return dep;
    }
}
