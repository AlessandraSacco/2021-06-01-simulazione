package it.polito.tdp.genes.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	SimpleWeightedGraph<Genes,DefaultWeightedEdge> grafo;
	GenesDao dao;
	Map<String,Genes> idGenesMap;
	
	public Model() {
	dao = new GenesDao();
	idGenesMap= new HashMap<String,Genes>();
	dao.getAllGenes(idGenesMap);
	}
	
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<Genes,DefaultWeightedEdge> (DefaultWeightedEdge.class);
		// creare i vertici del grafo 
		Graphs.addAllVertices(this.grafo, dao.getAllVertices(idGenesMap));
	   
		// creare gli archi del grafo
		for(Arco a: this.dao.getArchi(idGenesMap)) {
			if(a.getG1().getChromosome()==a.getG2().getChromosome()) {
				Graphs.addEdgeWithVertices(this.grafo, a.getG1(), a.getG2(),2*Math.abs(a.getPeso()));
			}
			else if(a.getG2().getChromosome()!=a.getG1().getChromosome()) {
				Graphs.addEdgeWithVertices(this.grafo, a.getG1(), a.getG2(), Math.abs(a.getPeso()));
			}
		}
	
	
	}
	
	public int getVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getArchi() {
		return grafo.edgeSet().size();
	}
	
	public Graph<Genes,DefaultWeightedEdge> grafo(){
		return grafo;
	}
	

	
}
