/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;


import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import jdbc.Conexao;
import model.Cliente;
import model.Vendas;


/**
 *
 * @author
 */
public class VendasDAO {

    private Connection con;

    public VendasDAO() {
       this.con = Conexao.conectar();
    }
    
    //Cadastrar Venda
    
     public void cadastrarVenda(Vendas obj) {
            
         try {
              String sql = "insert into tb_vendas (cliente_id, data_venda, total_venda,observacoes)  values (?,?,?,?)";
                 
               //2 passo - conectar o banco de dados e organizar o comando sql
              
                PreparedStatement stmt = con.prepareStatement(sql);
                
                stmt.setString(1, obj.getCliente().getId);
                stmt.setString(2, obj.getData_venda());
                stmt.setDouble(3, obj.getTotal_venda());
                stmt.setString(4, obj.getObs());
                
                stmt.execute();
                stmt.close();
         } catch (Exception e) {
         
            JOptionPane.showMessageDialog(null, "Erro: " + e);

        }

    }
     
      //Retorna a ultima venda
     
     public int retornaUltimaVenda() throws SQLException{
         try{
             
             int idvenda = 0;
             
             String sql = "Selec max(id) id from tb_vendas ";
             
             PreparedStatement ps = con.prepareStatement(sql);
             
             ResultSet rs = ps.executeQuery();
             
             if(rs.next()){
                 
                 Vendas p = new Vendas();
                 
                 p.setId(rs.getInt("id"));
                 idvenda = p.getId();
             }
             
             return idvenda;
             
         }catch(SQLException e){
             
             throw new RuntimeException(e);
         }
         
     }
     
      //Metodo que filtra Vendas por Datas
     
     public List <Vendas> listarVendasPorPeriodo(LocalDate data_inicio, LocalDate data_fim) {
         try{
             // 1 passo - uma lista
             List<Vendas> lista = new ArrayList<>();
             // 2 passo - criar o sql 
             String sql = "select v.id, date_format(v.data_venda, '%d/%m/%Y') as data_formatada, c.nome, v.total_venda, v.observacoes from tb_vendas as v" 
                    + "inner join tb_clientes as c on (v.cliente_id = c.id) where v.data_venda BETWEEN ? AND ?";
             
             PreparedStatement stmt = con.prepareStatement(sql);
             
             stmt.setString(1, data_inicio.toString());
             stmt.setString(2, data_fim.toString());
             
             ResultSet rs = stmt.executeQuery();
             
             while (rs.next()) {
                 
                 Vendas obj = new Vendas();
                 Cliente c = new Cliente();
                 
                 obj.setId(rs.getInt("v.id"));
                 obj.setData_venda(rs.getString("data_formatada"));
                 c.setNome(rs.getString("c.nome"));
                 obj.setTotal_venda(rs.getDouble("v.total_venda"));
                 obj.setObs(rs.getString("v.observacoes"));
                 
                 obj.setCliente(c);
                 lista.add(obj);
                 
                 
             }
             return lista;
             
         }catch (SQLException erro) {
         
            JOptionPane.showMessageDialog(null, "Erro: " + erro);
            return null;
        }
        
}
     
         
     
     public void excluirVenda(Vendas obj) {
        try {

            //1 passo  - criar o comando sql
            String sql = "delete from tb_vendas where id = ?";

            //2 passo - conectar o banco de dados e organizar o comando sql
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, obj.getId());

            //3 passo - executar o comando sql
            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Excluido com Sucesso!");

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro: " + erro);

        }

    }
     
}

