package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Arco;
import it.polito.tdp.genes.model.Genes;


public class GenesDao {
	
	public void getAllGenes(Map<String, Genes> idGenesMap){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes ";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
               if(!idGenesMap.containsKey(res.getString("GeneID"))) {
				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				
				idGenesMap.put(genes.getGeneId(), genes);
				
			}
			}
			res.close();
			st.close();
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		
		}
	}
	public List<Genes> getAllVertices(Map<String, Genes> idGenesMap){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome "
				+ "FROM genes, interactions "
				+ "WHERE genes.GeneID=interactions.GeneID1 "
				+ "AND genes.GeneID NOT IN (SELECT DISTINCT GeneID "
				+ "                FROM genes, interactions "
				+ "					 WHERE genes.GeneID=interactions.GeneID2) ";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
               if(idGenesMap.containsKey(res.getString("GeneID"))) {
				Genes genes = idGenesMap.get(res.getString("GeneID"));
				
				result.add(genes);
			}
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
		    return null;
		}
	}

	public List<Arco> getArchi(Map<String, Genes> idGenesMap) {
		String sql = "SELECT g1.GeneID, g2.GeneID, i.Expression_Corr AS peso "
				+ "FROM genes g1, genes g2, interactions i "
				+ "WHERE g1.GeneID=i.GeneID1 "
				+ "AND g2.GeneID=i.GeneID2 "
				+ "AND g1.GeneID>g2.GeneID "
				+ "GROUP BY g1.GeneID, g2.GeneID ";
		List<Arco> result = new ArrayList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
               if(idGenesMap.containsKey(res.getString("g1.GeneID")) && idGenesMap.containsKey(res.getString("g2.GeneID"))) {
				Genes genes1= idGenesMap.get(res.getString("g1.GeneID"));
				Genes genes2= idGenesMap.get(res.getString("g2.GeneID"));
				Arco arco = new Arco(genes1,genes2,res.getDouble("peso"));
				result.add(arco);
			}
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
		    return null;
		}
	}
	
	
	


	
}
